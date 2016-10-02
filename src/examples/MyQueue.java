package examples;

/**
 * Created by mathewthekkekara on 02.10.16.
 */
public class MyQueue<E>  implements Queue<E>{
    private E[] store = (E[])new Object[1];
    private int in;
    private int out;
    private int amount;
    @Override
    public void enqueue(E o) {
        if(amount == size()) expand();
        if(in == size()) in = 0;
        store[in++] = o;
        amount++;
    }

    //Diese Funktion erweitert die Queue wenn diese voll ist
    public void expand(){
        E[] temp = (E[])new Object[size()*2];
        int j=0;
        while(!isEmpty()){
            temp[j++]=dequeue();
        }
        out=0;
        in=j;
        amount=j;
        store = temp;
    }
    @Override
    public E dequeue() {
        if(amount==0) throw new RuntimeException("queue is empty");
        if(out==size())out=0;
        amount--;
        return store[out++];
    }

    @Override
    public E head() {return store[out];}

    @Override
    public int size() {
        return store.length;
    }

    @Override
    public boolean isEmpty() {
        return (amount == 0);
    }

    public static void main(String[] args) {
        MyQueue<String> queue = new MyQueue<>();
        queue.enqueue("Martina");
        queue.enqueue("Florian");
        queue.enqueue("Cindarella");
        queue.enqueue("Noelia");
        queue.enqueue("Mathew");
        queue.enqueue("Joris");
        queue.enqueue("Yves");
        queue.enqueue("Sascha");
        queue.enqueue("Anker");
        queue.enqueue("Lea");

        while(!queue.isEmpty())
            System.out.println(queue.dequeue());
        System.out.println("queue is cleared");
        System.out.println("size of queue: "+queue.size());

    }


}
