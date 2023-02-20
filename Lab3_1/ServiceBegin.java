//@author Shen ChenZi (Group 08)
public class ServiceBegin extends Event {
    private Customer customer;
    private Counter counter;
    private CounterList counterList;

    public ServiceBegin(Customer customer, Counter counter, double time, CounterList counterList) {
        super(time);
        this.counter = counter;
        this.customer = customer;
        this.counterList = counterList;
    }

    @Override
    public Event[] simulate() {
        counter.setUnavailable();
        double finalTime = this.getTime() + customer.getServiceTime();
        Event[] events = new Event[1];
        events[0] = new ServiceEnd(this.customer, this.counter, finalTime, counterList);
        return events;
    }

    @Override
    public String toString() {
        return super.toString() + ": " + customer.toString() +
                " service begin (by " + counter.toString() + " " + counter.getCounterQue().toString() + ")";
    }

}
