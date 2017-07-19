package f.FinalTests;

import b.JavaBeans.Company;
import b.JavaBeans.Customer;
import c.Facades.AdminFacade;
import c.Facades.ClientType;
import e.CouponSystem.CouponSystem;
import e.CouponSystem.CouponSystemException;

/**
 * This class puts the CouponSystem to the test as a whole , and particularly
 * AdminFacade. It relies on the outcome of the file AddingToTablesForTests,
 * which creates new companies, customers and coupons in the database tables. It
 * demonstrates the system abilities only in cases where Exceptions are thrown.
 */

public class AdminExceptionsTest {

	public static void main(String[] args) {

		CouponSystem couponSystem = null;
		AdminFacade adminFacade = null;
		Company company1 = null;
		Company company2 = null;
		Company company3 = null;
		Customer customer1 = null;
		Customer customer2 = null;
		Customer customer3 = null;

		try {

			// Initialization of the connection pool and the daily thread

			couponSystem = CouponSystem.getInstance();

			// Testing the method login(String userName, String password,
			// ClientType clientType) when trying to login with invalid data

			adminFacade = (AdminFacade) couponSystem.login("admin", "1111", ClientType.ADMIN);

		} catch (CouponSystemException e) {
			System.out.println(e.getMessage());
			if (e.getCause() != null)
				System.out.println(e.getCause());
		}

		if (couponSystem == null) {
			System.out.println("The database server is down. This program will come to a halt.");
			System.exit(1);
		}

		System.out.println("==========");

		try {

			// Loading of DAOs and attainment of AdminFacade via proper login

			adminFacade = (AdminFacade) couponSystem.login("admin", "1234", ClientType.ADMIN);

			// Testing the method createCompany(Company company) when trying to
			// create a company whose name already exists in the database

			company1 = new Company("Walking with grace", "walker33", "walk@grace.co.il");
			adminFacade.createCompany(company1);
			System.out.println("The company you have created is: " + company1);
			// The message won't be printed in light of the expected Exception

		} catch (CouponSystemException e) {
			System.out.println(e.getMessage());
			if (e.getCause() != null)
				System.out.println(e.getCause());
		}

		System.out.println("==========");

		try {

			// Testing the method createCompany(Company company) when trying to
			// create a company with null values as name/password/email

			company2 = new Company("Hello world", null, "hello@world.com");
			adminFacade.createCompany(company2);
			System.out.println("The company you have created is: " + company2);
			// The message won't be printed in light of the expected Exception

		} catch (CouponSystemException e) {
			System.out.println(e.getMessage());
			if (e.getCause() != null)
				System.out.println(e.getCause());
		}

		System.out.println("==========");

		try {

			// Testing the method readCompany(long id) when trying to read a
			// company that doesn't exist in the database

			Company companyRead = adminFacade.readCompany(9000);
			System.out.println("The company you have requested is: " + companyRead);
			// The message won't be printed in light of the expected Exception

		} catch (CouponSystemException e) {
			System.out.println(e.getMessage());
			if (e.getCause() != null)
				System.out.println(e.getCause());
		}

		System.out.println("==========");

		try {

			// Testing the method updateCompany(Company company) when trying to
			// update a company that doesn't exist in the database

			company2.setId(9999);
			adminFacade.updateCompany(company2);
			System.out.println("The company has been updated to: " + company2);
			// The message won't be printed in light of the expected Exception

		} catch (CouponSystemException e) {
			System.out.println(e.getMessage());
			if (e.getCause() != null)
				System.out.println(e.getCause());
		}

		System.out.println("==========");

		try {

			// Testing the method updateCompany(Company company) when trying to
			// update a company with a null value as its new email (firstly we
			// will create a new company that can be easily updated, and later
			// deleted)

			company3 = new Company("Yoyo", "yoyo9090", "yoyo@yoyo.co.il");
			adminFacade.createCompany(company3);
			company3.setEmail(null);
			adminFacade.updateCompany(company3);
			System.out.println("The company has been updated to: " + company3);
			// The message won't be printed in light of the expected Exception

		} catch (CouponSystemException e) {
			System.out.println(e.getMessage());
			if (e.getCause() != null)
				System.out.println(e.getCause());
		}

		System.out.println("==========");

		try {

			// Deleting the company created above so the test file could be
			// easily run again

			adminFacade.deleteCompany(company3);

			// Testing the method deleteCompany(Company company) when trying to
			// delete a company that doesn't exist in the database

			boolean deleteStatus = adminFacade.deleteCompany(company1);
			System.out.println("Has the company been deleted? " + deleteStatus);
			// The message won't be printed in light of the expected Exception

		} catch (CouponSystemException e) {
			System.out.println(e.getMessage());
			if (e.getCause() != null)
				System.out.println(e.getCause());
		}

		System.out.println("==========");

		try {

			// Testing the method createCustomer(Customer customer) when trying
			// to create a customer whose name already exists in the database

			customer1 = new Customer("David shoe guy", "walker33");
			adminFacade.createCustomer(customer1);
			System.out.println("The customer you have created is: " + customer1);
			// The message won't be printed in light of the expected Exception

		} catch (CouponSystemException e) {
			System.out.println(e.getMessage());
			if (e.getCause() != null)
				System.out.println(e.getCause());
		}

		System.out.println("==========");

		try {

			// Testing the method createCustomer(Customer customer) when trying
			// to create a customer with null values as name/password

			customer2 = new Customer(null, "Hello55");
			adminFacade.createCustomer(customer2);
			System.out.println("The customer you have created is: " + customer2);
			// The message won't be printed in light of the expected Exception

		} catch (CouponSystemException e) {
			System.out.println(e.getMessage());
			if (e.getCause() != null)
				System.out.println(e.getCause());
		}

		System.out.println("==========");

		try {

			// Testing the method readCustomer(long id) when trying to read a
			// customer that doesn't exist in the database

			Customer customerRead = adminFacade.readCustomer(9000);
			System.out.println("The customer you have requested is: " + customerRead);
			// The message won't be printed in light of the expected Exception

		} catch (CouponSystemException e) {
			System.out.println(e.getMessage());
			if (e.getCause() != null)
				System.out.println(e.getCause());
		}

		System.out.println("==========");

		try {

			// Testing the method updateCustomer(Customer customer) when trying
			// to update a customer that doesn't exist in the database

			customer2.setId(9999);
			adminFacade.updateCustomer(customer2);
			System.out.println("The customer has been updated to: " + customer2);
			// The message won't be printed in light of the expected Exception

		} catch (CouponSystemException e) {
			System.out.println(e.getMessage());
			if (e.getCause() != null)
				System.out.println(e.getCause());
		}

		System.out.println("==========");

		try {

			// Testing the method updateCustomer(Customer customer) when trying
			// to
			// update a customer with a null value as its new password (firstly
			// we
			// will create a new customer that can be easily updated, and later
			// deleted)

			customer3 = new Customer("Dodo", "dodo4040");
			adminFacade.createCustomer(customer3);
			customer3.setPassword(null);
			adminFacade.updateCustomer(customer3);
			System.out.println("The customer has been updated to: " + customer3);
			// The message won't be printed in light of the expected Exception

		} catch (CouponSystemException e) {
			System.out.println(e.getMessage());
			if (e.getCause() != null)
				System.out.println(e.getCause());
		}

		System.out.println("==========");

		try {

			// Deleting the customer created above so the test file could be
			// easily run again

			adminFacade.deleteCustomer(customer3);

			// Testing the method deleteCustomer(Customer customer) when trying
			// to delete a customer that doesn't exist in the database

			boolean deleteStatus = adminFacade.deleteCustomer(customer1);
			System.out.println("Has the customer been deleted? " + deleteStatus);
			// The message won't be printed in light of the expected Exception

		} catch (CouponSystemException e) {
			System.out.println(e.getMessage());
			if (e.getCause() != null)
				System.out.println(e.getCause());

		} finally {
			try {
				// Stopping the daily thread and closing the connection pool
				couponSystem.shutdown();
			} catch (CouponSystemException e) {
				e.printStackTrace();
			}
		}

		System.out.println("===== End of main =====");

	}

}