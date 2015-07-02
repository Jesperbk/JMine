package dk.jbk.JMine;

import dk.jbk.JMine.controller.IntegerGenerator;

public class MockIntegerGenerator implements IntegerGenerator {
	int[] numbersToReturn;
	int indexOfNextNumber;

	public MockIntegerGenerator(int... numbersToReturn) {
		this.numbersToReturn = numbersToReturn;
		indexOfNextNumber = 0;
	}

	@Override
	public int getIntegerLessThan(int bound) {
		if (indexOfNextNumber < numbersToReturn.length) {
			return numbersToReturn[indexOfNextNumber++];
		}
		else {
			return 0;
		}
	}
}
