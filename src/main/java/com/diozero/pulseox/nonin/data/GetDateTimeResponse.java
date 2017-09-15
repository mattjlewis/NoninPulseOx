package com.diozero.pulseox.nonin.data;

import java.util.Calendar;

public class GetDateTimeResponse implements NoninResponseInterface {
	private short year;
	private short month;
	private short day;
	private short hour;
	private short minute;
	private short second;

	public GetDateTimeResponse(short year, short month, short day,
			short hour, short minute, short second) {
		this.year = year;
		this.month = month;
		this.day = day;
		this.hour = hour;
		this.minute = minute;
		this.second = second;
		
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.MONTH, day);
		calendar.set(Calendar.DAY_OF_MONTH, day);
		calendar.set(Calendar.HOUR_OF_DAY, hour);
		calendar.set(Calendar.MINUTE, minute);
		calendar.set(Calendar.SECOND, second);
		calendar.set(Calendar.MILLISECOND, 0);
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

	@Override
	public String toString() {
		return getClass().getSimpleName() + ": " + day + "/" + month + "/" + year
				+ " " + hour + ":" + minute + ":" + second;
	}
}
