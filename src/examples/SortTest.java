package examples;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;


/**
 * @author ps
 * Various sort programs for int arrays (exercise) 
 */
public class SortTest {
	
	
	public static long cnt;
	static Random rand = new Random();
	static int [] b;

	/**
	 * @param a int aray
	 * @return 'true' if 'a' is sorted 
	 */
	public static boolean sortCheck(int[] a) {
		for (int i=0;i<a.length-1;i++){
			if (a[i]>a[i+1]) return false; 
		}
		return true;
	}	

	public static void mergeSort(int [] a){
		b = new int[a.length];
		mSort(a,0,a.length-1);
		a=b;
	}
	
	private static void mSort(int[] a, int from, int to) {
		if (from==to) return;
		int med = (from+to)/2;
		mSort(a,from,med);
		mSort(a,med+1,to);
		merge(a,from,med,to);
	}

	private static int parPos(int i){return (i-1)/2;}
	private static int childLeftPos(int i){return i*2+1;}
	private static int childRightPos(int i){return i*2+2;}

	/*
	* the input Array will get sorted
	* The method for sorting is to get a MaxHeap and swap the first with the last place
	* and create a maxHeap without the swaped place behind*/
	public static void arraySort(int[] a){
		//and Array Sort with downHeap
		heapMaxSort(a);
		if(checkMaxHeap(a)){
			for(int i= a.length-1;i>0;i--){
				swap(a,0,i);
				downHeapMax(a,0,i);
			}
		}
	}

	/*
	* UpHeap Method
	* HeapMax Sortiert ein als Tree anzusehender Array in einen MaxHeap ( grösster Element am Root)
	* */
//	public static void heapMaxSort(int[] a) {
//		for (int i = 1; i < a.length; i++) {
//			upHeapMax(a, i);
//
//		}
//	}
//	/*
//	* UpHeap Method
//	* HeapMinSort sortiert ein als Tree anzusehender Array in einen MinHeap ( kleinster Element am Root)
//	* */
//	//HeapMax Sortiert ein als Tree anzusehender Array in einen MaxHeap ( grösster Element am Root)
//	public static void heapMinSort(int[] a) {
//		for (int i = 1; i < a.length; i++) {
//			upHeapMin(a, i);
//		}
//	}

	/*
	* downHeap Method
	* HeapMax sortiert ein als Tree anzusehender Array in einen MaxHeap (grösster Element am Root)
	*/
 	public static void heapMaxSort(int[] a){
		for(int i = a.length/2-1;i>=0;i--){
			downHeapMax(a,i,a.length);
		}
	}
	/*
	* DownHeap Method
	* HeapMinSort sortiert ein als Tree anzusehender Array in einen MinHeap ( kleinster Element am Root)
	* */
	public static void heapMinSort(int[] a){
		for(int i = a.length/2-1;i>=0;i--){
			downHeapMin(a,i,a.length);
		}
	}

	/*
	* @ a: int Array as basic
	* @ i: The Position
	* @ len: the endposition of search in Array (to make bounds inside the array)
	* @ returns: the smaller childPosition of i in the Array a (Array interpretated as Heap)
	* */
	private static int getSmallerChildPos(int[] a, int i, int len){
		//if there is no right child for that node
		if(childRightPos(i)>=len) return childLeftPos(i);
		//compare both children and return the position of the bigger child
		int SmallerChildPos = (a[childLeftPos(i)]<a[childRightPos(i)]) ? childLeftPos(i) : childRightPos(i);
		return SmallerChildPos;
	}

    /*
    * @ a: int Array as basic
    * @ i: The Position
    * @ len: the endposition of search in Array (to make bounds inside the array)
    * @ returns: the bigger childPosition of i in the Array a (Array interpretated as Heap)
    * */
	private static int getBiggerChildPos(int[] a, int i, int len){
		//if there is no right child for that node
		if(childRightPos(i)>=len) return childLeftPos(i);
		//compare both children and return the position of the bigger child
		int BiggerChildPos = (a[childLeftPos(i)]>a[childRightPos(i)]) ? childLeftPos(i) : childRightPos(i);
		return BiggerChildPos;
	}

    /*
    * upHeaps the position Value in Array till the parent is bigger
    * */
	private static void upHeapMax(int[] a, int pos){
		// assume a[0..pos-1] us a maxHeap
		// swap a[pos] with its parent until heap-condition ok
		while(pos>0){
			if(a[parPos(pos)]>a[pos]) break;
			swap(a,parPos(pos),pos);
			pos = parPos(pos);
		}
	}

    /*
    * upHeaps the position Value in Array till the parent is smaller
    * */
	private static void upHeapMin(int[] a, int pos){
		// assume a[0..pos-1] us a minHeap
		// swap a[pos] with its parent until heap-condition ok
		while(pos>0){
			if(a[parPos(pos)] < a[pos]) break;
			swap(a,parPos(pos),pos);
			pos = parPos(pos);
		}
	}

    /*
    * downHeaps the position Value in Array till both children are bigger (maxHeap-> smallest at root)
    * */
	private static void downHeapMin (int[] a, int pos,int len){
		// assume a[0..len] is a maxHeap with exception
		// of a[pos] which is possible too small.
		// swap a[pos] with the greater of its children until
		// heap condition is ok
		while(getSmallerChildPos(a,pos,len) < len){
			int smallerChildPos = getSmallerChildPos(a,pos,len);
			if(a[pos] < a[smallerChildPos]) break;
			swap(a,pos,smallerChildPos);
			pos=smallerChildPos;
		}
	}

    /*
    * downHeaps the position Value in Array till both children are smaller (maxHeap->biggest at root)
    * */
	private static void downHeapMax (int[] a, int pos,int len){
		// assume a[0..len] is a maxHeap with exception
		// of a[pos] which is possible too small.
		// swap a[pos] with the greater of its children until
		// heap condition is ok
		while(getBiggerChildPos(a,pos,len) < len){
			int biggerChildPos = getBiggerChildPos(a,pos,len);
			if(a[pos] > a[biggerChildPos]) break;
			swap(a,pos,biggerChildPos);
			pos=biggerChildPos;
		}
	}

    /*
    * checks if the given Array is a MaxHeap
    * */
	private static boolean checkMaxHeap(int[] a){
		for(int i=0;i<a.length;i++)	if(a[i]>a[parPos(i)]) return false;
		return true;
	}

    /*
* checks if the given Array is a MinHeap
* */
	private static boolean checkMinHeap(int[] a){
		for(int i=0;i<a.length;i++)	if(a[i]<a[parPos(i)]) return false;
		return true;
	}

	private static void printHeap(int[] iArr){
        for (int a: iArr){
			System.out.print("-["+a+"]-");
		}
		System.out.println("");
	}

	private static void merge(int[] a, int from, int med, int to) {
		int left=from;
		int right=med+1;
		int i= from;
		while(left<=med&&right<=to){
			if(a[left]<a[right]) b[i++] = a[left++];
			else b[i++]= a[right++];
		}
		while(left<=med){
			b[i++]=a[left++];
		}
		while(right<=to){
			b[i++]=a[right++];
		}
		for(int j=i-1;j>=0;j--)a[j]=b[j];

	}

	/**
	 * Non optimized bubble sort for an int array 
	 * @param a
	 */
	public static void bubbleSort(int[] a) {
		cnt=0;
		int m = a.length-1;
		for(int i=m; i>0; i--){ 

			for (int k=0; k < i; k++){
				if(a[k]>a[k+1]) swap(a,k,k+1);
			}
			// now a[i] is on its final position!
		}
	}

	/**
	 * swap the array elements a[i] and a[k]
	 * @param a int array 
	 * @param i position in the array 'a'
	 * @param k position in the array 'a'
	 */
	static void swap(int [] a, int i, int k){
		int tmp=a[i];
		a[i]=a[k];
		a[k]=tmp;
		cnt++;
	}

	public static void main(String[] args) {
//		int[] iArr = {2,3,5,6,8,2,9,0,5,6,0,9,4,90,0,23,8,37,6,5,45,9,8};
//        System.out.println("This is the raw Array");
//        printHeap(iArr);
//        //make a max Array
//		heapMaxSort(iArr);
//        System.out.println();
//        System.out.println("This Array is Maxsorted: ["+checkMaxHeap(iArr)+"]");
//		printHeap(iArr);
//
//        //make a min Array
//		heapMinSort(iArr);
//        System.out.println();
//        System.out.println("This Array is Minsorted: ["+checkMinHeap(iArr)+"]");
//		printHeap(iArr);
//
//		arraySort(iArr);
//        System.out.println();
//        System.out.println("This Array is sorted: ["+sortCheck(iArr)+"]");
//		printHeap(iArr);

//		long t1=0,t2=0,te1=0,te2=0,eTime=0,time=0;
//		int n = 1000000;
//		// we need a random generator
//		Random rand=new Random(Integer.MAX_VALUE);
//		//rand.setSeed(54326346); // initialize always in the same state
//		ThreadMXBean threadBean = ManagementFactory.getThreadMXBean();
//		// new array
//		int [] a = new int[n];
//		// fill it randomly
//		for (int i=0;i<a.length ;i++) {
//			a[i]=i;// rand.nextInt(n);
//		}
//
//		// mix: a litle bit
//		for (int i=0;i<a.length ;i++) {
//			swap(a,i,rand.nextInt(n-1));
//		}
//
//		cnt=0;  // for statistcs reasons
//		// get Time
//		te1=System.nanoTime();
//		t1 = threadBean.getCurrentThreadCpuTime();
//		//System.out.println("non sorted array"+ Arrays.toString(a));
//		mergeSort(a);
//		//System.out.println("sorted Array"+Arrays.toString(a));
//		te2 = System.nanoTime();
//		t2 = threadBean.getCurrentThreadCpuTime();
//		time=t2-t1;
//		eTime=te2-te1;
//		System.out.println("# elements: "+n);
//		System.out.println("CPU-Time usage: "+time/1000000.0+" ms");
//		System.out.println("elapsed time: "+eTime/1e6+" ms");
//		System.out.println("sorted? "+sortCheck(a));
//		System.out.println("swap operation needed: "+cnt);
//		// ok
	}


}
