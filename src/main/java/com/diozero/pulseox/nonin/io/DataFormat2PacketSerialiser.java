package com.diozero.pulseox.nonin.io;

import com.diozero.pulseox.nonin.data.DataFormat2Frame;
import com.diozero.pulseox.nonin.data.DataFormat2Packet;
import com.diozero.util.IOUtil;

public class DataFormat2PacketSerialiser {
	public static final int FRAME_COUNT = 25;

	public static DataFormat2Packet read(DataFormat2Frame[] frames) {
		if (frames.length != FRAME_COUNT) {
			return null;
		}

		short[] plethysmograph_values = new short[FRAME_COUNT];

		short hr_msb = 0;
		short hr_lsb = 0;
		short spo2 = 0;
		short software_revision = 0;
		short reserved1 = 0;
		short timer_msb = 0;
		short timer_lsb = 0;
		short status2 = 0;
		boolean smart_point_algorithm = false;
		boolean low_battery = false;
		short spo2_d = 0;
		short spo2_fast = 0;
		short spo2_bb = 0;
		short reserved2 = 0;
		short reserved3 = 0;
		short ehr_msb = 0;
		short ehr_lsb = 0;
		short e_spo2 = 0;
		short e_spo2_d = 0;
		short reserved4 = 0;
		short reserved5 = 0;
		short hrd_msb = 0;
		short hrd_lsb = 0;
		short ehrd_msb = 0;
		short ehrd_lsb = 0;
		short reserved6 = 0;
		short reserved7 = 0;

		for (int frame_count = 0; frame_count < FRAME_COUNT; frame_count++) {
			plethysmograph_values[frame_count] = frames[frame_count].getPlethysmograph();

			short val_byte = frames[frame_count].getValue();
			switch (frame_count) {
			case 0:
				hr_msb = (short) (val_byte & 0x03);
				break;
			case 1:
				hr_lsb = (short) (val_byte & 0x7f);
				break;
			case 2:
				spo2 = (short) (val_byte & 0x7f);
				break;
			case 3:
				software_revision = val_byte;
				break;
			case 4:
				reserved1 = val_byte;
				break;
			case 5:
				timer_msb = (short) (val_byte & 0x7f);
				break;
			case 6:
				timer_lsb = (short) (val_byte & 0x7f);
				break;
			case 7:
				status2 = val_byte;
				smart_point_algorithm = (status2 & 0x20) != 0;
				low_battery = (status2 & 0x01) != 0;
				break;
			case 8:
				spo2_d = val_byte;
				break;
			case 9:
				spo2_fast = val_byte;
				break;
			case 10:
				spo2_bb = val_byte;
				break;
			case 11:
				reserved2 = val_byte;
				break;
			case 12:
				reserved3 = val_byte;
				break;
			case 13:
				ehr_msb = (short) (val_byte & 0x03);
				break;
			case 14:
				ehr_lsb = (short) (val_byte & 0x7f);
				break;
			case 15:
				e_spo2 = val_byte;
				break;
			case 16:
				e_spo2_d = val_byte;
				break;
			case 17:
				reserved4 = val_byte;
				break;
			case 18:
				reserved5 = val_byte;
				break;
			case 19:
				hrd_msb = (short) (val_byte & 0x03);
				break;
			case 20:
				hrd_lsb = (short) (val_byte & 0x7f);
				break;
			case 21:
				ehrd_msb = (short) (val_byte & 0x03);
				break;
			case 22:
				ehrd_lsb = (short) (val_byte & 0x7f);
				break;
			case 23:
				reserved6 = val_byte;
				break;
			case 24:
				reserved7 = val_byte;
				break;
			}
		}

		return new DataFormat2Packet(plethysmograph_values, readInt(hr_msb, hr_lsb), spo2, software_revision, reserved1,
				readInt(timer_msb, timer_lsb), smart_point_algorithm, low_battery, spo2_d, spo2_fast, spo2_bb,
				reserved2, reserved3, readInt(ehr_msb, ehr_lsb), e_spo2, e_spo2_d, reserved4, reserved5,
				readInt(hrd_msb, hrd_lsb), readInt(ehrd_msb, ehrd_lsb), reserved6, reserved7);
	}

	/** Even though the data is transmitted in 8 bit bytes, only 7 bits are used */
	private static int readInt(short msb, short lsb) {
		if ((msb & 0x01) != 0) {
			lsb |= 0x80;
		}
		msb >>= 1;

		return IOUtil.toInt(msb, lsb);
	}
}
