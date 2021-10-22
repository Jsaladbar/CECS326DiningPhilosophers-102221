/**
 * DiningServer.java
 *
 * This class contains the methods called by the  philosophers.
 *
 */

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class DiningServerImpl implements DiningServer {

	// private variables
	Lock lock = new ReentrantLock();
	Condition[] forks = new Condition[5];
	Philosopher[] philArr = new Philosopher[5];
  long timeRef[] = new long[5];

	// constructor
	public DiningServerImpl() {
		// start threads
		for (int i = 0; i < 5; i++) {
			forks[i] = lock.newCondition();
			Philosopher phil = new Philosopher(i, this);
			philArr[i] = phil;
			Thread philThread = new Thread(philArr[i]);
			philThread.start();
		}
	}
	
	/**
    * Philosopher philNum is taking available forks near him to eat.
    * @param philNum     The corresponding Philosopher.
	*/
@Override
public void takeForks(int philNum) {

	// acquires the lock
	lock.lock();
		
	try {
    // philosopher philNum is checking if he can eat
    philArr[philNum].setState("WAITING");
    timeRef[philNum] = System.currentTimeMillis();
    System.out.println("Philosopher " + (philNum + 1) + " is waiting to eat.");
    // checks if Philosophers next to Philosopher philNum is not eating and Philosopher philNum is waiting to eat
    // if Philosopher philNum can't eat then wait more
    checkFork(philNum);
    if (philArr[philNum].getState() != "EATING") 
			forks[philNum].await();
		} catch (InterruptedException e) {
				System.out.println("Exception Caught!");
    } finally{
        // releases lock
      lock.unlock();
    }

}

	/**
    * Philosopher philNum is returning the forks to the table
    * @param philNum     The corresponding Philosopher
	*/
	@Override
	public void returnForks(int philNum) {

		// acquires the lock
		lock.lock();
		try{
      // Philosopher philNum has finished eating and is now thinking
      philArr[philNum].setState("THINKING");
      System.out.println("Philosopher " + (philNum + 1) + " has finished eating and is now thinking.");
      // if Philosopher philNum can't eat then wait more
      checkFork((philNum+4)%5);
      // if Philosopher philNum can't eat then wait more
      checkFork((philNum+1)%5);
      // releases lock
    }finally{
		  lock.unlock();
    }
	}
	
	/**
    * Checks if fork on the table is available so Philosopher can eat.
    * @param fork     The corresponding fork
	*/
	private void checkFork(int fork) {
    long ttemp = System.currentTimeMillis();
		if ((philArr[(fork + 4) % 5].getState() != "EATING") && (philArr[fork].getState() == "WAITING") && (philArr[(fork + 1) % 5].getState() != "EATING"))  {
			philArr[fork].setState("EATING");
      ttemp -= timeRef[fork];
      System.out.println("Philosopher " + (fork + 1) + " is now eating." + " - - - Waited " + ttemp + "ms");
      forks[fork].signal();
		}
	}

}
