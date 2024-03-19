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

		int currentIdx = -search - 2;
		if (currentIdx >= keyframes.size()) return keyframes.get(keyframes.size() - 1).value();

		Keyframe current = keyframes.get(currentIdx);
		int nextIdx = currentIdx + 1;
		if (nextIdx >= keyframes.size()) return current.value();

		Keyframe next = keyframes.get(nextIdx);
		float x = (t - current.time()) / (next.time() - current.time());
		return current.value() + next.easing().apply(x) * (next.value() - current.value());
	}

	@Override
	public String toString() {
		return "Timeline" + keyframes;
	}
}
