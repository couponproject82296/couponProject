package b.DBDAOs;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;

import b.ConnectionPool.ConPoolException;
import b.ConnectionPool.ConnectionPool;
import b.DAOs.CouponDAO;
import b.DAOs.CustomerDAO;
import b.DAOs.DAOException;
import b.JavaBeans.Coupon;
import b.JavaBeans.CouponType;
import b.JavaBeans.Customer;

/**
 * This class implements the methods defined by the matching DAO interface,
 * which involve the database Customer table
 */

public class CustomerDBDAO implements CustomerDAO {

	// Attribute (the instance itself will be summoned via the DBDAO CTOR)

	private ConnectionPool pool;

	// CTOR (forms a CustomerDBDAO object; summons the connection pool instance)

	public CustomerDBDAO() throws DAOException {
		try {
			pool = ConnectionPool.getInstance();
		} catch (ConPoolException e) {
			throw new DAOException("Problem in creating the connection pool.", e);
		}
	}

	// CRUD methods applied regarding the customers in the database

	/**
	 * public long createCustomer(Customer customer)
	 * 
	 * @param Customer
	 * @throws DAOException
	 * @return long, the new id number given for the customer by the system
	 */

	@Override
	public long createCustomer(Customer customer) throws DAOException {

		final String CREATE = "INSERT INTO Customer(CUST_NAME, PASSWORD) VALUES(?, ?)";

		long id = 0;
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			connection = pool.getConnection();
			pstmt = connection.prepareStatement(CREATE, PreparedStatement.RETURN_GENERATED_KEYS);

			pstmt.setString(1, customer.getCustName());
			pstmt.setString(2, customer.getPassword());
			pstmt.executeUpdate();

			rs = pstmt.getGeneratedKeys();
			if (rs.next()) {
				id = rs.getLong(1);
				customer.setId(id); // Setting the new id in the customer object
			} else {
				throw new DAOException(
						"Problem in creating the customer. Please make sure the data you insert is valid.");
			}

		} catch (ConPoolException e) {
			throw new DAOException("Customer creation failed due to a problem arising from the connection pool.", e);
		} catch (SQLException e) {
			throw new DAOException("Customer creation failed. Please make sure the data you insert is valid.", e);
		} finally {
			pool.returnConnection(connection);
			try {
				if (pstmt != null)
					pstmt.close();
				if (rs != null)
					rs.close();
			} catch (SQLException e) {
				throw new DAOException("Problem while closing resources related to the customer creation.", e);
			}
		}
		return id;

	}

	/**
	 * public Customer readCustomer(long custId)
	 * 
	 * @param long
	 *            represents the customer id
	 * @throws DAOException
	 * @return Customer, the customer whose id was given as a parameter
	 */

	@Override
	public Customer readCustomer(long custId) throws DAOException {

		final String READ = "SELECT * FROM Customer WHERE ID=?";

		Connection connection = null;
		Customer customer = new Customer();
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			connection = pool.getConnection();
			pstmt = connection.prepareStatement(READ);
			pstmt.setLong(1, custId);
			rs = pstmt.executeQuery();

			if (rs.next()) {
				customer.setId(custId);
				customer.setCustName(rs.getString(2));
				customer.setPassword(rs.getString(3));
			} else {
				throw new DAOException("Customer number " + custId + " does not exist.");
			}
		} catch (ConPoolException e) {
			throw new DAOException(
					"The reading of the customer failed due to a problem arising from the connection pool.", e);
		} catch (SQLException e) {
			throw new DAOException("The reading of the customer failed. Please make sure the data you insert is valid.",
					e);
		} finally {
			pool.returnConnection(connection);
			try {
				if (pstmt != null)
					pstmt.close();
				if (rs != null)
					rs.close();
			} catch (SQLException e) {
				throw new DAOException("Problem while closing resources related to the reading of the customer.", e);
			}
		}
		return customer;
	}

	/**
	 * public Collection<Customer> readAllCustomers()
	 * 
	 * @throws DAOException
	 * @return Collection<Customer>, all the customers in the database
	 */

	@Override
	public Collection<Customer> readAllCustomers() throws DAOException {

		Collection<Customer> customers = new LinkedList<>();
		final String CUSTOMERS = "SELECT * FROM Customer ORDER BY id";

		Connection connection = null;
		Statement stmt = null;
		ResultSet rs = null;

		try {

			connection = pool.getConnection();
			stmt = connection.createStatement();
			rs = stmt.executeQuery(CUSTOMERS);

			while (rs.next()) {
				Customer customer = new Customer();
				customer.setId(rs.getLong(1));
				customer.setCustName(rs.getString(2));
				customer.setPassword(rs.getString(3));
				customers.add(customer);
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
				if (stmt != null)
					stmt.close();
				if (rs != null)
					rs.close();
			} catch (SQLException e) {
				throw new DAOException("Problem while closing resources related to the reading of the customers.", e);
			}
		}
		return customers;

	}

	/**
	 * public Collection<Coupon> readCoupons(long custId) reads coupons from
	 * table Coupon based on the CUST_ID depicted in table Customer_Coupon
	 * 
	 * @param long
	 *            representing a certain customer id
	 * @throws DAOException
	 * @return Collection<Coupon>, all the coupons related to the specified
	 *         customer
	 */

	@Override
	public Collection<Coupon> readCoupons(long custId) throws DAOException {

		Collection<Coupon> coupons = new LinkedList<>();
		final String COUPON = "SELECT Coupon.* FROM Coupon INNER JOIN Customer_Coupon ON COUPON.ID=Customer_Coupon.COUPON_ID WHERE Customer_Coupon.CUST_ID=?";

		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {

			connection = pool.getConnection();
			pstmt = connection.prepareStatement(COUPON);
			pstmt.setLong(1, custId);
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
		return coupons;

	}

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

	@Override
	public Collection<Coupon> readCouponsByType(CouponType type, long custId) throws DAOException {

		final String COUPONS_BY_TYPE = "SELECT Coupon.* FROM Coupon JOIN Customer_Coupon ON COUPON.ID=Customer_Coupon.COUPON_ID WHERE Customer_Coupon.CUST_ID=? AND Coupon.TYPE=?";

		Collection<Coupon> coupons = new LinkedList<>();
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			connection = pool.getConnection();
			pstmt = connection.prepareStatement(COUPONS_BY_TYPE);
			pstmt.setLong(1, custId);
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
			throw new DAOException("The reading of the customer coupons of type " + type
					+ " failed due to a problem arising from the connection pool.", e);
		} catch (SQLException e) {
			throw new DAOException("The reading of the customer coupons of type " + type
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
						"Problem while closing resources related to the reading of the customer coupons of type " + type
								+ ".",
						e);
			}

		}
		return coupons;
	}

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

	@Override
	public Collection<Coupon> readCouponsByPrice(double price, long custId) throws DAOException {

		final String COUPONS_BY_PRICE = "SELECT Coupon.* FROM Coupon JOIN Customer_Coupon ON COUPON.ID=Customer_Coupon.COUPON_ID WHERE Customer_Coupon.CUST_ID=? AND (Coupon.PRICE BETWEEN 0 AND ?)";

		Collection<Coupon> coupons = new LinkedList<>();
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			connection = pool.getConnection();
			pstmt = connection.prepareStatement(COUPONS_BY_PRICE);
			pstmt.setLong(1, custId);
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
			throw new DAOException("The reading of the customer coupons, costing up to " + price
					+ ", failed due to a problem arising from the connection pool.", e);
		} catch (SQLException e) {
			throw new DAOException("The reading of the customer coupons, costing up to " + price
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
						"Problem while closing resources related to the reading of the customer coupons, costing up to "
								+ price + ".",
						e);
			}
		}
		return coupons;

	}

	/**
	 * public boolean updateCustomer(Customer customer)
	 * 
	 * @param Customer
	 * @throws DAOException
	 * @return boolean, relating to the success of the updating process
	 */

	@Override
	public boolean updateCustomer(Customer customer) throws DAOException {

		final String UPDATE = "UPDATE Customer SET CUST_NAME=?, PASSWORD=? WHERE ID=?";

		Connection connection = null;
		PreparedStatement pstmt = null;

		try {

			connection = pool.getConnection();
			pstmt = connection.prepareStatement(UPDATE);

			pstmt.setString(1, customer.getCustName());
			pstmt.setString(2, customer.getPassword());
			pstmt.setLong(3, customer.getId());
			int recordsUpdated = pstmt.executeUpdate();

			// The method executeUpdate() will return 0 if no record was updated
			if (recordsUpdated == 0) {
				throw new DAOException(
						"Problem in updating the customer. Please make sure the data you insert is valid.");
			}

		} catch (ConPoolException e) {
			throw new DAOException("Customer update failed due to a problem arising from the connection pool.", e);
		} catch (SQLException e) {
			throw new DAOException("Customer update failed. Please make sure the data you insert is valid.", e);
		} finally {
			pool.returnConnection(connection);
			try {
				if (pstmt != null)
					pstmt.close();
			} catch (SQLException e) {
				throw new DAOException("Problem while closing resources related to the customer update.", e);
			}
		}
		return true; // No Exceptions thrown

	}

	/**
	 * public boolean deleteCustomer(Customer customer)
	 * 
	 * @param Customer
	 * @throws DAOException
	 * @return boolean, relating to the success of the deleting process
	 */

	@Override
	public boolean deleteCustomer(Customer customer) throws DAOException {

		final String DELETE = "DELETE FROM Customer WHERE ID=?";

		Connection connection = null;
		PreparedStatement pstmt = null;

		try {
			connection = pool.getConnection();
			pstmt = connection.prepareStatement(DELETE);
			pstmt.setLong(1, customer.getId());
			int recordsDeleted = pstmt.executeUpdate();

			// The method executeUpdate() will return 0 if no record was deleted
			if (recordsDeleted == 0) {
				throw new DAOException(
						"Problem in deleting the customer. Please make sure the data you insert is valid.");
			}

		} catch (ConPoolException e) {
			throw new DAOException("Customer deletion failed due to a problem arising from the connection pool.", e);
		} catch (SQLException e) {
			throw new DAOException("Customer deletion failed. Please make sure the data you insert is valid.", e);
		} finally {
			pool.returnConnection(connection);
			try {
				if (pstmt != null)
					pstmt.close();
			} catch (SQLException e) {
				throw new DAOException("Problem while closing resources related to the customer deletion.", e);
			}
		}
		return true; // No Exceptions thrown

	}

	/**
	 * public void deleteCustomerCoupons(long custId) deletes all customer
	 * coupons from table Customer_coupon
	 * 
	 * @param long
	 *            represents the customer id
	 * @throws DAOException
	 */

	@Override
	public void deleteCustomerCoupons(long custId) throws DAOException {

		final String COUPONS = "SELECT * FROM Customer_Coupon WHERE CUST_ID=?";
		final String DEL_CUST_COUP = "DELETE FROM Customer_Coupon WHERE COUPON_ID=?";

		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			connection = pool.getConnection();
			pstmt = connection.prepareStatement(COUPONS);
			pstmt.setLong(1, custId);
			rs = pstmt.executeQuery();

			while (rs.next()) {

				long coupId = rs.getLong(2);

				pstmt = connection.prepareStatement(DEL_CUST_COUP);
				pstmt.setLong(1, coupId);
				pstmt.executeUpdate();
				if (pstmt != null)
					pstmt.close();

			}

		} catch (ConPoolException e) {
			throw new DAOException(
					"The deletion of the customer coupons failed due to a problem arising from the connection pool.",
					e);
		} catch (SQLException e) {
			throw new DAOException(
					"The deletion of the customer coupons failed. Please make sure the data you insert is valid.", e);
		} finally {
			pool.returnConnection(connection);
			try {
				if (pstmt != null)
					pstmt.close();
				if (rs != null)
					rs.close();
			} catch (SQLException e) {
				throw new DAOException(
						"Problem while closing resources related to the deletion of the customer coupons.", e);
			}
		}

	}

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

	@Override
	public boolean login(long id, String password) throws DAOException {

		final String LOGIN = "SELECT * FROM Customer WHERE ID=? AND PASSWORD=?";

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
			throw new DAOException("Customer login failed due to a problem arising from the connection pool.", e);
		} catch (SQLException e) {
			throw new DAOException("Customer login failed. Please make sure the data you insert is valid.", e);
		} finally {
			pool.returnConnection(connection);
			try {
				if (pstmt != null)
					pstmt.close();
				if (rs != null)
					rs.close();
			} catch (SQLException e) {
				throw new DAOException("Problem while closing resources related to the customer login.", e);
			}
		}
		return loginStatus;

	}

	/**
	 * public boolean checkCustomerName(String name)
	 * 
	 * @param String
	 *            checked to make sure that this customer name doesn't already
	 *            exist in the database
	 * @throws DAOException
	 * @return boolean, relating to the existence of the name in the database
	 */

	@Override
	public boolean checkCustomerName(String name) throws DAOException {

		final String PRECHECK = "SELECT * FROM Customer WHERE CUST_NAME=?";

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
			throw new DAOException("Customer name-check failed due to a problem arising from the connection pool.", e);
		} catch (SQLException e) {
			throw new DAOException("Customer name-check failed. Please make sure the data you insert is valid.", e);
		} finally {
			pool.returnConnection(connection);
			try {
				if (pstmt != null)
					pstmt.close();
				if (rs != null)
					rs.close();
			} catch (SQLException e) {
				throw new DAOException("Problem while closing resources related to the customer name-check.", e);
			}
		}
		return doesNameExist;

	}

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

	public Collection<Coupon> readUnpurchasedCoupons(long custId) throws DAOException {

		CouponDAO couponDAO = new CouponDBDAO();
		Collection<Coupon> allSystemCoupons = couponDAO.readAllCoupons();
		Collection<Coupon> purchasedCoupons = this.readCoupons(custId);
		Collection<Coupon> coupons = new LinkedList<>();
		Date currentDate = new Date();

		// Putting on the list only coupons that have not been purchased by the
		// customer, that are still in stock, and whose end date has yet to pass

		for (Coupon systemCoupon : allSystemCoupons) {
			boolean validForList = true;
			for (Coupon purchasedCoupon : purchasedCoupons) {
				if (systemCoupon.getId() == purchasedCoupon.getId() || systemCoupon.getAmount() == 0
						|| systemCoupon.getEndDate().before(currentDate)) {
					validForList = false;
					break;
				}
			}
			if (validForList) {
				coupons.add(systemCoupon);
			}
		}
		return coupons;

	}

}