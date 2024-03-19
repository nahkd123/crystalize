package io.github.nahkd123.crystalize.texture;

import io.github.nahkd123.crystalize.blob.Blob;

public record Texture(String id, int width, int height, float uvWidth, float uvHeight, double frameTime, Blob data) {
	/**
	 * <p>
	 * Make a new texture.
	 * </p>
	 * 
	 * @param id        The ID of the textures. Minecraft resources generation will
	 *                  place all textures under the
	 *                  {@code assets/<namespace>/textures/crystalize/<modelId>/<textureId>.png}
	 *                  format.
	 * @param width     The width of this texture in pixels.
	 * @param height    The height of this texture in pixels. The defined size will
	 *                  be used for texture animations.
	 * @param uvWidth   The UV width of this texture.
	 * @param uvHeight  The UV height of this texture.
	 * @param frameTime The animation duration. If the texture is animated, the
	 *                  metadata file will be placed as
	 *                  {@code assets/<namespace>/textures/crystalize/<modelId>/<textureId>.png.mcmeta}.
	 * @param data      The raw PNG data.
	 */
	public Texture {
	}

	@Override
	public String toString() {
		return "Texture["
			+ "id=" + id +
			", size=" + width + "*" + height +
			", uvSize=" + uvWidth + "*" + uvHeight +
			", frameTime=" + frameTime +
			". data=" + data;
	}
}
