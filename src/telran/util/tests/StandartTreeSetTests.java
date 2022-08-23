package telran.util.tests;

import telran.util.Collection;
import telran.util.StandardTreeSet;

public class StandartTreeSetTests extends SortedSetTests {

	@Override
	protected Collection<Integer> createCollection() {
		return new StandardTreeSet<Integer>();
	}

}
