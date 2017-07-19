package e.CouponSystem;

/**
 * This class will serve custom exceptions derived from the coupon system
 */

public class CouponSystemException extends Exception {

	private static final long serialVersionUID = 1L;

	public CouponSystemException(String message, Throwable cause) {
		super(message, cause);
	}

	public CouponSystemException(String message) {
		super(message);
	}

}