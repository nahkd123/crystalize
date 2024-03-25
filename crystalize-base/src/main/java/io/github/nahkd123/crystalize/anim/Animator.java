package io.github.nahkd123.crystalize.anim;

import java.util.EnumMap;
import java.util.Map;

import org.joml.Vector3f;

import io.github.nahkd123.crystalize.model.ElementGroup;
import io.github.nahkd123.crystalize.utils.Transformation;

/**
 * <p>
 * Animator animates a single part in the model.
 * </p>
 */
public class Animator {
	private String groupId;
	private Map<Channel, TimelineGroup> channels = new EnumMap<>(Channel.class);

	public Animator(String groupId) {
		this.groupId = groupId;
	}

	/**
	 * <p>
	 * Get the ID of the group (which is {@link ElementGroup#id()}) that this
	 * animator will apply transformations to.
	 * </p>
	 * 
	 * @return The group ID.
	 */
	public String getGroupId() { return groupId; }

	/**
	 * <p>
	 * Get the animation channel. Each channel have 3 different subchannels.
	 * Keyframes across all 3 subchannels are not linked to each other.
	 * </p>
	 * 
	 * @param channel The channel type.
	 * @return The animation channel.
	 */
	public TimelineGroup getChannel(Channel channel) {
		TimelineGroup group = channels.get(channel);
		if (group == null) channels.put(channel, group = new TimelineGroup(channel));
		return group;
	}

	/**
	 * <p>
	 * Get the part transformation at given time (in seconds).
	 * </p>
	 * 
	 * @param t The time in seconds.
	 * @return The transformation at given time.
	 */
	public Transformation getAt(float t) {
		Vector3f translation = getChannel(Channel.TRANSLATION).interpolate(t);
		Vector3f rotation = getChannel(Channel.ROTATION).interpolate(t);
		Vector3f scale = getChannel(Channel.SCALE).interpolate(t);
		return new Transformation(translation, rotation, scale);
	}

	@Override
	public String toString() {
		return "Animator[" + groupId + " => " + channels + "]";
	}
}
