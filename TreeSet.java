package edu.uwm.cs351;

import java.util.AbstractList;
import java.util.AbstractSet;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

//NOTES ABOUT THIS ASSIGNMENT

//referenced stack overflow and youTube for some design.
//Referenced HW9 and HW10 mostly
//Asked Kayla about computing the currentIndex in removerange.
//I was able to solve it on my own before she got back to me.
//test etc

public class TreeSet<T> extends AbstractSet<T>{

	private static class Node<T>
	{
		private T value;
		private Node<T> left;
		private Node<T> right;
		private int size; //newly implemented size to keep track of parent node sizes

		public Node(T t)
		{
			value = t;
			left = right = null; //hw10
			size = 1;
		}

		public T getValue() //used to get values
		{
			return value;
		}

		public void setValue(T t)
		{
			value = t;
		}
		public int getSize()
		{
			return size;
		}
	}

	private Comparator<T> _comparator;
	private Node<T> _root;
	private SetList list; //stores the list eventually

	public TreeSet()
	{
		this(null); //Redundant code - refer to the next constructor
	}

	@SuppressWarnings("unchecked")
	public TreeSet(Comparator<T> c) //hw10
	{
		if (c == null)
		{
			_comparator = new Comparator<T>()
			{
				public int compare(T arg0, T arg1)
				{
					return ((Comparable<T>)arg0).compareTo(arg1);
				}
			};
		}
		else
		{
			_comparator = c;
		}
		_root = null;
		list = new SetList();

		assert wellFormed() : "Invariant broken after constructor(Comparator)";
	}

	private boolean report(String message)
	{
		System.err.println("Invariant error: " + message);
		return false;
	}

	public boolean wellFormed()
	{
		if (_comparator == null) return report("Null comparator");
		if (checkInRange(_root, null, null) == false) return report("Bad range");
		if (checkSize(_root) == false) return report("Wrong size");

		return true;
	}
	private boolean checkSize(Node<T> root) 
	{
		if (root == null) return true;
		int leftSide = size(root.left);
		int rightSide = size(root.right);

		if (leftSide + rightSide + 1 != size(root))
		{
			return false;
		}

		return checkSize(root.left) && checkSize(root.right);
	}

	private boolean checkInRange(Node<T> root, T lower, T upper) {
		if (root == null) return true;

		if (lower != null && _comparator.compare(root.getValue(), lower) <= 0)
		{
			return report("Order wrong!: " + size(root));
		}

		if (upper != null && _comparator.compare(root.getValue(), upper) >= 0)
		{
			return report("Order wrong!: " + size(root));
		}
		return checkInRange(root.left, lower, root.getValue()) &&
				checkInRange(root.right, root.getValue(), upper);
	}

	@Override
	public Iterator<T> iterator() 
	{
		return list.iterator();
	}

	@Override
	public int size() 
	{
		assert wellFormed() : "Invariant broken at start of size";
		return size(_root);
	}

	private int size(Node<T> root) //the getSizeHelper returns the size of the sub tree
	{
		if (root == null)
		{
			return 0;
		}
		return root.getSize();
	}

	public boolean add(T thisItem)
	{
		assert wellFormed() : "Invariant broken at the start of add";

		if (contains(thisItem)) return false;
		if (_root == null)
		{
			_root = new Node<T>(thisItem);
			list.modified();
			return true;
		}
		list.modified();
		boolean result = addHelper(thisItem, _root);
		assert wellFormed() : "Invariant broken at the end of add";

		return result;
	}

	private boolean addHelper(T thisItem, Node<T> root) //adds thisItem 
	{
		++root.size;
		if (_comparator.compare(thisItem, root.getValue()) < 0)
		{
			if (root.left == null)
			{
				root.left = new Node<T>(thisItem);
			}
			else
			{
				return addHelper(thisItem, root.left);
			}
		}
		else
		{
			if (root.right == null)
			{
				root.right = new Node<T>(thisItem);
			}
			else
			{
				return addHelper(thisItem, root.right);
			}
		}
		return true;
	}

	public List<T> asList() //returns a view of the backed list
	{
		return list;
	}

	@SuppressWarnings("unchecked")
	private T asItem(Object o) //hw10 askey, checks if item exist in tree
	{
		if (_root == null || o == null) return null;

		try 
		{
			_comparator.compare(_root.getValue(), (T) o);
			_comparator.compare(_root.getValue(), (T) o);
			return (T)o;
		}
		catch (ClassCastException ex)
		{
			return null;
		}
	}

	public boolean contains(Object o) //hw10
	{
		assert wellFormed() : "Invariant broken at start of contains";
		if (asItem(o) == null)
		{
			return false;
		}

		return containsHelper(asItem(o), _root);
	}

	private boolean containsHelper(T searchThis, Node<T> root)
	{
		if (root == null) return false;
		int c = _comparator.compare(searchThis, root.getValue());

		if (c == 0) return true;
		if (c < 0) return containsHelper(searchThis, root.left);

		return containsHelper(searchThis, root.right);
	}

	public boolean remove(Object o)
	{
		assert wellFormed() : "Invariant broken at start of remove";
		if (asItem(o) == null) return false;
		if (contains(asItem(o)) == false) return false;

		_root = removeHelper(asItem(o), _root);
		list.modified();

		assert wellFormed() : "Invariant broken at end of remove";

		return true;
	}

	private Node<T> removeHelper(T searchThis, Node<T> root)
	{
		if (root == null) return root;

		--root.size;
		int c = _comparator.compare(searchThis, root.getValue());

		if (c < 0)
		{
			root.left = removeHelper(searchThis, root.left);
		}
		else if (c > 0)
		{
			root.right = removeHelper(searchThis, root.right);
		}
		else
		{
			if (root.left != null && root.right != null) //case 1
			{
				root.setValue(findMinHelper(root.right));
				root.right = removeHelper(root.getValue(), root.right);
			}
			else if (root.left != null) //case 2
			{
				root = root.left;
			}
			else if (root.right != null) //case 3
			{
				root = root.right;
			}
			else
			{
				root = null;
			}
		}
		return root;
	}

	private T findMinHelper(Node<T> root) //find successor
	{
		if (root.left == null)
		{
			return root.getValue();
		}

		return findMinHelper(root.left);
	}

	private class SetList extends AbstractList<T> 
	{
		private void modified()
		{
			++modCount;
		}

		@Override
		public T get(int index) 
		{
			if (index < 0) throw new IndexOutOfBoundsException();
			if (index >= size()) throw new IndexOutOfBoundsException();

			return getHelper(index, _root);
		}

		private T getHelper(int index, Node<T> root) //uses index to find lower search time
		{
			int leftSize = TreeSet.this.size(root.left);

			if (index == leftSize)
			{
				return root.getValue();
			}
			else if (index > leftSize)
			{
				if (root.right != null)
				{
					return getHelper(index -leftSize -1, root.right);
				}
			}
			else if (index < leftSize)
			{
				return getHelper(index, root.left);
			}

			return null;
		}

		@Override
		public int size() 
		{
			return TreeSet.this.size();
		}

		public T remove(int index)
		{
			T returnThis = get(index);
			if (get(index) != null)
			{
				remove(get(index));
			}

			++modCount;
			return returnThis;
		}

		public int indexOf(Object o)
		{
			if (contains(o) == false) return -1;

			return indexOfHelper(asItem(o), _root);
		}

		private int indexOfHelper(T thisItem, Node<T> root)
		{
			if (root == null) return -1;
			int c = _comparator.compare(thisItem, root.getValue());
			int leftSize = TreeSet.this.size(root.left);

			if (c == 0) 
			{
				return leftSize;
			}

			if (c < 0)
			{
				return indexOfHelper(thisItem, root.left);
			}
			else
			{
				return indexOfHelper(thisItem, root.right) + leftSize + 1;
			}
		}

		public int lastIndexOf(Object o)
		{
			return indexOf(o);
		}

		public boolean remove(Object o)
		{
			return TreeSet.this.remove(o);
		}

		public boolean contains(Object o)
		{
			return TreeSet.this.contains(o);
		}

		protected void removeRange(int lo, int hi)
		{
			if (hi <= lo)
			{
				return;
			}
			_root = doRemoveRange(_root, lo, hi, 0);
			++modCount;
		}

		private Node<T> doRemoveRange(Node<T> root, int lo, int hi, int index) //lab8
		{
			if(root == null) return root;
			int computeCurrentIndex = TreeSet.this.size(root.left) + index; //compute

			if (hi <= computeCurrentIndex)
			{
				root.left = doRemoveRange(root.left, lo, hi, index);
				
				int resize = TreeSet.this.size(root.left) + TreeSet.this.size(root.right) + 1; //resizing
				root.size = resize;
			}
			else if (lo > computeCurrentIndex)
			{
				root.right = doRemoveRange(root.right, lo, hi, computeCurrentIndex + 1);
				
				int resize = TreeSet.this.size(root.left) + TreeSet.this.size(root.right) + 1;
				root.size = resize;
				
			}
			else
			{
				root.left = doRemoveRange(root.left, lo, hi, index);
				root.right = doRemoveRange(root.right, lo, hi, computeCurrentIndex + 1);
				
				Node<T> thisNow = TreeSet.this.removeHelper(root.getValue(), root);
				root = thisNow;
				
				if (root != null)
				{
					root.size = TreeSet.this.size(root.left) + TreeSet.this.size(root.right) + 1;
				}
			}
			return root;
		}
	}

}	
