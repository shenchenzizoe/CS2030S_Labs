//@author Shen ChenZi (Group 08)
public class ServiceEnd extends Event {
    private Customer customer;
    private Counter counter;
    private CounterList counterList;

    public ServiceEnd(Customer customer, Counter counter, double time, CounterList counterList) {
        super(time);
        this.customer = customer;
        this.counter = counter;
        this.counterList = counterList;
    }

    @Override
    public Event[] simulate() {
        // Make counter available
        this.counter.setAvailable();
        Customer shopLeaveC = counterList.deque();
        Customer counterLeaveC = counter.deQue();
        if (shopLeaveC != null) {
            if (counterLeaveC != null) {
                return new Event[]{
                        new Departure(this.customer, this.getTime()),
                        new ServiceBegin(counterLeaveC, this.counter, this.getTime(), this.counterList),
                        new QueCounter(shopLeaveC, this.counter, this.getTime())
                };
            } else {
                return new Event[]{
                        new Departure(this.customer, this.getTime()),
                        new ServiceBegin(shopLeaveC, this.counter, this.getTime(), this.counterList),
                };
            }
        } else {
            if (counterLeaveC != null) {
                return new Event[]{
                        new Departure(this.customer, this.getTime()),
                        new ServiceBegin(counterLeaveC, this.counter, this.getTime(), this.counterList),
                };
            } else {
                return new Event[]{
                        new Departure(this.customer, this.getTime())
                };
            }
        }
    }


    @Override
    public String toString() {
        return super.toString() + ": " + customer.toString() +
                " service done (by " + counter.toString() + " " + counter.getCounterQue().toString() + ")";
    }
}
