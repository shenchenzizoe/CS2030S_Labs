//@author Shen ChenZi (Group 08)
public class ServiceEnd extends Event{
    private Customer customer;
    private Counter counter;
    private CounterList counterList;
    public ServiceEnd(Customer customer, Counter counter,double time, CounterList counterList){
        super(time);
        this.customer = customer;
        this.counter = counter;
        this.counterList = counterList;
    }
    @Override
    public Event[] simulate() {
        if (!counter.getCounterQue().isEmpty()) {
            Event[] events = new Event[2];
            events[0] = new Departure(this.customer, this.getTime());
            this.customer = counter.deQue();
            events[1] = new ServiceBegin(customer, counter, this.getTime(), counterList);
            return events;
        } else {
            if (!counterList.isEmpty()) {
                Event[] events = new Event[2];
                events[0] = new Departure(this.customer, this.getTime());
                this.customer = counterList.deque();
                events[1] = new ServiceBegin(customer, counter, this.getTime(), counterList);
                return events;
            } else {
                counter.setAvailable();
                return new Event[]{new Departure(this.customer, this.getTime())};
            }

        }
    }
    @Override
    public String toString() {
        return super.toString() + ": " + customer.toString() + " service done (by " + counter.toString() + ")";
    }
}
