package examples;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.Iterator;
import java.util.LinkedList;

public class MyLinkedList<E> implements List<E>, Iterable<E> {
	private Position<E> itr;

	@Override
	public Iterator<Position<E>> positions() {
		return new Iterator<Position<E>>(){
			LNode curent = first;
			@Override
			public boolean hasNext() {
				return curent!=null;
			}

			@Override
			public Position<E> next() {
				LNode ret = curent;
				curent = curent.next;
				return ret;
			}

		};
	}

	@Override
	public Iterator<E> elements() {
		return new Iterator<E>(){
			LNode curent = first;
			@Override
			public boolean hasNext() {
				return curent != null;
			}

			@Override
			public E next() {
				E ret = curent.elem;
				curent = curent.next;
				return ret;
			}};
	}

	@Override
	public Iterator<E> iterator() {
		return elements();
	}


	// auxiliary class for the list positions
	private class LNode implements Position<E>{
		E elem;
		LNode prev,next;
		Object creator = MyLinkedList.this;
		
		@Override
		public E element() {
			return elem;
		}		
	}
	
	private LNode first,last;
	private int size;
	
	
	private MyLinkedList<E>.LNode checkAndCast(Position<E> p) {
		LNode n;
		try {
			n = (LNode) p;
		} catch (ClassCastException e) {
			throw new RuntimeException("This is not a Position belonging to MyLinkedList"); 
		}
		if (n.creator == null) throw new RuntimeException("position was allready deleted!");
		if (n.creator != this) throw new RuntimeException("position belongs to another MyLinkedList instance!");			
		return n;
	}



	@Override
	public Position<E> first() {
		return first;
	}

	@Override
	public Position<E> last() {
		return last;
	}

	@Override
	public boolean isFirst(Position<E> p) {		
		return first==checkAndCast(p);
	}

	@Override
	public boolean isLast(Position<E> p) {
		return last == checkAndCast(p);
	}

	@Override
	public Position<E> next(Position<E> p) {
		return checkAndCast(p).next;
	}

	@Override
	public Position<E> previous(Position<E> p) {
		return checkAndCast(p).prev;
	}

	@Override
	public E replaceElement(Position<E> p, E o) {
		LNode n  = checkAndCast(p);
		E ret=n.elem;
		n.elem=o;
		return ret;
	}

	@Override
	public Position<E> insertFirst(E o) {
		LNode n = new LNode();
		n.elem=o;
		n.next = first;
		if (first!=null){
			first.prev = n;
		}
		else {
			last = n;
		}
		first = n;
		size++;
		return n;
	}

	@Override
	public Position<E> insertLast(E o) {
		LNode n = new LNode();
		n.elem = o;
		n.prev = last;
		if (last != null) {
			last.next = n;
		} else {
			first = n;
		}
		last = n;
		size++;
		return n;
	}

	@Override
	public Position<E> insertBefore(Position<E> p, E o) {
		//The Position
		LNode pos = checkAndCast(p);
		//The new Object to input
		LNode obj = new LNode();
		obj.elem = o;
		if(pos.prev==null) return insertFirst(o);
		//link oLNode with LNode before
		obj.prev= pos.prev;
		obj.next = pos;

		//Link pos prev and next with oLNode
		pos.prev.next=obj;
		pos.prev = obj;
		// return the position
		size++;
		return obj;
	}

	@Override
	public Position<E> insertAfter(Position<E> p, E o) {
		//The Position
		LNode pos = checkAndCast(p);
		//The new Object to input
		LNode obj = new LNode();
		obj.elem = o;
		if(pos.next==null) return insertLast(o);
		//link oLNode with LNode before
		obj.prev= pos;
		obj.next = pos.next;

		//Link pos prev and next with oLNode
		pos.next.prev=obj;
		pos.next = obj;
		// return the position
		size++;
		return obj;
	}

	@Override
	public void remove(Position<E> p) {
		LNode n = checkAndCast(p);
		n.creator = null; // invalidate!
		if (n.prev != null) n.prev.next = n.next;
		else first = n.next;
		if (n.next != null) n.next.prev=n.prev;
		else last = n.prev;
		size--;
	}


	@Override
	public int size() {
		// TODO Auto-generated method stub
		return size;
	}

	@Override
	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return size==0;
	}



	public static void main(String[] args) {
		List<Integer> ll = new MyLinkedList<>();
		Position<Integer> p9 = ll.insertFirst(9);
		Position<Integer> p4 = ll.insertBefore(p9,4);
		Position<Integer> p14 = ll.insertAfter(p9,14);
		Position<Integer> p6 = ll.insertBefore(p9,6);
		Position<Integer> p11 = ll.insertAfter(p9,11);
		Position<Integer> p10 = ll.insertBefore(p11,10);
		Position<Integer> p13 = ll.insertBefore(p14,13);
		Position<Integer> p7 = ll.insertBefore(p6,7);
		Position<Integer> p3 = ll.insertBefore(p4,3);
		ll.remove(p9);


		//Position p = ll.first();
		//while (p != null) {
		//	System.out.println(p.element());
		//	p=ll.next(p);
		//}
		// Iterator version to print the Array list
		Iterator<Position<Integer>> llit = ll.positions();
		while(llit.hasNext()) System.out.println(llit.next().element());

		//LinkedList jl = new LinkedList();
		//Iterator it = jl.iterator();
		//it.has
		ThreadMXBean threadBean = ManagementFactory.getThreadMXBean();	
		final int N=10000;
		Position<Integer>[] pos = new Position[N];
		Integer [] ints = new Integer[N];
		long t1,t2,te1,te2;
		LinkedList<Integer> jl = new LinkedList<>();
		t1 = threadBean.getCurrentThreadCpuTime();
		te1 = System.nanoTime();
		for (int i=0;i<N;i++) {
			ints[i]=i;
			jl.addFirst(i);
		}
		for (int i=0;i<N;i++) jl.remove(ints[i]);
		te2 = System.nanoTime();
		t2 = threadBean.getCurrentThreadCpuTime();
		System.out.println("java LinkedList: time to stor "+N+" elements:[s] "+1E-9*(te2-te1));
		System.out.println(" cpu time:[s] "+1E-9*(t2-t1));
		t1 = threadBean.getCurrentThreadCpuTime();
		te1 = System.nanoTime();
		for (int i=0;i<N;i++) pos[i] = ll.insertLast(i);
		for (int i=0;i<N;i++) ll.remove(pos[i]);
		te2 = System.nanoTime();
		t2 = threadBean.getCurrentThreadCpuTime();
		System.out.println("MyLinkedList: time to stor "+N+" elements:[s] "+1E-9*(te2-te1));
		System.out.println(" cpu time: "+1E-9*(t2-t1));	
	}

}
