package com.emobtech.networkingme;

import java.io.IOException;

public class RequestOperation {
	
	private Request request;
	private Listener listener;
	
	public RequestOperation(Request request) {
		this.request = request;
	}

	public RequestOperation(Request request, Listener listener) {
		this.request = request;
		this.listener = listener;
	}
	
	public void setListener(Listener listener) {
		this.listener = listener;
	}
	
	public void start() {
		Response response = null;
		try {
			response = request.send();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//
		if (listener != null) {
			if (response.wasSuccessfull()) {
				listener.onSuccess(request, response);
			} else {
				listener.onFailure(request, response);
			}
		}
	}

	public static interface Listener {
		void onSuccess(Request request, Response response);
		void onFailure(Request request, Response response);
	}
}

