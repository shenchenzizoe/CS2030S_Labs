public class TakeQueue extends Event {
    private Customer customer;
    private CounterList counterList;

    public TakeQueue(Customer customer, CounterList counterList, double time) {
        super(time);
        this.customer = customer;
        this.counterList = counterList;
    }

    @Override
    public Event[] simulate() {
        this.counterList.que(customer);
        return new Event[]{};
    }

    @Override
    public String toString() {
        return super.toString() + ": " + customer.toString() + " joined shop queue " + counterList.qString();
    }

}
