package com.diozero.pulseox.nonin.data;

interface DataFormatTypeValues {
	static final short DATA_FORMAT_02_VALUE = 0x02;
	static final short DATA_FORMAT_07_VALUE = 0x07;
	static final short DATA_FORMAT_08_VALUE = 0x08;
	static final short DATA_FORMAT_13_VALUE = 0x0D;
}

public enum DataFormatType {
	/** Continuous data transmission of 5 byte data packet 75 times per second (8-bit resolution) */
	DATA_FORMAT_02(DataFormatTypeValues.DATA_FORMAT_02_VALUE),
	/** Continuous data transmission of 5 byte data packet 75 times per second (16-bit resolution) */
	DATA_FORMAT_07(DataFormatTypeValues.DATA_FORMAT_07_VALUE),
	/** Continuous data transmission of 4 byte data packet once per second (8-bit resolution) */
	DATA_FORMAT_08(DataFormatTypeValues.DATA_FORMAT_08_VALUE),
	/** SmartPoint Algorithm */
	DATA_FORMAT_13(DataFormatTypeValues.DATA_FORMAT_13_VALUE);

	private short value;
	private DataFormatType(short value) {
		this.value = value;
	}
	
	public short getValue() {
		return value;
	}
	
	public static DataFormatType forValue(short value) {
		DataFormatType type;
		switch (value) {
		case DataFormatTypeValues.DATA_FORMAT_02_VALUE:
			type = DATA_FORMAT_02;
			break;
		case DataFormatTypeValues.DATA_FORMAT_07_VALUE:
			type = DATA_FORMAT_07;
			break;
		case DataFormatTypeValues.DATA_FORMAT_08_VALUE:
			type = DATA_FORMAT_08;
			break;
		case DataFormatTypeValues.DATA_FORMAT_13_VALUE:
			type = DATA_FORMAT_13;
			break;
		default:
			type = null;
		}
		
		return type;
	}
}
