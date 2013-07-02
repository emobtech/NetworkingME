package com.emobtech.networkingme;

import java.util.Vector;

final class ThreadDispatcher implements Runnable {

	private static ThreadDispatcher singleton;
	private Vector queue;
	private Thread thread;

	public synchronized static ThreadDispatcher getInstance() {
		if (singleton == null) {
			singleton = new ThreadDispatcher();
		}
		//
		return singleton;
	}
	
	public void dispatch(Runnable runnable) {
		synchronized (queue) {
			queue.addElement(runnable);
			queue.notifyAll();
		}
	}
	
	public void run() {
		try {
			Runnable process = null;
			//
			while (true) {
                synchronized (queue) {
                    if (queue.size() > 0) {
                    	process = (Runnable)queue.elementAt(0);
                    	queue.removeElementAt(0);
                    } else {
                    	queue.wait();
                    }
                }
                //
                if (process != null) {
                    process.run();
                    process = null;
                }
            }
        } catch (Throwable t) {
        	t.printStackTrace();
        }		
	}
	
	private ThreadDispatcher() {
		queue = new Vector(5);
		thread = new Thread(this);
		//
		thread.start();
	}
}
