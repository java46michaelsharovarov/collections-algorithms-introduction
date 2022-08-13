package telran.util.tests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Random;
import java.util.function.Predicate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import telran.util.Collection;

abstract class CollectionTests {
	protected final static int NUMBER_OF_ADD_ELEM = 100;
	private static final int N_RUNS = 1000;
	private static final int N_NUMBERS = 1000;
	private static final int N_RANDOM_RUNS = 10;
	private static final int N_RANDOM_NUMBERS = 10;
	protected Collection<Integer> collection;

	protected abstract Collection<Integer> createCollection();

	Integer[] expected = { 10, -5, 13, 20, 40, 15 };
	Random rand = new Random();
	int initialSize = 0;

	@BeforeEach
	void setUp() throws Exception {
		collection = createCollection();
		fillCollection();
	}

	private void fillCollection() {
		for (int elt : expected) {
			collection.add(elt++);
			initialSize++;
		}
	}

	@Test
	void addTest() {
		assertTrue(collection.add(100));
		assertTrue(collection.add(10));
		int expectedSize = collection.size() + NUMBER_OF_ADD_ELEM;
		int count = NUMBER_OF_ADD_ELEM;
		while (count-- > 0) {
			collection.add(rand.nextInt(50));
		}
		assertEquals(expectedSize, collection.size());
	}

	@Test
	void removeTest() {
		int expectedSize = collection.size() - 1;
		assertTrue(collection.remove(expected[0]));
		assertEquals(expectedSize, collection.size());
		assertFalse(collection.remove(expected[0]));
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
		Integer[] expected1 = { -5, 13, 15 };
		assertTrue(collection.removeIf(new EvenPredicate()));
		assertArrayEquals(expected1, collection.toArray(new Integer[0]));
		for (int i = 0; i < N_RANDOM_RUNS; i++) {
			fillRandomCollection();
			collection.removeIf(new EvenPredicate());
			for (int num : collection) {
				assertTrue(num % 2 != 0);
			}
		}
	}

	private void fillRandomCollection() {
		collection = createCollection();
		for (int i = 0; i < N_RANDOM_NUMBERS; i++) {
			collection.add((int) (Math.random() * Integer.MAX_VALUE));
		}
	}

	@Test
	void removeIfOddTest() {
		Integer[] expected1 = { 10, 20, 40 };
		assertTrue(collection.removeIf(new OddPredicate()));
		Integer[] res = collection.toArray(new Integer[0]);
		Arrays.sort(res);
		assertArrayEquals(expected1, res);
	}

	@Test
	void removeIfMultiplicityTest() {
		Integer[] expected1 = { -5, 10, 13, 20, 40 };
		assertTrue(collection.removeIf(new MultiplicityOfThreePredicate()));
		Integer[] res = collection.toArray(new Integer[0]);
		Arrays.sort(res);
		assertArrayEquals(expected1, res);
	}

	@Test
	void containTest() {
		assertTrue(collection.contains(10));
		assertFalse(collection.contains(1000));
	}

	@Test
	void sizeTest() {
		assertEquals(initialSize, collection.size());
	}

	@Test
	void toArrayTest() {
		Integer[] expected2 = new Integer[100];
		assertArrayEquals(expected, collection.toArray(new Integer[0]));
		assertTrue(expected == collection.toArray(expected));
		assertTrue(expected2 == collection.toArray(expected2));
		assertArrayEquals(expected, Arrays.copyOf(expected2, collection.size()));
		for (int i = collection.size(); i < expected2.length; i++) {
			assertNull(expected2[i]);
		}
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
		for (int i = 0; i < N_NUMBERS; i++) {
			collection.add(i);
		}

	}

}