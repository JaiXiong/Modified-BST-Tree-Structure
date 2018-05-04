import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;

import edu.uwm.cs351.TreeSet;


public class TestTreeSet extends TestAbstractOrderedSet<String> {

	@Override
	protected void initCollection() {
		tree = new TreeSet<String>();
		list = tree.asList();
		c = tree;
		e0 = "apples";
		e1 = "bread";
		e2 = "carrots";
		e3 = "dill";
		e4 = "eggs";
		e5 = "fish";
		e6 = "garlic";
		e7 = "honey";
		e8 = "ice cream";
		e9 = "jelly";
		tree2 = new TreeSet<Integer>(new Comparator<Integer>() {
			@Override
			public int compare(Integer o1, Integer o2) {
				// return o1.toString().compareTo(o2.toString());
				return o2 - o1;
			}
		});
		list2 = tree2.asList();
	}

	TreeSet<String> tree;
	List<String> list;

	TreeSet<Integer> tree2;
	List<Integer> list2;

	private void makeBigTree() {
		// (((1)2(((3(4))5)6((7(8))9)))10((11)12(13)))
		tree.add("10");
		tree.add("02");
		tree.add("01");
		tree.add("06");
		tree.add("05");
		tree.add("03");
		tree.add("04");
		tree.add("09");
		tree.add("07");
		tree.add("08");
		tree.add("12");
		tree.add("11");
		tree.add("13");
	}

	public void test00() {
		tree2 = new TreeSet<Integer>(null); // use natural comparator
		tree2.add(199);
		tree2.add(100);
		tree2.add(999);
		Iterator<Integer> it = tree2.iterator();
		assertEquals(100,it.next().intValue());
		assertEquals(199,it.next().intValue());
		assertEquals(999,it.next().intValue());
	}

	//new added test cases as requested
	public void test30() {
		tree.add("hello");
		Iterator<String> it = list.iterator();
		tree.add("bye");
		assertException(ConcurrentModificationException.class,() -> { it.next(); } );
	}

	public void test31() {
		tree.add("hello");
		Iterator<String> it = list.iterator();
		tree.remove("hello");
		assertException(ConcurrentModificationException.class,() -> {it.next(); });
	}

	public void test32() {
		tree.add("hello");
		tree.add("bye");
		tree.add("world");
		Iterator<String> it = tree.iterator();
		tree.asList().subList(1, 2).clear();
		assertException(ConcurrentModificationException.class,() -> {it.next(); });
	}
	public void test40() {
		assertEquals(0,list2.size());
		tree2.add(88);
		assertEquals(1,list2.size());
		tree2.add(99);
		assertEquals(2,list2.size());
		tree2.add(67);
		assertEquals(3,list2.size());
	}

	public void test41() {
		assertFalse(list2.contains(100));
		tree2.add(100);
		assertFalse(list2.contains(88));
		assertTrue(list2.contains(100));
		assertTrue(list2.contains((Object)new Integer(100)));
		assertFalse(list2.contains(192));
		assertFalse(list2.contains("hello"));
		assertFalse(list2.contains(new Object()));
		tree2.add(144);
		assertTrue(list2.contains((Object)new Integer(144)));
		assertFalse(list2.contains((Object)new Integer(200)));
	}

	public void test42() {
		assertFalse(list2.remove("hello"));
		assertFalse(list2.remove((Object)new Integer(110)));
		tree2.add(1066);
		tree2.add(1492);
		tree2.add(1346);
		tree2.add(-586);
		assertFalse(list2.remove("hello"));
		assertFalse(list2.remove(new Integer(410)));
		assertTrue(list2.remove((Object)new Integer(1492)));
		assertTrue(list2.remove(new Integer(-586)));
	}

	public void test50() {
		assertException(IndexOutOfBoundsException.class, () -> { list.get(0); } );
	}

	public void test51() {
		tree.add("hi");
		assertEquals("hi",list.get(0));
	}

	public void test52() {
		tree.add("apples");
		tree.add("bananas");
		assertEquals("apples",list.get(0));
		assertEquals("bananas",list.get(1));
	}

	public void test59() {
		tree.add("6");
		tree.add("3");
		tree.add("4");
		tree.add("1");
		tree.add("2");
		tree.add("9");
		tree.add("7");
		tree.add("8");
		tree.add("5");
		assertSame(list,tree.asList());
		assertEquals("1",list.get(0));
		assertEquals("2",list.get(1));
		assertEquals("3",list.get(2));
		assertEquals("4",list.get(3));
		assertEquals("5",list.get(4));
		assertEquals("6",list.get(5));
		assertEquals("7",list.get(6));
		assertEquals("8",list.get(7));
		assertEquals("9",list.get(8));
		assertException(IndexOutOfBoundsException.class,() -> { list.get(9); });
		assertException(IndexOutOfBoundsException.class,() -> { list.get(-1); });
	}

	public void test61() {
		tree.add("furry");
		assertEquals("furry",list.remove(0));
		assertEquals(0,tree.size());
	}

	public void test62() {
		tree.add("sneezy");
		tree.add("dopey");
		assertEquals("sneezy",list.remove(1));
		assertEquals("dopey", list.listIterator(1).previous());
	}

	public void test63() {
		tree.add("foo");
		tree.add("bar");
		tree.add("quux");
		assertEquals("quux",list.remove(2));
		assertEquals(2,tree.size());
		assertEquals("foo",list.get(1));
	}

	public void test66() {
		makeBigTree();
		assertEquals("06",list.remove(5));
		assertEquals(12,tree.size());
		assertEquals("05",list.get(4));
		assertEquals("07",list.get(5));
	}

	public void test67() {
		makeBigTree();
		assertEquals("07",list.remove(6));
		assertEquals(12,tree.size());
		assertEquals("06",list.get(5));
		assertEquals("08",list.get(6));
	}

	public void test69() {
		makeBigTree();
		assertException(IndexOutOfBoundsException.class,() -> { list.remove(13); });
		assertException(IndexOutOfBoundsException.class,() -> { list.remove(-1); });	
		list.remove(12);
		assertEquals(12,tree.size());
	}

	public void test70() {
		assertEquals(-1,list.indexOf("hello"));
		assertEquals(-1,list.indexOf(47));
	}

	public void test71() {
		tree2.add(343);
		assertEquals(-1,list2.indexOf(243));
		assertEquals(0,list2.indexOf(343));
		assertEquals(-1,list2.indexOf(625));
		assertEquals(-1,list2.indexOf(null));
		assertEquals(-1,list2.indexOf("hello"));
	}

	public void test72() {
		tree2.add(256);
		tree2.add(128);
		assertEquals(-1,list2.indexOf(64));
		assertEquals(1,list2.indexOf(128)); // reverse order!
		assertEquals(0,list2.indexOf(256));
		assertEquals(-1,list2.indexOf(512));
	}

	public void test79() {
		makeBigTree();
		assertEquals(-1,list.indexOf("00"));
		assertEquals(0,list.indexOf("01"));
		assertEquals(1,list.indexOf("02"));
		assertEquals(2,list.indexOf("03"));
		assertEquals(3,list.indexOf("04"));
		assertEquals(4,list.indexOf("05"));
		assertEquals(5,list.indexOf("06"));
		assertEquals(6,list.indexOf("07"));
		assertEquals(7,list.indexOf("08"));
		assertEquals(8,list.indexOf("09"));
		assertEquals(9,list.indexOf("10"));
		assertEquals(10,list.indexOf("11"));
		assertEquals(11,list.indexOf("12"));
		assertEquals(12,list.indexOf("13"));
		assertEquals(-1,list.indexOf("9"));
		assertEquals(-1,list.indexOf(9));
	}


	public void test80() {
		assertEquals(-1,list.lastIndexOf("hello"));
	}

	public void test89() {
		makeBigTree();
		assertEquals(-1,list.lastIndexOf("00"));
		assertEquals(0,list.lastIndexOf("01"));
		assertEquals(1,list.lastIndexOf("02"));
		assertEquals(2,list.lastIndexOf("03"));
		assertEquals(3,list.lastIndexOf("04"));
		assertEquals(4,list.lastIndexOf("05"));
		assertEquals(5,list.lastIndexOf("06"));
		assertEquals(6,list.lastIndexOf("07"));
		assertEquals(7,list.lastIndexOf("08"));
		assertEquals(8,list.lastIndexOf("09"));
		assertEquals(9,list.lastIndexOf("10"));
		assertEquals(10,list.lastIndexOf("11"));
		assertEquals(11,list.lastIndexOf("12"));
		assertEquals(12,list.lastIndexOf("13"));
		assertEquals(-1,list.lastIndexOf("9"));
		assertEquals(-1,list.lastIndexOf(9));		
	}

	protected <E> void removeRange(TreeSet<E> ts, int lo, int hi) {
		ts.asList().subList(lo,hi).clear();
	}

	public void test90() {
		removeRange(tree,0,0);
	}

	public void test91() {
		tree.add("hello");
		removeRange(tree,0,0);
		assertEquals(1,tree.size());
		removeRange(tree,1,1);
		assertEquals(1,tree.size());
		removeRange(tree,0,1);
		assertEquals(0,tree.size());
	}

	public void test92() {
		tree.add("star");
		tree.add("moon");
		removeRange(tree,0,1);
		assertEquals(1,tree.size());
		assertEquals("star",list.get(0));
		tree.add("sun");
		removeRange(tree,1,2);
		assertEquals(1,tree.size());
		assertEquals("star",list.get(0));
		tree.add("earth");
		removeRange(tree,0,2);
		assertEquals(0,tree.size());
	}

	public void test93() {
		tree.add("red");
		tree.add("orange");
		tree.add("yellow");
		removeRange(tree,0,1);
		assertEquals(2,tree.size());
		assertEquals("red",list.get(0));
		assertEquals("yellow",list.get(1));
		tree.add("green");
		removeRange(tree,1,2);
		assertEquals(2,tree.size());
		assertEquals("green",list.get(0));
		assertEquals("yellow",list.get(1));
		tree.add("blue");
		removeRange(tree,2,3);
		assertEquals(2,tree.size());
		assertEquals("blue",list.get(0));
		assertEquals("green",list.get(1));
		tree.add("indigo");
		removeRange(tree,0,2);
		assertEquals(1,tree.size());
		assertEquals("indigo",list.get(0));
	}

	public void test94() {
		makeBigTree();
		Iterator<String> it = tree.iterator();
		removeRange(tree,5,9);
		assertException(ConcurrentModificationException.class,() -> { it.next(); });
		assertEquals(9,tree.size());
		assertEquals("05",list.get(4));
		assertEquals("10",list.get(5));
	}

	public void test95() {
		makeBigTree();
		removeRange(tree, 1, 6);
		assertEquals(8,tree.size());
		assertEquals("01",list.get(0));
		assertEquals("07",list.get(1));
		assertEquals("08",list.get(2));
	}

	public void test96() {
		makeBigTree();
		removeRange(tree, 6, 13);
		assertEquals(6,tree.size());
		assertEquals("06",list.get(5));
	}

	public void test97() {
		makeBigTree();
		removeRange(tree,0, 7);
		assertEquals(6,tree.size());
		assertEquals("08",list.get(0));
	}

	public void test98() {
		makeBigTree();
		removeRange(tree,2,11);
		assertEquals(4,tree.size());
		assertEquals("12",list.get(2));
	}

	public void test99() {
		makeBigTree();
		removeRange(tree,0,13);
		assertEquals(0,tree.size());
	}
}
