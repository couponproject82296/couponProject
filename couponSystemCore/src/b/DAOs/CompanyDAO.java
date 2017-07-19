package b.DAOs;

import java.util.Collection;
import java.util.Date;

import b.JavaBeans.Company;
import b.JavaBeans.Coupon;
import b.JavaBeans.CouponType;

/**
 * This interface defines the methods that involve the database Company table,
 * which need to be implemented by the corresponding concrete DBDAO class
 */

public interface CompanyDAO {

	/**
	 * public long createCompany(Company company)
	 * 
	 * @param Company
	 * @throws DAOException
	 * @return long, the new id number given for the company by the system
	 */

	long createCompany(Company company) throws DAOException;

	/**
	 * public Company readCompany(long compId)
	 * 
	 * @param long
	 *            represents the company id
	 * @throws DAOException
	 * @return Company, the company whose id was given as a parameter
	 */

	Company readCompany(long compId) throws DAOException;

	/**
	 * public Collection<Company> readAllCompanies()
	 * 
	 * @throws DAOException
	 * @return Collection<Company>, all the companies existing in the database
	 */

	Collection<Company> readAllCompanies() throws DAOException;

	/**
	 * public Coupon readCoupon(long coupId, long compId)
	 * 
	 * @param long
	 *            represents the coupon id
	 * @param long
	 *            represents the company id
	 * @throws DAOException
	 * @return Coupon, the coupon whose id was given as a parameter
	 */

	Coupon readCoupon(long coupId, long compId) throws DAOException;

	/**
	 * public Collection<Coupon> readCoupons(long compId)
	 * 
	 * @param long
	 *            representing a certain company id
	 * @throws DAOException
	 * @return Collection<Coupon>, all the coupons related to the specified
	 *         company
	 */

	Collection<Coupon> readCoupons(long compId) throws DAOException;

	/**
	 * public Collection<Coupon> readCouponsByType(CouponType type, long compId)
	 * 
	 * @param long
	 *            represents the company id
	 * @param CouponType
	 *            represents the coupon type
	 * @throws DAOException
	 * @return Collection<Coupon>, all the coupons related to the specified
	 *         company with the specified type.
	 */

	Collection<Coupon> readCouponsByType(CouponType type, long compId) throws DAOException;

	/**
	 * public Collection<Coupon> readCouponsByPrice(double price, long compId)
	 * 
	 * @param long
	 *            represents the company id
	 * @param double
	 *            represents the coupon price
	 * @throws DAOException
	 * @return Collection<Coupon>, all the coupons related to the specified
	 *         company up to the specified price.
	 */

	Collection<Coupon> readCouponsByPrice(double price, long compId) throws DAOException;

	/**
	 * public Collection<Coupon> readCouponsByDate(Date endDate, long compId)
	 * 
	 * @param long
	 *            represents the company id
	 * @param Date
	 *            represents the end date up to which coupons should be read
	 * @throws DAOException
	 * @return Collection<Coupon>, all the coupons(related to the specified
	 *         company) in the database whose end date is lower than/equal to
	 *         the specified end date
	 */

	Collection<Coupon> readCouponsByDate(Date endDate, long compId) throws DAOException;

	/**
	 * public boolean updateCompany(Company company)
	 * 
	 * @param Company
	 * @throws DAOException
	 * @return boolean, relating to the success of the updating process
	 */

	boolean updateCompany(Company company) throws DAOException;

	/**
	 * public boolean deleteCompany(Company company)
	 * 
	 * @param Company
	 * @throws DAOException
	 * @return boolean, relating to the success of the deleting process
	 */

	boolean deleteCompany(Company company) throws DAOException;

	/**
	 * public void deleteCompanyCoupons(long compId) deletes all company coupons
	 * from all the database tables: Coupon, Company_Coupon and Customer_coupon
	 * 
	 * @param long
	 *            represents the company id
	 * @throws DAOException
	 */

	void deleteCompanyCoupons(long compId) throws DAOException;

	/**
	 * public boolean login(long id, String password)
	 * 
	 * @param long
	 *            represents the company id
	 * @param String
	 *            represents the company password
	 * @throws DAOException
	 * @return boolean, relating to the success of the login process; determined
	 *         based on the accordance of the two parameters to those in table
	 *         Company
	 */

	boolean login(long id, String password) throws DAOException;

	/**
	 * public boolean checkCompanyName(String name)
	 * 
	 * @param String
	 *            checked to make sure that this company name doesn't already
	 *            exist in the database
	 * @throws DAOException
	 * @return boolean, relating to the existence of the name in the database
	 */

	boolean checkCompanyName(String name) throws DAOException;

}