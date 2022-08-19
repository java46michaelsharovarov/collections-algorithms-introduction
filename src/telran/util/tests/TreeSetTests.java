package telran.util.tests;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import telran.util.Collection;
import telran.util.TreeSet;

public class TreeSetTests extends SetTests {
    TreeSet<Integer> tree;
	@Override
	protected Collection<Integer> createCollection() {
		
		return new TreeSet<Integer>();
	}
	
	@Override
	@BeforeEach
	void setUp() throws Exception {
		super.setUp();
		tree = (TreeSet<Integer>)collection;
	}
	
	@Test
	@Override
	void toArrayTest() {
		Arrays.sort(expected);
		super.toArrayTest();
	}
	
	@Test
	void firstTest() {
		assertEquals((Integer)(-5), tree.first());
	}
	
	@Test
	void lastTest() {
		assertEquals((Integer)(40), tree.last());
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
		/*
		   10
		     -5
		     13
		       20
		         15
		         40
		 */
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
			tree.add(elt++);
		}
	}

}