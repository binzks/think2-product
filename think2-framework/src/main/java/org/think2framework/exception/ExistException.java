package org.think2framework.exception;

/**
 * Created by zhoubin on 16/6/1. 已存在异常
 */
public class ExistException extends RuntimeException {

	public ExistException() {
		super();
	}

	public ExistException(String message) {
		super(message + " is already exist");
	}

	public ExistException(String message, Throwable cause) {
		super(message + " is already exist", cause);
	}

	public ExistException(Throwable cause) {
		super(cause);
	}

	protected ExistException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message + " is already exist", cause, enableSuppression, writableStackTrace);
	}

}
