package org.think2framework.exception;

/**
 * 不支持的异常
 */
public class NonsupportException extends RuntimeException {

	public NonsupportException() {
		super();
	}

	public NonsupportException(String message) {
		super(message + " is not supported");
	}

	public NonsupportException(String message, Throwable cause) {
		super(message + " is not supported", cause);
	}

	public NonsupportException(Throwable cause) {
		super(cause);
	}

	protected NonsupportException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message + " is not supported", cause, enableSuppression, writableStackTrace);
	}

}
