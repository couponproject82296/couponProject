package b.DBDAOs;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;

import b.ConnectionPool.ConPoolException;
import b.ConnectionPool.ConnectionPool;
import b.DAOs.CompanyDAO;
import b.DAOs.DAOException;
import b.JavaBeans.Company;
import b.JavaBeans.Coupon;
import b.JavaBeans.CouponType;

/**
 * This class implements the methods defined by the matching DAO interface,
 * which involve the database Company table
 */

public class CompanyDBDAO implements CompanyDAO {

	// Attribute (the instance itself will be summoned via the DBDAO CTOR)

	private ConnectionPool pool;

	// CTOR (forms a CompanyDBDAO object; summons the connection pool instance)

	public CompanyDBDAO() throws DAOException {
		try {
			pool = ConnectionPool.getInstance();
		} catch (ConPoolException e) {
			throw new DAOException("Problem in creating the connection pool.", e);
		}
	}

	// CRUD methods applied regarding the companies in the database

	/**
	 * public long createCompany(Company company)
	 * 
	 * @param Company
	 * @throws DAOException
	 * @return long, the new id number given for the company by the system
	 */

	@Override
	public long createCompany(Company company) throws DAOException {

		final String CREATE = "INSERT INTO Company(COMP_NAME, PASSWORD, EMAIL) VALUES(?, ?, ?)";

		long id = 0;
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			connection = pool.getConnection();
			pstmt = connection.prepareStatement(CREATE, PreparedStatement.RETURN_GENERATED_KEYS);

			pstmt.setString(1, company.getCompName());
			pstmt.setString(2, company.getPassword());
			pstmt.setString(3, company.getEmail());
			pstmt.executeUpdate();

			rs = pstmt.getGeneratedKeys();
			if (rs.next()) {
				id = rs.getLong(1);
				company.setId(id); // Setting the new id in the company object
			} else {
				throw new DAOException(
						"Problem in creating the company. Please make sure the data you insert is valid.");
			}

		} catch (ConPoolException e) {
			throw new DAOException("Company creation failed due to a problem arising from the connection pool.", e);
		} catch (SQLException e) {
			throw new DAOException("Company creation failed. Please make sure the data you insert is valid.", e);
		} finally {
			pool.returnConnection(connection);
			try {
				if (pstmt != null)
					pstmt.close();
				if (rs != null)
					rs.close();
			} catch (SQLException e) {
				throw new DAOException("Problem while closing resources related to the company creation.", e);
			}
		}
		return id;

	}

	/**
	 * public Company readCompany(long compId)
	 * 
	 * @param long
	 *            represents the company id
	 * @throws DAOException
	 * @return Company, the company whose id was given as a parameter
	 */

	@Override
	public Company readCompany(long compId) throws DAOException {

		final String READ = "SELECT * FROM Company WHERE ID=?";

		Connection connection = null;
		Company company = new Company();
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			connection = pool.getConnection();
			pstmt = connection.prepareStatement(READ);
			pstmt.setLong(1, compId);
			rs = pstmt.executeQuery();

			if (rs.next()) {
				company.setId(compId);
				company.setCompName(rs.getString(2));
				company.setPassword(rs.getString(3));
				company.setEmail(rs.getString(4));
			} else {
				throw new DAOException("Company number " + compId + " does not exist.");
			}
		} catch (ConPoolException e) {
			throw new DAOException(
					"The reading of the company failed due to a problem arising from the connection pool.", e);
		} catch (SQLException e) {
			throw new DAOException("The reading of the company failed. Please make sure the data you insert is valid.",
					e);
		} finally {
			pool.returnConnection(connection);
			try {
				if (pstmt != null)
					pstmt.close();
				if (rs != null)
					rs.close();
			} catch (SQLException e) {
				throw new DAOException("Problem while closing resources related to the reading of the company.", e);
			}
		}
		return company;

	}

	/**
	 * public Collection<Company> readAllCompanies()
	 * 
	 * @throws DAOException
	 * @return Collection<Company>, all the companies existing in the database
	 */

	@Override
	public Collection<Company> readAllCompanies() throws DAOException {

		Collection<Company> companies = new LinkedList<>();
		final String COMPANIES = "SELECT * FROM Company ORDER BY id";

		Connection connection = null;
		Statement stmt = null;
		ResultSet rs = null;

		try {

			connection = pool.getConnection();
			stmt = connection.createStatement();
			rs = stmt.executeQuery(COMPANIES);

			while (rs.next()) {
				Company company = new Company();
				company.setId(rs.getLong(1));
				company.setCompName(rs.getString(2));
				company.setPassword(rs.getString(3));
				company.setEmail(rs.getString(4));
				companies.add(company);
			}

		} catch (ConPoolException e) {
			throw new DAOException(
					"The reading of the companies failed due to a problem arising from the connection pool.", e);
		} catch (SQLException e) {
			throw new DAOException(
					"The reading of the companies failed. Please make sure the data you insert is valid.", e);
		} finally {
			pool.returnConnection(connection);
			try {
				if (stmt != null)
					stmt.close();
				if (rs != null)
					rs.close();
			} catch (SQLException e) {
				throw new DAOException("Problem while closing resources related to the reading of the companies.", e);
			}
		}
		return companies;

	}

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

	@Override
	public Coupon readCoupon(long coupId, long compId) throws DAOException {

		final String COUPON = "SELECT Coupon.* FROM Coupon INNER JOIN Company_Coupon ON COUPON.ID=Company_Coupon.COUPON_ID WHERE Company_Coupon.COMP_ID=? AND Coupon.ID=?";

		Connection connection = null;
		Coupon coupon = new Coupon();
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			connection = pool.getConnection();
			pstmt = connection.prepareStatement(COUPON);
			pstmt.setLong(1, compId);
			pstmt.setLong(2, coupId);
			rs = pstmt.executeQuery();

			if (rs.next()) {
				coupon.setId(rs.getLong(1));
				coupon.setTitle(rs.getString(2));
				coupon.setStartDate(rs.getDate(3));
				coupon.setEndDate(rs.getDate(4));
				coupon.setAmount(rs.getInt(5));
				coupon.setType(CouponType.valueOf(rs.getString(6)));
				coupon.setMessage(rs.getString(7));
				coupon.setPrice(rs.getDouble(8));
				coupon.setImage(rs.getString(9));
			} else {
				throw new DAOException(
						"No suitable coupon was found. Please make sure the coupon id is correct and belongs to the company.");
			}
		} catch (ConPoolException e) {
			throw new DAOException(
					"The reading of the company coupon failed due to a problem arising from the connection pool.", e);
		} catch (SQLException e) {
			throw new DAOException(
					"The reading of the company coupon failed. Please make sure the data you insert is valid.", e);
		} finally {
			pool.returnConnection(connection);
			try {
				if (pstmt != null)
					pstmt.close();
				if (rs != null)
					rs.close();
			} catch (SQLException e) {
				throw new DAOException("Problem while closing resources related to the reading of the company coupon.",
						e);
			}
		}
		return coupon;

	}

	/**
	 * public Collection<Coupon> readCoupons(long compId) reads coupons from
	 * table Coupon based on the COMP_ID depicted in table Company_Coupon
	 * 
	 * @param long
	 *            representing a certain company id
	 * @throws DAOException
	 * @return Collection<Coupon>, all the coupons related to the specified
	 *         company
	 */

	@Override
	public Collection<Coupon> readCoupons(long compId) throws DAOException {

		Collection<Coupon> coupons = new LinkedList<>();
		final String COUPON = "SELECT Coupon.* FROM Coupon INNER JOIN Company_Coupon ON COUPON.ID=Company_Coupon.COUPON_ID WHERE Company_Coupon.COMP_ID=?";

		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {

			connection = pool.getConnection();
			pstmt = connection.prepareStatement(COUPON);
			pstmt.setLong(1, compId);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				Coupon coupon = new Coupon();
				coupon.setId(rs.getLong(1));
				coupon.setTitle(rs.getString(2));
				coupon.setStartDate(rs.getDate(3));
				coupon.setEndDate(rs.getDate(4));
				coupon.setAmount(rs.getInt(5));
				coupon.setType(CouponType.valueOf(rs.getString(6)));
				coupon.setMessage(rs.getString(7));
				coupon.setPrice(rs.getDouble(8));
				coupon.setImage(rs.getString(9));
				coupons.add(coupon);
			}

		} catch (ConPoolException e) {
			throw new DAOException(
					"The reading of the company coupons failed due to a problem arising from the connection pool.", e);
		} catch (SQLException e) {
			throw new DAOException(
					"The reading of the company coupons failed. Please make sure the data you insert is valid.", e);
		} finally {
			pool.returnConnection(connection);
			try {
				if (pstmt != null)
					pstmt.close();
				if (rs != null)
					rs.close();
			} catch (SQLException e) {
				throw new DAOException("Problem while closing resources related to the reading of the company coupons.",
						e);
			}
		}
		return coupons;

	}

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

	@Override
	public Collection<Coupon> readCouponsByType(CouponType type, long compId) throws DAOException {

		final String COUPONS_BY_TYPE = "SELECT Coupon.* FROM Coupon JOIN Company_Coupon ON COUPON.ID=Company_Coupon.COUPON_ID WHERE Company_Coupon.COMP_ID=? AND Coupon.TYPE=?";

		Collection<Coupon> coupons = new LinkedList<>();
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			connection = pool.getConnection();
			pstmt = connection.prepareStatement(COUPONS_BY_TYPE);
			pstmt.setLong(1, compId);
			pstmt.setString(2, type.toString());
			rs = pstmt.executeQuery();

			while (rs.next()) {
				Coupon coupon = new Coupon();
				coupon.setId(rs.getLong(1));
				coupon.setTitle(rs.getString(2));
				coupon.setStartDate(rs.getDate(3));
				coupon.setEndDate(rs.getDate(4));
				coupon.setAmount(rs.getInt(5));
				coupon.setType(type);
				coupon.setMessage(rs.getString(7));
				coupon.setPrice(rs.getDouble(8));
				coupon.setImage(rs.getString(9));
				coupons.add(coupon);
			}
		} catch (ConPoolException e) {
			throw new DAOException("The reading of the company coupons of type " + type
					+ " failed due to a problem arising from the connection pool.", e);
		} catch (SQLException e) {
			throw new DAOException("The reading of the company coupons of type " + type
					+ " failed. Please make sure the data you insert is valid.", e);
		} finally {
			pool.returnConnection(connection);
			try {
				if (pstmt != null)
					pstmt.close();
				if (rs != null)
					rs.close();
			} catch (SQLException e) {
				throw new DAOException(
						"Problem while closing resources related to the reading of the company coupons of type " + type
								+ ".",
						e);
			}

		}
		return coupons;
	}

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

	@Override
	public Collection<Coupon> readCouponsByPrice(double price, long compId) throws DAOException {

		final String COUPONS_BY_PRICE = "SELECT Coupon.* FROM Coupon JOIN Company_Coupon ON COUPON.ID=Company_Coupon.COUPON_ID WHERE Company_Coupon.COMP_ID=? AND Coupon.PRICE BETWEEN 0 AND ?";

		Collection<Coupon> coupons = new LinkedList<>();
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			connection = pool.getConnection();
			pstmt = connection.prepareStatement(COUPONS_BY_PRICE);
			pstmt.setLong(1, compId);
			pstmt.setDouble(2, price);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				Coupon coupon = new Coupon();
				coupon.setId(rs.getLong(1));
				coupon.setTitle(rs.getString(2));
				coupon.setStartDate(rs.getDate(3));
				coupon.setEndDate(rs.getDate(4));
				coupon.setAmount(rs.getInt(5));
				coupon.setType(CouponType.valueOf(rs.getString(6)));
				coupon.setMessage(rs.getString(7));
				coupon.setPrice(rs.getDouble(8));
				coupon.setImage(rs.getString(9));
				coupons.add(coupon);
			}

		} catch (ConPoolException e) {
			throw new DAOException("The reading of the company coupons, costing up to " + price
					+ ", failed due to a problem arising from the connection pool.", e);
		} catch (SQLException e) {
			throw new DAOException("The reading of the company coupons, costing up to " + price
					+ ", failed. Please make sure the data you insert is valid.", e);
		} finally {
			pool.returnConnection(connection);
			try {
				if (pstmt != null)
					pstmt.close();
				if (rs != null)
					rs.close();
			} catch (SQLException e) {
				throw new DAOException(
						"Problem while closing resources related to the reading of the company coupons, costing up to "
								+ price + ".",
						e);
			}
		}
		return coupons;

	}

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

	@Override
	public Collection<Coupon> readCouponsByDate(Date endDate, long compId) throws DAOException {

		Calendar calendar = Calendar.getInstance();
		calendar.set(2017, Calendar.MARCH, 21, 0, 0, 0);
		final Date date = calendar.getTime();

		java.sql.Date date1 = new java.sql.Date(date.getTime());
		java.sql.Date date2 = new java.sql.Date(endDate.getTime());

		final String COUPONS_BY_DATE = "SELECT Coupon.* FROM Coupon JOIN Company_Coupon ON COUPON.ID=Company_Coupon.COUPON_ID WHERE Company_Coupon.COMP_ID=? AND (Coupon.END_DATE BETWEEN ? AND ?)";

		Collection<Coupon> coupons = new LinkedList<>();
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			connection = pool.getConnection();
			pstmt = connection.prepareStatement(COUPONS_BY_DATE);
			pstmt.setLong(1, compId);
			pstmt.setDate(2, date1);
			pstmt.setDate(3, date2);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				Coupon coupon = new Coupon();
				coupon.setId(rs.getLong(1));
				coupon.setTitle(rs.getString(2));
				coupon.setStartDate(rs.getDate(3));
				coupon.setEndDate(rs.getDate(4));
				coupon.setAmount(rs.getInt(5));
				coupon.setType(CouponType.valueOf(rs.getString(6)));
				coupon.setMessage(rs.getString(7));
				coupon.setPrice(rs.getDouble(8));
				coupon.setImage(rs.getString(9));
				coupons.add(coupon);
			}

		} catch (ConPoolException e) {
			throw new DAOException("The reading of the company coupons, ending till " + date2
					+ ", failed due to a problem arising from the connection pool.", e);
		} catch (SQLException e) {
			throw new DAOException("The reading of the company coupons, ending till " + date2
					+ ", failed. Please make sure the data you insert is valid.", e);
		} finally {
			pool.returnConnection(connection);
			try {
				if (pstmt != null)
					pstmt.close();
				if (rs != null)
					rs.close();
			} catch (SQLException e) {
				throw new DAOException(
						"Problem while closing resources related to the reading of the company coupons, ending till "
								+ date2 + ".",
						e);
			}
		}
		return coupons;

	}

	/**
	 * public boolean updateCompany(Company company)
	 * 
	 * @param Company
	 * @throws DAOException
	 * @return boolean, relating to the success of the updating process
	 */

	@Override
	public boolean updateCompany(Company company) throws DAOException {

		final String UPDATE = "UPDATE Company SET COMP_NAME=?, PASSWORD=?, EMAIL=? WHERE ID=?";

		Connection connection = null;
		PreparedStatement pstmt = null;

		try {

			connection = pool.getConnection();
			pstmt = connection.prepareStatement(UPDATE);

			pstmt.setString(1, company.getCompName());
			pstmt.setString(2, company.getPassword());
			pstmt.setString(3, company.getEmail());
			pstmt.setLong(4, company.getId());
			int recordsUpdated = pstmt.executeUpdate();

			// The method executeUpdate() will return 0 if no record was updated
			if (recordsUpdated == 0) {
				throw new DAOException(
						"Problem in updating the company. Please make sure the data you insert is valid.");
			}

		} catch (ConPoolException e) {
			throw new DAOException("Company update failed due to a problem arising from the connection pool.", e);
		} catch (SQLException e) {
			throw new DAOException("Company update failed. Please make sure the data you insert is valid.", e);
		} finally {
			pool.returnConnection(connection);
			try {
				if (pstmt != null)
					pstmt.close();
			} catch (SQLException e) {
				throw new DAOException("Problem while closing resources related to the company update.", e);
			}
		}
		return true; // No Exceptions thrown

	}

	/**
	 * public boolean deleteCompany(Company company)
	 * 
	 * @param Company
	 * @throws DAOException
	 * @return boolean, relating to the success of the deleting process
	 */

	@Override
	public boolean deleteCompany(Company company) throws DAOException {

		final String DELETE = "DELETE FROM Company WHERE ID=?";

		Connection connection = null;
		PreparedStatement pstmt = null;

		try {
			connection = pool.getConnection();
			pstmt = connection.prepareStatement(DELETE);
			pstmt.setLong(1, company.getId());
			int recordsDeleted = pstmt.executeUpdate();

			// The method executeUpdate() will return 0 if no record was deleted
			if (recordsDeleted == 0) {
				throw new DAOException(
						"Problem in deleting the company. Please make sure the data you insert is valid.");
			}

		} catch (ConPoolException e) {
			throw new DAOException("Company deletion failed due to a problem arising from the connection pool.", e);
		} catch (SQLException e) {
			throw new DAOException("Company deletion failed. Please make sure the data you insert is valid.", e);
		} finally {
			pool.returnConnection(connection);
			try {
				if (pstmt != null)
					pstmt.close();
			} catch (SQLException e) {
				throw new DAOException("Problem while closing resources related to the company deletion.", e);
			}
		}
		return true; // No Exceptions thrown

	}

	/**
	 * public void deleteCompanyCoupons(long compId) deletes all company coupons
	 * from all the database tables: Coupon, Company_Coupon and Customer_coupon
	 * 
	 * @param long
	 *            represents the company id
	 * @throws DAOException
	 */

	@Override
	public void deleteCompanyCoupons(long compId) throws DAOException {

		final String COUPONS = "SELECT * FROM Company_Coupon WHERE COMP_ID=?";
		final String DEL_COUP = "DELETE FROM Coupon WHERE ID=?";
		final String DEL_COMP_COUP = "DELETE FROM Company_Coupon WHERE COUPON_ID=?";
		final String DEL_CUST_COUP = "DELETE FROM Customer_Coupon WHERE COUPON_ID=?";

		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			connection = pool.getConnection();
			pstmt = connection.prepareStatement(COUPONS);
			pstmt.setLong(1, compId);
			rs = pstmt.executeQuery();

			while (rs.next()) {

				long coupId = rs.getLong(2);

				pstmt = connection.prepareStatement(DEL_COUP);
				pstmt.setLong(1, coupId);
				pstmt.executeUpdate();
				if (pstmt != null)
					pstmt.close();

				pstmt = connection.prepareStatement(DEL_COMP_COUP);
				pstmt.setLong(1, coupId);
				pstmt.executeUpdate();
				if (pstmt != null)
					pstmt.close();

				pstmt = connection.prepareStatement(DEL_CUST_COUP);
				pstmt.setLong(1, coupId);
				pstmt.executeUpdate();
				if (pstmt != null)
					pstmt.close();

			}

		} catch (ConPoolException e) {
			throw new DAOException(
					"The deletion of the company coupons failed due to a problem arising from the connection pool.", e);
		} catch (SQLException e) {
			throw new DAOException(
					"The deletion of the company coupons failed. Please make sure the data you insert is valid.", e);
		} finally {
			pool.returnConnection(connection);
			try {
				if (pstmt != null)
					pstmt.close();
				if (rs != null)
					rs.close();
			} catch (SQLException e) {
				throw new DAOException(
						"Problem while closing resources related to the deletion of the company coupons.", e);
			}
		}

	}

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

	@Override
	public boolean login(long id, String password) throws DAOException {

		final String LOGIN = "SELECT * FROM Company WHERE ID=? AND PASSWORD=?";

		boolean loginStatus = false;
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {

			connection = pool.getConnection();
			pstmt = connection.prepareStatement(LOGIN);
			pstmt.setLong(1, id);
			pstmt.setString(2, password);
			rs = pstmt.executeQuery();

			if (rs.next())
				loginStatus = true;

		} catch (ConPoolException e) {
			throw new DAOException("Company login failed due to a problem arising from the connection pool.", e);
		} catch (SQLException e) {
			throw new DAOException("Company login failed. Please make sure the data you insert is valid.", e);
		} finally {
			pool.returnConnection(connection);
			try {
				if (pstmt != null)
					pstmt.close();
				if (rs != null)
					rs.close();
			} catch (SQLException e) {
				throw new DAOException("Problem while closing resources related to the company login.", e);
			}
		}
		return loginStatus;

	}

	/**
	 * public boolean checkCompanyName(String name)
	 * 
	 * @param String
	 *            checked to make sure that this company name doesn't already
	 *            exist in the database
	 * @throws DAOException
	 * @return boolean, relating to the existence of the name in the database
	 */

	@Override
	public boolean checkCompanyName(String name) throws DAOException {

		final String PRECHECK = "SELECT * FROM Company WHERE COMP_NAME=?";

		boolean doesNameExist = false;
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {

			connection = pool.getConnection();
			pstmt = connection.prepareStatement(PRECHECK);
			pstmt.setString(1, name);
			rs = pstmt.executeQuery();

			if (rs.next())
				doesNameExist = true;

		} catch (ConPoolException e) {
			throw new DAOException("Company name-check failed due to a problem arising from the connection pool.", e);
		} catch (SQLException e) {
			throw new DAOException("Company name-check failed. Please make sure the data you insert is valid.", e);
		} finally {
			pool.returnConnection(connection);
			try {
				if (pstmt != null)
					pstmt.close();
				if (rs != null)
					rs.close();
			} catch (SQLException e) {
				throw new DAOException("Problem while closing resources related to the company name-check.", e);
			}
		}
		return doesNameExist;

	}

}