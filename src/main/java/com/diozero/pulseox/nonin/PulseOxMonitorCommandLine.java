package com.diozero.pulseox.nonin;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.diozero.pulseox.nonin.data.*;
import com.diozero.pulseox.nonin.io.Nonin9560PulseOxJssc;
import com.diozero.pulseox.nonin.io.PulseOxMonitorInterface;

public class PulseOxMonitorCommandLine implements PulseOxListenerInterface {
	private static final Logger logger = LogManager.getLogger(PulseOxMonitorCommandLine.class);
	
	private static final DataFormatType DEFAULT_DATA_FORMAT = DataFormatType.DATA_FORMAT_02;
	
	public static void main(String[] args) {
		// On Linux: /dev/rfcomm0
		// On Windows: COMxx
		String serial_port;
		if (args.length == 0) {
			logger.error("Error: Usage: " + PulseOxMonitorCommandLine.class.getName() + " <serial-port-name> [data format]");
			System.exit(1);
		}
		serial_port = args[0];
		
		DataFormatType data_format;
		if (args.length > 1) {
			data_format = DataFormatType.forValue(Short.parseShort(args[1]));
		} else {
			data_format = DEFAULT_DATA_FORMAT;
		}
		
		new PulseOxMonitorCommandLine(data_format, serial_port).start();
	}

	
	private PulseOxMonitorInterface pulseOxMonitor;
	
	public PulseOxMonitorCommandLine(DataFormatType dataFormat, String serialPortName) {
		pulseOxMonitor = new Nonin9560PulseOxJssc(dataFormat, serialPortName, this);
	}
	
	public void start() {
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			@Override
			public void run() {
				if (pulseOxMonitor != null) {
					pulseOxMonitor.stop();
				}
			}
		}));
		
		pulseOxMonitor.start();
		
		try {
			int count = 0;
			while (pulseOxMonitor.isConnected()) {
				Thread.sleep(1000);
				
				if (pulseOxMonitor.isClearToSend()) {
					switch (count++) {
					case 0:
						pulseOxMonitor.sendMessage(new NoninGetTimeRequest());
						break;
					case 1:
						pulseOxMonitor.sendMessage(new NoninSetTimeRequest());
						break;
					case 2:
						pulseOxMonitor.sendMessage(new NoninGetTimeRequest());
						break;
					case 300:
						logger.info("Reached count max, shutting down");
						pulseOxMonitor.stop();
						break;
					}
				}
			}
		} catch (InterruptedException ie) {
			logger.error("Interrupted: " + ie, ie);
		} finally {
			pulseOxMonitor.stop();
		}
	}

	@Override
	public void messageReceived(NoninResponseInterface response) {
		if (response instanceof DataFormat2Frame) {
			logger.trace("Got message: " + response);
		} else {
			logger.info("Got message: " + response);
		}
	}
}
