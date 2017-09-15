package com.diozero.pulseox.nonin.data;

public enum PerfusionType {
	NONE,
	/** Amplitude representation of high signal quality (occurs only during pulse) */
	GREEN,
	/** Amplitude representation of low/poor signal quality (occurs only during pulse) */
	RED,
	/** Amplitude representation of low/marginal signal quality (occurs only during pulse) */
	YELLOW
}
