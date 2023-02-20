import java.util.Collections;

//@author Shen ChenZi (Group 08)
public class CounterList {
    private Array<Counter> counters;;
    private int counterNum;
    private Queue q;
    public CounterList(int counterNum, int QueueLength, int CounterQueL){
        this.counterNum = counterNum;
        this.counters = new Array<>(counterNum);
        this.q = new Queue(QueueLength);
        for(int i = 0; i < counterNum; i++){
            counters.set(i,new Counter(CounterQueL));
        }
    }
    public Counter findCounter() {
        Counter curCounter = counters.min();
        if (curCounter.isAvailable()) {
            return curCounter;
        } else {
            if (!curCounter.isQueFull()) {
                return curCounter;
            } else {
                return null;
            }
        }
    }
    public boolean isFull(){
       return q.isFull();
    }
    public boolean que(Customer customer){
        return q.enq(customer);
    }

    public Customer deque(){
        return (Customer) q.deq();
    }

    public String qString(){
        return q.toString();
    }

    public boolean isEmpty(){
        return q.isEmpty();
    }

}
