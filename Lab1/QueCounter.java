public class QueCounter extends Event {
    private Customer customer;
    private Counter counter;

    QueCounter(Customer customer,Counter counter,double time){
        super(time);
        this.counter = counter;
        this.customer = customer;
    }

    @Override
    public double getTime() {
        return super.getTime();
    }

    @Override
    public Event[] simulate(){
        this.counter.joinQue(customer);
        return new Event[]{};
    }

    @Override
    public String toString() {
        return super.toString() + ": " + customer.toString() + " joined counter queue (at " +
                counter.toString() + " " + counter.getCounterQue().toString() + ")";
    }
}
