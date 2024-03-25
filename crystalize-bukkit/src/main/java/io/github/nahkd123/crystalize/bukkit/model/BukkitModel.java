package io.github.nahkd123.crystalize.bukkit.model;

import java.util.Objects;

import org.bukkit.World;
import org.bukkit.entity.ItemDisplay;
import org.joml.Vector3f;

import io.github.nahkd123.crystalize.bukkit.CrystalizeBukkit;
import io.github.nahkd123.crystalize.model.Element;
import io.github.nahkd123.crystalize.model.ElementGroup;

public class BukkitModel {
	private AttachmentPoint attachment;
	private RegisteredModel base;
	private BonePart root;

	public final Vector3f modelTranslation = new Vector3f();
	public final Vector3f modelRotation = new Vector3f();

	public BukkitModel(AttachmentPoint attachment, RegisteredModel base) {
		Objects.requireNonNull(attachment, "attachment can't be null");
		Objects.requireNonNull(base, "base can't be null");
		attachment.onModelAttached(this);
		this.attachment = attachment;
		this.base = base;
		this.root = createBone(base.template().root(), null);
		this.root.updateTree();
	}

	public AttachmentPoint getAttachment() { return attachment; }

	public RegisteredModel getBase() { return base; }

	private BonePart createBone(ElementGroup template, BonePart parent) {
		ModelsManager modelsManager = CrystalizeBukkit.getInstance().getModelsManager();
		ItemModel model = modelsManager.getItemModel(base.getItemModelPathFor(template));
		World world = attachment.getAttachmentLocation().getWorld();
		ItemDisplay disp = world.spawn(attachment.getAttachmentLocation(), ItemDisplay.class, entity -> {
			entity.setItemStack(model.createStack());
			entity.setPersistent(false);
		});

		BonePart part = new BonePart(this, parent, disp, template);

		for (Element child : template.children()) {
			if (!(child instanceof ElementGroup groupChild)) continue;
			BonePart childPart = createBone(groupChild, part);
			part.getChildren().add(childPart);
		}

		return part;
	}

	public void tick() {
		// TODO animation
		root.updateTree();
	}
}
