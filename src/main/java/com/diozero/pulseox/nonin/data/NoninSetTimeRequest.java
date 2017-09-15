package com.diozero.pulseox.nonin.data;

public class NoninSetTimeRequest implements NoninRequestInterface {
	@Override
	public byte[] getBytes() {
		return null;
	}
	
	@Override
	public short getOpCode() {
		return Nonin9560Constants.DATE_TIME_OP_CODE;
	}
	
	@Override
	public String getName() {
		return "Set Time";
	}
}
