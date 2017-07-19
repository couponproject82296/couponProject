package f.FinalTests;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;

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
 * demonstrates the system abilities in cases where no Exceptions are thrown.
 */

public class CustomerDirectTest {

	public static void main(String[] args) {

		CouponSystem couponSystem = null;

		Calendar calendar = Calendar.getInstance();
		calendar.set(2017, Calendar.JUNE, 21, 0, 0, 0);
		Date date1 = calendar.getTime();
		calendar.set(2017, Calendar.JUNE, 25, 0, 0, 0);
		Date date2 = calendar.getTime();

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

			Customer customer = new Customer();
			List<Customer> customers = (List<Customer>) adminFacade.readAllCustomers();
			if (customers.size() != 0) {
				customer = customers.get(0);
			} else {
				System.out.println("There are no customers in the database.");
				System.out.println(
						"This program will shut-down. Please make sure that customers added by 'AddingToTablesForTests' haven't been deleted.");
				System.exit(1);
			}

			// Issuing a coupon by the company above for subsequent coupon
			// purchase

			CompanyFacade companyFacade = (CompanyFacade) couponSystem.login(company.getId(), "arthrt8080",
					ClientType.COMPANY);

			Coupon coupon = new Coupon("Everyday shoes", date1, date2, 33, CouponType.SHOES,
					"Amazing prices for all Everyday shoes", 86, "http://everydayshoes.com");
			companyFacade.createCoupon(coupon);

			// Loading of DAOs and attainment of CustomerFacade via proper login

			CustomerFacade customerFacade = (CustomerFacade) couponSystem.login(customer.getId(), "footy66",
					ClientType.CUSTOMER);

			// Testing the method purchaseCoupon(Coupon coupon)

			customerFacade.purchaseCoupon(coupon);
			System.out.println("The coupon you have purchased is: " + coupon);

			System.out.println("==========");

			// Testing the method readCustomer()

			Customer customerRead = customerFacade.readCustomer();
			System.out.println("The customer you have requested is: " + customerRead);

			System.out.println("==========");

			// Testing the method readAllPurchasedCoupons()

			Collection<Coupon> coupons = customerFacade.readAllPurchasedCoupons();
			if (coupons.size() != 0) {
				for (Coupon coup : coupons) {
					System.out.println("The following coupon belongs to the customer: " + coup);
				}
			} else {
				System.out.println("There are no coupons belonging to the customer.");
			}

			System.out.println("==========");

			// Testing the method readAllPurchasedCouponsByType(CouponType type)

			coupons = customerFacade.readAllPurchasedCouponsByType(CouponType.SHOES);
			if (coupons.size() != 0) {
				for (Coupon coup : coupons) {
					System.out.println(
							"The following coupon of type " + CouponType.SHOES + " belongs to the customer: " + coup);
				}
			} else {
				System.out
						.println("There are no coupons of type " + CouponType.SHOES + " that belong to the customer.");
			}

			coupons = customerFacade.readAllPurchasedCouponsByType(CouponType.SPORTS);
			if (coupons.size() != 0) {
				for (Coupon coup : coupons) {
					System.out.println(
							"The following coupon of type " + CouponType.SPORTS + " belongs to the customer: " + coup);
				}
			} else {
				System.out
						.println("There are no coupons of type " + CouponType.SPORTS + " that belong to the customer.");
			}

			System.out.println("==========");

			// Testing the method readAllPurchasedCouponsByPrice(double price)

			double price = 150;
			coupons = customerFacade.readAllPurchasedCouponsByPrice(price);
			if (coupons.size() != 0) {
				for (Coupon coup : coupons) {
					System.out.println(
							"The following coupon, costing up to " + price + ", belongs to the customer: " + coup);
				}
			} else {
				System.out.println("There are no coupons costing up to " + price + " that belong to the customer.");
			}

			price = 50;
			coupons = customerFacade.readAllPurchasedCouponsByPrice(price);
			if (coupons.size() != 0) {
				for (Coupon coup : coupons) {
					System.out.println(
							"The following coupon, costing up to " + price + ", belongs to the customer: " + coup);
				}
			} else {
				System.out.println("There are no coupons costing up to " + price + " that belong to the customer.");
			}

			System.out.println("==========");

			// Deleting the coupon created above (so that this test file could
			// be run endlessly) by attaining the company that issued the

			boolean deleteStatus = companyFacade.deleteCoupon(coupon);
			System.out.println("Has the coupon been deleted successfully? " + deleteStatus);

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