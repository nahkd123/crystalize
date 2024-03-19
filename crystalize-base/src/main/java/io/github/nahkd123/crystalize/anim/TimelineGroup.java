package io.github.nahkd123.crystalize.anim;

import java.util.EnumMap;
import java.util.Map;

import org.joml.Vector3f;

public class TimelineGroup {
	private Channel channel;
	private Map<Subchannel, Timeline> subchannels = new EnumMap<>(Subchannel.class);

	public TimelineGroup(Channel channel) {
		this.channel = channel;
	}

	public Channel getChannel() { return channel; }

	public Timeline getSubchannel(Subchannel subchannel) {
		Timeline timeline = subchannels.get(subchannel);
		if (timeline == null) subchannels.put(subchannel,
			timeline = new Timeline(channel, subchannel, channel == Channel.SCALE ? 1f : 0f));
		return timeline;
	}

	public Vector3f interpolate(float t, Vector3f out) {
		out.x = getSubchannel(Subchannel.X).interpolate(t);
		out.y = getSubchannel(Subchannel.Y).interpolate(t);
		out.z = getSubchannel(Subchannel.Z).interpolate(t);
		return out;
	}

	public Vector3f interpolate(float t) {
		return interpolate(t, new Vector3f());
	}

	@Override
	public String toString() {
		return subchannels.toString();
	}
}
