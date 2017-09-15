package com.diozero.pulseox.nonin.data;

public class NoninSetDataFormatRequest implements NoninRequestInterface {
	private DataFormatType dataFormat;
	
	public NoninSetDataFormatRequest(DataFormatType dataFormat) {
		this.dataFormat = dataFormat;
	}
	
	@Override
	public byte[] getBytes() {
		byte data_type = 0x02;
		return new byte[] { data_type, (byte)dataFormat.getValue() };
	}
	
	@Override
	public short getOpCode() {
		return Nonin9560Constants.DATA_FORMAT_OP_CODE;
	}
	
	@Override
	public String getName() {
		return "Set Data Format";
	}
}
