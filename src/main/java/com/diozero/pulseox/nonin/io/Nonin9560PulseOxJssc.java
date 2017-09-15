package com.diozero.pulseox.nonin.io;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.diozero.pulseox.nonin.PulseOxListenerInterface;
import com.diozero.pulseox.nonin.data.*;
import com.diozero.util.IOUtil;

import jssc.*;

public class Nonin9560PulseOxJssc implements PulseOxMonitorInterface, SerialPortEventListener {
	private static final Logger logger = LogManager.getLogger(Nonin9560PulseOxJssc.class);

	private String serialPortName;
	private SerialPort serialPort;
	private DataFormatType dataFormat;
	private PipedOutputStream pipedOutputStream;
	private NoninInputStreamReader streamReader;
	private boolean clearToSend;
	private boolean connected;
	private PulseOxListenerInterface listener;
	
	public Nonin9560PulseOxJssc(DataFormatType dataFormat, String serialPortName,
			PulseOxListenerInterface listener) {
		this.dataFormat = dataFormat;
		this.serialPortName = serialPortName;
		this.listener = listener;
	}
	
	public String getSerialPortName() {
		return serialPortName;
	}

	@Override
	public void start() {
		logger.info("Available serial ports:");
		for (String portName : SerialPortList.getPortNames()) {
			logger.info(portName);
		}
		
		try {
			pipedOutputStream = new PipedOutputStream();
			PipedInputStream pis = new PipedInputStream(pipedOutputStream);
			
			// On Linux: /dev/rfcomm0
			// On Windows: COMxx. Connecting prompts for a pin - this is the 6 digit code in the connect prompt
			// The 9560 has a six digit identification number printed on the battery door
			// Actually printed on the right hand side (when wearing it), the PIN value
			serialPort = new SerialPort(serialPortName);
			
			logger.info("isOpened? " + serialPort.isOpened());
			
			connected = serialPort.openPort();
			
			streamReader = new NoninInputStreamReader(this, listener, pis, dataFormat);
			Thread t = new Thread(streamReader);
			t.start();
			
			serialPort.setEventsMask(SerialPort.MASK_RXCHAR |
					/*SerialPort.MASK_RXFLAG | */SerialPort.MASK_TXEMPTY |
					SerialPort.MASK_CTS | SerialPort.MASK_DSR |
					SerialPort.MASK_RLSD | SerialPort.MASK_BREAK |
					SerialPort.MASK_ERR | SerialPort.MASK_RING);
			serialPort.addEventListener(this);
			serialPort.setParams(SerialPort.BAUDRATE_9600, SerialPort.DATABITS_8,
					SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
			logger.info("Port opened: " + connected);
		} catch (SerialPortException spe) {
			logger.error("Error: " + spe, spe);
		} catch (IOException ioe) {
			logger.error("Error: " + ioe, ioe);
		}
	}

	@Override
	public void stop() {
		logger.info("stop()");
		
		connected = false;
		
		if (serialPort != null && serialPort.isOpened()) {
			logger.info("Closing serial port...");
			try {
				boolean closed = serialPort.closePort();
				logger.info("Closed serial port: " + closed);
			} catch (Exception e) {
				logger.warn("Error: " + e, e);
			}
		}
		
		if (pipedOutputStream != null) {
			try {
				pipedOutputStream.close();
			} catch (Exception e) {
				logger.error("Error closing piped output stream: " + e, e);
			}
		}
		
		if (streamReader != null) {
			//streamReader = null;
		}
	}
	
	@Override
	public boolean isConnected() {
		return connected;
	}
	
	@Override
	public boolean isClearToSend() {
		return clearToSend;
	}
	
	@Override
	public void sendMessage(NoninRequestInterface request) {
		logger.info("Sending request '" + request.getName() + "'");
		
		try {
			sendMessage(request.getOpCode(), request.getBytes());
		} catch (SerialPortException spe) {
			logger.error("Serial port exception: " + spe, spe);
			
			stop();
		}
	}
	
	private void sendMessage(short opCode, byte ... payload) throws SerialPortException {
		int payload_length = payload == null ? 0 : payload.length;
		byte[] request = new byte[4 + payload_length];
		request[0] = Nonin9560Constants.STX;
		request[1] = (byte)opCode;
		request[2] = (byte)payload_length;
		if (payload != null) {
			System.arraycopy(payload, 0, request, 3, payload.length);
		}
		request[request.length-1] = Nonin9560Constants.ETX;
		
		logger.info("Sending request '" + IOUtil.bytesToHex(request) + "'");
		boolean written = serialPort.writeBytes(request);
		logger.trace("written: " + written);
	}

	@Override
	public void serialEvent(SerialPortEvent event) {
		logger.trace("serialEvent(type=" + event.getEventType()
				+ ", value=" + event.getEventValue()
				+ ", portName=" + event.getPortName()
				+ ")");
		
		// If data is available
		if (event.isRXCHAR()) {
			if (event.getEventValue() > 0) {
				try {
					byte[] bytes = serialPort.readBytes();
					if (bytes == null) {
						logger.warn("Null bytes returned from readBytes, expected " + event.getEventValue() + " bytes");
						return;
					}
					
					logger.trace("Read: " + IOUtil.bytesToHex(bytes));
					
					pipedOutputStream.write(bytes);
					pipedOutputStream.flush();
				} catch (Exception e) {
					logger.error("Error: " + e, e);
					stop();
				}
			}
		}
		/*
		 * Hardware Handshaking: The second method of handshaking is to use actual hardware lines.
		 * Like the Tx and Rx lines, the RTS/CTS and DTR/DSR lines work together with one being
		 * the output and the other the input. The first set of lines are Request to Send (RTS)
		 * and Clear to Send (CTS). When a receiver is ready for data, it will assert the RTS
		 * line indicating it is ready to receive data. This is then read by the sender at the CTS
		 * input, indicating it is clear to send the data. The next set of lines are Data Terminal
		 * Ready (DTR) and Data Set Ready (DSR). These lines are used mainly for modem
		 * communication. They allow the serial port and the modem to communicate their status.
		 * For example, when the modem is ready for data to be sent from the PC, it will assert
		 * the DTR line indicating that a connection has been made across the phone line. This is
		 * read in through the DSR line and the PC can begin to send data. The general rule of
		 * thumb is that the DTR/DSR lines are used to indicate that the system is ready for
		 * communication where the RTS/CTS lines are used for individual packets of data. 
			SerialPort.MASK_RXCHAR	1	Bytes count in input buffer
			SerialPort.MASK_RXFLAG	2	Bytes count in input buffer (not supported on Linux)
			SerialPort.MASK_TXEMPTY	4	Bytes count on output buffer
			SerialPort.MASK_CTS		8	Clear to Send (0 - Off, 1 - On)
			SerialPort.MASK_DSR		16	Data Set Ready (0 - Off, 1 - On)
			SerialPort.MASK_RLSD	32	Receive Line Signal Detect (0 - Off, 1 - On) [AKA Carrier Detect - CD]
			SerialPort.MASK_BREAK	64	0
			SerialPort.MASK_ERR		128	Mask of errors
			SerialPort.MASK_RING	256	State of RING line (0 - OFF, 1 - ON)
		 */
		// Has the Clear to Send line changed state?
		if (event.isCTS()) {
			logger.info("Clear to send? " + (event.getEventValue() == 1));
			clearToSend = event.getEventValue() == 1;
			if (clearToSend) {
				sendMessage(new NoninSetDataFormatRequest(dataFormat));
			}
		}
		
		if (event.isDSR()) {
			logger.info("Data set ready (i.e. device ready to communicate)? " + (event.getEventValue() == 1));
		}
		
		if (event.isRLSD()) {
			logger.info("Receive Line Signal Detect? " + (event.getEventValue() == 1));
		}
		
		if (event.isBREAK()) {
			logger.info("Break message value: " + event.getEventValue());
		}
		
		if (event.isERR()) {
			logger.info("Error message value: " + event.getEventValue());
		}
		
		if (event.isRLSD()) {
			logger.info("RING Line state: " + (event.getEventValue() == 1));
		}
	}
}
