package b.DBDAOs;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.LinkedList;

import b.ConnectionPool.ConPoolException;
import b.ConnectionPool.ConnectionPool;
import b.DAOs.CustCoupDAO;
import b.DAOs.DAOException;

/**
 * This class implements the methods defined by the matching DAO interface,
 * which involve the database Customer_Coupon table
 */

public class CustCoupDBDAO implements CustCoupDAO {

	// Attribute (the instance itself will be summoned via the DBDAO CTOR)

	private ConnectionPool pool;

	// CTOR (forms a CustCoupDBDAO object; summons the connection pool instance)

	public CustCoupDBDAO() throws DAOException {
		try {
			pool = ConnectionPool.getInstance();
		} catch (ConPoolException e) {
			throw new DAOException("Problem in creating the connection pool.", e);
		}
	}

	// CRUD methods applied regarding the Customer_Coupon table

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

	@Override
	public boolean createCoupon(long custId, long coupId) throws DAOException {

		final String CREATE = "INSERT INTO Customer_Coupon(CUST_ID, COUPON_ID) VALUES(?, ?)";

		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			connection = pool.getConnection();
			pstmt = connection.prepareStatement(CREATE);

			pstmt.setLong(1, custId);
			pstmt.setLong(2, coupId);
			int recordsCreated = pstmt.executeUpdate();

			// The method executeUpdate() will return 0 if no record was created
			if (recordsCreated == 0) {
				throw new DAOException(
						"Problem in adding the coupon to the customer. Please make sure the data you insert is valid.");
			}

		} catch (ConPoolException e) {
			throw new DAOException(
					"The addition of the coupon to the customer failed due to a problem arising from the connection pool.",
					e);
		} catch (SQLException e) {
			throw new DAOException(
					"The addition of the coupon to the customer failed. Please make sure the data you insert is valid.",
					e);
		} finally {
			pool.returnConnection(connection);
			try {
				if (pstmt != null)
					pstmt.close();
				if (rs != null)
					rs.close();
			} catch (SQLException e) {
				throw new DAOException(
						"Problem while closing resources related to the addition of the coupon to the customer.", e);
			}
		}
		return true; // No Exceptions thrown

	}

	/**
	 * public Collection<Long> readAllCoupons(long custId)
	 * 
	 * @param long
	 *            represents the customer id
	 * @throws DAOException
	 * @return Collection<Long>, all the coupon id numbers related to the
	 *         specified customer
	 */

	@Override
	public Collection<Long> readAllCoupons(long custId) throws DAOException {

		final String COUPONS = "SELECT * FROM Customer_Coupon WHERE CUST_ID=?";

		Collection<Long> couponIds = new LinkedList<>();
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			connection = pool.getConnection();
			pstmt = connection.prepareStatement(COUPONS);
			pstmt.setLong(1, custId);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				couponIds.add(rs.getLong(2));
			}
		} catch (ConPoolException e) {
			throw new DAOException(
					"The reading of the customer coupons failed due to a problem arising from the connection pool.", e);
		} catch (SQLException e) {
			throw new DAOException(
					"The reading of the customer coupons failed. Please make sure the data you insert is valid.", e);
		} finally {
			pool.returnConnection(connection);
			try {
				if (pstmt != null)
					pstmt.close();
				if (rs != null)
					rs.close();
			} catch (SQLException e) {
				throw new DAOException(
						"Problem while closing resources related to the reading of the customer coupons.", e);
			}
		}
		return couponIds;

	}

	/**
	 * public Collection<Long> readAllCustomers(long coupId)
	 * 
	 * @param long
	 *            represents the coupon id
	 * @throws DAOException
	 * @return Collection<Long>, represents the id numbers of the customers who
	 *         purchased the coupon
	 */

	@Override
	public Collection<Long> readAllCustomers(long coupId) throws DAOException {

		final String READ = "SELECT * FROM Customer_Coupon WHERE COUPON_ID=?";

		Collection<Long> custIds = new LinkedList<>();
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			connection = pool.getConnection();
			pstmt = connection.prepareStatement(READ);
			pstmt.setLong(1, coupId);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				custIds.add(rs.getLong(1));
			}
		} catch (ConPoolException e) {
			throw new DAOException(
					"The reading of the customers failed due to a problem arising from the connection pool.", e);
		} catch (SQLException e) {
			throw new DAOException(
					"The reading of the customers failed. Please make sure the data you insert is valid.", e);
		} finally {
			pool.returnConnection(connection);
			try {
				if (pstmt != null)
					pstmt.close();
				if (rs != null)
					rs.close();
			} catch (SQLException e) {
				throw new DAOException("Problem while closing resources related to the reading of the customers.", e);
			}
		}
		return custIds;
	}

	/**
	 * public int deleteCoupon(long coupId) deletes all the coupon records, i.e.
	 * all the customers that have purchased the specified coupon
	 * 
	 * @param long
	 *            represents the coupon id
	 * @throws DAOException
	 * @return int, the number of records deleted from the table
	 */

	@Override
	public int deleteCoupon(long coupId) throws DAOException {

		final String DELETE = "DELETE FROM Customer_Coupon WHERE COUPON_ID=?";

		int recordsDeleted = 0;
		Connection connection = null;
		PreparedStatement pstmt = null;

		try {

			connection = pool.getConnection();
			pstmt = connection.prepareStatement(DELETE);
			pstmt.setLong(1, coupId);
			recordsDeleted = pstmt.executeUpdate();
			// The method executeUpdate() will return 0 if no record was deleted

		} catch (ConPoolException e) {
			throw new DAOException(
					"The deletion of the customer coupon failed due to a problem arising from the connection pool.", e);
		} catch (SQLException e) {
			throw new DAOException(
					"The deletion of the customer coupon failed. Please make sure the data you insert is valid.", e);
		} finally {
			pool.returnConnection(connection);
			try {
				if (pstmt != null)
					pstmt.close();
			} catch (SQLException e) {
				throw new DAOException(
						"Problem while closing resources related to the deletion of the customer coupon.", e);
			}
		}
		return recordsDeleted;

	}

	/**
	 * public int deleteCustomer(long custId) deletes all the customer records,
	 * i.e. all the coupons purchased by the specified customer
	 * 
	 * @param long
	 *            represents the customer id
	 * @throws DAOException
	 * @return int, the number of records deleted from the table
	 */

	@Override
	public int deleteCustomer(long custId) throws DAOException {

		final String DELETE = "DELETE FROM Customer_Coupon WHERE CUST_ID=?";

		int recordsDeleted = 0;
		Connection connection = null;
		PreparedStatement pstmt = null;

		try {

			connection = pool.getConnection();
			pstmt = connection.prepareStatement(DELETE);
			pstmt.setLong(1, custId);
			pstmt.executeUpdate();
			recordsDeleted = pstmt.executeUpdate();
			// The method executeUpdate() will return 0 if no record was deleted

		} catch (ConPoolException e) {
			throw new DAOException(
					"The deletion of all the customer coupons failed due to a problem arising from the connection pool.",
					e);
		} catch (SQLException e) {
			throw new DAOException(
					"The deletion of all the customer coupons failed. Please make sure the data you insert is valid.",
					e);
		} finally {
			pool.returnConnection(connection);
			try {
				if (pstmt != null)
					pstmt.close();
			} catch (SQLException e) {
				throw new DAOException(
						"Problem while closing resources related to the deletion of all the customer coupons.", e);
			}
		}
		return recordsDeleted;

	}

}