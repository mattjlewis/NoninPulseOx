package com.diozero.pulseox.nonin.io;

import java.io.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.diozero.pulseox.nonin.PulseOxListenerInterface;
import com.diozero.pulseox.nonin.data.*;
import com.diozero.util.IOUtil;

public class NoninInputStreamReader implements Closeable, Runnable {
	private static final Logger logger = LogManager.getLogger(NoninInputStreamReader.class);
	
	private PulseOxMonitorInterface controller;
	private PulseOxListenerInterface listener;
	private InputStream inputStream;
	private DataFormatType dataFormat;
	
	public NoninInputStreamReader(PulseOxMonitorInterface controller,
			PulseOxListenerInterface listener, InputStream inputStream, DataFormatType dataFormat) {
		this.controller = controller;
		this.listener = listener;
		this.inputStream = new BufferedInputStream(inputStream);
		this.dataFormat = dataFormat;
	}

	@Override
	public void run() {
		try {
			// Data format 2 and 7 - encode data packets across multiple frames
			// Other data packets may be sent between frames hence the parse
			// cannot loop across multiple frames to return a complete packet
			int frame_count = -1;
			DataFormat2Frame[] packet2_frames = new DataFormat2Frame[DataFormat2PacketSerialiser.FRAME_COUNT];
			while (true) {
				inputStream.mark(1);
				short b = IOUtil.readUByte(inputStream);
				
				switch (b) {
				case Nonin9560Constants.STX:
					b = IOUtil.readUByte(inputStream);
					// Get date response?
					if (b == Nonin9560Constants.DATE_TIME_RESPONSE_OP_CODE) {
						short size = IOUtil.readUByte(inputStream);	// Should be 6
						if (size != 6) {
							logger.warn("Error, expected 6 bytes for date / time, got " + size);
						}
						short year = IOUtil.readUByte(inputStream);
						short month = IOUtil.readUByte(inputStream);
						short day = IOUtil.readUByte(inputStream);
						short hours = IOUtil.readUByte(inputStream);
						short minutes = IOUtil.readUByte(inputStream);
						short seconds = IOUtil.readUByte(inputStream);
						
						// Should be ETX
						b = IOUtil.readUByte(inputStream);
						if (b != Nonin9560Constants.ETX) {
							logger.warn(String.format(
									"Error expected 0x%x, got 0x%x\n",
									Short.valueOf(Nonin9560Constants.ETX), Short.valueOf(b)));
						}
						
						listener.messageReceived(new GetDateTimeResponse(
								year, month, day, hours, minutes, seconds));
					} else {
						logger.warn("Unrecognised STX response 0x%x\n", Short.valueOf(b));
						do {
							b = IOUtil.readUByte(inputStream);
						} while (b != Nonin9560Constants.ETX);
					}
					break;
				case Nonin9560Constants.ACK:
					listener.messageReceived(new AckResponse());
					break;
				case Nonin9560Constants.NACK:
					listener.messageReceived(new NackResponse());
					break;
				default:
					inputStream.reset();
					switch (dataFormat) {
					case DATA_FORMAT_02:
						DataFormat2Frame frame = DataFormat2FrameSerialiser.read(inputStream, frame_count);
						listener.messageReceived(frame);
						if (frame.isSync()) {
							if (frame_count > 0) {
								// FIXME This does occur! Need to send out partial packet or just ignore?
								logger.warn("Got unexpected frame sync, frame count=" + frame_count);
							}
							frame_count = 0;
						}
						if (frame_count != -1) {
							packet2_frames[frame_count] = frame;
							
							frame_count++;
							
							if (frame_count == DataFormat2PacketSerialiser.FRAME_COUNT) {
								listener.messageReceived(DataFormat2PacketSerialiser.read(packet2_frames));
								frame_count = 0;
							}
						}
						break;
					case DATA_FORMAT_07:
						// TODO Implement support for data format 07
						break;
					case DATA_FORMAT_08:
						listener.messageReceived(DataFormat8Serialiser.read(inputStream));
						break;
					case DATA_FORMAT_13:
						listener.messageReceived(DataFormat13Serialiser.read(inputStream));
						break;
					default:
						logger.warn("Unrecognised data format " + dataFormat);
					}
				}
			}
		} catch (IOException e) {
			logger.error("Error: " + e, e);
		} finally {
			try { inputStream.close(); } catch (Exception e) { }
			controller.stop();
		}
	}

	@Override
	public void close() throws IOException {
		inputStream.close();
	}
}
