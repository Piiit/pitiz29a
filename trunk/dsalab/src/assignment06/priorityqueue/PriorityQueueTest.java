package assignment06.priorityqueue;

import junit.framework.Assert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class PriorityQueueTest {

	private PriorityQueue q = null;

	@Before
	public void setUp() throws Exception {
		q = new PriorityQueue();
	}

	@After
	public void tearDown() throws Exception {
		q = null;
	}

	@Test
	public void testIsEmpty() throws Exception {
		Assert.assertEquals(true, q.isEmpty());
		q.insert(0);
		Assert.assertEquals(false, q.isEmpty());
		q.extractMax();
		Assert.assertEquals(true, q.isEmpty());
	}

	@Test
	public void testExtractMaxEmptyQueue() throws Exception {
		try {
			q.extractMax();
			Assert.fail("ExtractMax should throw an exception if the queue is empty!");
		} catch (Exception e) {
		}
	}
	
	@Test
	public void testInsertAndExtractMax() throws Exception {
		Assert.assertEquals(true, q.isEmpty());
		q.insert(10);
		q.insert(5);
		q.insert(20);
		Assert.assertEquals(false, q.isEmpty());
		Assert.assertEquals(20, q.extractMax());
		Assert.assertEquals(10, q.extractMax());
		Assert.assertEquals(5, q.extractMax());
		Assert.assertEquals(true, q.isEmpty());
	}
	
	@Test
	public void testInsertAndExtractMaxEqualNumbers() throws Exception {
		Assert.assertEquals(true, q.isEmpty());
		q.insert(10);
		q.insert(11);
		q.insert(10);
		q.insert(10);
		Assert.assertEquals(false, q.isEmpty());
		Assert.assertEquals(11, q.extractMax());
		Assert.assertEquals(10, q.extractMax());
		Assert.assertEquals(10, q.extractMax());
		Assert.assertEquals(10, q.extractMax());
		Assert.assertEquals(true, q.isEmpty());

	}

	@Test
	public void testInsertAndExtractMaxNegativeNumbers() throws Exception {
		Assert.assertEquals(true, q.isEmpty());
		q.insert(-2);
		q.insert(-1);
		Assert.assertEquals(false, q.isEmpty());
		Assert.assertEquals(-1, q.extractMax());
		Assert.assertEquals(-2, q.extractMax());
		Assert.assertEquals(true, q.isEmpty());
	}

	
}
