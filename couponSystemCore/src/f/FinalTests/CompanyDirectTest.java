package f.FinalTests;

import java.util.Calendar;
import java.util.Collection;
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
 * demonstrates the system abilities in cases where no Exceptions are thrown.
 */

public class CompanyDirectTest {

	public static void main(String[] args) {

		CouponSystem couponSystem = null;

		Calendar calendar = Calendar.getInstance();
		calendar.set(2017, Calendar.JUNE, 23, 0, 0, 0);
		Date date1 = calendar.getTime();
		calendar.set(2017, Calendar.JUNE, 25, 0, 0, 0);
		Date date2 = calendar.getTime();
		calendar.set(2017, Calendar.JUNE, 27, 0, 0, 0);
		Date date3 = calendar.getTime();

		try {

			// Initialization of the connection pool and the daily thread

			couponSystem = CouponSystem.getInstance();

			// Reading companies from the database for subsequent login (we
			// don't use the explicit id for the login since the data could be
			// deleted and added over and over, while the id changes each time)

			AdminFacade adminFacade = (AdminFacade) couponSystem.login("admin", "1234", ClientType.ADMIN);

			Company company = new Company();
			List<Company> companies = (List<Company>) adminFacade.readAllCompanies();
			if (companies.size() != 0) {
				company = companies.get(0);
			} else {
				System.out.println("There are no companies in the database.");
				System.out.println(
						"This program will shut-down. Please make sure that companies added by 'AddingToTablesForTests' haven't been deleted.");
				System.exit(1);
			}

			// Loading of DAOs and attainment of CompanyFacade via proper login

			CompanyFacade companyFacade = (CompanyFacade) couponSystem.login(company.getId(), "shoes5656",
					ClientType.COMPANY);

			// Testing the method createCoupon(Coupon coupon)

			Coupon coupon = new Coupon("Books for the sole", date1, date2, 26, CouponType.BOOKS,
					"All books in 'Booklings' at half-price", 37.5, "http://booksforsole.com");
			companyFacade.createCoupon(coupon);
			System.out.println("The coupon you have created is: " + coupon);

			System.out.println("==========");

			// Testing the method readCompany()

			Company companyRead = companyFacade.readCompany();
			System.out.println("The company you have requested is: " + companyRead);

			System.out.println("==========");

			// Testing the method readCoupon(long coupId)

			Coupon couponRead = companyFacade.readCoupon(coupon.getId());
			System.out.println("The coupon you have requested is: " + couponRead);

			System.out.println("==========");

			// Testing the method readAllCoupons()

			Collection<Coupon> coupons = companyFacade.readAllCoupons();
			if (coupons.size() != 0) {
				for (Coupon coup : coupons) {
					System.out.println("The following coupon belongs to the company: " + coup);
				}
			} else {
				System.out.println("There are no coupons belonging to the company.");
			}

			System.out.println("==========");

			// Testing the method readCouponByType(CouponType type)

			coupons = companyFacade.readCouponByType(CouponType.BOOKS);
			if (coupons.size() != 0) {
				for (Coupon coup : coupons) {
					System.out.println(
							"The following coupon of type " + CouponType.BOOKS + " belongs to the company: " + coup);
				}
			} else {
				System.out.println("There are no coupons of type " + CouponType.BOOKS + " that belong to the company.");
			}

			coupons = companyFacade.readCouponByType(CouponType.SPORTS);
			if (coupons.size() != 0) {
				for (Coupon coup : coupons) {
					System.out.println(
							"The following coupon of type " + CouponType.SPORTS + " belongs to the company: " + coup);
				}
			} else {
				System.out
						.println("There are no coupons of type " + CouponType.SPORTS + " that belong to the company.");
			}

			System.out.println("==========");

			// Testing the method readCouponByPrice(double price)

			double price = 100;
			coupons = companyFacade.readCouponByPrice(price);
			if (coupons.size() != 0) {
				for (Coupon coup : coupons) {
					System.out.println(
							"The following coupon, costing up to " + price + ", belongs to the company: " + coup);
				}
			} else {
				System.out.println("There are no coupons costing up to " + price + " that belong to the company.");
			}

			price = 25;
			coupons = companyFacade.readCouponByPrice(price);
			if (coupons.size() != 0) {
				for (Coupon coup : coupons) {
					System.out.println(
							"The following coupon, costing up to " + price + ", belongs to the company: " + coup);
				}
			} else {
				System.out.println("There are no coupons costing up to " + price + " that belong to the company.");
			}

			System.out.println("==========");

			// Testing the method readCouponByDate(endDate)

			java.sql.Date date2Sql = new java.sql.Date(date2.getTime());
			coupons = companyFacade.readCouponByDate(date2Sql);
			if (coupons.size() != 0) {
				coupons = companyFacade.readCouponByDate(date2Sql);
				for (Coupon coup : coupons) {
					System.out.println(
							"The following coupon, ending till " + date2Sql + ", belongs to the company: " + coup);
				}
			} else {
				System.out.println("There are no coupons, ending till " + date2Sql + ", that belong to the company.");
			}

			java.sql.Date date1Sql = new java.sql.Date(date1.getTime());
			coupons = companyFacade.readCouponByDate(date1Sql);
			if (coupons.size() != 0) {
				coupons = companyFacade.readCouponByDate(date1Sql);
				for (Coupon coup : coupons) {
					System.out.println(
							"The following coupon, ending till " + date1Sql + ", belongs to the company: " + coup);
				}
			} else {
				System.out.println("There are no coupons, ending till " + date1Sql + ", that belong to the company.");
			}

			System.out.println("==========");

			// Testing the method updateCoupon(Coupon coupon)

			coupon.setTitle("Books all day"); // Will not be altered!
			coupon.setEndDate(date3);
			coupon.setPrice(52);
			companyFacade.updateCoupon(coupon);
			// Both the coupon instance and database record are updated:
			System.out.println("The coupon instance has been updated to: " + coupon);
			coupon = companyFacade.readCoupon(coupon.getId());
			System.out.println("The coupon listing has been updated to: " + coupon);

			System.out.println("==========");

			// Testing the method deleteCoupon(Coupon coupon)

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