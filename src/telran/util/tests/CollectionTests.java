package telran.util.tests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Random;
import java.util.function.Predicate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import telran.util.Collection;

abstract class  CollectionTests {
	protected final static int NUMBER_OF_ADD_ELEM = 100;
	private static final int N_RUNS = 10000;
	private static final int N_NUMBERS = 10000;
	protected Collection<Integer> collection;
	protected abstract Collection<Integer> createCollection();
	Integer[] array = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15};
	Random rand = new Random();
	int initialSize = 0;
	@BeforeEach
	void setUp() throws Exception {
		collection = createCollection();
		fillCollection();
	}

	private void fillCollection() {
		for(int elt: array) {
			collection.add(elt++);
			initialSize++;
		}
	}

	@Test
	void addTest() {
		assertTrue(collection.add(16));
		assertTrue(collection.add(1));
		int expectedSize = collection.size() + NUMBER_OF_ADD_ELEM;
		int count = NUMBER_OF_ADD_ELEM;
		while(count-- > 0) {
			collection.add(rand.nextInt(50));
		}
		assertEquals(expectedSize, collection.size());
	}	

	@Test
	void removeTest() {
		int expectedSize = collection.size() - 1;
		assertTrue(collection.remove(15));
		assertEquals(expectedSize, collection.size());
		assertFalse(collection.remove(333));
		assertEquals(expectedSize, collection.size());
	}
	
	@Test
	void removeIfAllTrueAndAllFalseTest() {		
		int expectedSize = collection.size();
		Predicate<Integer> AllFalsePredicate = new AllFalsePredicate();
		assertFalse(collection.removeIf(AllFalsePredicate));
		assertEquals(expectedSize, collection.size());
		assertTrue(collection.removeIf(AllFalsePredicate.negate()));
		assertEquals(0, collection.size());
	}
	@Test
	void removeIfEvenTest() {
		Integer[] expected1 = {1, 3, 5, 7, 9, 11, 13, 15};
		assertTrue(collection.removeIf(new EvenPredicate()));
		assertArrayEquals(expected1, collection.toArray(new Integer[0]));
	}
	
	@Test
	void removeIfOddTest() {
		Integer[] expected1 = {0, 2, 4, 6, 8, 10, 12, 14};
		assertTrue(collection.removeIf(new OddPredicate()));
		assertArrayEquals(expected1, collection.toArray(new Integer[0]));
	}
	
	@Test
	void removeIfMultiplicityTest() {
		Integer[] expected1 = {1, 2, 4, 5, 7, 8, 10, 11, 13, 14};
		assertTrue(collection.removeIf(new MultiplicityOfThreePredicate()));
		assertArrayEquals(expected1, collection.toArray(new Integer[0]));
	}
	
	@Test
	void containTest() {
		assertTrue(collection.contains(15));
		assertFalse(collection.contains(333));
	}

	@Test
	void sizeTest() {
		assertEquals(initialSize, collection.size());
	}
	@Test
	void toArrayTest() {
		Integer[] expected1 = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15};
		Integer [] expected2 = new Integer[100];
		assertArrayEquals(expected1, collection.toArray(new Integer[0]));
		assertTrue(expected1 == collection.toArray(expected1));
		assertTrue(expected2 == collection.toArray(expected2));
		assertArrayEquals(expected1, Arrays.copyOf(expected2, collection.size()));
	}
	
	@Test
	void wrongRemoveIteratorTest() {
		Iterator<Integer> it = collection.iterator();
		wrongRemove(it);
		it.next();
		it.next();
		it.remove();
		wrongRemove(it);
	}

	private void wrongRemove(Iterator<Integer> it) {
		boolean wasExeption = false;
		try {
			it.remove();
		} catch (IllegalStateException e) {
			wasExeption = true;
		}
		assertTrue(wasExeption);
	}
	
	@Test
	void removeIfPerformanceTest() {
		Predicate<Integer> predicate = new AllFalsePredicate().negate();
		for (int i = 0; i < N_RUNS; i++) {
			fillLargeCollection();
			collection.removeIf(predicate);
		}
	}
	private void fillLargeCollection() {
		for(int i = 0; i < N_NUMBERS; i++) {
			collection.add(i);
		}
		
	}

}