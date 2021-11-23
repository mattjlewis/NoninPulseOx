package com.diozero.pulseox.nonin.data;

public class DataFormat2Frame implements NoninResponseInterface {
	private boolean artifact;
	private boolean outOfTrack;
	private boolean sensorAlarm;
	private PerfusionType perfusion;
	private boolean sync;
	private short plethysmograph;
	private short value;

	public DataFormat2Frame(boolean artifact, boolean outOfTrack, boolean sensorAlarm, PerfusionType perfusion,
			boolean sync, short plethysmograph, short value) {
		this.artifact = artifact;
		this.outOfTrack = outOfTrack;
		this.sensorAlarm = sensorAlarm;
		this.perfusion = perfusion;
		this.sync = sync;
		this.plethysmograph = plethysmograph;
		this.value = value;
	}

	public boolean isArtifact() {
		return artifact;
	}

	public boolean isOutOfTrack() {
		return outOfTrack;
	}

	public boolean isSensorAlarm() {
		return sensorAlarm;
	}

	public PerfusionType getPerfusion() {
		return perfusion;
	}

	public boolean isSync() {
		return sync;
	}

	public short getPlethysmograph() {
		return plethysmograph;
	}

	public short getValue() {
		return value;
	}

	@Override
	public String toString() {
		return "DataFormat2Frame [artifact=" + artifact + ", outOfTrack=" + outOfTrack + ", sensorAlarm=" + sensorAlarm
				+ ", perfusion=" + perfusion + ", sync=" + sync + ", plethysmograph=" + plethysmograph + ", value="
				+ value + "]";
	}
}
