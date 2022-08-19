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

	private static final String FILL_SYMBOL = " ";
	private static final int N_SYMBOLS_PER_LEVEL = 3;

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
		//no cycles allowed
		Node<T> newNode = new Node<>(obj);
		boolean res = add(root, newNode);
		if (res) {
			size++;
		}
		return res;
	}

	private boolean add(Node<T> parent, Node<T> newNode) {
		boolean res = true;
		if (parent == null) {
			root = newNode;
		} else {
			int resComp = comp.compare(newNode.obj, parent.obj);
			if (resComp == 0) {
				res = false;
			} else {
				if (resComp < 0) {
					if (parent.left == null) {
						insert(parent, newNode, true);//new node inserted to left from parent
					} else {
						add(parent.left, newNode);
					}
				} else {
					if (parent.right == null) {
						insert(parent, newNode, false);//new node inserted to right from parent
					} else {
						add(parent.right, newNode);
					}
				}
			}
		}
		return res;
	}
	
	private void insert(Node<T> parent, Node<T> newNode, boolean isLeft) {
		if (isLeft) {
			parent.left = newNode;
		} else {
			parent.right = newNode;
		}
		newNode.parent = parent;		
	}

//	@Override
//	public boolean add(T obj) {
//		Node<T> parent = getNodeOrParent(obj);
//		boolean res = false;
//		int compRes = 0;
//		if (parent == null || (compRes = comp.compare(obj, parent.obj)) != 0) {
//			Node<T> newNode = new Node<>(obj);
//			if (parent == null) {
//				root = newNode;
//			} else if (compRes > 0) {
//				parent.right = newNode;
//			} else {
//				parent.left = newNode;
//			}
//			res = true;
//			newNode.parent = parent;
//			size++;
//		}
//		return res;
//	}

	@Override
	public boolean remove(Object pattern) {
		@SuppressWarnings("unchecked")
		Node<T> node  = getNodeOrParent((T) pattern);		
		if (node != null && node.obj.equals(pattern)) {
			removeNode(node);
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
		size--; 
	}

	private void removeNonJunctionNode(Node<T> node) {	
		Node<T> parent = node.parent;
		Node<T> child = node.right != null ? node.right : node.left;
		if(parent != null) {
			if(node == parent.right) {
				parent.right = child;				
			} else {
				parent.left = child;				
			}
		} else {
			root = child;
		}	
		if (child != null) {
			child.parent = parent;
		}
	}

	private void removeJunctionNode(Node<T> node) {
		Node<T> tmp = getLeastNodeFrom(node.right);
		node.obj = tmp.obj;
		removeNonJunctionNode(tmp);
//		node.right = tmp.right;
//		if(tmp.right != null) {
//			tmp.right.parent = node;
//		}
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

		@Override
		public boolean hasNext() {
			return current != null;
		}

		@Override
		public T next() {
			if (!hasNext()) {
				throw new NoSuchElementException();
			}
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
			if(prev == null) {
				throw new IllegalStateException();
			}
			if (isJunction(prev)) {
				current = prev;
			}
			removeNode(prev);
			prev = null;
		}

	}	

	public void displayRotated() {
		displayRotated(root, 0);
	}
	private void displayRotated(Node<T> root, int level) {
		if (root != null) {
			displayRotated(root.right, level + 1);
			displayRoot(root, level);
			displayRotated(root.left, level + 1);
		}
		
	}
	private void displayRoot(Node<T> root, int level) {
		System.out.printf("%s%s\n",FILL_SYMBOL.repeat(level * N_SYMBOLS_PER_LEVEL), root.obj);
		
	}
	
	public void displayAsDirectory() {
		displayAsDirectory(root, 0);
	}
	private void displayAsDirectory(Node<T> root, int level) {
		if (root == null) return;
		displayRoot(root, level);
		displayAsDirectory(root.left, level + 1);
		displayAsDirectory(root.right, level + 1);
	}

	public int height() {
		return height(root);
	}
	private int height(Node<T> root) {
		int res = 0;
		if (root != null) {
			int heightLeft = height(root.left);
			int heightRight = height(root.right);
			res = Math.max(heightLeft, heightRight) + 1;
		}
		return res;
	}
	
	public int width() {		
		return width(root);
	}
	private int width(Node<T> root) {
		int res = 0;
		if (root != null) {
			res = root.left == null && root.right == null
					? 1 
					: width(root.left) + width(root.right);				
		}
		return res;
	}
	/**
	 * tree inversion -  swap of left and right subtrees
	 */
	public void inversion() {
		inversion(root);
	}

	private void inversion(Node<T> root) {
		if (root == null) return;
		swap(root);
		inversion(root.left);
		inversion(root.right);		
	}

	private void swap(Node<T> root) {
		Node<T> tmp = root.left;
		root.left = root.right;
		root.right = tmp;		
	}
	

}