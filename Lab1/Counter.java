//@author Shen ChenZi (Group 08)
public class Counter implements Comparable<Counter> {
    private boolean available;
    private static int count = 0;
    private int id;
    private Queue<Customer> counterQue;

    public Counter(int L) {
        this.id = count;
        this.available = true;
        this.counterQue = new Queue<Customer>(L);
        count++;
    }

    public boolean isQueFull(){
        return counterQue.isFull();
    }

    public void joinQue(Customer customer){
        counterQue.enq(customer);
    }

    public Customer deQue(){
        // only Customer can join this Queue
        return (Customer) counterQue.deq();
    }

    public boolean isAvailable() {
        return this.available;
    }

    public void setAvailable() {
        this.available = true;
    }

    public void setUnavailable() {
        this.available = false;
    }

    public String toString() {
        return "S" + this.id;
    }

    public Queue<Customer> getCounterQue() {
        return counterQue;
    }

    @Override
    public int compareTo(Counter counter) {
        if (this.isAvailable()) {
            return -1;
        } else {
            if (counter.isAvailable()) {
                return 1;
            } else {
                if (this.getCounterQue().length() > counter.getCounterQue().length()) {
                    return 1;
                } else {
                    if (this.getCounterQue().length() < counter.getCounterQue().length()) {
                        return -1;
                    } else {
                        if (this.id < counter.id) {
                            return -1;
                        } else {
                            return 1;
                        }
                    }
                }
            }
        }
    }
}

/*This class should be able to set the counter id
Show counter availability, and perform service start and leave method to change its availability;
 */