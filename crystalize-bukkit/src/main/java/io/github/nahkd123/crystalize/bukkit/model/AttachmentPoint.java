package io.github.nahkd123.crystalize.bukkit.model;

import org.bukkit.Location;

public interface AttachmentPoint {
	/**
	 * <p>
	 * Get the location of this attachment point. This should return a clone of
	 * internal location object.
	 * </p>
	 * 
	 * @return The attachment location in world space.
	 */
	public Location getAttachmentLocation();

	/**
	 * <p>
	 * Called when the model is attached to this attachment point. The attachment
	 * point implementation should call {@link BukkitModel#tick()} on every
	 * animation tick (which in Bukkit, is server tick).
	 * </p>
	 * 
	 * @param model The model.
	 */
	public void onModelAttached(BukkitModel model);
}
