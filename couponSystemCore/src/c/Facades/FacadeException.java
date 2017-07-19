package c.Facades;

import e.CouponSystem.CouponSystemException;

/**
 * This class will allow us to generate custom made checked Exceptions for the
 * different types of Facades
 */

public class FacadeException extends CouponSystemException {

	private static final long serialVersionUID = 1L;

	public FacadeException(String message, Throwable cause) {
		super(message, cause);
	}

	public FacadeException(String message) {
		super(message);
	}

}