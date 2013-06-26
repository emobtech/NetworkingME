package com.emobtech.networkingme;

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
		Response response = request.send();
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

