package telran.util.tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import telran.util.List;

abstract class ListTests extends CollectionTests {

	List<Integer> list;
	
	@BeforeEach
	void creatList() throws Exception {
		list = (List<Integer>) collection;
	}
	@Test
	void addIndexTest() {		
		assertTrue(list.add(0, 555));
		assertEquals(555, list.get(0));
		int expectedSize = list.size() + NUMBER_OF_ADD_ELEM;
		int index = 2;
		int count = NUMBER_OF_ADD_ELEM;
		while(count-- > 0) {
			list.add(index++, rand.nextInt(50));
		}
		assertEquals(expectedSize, list.size());
		assertTrue(list.add(list.size() - 1, 777));
		assertEquals(777, list.get(list.size() - 2));
		expectedSize = list.size();
		try {
			assertFalse(list.add(-1, 888));
			fail();
		} catch (IndexOutOfBoundsException e) {}
		assertEquals(expectedSize, list.size());		
	}
	
	@Test
	void removeIndexTest() {
		int[] arOfIndexes = {0, 2};
		for(int i = 0; i < arOfIndexes.length; i++) {
			int expected1 = list.get(arOfIndexes[i]);
			int expected2 = list.get(arOfIndexes[i] + 1);
			int expectedSize = list.size() - 1;
			assertEquals(expected1, list.remove(arOfIndexes[i]));
			assertEquals(expectedSize, list.size());
			assertEquals(expected2, list.get(arOfIndexes[i]));
		}	
		int expected1 = list.get(list.size() - 1);
		int expected2 = list.get(list.size() - 2);
		int expectedSize = list.size() - 1;
		assertEquals(expected1, list.remove(list.size() - 1));
		assertEquals(expectedSize, list.size());
		assertEquals(expected2, list.get(list.size() - 1));		
		try {
			assertNull(list.remove(-1));
			fail();
		} catch (IndexOutOfBoundsException e) {}
	}	
	
	@Test
	void indexOfTest() {
		list.add(3, 9);
		assertEquals(3, list.indexOf(9));
	}
	
	@Test
	void lastIndexOfTest() {
		list.add(9);
		assertEquals(list.size() - 1, list.lastIndexOf(9));
	}
	
	@Test
	void getTest() {
		assertEquals(0, list.get(0));
		try {
			assertNull(list.get(-1));
			fail();
		} catch (IndexOutOfBoundsException e) {}
	}
}