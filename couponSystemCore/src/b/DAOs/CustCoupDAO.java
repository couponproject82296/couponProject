package b.DAOs;

import java.util.Collection;

/**
 * This interface defines the methods that involve the database Customer_Coupon
 * table, which need to be implemented by the corresponding concrete DBDAO class
 */

public interface CustCoupDAO {

	/**
	 * public boolean createCoupon(long custId, coupId)
	 * 
	 * @param long
	 *            represents the customer id
	 * @param long
	 *            represents the coupon id
	 * @throws DAOException
	 * @return boolean, relating to the success of the creation process
	 */

	boolean createCoupon(long custId, long coupId) throws DAOException;

	/**
	 * public Collection<Long> readAllCoupons(long custId)
	 * 
	 * @param long
	 *            represents the customer id
	 * @throws DAOException
	 * @return Collection<Long>, all the coupon id numbers related to the
	 *         specified customer
	 */

	Collection<Long> readAllCoupons(long custId) throws DAOException;

	/**
	 * public Collection<Long> readAllCustomers(long coupId)
	 * 
	 * @param long
	 *            represents the coupon id
	 * @throws DAOException
	 * @return Collection<Long>, represents the id numbers of the customers who
	 *         purchased the coupon
	 */

	Collection<Long> readAllCustomers(long coupId) throws DAOException;

	/**
	 * public int deleteCoupon(long coupId) deletes all the coupon records, i.e.
	 * all the customers that have purchased the specified coupon
	 * 
	 * @param long
	 *            represents the coupon id
	 * @throws DAOException
	 * @return int, the number of records deleted from the table
	 */

	int deleteCoupon(long coupId) throws DAOException;

	/**
	 * public int deleteCustomer(long custId) deletes all the customer records,
	 * i.e. all the coupons purchased by the specified customer
	 * 
	 * @param long
	 *            represents the company id
	 * @throws DAOException
	 * @return int, the number of records deleted from the table
	 */

	int deleteCustomer(long custId) throws DAOException;

}