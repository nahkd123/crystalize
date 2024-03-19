package io.github.nahkd123.crystalize.anim;

import java.util.EnumMap;
import java.util.Map;

import org.joml.Vector3f;

import io.github.nahkd123.crystalize.utils.Transformation;

public class Animator {
	private String groupId;
	private Map<Channel, TimelineGroup> channels = new EnumMap<>(Channel.class);

	public Animator(String groupId) {
		this.groupId = groupId;
	}

	public String getGroupId() { return groupId; }

	public TimelineGroup getChannel(Channel channel) {
		TimelineGroup group = channels.get(channel);
		if (group == null) channels.put(channel, group = new TimelineGroup(channel));
		return group;
	}

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
