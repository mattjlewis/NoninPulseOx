package com.diozero.pulseox.nonin.io;

import java.io.IOException;
import java.io.InputStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.diozero.pulseox.nonin.data.DataFormat2Frame;
import com.diozero.pulseox.nonin.data.PerfusionType;
import com.diozero.util.IOUtil;

/**
 * This data format provides continuous data transmission of a 5 byte data
 * packet sent 75 times per second. The data packet includes real-time data of:
 * 8-bit waveform value, beat-to-beat SpO2 value, SpO2 and Pulse Rates values
 * formatted for both recording and display purposes, status of the measurement
 * and battery.
 *
 * A frame consists of 5 bytes; a packet consists of 25 frames. Three packets
 * (75 frames) are transmitted each second.
 */
public class DataFormat2FrameSerialiser {
	private static final Logger logger = LogManager.getLogger(DataFormat2FrameSerialiser.class);

	public static DataFormat2Frame read(InputStream inputStream, int frameCount) throws IOException {
		int calculated_check_sum = 0;

		short byte1 = IOUtil.readUByte(inputStream);
		calculated_check_sum = (calculated_check_sum + byte1) & 0xff;
		short status = IOUtil.readUByte(inputStream);
		calculated_check_sum = (calculated_check_sum + status) & 0xff;
		short plethysmograph = IOUtil.readUByte(inputStream);
		calculated_check_sum = (calculated_check_sum + plethysmograph) & 0xff;
		short val_byte = IOUtil.readUByte(inputStream);
		calculated_check_sum = (calculated_check_sum + val_byte) & 0xff;
		short check_sum = IOUtil.readUByte(inputStream);

		// byte1 should always be 0x01
		if (byte1 != 0x01) {
			logger.warn(String.format("Frame error on first byte, expected 0x01, got 0x%x", Short.valueOf(byte1)));
		}

		if (check_sum != calculated_check_sum) {
			logger.warn("Check sum error, expected " + check_sum + ", calculated " + calculated_check_sum);
		}

		// Unpack the status field
		boolean artifact = (status & 0b00100000) != 0;
		boolean out_of_track = (status & 0b00010000) != 0;
		boolean sensor_alarm = (status & 0b00001000) != 0;
		byte prof_val = (byte) ((status & 0b00000110) >> 1);
		PerfusionType perfusion;
		switch (prof_val) {
		case 0:
			perfusion = PerfusionType.NONE;
			break;
		case 1:
			perfusion = PerfusionType.GREEN;
			break;
		case 2:
			perfusion = PerfusionType.RED;
			break;
		case 3:
			perfusion = PerfusionType.YELLOW;
			break;
		default:
			logger.warn("Unrecognised perfusion value: " + prof_val);
			perfusion = null;
		}
		boolean sync = (status & 0x01) != 0;

		if (sync && frameCount != 0) {
			frameCount = 0;
		}

		return new DataFormat2Frame(artifact, out_of_track, sensor_alarm, perfusion, sync, plethysmograph, val_byte);
	}
}

/*
 * Example: 01804900CA 0180464A11 01804400C5 01804300C4 01814300C5 0180434A0E
 * 0180436024 0180439458 01804300C4 01804100C2 01803E17D6 01803C04C1 018039601A
 * 0180366017 0180356016 01803400B5 01803400B5 01803400B5 0180354A00 0180366017
 * 0180376018 01803A00BB 01804000C1 01804A00CB 0180574A22 01806900EA 01807C4A47
 * 0180900011 0180A10022 0181AF0031
 */
