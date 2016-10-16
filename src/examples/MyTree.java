package examples;

import java.util.Iterator;

public class MyTree<E> implements Tree<E> {

	class TNode implements Position<E>{
		E elem;
		TNode parent;
		MyLinkedList<TNode> children = new MyLinkedList<>();
		Position<TNode> myChildrenPos;

		Object creator = MyTree.this;
		@Override
		public E element() {
			return elem;
		}
	}

	private TNode root;
	private int size;

	@Override
	public Position<E> root() {
		return root;
	}

	@Override
	public Position<E> createRoot(E o) {
		TNode n = new TNode();
		n.elem = o;
		size++;
		root=n;
		return n;
	}

	private TNode checkAndCast(Position<E> p) {
		TNode n;
		try {
			n = (TNode) p;
		} catch (ClassCastException e) {
			throw new RuntimeException("This is not a Position belonging to MyTree");
		}
		if (n.creator == null) throw new RuntimeException("position was allready deleted!");
		if (n.creator != this) throw new RuntimeException("position belongs to another MyTree instance!");
		return n;
	}

	@Override
	public Position<E> parent(Position<E> child) {
		return checkAndCast(child).parent;
	}

	@Override
	public Iterator<Position<E>> childrenPositions(Position<E> parent) {

		TNode p = checkAndCast(parent);
		return new Iterator<Position<E>>(){
			Iterator<TNode> it = p.children.elements();
			@Override
			public boolean hasNext() {
				return it.hasNext();
			}

			@Override
			public Position<E> next() {
				return it.next();
			}

		};
	}

	@Override
	public Iterator<E> childrenElements(Position<E> parent) {
		childrenPositions(parent);
		return null;
	}

	@Override
	public int numberOfChildren(Position<E> parent) {
		TNode p = checkAndCast(parent);
		return p.children.size();
	}

	@Override
	public Position<E> insertParent(Position<E> p, E o) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Position<E> addChild(Position<E> parent, E o) {
		TNode np = checkAndCast(parent);
		TNode n = new TNode();
		n.elem = o;
		n.parent = np;
		size++;
		n.myChildrenPos = np.children.insertLast(n);
		return n;
	}

	@Override
	public Position<E> addChildAt(int pos, Position<E> parent, E o) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Position<E> addSiblingAfter(Position<E> sibling, E o) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Position<E> addSiblingBefore(Position<E> sibling, E o) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void remove(Position<E> p) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isExternal(Position<E> p) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isInternal(Position<E> p) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int size() {
		// TODO Auto-generated method stub
		return 0;
	}

	public void print(TNode r, String ind){
		System.out.println(ind+r.element().toString());
		for(TNode c: r.children) print(c,ind+"--|");
	}


	public void removeSubtree(Position<E> p){
		TNode n = checkAndCast(p);
		Iterator<TNode> it = n.children.elements();
		while (it.hasNext()) remove(it.next());
		remove(n);
	}

	@Override
	public E replaceElement(Position<E> p, E o) {
		// TODO Auto-generated method stub
		return null;
	}

	public static void main(String[] args) {
		MyTree<String> t = new MyTree<>();
		Position<String> b = t.createRoot("Book");
		Position<String> c1 = t.addChild(b,"chapter 1");
		Position<String> c2 = t.addChild(b,"chapter 2");
		Position<String> c3 = t.addChild(b,"chapter 3");
		Position<String> c21 = t.addChild(c2,"chapter 2.1");
		Position<String> c31 = t.addChild(c3,"chapter 3.1");
		Position<String> c32 = t.addChild(c3,"chapter 3.2");
		Position<String> c33 = t.addChild(c3,"chapter 3.3");
		Position<String> c311 = t.addChild(c31,"chapter 3.1.1");
		Position<String> c312 = t.addChild(c31,"chapter 3.1.2");
		Position<String> c313 = t.addChild(c31,"chapter 3.1.3");
		Position<String> c314 = t.addChild(c31,"chapter 3.1.4");

		t.print(t.root,"--|");
		System.out.println();
	}

}
