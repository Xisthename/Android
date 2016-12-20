package se.mah.ae3317.friendfinder;

/**
 * Created by xisth on 2016-09-30.
 */
public class RunOnThread {
	private Buffer<Runnable> buffer = new Buffer<Runnable>();
	private Worker worker;
	
	public void start() {
		if (worker == null) {
		    worker = new Worker();
		    worker.start();
		}
	}
	
	public void stop() {
		if (worker != null) {
		    worker.interrupt();
		    worker = null;
		}
	}
	
	public void execute(Runnable runnable) {
		buffer.put(runnable);
	}
	
	private class Worker extends Thread {
		public void run() {
			Runnable runnable;
			while (worker!= null) {
				try {
					runnable = buffer.get();
					runnable.run();
				}
				catch (InterruptedException e) {
					worker = null;
				}
			}
		}
	}
}
