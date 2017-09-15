package com.diozero.pulseox.nonin.data;

public class DataFormat13 implements NoninResponseInterface {
	private int packetType;
	private int dataLength;
	private short hundredths;
	private short year;
	private short month;
	private short day;
	private short hour;
	private short minute;
	private short second;
	private short fraction;
	private boolean smartPointAlgorithm;
	private boolean noMeasurement;
	private boolean fromMemory;
	private boolean lowBattery;
	private int heartRate;
	private short reserved;
	private short spo2;
	
	public DataFormat13(int packetType, int dataLength, short hundredths,
			short year, short month, short day, short hour, short minute,
			short second, short fraction, boolean smartPointAlgorithm,
			boolean noMeasurement, boolean fromMemory, boolean lowBattery,
			int heartRate, short reserved, short spo2) {
		this.packetType = packetType;
		this.dataLength = dataLength;
		this.hundredths = hundredths;
		this.year = year;
		this.month = month;
		this.day = day;
		this.hour = hour;
		this.minute = minute;
		this.second = second;
		this.fraction = fraction;
		this.smartPointAlgorithm = smartPointAlgorithm;
		this.noMeasurement = noMeasurement;
		this.fromMemory = fromMemory;
		this.lowBattery = lowBattery;
		this.heartRate = heartRate;
		this.reserved = reserved;
		this.spo2 = spo2;
	}

	public int getPacketType() {
		return packetType;
	}

	public int getDataLength() {
		return dataLength;
	}

	public short getHundredths() {
		return hundredths;
	}

	public short getYear() {
		return year;
	}

	public short getMonth() {
		return month;
	}

	public short getDay() {
		return day;
	}

	public short getHour() {
		return hour;
	}

	public short getMinute() {
		return minute;
	}

	public short getSecond() {
		return second;
	}

	public short getFraction() {
		return fraction;
	}

	public boolean isSmartPointAlgorithm() {
		return smartPointAlgorithm;
	}

	public boolean isNoMeasurement() {
		return noMeasurement;
	}

	public boolean isFromMemory() {
		return fromMemory;
	}

	public boolean isLowBattery() {
		return lowBattery;
	}

	public int getHeartRate() {
		return heartRate;
	}

	public short getReserved() {
		return reserved;
	}

	public short getSpo2() {
		return spo2;
	}

	@Override
	public String toString() {
		return "DataFormat13 [packetType=" + packetType + ", dataLength="
				+ dataLength + ", hundredths=" + hundredths + ", year=" + year
				+ ", month=" + month + ", day=" + day + ", hour=" + hour
				+ ", minute=" + minute + ", second=" + second + ", fraction="
				+ fraction + ", smartPointAlgorithm=" + smartPointAlgorithm
				+ ", noMeasurement=" + noMeasurement + ", fromMemory="
				+ fromMemory + ", lowBattery=" + lowBattery + ", heartRate="
				+ heartRate + ", reserved=" + reserved + ", spo2=" + spo2 + "]";
	}
}
