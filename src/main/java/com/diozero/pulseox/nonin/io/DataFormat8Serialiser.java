package com.diozero.pulseox.nonin.io;

import java.io.IOException;
import java.io.InputStream;

import com.diozero.pulseox.nonin.data.DataFormat8;
import com.diozero.util.IOUtil;

public class DataFormat8Serialiser {
	private static final short OUT_OF_TRACK_BIT_MASK = 0x20;
	private static final short LOW_PERFUSION_BIT_MASK = 0x10;
	private static final short MARGINAL_PERFUSION_BIT_MASK = 0x08;
	private static final short ARTIFACT_BIT_MASK = 0x04;
	private static final short SMART_POINT_ALGORITHM_BIT_MASK = 0x20;
	private static final short SENSOR_ALARM_BIT_MASK = 0x08;
	private static final short LOW_BATTERY_BIT_MASK = 0x01;
	
	public static DataFormat8 read(InputStream is) throws IOException {
		short status1 = IOUtil.readUByte(is);
		boolean out_of_track = (status1 & OUT_OF_TRACK_BIT_MASK) != 0;
		boolean low_perfusion = (status1 & LOW_PERFUSION_BIT_MASK) != 0;
		boolean marginal_perfusion = (status1 & MARGINAL_PERFUSION_BIT_MASK) != 0;
		boolean artifact = (status1 & ARTIFACT_BIT_MASK) != 0;
		short heart_rate = (short)(((status1 & 0x03) << 7) | IOUtil.readUByte(is));
		short spo2 = IOUtil.readUByte(is);
		short status2 = IOUtil.readUByte(is);
		boolean smart_point_algorithm = (status2 & SMART_POINT_ALGORITHM_BIT_MASK) != 0;
		boolean sensor_alarm = (status2 & SENSOR_ALARM_BIT_MASK) != 0;
		boolean low_battery = (status2 & LOW_BATTERY_BIT_MASK) != 0;
		
		return new DataFormat8(out_of_track, low_perfusion, marginal_perfusion,
				artifact, heart_rate, spo2, smart_point_algorithm, sensor_alarm, low_battery);
	}
}
