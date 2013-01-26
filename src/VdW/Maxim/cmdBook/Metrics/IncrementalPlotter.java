package VdW.Maxim.cmdBook.Metrics;

import java.util.concurrent.atomic.AtomicInteger;

import VdW.Maxim.cmdBook.Metrics.Metrics.Plotter;

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
