package org.think2framework.exception;

/**
 * 不存在异常
 */
public class NonExistException extends RuntimeException {

	public NonExistException() {
		super();
	}

	public NonExistException(String message) {
		super(message + " is not exist");
	}

	public NonExistException(String message, Throwable cause) {
		super(message + " is not exist", cause);
	}

	public NonExistException(Throwable cause) {
		super(cause);
	}

	protected NonExistException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message + " is not exist", cause, enableSuppression, writableStackTrace);
	}

}
