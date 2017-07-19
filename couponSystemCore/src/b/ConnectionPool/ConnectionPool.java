package b.ConnectionPool;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * This class creates the connection pool singleton that will serve operations
 * involving the database
 */

public class ConnectionPool {

	// Statics

	private static ConnectionPool instance;
	public static final int MAX_CONNECTIONS = 10;

	// Attributes

	private Set<Connection> conSet = new HashSet<>();
	private boolean isPoolOpen;

	// Private CTOR (creates the pool instance and opens all the connections)

	private ConnectionPool() throws ConPoolException {

		String dbUrl = null;
		Connection connection = null;
		String driverName = null;
		BufferedReader in = null;

		try {

			isPoolOpen = false; // This will change if no exceptions are thrown

			driverName = "com.mysql.jdbc.Driver";
			Class.forName(driverName);

			// Reading the URL of the database from a file

			in = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/Files/DatabaseURL")));
			dbUrl = in.readLine();
			// For running the project not (!) as a jar, the following row needs
			// to supplement the former (and some exceptions should be omitted):
//			dbUrl = "jdbc:mysql://localhost:3306/dbcoupon";

			// Populating the conSet with the potential MAX_CONNECTIONS

			for (int i = 0; i < MAX_CONNECTIONS; i++) {

				conSet.add(DriverManager.getConnection(dbUrl, "root", "root"));
			}
			// If we are here, the connection pool is ready for use

			isPoolOpen = true;

		} catch (ClassNotFoundException e) {
			throw new ConPoolException("Problem in the connection pool, related to driver loading.", e);
		} catch (SQLException e) {
			throw new ConPoolException(
					"Problem in the coonection pool, while adding connections. Please make sure that the database server is running.",
					e);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (in != null)
					in.close();
				if (connection != null)
					connection.close();
			} catch (IOException | SQLException e) {
				throw new ConPoolException("Problem in the connection pool, while closing resources.", e);
			}
		}
	}

	// Getter for ConnectionPool instance (summons the ConnectionPool CTOR)

	public static ConnectionPool getInstance() throws ConPoolException {
		if (instance == null) {
			instance = new ConnectionPool();
		}
		return instance;
	}

	// Getters for attributes (no setters, since the set of connections and
	// the status of the pool should not be altered outside the methods below)

	public Set<Connection> getConSet() {
		return conSet;
	}

	public boolean isPoolOpen() {
		return isPoolOpen;
	}

	// Methods dealing with the pool connections in relation to system clients

	/**
	 * public synchronized Connection getConnection()
	 * 
	 * @throws ConPoolException
	 * @return Connection
	 */

	public synchronized Connection getConnection() throws ConPoolException {
		Connection connection;
		if (isPoolOpen) {
			while (conSet.isEmpty()) {
				try {
					this.wait();
				} catch (InterruptedException e) {
					throw new ConPoolException("User interrupted while waiting for a connection.", e);
				}
			}
			this.notifyAll();
			Iterator<Connection> iterator = conSet.iterator();
			connection = iterator.next();
			conSet.remove(connection);
			return connection;
		} else {
			throw new ConPoolException(
					"No connection has been made since the connection pool has been closed for use.");
		}
	}

	/**
	 * public synchronized void returnConnection(Connection userCon)
	 * 
	 * @param Connection
	 */

	public synchronized void returnConnection(Connection userCon) {
		conSet.add(userCon);
		this.notifyAll();
	}

	/**
	 * public void closeAllConnections() closes connections during system
	 * shut-down; active users are awaited to finish their work and return the
	 * connection
	 * 
	 * @throws ConPoolException
	 */

	public void closeAllConnections() throws ConPoolException {

		isPoolOpen = false; // Will prevent new users from getting connections

		while (conSet.size() < MAX_CONNECTIONS)
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				throw new ConPoolException("System interrupted while waiting for all connection to be returned.", e);
			}

		for (Connection connection : conSet) {
			try {
				connection.close();
			} catch (SQLException e) {
				throw new ConPoolException("Problem in the connection pool, while closing all connections.", e);
			}
		}

	}

}