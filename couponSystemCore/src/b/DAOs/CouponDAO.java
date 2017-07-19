package b.DAOs;

import java.util.Collection;
import java.util.Date;

import b.JavaBeans.Coupon;
import b.JavaBeans.CouponType;

/**
 * This interface defines the methods that involve the database Coupon table,
 * which need to be implemented by the corresponding concrete DBDAO class
 */

public interface CouponDAO {

	/**
	 * public long createCoupon(Coupon coupon)
	 * 
	 * @param Coupon
	 * @throws DAOException
	 * @return long, the new id number given for the coupon by the system
	 */

	long createCoupon(Coupon coupon) throws DAOException;

	/**
	 * public Coupon readCoupon(long coupId)
	 * 
	 * @param long
	 *            represents the coupon id
	 * @throws DAOException
	 * @return Coupon, the coupon whose id was given as a parameter
	 */

	Coupon readCoupon(long coupId) throws DAOException;

	/**
	 * public Collection<Coupon> readAllCoupons()
	 * 
	 * @throws DAOException
	 * @return Collection<Coupon>, all the coupons in the database
	 */

	Collection<Coupon> readAllCoupons() throws DAOException;

	/**
	 * public Collection<Coupon> readCouponByType(CouponType type)
	 * 
	 * @throws DAOException
	 * @return Collection<Coupon>, all the coupons in the database of the
	 *         specified type
	 */

	Collection<Coupon> readCouponByType(CouponType type) throws DAOException;

	/**
	 * public Collection<Coupon> readCouponByPrice(double price)
	 * 
	 * @param double
	 *            represents the price up to which coupon results should be read
	 * @throws DAOException
	 * @return Collection<Coupon>, all the coupons in the database whose price
	 *         is lower than/equal to the specified price
	 */

	Collection<Coupon> readCouponByPrice(double price) throws DAOException;

	/**
	 * public Collection<Coupon> readCouponByDate(Date endDate)
	 * 
	 * @param Date
	 *            represents the end date up to which coupons should be read
	 * @throws DAOException
	 * @return Collection<Coupon>, all the coupons in the database whose end
	 *         date is lower than/equal to the specified end date
	 */

	Collection<Coupon> readCouponByDate(Date endDate) throws DAOException;

	/**
	 * public boolean updateCoupon(Coupon coupon)
	 * 
	 * @param Coupon
	 * @throws DAOException
	 * @return boolean, relating to the success of the updating process
	 */

	boolean updateCoupon(Coupon coupon) throws DAOException;

	/**
	 * public boolean deleteCoupon(Coupon coupon)
	 * 
	 * @param Coupon
	 * @throws DAOException
	 * @return boolean, relating to the success of the deleting process
	 */

	boolean deleteCoupon(Coupon coupon) throws DAOException;

	/**
	 * public void deleteExpiredCoupons(Date endDate) deletes all expired
	 * coupons from all the database tables: Coupon, Company_Coupon and
	 * Customer_coupon
	 * 
	 * @param Date
	 *            represents the end date up to which coupons should be deleted
	 * @throws DAOException
	 */

	void deleteExpiredCoupons(Date endDate) throws DAOException;

	/**
	 * public boolean checkCouponTitle(String title)
	 * 
	 * @param String
	 *            checked to make sure that this coupon title doesn't already
	 *            exist in the database
	 * @throws DAOException
	 * @return boolean, relating to the existence of the title in the database
	 */

	boolean checkCouponTitle(String title) throws DAOException;

}