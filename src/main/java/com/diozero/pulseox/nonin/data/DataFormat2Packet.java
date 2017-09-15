package com.diozero.pulseox.nonin.data;

import java.util.Arrays;

public class DataFormat2Packet implements NoninResponseInterface {
	private short[] plethmographValues;
	private int heartRate;
	private short spO2;
	private short softwareRevision;
	private short reserved1;
	private int timer;
	private boolean smartPointAlgorithm;
	private boolean lowBattery;
	private short spO2Display;
	private short spO2FastAverage;
	private short spO2BeatToBeat;
	private short reserved2;
	private short reserved3;
	private int extendedHeartRateAverage;
	private short extendedSpO2Average;
	private short extendedSpO2Display;
	private short reserved4;
	private short reserved5;
	private int heartRateDisplay;
	private int extendedHeartRateDisplay;
	private short reserved6;
	private short reserved7;
	
	public DataFormat2Packet(short[] plethmographValues, int heartRate, short spO2,
			short softwareRevision, short reserved1, int timer, boolean smartPointAlgorithm,
			boolean lowBattery, short spO2Display, short spO2FastAverage, short spO2BeatToBeat,
			short reserved2, short reserved3, int extendedHeartRateAverage,
			short extendedSpO2Average, short extendedSpO2Display, short reserved4, short reserved5,
			int heartRateDisplay, int extendedHeartRateDisplay, short reserved6, short reserved7) {
		this.plethmographValues = plethmographValues;
		this.heartRate = heartRate;
		this.spO2 = spO2;
		this.softwareRevision = softwareRevision;
		this.reserved1 = reserved1;
		this.timer = timer;
		this.smartPointAlgorithm = smartPointAlgorithm;
		this.lowBattery = lowBattery;
		this.spO2Display = spO2Display;
		this.spO2FastAverage = spO2FastAverage;
		this.spO2BeatToBeat = spO2BeatToBeat;
		this.reserved2 = reserved2;
		this.reserved3 = reserved3;
		this.extendedHeartRateAverage = extendedHeartRateAverage;
		this.extendedSpO2Average = extendedSpO2Average;
		this.extendedSpO2Display = extendedSpO2Display;
		this.reserved4 = reserved4;
		this.reserved5 = reserved5;
		this.heartRateDisplay = heartRateDisplay;
		this.extendedHeartRateDisplay = extendedHeartRateDisplay;
		this.reserved6 = reserved6;
		this.reserved7 = reserved7;
	}

	public short[] getPlethmographValues() {
		return plethmographValues;
	}

	public int getHeartRate() {
		return heartRate;
	}

	public short getSpO2() {
		return spO2;
	}

	public short getSoftwareRevision() {
		return softwareRevision;
	}

	public short getReserved1() {
		return reserved1;
	}

	public int getTimer() {
		return timer;
	}

	public boolean isSmartPointAlgorithm() {
		return smartPointAlgorithm;
	}

	public boolean isLowBattery() {
		return lowBattery;
	}

	public short getSpO2Display() {
		return spO2Display;
	}

	public short getSpO2FastAverage() {
		return spO2FastAverage;
	}

	public short getSpO2BeatToBeat() {
		return spO2BeatToBeat;
	}

	public short getReserved2() {
		return reserved2;
	}

	public short getReserved3() {
		return reserved3;
	}

	public int getExtendedHeartRateAverage() {
		return extendedHeartRateAverage;
	}

	public short getExtendedSpO2Average() {
		return extendedSpO2Average;
	}

	public short getExtendedSpO2Display() {
		return extendedSpO2Display;
	}

	public short getReserved4() {
		return reserved4;
	}

	public short getReserved5() {
		return reserved5;
	}

	public int getHeartRateDisplay() {
		return heartRateDisplay;
	}

	public int getExtendedHeartRateDisplay() {
		return extendedHeartRateDisplay;
	}

	public short getReserved6() {
		return reserved6;
	}

	public short getReserved7() {
		return reserved7;
	}

	@Override
	public String toString() {
		return "DataFormat2Packet [plethmographValues="
				+ Arrays.toString(plethmographValues) + ", heartRate="
				+ heartRate + ", spO2=" + spO2 + ", softwareRevision="
				+ softwareRevision + ", reserved1=" + reserved1 + ", timer="
				+ timer + ", smartPointAlgorithm=" + smartPointAlgorithm
				+ ", lowBattery=" + lowBattery + ", spO2Display=" + spO2Display
				+ ", spO2FastAverage=" + spO2FastAverage + ", spO2BeatToBeat="
				+ spO2BeatToBeat + ", reserved2=" + reserved2 + ", reserved3="
				+ reserved3 + ", extendedHeartRateAverage="
				+ extendedHeartRateAverage + ", extendedSpO2Average="
				+ extendedSpO2Average + ", extendedSpO2Display="
				+ extendedSpO2Display + ", reserved4=" + reserved4
				+ ", reserved5=" + reserved5 + ", heartRateDisplay="
				+ heartRateDisplay + ", extendedHeartRateDisplay="
				+ extendedHeartRateDisplay + ", reserved6=" + reserved6
				+ ", reserved7=" + reserved7 + "]";
	}
}
