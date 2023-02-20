//@author Shen ChenZi (Group 08)
public class Arrival extends Event {
    private Customer customer;
    private CounterList counterList;

    public Arrival(double arriveTime, Customer customer, CounterList counterList){
        super(arriveTime);
        this.customer = customer;
        this.counterList = counterList;
    }
    @Override
    public Event[] simulate(){
        Counter counter = this.counterList.findCounter();
        Event[] events = new Event[1];
        if(counter != null){
           if(counter.isAvailable()){
               events[0] = new ServiceBegin(this.customer,counter,this.getTime(),this.counterList);
           }
           else {
               events[0] = new QueCounter(this.customer, counter,this.getTime());
           }
        }
        else {
            if(counterList.isFull()) {
                events[0] = new Departure(customer, this.getTime());
            }
            else {
               events[0] = new TakeQueue(customer,counterList,this.getTime());
               //add the customer into the queue
            }
        }
        return events;

    }
    @Override
    public String toString() {
        return super.toString() + ": " + customer.toString() + " arrived " + counterList.qString();
    }
}
