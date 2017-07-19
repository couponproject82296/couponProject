package c.Facades;

import java.sql.Date;
import java.util.Calendar;
import java.util.Collection;
import java.util.LinkedList;

import b.DAOs.CompCoupDAO;
import b.DAOs.CompanyDAO;
import b.DAOs.CouponDAO;
import b.DAOs.CustCoupDAO;
import b.DAOs.DAOException;
import b.DBDAOs.CompCoupDBDAO;
import b.DBDAOs.CompanyDBDAO;
import b.DBDAOs.CouponDBDAO;
import b.DBDAOs.CustCoupDBDAO;
import b.JavaBeans.Company;
import b.JavaBeans.Coupon;
import b.JavaBeans.CouponType;

/**
 * This class will serve to determine company permissions
 */

public class CompanyFacade implements CouponClientFacade {

	// Attributes (the DAO instances themselves will be created via the CTOR)

	private CompanyDAO companyDAO;
	private CouponDAO couponDAO;
	private CompCoupDAO compCoupDAO;
	private CustCoupDAO custCoupDAO;
	private long companyId;

	// CTOR (creates both the CompanyFacade and the DAO instances)

	public CompanyFacade(long companyId) throws FacadeException {
		try {
			companyDAO = new CompanyDBDAO();
			couponDAO = new CouponDBDAO();
			compCoupDAO = new CompCoupDBDAO();
			custCoupDAO = new CustCoupDBDAO();
			this.companyId = companyId;
		} catch (DAOException e) {
			throw new FacadeException("Problem in creating the company access display.", e);
		}
	}

	// CRUD Methods related to operations on coupons in the database

	/**
	 * public long createCoupon(Coupon coupon) creates a unique coupon in Coupon
	 * and in Company_Coupon
	 * 
	 * @param Coupon
	 *            checked according to coupon title to make sure it doesn't
	 *            already exist in the database, in addition to verifying that
	 *            all coupon fields of type String aren't null and that the
	 *            dates, amount and price are legitimate values
	 * @throws FacadeException
	 * @return long, the new id number given for the coupon by the system
	 */

	public long createCoupon(Coupon coupon) throws FacadeException {

		long id = 0;

		try {

			// Firstly, we need to make sure that the coupon fields of type
			// String (or Enum) don't include any null values

			if (coupon.getTitle() == null || coupon.getTitle().length() == 0 || coupon.getType() == null
					|| coupon.getType().toString().length() == 0 || coupon.getMessage() == null
					|| coupon.getMessage().length() == 0 || coupon.getImage() == null
					|| coupon.getImage().length() == 0) {
				throw new FacadeException(
						"The coupon was not created since the title/type/message/image of the coupon can not be null.");
			}

			// Secondly, we need to make sure that the coupon title doesn't
			// already exist in the database

			if (couponDAO.checkCouponTitle(coupon.getTitle())) {
				throw new FacadeException("The coupon was not created since the coupon " + coupon.getTitle()
						+ " already exists under this title.");
			}

			// Thirdly, we need to make sure that the start date and the end
			// date of the coupon are legitimate values

			java.util.Date currentDate = Calendar.getInstance().getTime();
			if (coupon.getStartDate().before(currentDate)) {
				throw new FacadeException("The coupon was not created since the start date of the coupon ("
						+ coupon.getStartDate() + ") can not precede the current date (" + currentDate + ")");
			}

			if (coupon.getEndDate().before(coupon.getStartDate())) {
				throw new FacadeException("The coupon was not created since the end date of the coupon ("
						+ coupon.getEndDate() + ") can not precede its start date (" + coupon.getStartDate() + ")");
			}

			// Then, we need to make sure that the amount and price are
			// legitimate values (i.e. positive)

			if (coupon.getAmount() <= 0) {
				throw new FacadeException(
						"The coupon was not created since the coupon amount must receive a positive value.");
			}

			if (coupon.getPrice() <= 0) {
				throw new FacadeException(
						"The coupon was not created since the coupon price must receive a positive value.");
			}

			// Lastly, if the coupon title doesn't already exist, and all the
			// fields are properly defined, we need to create the new coupon in
			// the database: in table Coupon and in table Company_Coupon

			id = couponDAO.createCoupon(coupon);
			compCoupDAO.createCoupon(this.companyId, id);

		} catch (DAOException e) {
			throw new FacadeException("A problem arose in the attempt to create the company coupon.", e);
		}
		return id;

	}

	/**
	 * public Company readCompany()
	 * 
	 * @throws FacadeException
	 * @return Company, the company that is logged-in
	 */

	public Company readCompany() throws FacadeException {

		Company company = null;

		try {
			company = companyDAO.readCompany(this.companyId);
		} catch (DAOException e) {
			throw new FacadeException("A problem arose in the attempt to read the company.", e);
		}
		return company;

	}

	/**
	 * public Coupon readCoupon(long coupId)
	 * 
	 * @param long
	 *            represents the coupon id
	 * @throws FacadeException
	 * @return Coupon, the coupon whose id was given as a parameter
	 */

	public Coupon readCoupon(long coupId) throws FacadeException {

		Coupon coupon = null;

		try {
			coupon = companyDAO.readCoupon(coupId, this.companyId);
		} catch (DAOException e) {
			throw new FacadeException("A problem arose in the attempt to read the company coupon.", e);
		}
		return coupon;

	}

	/**
	 * public Collection<Coupon> readAllCoupons()
	 * 
	 * @throws FacadeException
	 * @return Collection<Coupon>, all the coupons related to the specific
	 *         logged-in company
	 */

	public Collection<Coupon> readAllCoupons() throws FacadeException {

		Collection<Coupon> coupons = null;

		try {
			coupons = companyDAO.readCoupons(this.companyId);
		} catch (DAOException e) {
			throw new FacadeException("A problem arose in the attempt to read the company coupons.", e);
		}
		return coupons;

	}

	/**
	 * public Collection<Coupon> readCouponByType(CouponType type)
	 * 
	 * @param CouponType
	 *            represents the requested coupon category
	 * @throws FacadeException
	 * @return Collection<Coupon>, all the coupons of the logged-in company of
	 *         the denoted type
	 */

	public Collection<Coupon> readCouponByType(CouponType type) throws FacadeException {

		Collection<Coupon> coupons = new LinkedList<>();

		try {
			coupons = companyDAO.readCouponsByType(type, this.companyId);
		} catch (DAOException e) {
			throw new FacadeException(
					"A problem arose in the attempt to read the company coupons of type " + type + ".", e);
		}
		return coupons;
	}

	/**
	 * public Collection<Coupon> readCouponByPrice(double price)
	 * 
	 * @param double
	 *            represents the price up to which coupon results should be read
	 * @throws FacadeException
	 * @return Collection<Coupon>, all the coupons of the logged-in company
	 *         whose price is lower than/equal to the specified price
	 */

	public Collection<Coupon> readCouponByPrice(double price) throws FacadeException {

		Collection<Coupon> coupons = new LinkedList<>();

		try {
			coupons = companyDAO.readCouponsByPrice(price, this.companyId);
		} catch (DAOException e) {
			throw new FacadeException(
					"A problem arose in the attempt to read the company coupons, costing up to " + price + ".", e);
		}
		return coupons;
	}

	/**
	 * public Collection<Coupon> readCouponByDate(Date endDate)
	 * 
	 * @param Date
	 *            represents the end date up to which coupon results should be
	 *            read
	 * @throws FacadeException
	 * @return Collection<Coupon>, all the coupons of the logged-in company
	 *         whose end date is lower than/equal to the specified end date
	 */

	public Collection<Coupon> readCouponByDate(Date endDate) throws FacadeException {

		Collection<Coupon> coupons = new LinkedList<>();

		try {
			coupons = companyDAO.readCouponsByDate(endDate, this.companyId);
		} catch (DAOException e) {
			throw new FacadeException(
					"A problem arose in the attempt to read the company coupons, ending till " + endDate + ".", e);
		}
		return coupons;
	}

	/**
	 * public boolean updateCoupon(Coupon coupon) updates only the end date and
	 * the price of the coupon
	 * 
	 * @param Company
	 * @throws FacadeException
	 * @return boolean, relating to the success of the updating process
	 */

	public boolean updateCoupon(Coupon coupon) throws FacadeException {

		try {

			// Firstly, we need to read the current coupon from the database
			// (an Exception will be thrown if the coupon doesn't exist or
			// doesn't belong to the company)

			Coupon currentCoupon = companyDAO.readCoupon(coupon.getId(), this.companyId);

			// Secondly, we need to make sure that coupon fields designated for
			// update are legitimate values

			if (coupon.getEndDate().before(currentCoupon.getStartDate())) {
				throw new FacadeException("The coupon was not updated since the end date of the Coupon ("
						+ coupon.getEndDate() + ") can not precede its start date (" + coupon.getStartDate() + ")");
			}

			if (coupon.getPrice() <= 0) {
				throw new FacadeException(
						"The coupon was not updated since the coupon price must receive a positive value.");
			}

			// Thirdly, we need to update the fields of the end date and price

			currentCoupon.setEndDate(coupon.getEndDate());
			currentCoupon.setPrice(coupon.getPrice());

			// Next, we need to update the altered coupon in the database

			couponDAO.updateCoupon(currentCoupon);

			// Lastly, we need to update the fields other than the end date and
			// price of the coupon instance, given as a parameter, in case an
			// update was attempted for those fields as well

			coupon.setTitle(currentCoupon.getTitle());
			coupon.setStartDate(currentCoupon.getStartDate());
			coupon.setAmount(currentCoupon.getAmount());
			coupon.setType(currentCoupon.getType());
			coupon.setMessage(currentCoupon.getMessage());
			coupon.setImage(currentCoupon.getImage());

		} catch (DAOException e) {
			throw new FacadeException("A problem arose in the attempt to update the company coupon.", e);
		}
		return true; // No Exceptions thrown

	}

	/**
	 * public boolean deleteCoupon(Coupon coupon) deletes the coupon from all
	 * tables in which it appears: Coupon, Company_Coupon and Customer_Coupon
	 * 
	 * @param Coupon
	 * @throws FacadeException
	 * @return boolean, relating to the success of the entire deleting process
	 */

	public boolean deleteCoupon(Coupon coupon) throws FacadeException {

		try {

			// Firstly, we need to make sure that the coupon belongs to the
			// company (else an Exception will be thrown)

			companyDAO.readCoupon(coupon.getId(), this.companyId);

			// Secondly, we need to delete the coupon from tables
			// Coupon, Company_Coupon and Customer_Coupon

			couponDAO.deleteCoupon(coupon);
			compCoupDAO.deleteCoupon(coupon.getId());
			custCoupDAO.deleteCoupon(coupon.getId());

		} catch (DAOException e) {
			throw new FacadeException("A problem arose in the attempt to delete the company coupon.", e);
		}
		return true; // No Exceptions thrown

	}

}