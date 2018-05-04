import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

import junit.framework.TestCase;
import edu.uwm.cs351.TreeSet;


public class TestEfficiency extends TestCase {

	private TreeSet<Integer> tree;
	
	private Random random;
	
	private static final int POWER = 22; // 2 million entries
	private static final int TESTS = 100000;
	
	protected void setUp() throws Exception {
		super.setUp();
		random = new Random();
		try {
			assert tree.size() == TESTS : "cannot run test with assertions enabled";
		} catch (NullPointerException ex) {
			throw new IllegalStateException("Cannot run test with assertions enabled");
		}
		tree = new TreeSet<Integer>();
		int max = (1 << (POWER)); // 2^(POWER) = 2 million
		for (int power = POWER; power > 1; --power) {
			int incr = 1 << power;
			for (int i=1 << (power-1); i < max; i += incr) {
				tree.add(i);
			}
		}
	}
		
	
	@Override
	protected void tearDown() throws Exception {
		tree = null;
		super.tearDown();
	}


	public void testIsEmpty() {
		for (int i=0; i < TESTS; ++i) {
			assertFalse(tree.isEmpty());
		}
	}
	
	public void testSize() {
		for (int i=0; i < TESTS; ++i) {
			assertEquals((1<<(POWER-1))-1,tree.size());
		}
	}

	public void testContains() {
		for (int i=0; i < TESTS; ++i) {
			int r = random.nextInt(TESTS);
			assertTrue(tree.contains(r*4+2));
			assertFalse(tree.contains(r*2+1));
		}
	}
	
	public void testIterator() {
		Iterator<Integer> it = tree.iterator();
		for (int i=0; i < TESTS; ++i) {
			assertTrue("After " + i + " next(), should still have next",it.hasNext());
			it.next();
		}
	}
	
	public void testIteratorCreation() {
		for (int i=0; i < TESTS; ++i) {
			Iterator<Integer> it = tree.iterator();
			assertTrue(it.hasNext());
			assertEquals(2,it.next().intValue());
			assertTrue(it.hasNext());
			assertEquals(4,it.next().intValue());
			assertTrue(it.hasNext());
		}
	}
	
	public void testRemove() {
		Set<Integer> touched = new HashSet<Integer>();
		for (int i=0; i < TESTS; ++i) {
			int r = random.nextInt(TESTS);
			if (!touched.add(r)) continue; // don't check again
			assertTrue(tree.remove(r*4+2));
			assertFalse(tree.remove(r*2+1));
		}
	}
	
	public void testGet() {
		for (int i=0; i < TESTS; ++i) {
			int r = random.nextInt(TESTS);
			assertEquals(r*2+2,tree.asList().get(r).intValue());
		}
	}
	
	public void testIndexOf() {
		List<Integer> l = tree.asList();
		for (int i=0; i < TESTS; ++i) {
			int r = random.nextInt(TESTS);
			if ((r & 1) == 0) {
				assertEquals(r/2-1,l.indexOf((Object)new Integer(r)));
			} else {
				assertEquals(-1,l.indexOf(r));
			}
		}
	}
	
	public void testLastIndexOf() {
		List<Integer> l = tree.asList();
		for (int i=0; i < TESTS; ++i) {
			int r = random.nextInt(TESTS);
			if ((r & 1) == 0) {
				assertEquals(r/2-1,l.lastIndexOf((Object)new Integer(r)));
			} else {
				assertEquals(-1,l.lastIndexOf(r));
			}
		}
	}
	
	public void testContainsList() {
		for (int i=0; i < TESTS; ++i) {
			int r = random.nextInt(TESTS);
			assertTrue(tree.asList().contains(r*4+2));			
			assertFalse(tree.asList().contains(r*4+1));			
		}
	}
	
	public void testRemoveList() {
		Set<Integer> tested = new HashSet<>();
		for (int i=0; i < TESTS; ++i) {
			int r = random.nextInt(TESTS);
			if (tested.add(r)) {
				assertTrue(tree.asList().remove(new Integer(r*4+2)));	
			}
			assertFalse(tree.asList().remove(new Integer(r*4+1)));			
		}
	}
	
	public void testRemoveIterator() {
		int removed = 0;
		int max = (1 << POWER);
		assertEquals(max/2-1,tree.size());
		Iterator<Integer> it = tree.iterator();
		for (int i = 2; i < max; i += 2) {
			assertEquals(new Integer(i),it.next());
			if (random.nextBoolean()) {
				it.remove();
				++removed;
			}
		}
		assertEquals(max/2-1-removed,tree.size());
	}
	
	public void testRemoveRange() {
		tree.add(1 << POWER);
		for (int power = POWER-2; power >= 0; --power) {
			int n = 1 << power;
			assertEquals(n*2,tree.size());
			tree.asList().subList(n,2*n).clear();
		}
		assertEquals(1,tree.size());
	}
	
	public void testRemoveRangeSmall() {
		int size = (1 << (POWER-1))-1;
		assertEquals("Broken before test!", size, tree.size());
		for (int i=0; i < size; ++i) {
			tree.asList().subList(i, i);
		}
		assertEquals(size, tree.size());
		for (int i=size; i > 0; i-=2) {
			tree.asList().subList(i-1,i).clear();
		}
		assertEquals(size/2,tree.size());
	}
}
