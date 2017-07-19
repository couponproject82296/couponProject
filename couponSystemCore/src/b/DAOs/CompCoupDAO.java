package b.DAOs;

import java.util.Collection;

/**
 * This interface defines the methods that involve the database Company_Coupon
 * table, which need to be implemented by the corresponding concrete DBDAO class
 */

public interface CompCoupDAO {

	/**
	 * public boolean createCoupon(long compId, long coupId)
	 * 
	 * @param long
	 *            represents the company id
	 * @param long
	 *            represents the coupon id
	 * @throws DAOException
	 * @return boolean, relating to the success of the creation process
	 */

	boolean createCoupon(long compId, long coupId) throws DAOException;

	/**
	 * public Collection<Long> readAllCoupons(long compId)
	 * 
	 * @param long
	 *            represents the company id
	 * @throws DAOException
	 * @return Collection<Long>, all the coupon id numbers related to the
	 *         specified company
	 */

	Collection<Long> readAllCoupons(long compId) throws DAOException;

	/**
	 * public long readCompany(long coupId)
	 * 
	 * @param long
	 *            represents the coupon id
	 * @throws DAOException
	 * @return long, represents the id of the company who issued the coupon
	 */

	long readCompany(long coupId) throws DAOException;

	/**
	 * public boolean deleteCoupon(long coupId)
	 * 
	 * @param long
	 *            represents the coupon id
	 * @throws DAOException
	 * @return boolean, relating to the success of the deleting process
	 */

	boolean deleteCoupon(long coupId) throws DAOException;

	/**
	 * public int deleteCompany(long compId) deletes all the company records,
	 * i.e. all the coupons belonging to the specified company
	 * 
	 * @param long
	 *            represents the company id
	 * @throws DAOException
	 * @return int, the number of records deleted from the table
	 */

	int deleteCompany(long compId) throws DAOException;

}