package io.github.nahkd123.crystalize.fabric.model;

/**
 * <p>
 * Model translation strategy. Default is {@link #MIXED}.
 * </p>
 * 
 * @see #TRANSLATION_ONLY
 * @see #MIXED
 * @see #POSITION_ONLY
 */
public enum TranslateStrategy {
	/**
	 * <p>
	 * Only use local bone translation. Guaranteed smoothness for translation,
	 * rotation and scale thanks to interpolation, but might not be rendered when
	 * the model origin is out of sight. Best for small models.
	 * </p>
	 */
	TRANSLATION_ONLY,

	/**
	 * <p>
	 * Mixed between world positioning and local translating. Allows parts to rotate
	 * smoothly while allowing model to be rendered on client partially. Best for
	 * models with decent amount of parts. This is the default strategy.
	 * </p>
	 */
	MIXED,

	/**
	 * <p>
	 * Only use world positioning for bones. The bones are teleported in the world
	 * space, allowing parts of the model to be rendered even if the origin is out
	 * of sight, but no interpolation is applied. Best for models with large amount
	 * of parts.
	 * </p>
	 */
	POSITION_ONLY;
}
