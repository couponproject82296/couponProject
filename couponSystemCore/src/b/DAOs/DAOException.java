package b.DAOs;

import e.CouponSystem.CouponSystemException;

/**
 * This class will help us generate custom made checked Exceptions specifically
 * related to the Data Access Objects
 */

public class DAOException extends CouponSystemException {

	private static final long serialVersionUID = 1L;

	public DAOException(String message, Throwable cause) {
		super(message, cause);
	}

	public DAOException(String message) {
		super(message);
	}

}