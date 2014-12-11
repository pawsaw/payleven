package de.psawicki.payleven.rest;

public class PaylevenRESTException extends Exception {

	public PaylevenRESTException() {
		super();
	}

	public PaylevenRESTException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
	}

	public PaylevenRESTException(String detailMessage) {
		super(detailMessage);
	}

	public PaylevenRESTException(Throwable throwable) {
		super(throwable);
	}

}
