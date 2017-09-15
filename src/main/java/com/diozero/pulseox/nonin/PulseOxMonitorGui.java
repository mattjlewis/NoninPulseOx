package com.diozero.pulseox.nonin;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.diozero.pulseox.nonin.data.*;
import com.diozero.pulseox.nonin.io.Nonin9560PulseOxJssc;
import com.diozero.pulseox.nonin.io.PulseOxMonitorInterface;

import info.monitorenter.gui.chart.Chart2D;
import info.monitorenter.gui.chart.ITrace2D;
import info.monitorenter.gui.chart.rangepolicies.RangePolicyFixedViewport;
import info.monitorenter.gui.chart.traces.Trace2DLtd;
import info.monitorenter.util.Range;

public class PulseOxMonitorGui extends JPanel implements PulseOxListenerInterface {
	private static final long serialVersionUID = -67949939230864064L;
	private static final Logger logger = LogManager.getLogger(PulseOxMonitorGui.class);
	private static final DataFormatType DEFAULT_DATA_FORMAT = DataFormatType.DATA_FORMAT_02;
	
	public static void main(String[] args) {
		// On Linux: /dev/rfcomm0
		// On Windows: COMxx
		String serial_port;
		if (args.length == 0) {
			logger.error("Error: Usage: " + PulseOxMonitorGui.class.getName() + " <serial-port-name> [data format]");
			System.exit(1);
		}
		serial_port = args[0];
		
		DataFormatType data_format;
		if (args.length > 1) {
			data_format = DataFormatType.forValue(Short.parseShort(args[1]));
		} else {
			data_format = DEFAULT_DATA_FORMAT;
		}
		
		final PulseOxMonitorGui gui = new PulseOxMonitorGui(data_format, serial_port);
		//gui.set
		
		JFrame frame = new JFrame("Heart Rate");
		frame.setLayout(new BorderLayout());
		frame.getContentPane().add(gui, BorderLayout.CENTER);
		frame.setSize(800, 600);
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				logger.info("windowClosing()");
				gui.stop();
				System.exit(1);
			}
		});
		frame.setVisible(true);
		
		gui.start();
	}

	private PulseOxMonitorInterface pulseOxMonitor;

	private Chart2D spo2Chart;
	private ITrace2D spo2Trace;
	private int spo2Point;
	private JLabel spo2Value;
	
	private Chart2D heartRateChart;
	private ITrace2D heartRateTrace;
	private int heartRatePoint;
	private JLabel heartRateValue;
	
	private Chart2D plethmographChart;
	private ITrace2D plethmographTrace;
	private int plethmographPoint;
	
	public PulseOxMonitorGui(DataFormatType dataFormat, String serialPortName) {
		pulseOxMonitor = new Nonin9560PulseOxJssc(dataFormat, serialPortName, this);
		
		createModels();
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		setBackground(Color.BLACK);
		
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.LINE_AXIS));
		panel.add(spo2Chart);
		panel.add(spo2Value);
		panel.setAlignmentX(Component.LEFT_ALIGNMENT);
		panel.setBackground(Color.BLACK);
		add(panel);
		
		add(Box.createRigidArea(new Dimension(0, 10)));
		
		panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.LINE_AXIS));
		panel.add(heartRateChart);
		panel.add(heartRateValue);
		panel.setAlignmentX(Component.LEFT_ALIGNMENT);
		panel.setBackground(Color.BLACK);
		add(panel);
		
		add(Box.createRigidArea(new Dimension(0, 10)));
		
		plethmographChart.setAlignmentX(Component.LEFT_ALIGNMENT);
		add(plethmographChart);
	}
	
	private void createModels() {
		spo2Chart = new Chart2D();
		spo2Chart.setUseAntialiasing(true);
		spo2Chart.setBackground(Color.BLACK);
		spo2Chart.setForeground(Color.GREEN);
		spo2Chart.setGridColor(Color.RED);
		spo2Chart.getAxisY().setRangePolicy(new RangePolicyFixedViewport(new Range(70, 100)));
		spo2Trace = new Trace2DLtd(1000, "SpO2 (%)");
		spo2Trace.setColor(Color.CYAN);
		spo2Chart.addTrace(spo2Trace);
		spo2Value = new JLabel("100%");
		spo2Value.setBackground(Color.BLACK);
		spo2Value.setForeground(Color.CYAN);
		spo2Value.setFont(spo2Value.getFont().deriveFont(50f));
		
		heartRateChart = new Chart2D();
		heartRateChart.setUseAntialiasing(true);
		heartRateChart.setBackground(Color.BLACK);
		heartRateChart.setForeground(Color.GREEN);
		heartRateChart.setGridColor(Color.RED);
		//heartRateChart.getAxisY().setRangePolicy(new RangePolicyFixedViewport(new Range(0, 250)));
		heartRateTrace = new Trace2DLtd(1000, "Heart Rate");
		heartRateTrace.setColor(Color.YELLOW);
		heartRateChart.addTrace(heartRateTrace);
		heartRateValue = new JLabel("   ");
		heartRateValue.setBackground(Color.BLACK);
		heartRateValue.setForeground(Color.YELLOW);
		heartRateValue.setFont(heartRateValue.getFont().deriveFont(50f));
		
		plethmographChart = new Chart2D();
		plethmographChart.setUseAntialiasing(true);
		plethmographChart.setBackground(Color.BLACK);
		plethmographChart.setForeground(Color.GREEN);
		plethmographChart.setGridColor(Color.RED);
		//plethmographChart.getAxisY().setRangePolicy(new RangePolicyFixedViewport(new Range(0, 256)));
		plethmographTrace = new Trace2DLtd(1000, "Plethmograph");
		plethmographTrace.setColor(Color.GREEN.brighter().brighter());
		plethmographChart.addTrace(plethmographTrace);
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
					/*
					case 0:
						pulseOxInterface.sendMessage(new NoninGetTimeRequest());
						break;
					case 1:
						pulseOxInterface.sendMessage(new NoninSetTimeRequest());
						break;
					case 2:
						pulseOxInterface.sendMessage(new NoninGetTimeRequest());
						break;
					*/
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

	protected void stop() {
		pulseOxMonitor.stop();
	}

	@Override
	public void messageReceived(NoninResponseInterface response) {
		if (response instanceof DataFormat2Frame) {
			DataFormat2Frame frame = (DataFormat2Frame)response;
			plethmographTrace.addPoint(plethmographPoint++, frame.getPlethmograph());
		} else if (response instanceof DataFormat2Packet) {
			DataFormat2Packet packet = (DataFormat2Packet)response;
			heartRateTrace.addPoint(heartRatePoint++, packet.getHeartRateDisplay());
			heartRateValue.setText(Integer.toString(packet.getHeartRateDisplay()));
			spo2Trace.addPoint(spo2Point++, packet.getSpO2Display());
			spo2Value.setText(Integer.toString(packet.getSpO2Display()) + "%");
		} else {
			//logger.info("Got message: " + response);
		}
	}
}
