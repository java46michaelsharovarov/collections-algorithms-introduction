package telran.util.tests;

import static org.junit.Assert.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import telran.util.Collection;
import telran.util.TreeSet;

public class TreeSetTests extends SortedSetTests {
    TreeSet<Integer> tree;
	@Override
	protected Collection<Integer> createCollection() {		
		return new TreeSet<Integer>();
	}
	
	int index = 0;
	@Override
	protected void orderLargeArray() {
		Integer tmp[] = new Integer[largeArray.length];
		index = 0;
		orderLargeArray(tmp, 0, largeArray.length - 1);
		largeArray = tmp;
	}
	private void orderLargeArray(Integer[] tmp, int left, int right) {
		if (left <= right) {
			int middle = (left + right) / 2;
			tmp[index++] = largeArray[middle];
			orderLargeArray(tmp, left, middle - 1);
			orderLargeArray(tmp, middle + 1, right);
		}		
	}
	
//	@Override
//	protected void orderLargeArray() {
//		Integer tmp[] = {0,1,2,3,4};
//		index = 0;
//		orderLargeArray(tmp, 0, tmp.length - 1);
//		largeArray = tmp;
//	}
//	private void orderLargeArray(Integer[] tmp, int left, int right) {
//		if (left <= right) {
//			int middle = (left + right) / 2;
//			tmp[index++] = largeArray[middle];
//			orderLargeArray(tmp, left, middle - 1);
//			orderLargeArray(tmp, middle + 1, right);
//		}		
//	}
	
	@Override
	@BeforeEach
	void setUp() throws Exception {
		super.setUp();
		tree = (TreeSet<Integer>)collection;
	} 
	
	@Test
	void displayRotatedTest() {
		System.out.println("*".repeat(10));
		tree.displayRotated();
		System.out.println("*".repeat(10));
	}
	
	@Test
	void displayDirectoryTest() {
		Integer[] expected = { 10, -5, 13, 20, 40, 15, 2, -6, 80, -8, -4, 1, 3 };
		TreeSet<Integer> treeTest = (TreeSet<Integer>) createCollection();	
		fillCollection(expected, treeTest);			
		System.out.println("*".repeat(10));
		treeTest.displayAsDirectory();
		System.out.println("*".repeat(10));
	}
	
	@Test
	void heightTest() {
		assertEquals(4, tree.height());
	}
	
	@Test
	void widthTest() {
		assertEquals(3, tree.width());
	}
	
	@Test
	void inversionTest() {
		tree.inversion();
		Integer expected[] = {40, 20, 15, 13, 10, -5};
		assertArrayEquals(expected, tree.toArray(new Integer[0]));
		containTest();
		Integer[] array = { -8, -5, -2, 0, 2, 8, 9, 15, 17, 50, 89, 102, 200 };
		TreeSet<Integer> treeTest = (TreeSet<Integer>) createCollection();	
		fillCollection(array, treeTest);
		Integer[] expectedArray = { 200, 102, 89, 50, 17, 15, 9, 8, 2, 0, -2, -5, -8 };
		treeTest.inversion();
		assertArrayEquals(expectedArray, treeTest.toArray(new Integer[0]));
	}

	private void fillCollection(Integer[] array, TreeSet<Integer> tree) {
		for (int elt : array) {
			tree.add(elt);
		}
	}
	
	@Test
	void balanceTest() {
		Integer[] array = new Integer[63];
		fillArraySequence(array);
		collection = new TreeSet<>();
		tree = (TreeSet<Integer>)collection;
		fillCollection(array);
		assertEquals(63, tree.size());
		assertEquals(63, tree.height());
		assertEquals(1, tree.width());
		tree.balance();
		assertEquals(6, tree.height());
		assertEquals(32, tree.width());
		assertArrayEquals(array, tree.toArray(new Integer[0]));		
	}

}