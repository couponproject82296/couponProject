package b.DBDAOs;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.LinkedList;

import b.ConnectionPool.ConPoolException;
import b.ConnectionPool.ConnectionPool;
import b.DAOs.CompCoupDAO;
import b.DAOs.DAOException;

/**
 * This class implements the methods defined by the matching DAO interface,
 * which involve the database Company_Coupon table
 */

public class CompCoupDBDAO implements CompCoupDAO {

	// Attribute (the instance itself will be summoned via the DBDAO CTOR)

	private ConnectionPool pool;

	// CTOR (forms a CompCoupDBDAO object; summons the connection pool instance)

	public CompCoupDBDAO() throws DAOException {
		try {
			pool = ConnectionPool.getInstance();
		} catch (ConPoolException e) {
			throw new DAOException("Problem in creating the connection pool.", e);
		}
	}

	// CRUD methods applied regarding the Company_Coupon table

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

	@Override
	public boolean createCoupon(long compId, long coupId) throws DAOException {

		final String CREATE = "INSERT INTO Company_Coupon(COMP_ID, COUPON_ID) VALUES(?, ?)";

		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {

			connection = pool.getConnection();
			pstmt = connection.prepareStatement(CREATE);

			pstmt.setLong(1, compId);
			pstmt.setLong(2, coupId);
			int recordsCreated = pstmt.executeUpdate();

			// The method executeUpdate() will return 0 no record was created
			if (recordsCreated == 0) {
				throw new DAOException(
						"Problem in adding the coupon to the company. Please make sure the data you insert is valid.");
			}

		} catch (ConPoolException e) {
			throw new DAOException(
					"The addition of the coupon to the company failed due to a problem arising from the connection pool.",
					e);
		} catch (SQLException e) {
			throw new DAOException(
					"The addition of the coupon to the company failed. Please make sure the data you insert is valid.",
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
						"Problem while closing resources related to the addition of the coupon to the company.", e);
			}
		}
		return true; // No Exceptions thrown

	}

	/**
	 * public Collection<Long> readAllCoupons(long compId)
	 * 
	 * @param long
	 *            represents the company id
	 * @throws DAOException
	 * @return Collection<Long>, all the coupon id numbers related to the
	 *         specified company
	 */

	@Override
	public Collection<Long> readAllCoupons(long compId) throws DAOException {

		final String COUPONS = "SELECT * FROM Company_Coupon WHERE COMP_ID=?";

		Collection<Long> couponIds = new LinkedList<>();
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			connection = pool.getConnection();
			pstmt = connection.prepareStatement(COUPONS);
			pstmt.setLong(1, compId);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				couponIds.add(rs.getLong(2));
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
		return couponIds;

	}

	/**
	 * public long readCompany(long coupId)
	 * 
	 * @param long
	 *            represents the coupon id
	 * @throws DAOException
	 * @return long, represents the id of the company who issued the coupon
	 */

	@Override
	public long readCompany(long coupId) throws DAOException {

		final String READ = "SELECT * FROM Company_Coupon WHERE COUPON_ID=?";

		long compId = 0;
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			connection = pool.getConnection();
			pstmt = connection.prepareStatement(READ);
			pstmt.setLong(1, coupId);
			rs = pstmt.executeQuery();

			if (rs.next()) {
				compId = rs.getLong(1);
			} else {
				throw new DAOException("Coupon number " + coupId + " does not exist.");
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
		return compId;

	}

	/**
	 * public boolean deleteCoupon(long coupId)
	 * 
	 * @param long
	 *            represents the coupon id
	 * @throws DAOException
	 * @return boolean, relating to the success of the deleting process
	 */

	@Override
	public boolean deleteCoupon(long coupId) throws DAOException {

		final String DELETE = "DELETE FROM Company_Coupon WHERE COUPON_ID=?";

		Connection connection = null;
		PreparedStatement pstmt = null;

		try {

			connection = pool.getConnection();
			pstmt = connection.prepareStatement(DELETE);
			pstmt.setLong(1, coupId);
			int recordsDeleted = pstmt.executeUpdate();

			// The method executeUpdate() will return 0 if no record was deleted
			if (recordsDeleted == 0) {
				throw new DAOException(
						"Problem in deleting the coupon from the company. Please make sure the data you insert is valid.");
			}

		} catch (ConPoolException e) {
			throw new DAOException(
					"The deletion of the company coupon failed due to a problem arising from the connection pool.", e);
		} catch (SQLException e) {
			throw new DAOException(
					"The deletion of the company coupon failed. Please make sure the data you insert is valid.", e);
		} finally {
			pool.returnConnection(connection);
			try {
				if (pstmt != null)
					pstmt.close();
			} catch (SQLException e) {
				throw new DAOException("Problem while closing resources related to the deletion of the company coupon.",
						e);
			}
		}
		return true; // No Exceptions thrown

	}

	/**
	 * public int deleteCompany(long compId) deletes all the company records,
	 * i.e. all the coupons belonging to the specified company
	 * 
	 * @param long
	 *            represents the company id
	 * @throws DAOException
	 * @return int, the number of records deleted from the table
	 */

	@Override
	public int deleteCompany(long compId) throws DAOException {

		final String DELETE = "DELETE FROM Company_Coupon WHERE COMP_ID=?";

		int recordsDeleted = 0;
		Connection connection = null;
		PreparedStatement pstmt = null;

		try {

			connection = pool.getConnection();
			pstmt = connection.prepareStatement(DELETE);
			pstmt.setLong(1, compId);
			recordsDeleted = pstmt.executeUpdate();
			// The method executeUpdate() will return 0 if no record was deleted

		} catch (ConPoolException e) {
			throw new DAOException(
					"The deletion of all the company coupons failed due to a problem arising from the connection pool.",
					e);
		} catch (SQLException e) {
			throw new DAOException(
					"The deletion of all the company coupons failed. Please make sure the data you insert is valid.",
					e);
		} finally {
			pool.returnConnection(connection);
			try {
				if (pstmt != null)
					pstmt.close();
			} catch (SQLException e) {
				throw new DAOException(
						"Problem while closing resources related to the deletion of all the company coupons.", e);
			}
		}
		return recordsDeleted;

	}

}