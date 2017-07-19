package f.FinalTests;

import java.util.Collection;
import java.util.List;

import b.JavaBeans.Coupon;
import b.JavaBeans.Customer;
import c.Facades.AdminFacade;
import c.Facades.ClientType;
import c.Facades.CustomerFacade;
import e.CouponSystem.CouponSystem;
import e.CouponSystem.CouponSystemException;

public class TestingReadAllUnpurchasedCoupons {

	public static void main(String[] args) {

		CouponSystem couponSystem = null;

		try {

			// Initialization of the connection pool and the daily thread

			couponSystem = CouponSystem.getInstance();

			// Reading customers from the database for subsequent login (we
			// don't use the explicit id numbers for the login since the data
			// could be deleted and added over and over, while the id number
			// changes each time)

			AdminFacade adminFacade = (AdminFacade) couponSystem.login("admin", "1234", ClientType.ADMIN);

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

			// Loading of DAOs and attainment of CustomerFacade via proper login

			CustomerFacade customerFacade = (CustomerFacade) couponSystem.login(customer.getId(), "footy66",
					ClientType.CUSTOMER);

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

			// Testing the method readAllUnpurchasedCoupons()

			coupons = customerFacade.readAllUnpurchasedCoupons();
			if (coupons.size() != 0) {
				for (Coupon coup : coupons) {
					System.out.println("The following coupon does NOT belong to the customer: " + coup);
				}
			} else {
				System.out.println("There are no coupons NOT belonging to the customer.");
			}

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