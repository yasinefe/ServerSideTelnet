package com.yefe.telnet.serverside.test.util;

/**
 * I use lock object to write multi threaded unit tests For example one thread starts and waits and the other threads
 * starts and when these threads finishs their logic then first thread will be notified
 */
public class LockObject {

	private long WAITING_DURATION_PER_PERIOD = 10;

	private int count;
	private int index;
	private int maxWaitingPeriod = 1000;
	private boolean locked;

	public LockObject(int count) {
		this.count = count;
	}

	public synchronized void lock() {
		locked = true;
	}

	public static LockObject createLock(int count) {
		return new LockObject(count);
	}

	/**
	 * Creates locked object with count as 1
	 * 
	 * @return LockObject
	 */
	public static LockObject createLock() {
		LockObject lockObject = new LockObject(1);
		return lockObject;
	}

	/**
	 * Creates locked object
	 * 
	 * @param count
	 *            is used to keep how many release will be called to release completely
	 * @return LockObject
	 */
	public static LockObject createLocked(int count) {
		LockObject lockObject = new LockObject(count);
		lockObject.lock();
		return lockObject;
	}

	public static LockObject createLocked() {
		LockObject lockObject = new LockObject(1);
		lockObject.lock();
		return lockObject;
	}

	public synchronized void release() {
		index++;
		if (index == count) {
			locked = false;
		}
	}

	public long waitFor() {
		int waitingPeriod = 0;
		while (locked && waitingPeriod <= maxWaitingPeriod) {
			waitingPeriod++;
			try {
				Thread.sleep(WAITING_DURATION_PER_PERIOD);
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
		}
		return waitingPeriod * WAITING_DURATION_PER_PERIOD;
	}

}