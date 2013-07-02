package com.emobtech.networkingme;

import java.io.IOException;


public final class RequestOperation implements Runnable {
	
	public static interface Listener {
		void onSuccess(Request request, Response response);
		void onFailure(Request request, Exception exception);
	}

	private Request request;
	private Listener listener;
	
	public RequestOperation(Request request) {
		if (request == null) {
			throw new IllegalArgumentException("Request is null!");
		}
		//
		this.request = request;
	}
	
	public void start(Listener listener) {
		this.listener = listener;
		//
		ThreadDispatcher.getInstance().dispatch(this);
	}
	
	public Response start() throws IOException, SecurityException {
		return request.send();
	}

	public void run() {
		try {
			Response response = request.send();
			//
			if (listener != null) {
				listener.onSuccess(request, response);
			}
		} catch (Exception e) {
			if (listener != null) {
				listener.onFailure(request, e);
			}
		}
	}
}
