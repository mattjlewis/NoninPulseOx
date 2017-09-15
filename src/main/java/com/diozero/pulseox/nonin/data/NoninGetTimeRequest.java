package com.diozero.pulseox.nonin.data;

import java.util.Calendar;

public class NoninGetTimeRequest implements NoninRequestInterface {
	@Override
	public byte[] getBytes() {
		Calendar cal = Calendar.getInstance();
		
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH)+1;
		int day = cal.get(Calendar.DAY_OF_MONTH);
		int hours = cal.get(Calendar.HOUR_OF_DAY);
		int minutes = cal.get(Calendar.MINUTE);
		int seconds = cal.get(Calendar.SECOND);
		
		return new byte[] { (byte)(year - 2000), (byte)month, (byte)day,
				(byte)hours, (byte)minutes, (byte)seconds };
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
