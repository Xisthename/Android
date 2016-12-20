package se.mah.ae3317.friendfinder;

import java.util.LinkedList;

/**
 * Created by xisth on 2016-09-30.
 */
public class Buffer<T> {
	private LinkedList<T> buffer = new LinkedList<T>();
	
	public synchronized void put(T element) {
		buffer.addLast(element);
		notifyAll();
	}
	
	public synchronized T get() throws InterruptedException {
		while(buffer.isEmpty()) {
			wait();
		}
		return buffer.removeFirst();
	}
}
