package a.DatabaseTables;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * This class creates the five required database tables: Company, Customer,
 * Coupon, Customer_Coupon and Company_Coupon
 */

public class CreatingTables {

	public static void main(String[] args) throws ClassNotFoundException, IOException, SQLException {

		String driverName = null;
		String dbUrl = null;
		File file = null;
		Connection connection = null;
		BufferedReader in = null;
		Statement stmt = null;

		try {

			driverName = "com.mysql.jdbc.Driver";
			Class.forName(driverName);
			System.out.println("The driver " + driverName + " is loaded.");

			// Reading the URL of the database from a file

			file = new File("Files/DatabaseURL");
			in = new BufferedReader(new FileReader(file));
			dbUrl = in.readLine();

			// Defining the tables

			String company = "CREATE TABLE Company(ID BIGINT PRIMARY KEY AUTO_INCREMENT, COMP_NAME VARCHAR(70), PASSWORD VARCHAR(20), EMAIL VARCHAR(35))";
			String customer = "CREATE TABLE Customer(ID BIGINT PRIMARY KEY AUTO_INCREMENT, CUST_NAME VARCHAR(70), PASSWORD VARCHAR(20))";
			String coupon = "CREATE TABLE Coupon(ID BIGINT PRIMARY KEY AUTO_INCREMENT, TITLE VARCHAR(100), START_DATE DATE, END_DATE DATE, AMOUNT INTEGER, TYPE VARCHAR(70), MESSAGE VARCHAR(500), PRICE DOUBLE, IMAGE MEDIUMBLOB)";
			String customer_coupon = "CREATE TABLE Customer_Coupon(CUST_ID BIGINT, COUPON_ID BIGINT, PRIMARY KEY(CUST_ID, COUPON_ID))";
			String company_coupon = "CREATE TABLE Company_Coupon(COMP_ID BIGINT, COUPON_ID BIGINT, PRIMARY KEY(COMP_ID, COUPON_ID))";

			// Connecting to the database and creating the tables above

			connection = DriverManager.getConnection(dbUrl,"root","root");
			System.out.println("Connected to: " + dbUrl + ".");

			stmt = connection.createStatement();
			stmt.executeUpdate(company);
			System.out.println("The Company table was successfully created in the database.");
			stmt.executeUpdate(customer);
			System.out.println("The Customer table was successfully created in the database.");
			stmt.executeUpdate(coupon);
			System.out.println("The Coupon table was successfully created in the database.");
			stmt.executeUpdate(customer_coupon);
			System.out.println("The Customer_Coupon table was successfully created in the database.");
			stmt.executeUpdate(company_coupon);
			System.out.println("The Company_Coupon table was successfully created in the database.");

		} catch (ClassNotFoundException | IOException | SQLException e) {
			System.out.println(e.getMessage());
			if (e.getCause() != null)
				System.out.println(e.getCause());
		} finally {
			try {
				if (in != null)
					in.close();
				if (stmt != null)
					stmt.close();
				if (connection != null)
					connection.close();
			} catch (IOException | SQLException e) {
				System.out.println(e.getMessage());
				if (e.getCause() != null)
					System.out.println(e.getCause());
			}
			System.out.println("You are now disconnected from the database.");
		}

	}

}