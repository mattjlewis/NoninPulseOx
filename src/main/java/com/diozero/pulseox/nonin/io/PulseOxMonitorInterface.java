package com.diozero.pulseox.nonin.io;

import com.diozero.pulseox.nonin.data.NoninRequestInterface;

public interface PulseOxMonitorInterface {
	public void start();
	public void stop();
	public void sendMessage(NoninRequestInterface request);
	public boolean isConnected();
	public boolean isClearToSend();
}
