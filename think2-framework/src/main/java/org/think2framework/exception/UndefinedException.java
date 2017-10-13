package org.think2framework.exception;

/**
 * Created by zhoubin on 16/6/1. 未定义异常
 */
public class UndefinedException extends RuntimeException {

	public UndefinedException() {
		super();
	}

	public UndefinedException(String message) {
		super(message + " is undefined");
	}

	public UndefinedException(String message, Throwable cause) {
		super(message + " is undefined", cause);
	}

	public UndefinedException(Throwable cause) {
		super(cause);
	}

	protected UndefinedException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message + " is undefined", cause, enableSuppression, writableStackTrace);
	}

}
