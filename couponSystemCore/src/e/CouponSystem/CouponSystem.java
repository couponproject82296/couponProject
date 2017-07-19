package e.CouponSystem;

import b.ConnectionPool.ConPoolException;
import b.ConnectionPool.ConnectionPool;
import b.DAOs.CompanyDAO;
import b.DAOs.CustomerDAO;
import b.DAOs.DAOException;
import b.DBDAOs.CompanyDBDAO;
import b.DBDAOs.CustomerDBDAO;
import c.Facades.AdminFacade;
import c.Facades.ClientType;
import c.Facades.CompanyFacade;
import c.Facades.CouponClientFacade;
import c.Facades.CustomerFacade;
import c.Facades.FacadeException;
import d.DailyThread.DailyCouponExpirationTask;

/**
 * This CouponSystem class is a Singleton that will use as a base, which ties
 * together all system components. It will allows users (i.e. clients) to login
 * into the system and to perform a variety of tasks according to their identity
 */

public class CouponSystem {

	// Statics

	private static CouponSystem instance;

	// Attributes

	private DailyCouponExpirationTask task;
	private Thread dailyThread;

	// Private CTOR (loads and initializes the DailyCouponExpirationTask)

	private CouponSystem() throws CouponSystemException  {
		try {
			task = new DailyCouponExpirationTask();
			dailyThread = new Thread(task, "DailyThread");
			dailyThread.start();
		} catch (CouponSystemException e) {
			throw new CouponSystemException("System startup failed.");
		}
		
	}

	// Getter for CouponSystem instance (summons the CouponSystem CTOR)

	public static CouponSystem getInstance() throws CouponSystemException {
		if (instance == null) {
			instance = new CouponSystem();
		}
		return instance;
	}

	// Login methods: the overload results from the different first parameter
	// required by the administrator (user name) versus a company/customer (id)

	/**
	 * public CouponClientFacade login(String userName, String password,
	 * ClientType clientType)
	 * 
	 * @param String
	 *            represents the administrator user name
	 * @param String
	 *            represents the administrator password
	 * @param ClientType
	 *            represents the administrator client type
	 * @throws CouponSystemException
	 * @return CouponClientFacade, returned based on the accordance of the two
	 *         first parameters to constant values and of the third parameter to
	 *         the suitable client type
	 */

	public CouponClientFacade login(String userName, String password, ClientType clientType)
			throws CouponSystemException {

		try {
			final String USER = "admin";
			final String PASS = "1234";

			if (userName.equals(USER) && password.equals(PASS) && clientType.equals(ClientType.ADMIN)) {
				CouponClientFacade adminFacade = new AdminFacade();
				return adminFacade;
			} else {
				throw new CouponSystemException("Login failed. Please make sure the details you insert are correct.");
			}
		} catch (FacadeException e) {
			throw new CouponSystemException("Problem in creating the required client access display.", e);
		}

	}

	/**
	 * public CouponClientFacade login(long id, String password, ClientType
	 * clientType)
	 * 
	 * @param long
	 *            represents the company/customer id
	 * @param String
	 *            represents the company/customer password
	 * @param ClientType
	 *            represents the company/customer client type
	 * @throws CouponSystemException
	 * @return CouponClientFacade, returned based on the accordance of the two
	 *         first parameters to the database values and of the third
	 *         parameter to the suitable client type
	 */

	public CouponClientFacade login(long id, String password, ClientType clientType) throws CouponSystemException {

		try {
			CompanyDAO companyDAO = new CompanyDBDAO();
			CustomerDAO customerDAO = new CustomerDBDAO();

			if (companyDAO.login(id, password) && clientType.equals(ClientType.COMPANY)) {
				CouponClientFacade companyFacade = new CompanyFacade(id);
				return companyFacade;
			} else if (customerDAO.login(id, password) && clientType.equals(ClientType.CUSTOMER)) {
				CouponClientFacade customerFacade = new CustomerFacade(id);
				return customerFacade;
			} else {
				throw new CouponSystemException("Login failed. Please make sure the details you insert are correct.");
			}
		} catch (DAOException | FacadeException e) {
			throw new CouponSystemException("Problem in creating the required client access display.", e);
		}

	}

	/**
	 * public void shutdown() executes a system shutdown by closing the daily
	 * thread and the connection pool
	 * 
	 * @throws CouponSystemException
	 */

	public void shutdown() throws CouponSystemException {

		// Firstly, we need to stop the daily thread

		task.stopTask(dailyThread);

		// Secondly, we need to close the connection pool

		try {
			ConnectionPool.getInstance().closeAllConnections();
		} catch (ConPoolException e) {
			throw new CouponSystemException("Unable to perform connection shutdown.");
		}

	}

}