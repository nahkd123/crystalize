package io.github.nahkd123.crystalize.anim;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Timeline {
	private List<Keyframe> keyframes = new ArrayList<>();
	private Channel channel;
	private Subchannel subchannel;
	private float defaultValue;

	public Timeline(Channel channel, Subchannel subchannel, float defaultValue) {
		this.channel = channel;
		this.subchannel = subchannel;
		this.defaultValue = defaultValue;
	}

	public Channel getChannel() { return channel; }

	public Subchannel getSubchannel() { return subchannel; }

	public List<Keyframe> getKeyframes() { return Collections.unmodifiableList(keyframes); }

	public void setKeyframe(Keyframe keyframe) {
		int search = Collections.binarySearch(keyframes, keyframe);
		if (search >= 0) keyframes.set(search, keyframe);
		else {
			int insert = -search - 1;
			keyframes.add(insert, keyframe);
		}
	}

	public void removeKeyframe(Keyframe keyframe) {
		int search = Collections.binarySearch(keyframes, keyframe);
		if (search >= 0) keyframes.remove(search);
	}

	public float interpolate(float t) {
		if (keyframes.size() == 0) return defaultValue;
		int search = Collections.binarySearch(keyframes, new Keyframe(t, 0));
		if (search >= 0) return keyframes.get(search).time();

		int nextIdx = -search - 1;
		if (nextIdx >= keyframes.size()) return keyframes.get(keyframes.size() - 1).value();

		Keyframe next = keyframes.get(nextIdx);
		int prevIdx = nextIdx - 1;
		if (prevIdx < 0) return next.value();

		Keyframe prev = keyframes.get(prevIdx);
		float x = (t - prev.time()) / (next.time() - prev.time());
		return prev.value() + next.easing().apply(x) * (next.value() - prev.value());
	}

	@Override
	public String toString() {
		return "Timeline" + keyframes;
	}
}
