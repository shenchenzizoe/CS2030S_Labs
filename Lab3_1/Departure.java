//@author Shen ChenZi (Group 08)
public class Departure extends Event {
    private Customer customer;

    public Departure(Customer customer, double time) {
        super(time);
        this.customer = customer;
    }

    @Override
    public Event[] simulate() {
        return new Event[]{};
    }

    @Override
    public String toString() {
        return super.toString() + ": " + customer.toString() + " departed";
    }
}
