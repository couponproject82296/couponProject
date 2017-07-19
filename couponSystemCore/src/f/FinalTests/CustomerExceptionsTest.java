package f.FinalTests;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import b.DAOs.CouponDAO;
import b.DBDAOs.CouponDBDAO;
import b.JavaBeans.Company;
import b.JavaBeans.Coupon;
import b.JavaBeans.CouponType;
import b.JavaBeans.Customer;
import c.Facades.AdminFacade;
import c.Facades.ClientType;
import c.Facades.CompanyFacade;
import c.Facades.CustomerFacade;
import e.CouponSystem.CouponSystem;
import e.CouponSystem.CouponSystemException;

/**
 * This class puts the CouponSystem to the test as a whole , and particularly
 * CustomerFacade. It relies on the outcome of the file AddingToTablesForTests,
 * which creates new companies, customers and coupons in the database tables. It
 * demonstrates the system abilities only in cases where Exceptions are thrown.
 */

public class CustomerExceptionsTest {

	public static void main(String[] args) {

		CouponSystem couponSystem = null;
		CustomerFacade customerFacade = null;
		CouponDAO couponDAO = null;
		Customer customer = null;
		Coupon coupon1 = null;
		Coupon coupon2 = null;
		Coupon coupon3 = null;

		Calendar calendar = Calendar.getInstance();
		calendar.set(2017, Calendar.JUNE, 20, 0, 0, 0);
		Date date1 = calendar.getTime();
		calendar.set(2017, Calendar.JUNE, 29, 0, 0, 0);
		Date date2 = calendar.getTime();
		calendar.set(2017, Calendar.MARCH, 20, 0, 0, 0);
		Date date3 = calendar.getTime();
		calendar.set(2017, Calendar.MARCH, 29, 0, 0, 0);
		Date date4 = calendar.getTime();

		try {

			// Initialization of the connection pool and the daily thread

			couponSystem = CouponSystem.getInstance();

			// Reading companies and customers from the database for subsequent
			// login (we don't use the explicit id numbers for the login since
			// the data could be deleted and added over and over, while the id
			// number changes each time)

			AdminFacade adminFacade = (AdminFacade) couponSystem.login("admin", "1234", ClientType.ADMIN);

			Company company = new Company();
			List<Company> companies = (List<Company>) adminFacade.readAllCompanies();
			if (companies.size() != 0) {
				company = companies.get(1);
			} else {
				System.out.println("There are no companies in the database.");
				System.out.println(
						"This program will shut-down. Please make sure that companies added by 'AddingToTablesForTests' haven't been deleted.");
				System.exit(1);
			}

			List<Customer> customers = (List<Customer>) adminFacade.readAllCustomers();
			if (customers.size() != 0) {
				customer = customers.get(0);
			} else {
				System.out.println("There are no customers in the database.");
				System.out.println(
						"This program will shut-down. Please make sure that customers added by 'AddingToTablesForTests' haven't been deleted.");
				System.exit(1);
			}

			// Reading the company coupons for a subsequent coupon purchase
			// attempt

			CompanyFacade companyFacade = (CompanyFacade) couponSystem.login(company.getId(), "arthrt8080",
					ClientType.COMPANY);

			List<Coupon> companyCoupons = (List<Coupon>) companyFacade.readAllCoupons();
			if (companyCoupons.size() != 0) {
				coupon1 = companyCoupons.get(0);
				coupon2 = companyCoupons.get(1);
			} else {
				System.out.println("There are no coupons in the database.");
				System.out.println(
						"This program will shut-down. Please make sure that coupons added by 'AddingToTablesForTests' haven't been deleted.");
				System.exit(1);
			}

			// Testing the method login(long id, String password, ClientType
			// clientType) when trying to login with invalid data

			customerFacade = (CustomerFacade) couponSystem.login(customer.getId(), "6666", ClientType.CUSTOMER);

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

			// Loading of DAOs and attainment of CustomerFacade via proper login

			customerFacade = (CustomerFacade) couponSystem.login(customer.getId(), "footy66", ClientType.CUSTOMER);

			// Testing the method purchaseCoupon(Coupon coupon) when trying to
			// purchase a coupon that is out of stock

			customerFacade.purchaseCoupon(coupon1);
			System.out.println("The coupon you have purchased is: " + coupon1);
			// The message won't be printed in light of the expected Exception

		} catch (CouponSystemException e) {
			System.out.println(e.getMessage());
			if (e.getCause() != null)
				System.out.println(e.getCause());
		}

		System.out.println("==========");

		try {

			// Testing the method purchaseCoupon(Coupon coupon) when trying to
			// purchase a coupon that is not valid anymore (firstly we will
			// create a coupon that is not valid via CouponDBDAO, and we will
			// later delete this coupon)

			coupon3 = new Coupon("Coke is not a joke", date3, date4, 28, CouponType.BEVERAGES,
					"Enjoy a great coke at a great price", 15.2, "http://cokenojoke.com");
			couponDAO = new CouponDBDAO();
			couponDAO.createCoupon(coupon3);
			customerFacade.purchaseCoupon(coupon3);
			System.out.println("The coupon you have purchased is: " + coupon3);
			// The message won't be printed in light of the expected Exception

		} catch (CouponSystemException e) {
			System.out.println(e.getMessage());
			if (e.getCause() != null)
				System.out.println(e.getCause());
		}

		System.out.println("==========");

		try {

			// Deleting the coupon created above, since it is no longer needed

			couponDAO.deleteCoupon(coupon3);

			// Testing the method purchaseCoupon(Coupon coupon) when trying to
			// purchase a coupon that doesn't exist in the database

			Coupon coupon = new Coupon("Everyday shoes", date1, date2, 33, CouponType.SHOES,
					"Amazing prices for all Everyday shoes", 86, "http://everydayshoes.com");
			customerFacade.purchaseCoupon(coupon);
			System.out.println("The coupon you have purchased is: " + coupon);
			// The message won't be printed in light of the expected Exception

		} catch (CouponSystemException e) {
			System.out.println(e.getMessage());
			if (e.getCause() != null)
				System.out.println(e.getCause());
		}

		System.out.println("==========");

		try {

			// Testing the method purchaseCoupon(Coupon coupon) when trying to
			// purchase a coupon that has already been purchased by the customer

			customerFacade.purchaseCoupon(coupon2);
			System.out.println("The coupon you have purchased is: " + coupon2);
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