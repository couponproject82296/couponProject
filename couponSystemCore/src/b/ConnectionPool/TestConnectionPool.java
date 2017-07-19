package b.ConnectionPool;

import java.sql.Connection;

/**
 * This class puts the connection pool singleton to the test
 */

public class TestConnectionPool {

	public static void main(String[] args) {

		try {

			ConnectionPool pool = ConnectionPool.getInstance();
			boolean poolStatus = pool.isPoolOpen();
			System.out.println("Is the connection pool open? " + poolStatus);

			Connection connection = pool.getConnection();
			poolStatus = pool.isPoolOpen();
			System.out.println("Is the connection pool open? " + poolStatus);

			pool.returnConnection(connection);
			poolStatus = pool.isPoolOpen();
			System.out.println("Is the connection pool open? " + poolStatus);

			pool.closeAllConnections();
			poolStatus = pool.isPoolOpen();
			System.out.println("Is the connection pool open? " + poolStatus);

		} catch (ConPoolException e) {
			System.out.println(e.getMessage());
			if (e.getCause() != null)
				System.out.println(e.getCause());
		}

	}

}