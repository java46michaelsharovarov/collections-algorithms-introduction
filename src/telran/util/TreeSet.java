package telran.util;

import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class TreeSet<T> implements SortedSet<T> {

	private static class Node<T> {
		T obj;
		Node<T> parent;
		Node<T> left;
		Node<T> right;

		Node(T obj) {
			this.obj = obj;
		}
	}

	private Node<T> root;
	int size;
	Comparator<T> comp;

	public TreeSet(Comparator<T> comp) {
		this.comp = comp;
	}

	@SuppressWarnings("unchecked")
	public TreeSet() {
		this((Comparator<T>) Comparator.naturalOrder());
	}

	@Override
	public boolean add(T obj) {
		Node<T> parent = getNodeOrParent(obj);
		boolean res = false;
		int compRes = 0;
		if (parent == null || (compRes = comp.compare(obj, parent.obj)) != 0) {
			Node<T> newNode = new Node<>(obj);
			if (parent == null) {
				root = newNode;
			} else if (compRes > 0) {
				parent.right = newNode;
			} else {
				parent.left = newNode;
			}
			res = true;
			newNode.parent = parent;
			size++;
		}
		return res;
	}

	@Override
	public boolean remove(Object pattern) {
		@SuppressWarnings("unchecked")
		Node<T> node  = getNodeOrParent((T) pattern);		
		if (node != null && node.obj.equals(pattern)) {
			removeNode(node);
			size--; 
			return true;
		}
		return false;		
	}
	
	private void removeNode(Node<T> node) {
		if (isJunction(node)) {
			removeJunctionNode(node);
		} else {
			removeNonJunctionNode(node);
		}
	}

	private void removeNonJunctionNode(Node<T> node) {	
		Node<T> parentNode = node.parent;
		if(parentNode != null) {
			if(node == parentNode.right) {
				parentNode.right = nextNode(node);
				if (parentNode.right != null) {
					parentNode.right.parent = parentNode;
				}
			} else {
				parentNode.left = nextNode(node);
				if (parentNode.left != null) {
					parentNode.left.parent = parentNode;
				}
			}
		} else {
			root = nextNode(node);
			if (root != null) {
				root.parent = null;
			}
		}		
	}

	private Node<T> nextNode(Node<T> node) {
		return node.right != null ? node.right : node.left;
	}

	private void removeJunctionNode(Node<T> node) {
		Node<T> tmp = getLeastNodeFrom(node.right);
		node.obj = tmp.obj;
		node.right = tmp.right;
		if(tmp.right != null) {
			tmp.right.parent = node;
		}
	}

	private boolean isJunction(Node<T> node) {
		if(node.left != null && node.right != null ) {
			return true;
		}
		return false;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean contains(Object pattern) {
		Node<T> current = root;
		int compRes = 0;
		while (current != null) {
			compRes = comp.compare((T) pattern, current.obj);
			if (compRes == 0) {
				return true;
			}
			current = compRes > 0 ? current.right : current.left;
		}
		return false;
	}

	@Override
	public int size() {
		return size;
	}

	@Override
	public Iterator<T> iterator() {
		return new TreeSetIterator();
	}

	@Override
	public T first() {
		if (root == null) {
			return null;
		}
		return getLeastNodeFrom(root).obj;
	}

	@Override
	public T last() {
		if (root == null) {
			return null;
		}
		return getMostNodeFrom(root).obj;
	}

	private Node<T> getNodeOrParent(T obj) {
		Node<T> current = root;
		Node<T> parent = null;
		int compRes = 0;
		while (current != null) {
			parent = current;
			compRes = comp.compare(obj, current.obj);
			if (compRes == 0) {
				break;
			}
			current = compRes > 0 ? current.right : current.left;
		}
		return parent;
	}

	private Node<T> getLeastNodeFrom(Node<T> node) {
		while (node.left != null) {
			node = node.left;
		}
		return node;
	}
	
	private Node<T> getMostNodeFrom(Node<T> node) {
		while (node.right != null) {
			node = node.right;
		}
		return node;
	}
	
	private class TreeSetIterator implements Iterator<T> {
		Node<T> current = root == null ? null : getLeastNodeFrom(root);
		Node<T> prev;
		boolean wasNext = false;

		@Override
		public boolean hasNext() {
			return current != null;
		}

		@Override
		public T next() {
			if (!hasNext()) {
				throw new NoSuchElementException();
			}
			wasNext = true;
			prev = current;
			updateCurrent();
			return prev.obj;
		}

		private void updateCurrent() {
			current = current.right != null ? getLeastNodeFrom(current.right) : getGreaterParent(current);
		}

		private Node<T> getGreaterParent(Node<T> node) {
			while (node.parent != null && node.parent.left != node) {
				node = node.parent;
			}
			return node.parent;
		}

		@Override
		public void remove() {
			if(!wasNext) {
				throw new IllegalStateException();
			}
			TreeSet.this.remove(prev.obj);
			wasNext = false;
		}

	}

}