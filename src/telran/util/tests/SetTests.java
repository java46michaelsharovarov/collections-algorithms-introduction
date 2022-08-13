package telran.util.tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public abstract class SetTests extends CollectionTests {

	@Test
	@Override
	void addTest() {
		assertTrue(collection.add(100));
		assertFalse(collection.add(10));
		int expectedSize = collection.size() + NUMBER_OF_ADD_ELEM;
		for (int i = 0; i < NUMBER_OF_ADD_ELEM; i++) {
			collection.add(101 + i);
		}
		assertEquals(expectedSize, collection.size());
	}	

}