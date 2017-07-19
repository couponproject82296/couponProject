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
import b.DAOs.CouponDAO;
import b.DAOs.DAOException;
import b.JavaBeans.Coupon;
import b.JavaBeans.CouponType;

/**
 * This class implements the methods defined by the matching DAO interface,
 * which involve the database Coupon table
 */

public class CouponDBDAO implements CouponDAO {

	// Attribute (the instance itself will be summoned via the DBDAO CTOR)

	private ConnectionPool pool;

	// CTOR (forms a CouponDBDAO object; summons the connection pool instance)

	public CouponDBDAO() throws DAOException {
		try {
			pool = ConnectionPool.getInstance();
		} catch (ConPoolException e) {
			throw new DAOException("Problem in creating the connection pool.", e);
		}
	}

	// CRUD methods applied regarding the coupons in the database

	/**
	 * public long createCoupon(Coupon coupon)
	 * 
	 * @param Coupon
	 * @throws DAOException
	 * @return long, the new id number given for the coupon by the system
	 */

	@Override
	public long createCoupon(Coupon coupon) throws DAOException {

		final String CREATE = "INSERT INTO Coupon(TITLE, START_DATE, END_DATE, AMOUNT, TYPE, MESSAGE, PRICE, IMAGE) VALUES(?, ?, ?, ?, ?, ?, ?, ?)";

		long id = 0;
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			connection = pool.getConnection();
			pstmt = connection.prepareStatement(CREATE, PreparedStatement.RETURN_GENERATED_KEYS);

			pstmt.setString(1, coupon.getTitle());
			java.util.Date startDate = coupon.getStartDate();
			java.sql.Date sDate = new java.sql.Date(startDate.getTime());
			pstmt.setDate(2, sDate);
			java.util.Date endDate = coupon.getEndDate();
			java.sql.Date eDate = new java.sql.Date(endDate.getTime());
			pstmt.setDate(3, eDate);
			pstmt.setInt(4, coupon.getAmount());
			pstmt.setString(5, coupon.getType().toString());
			pstmt.setString(6, coupon.getMessage());
			pstmt.setDouble(7, coupon.getPrice());
			pstmt.setString(8, coupon.getImage());
			pstmt.executeUpdate();

			rs = pstmt.getGeneratedKeys();
			if (rs.next()) {
				id = rs.getLong(1);
				coupon.setId(id); // Setting the new id in the coupon object
			} else {
				throw new DAOException(
						"Problem in creating the coupon. Please make sure the data you insert is valid.");
			}

		} catch (ConPoolException e) {
			throw new DAOException("Coupon creation failed due to a problem arising from the connection pool.", e);
		} catch (SQLException e) {
			throw new DAOException("Coupon creation failed. Please make sure the data you insert is valid.", e);
		} finally {
			pool.returnConnection(connection);
			try {
				if (pstmt != null)
					pstmt.close();
				if (rs != null)
					rs.close();
			} catch (SQLException e) {
				throw new DAOException("Problem while closing resources related to the coupon creation.", e);
			}
		}
		return id;

	}

	/**
	 * public Coupon readCoupon(long coupId)
	 * 
	 * @param long
	 *            represents the coupon id
	 * @throws DAOException
	 * @return Coupon, the coupon whose id was given as a parameter
	 */

	@Override
	public Coupon readCoupon(long coupId) throws DAOException {

		final String READ = "SELECT * FROM Coupon WHERE ID=?";

		Connection connection = null;
		Coupon coupon = new Coupon();
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			connection = pool.getConnection();
			pstmt = connection.prepareStatement(READ);
			pstmt.setLong(1, coupId);
			rs = pstmt.executeQuery();

			if (rs.next()) {
				coupon.setId(coupId);
				coupon.setTitle(rs.getString(2));
				coupon.setStartDate(rs.getDate(3));
				coupon.setEndDate(rs.getDate(4));
				coupon.setAmount(rs.getInt(5));
				coupon.setType(CouponType.valueOf(rs.getString(6)));
				coupon.setMessage(rs.getString(7));
				coupon.setPrice(rs.getDouble(8));
				coupon.setImage(rs.getString(9));
			} else {
				throw new DAOException("Coupon number " + coupId + " does not exist.");
			}
		} catch (ConPoolException e) {
			throw new DAOException(
					"The reading of the coupon failed due to a problem arising from the connection pool.", e);
		} catch (SQLException e) {
			throw new DAOException("The reading of the coupon failed. Please make sure the data you insert is valid.",
					e);
		} finally {
			pool.returnConnection(connection);
			try {
				if (pstmt != null)
					pstmt.close();
				if (rs != null)
					rs.close();
			} catch (SQLException e) {
				throw new DAOException("Problem while closing resources related to the reading of the coupon.", e);
			}
		}
		return coupon;

	}

	/**
	 * public Collection<Coupon> readAllCoupons()
	 * 
	 * @throws DAOException
	 * @return Collection<Coupon>, all the coupons in the database
	 */

	@Override
	public Collection<Coupon> readAllCoupons() throws DAOException {

		Collection<Coupon> coupons = new LinkedList<>();
		final String COUPONS = "SELECT * FROM Coupon ORDER BY id";

		Connection connection = null;
		Statement stmt = null;
		ResultSet rs = null;

		try {
			connection = pool.getConnection();
			stmt = connection.createStatement();
			rs = stmt.executeQuery(COUPONS);

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
					"The reading of the coupons failed due to a problem arising from the connection pool.", e);
		} catch (SQLException e) {
			throw new DAOException("The reading of the coupons failed. Please make sure the data you insert is valid.",
					e);
		} finally {
			pool.returnConnection(connection);
			try {
				if (stmt != null)
					stmt.close();
				if (rs != null)
					rs.close();
			} catch (SQLException e) {
				throw new DAOException("Problem while closing resources related to the reading of the coupons.", e);
			}
		}
		return coupons;

	}

	/**
	 * public Collection<Coupon> readCouponByType(CouponType type)
	 * 
	 * @throws DAOException
	 * @return Collection<Coupon>, all the coupons in the database of the
	 *         specified type
	 */

	@Override
	public Collection<Coupon> readCouponByType(CouponType type) throws DAOException {

		final String COUPONS_BY_TYPE = "SELECT * FROM Coupon WHERE TYPE=?";

		Collection<Coupon> coupons = new LinkedList<>();
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			connection = pool.getConnection();
			pstmt = connection.prepareStatement(COUPONS_BY_TYPE);
			pstmt.setString(1, type.toString());
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
			throw new DAOException("The reading of the coupons of type " + type
					+ " failed due to a problem arising from the connection pool.", e);
		} catch (SQLException e) {
			throw new DAOException("The reading of the coupons of type " + type
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
						"Problem while closing resources related to the reading of the coupons of type " + type + ".",
						e);
			}
		}
		return coupons;

	}

	/**
	 * public Collection<Coupon> readCouponByPrice(double price)
	 * 
	 * @param double
	 *            represents the price up to which coupon results should be read
	 * @throws DAOException
	 * @return Collection<Coupon>, all the coupons in the database whose price
	 *         is lower than/equal to the specified price
	 */

	@Override
	public Collection<Coupon> readCouponByPrice(double price) throws DAOException {

		final String COUPONS_BY_PRICE = "SELECT * FROM Coupon WHERE PRICE BETWEEN 0 AND ?";

		Collection<Coupon> coupons = new LinkedList<>();
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			connection = pool.getConnection();
			pstmt = connection.prepareStatement(COUPONS_BY_PRICE);
			pstmt.setDouble(1, price);
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
			throw new DAOException("The reading of the coupons, costing up to " + price
					+ ", failed due to a problem arising from the connection pool.", e);
		} catch (SQLException e) {
			throw new DAOException("The reading of the coupons, costing up to " + price
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
						"Problem while closing resources related to the reading of the coupons, costing up to " + price
								+ ".",
						e);
			}
		}
		return coupons;

	}

	/**
	 * public Collection<Coupon> readCouponByDate(Date endDate)
	 * 
	 * @param Date
	 *            represents the end date up to which coupons should be read
	 * @throws DAOException
	 * @return Collection<Coupon>, all the coupons in the database whose end
	 *         date is lower than/equal to the specified end date
	 */

	@Override
	public Collection<Coupon> readCouponByDate(Date endDate) throws DAOException {

		Calendar calendar = Calendar.getInstance();
		calendar.set(2017, Calendar.MARCH, 21, 0, 0, 0);
		final Date date = calendar.getTime();

		java.sql.Date date1 = new java.sql.Date(date.getTime());
		java.sql.Date date2 = new java.sql.Date(endDate.getTime());

		final String COUPONS_BY_DATE = "SELECT * FROM Coupon WHERE END_DATE BETWEEN ? AND ?";

		Collection<Coupon> coupons = new LinkedList<>();
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			connection = pool.getConnection();
			pstmt = connection.prepareStatement(COUPONS_BY_DATE);
			pstmt.setDate(1, date1);
			pstmt.setDate(2, date2);
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
			throw new DAOException("The reading of the coupons, ending till " + date2
					+ ", failed due to a problem arising from the connection pool.", e);
		} catch (SQLException e) {
			throw new DAOException("The reading of the coupons, ending till " + date2
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
						"Problem while closing resources related to the reading of the coupons, ending till " + date2
								+ ".",
						e);
			}
		}
		return coupons;

	}

	/**
	 * public boolean updateCoupon(Coupon coupon)
	 * 
	 * @param Coupon
	 * @throws DAOException
	 * @return boolean, relating to the success of the updating process
	 */

	@Override
	public boolean updateCoupon(Coupon coupon) throws DAOException {

		final String UPDATE = "UPDATE Coupon SET TITLE=?, START_DATE=?, END_DATE=?, AMOUNT=?, TYPE=?, MESSAGE=?, PRICE=?, IMAGE=? WHERE ID=?";

		Connection connection = null;
		PreparedStatement pstmt = null;

		try {

			connection = pool.getConnection();
			pstmt = connection.prepareStatement(UPDATE);

			pstmt.setString(1, coupon.getTitle());
			java.util.Date startDate = coupon.getStartDate();
			java.sql.Date sDate = new java.sql.Date(startDate.getTime());
			pstmt.setDate(2, sDate);
			java.util.Date endDate = coupon.getEndDate();
			java.sql.Date eDate = new java.sql.Date(endDate.getTime());
			pstmt.setDate(3, eDate);
			pstmt.setInt(4, coupon.getAmount());
			pstmt.setString(5, coupon.getType().toString());
			pstmt.setString(6, coupon.getMessage());
			pstmt.setDouble(7, coupon.getPrice());
			pstmt.setString(8, coupon.getImage());
			pstmt.setLong(9, coupon.getId());
			int recordsUpdated = pstmt.executeUpdate();

			// The method executeUpdate() will return 0 if no record was updated
			if (recordsUpdated == 0) {
				throw new DAOException(
						"Problem in updating the coupon. Please make sure the data you insert is valid.");
			}

		} catch (ConPoolException e) {
			throw new DAOException("Coupon update failed due to a problem arising from the connection pool.", e);
		} catch (SQLException e) {
			throw new DAOException("Coupon update failed. Please make sure the data you insert is valid.", e);
		} finally {
			pool.returnConnection(connection);
			try {
				if (pstmt != null)
					pstmt.close();
			} catch (SQLException e) {
				throw new DAOException("Problem while closing resources related to the coupon update.", e);
			}
		}
		return true; // No Exceptions thrown

	}

	/**
	 * public boolean deleteCoupon(Coupon coupon)
	 * 
	 * @param Coupon
	 * @throws DAOException
	 * @return boolean, relating to the success of the deleting process
	 */

	@Override
	public boolean deleteCoupon(Coupon coupon) throws DAOException {

		final String DELETE = "DELETE FROM Coupon WHERE ID=?";

		Connection connection = null;
		PreparedStatement pstmt = null;

		try {
			connection = pool.getConnection();
			pstmt = connection.prepareStatement(DELETE);
			pstmt.setLong(1, coupon.getId());
			int recordsDeleted = pstmt.executeUpdate();

			// The method executeUpdate() will return 0 if no record was deleted
			if (recordsDeleted == 0) {
				throw new DAOException(
						"Problem in deleting the coupon. Please make sure the data you insert is valid.");
			}

		} catch (ConPoolException e) {
			throw new DAOException("Coupon deletion failed due to a problem arising from the connection pool.", e);
		} catch (SQLException e) {
			throw new DAOException("Coupon deletion failed. Please make sure the data you insert is valid.", e);
		} finally {
			pool.returnConnection(connection);
			try {
				if (pstmt != null)
					pstmt.close();
			} catch (SQLException e) {
				throw new DAOException("Problem while closing resources related to the coupon deletion.", e);
			}
		}
		return true; // No Exceptions thrown

	}

	/**
	 * public void deleteExpiredCoupons(Date endDate) deletes all expired
	 * coupons from all the database tables: Coupon, Company_Coupon and
	 * Customer_coupon
	 * 
	 * @param Date
	 *            represents the end date up to which coupons should be deleted
	 * @throws DAOException
	 */

	@Override
	public void deleteExpiredCoupons(Date endDate) throws DAOException {

		Calendar calendar = Calendar.getInstance();
		calendar.set(2017, Calendar.MARCH, 21, 0, 0, 0);
		final Date date = calendar.getTime();

		java.sql.Date date1 = new java.sql.Date(date.getTime());
		java.sql.Date date2 = new java.sql.Date(endDate.getTime());

		final String COUPONS = "SELECT * FROM Coupon WHERE END_DATE BETWEEN ? AND ?";
		final String DEL_COUP = "DELETE FROM Coupon WHERE ID=?";
		final String DEL_COMP_COUP = "DELETE FROM Company_Coupon WHERE COUPON_ID=?";
		final String DEL_CUST_COUP = "DELETE FROM Customer_Coupon WHERE COUPON_ID=?";

		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			connection = pool.getConnection();
			pstmt = connection.prepareStatement(COUPONS);
			pstmt.setDate(1, date1);
			pstmt.setDate(2, date2);
			rs = pstmt.executeQuery();

			while (rs.next()) {

				long coupId = rs.getLong(1);

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
					"The deletion of the expired coupons failed due to a problem arising from the connection pool.", e);
		} catch (SQLException e) {
			throw new DAOException(
					"The deletion of the expired coupons failed. Please make sure the data you insert is valid.", e);
		} finally {
			pool.returnConnection(connection);
			try {
				if (pstmt != null)
					pstmt.close();
				if (rs != null)
					rs.close();
			} catch (SQLException e) {
				throw new DAOException(
						"Problem while closing resources related to the deletion of the expired coupons.", e);
			}
		}

	}

	/**
	 * public boolean checkCouponTitle(String title)
	 * 
	 * @param String
	 *            checked to make sure that this coupon title doesn't already
	 *            exist in the database
	 * @throws DAOException
	 * @return boolean, relating to the existence of the title in the database
	 */

	@Override
	public boolean checkCouponTitle(String title) throws DAOException {

		final String PRECHECK = "SELECT * FROM Coupon WHERE TITLE=?";

		boolean doesTitleExist = false;
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {

			connection = pool.getConnection();
			pstmt = connection.prepareStatement(PRECHECK);
			pstmt.setString(1, title);
			rs = pstmt.executeQuery();

			if (rs.next())
				doesTitleExist = true;

		} catch (ConPoolException e) {
			throw new DAOException("Coupon title-check failed due to a problem arising from the connection pool.", e);
		} catch (SQLException e) {
			throw new DAOException("Coupon title-check failed. Please make sure the data you insert is valid.", e);
		} finally {
			pool.returnConnection(connection);
			try {
				if (pstmt != null)
					pstmt.close();
				if (rs != null)
					rs.close();
			} catch (SQLException e) {
				throw new DAOException("Problem while closing resources related to the coupon title-check.", e);
			}
		}
		return doesTitleExist;

	}

}