package telran.util;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class LinkedList<T> implements List<T> {
	
	private static class Node<T> {
		T obj;
		Node<T> next;
		Node<T> prev;
		Node(T obj) {
			this.obj = obj;
		}
	}
	
	private Node<T> head;
	private Node<T> tail;
	private int size;

	@Override
	public boolean add(T obj) {
		Node<T> newNode = new Node<>(obj);
		if (head == null) {
			head = tail = newNode;
		} else {
			newNode.prev = tail;
			tail.next = newNode;
			tail = newNode;
		}
		size++;
		return true;
	}

	@Override
	public boolean remove(Object pattern) {
		int index = indexOf(pattern);
		if(index < 0) {
			return false;
		}
		removeNode(getNodeByIndex(index));
		return true;
	}
	
	@Override
	public int size() {		
		return size;
	}

	@Override
	public boolean add(int index, T obj) {
		if (index < 0 || index > size) {
			return false;
		}
		if (index == size) {
			add(obj);
		} else if (index == 0) {
			addHead(obj);
		} else {
			addByIndex(index, obj);
		}
		return true;
	}

	@Override
	public T remove(int index) {
		if (checkExistingIndex(index)) {
			Node<T> current = getNodeByIndex(index);
				removeNode(current);
				return current.obj;
		}
		return null;
	}

	@Override
	public int indexOf(Object pattern) {	
		Node<T> current = head;
		for(int i = 0; i < size; i++) {
			if(current.obj.equals(pattern)) {
				return i;
			}
			current = current.next;
		}
		return -1;
	}

	@Override
	public int lastIndexOf(Object pattern) {
		Node<T> current = tail;
		for(int i = size - 1; i >= 0; i--) {
			if(current.obj.equals(pattern)) {
				return i;
			}
			current = current.prev;
		}
		return -1;
	}

	@Override
	public T get(int index) {
		return checkExistingIndex(index) ? getNodeByIndex(index).obj : null;
	}

	@Override
	public Iterator<T> iterator() {
		return new LinkedListIterator();
	}
	
	public void reverse() {
		Node<T> current = head;
		Node<T> tmp = null;
		for(int i = 0; i < size - 1; i++) {
			if(current == head) {
				head.prev = head.next;
				tail.next = tail.prev;
				head.next = tail.prev = null;
				tmp = head;
				head = tail;
				tail = tmp;
			} else {
				tmp = current.next;
				current.next = current.prev;
				current.prev = tmp;
			}
			current = current.prev;
		}
	}	
	
	private class LinkedListIterator implements Iterator<T> {
		Node<T> current = head;
		boolean wasNext = false;
		
		@Override
		public boolean hasNext() {
			return current!= null;
		}

		@Override
		public T next() {
			if(!hasNext()) {
				throw new NoSuchElementException();
			}
			wasNext = true;
			T currentObj = current.obj;
			current = current.next;
			return currentObj;
		}	
		
		@Override
		public void remove() {
			if(!wasNext) {
				throw new IllegalStateException();
			}
			if(current == null) {
				removeNode(tail);
			} else {
				removeNode(current.prev);
			}
			wasNext = false;
		}
	}

	private void addByIndex(int index, T obj) {
		Node<T> newNode = new Node<>(obj);
		Node<T> afterNode = getNodeByIndex(index);
		Node<T> beforeNode = afterNode.prev;
		newNode.next = afterNode;
		afterNode.prev = newNode;
		beforeNode.next = newNode;
		newNode.prev = beforeNode;
		size++;
	}

	private Node<T> getNodeByIndex(int index) {		
		return index > size / 2 ? getNodeRightToLeft(index) : getNodeLeftToRight(index);
	}

	private Node<T> getNodeLeftToRight(int index) {
		Node<T> current = head;
		for (int i = 0; i < index; i++) {
			current = current.next;
		}
		return current;
	}

	private Node<T> getNodeRightToLeft(int index) {
		Node<T> current = tail;
		for (int i = size - 1; i > index; i--) {
			current = current.prev;
		}
		return current;
	}

	private void addHead(T obj) {
		size++;
		Node<T> newNode = new Node<>(obj);
		newNode.next = head;
		head.prev = newNode;
		head = newNode;		
	}

	private boolean checkExistingIndex(int index) {		
		return index >= 0 && index < size;
	}

	private void removeNode(Node<T> current) {
		if(current == head) {
			removeHead();
		} else if (current == tail) {
			removeTail();
		} else {
			removeMiddle(current);
		}
		size--;
	} 
	
	private void removeMiddle(Node<T> current) {
		Node<T> afterNode = current.next;
		Node<T> beforeNode = current.prev;
		beforeNode.next = afterNode;
		afterNode.prev = beforeNode;
	}

	private void removeTail() {
		tail = tail.prev;
		tail.next = null;
	}

	private void removeHead() {
		if (head == tail) {
			head = tail = null;
		} else {
			head = head.next;
			head.prev = null;
		}
	}

}