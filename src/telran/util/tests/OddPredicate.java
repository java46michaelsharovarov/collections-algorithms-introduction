package telran.util.tests;

import java.util.function.Predicate;

public class OddPredicate implements Predicate<Integer> {

	@Override
	public boolean test(Integer t) {		
		return t % 2 != 0;
	}

}