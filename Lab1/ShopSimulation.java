import java.util.Scanner;

/**
 * This class implements a shop simulation.
 *
 * @author Shen Chenzi (Group 08)
 * @version CS2030S AY21/22 Semester 2
 */ 
class ShopSimulation extends Simulation {
  /** 
   * The availability of counters in the shop.
   * [ChenZi] CREATE COUNTERS CLASS
   */

  /** 
   * The list of customer arrival events to populate
   * the simulation with.
   * [ChenZi] CREATE NEW CUSTOMER CLASS
   */
  public Event[] initEvents;

  private CounterList counterList;

  /** 
   * Constructor for a shop simulation. 
   *
   * @param sc A scanner to read the parameters from.  The first
   *           integer scanned is the number of customers; followed
   *           by the number of service counters.  Next is a 
   *           sequence of (arrival time, service time) pair, each
   *           pair represents a customer.
   */
  public ShopSimulation(Scanner sc) {
    initEvents = new Event[sc.nextInt()];
    int numOfCounters = sc.nextInt();
    int CounterQLength = sc.nextInt();
    int QueueLength = sc.nextInt();
    this.counterList = new CounterList(numOfCounters,QueueLength,CounterQLength);
    int id = 0;
    while (sc.hasNextDouble()) {
      double arrivalTime = sc.nextDouble();
      double serviceTime = sc.nextDouble();
      Customer customer = new Customer(serviceTime,arrivalTime);
      initEvents[id] = new Arrival(arrivalTime,customer,this.counterList);
      id ++;
    }
  }

  /**
   * Retrieve an array of events to populate the 
   * simulator with.
   *[ChenZi] ShopEvent Change to ShopEvent(Customer, Counter, sth)
   * Customer provide ID, and arrivalTime
   * Counter provides availability and serviceTime
   * @return An array of events for the simulator.
   */
  @Override
  public Event[] getInitialEvents() {
    return initEvents;
  }
}
