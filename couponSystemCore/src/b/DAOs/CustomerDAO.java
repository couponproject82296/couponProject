package b.DAOs;

import java.util.Collection;

import b.JavaBeans.Coupon;
import b.JavaBeans.CouponType;
import b.JavaBeans.Customer;

/**
 * This interface defines the methods that involve the database Customer table,
 * which need to be implemented by the corresponding concrete DBDAO class
 */

public interface CustomerDAO {

	/**
	 * public long createCustomer(Customer customer)
	 * 
	 * @param Customer
	 * @throws DAOException
	 * @return long, the new id number given for the customer by the system
	 */

	long createCustomer(Customer customer) throws DAOException;

	/**
	 * public Customer readCustomer(long custId)
	 * 
	 * @param long
	 *            represents the customer id
	 * @throws DAOException
	 * @return Customer, the customer whose id was given as a parameter
	 */

	Customer readCustomer(long custId) throws DAOException;

	/**
	 * public Collection<Customer> readAllCustomers()
	 * 
	 * @throws DAOException
	 * @return Collection<Customer>, all the customers in the database
	 */

	Collection<Customer> readAllCustomers() throws DAOException;

	/**
	 * public Collection<Coupon> readCoupons(long custId)
	 * 
	 * @param long
	 *            representing a certain customer id
	 * @throws DAOException
	 * @return Collection<Coupon>, all the coupons related to the specified
	 *         customer
	 */

	Collection<Coupon> readCoupons(long custId) throws DAOException;

	/**
	 * public Collection<Coupon> readCouponsByType(CouponType type, long custId)
	 * 
	 * @param long
	 *            represents the customer id
	 * @param CouponType
	 *            represents the coupon type
	 * @throws DAOException
	 * @return Collection<Coupon>, all the coupons related to the specified
	 *         customer by the specified type.
	 */

	Collection<Coupon> readCouponsByType(CouponType type, long custId) throws DAOException;

	/**
	 * public Collection<Coupon> readCouponsByPrice(double price, long custId)
	 * 
	 * @param long
	 *            represents the customer id
	 * @param double
	 *            represents the coupon price
	 * @throws DAOException
	 * @return Collection<Coupon>, all the coupons related to the specified
	 *         customer up to the specified price.
	 */

	Collection<Coupon> readCouponsByPrice(double price, long custId) throws DAOException;

	/**
	 * public boolean updateCustomer(Customer customer)
	 * 
	 * @param Customer
	 * @throws DAOException
	 * @return boolean, relating to the success of the updating process
	 */

	boolean updateCustomer(Customer customer) throws DAOException;

	/**
	 * public boolean deleteCustomer(Customer customer)
	 * 
	 * @param Customer
	 * @throws DAOException
	 * @return boolean, relating to the success of the deleting process
	 */

	boolean deleteCustomer(Customer customer) throws DAOException;

	/**
	 * public void deleteCustomerCoupons(long custId) deletes all customer
	 * coupons from table Customer_coupon
	 * 
	 * @param long
	 *            represents the customer id
	 * @throws DAOException
	 */

	void deleteCustomerCoupons(long custId) throws DAOException;

	/**
	 * public boolean login(long id, String password)
	 * 
	 * @param long
	 *            represents the customer id
	 * @param String
	 *            represents the customer password
	 * @throws DAOException
	 * @return boolean, relating to the success of the login process; determined
	 *         based on the accordance of the two parameters to those in table
	 *         Customer
	 */

	boolean login(long id, String password) throws DAOException;

	/**
	 * public boolean checkCustomerName(String name)
	 * 
	 * @param String
	 *            checked to make sure that this customer name doesn't already
	 *            exist in the database
	 * @throws DAOException
	 * @return boolean, relating to the existence of the name in the database
	 */

	boolean checkCustomerName(String name) throws DAOException;

	// The following method was added for the user interface (within the client
	// side) of the method purchaseCoupon (displaying all unpurchased coupons)

	/**
	 * public Collection<Coupon> readUnpurchasedCoupons(long custId)
	 * 
	 * @param long
	 *            representing a certain customer id
	 * @throws DAOException
	 * @return Collection<Coupon>, all the coupons unpurchased by the specified
	 *         customer
	 */

	Collection<Coupon> readUnpurchasedCoupons(long custId) throws DAOException;

}