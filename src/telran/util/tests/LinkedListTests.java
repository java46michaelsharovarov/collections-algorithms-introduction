package telran.util.tests;

import telran.util.Collection;
import telran.util.LinkedList;

public class LinkedListTests extends ListTests {

	@Override
	protected Collection<Integer> createCollection() {
		return new LinkedList<>();
	}

}
