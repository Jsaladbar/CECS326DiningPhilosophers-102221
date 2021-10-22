/**
 * Philosopher.java
 *
 * This class represents each philosopher thread.
 * Philosophers alternate between eating and thinking.
 *
 */


public class Philosopher implements Runnable {
	
 	// private variables
	private int philNum;
	private DiningServerImpl dTable;
	private String state;

	// constructor
	public Philosopher() {}
	
	public Philosopher(int philNum, DiningServerImpl dTable) {
		this.philNum = philNum;
		this.dTable = dTable;
		// if even philNum then this Philosopher is thinking
		if (philNum % 1==0) {
			this.state = "THINKING";
		}
		// else this Philosopher is already eating
		else {
			this.state = "EATING";
		}
	}
	
	/**
    * Stalls so the Philosopher can do something.
    * @exception	Throws an Exception if thread is interrupted.
	*/
	private void doSomething() throws InterruptedException {
		// generate random time between 1-3 seconds
		long time = (long)(Math.random() * 2000) + 1000;
		Thread.sleep(time);
	}
	
	
	@Override
	public void run() {
		while (true) {
			try {
				// this Philosopher takes forks off the table to eat
				dTable.takeForks(philNum);
				// stall
				doSomething();
				// this Philosopher returns forks to the table to think
				dTable.returnForks(philNum);
			} catch (Exception e) {
				System.out.println("Exception Caught!");
			}
		}

	}
	
	/**
    * Gets this Philosopher's state;
    * @return        The state that this Philosopher is in.
	*/
	public String getState() {
		return this.state;
	}

	/**
    * Sets this Philosopher's state.
    * @param state	The state the Philosopher is going to be.
	*/
	public void setState(String state) {
		this.state = state;
	}
}