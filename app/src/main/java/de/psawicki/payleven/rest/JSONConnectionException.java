package de.psawicki.payleven.rest;

import java.io.IOException;

import org.json.JSONObject;

public class JSONConnectionException extends IOException {
	
	/**
	 * For serialization.
	 */
	private static final long serialVersionUID = 5115622997733633251L;

	public static enum Error {
		
		BAD_REQUEST("BAD_REQUEST"),
		ILLEGAL_STATE("ILLEGAL_STATE"),
		INTERNAL_SERVER_ERROR("INTERNAL_SERVER_ERROR"),
		NOT_FOUND("NOT_FOUND"),
		FORBIDDEN("FORBIDDEN"),
		MALFORMED_JSON("MALFORMED_JSON"),
		UNKNOWN("UNKNOWN");
		
		private Error(String value) {
			this.value = value;
		}
		
		public String value;
	}

	private Error error;
	private String message;

	public JSONConnectionException(JSONObject responseJSON) {
		super();
		try {
			this.error = Error.valueOf(responseJSON.getString("error"));
		} catch (Exception e) {
			this.error = Error.UNKNOWN;
		}
		
		try {
			this.message = responseJSON.getString("msg");
		} catch (Exception e) {
			this.message = Error.UNKNOWN.value;
		}
	}
	
	public JSONConnectionException(Error error, String message) {
		super();
		this.error = error;
		this.message = message;
	}



	public Error getError() {
		return error;
	}

	public String getMessage() {
		return message;
	}

	
}
