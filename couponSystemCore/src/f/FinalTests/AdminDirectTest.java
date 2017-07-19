package f.FinalTests;

import java.util.Collection;

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
 * demonstrates the system abilities in cases where no Exceptions are thrown.
 */

public class AdminDirectTest {

	public static void main(String[] args) {

		CouponSystem couponSystem = null;

		try {

			// Initialization of the connection pool and the daily thread

			couponSystem = CouponSystem.getInstance();

			// Loading of DAOs and attainment of AdminFacade via proper login

			AdminFacade adminFacade = (AdminFacade) couponSystem.login("admin", "1234", ClientType.ADMIN);

			// Testing the method createCompany(Company company)

			Company company = new Company("Yoyo", "yoyo9090", "yoyo@yoyo.co.il");
			adminFacade.createCompany(company);
			System.out.println("The company you have created is: " + company);

			System.out.println("==========");

			// Testing the method readCompany(long id)

			Company companyRead = adminFacade.readCompany(company.getId());
			System.out.println("The company you have requested is: " + companyRead);

			System.out.println("==========");

			// Testing the method readAllCompanies()

			Collection<Company> companies = adminFacade.readAllCompanies();
			if (companies.size() != 0) {
				for (Company comp : companies) {
					System.out.println("The following company exists in the database: " + comp);
				}
			} else {
				System.out.println("There are no companies in the database.");
			}

			System.out.println("==========");

			// Testing the method updateCompany(Company company)

			company.setCompName("YoyoAllDay"); // Will not be altered!
			company.setPassword("yoyo1111");
			company.setEmail("yoyo@yoyo.com");
			adminFacade.updateCompany(company);
			// Both the company instance and database record are updated:
			System.out.println("The company instance has been updated to: " + company);
			company = adminFacade.readCompany(company.getId());
			System.out.println("The company listing has been updated to: " + company);

			System.out.println("==========");

			// Testing the method deleteCompany(Company company)

			boolean deleteStatus = adminFacade.deleteCompany(company);
			System.out.println("Has the company been deleted successfully? " + deleteStatus);

			System.out.println("==========");

			// Testing the method createCustomer(Customer customer)

			Customer customer = new Customer("Dodo", "dodo4040");
			adminFacade.createCustomer(customer);
			System.out.println("The customer you have created is: " + customer);

			System.out.println("==========");

			// Testing the method readCustomer(long id)

			Customer customerRead = adminFacade.readCustomer(customer.getId());
			System.out.println("The customer you have requested is: " + customerRead);

			System.out.println("==========");

			// Testing the method readAllCustomers()

			Collection<Customer> customers = adminFacade.readAllCustomers();
			if (customers.size() != 0) {
				for (Customer cust : customers) {
					System.out.println("The following customer exists in the database: " + cust);
				}
			} else {
				System.out.println("There are no customers in the database.");
			}

			System.out.println("==========");

			// Testing the method updateCustomer(Customer customer)

			customer.setCustName("DodoAllDay"); // Will not be altered!
			customer.setPassword("dodo2222");
			adminFacade.updateCustomer(customer);
			// Both the customer instance and database record are updated:
			System.out.println("The customer instance has been updated to :" + customer);
			customer = adminFacade.readCustomer(customer.getId());
			System.out.println("The customer listing has been updated to :" + customer);

			System.out.println("==========");

			// Testing the method deleteCustomer(Customer customer)

			deleteStatus = adminFacade.deleteCustomer(customer);
			System.out.println("Has the customer been deleted successfully? " + deleteStatus);

			System.out.println("==========");

		} catch (CouponSystemException e) {
			System.out.println(e.getMessage());
			if (e.getCause() != null)
				System.out.println(e.getCause());

		} finally {
			if (couponSystem == null) {
				System.out.println("The database server is down. This program will come to a halt.");
			} else {
				try {
					// Stopping the daily thread and closing the connection pool
					couponSystem.shutdown();
				} catch (CouponSystemException e) {
					e.printStackTrace();
				}
			}
		}

		System.out.println("===== End of main =====");

	}

}