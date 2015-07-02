package dk.jbk.JMine;


import java.util.Random;

public class LibraryIntegerGenerator implements IntegerGenerator {
	private Random randomGenerator;

	public LibraryIntegerGenerator() {
		this.randomGenerator = new Random();
	}

	@Override
	public int getIntegerLessThan(int bound) {
		return this.randomGenerator.nextInt(bound);
	}
}
