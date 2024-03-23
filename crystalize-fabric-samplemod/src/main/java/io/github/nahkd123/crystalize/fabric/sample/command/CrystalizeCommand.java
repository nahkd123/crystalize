package io.github.nahkd123.crystalize.fabric.sample.command;

import static net.minecraft.command.argument.IdentifierArgumentType.identifier;
import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;
import static net.minecraft.text.Text.translatable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.Dynamic2CommandExceptionType;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.SuggestionProvider;

import eu.pb4.polymer.virtualentity.api.attachment.ChunkAttachment;
import eu.pb4.polymer.virtualentity.api.attachment.HolderAttachment;
import io.github.nahkd123.crystalize.anim.AnimateMode;
import io.github.nahkd123.crystalize.anim.Animation;
import io.github.nahkd123.crystalize.anim.controller.AnimationController;
import io.github.nahkd123.crystalize.anim.controller.TemplatedAnimationController;
import io.github.nahkd123.crystalize.fabric.model.CrystalizeElementHolder;
import io.github.nahkd123.crystalize.fabric.model.ModelsManager;
import io.github.nahkd123.crystalize.fabric.model.RegisteredModel;
import io.github.nahkd123.crystalize.fabric.model.TranslateStrategy;
import io.github.nahkd123.crystalize.fabric.sample.CrystalizeSampleMod;
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.minecraft.command.argument.BlockPosArgumentType;
import net.minecraft.command.argument.IdentifierArgumentType;
import net.minecraft.command.argument.UuidArgumentType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

public class CrystalizeCommand {
	// @formatter:off
	public static final DynamicCommandExceptionType UNKNOWN_MODEL_ID = new DynamicCommandExceptionType(id -> translatable("crystalize.command.unknownModelId", id));
	public static final Dynamic2CommandExceptionType UNKNOWN_ANIMATION_ID = new Dynamic2CommandExceptionType((model, anim) -> translatable("crystalize.command.unknownAnimationId", model, anim));
	public static final DynamicCommandExceptionType NO_HOLDER_UUID = new DynamicCommandExceptionType(uuid -> translatable("crystalize.command.noHolder", uuid));
	public static final DynamicCommandExceptionType UNKNOWN_TRANSLATION_STRATEGY = new DynamicCommandExceptionType(type -> translatable("crystalize.command.unknownTranslationStrategy", type));
	// @formatter:on

	public static LiteralArgumentBuilder<ServerCommandSource> command(ModelsManager modelsManager) {
		return literal("crystalize")
			.requires(source -> source.hasPermissionLevel(3))
			.then(place(modelsManager))
			.then(addAnimation())
			.then(inspect());
	}

	// Setup attachments so we can use commands to modify our models after placed
	// with /placemodel command. This utilizes Attachment API from Fabric API.
	private static final AttachmentType<Map<UUID, CrystalizeElementHolder>> HOLDERS = AttachmentRegistry
		.createDefaulted(
			new Identifier(CrystalizeSampleMod.MODID, "holders"),
			() -> new HashMap<>());

	private static LiteralArgumentBuilder<ServerCommandSource> place(ModelsManager modelsManager) {
		return literal("place").then(argument("modelId", identifier())
			.suggests((context, builder) -> {
				modelsManager.getRegisteredModels().keySet().forEach(k -> builder.suggest(k.toString()));
				return builder.buildFuture();
			})
			.then(argument("location", BlockPosArgumentType.blockPos())
				.then(argument("strategy", StringArgumentType.string())
					.suggests((context, builder) -> builder
					// @formatter:off
						.suggest(TranslateStrategy.MIXED.toString(), translatable("crystalize.command.mixed"))
						.suggest(TranslateStrategy.POSITION_ONLY.toString(), translatable("crystalize.command.positionOnly"))
						.suggest(TranslateStrategy.TRANSLATION_ONLY.toString(), translatable("crystalize.command.translationOnly"))
						.buildFuture())
					// @formatter:on
					.executes(context -> executePlaceModel(context, modelsManager, true)))
				.executes(context -> executePlaceModel(context, modelsManager, false))));
	}

	private static int executePlaceModel(CommandContext<ServerCommandSource> context, ModelsManager modelsManager, boolean withTranslationStrategy) throws CommandSyntaxException {
		Identifier modelId = IdentifierArgumentType.getIdentifier(context, "modelId");
		TranslateStrategy strategy;
		RegisteredModel model = modelsManager.getModel(modelId);
		if (model == null) throw UNKNOWN_MODEL_ID.create(modelId.toString());

		if (withTranslationStrategy) {
			String string = StringArgumentType.getString(context, "strategy");

			try {
				strategy = TranslateStrategy.valueOf(string);
			} catch (IllegalArgumentException e) {
				throw UNKNOWN_TRANSLATION_STRATEGY.create(string);
			}
		} else {
			strategy = TranslateStrategy.MIXED;
		}

		CrystalizeElementHolder holder = new CrystalizeElementHolder(model, strategy);
		BlockPos location = BlockPosArgumentType.getBlockPos(context, "location");
		HolderAttachment atch = ChunkAttachment.ofTicking(holder, context.getSource().getWorld(), location);
		atch.tick();
		UUID generatedId = UUID.randomUUID();
		context.getSource().getWorld().getAttachedOrCreate(HOLDERS).put(generatedId, holder);

		context.getSource().sendFeedback(() -> translatable("crystalize.command.placedModel",
			location.getX(),
			location.getY(),
			location.getZ(),
			generatedId.toString()), true);
		return 0;
	}

	private static LiteralArgumentBuilder<ServerCommandSource> addAnimation() {
		return literal("add-animation").then(argument("uuid", UuidArgumentType.uuid()).suggests(holdersUuids())
			.then(argument("animationId", StringArgumentType.string()).suggests(holderAnimationIds())
				.then(argument("timescale", FloatArgumentType.floatArg(0.01f, 100f))
					.executes(context -> executeAddAnimation(context, true)))
				.executes(context -> executeAddAnimation(context, false))));
	}

	private static int executeAddAnimation(CommandContext<ServerCommandSource> context, boolean withTimescale) throws CommandSyntaxException {
		UUID uuid = UuidArgumentType.getUuid(context, "uuid");
		Map<UUID, CrystalizeElementHolder> map = context.getSource().getWorld().getAttachedOrCreate(HOLDERS);
		CrystalizeElementHolder holder = map.get(uuid);
		if (holder == null) throw NO_HOLDER_UUID.create(uuid);

		String animationId = StringArgumentType.getString(context, "animationId");
		Optional<Animation> animationOpt = holder.getTemplate().animations().stream()
			.filter(a -> a.id().equals(animationId))
			.findAny();
		if (animationOpt.isEmpty()) throw UNKNOWN_ANIMATION_ID.create(holder.getModelId(), animationId);

		Animation animation = animationOpt.get();
		float timeScale = withTimescale ? FloatArgumentType.getFloat(context, "timescale") : 1f;
		AnimateMode modeOverride = null; // TODO allow overriding animate mode

		AnimationController controller = new TemplatedAnimationController(animation, timeScale, modeOverride);
		holder.addAnimation(controller);
		context.getSource().sendFeedback(() -> translatable("crystalize.command.addedAnimation",
			uuid,
			holder.getModelId(),
			animationId), true);
		return 0;
	}

	private static SuggestionProvider<ServerCommandSource> holderAnimationIds() {
		return (context, builder) -> {
			UUID uuid = UuidArgumentType.getUuid(context, "uuid");
			Map<UUID, CrystalizeElementHolder> map = context.getSource().getWorld().getAttachedOrCreate(HOLDERS);
			CrystalizeElementHolder holder = map.get(uuid);
			if (holder == null) throw NO_HOLDER_UUID.create(uuid);
			holder.getTemplate().animations().forEach(a -> builder.suggest(a.id()));
			return builder.buildFuture();
		};
	}

	private static SuggestionProvider<ServerCommandSource> holdersUuids() {
		return (context, builder) -> {
			Map<UUID, CrystalizeElementHolder> map = context.getSource().getWorld().getAttachedOrCreate(HOLDERS);
			map.forEach((k, v) -> builder.suggest(k.toString(), Text.literal(v.getModelId().toString())));
			return builder.buildFuture();
		};
	}

	private static LiteralArgumentBuilder<ServerCommandSource> inspect() {
		return literal("inspect").then(argument("uuid", UuidArgumentType.uuid()).suggests(holdersUuids())
			.executes(context -> {
				ServerCommandSource source = context.getSource();
				UUID uuid = UuidArgumentType.getUuid(context, "uuid");
				Map<UUID, CrystalizeElementHolder> map = source.getWorld().getAttachedOrCreate(HOLDERS);
				CrystalizeElementHolder holder = map.get(uuid);
				if (holder == null) throw NO_HOLDER_UUID.create(uuid);

				source.sendMessage(translatable("crystalize.command.inspect", uuid, holder.getModelId()));
				List<AnimationController> controllers = new ArrayList<>(holder.getAnimationControllers());

				for (int i = 0; i < controllers.size(); i++) {
					AnimationController controller = controllers.get(i);
					source.sendMessage(translatable("crystalize.command.inspectAnimation", i, controller));
				}

				source.sendMessage(translatable("crystalize.command.inspectEnd"));
				return 0;
			}));
	}
}
