package be.maximvdw.cmdBook.metrics;

import java.util.concurrent.atomic.AtomicInteger;

import be.maximvdw.cmdBook.metrics.Metrics.Plotter;

public class IncrementalPlotter extends Plotter {

	private final AtomicInteger value = new AtomicInteger(0);

	public IncrementalPlotter(final String name) {
		super(name);
	}
	
	@Override
	public int getValue() {
		return value.get();
	}

	public void increment() {
		value.incrementAndGet();
	}

}
