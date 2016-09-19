package examples;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.ArrayList;
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
	}

	private static void mSort(int[] a, int from, int to) {
		if (from==to) return;
		int med = (from+to)/2;
		mSort(a,from,med);
		mSort(a,med+1,to);
		merge(a,from,med,to);
	}

	private static void merge(int[] a, int from, int med, int to) {
		int left=from;
		int right=med+1;
		int i=from; // target
		while(true){
			// first part allready finished?
			if (left>med) 
				// yes (nothing to do) break
				break;
			// second part alschwoblready finished?
			else if(right>to){
				// yes. copy reminder of part1 and then break
				while(left<=med) b[i++]=a[left++];
				break;
			}
			// merge going on:
			else { 
				// copy the smaller of the two candidates
				if (a[left]<=a[right]) b[i++]=a[left++];
				else b[i++] = a[right++];		 
			}
		}
		// copy back
		while (--i>=from) a[i]=b[i];

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
		long t1=0,t2=0,te1=0,te2=0,eTime=0,time=0;
		int n = 100000000;
		// we need a random generator
		Random rand=new Random(Integer.MAX_VALUE);
		//rand.setSeed(54326346); // initialize always in the same state
		ThreadMXBean threadBean = ManagementFactory.getThreadMXBean();	
		// new array
		int [] a = new int[n];
		// fill it randomly
		for (int i=0;i<a.length ;i++) {
			a[i]=i;// rand.nextInt(n);
		}

		// mix: a litle bit 
		for (int i=0;i<a.length ;i++) {
			swap(a,i,rand.nextInt(n-1));
		}
		
		cnt=0;  // for statistcs reasons
		// get Time
		te1=System.nanoTime();
		t1 = threadBean.getCurrentThreadCpuTime();
		mergeSort(a);
		te2 = System.nanoTime();
		t2 = threadBean.getCurrentThreadCpuTime();
		time=t2-t1;
		eTime=te2-te1;
		System.out.println("# elements: "+n);
		System.out.println("CPU-Time usage: "+time/1000000.0+" ms");
		System.out.println("elapsed time: "+eTime/1e6+" ms");
		System.out.println("sorted? "+sortCheck(a));
		System.out.println("swap operation needed: "+cnt);		
		// ok
	}


}
