//@author Shen ChenZi (Group 08)
public class Customer {
    private static int count = 0;
    private double arrivalTime;
    private double serviceTime;
    private int id;

    public Customer(double serviceTime,double arrivalTime) {
        this.id = count;
        this.serviceTime = serviceTime;
        this.arrivalTime = arrivalTime;
        count++;
    }

    public double getServiceTime() {
        return serviceTime;
    }

    public String toString() {
        return "C" + id;
    }
}

