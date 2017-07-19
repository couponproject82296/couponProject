package f.FinalTests;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import b.JavaBeans.Company;
import b.JavaBeans.Coupon;
import b.JavaBeans.CouponType;
import c.Facades.AdminFacade;
import c.Facades.ClientType;
import c.Facades.CompanyFacade;
import e.CouponSystem.CouponSystem;
import e.CouponSystem.CouponSystemException;

/**
 * This class puts the CouponSystem to the test as a whole , and particularly
 * CompanyFacade. It relies on the outcome of the file AddingToTablesForTests,
 * which creates new companies, customers and coupons in the database tables. It
 * demonstrates the system abilities only in cases where Exceptions are thrown.
 */

public class CompanyExceptionsTest {

	public static void main(String[] args) {

		CouponSystem couponSystem = null;
		CompanyFacade companyFacade = null;
		Company company1 = null;
		Company company2 = null;
		Coupon coupon1 = null;
		Coupon coupon2 = null;
		Coupon coupon3 = null;

		Calendar calendar = Calendar.getInstance();
		calendar.set(2017, Calendar.JUNE, 20, 0, 0, 0);
		Date date1 = calendar.getTime();
		calendar.set(2017, Calendar.JUNE, 29, 0, 0, 0);
		Date date2 = calendar.getTime();
		calendar.set(2017, Calendar.MARCH, 29, 0, 0, 0);
		Date date3 = calendar.getTime();

		try {

			// Initialization of the connection pool and the daily thread

			couponSystem = CouponSystem.getInstance();

			// Reading companies and customers from the database for subsequent
			// login (we don't use the explicit id numbers for the login since
			// the data could be deleted and added over and over, while the id
			// number changes each time)

			AdminFacade adminFacade = (AdminFacade) couponSystem.login("admin", "1234", ClientType.ADMIN);

			company1 = new Company();
			List<Company> companies = (List<Company>) adminFacade.readAllCompanies();
			if (companies.size() != 0) {
				company1 = companies.get(1);
				company2 = companies.get(3);
			} else {
				System.out.println("There are no companies in the database.");
				System.out.println(
						"This program will shut-down. Please make sure that companies added by 'AddingToTablesForTests' haven't been deleted.");
				System.exit(1);
			}

			// Reading the coupons of company2 for a subsequent coupon reading
			// attempt

			companyFacade = (CompanyFacade) couponSystem.login(company2.getId(), "slices4567", ClientType.COMPANY);

			List<Coupon> companyCoupons = (List<Coupon>) companyFacade.readAllCoupons();
			if (companyCoupons.size() != 0) {
				coupon1 = companyCoupons.get(0);
			} else {
				System.out.println("There are no coupons in the database.");
				System.out.println(
						"This program will shut-down. Please make sure that coupons added by 'AddingToTablesForTests' haven't been deleted.");
				System.exit(1);
			}

			// Testing the method login(long id, String password, ClientType
			// clientType) when trying to login with invalid data

			companyFacade = (CompanyFacade) couponSystem.login(company1.getId(), "3333", ClientType.COMPANY);

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

			// Loading of DAOs and attainment of CompanyFacade via proper login

			companyFacade = (CompanyFacade) couponSystem.login(company1.getId(), "arthrt8080", ClientType.COMPANY);

			// Testing the method createCoupon(Coupon coupon) when trying to
			// create a coupon with null values as title/type/message/image

			coupon2 = new Coupon("Everyday shoes", date1, date2, 33, CouponType.SHOES, null, 86,
					"http://everydayshoes.com");
			companyFacade.createCoupon(coupon2);
			System.out.println("The coupon you have created is: " + coupon2);
			// The message won't be printed in light of the expected Exception

		} catch (CouponSystemException e) {
			System.out.println(e.getMessage());
			if (e.getCause() != null)
				System.out.println(e.getCause());
		}

		System.out.println("==========");

		try {

			// Testing the method createCoupon(Coupon coupon) when trying to
			// create a coupon that already exists in the database

			coupon2 = new Coupon("Dream vacation - 50% off!", date1, date2, 25, CouponType.TRAVELING,
					"Once in a life time experience", 125, "http://vacations.com");
			companyFacade.createCoupon(coupon2);
			System.out.println("The coupon you have created is: " + coupon2);
			// The message won't be printed in light of the expected Exception

		} catch (CouponSystemException e) {
			System.out.println(e.getMessage());
			if (e.getCause() != null)
				System.out.println(e.getCause());
		}

		System.out.println("==========");

		try {

			// Testing the method createCoupon(Coupon coupon) when trying to
			// create a coupon with an illegal start date

			coupon2 = new Coupon("Mazda going wild", date3, date2, 10, CouponType.CARS,
					"Get the new Mazda at only 999,999$", 120, "http://mazda.com");
			companyFacade.createCoupon(coupon2);
			System.out.println("The coupon you have created is: " + coupon2);
			// The message won't be printed in light of the expected Exception

		} catch (CouponSystemException e) {
			System.out.println(e.getMessage());
			if (e.getCause() != null)
				System.out.println(e.getCause());
		}

		System.out.println("==========");

		try {

			// Testing the method createCoupon(Coupon coupon) when trying to
			// create a coupon with an illegal end date

			coupon2 = new Coupon("Mazda going wild", date2, date1, 10, CouponType.CARS,
					"Get the new Mazda at only 999,999$", 120, "http://mazda.com");
			companyFacade.createCoupon(coupon2);
			System.out.println("The coupon you have created is: " + coupon2);
			// The message won't be printed in light of the expected Exception

		} catch (CouponSystemException e) {
			System.out.println(e.getMessage());
			if (e.getCause() != null)
				System.out.println(e.getCause());
		}

		System.out.println("==========");

		try {

			// Testing the method createCoupon(Coupon coupon) when trying to
			// create a coupon with an illegal amount value

			coupon2 = new Coupon("Mazda going wild", date1, date2, -2, CouponType.CARS,
					"Get the new Mazda at only 999,999$", 120, "http://mazda.com");
			companyFacade.createCoupon(coupon2);
			System.out.println("The coupon you have created is: " + coupon2);
			// The message won't be printed in light of the expected Exception

		} catch (CouponSystemException e) {
			System.out.println(e.getMessage());
			if (e.getCause() != null)
				System.out.println(e.getCause());
		}

		System.out.println("==========");

		try {

			// Testing the method createCoupon(Coupon coupon) when trying to
			// create a coupon with an illegal price value

			coupon2 = new Coupon("Mazda going wild", date1, date2, 10, CouponType.CARS,
					"Get the new Mazda at only 999,999$", 0, "http://mazda.com");
			companyFacade.createCoupon(coupon2);
			System.out.println("The coupon you have created is: " + coupon2);
			// The message won't be printed in light of the expected Exception

		} catch (CouponSystemException e) {
			System.out.println(e.getMessage());
			if (e.getCause() != null)
				System.out.println(e.getCause());
		}

		System.out.println("==========");

		try {

			// Testing the method readCoupon(long coupId) when trying to read a
			// coupon that doesn't exist in the database

			companyFacade.readCoupon(7777);
			System.out.println("The coupon you have requested is: " + coupon2);
			// The message won't be printed in light of the expected Exception

		} catch (CouponSystemException e) {
			System.out.println(e.getMessage());
			if (e.getCause() != null)
				System.out.println(e.getCause());
		}

		System.out.println("==========");

		try {

			// Testing the method readCoupon(long coupId) when trying to read a
			// coupon that doesn't belong to the company

			companyFacade.readCoupon(coupon1.getId());
			System.out.println("The coupon you have requested is: " + coupon1);
			// The message won't be printed in light of the expected Exception

		} catch (CouponSystemException e) {
			System.out.println(e.getMessage());
			if (e.getCause() != null)
				System.out.println(e.getCause());
		}

		System.out.println("==========");

		try {

			// Testing the method updateCoupon(Coupon coupon) when trying to
			// update a coupon that doesn't exist in the database

			coupon2.setPrice(50);
			companyFacade.updateCoupon(coupon2);
			System.out.println("The coupon has been updated to: " + coupon2);
			// The message won't be printed in light of the expected Exception

		} catch (CouponSystemException e) {
			System.out.println(e.getMessage());
			if (e.getCause() != null)
				System.out.println(e.getCause());
		}

		System.out.println("==========");

		try {

			// Testing the method updateCoupon(Coupon coupon) when trying to
			// update a coupon that doesn't belong to the company

			coupon1.setPrice(50);
			companyFacade.updateCoupon(coupon1);
			System.out.println("The coupon has been updated to: " + coupon1);
			// The message won't be printed in light of the expected Exception

		} catch (CouponSystemException e) {
			System.out.println(e.getMessage());
			if (e.getCause() != null)
				System.out.println(e.getCause());
		}

		System.out.println("==========");

		try {

			// Testing the method updateCoupon(Coupon coupon) when trying to
			// update a coupon with a negative value as its new amount (firstly
			// we will create a new coupon that can be easily updated, and later
			// deleted)

			coupon3 = new Coupon("Books for the sole", date1, date2, 26, CouponType.BOOKS,
					"All books in 'Booklings' at half-price", 37.5, "http://booksforsole.com");
			companyFacade.createCoupon(coupon3);
			coupon3.setPrice(-3);
			companyFacade.updateCoupon(coupon3);
			System.out.println("The coupon has been updated to: " + coupon3);
			// The message won't be printed in light of the expected Exception

		} catch (CouponSystemException e) {
			System.out.println(e.getMessage());
			if (e.getCause() != null)
				System.out.println(e.getCause());
		}

		System.out.println("==========");

		try {

			// Testing the method updateCoupon(Coupon coupon) when trying to
			// update a coupon with an end date the precedes the start date

			coupon3.setEndDate(date3);
			companyFacade.updateCoupon(coupon3);
			System.out.println("The coupon has been updated to: " + coupon3);
			// The message won't be printed in light of the expected Exception

		} catch (CouponSystemException e) {
			System.out.println(e.getMessage());
			if (e.getCause() != null)
				System.out.println(e.getCause());
		}

		System.out.println("==========");

		try {

			// Deleting the coupon created above so the test file could be
			// easily run again

			companyFacade.deleteCoupon(coupon3);

			// Testing the method deleteCoupon(Coupon coupon) when trying to
			// delete a coupon that doesn't exist in the database

			boolean deleteStatus = companyFacade.deleteCoupon(coupon2);
			System.out.println("Has the coupon been deleted successfully? " + deleteStatus);
			// The message won't be printed in light of the expected Exception

		} catch (CouponSystemException e) {
			System.out.println(e.getMessage());
			if (e.getCause() != null)
				System.out.println(e.getCause());
		}

		System.out.println("==========");

		try {

			// Testing the method deleteCoupon(Coupon coupon) when trying to
			// delete a coupon that doesn't belong to the company

			boolean deleteStatus = companyFacade.deleteCoupon(coupon1);
			System.out.println("Has the coupon been deleted successfully? " + deleteStatus);
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