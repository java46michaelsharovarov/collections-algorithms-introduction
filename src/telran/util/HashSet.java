package telran.util;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class HashSet<T> extends AbstractCollection<T> implements Set<T> {
	
	private static final double DEFAULT_FACTOR = 0.75;
	private static final int DEFAULT_HASH_TABLE_CAPACITY = 16;
	private List<T> [] hashTable;
	private double factor;
	
	@SuppressWarnings("unchecked")
	public HashSet(int hashTableCapacity, double factor) {
		this.factor = factor;
		hashTable = new List[hashTableCapacity];
	}
	
	public HashSet(int hashTableCapacity) {
		this(hashTableCapacity, DEFAULT_FACTOR);
	}
	
	public HashSet() {
		this(DEFAULT_HASH_TABLE_CAPACITY, DEFAULT_FACTOR);
	}
	
	private class HashSetIterator implements Iterator<T> {
		int index = 0;
		Iterator<T> it;
		boolean wasNext = false;
		T next = getNext();
		T current;
		
		@Override
		public boolean hasNext() {
			return index < hashTable.length;
		}

		@Override
		public T next() {
			if(!hasNext()) {
				throw new NoSuchElementException();
			}
			wasNext = true;
			current = next;
			next = getNext();	
			return current;
		}
	
		private T getNext() {			
			for (int i = index; index < hashTable.length; i++, index++) {
				if (hashTable[i] != null && hashTable[i].size() > 0) {					
					if (it == null) {
						it = hashTable[i].iterator();
					}
					if (it.hasNext()) {
						return it.next();
					} else {
						it = null;						 
					}
				} 
			}
			return null;
		}
		
		@Override
		public void remove() {
			if(!wasNext) {
				throw new IllegalStateException();
			}
			HashSet.this.remove(current);
			wasNext = false;
		}
	}
	
	@Override
	public boolean add(T obj) {
		boolean res = false;
		if (!contains(obj)) {
			res = true;
			if (size >= hashTable.length * factor) {
				recreateHashTable();
			} 
			int hashTableInd = getHashTableIndex(obj.hashCode());
			if (hashTable[hashTableInd] == null) {
				hashTable[hashTableInd] = new LinkedList<T>();
			}
			hashTable[hashTableInd].add(obj);
			size++;
		}
		return res;
	}

	private void recreateHashTable() {
		HashSet<T> tmp = new HashSet<>(hashTable.length * 2);
		for (List<T> list: hashTable) {
			if (list != null) {
				for(T obj: list) {
					tmp.add(obj);
				}
			}
		}
		hashTable = tmp.hashTable;
	}

	private int getHashTableIndex(int hashCode) {
		int res = Math.abs(hashCode) % hashTable.length;
		return res;
	}

	@Override
	public boolean remove(Object pattern) {
		int index = getHashTableIndex(pattern.hashCode());
		boolean res = false;
		if (hashTable[index] != null) {
			res = hashTable[index].remove(pattern);
			if (res) {
				size--;
			}
		}
		return res;
	}

	@Override
	public boolean contains(Object pattern) {
		int index = getHashTableIndex(pattern.hashCode());
		boolean res = false;
		if (hashTable[index] != null) {
			res = hashTable[index].contains(pattern);
		}
		return res;
	}

	@Override
	public Iterator<T> iterator() {		
		return new HashSetIterator();
	}

}