/* ThreadDispatcher.java
 * 
 * Copyright (c) 2013 eMob Tech (http://www.emobtech.com/)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
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
