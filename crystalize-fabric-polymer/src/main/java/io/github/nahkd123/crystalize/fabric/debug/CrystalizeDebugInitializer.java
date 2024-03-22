package io.github.nahkd123.crystalize.fabric.debug;

import static net.minecraft.command.argument.BlockPosArgumentType.blockPos;
import static net.minecraft.command.argument.IdentifierArgumentType.identifier;
import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

import java.util.Arrays;
import java.util.Set;

import org.joml.Vector3f;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;

import eu.pb4.polymer.virtualentity.api.attachment.BlockBoundAttachment;
import eu.pb4.polymer.virtualentity.api.attachment.HolderAttachment;
import io.github.nahkd123.crystalize.anim.controller.FabrikController;
import io.github.nahkd123.crystalize.anim.controller.TemplatedAnimationController;
import io.github.nahkd123.crystalize.fabric.CrystalizeFabric;
import io.github.nahkd123.crystalize.fabric.model.CrystalizeElementHolder;
import io.github.nahkd123.crystalize.fabric.model.RegisteredModel;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.command.argument.BlockPosArgumentType;
import net.minecraft.command.argument.IdentifierArgumentType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

public class CrystalizeDebugInitializer implements ModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger("crystalize/debug");

	@Override
	public void onInitialize() {
		if (FabricLoader.getInstance().isDevelopmentEnvironment()) {
			LOGGER.info("Detected development environment. Initializing...");

			DebugDisplayModels.initializeModels();
			DebugCrystalizeModels.initializeModels();

			CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
				dispatcher.register(literal("crystalize").then(placeCommand()));
			});
		}
	}

	private LiteralArgumentBuilder<ServerCommandSource> placeCommand() {
		return literal("place").then(argument("location", blockPos()).then(argument("id", identifier())
			.suggests((context, builder) -> {
				Set<Identifier> ids = CrystalizeFabric.getInstance().getModelsManager().getRegisteredModels().keySet();
				ids.forEach(id -> builder.suggest(id.toString()));
				return builder.buildFuture();
			})
			.executes(ctx -> {
				BlockPos pos = BlockPosArgumentType.getBlockPos(ctx, "location");
				Identifier id = IdentifierArgumentType.getIdentifier(ctx, "id");
				RegisteredModel model = CrystalizeFabric.getInstance().getModelsManager().getModel(id);
				CrystalizeElementHolder holder = new CrystalizeElementHolder(model);
				HolderAttachment atch = BlockBoundAttachment.ofTicking(holder, ctx.getSource().getWorld(), pos);
				atch.tick();

				model.template().animations().stream()
					.filter(a -> a.id().equals("animation.walking"))
					.findFirst()
					.ifPresent(a -> holder.addAnimation(new TemplatedAnimationController(a, 1f, null)));

				if (model.id().getPath().equals("debug/bones_chain")) {
					FabrikController controller = new FabrikController(Arrays.asList(
						// These IDs are from Blockbench outliner (/outliner[...]/uuid)
						"f172f382-383a-7b8b-0190-5c61b59f3b65",
						"08b84a0a-6365-ea6e-22bf-a35dd5ef9fdd",
						"9af99c2b-0f12-ef38-d6e1-14164fa656ca",
						"c23a33ee-e1e8-a5bc-e270-9a2c3180afab"), new Vector3f(12, 12, 12));
					holder.addAnimation(controller);
					ctx.getSource().sendFeedback(() -> Text.literal("Added IK controller!"), false);
				}

				ctx.getSource().sendFeedback(() -> Text.literal("Placed model at " + pos), true);
				return 1;
			})));
	}
}
