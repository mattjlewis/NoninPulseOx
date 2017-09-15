package com.diozero.pulseox.nonin.data;

public class DataFormat8 implements NoninResponseInterface {
	private boolean outOfTrack;
	private boolean lowPerfusion;
	private boolean marginalPerfusion;
	private boolean artifact;
	private short heartRate;
	private short spo2;
	private boolean smartPointAlgorithm;
	private boolean sensorAlarm;
	private boolean lowBattery;
	
	public DataFormat8(boolean outOfTrack, boolean lowPerfusion,
			boolean marginalPerfusion, boolean artifact, short heartRate, short spo2,
			boolean smartPointAlgorithm, boolean sensorAlarm, boolean lowBattery) {
		this.outOfTrack = outOfTrack;
		this.lowPerfusion = lowPerfusion;
		this.marginalPerfusion = marginalPerfusion;
		this.artifact = artifact;
		this.heartRate = heartRate;
		this.spo2 = spo2;
		this.smartPointAlgorithm = smartPointAlgorithm;
		this.sensorAlarm = sensorAlarm;
		this.lowBattery = lowBattery;
	}
	
	/** An absence of consecutive good pulse signals. Indicates sustained period of Artifact */
	public boolean isOutOfTrack() {
		return outOfTrack;
	}
	
	/** Amplitude representation of low/no signal quality (holds for entire duration) */
	public boolean isLowPerfusion() {
		return lowPerfusion;
	}
	
	/** Amplitude representation of low/marginal signal quality (holds for entire duration) */
	public boolean isMarginalPerfusion() {
		return marginalPerfusion;
	}
	
	/** Indicated artifact condition on each pulse */
	public boolean isArtifact() {
		return artifact;
	}
	
	public short getHeartRate() {
		return heartRate;
	}
	
	public short getSpo2() {
		return spo2;
	}
	
	public boolean isSmartPointAlgorithm() {
		return smartPointAlgorithm;
	}
	
	public boolean isSensorAlarm() {
		return sensorAlarm;
	}
	
	public boolean isLowBattery() {
		return lowBattery;
	}

	@Override
	public String toString() {
		return "DataFormat8 [outOfTrack=" + outOfTrack + ", lowPerfusion="
				+ lowPerfusion + ", marginalPerfusion=" + marginalPerfusion
				+ ", artifact=" + artifact + ", heartRate=" + heartRate
				+ ", spo2=" + spo2 + ", smartPointAlgorithm="
				+ smartPointAlgorithm + ", sensorAlarm=" + sensorAlarm
				+ ", lowBattery=" + lowBattery + "]";
	}
}
