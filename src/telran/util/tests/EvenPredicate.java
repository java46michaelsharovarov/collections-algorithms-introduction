package telran.util.tests;

import java.util.function.Predicate;

public class EvenPredicate implements Predicate<Integer> {

	@Override
	public boolean test(Integer i) {
		return i % 2 == 0;
	}

}
