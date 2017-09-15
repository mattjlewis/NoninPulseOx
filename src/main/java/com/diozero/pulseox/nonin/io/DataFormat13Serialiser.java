package com.diozero.pulseox.nonin.io;

import java.io.IOException;
import java.io.InputStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.diozero.pulseox.nonin.data.DataFormat13;
import com.diozero.pulseox.nonin.data.Nonin9560Constants;
import com.diozero.util.IOUtil;

public class DataFormat13Serialiser {
	private static final Logger logger = LogManager.getLogger(DataFormat13Serialiser.class);
	
	private static final int SMART_POINT_ALGORITHM_BIT_MASK = 0x0200;
	private static final int NO_MEASUREMENT_BIT_MASK = 0x0100;
	private static final int FROM_MEMORY_BIT_MASK = 0x0010;
	private static final int LOW_BATTERY_BIT_MASK = 0x0001;
	private static final int NO_HEART_RATE_VALUE = 0x1ff;
	private static final short NO_SPO2_VALUE = 0x7f;
			
	public static DataFormat13 read(InputStream inputStream) throws IOException {
		IOUtil.readUByte(inputStream); // 1 - should be 00, NULL start sync
		IOUtil.readUByte(inputStream); // 2 - should be STX
		int packet_type = IOUtil.readUShort(inputStream);
		int data_length = IOUtil.readUShort(inputStream);
		int calculated_check_sum = 0;
		short hundredths = IOUtil.readUByte(inputStream); // 7 - Hundredths place of Year (default to 20)
		calculated_check_sum = (calculated_check_sum + hundredths) & 0xff;
		short year = IOUtil.readUByte(inputStream); // 8 - Year of Measurement (00-99)
		calculated_check_sum = (calculated_check_sum + year) & 0xff;
		short month = IOUtil.readUByte(inputStream); // 9 - Month of Measurement (01-12)
		calculated_check_sum = (calculated_check_sum + month) & 0xff;
		short day = IOUtil.readUByte(inputStream); // 10 - Day of Measurement (01-31 depending on the month)
		calculated_check_sum = (calculated_check_sum + day) & 0xff;
		short hour = IOUtil.readUByte(inputStream); // 11 - Hour of Measurement (00-23)
		calculated_check_sum = (calculated_check_sum + hour) & 0xff;
		short minute = IOUtil.readUByte(inputStream); // 12 - Minute of Measurement (00-59)
		calculated_check_sum = (calculated_check_sum + minute) & 0xff;
		short second = IOUtil.readUByte(inputStream); // 13 - Second of Measurement (00-59)
		calculated_check_sum = (calculated_check_sum + second) & 0xff;
		short fraction = IOUtil.readUByte(inputStream); // 14 - Fraction of second
		calculated_check_sum = (calculated_check_sum + fraction) & 0xff;
		short status_msb = IOUtil.readUByte(inputStream);
		calculated_check_sum = (calculated_check_sum + status_msb) & 0xff;
		short status_lsb = IOUtil.readUByte(inputStream);
		calculated_check_sum = (calculated_check_sum + status_lsb) & 0xff;
		int status = IOUtil.toInt(status_msb, status_lsb);
		short hr_msb = IOUtil.readUByte(inputStream);
		calculated_check_sum = (calculated_check_sum + hr_msb) & 0xff;
		short hr_lsb = IOUtil.readUByte(inputStream);
		calculated_check_sum = (calculated_check_sum + hr_lsb) & 0xff;
		int heart_rate = IOUtil.toInt(hr_msb, hr_lsb);
		short reserved = IOUtil.readUByte(inputStream); // 19 - Reserved for future use
		calculated_check_sum = (calculated_check_sum + reserved) & 0xff;
		short spo2 = IOUtil.readUByte(inputStream);
		calculated_check_sum = (calculated_check_sum + spo2) & 0xff;
		// This data section is expandable. As new parameters are available the data length
		// will increase. The minimum length of the Spot-check Data is 14 bytes (0x0E)
		for (int i=0; i<data_length-14; i++) {
			short b = IOUtil.readUByte(inputStream);
			calculated_check_sum = (calculated_check_sum + b) & 0xff;
		}
		short check_sum = IOUtil.readUByte(inputStream); // 21 - Checksum LSB - LSB of sum of Spot-check Data
		if (check_sum != calculated_check_sum) {
			logger.warn("Check sum=" + check_sum + ", calculated check_sum=" + calculated_check_sum);
		}
		short etx = IOUtil.readUByte(inputStream); // 22 - ETX - end of transmission
		if (etx != Nonin9560Constants.ETX) {
			logger.error("Error: Expected ETX (%x), got %x\n",
					Short.valueOf(Nonin9560Constants.ETX), Short.valueOf(etx));
		}
		
		boolean smart_point_algorithm = (status & SMART_POINT_ALGORITHM_BIT_MASK) != 0;
		boolean no_measurement = (status & NO_MEASUREMENT_BIT_MASK) != 0;
		boolean from_memory = (status & FROM_MEMORY_BIT_MASK) != 0;
		boolean low_battery = (status & LOW_BATTERY_BIT_MASK) != 0;
		
		if (heart_rate == NO_HEART_RATE_VALUE) {
			logger.info("No pulse reading");
		}
		if (spo2 == NO_SPO2_VALUE) {
			logger.info("No SpO2 reading");
		}
		
		return new DataFormat13(packet_type, data_length, hundredths, year, month, day,
				hour, minute, second, fraction, smart_point_algorithm, no_measurement,
				from_memory, low_battery, heart_rate, reserved, spo2);
	}
}
