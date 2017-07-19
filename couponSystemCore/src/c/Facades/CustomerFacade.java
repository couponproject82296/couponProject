package c.Facades;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;

import b.DAOs.CouponDAO;
import b.DAOs.CustCoupDAO;
import b.DAOs.CustomerDAO;
import b.DAOs.DAOException;
import b.DBDAOs.CouponDBDAO;
import b.DBDAOs.CustCoupDBDAO;
import b.DBDAOs.CustomerDBDAO;
import b.JavaBeans.Coupon;
import b.JavaBeans.CouponType;
import b.JavaBeans.Customer;

/**
 * This class will serve to determine customer permissions
 */

public class CustomerFacade implements CouponClientFacade {

	// Attributes (the DAO instances themselves will be created via the CTOR)

	private CustomerDAO customerDAO;
	private CouponDAO couponDAO;
	private CustCoupDAO custCoupDAO;
	private long customerId;

	// CTOR (creates both the CustomerFacade and the DAO instances)

	public CustomerFacade(long customerId) throws FacadeException {
		try {
			customerDAO = new CustomerDBDAO();
			couponDAO = new CouponDBDAO();
			custCoupDAO = new CustCoupDBDAO();
			this.customerId = customerId;
		} catch (DAOException e) {
			throw new FacadeException("Problem in creating the customer access display.", e);
		}
	}

	// Methods related to operations on coupon purchase in the database

	/**
	 * public boolean purchaseCoupon(Coupon coupon) creates the coupon in table
	 * Customer_Coupon (customer's history of coupon purchase); updates the
	 * amount of the coupon that was purchased in table Coupon
	 * 
	 * @param Coupon
	 * @throws FacadeException
	 * @return boolean, relating to the success of the entire purchase process
	 */

	public boolean purchaseCoupon(Coupon coupon) throws FacadeException {

		try {

			// Firstly, we need to make sure that the coupon exists in table
			// Coupon (i.e. exists in the database)

			Coupon coup = couponDAO.readCoupon(coupon.getId());

			// Secondly, we need to make sure that the coupon is still in stock

			int currentAmount = coup.getAmount();
			if (currentAmount == 0) {
				throw new FacadeException("The coupon was not purchased since it is out of stock.");
			}

			// Thirdly, we need to make sure that the coupon is still valid
			// (i.e. that the end date of the coupon has yet to pass), in case
			// the daily thread has yet to delete the expired coupons or has
			// failed to do so, or in case the company has updated the end date

			Date date = Calendar.getInstance().getTime();
			if (date.after(coup.getEndDate())) {
				throw new FacadeException("The coupon was not purchased since it is no longer valid.");
			}

			// Then, we need to add the coupon to the customer's purchase
			// history (if the customer has already purchased this coupon, an
			// Exception will be thrown, since the COUPON_ID - being a primary
			// key - must be unique)

			custCoupDAO.createCoupon(this.customerId, coupon.getId());

			// Lastly, if we are here, we need to update the amount of the
			// coupon in table Coupon

			coupon.setAmount(currentAmount - 1);
			couponDAO.updateCoupon(coupon);

		} catch (DAOException e) {
			throw new FacadeException("A problem arose in the attempt to purchase the coupon.", e);
		}
		return true; // No Exceptions thrown

	}

	/**
	 * public Customer readCustomer()
	 * 
	 * @throws FacadeException
	 * @return Customer, the customer that is logged-in
	 */

	public Customer readCustomer() throws FacadeException {

		Customer customer = null;

		try {
			customer = customerDAO.readCustomer(this.customerId);
		} catch (DAOException e) {
			throw new FacadeException("A problem arose in the attempt to read the customer.", e);
		}
		return customer;

	}

	/**
	 * public Collection<Coupon> readAllPurchasedCoupons()
	 * 
	 * @throws FacadeException
	 * @return Collection<Coupon>, all the coupons related to the specified
	 *         customer
	 */

	public Collection<Coupon> readAllPurchasedCoupons() throws FacadeException {

		Collection<Coupon> coupons = new LinkedList<>();

		try {
			coupons = customerDAO.readCoupons(this.customerId);
		} catch (DAOException e) {
			throw new FacadeException("A problem arose in the attempt to read the customer coupons.", e);
		}
		return coupons;

	}

	/**
	 * public Collection<Coupon> readAllPurchasedCouponsByType(CouponType type)
	 * 
	 * @param CouponType
	 *            represents the requested coupon category
	 * @throws FacadeException
	 * @return Collection<Coupon>, all the coupons of the logged-in customer of
	 *         the denoted type
	 */

	public Collection<Coupon> readAllPurchasedCouponsByType(CouponType type) throws FacadeException {

		Collection<Coupon> coupons = new LinkedList<>();

		try {
			coupons = customerDAO.readCouponsByType(type, this.customerId);
		} catch (DAOException e) {
			throw new FacadeException(
					"A problem arose in the attempt to read the customer coupons of type " + type + ".", e);
		}
		return coupons;

	}

	/**
	 * public Collection<Coupon> readAllPurchasedCouponsByPrice(double price)
	 * 
	 * @param double
	 *            represents the requested maximum coupon price
	 * @throws FacadeException
	 * @return Collection<Coupon>, all the coupons of the logged-in customer
	 *         whose price is lower than/equal to the denoted price
	 */

	public Collection<Coupon> readAllPurchasedCouponsByPrice(double price) throws FacadeException {

		Collection<Coupon> coupons = new LinkedList<>();

		try {
			coupons = customerDAO.readCouponsByPrice(price, this.customerId);
		} catch (DAOException e) {
			throw new FacadeException(
					"A problem arose in the attempt to read the customer coupons, costing up to " + price + ".", e);
		}
		return coupons;

	}

	/**
	 * public Collection<Coupon> readAllCoupons()
	 * 
	 * @throws FacadeException
	 * @return Collection<Coupon>, all the coupons in the system
	 */

	public Collection<Coupon> readAllCoupons() throws FacadeException {

		Collection<Coupon> coupons = new LinkedList<>();

		try {
			coupons = couponDAO.readAllCoupons();
		} catch (DAOException e) {
			throw new FacadeException("A problem arose in the attempt to read all the coupons in the system.", e);
		}
		return coupons;

	}

	// The following method was added for the user interface (within the client
	// side) of the method purchaseCoupon (displaying all unpurchased coupons)

	/**
	 * public Collection<Coupon> readAllUnpurchasedCoupons()
	 * 
	 * @throws FacadeException
	 * @return Collection<Coupon>, all the coupons not purchased by the
	 *         specified customer
	 */

	public Collection<Coupon> readAllUnpurchasedCoupons() throws FacadeException {

		Collection<Coupon> coupons = new LinkedList<>();

		try {
			coupons = customerDAO.readUnpurchasedCoupons(this.customerId);
		} catch (DAOException e) {
			throw new FacadeException("A problem arose in the attempt to read the customer's unpurchased coupons.", e);
		}
		return coupons;

	}

}