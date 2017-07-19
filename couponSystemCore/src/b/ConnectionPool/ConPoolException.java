package b.ConnectionPool;

import e.CouponSystem.CouponSystemException;

/**
 * This class represents an exceptional event where a problem occurred in the
 * attempt to create the connection pool instance; or where the connection pool
 * was closed while users were either still working on the system or trying to
 * connect to it
 */

public class ConPoolException extends CouponSystemException {

	private static final long serialVersionUID = 1L;

	public ConPoolException(String message, Throwable cause) {
		super(message, cause);
	}

	public ConPoolException(String message) {
		super(message);
	}

}