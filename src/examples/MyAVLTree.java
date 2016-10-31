package examples;

import java.util.Iterator;

public class MyAVLTree<K extends Comparable<? super K>, E> implements
		OrderedDictionary<K, E> {

	class AVLNode implements Locator<K,E> {
		K key;
		E elem;
		int height;
		
		AVLNode parent,left,right;
		Object creator = MyAVLTree.this;
		
		@Override
		public E element() {
			return elem;
		}

		@Override
		public K key() {
			return key;
		}
		
		boolean isExternal(){
			return left == null; // right would also be null!
		}
		
		boolean isLeftChild(){
			return parent!=null && parent.left==this;
		}
		
		boolean isRightChild(){
			return parent!=null && parent.right==this;
		}
		
		void expand(K key, E elem){
			this.key=key;
			this.elem=elem;
			this.left = new AVLNode();
			this.right = new AVLNode();
			left.parent=right.parent=this;
			height=1;
		}
		
	}

	private AVLNode root = new AVLNode();
	private int size;
	
	@Override
	public int size() {
		return size;
	}

	@Override
	public Locator<K, E> find(K key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Locator<K, E>[] findAll(K key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Locator<K, E> insert(K key, E o) {
		AVLNode act = root;
		while(!act.isExternal()){
			if(0 < act.key().compareTo(key)) act = act.left;
			else act = act.right;
		}
		act.expand(key,o);
		increaseHeight(act);
		checkStability(act.parent);
		size++;
		return act;
	}
	public void increaseHeight(AVLNode node){
		AVLNode act = node.parent;
		while(act!=null){
			act.height = (act.left.height < act.right.height)? act.right.height + 1: act.left.height +1;
			act = act.parent;
		}
	}

	//noch nicht getestet
	public Locator<K,E> getLeftest(AVLNode node){
		while(!node.left.isExternal()){
		node = node.left;
		}
		return node;
	}
//hier weiterfahren
	public void checkStability(AVLNode node){
		while(node!= null){
			int diff = node.left.height - node.right.height;
			if(diff > 1 | diff < -1) TreeStabilizer(node);
			node = node.parent;
		}
	}
	//going to get startet from there where the problem is found
	public void  TreeStabilizer(AVLNode node){
		//substitution Vars
		AVLNode x = null,y = null,z = null,a = null,b= null,c= null,d= null,parent= null,first= null,second= null,third = null;
		String branch;// Lc: Left Curved/ Rc: Right Curved/ Ls: Left straight Rs: Right Straigt
		boolean leftChild;
		parent = node.parent;
		leftChild = node.isLeftChild() ? true: false;

		//definiere die 3 Nodes in richtung der längsten Asts
		first = node;
		second = getElderChild(first);
		third = getElderChild(second);

		//find out if the branch to correct is stright or curved

		if(second.isLeftChild() && third.isLeftChild()) branch = "Ls"; // straight to Left
		if (second.isRightChild()&& third.isRightChild()) branch = "Rs"; // straight to Right
		if (second.isLeftChild()&& third.isRightChild()) branch = "Rc"; // curved to Right
		else branch = "Rs"; //curved to Left

		//define the children Subtituions
		switch(branch){
			case  "Ls":
				x = second;
				y = first;
				z = third;
				a = z.left;
				b = z.right;
				c = y.right;
				d = x.right;
				break;
			case  "Rs":
				x = second;
				y = first;
				z = third;
				a = first.left;
				b = second.left;
				c = third.left;
				d = third.right;
				break;
			case  "Lc":
				x = third;
				y = first;
				z= second;
				a = y.left;
				b = z.left;
				c = z.right;
				d = x.right;
				break;
			case  "Rc":
				x = third;
				y = second;
				z= first;
				a = x.left;
				b = z.left;
				c = z.right;
				d = x.right;
				break;
		}
		modify(parent,leftChild,x,y,z,a,b,c,d);

	}

	public void modify(AVLNode parent,boolean fromParentLeft, AVLNode x,AVLNode y, AVLNode z, AVLNode a, AVLNode b, AVLNode c, AVLNode d){
		z.left = c;
		z.right = d;
		z.left.parent = z.right.parent = z;

		y.left = a;
		y.right = b;
		y.left.parent = y.right.parent = y;

		if(parent!=null){
			if(fromParentLeft) parent.left = x;
			else parent.right = x;

			x.left = y;
			x.right = z;
			x.left.parent = x.right.parent = x;
			x.parent = parent;
		}
		else{
			root=x;
			root.parent = null;
			root.left = y;
			root.right = z;
			root.left.parent = root.right.parent = root;
		}
		increaseHeight(a);
		increaseHeight(b);
		increaseHeight(c);
		increaseHeight(d);

	}
	public AVLNode getElderChild(AVLNode node){
		if(node.isLeftChild()) return (node.left.height < node.right.height) ? node.right : node.left;
		else return (node.left.height <= node.right.height) ? node.right : node.left;
	}

	@Override
	public void remove(Locator<K, E> loc) {

		// TODO Auto-generated method stub

	}

	@Override
	public Locator<K, E> closestBefore(K key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Locator<K, E> closestAfter(K key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Locator<K, E> next(Locator<K, E> loc) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Locator<K, E> previous(Locator<K, E> loc) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Locator<K, E> min() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Locator<K, E> max() {
		// TODO Auto-generated method stub
		return null;
	}

	public void printKeys() {
		prittyPrint(root,"");
	}


	private void printKeys(AVLNode n, String ind) {
		if (n.isExternal()) return;
		printKeys(n.right,ind+"-");
		System.out.println(ind+n.key);
		printKeys(n.left,ind+"-");
	}

	private void prittyPrint(AVLNode r, String in) {
		if (r.isExternal()) return;
		// right subtree
		int sLen = in.length();
		String inNeu = in;
		if (r.isRightChild()) inNeu = in.substring(0,sLen-2)+"  ";
		prittyPrint(r.right,inNeu+" |");
		// root of the subtree
		String inN = in;
		if (sLen>0) inN = in.substring(0,sLen-1)+"+-";
		else inN = in+"-"; // root of the tree
		if ( ! r.right.isExternal()) System.out.println(inNeu+" |");
		else System.out.println(inNeu);
		System.out.println(inN+r.key()+"["+r.height+"]");//+"(h="+r.height+")"+":"+r.elem+")");
		// left subtree
		inNeu = in;
		if (r.isLeftChild()){
			inNeu = in.substring(0,sLen-2)+"  ";
		}
		prittyPrint(r.left,inNeu+" |");
	}

	@Override
	public Iterator<Locator<K, E>> sortedLocators() {
		// TODO Auto-generated method stub
		return null;
	}

	public static void main(String[] args) {
		MyAVLTree<Integer,String> myTree = new MyAVLTree();
		myTree.insert(1,"");
		myTree.insert(2,"");
		myTree.insert(3,"");
		myTree.insert(4,"");
		myTree.insert(5,"");
		myTree.insert(6,"");
		myTree.insert(6,"");
		myTree.insert(8,"");
		myTree.insert(9,"");
		myTree.insert(20,"");
		myTree.insert(2,"");
		myTree.insert(21,"");
		myTree.printKeys();

	}

}
