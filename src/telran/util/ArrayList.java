package telran.util;

import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Predicate;

public class ArrayList<T> extends AbstractCollection<T> implements List<T> {
	static final int DEFAULT_CAPACITY = 16;
	private T[] array;
	@SuppressWarnings("unchecked")	
	public ArrayList(int capacity) {
		array = (T[]) new Object[capacity];
	}
	
	public ArrayList() {
		this(DEFAULT_CAPACITY);
	}
	
	@Override
	public boolean add(T obj) {
		ensureCapacity();
		array[size++] = obj;
		return true;
	}

	@Override
	public boolean remove(Object pattern) {
		int index = indexOf(pattern);
		if(index < 0) {
			return false;
		}
		removeOneElement(index);
		return true;
	}
	
	@Override
	public boolean add(int index, T obj) {
		if(!checkExistingIndex(index)) {
			return false;
		}
		ensureCapacity();
		System.arraycopy(array, index, array, index + 1, (size++) - index);
		array[index] = obj;
		return true;
	}

	@Override
	public T remove(int index) {
		if(!checkExistingIndex(index)) {
			return null;
		}
		T res = array[index];
		removeOneElement(index);
		return res;
	}

	@Override
	public int lastIndexOf(Object pattern) {
		for(int i = size - 1; i >= 0; i--) {
			if(array[i].equals(pattern)) {
				return i;
			}
		}
		return -1;
	}
	
	@Override
	public T get(int index) {		
		return checkExistingIndex(index) ? array[index] : null;
	}
	
	@Override
	public boolean removeIf(Predicate<T> predicate) {
		int prevSize = size();
		int  index = 0;
		for(int i = 0; i < size; i++) {
			if(predicate.test(array[i])) {
				array[i] = null;
			} else {
				if(i != index) {
					T tmp = array[i];
					array[i] = null;
					array[index] = tmp;
				}
				index++;
			}
		}
		size = index;
		return size < prevSize;
	}

	@Override
	public Iterator<T> iterator() {		
		return new ArrayListIterator();
	}

	private class ArrayListIterator implements Iterator<T> {
		int next = 0;
		boolean wasNext = false;
		
		@Override
		public boolean hasNext() {
			return next < size;
		}
	
		@Override
		public T next() {
			if (!hasNext()) {
				throw new NoSuchElementException();
			}
			wasNext = true;
			return array[next++];
		}
		
		@Override
		public void remove() {
			if(!wasNext) {
				throw new IllegalStateException();
			}
			ArrayList.this.remove(--next);
			wasNext = false;
		}		
	}

	private void removeOneElement(int i) {
		System.arraycopy(array, i + 1, array, i, (size--) - (i + 1));
		array[size] = null;
	}

	private void ensureCapacity() {
		if (array.length == size) {
			array = Arrays.copyOf(array, size * 2);
		}
	}

}