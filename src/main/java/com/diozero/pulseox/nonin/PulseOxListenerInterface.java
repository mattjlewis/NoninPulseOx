package com.diozero.pulseox.nonin;

import com.diozero.pulseox.nonin.data.NoninResponseInterface;

public interface PulseOxListenerInterface {
	public void messageReceived(NoninResponseInterface response);
}
