package d.DailyThread;

import java.util.Calendar;
import java.util.Date;

import b.DAOs.CompCoupDAO;
import b.DAOs.CompanyDAO;
import b.DAOs.CouponDAO;
import b.DAOs.CustCoupDAO;
import b.DAOs.CustomerDAO;
import b.DBDAOs.CompCoupDBDAO;
import b.DBDAOs.CompanyDBDAO;
import b.DBDAOs.CouponDBDAO;
import b.DBDAOs.CustCoupDBDAO;
import b.DBDAOs.CustomerDBDAO;
import b.JavaBeans.Company;
import b.JavaBeans.Coupon;
import b.JavaBeans.CouponType;
import b.JavaBeans.Customer;
import e.CouponSystem.CouponSystemException;

/**
 * This test file puts the work of the daily thread (i.e. the deletion of
 * expired coupons from the database) to the test
 */

public class TestDailyCoupExpTask {

	public static void main(String[] args) {

		Calendar calendar = Calendar.getInstance();
		calendar.set(2017, Calendar.APRIL, 1, 0, 0, 0);
		Date date1 = calendar.getTime();
		calendar.set(2017, Calendar.APRIL, 8, 0, 0, 0);
		Date date2 = calendar.getTime();
		calendar.set(2017, Calendar.DECEMBER, 15, 0, 0, 0);
		Date date3 = calendar.getTime();

		try {

			CompanyDAO compDAO = new CompanyDBDAO();
			CustomerDAO custDAO = new CustomerDBDAO();
			CouponDAO coupDAO = new CouponDBDAO();
			CompCoupDAO compCoupDAO = new CompCoupDBDAO();
			CustCoupDAO custCoupDAO = new CustCoupDBDAO();

			// Adding companies, customers and coupons to the database, as these
			// will allow us to put the daily thread to the test

			Company company1 = new Company("Domino's Pizza", "1234d", "sale@dominos.com");
			long compId1 = compDAO.createCompany(company1);
			System.out.println("'Company1' has been added successfully to table Company. Its new id is: " + compId1);

			Company company2 = new Company("Hotels", "1234h", "sale@hotels.com");
			long compId2 = compDAO.createCompany(company2);
			System.out.println("'Company2' has been added successfully to table Company. Its new id is: " + compId2);

			System.out.println("==========");

			Customer customer1 = new Customer("Beni Kadosh", "1234c");
			long custId1 = custDAO.createCustomer(customer1);
			System.out.println("'Customer1' has been added successfully to table Company. Its new id is: " + custId1);

			System.out.println("==========");

			// Adding a coupon with an expired end date to the database (we use
			// the DAO instance rather than the proper Facade instance due to
			// the invalid dates involved, which will cause an exception to be
			// thrown via the Facade)

			Coupon coupon1 = new Coupon("30% off on Domino pizza", date1, date2, 50, CouponType.FOOD,
					"Best pizza with the best deal", 40, "http://dominos.com");
			long coupId1 = coupDAO.createCoupon(coupon1);
			System.out.println("'Coupon1' has been added successfully to table Coupon. Its new id is: " + coupId1);

			// Adding a coupon with a valid end date to the database (will serve
			// as a control subject)

			Coupon coupon2 = new Coupon("Dream vacation - 50% off! ", date1, date3, 25, CouponType.TRAVELING,
					"Once in a life time experience", 2000, "http://hotels.com");
			long coupId2 = coupDAO.createCoupon(coupon2);
			System.out.println("'Coupon2' has been added successfully to table Coupon. Its new id is: " + coupId2);

			System.out.println("==========");

			compCoupDAO.createCoupon(compId1, coupId1);
			System.out.println("'Coupon1' has been added to company1 in table Company_Coupon.");

			compCoupDAO.createCoupon(compId2, coupId2);
			System.out.println("'Coupon2' has been added to company2 in table Company_Coupon.");

			System.out.println("==========");

			custCoupDAO.createCoupon(custId1, coupId1);
			System.out.println("'Coupon1' has been added to customer1 in table Customer_Coupon.");

			custCoupDAO.createCoupon(custId1, coupId2);
			System.out.println("'Coupon2' has been added to customer1 in table Customer_Coupon.");

			System.out.println("==========");

			// Running the thread that will delete the expired coupons:

			DailyCouponExpirationTask runnable = new DailyCouponExpirationTask();
			Thread dailyThread = new Thread(runnable, "DailyThread");
			dailyThread.start();

			// We will allow the daily thread to run for 1 second (by putting
			// main to sleep), and will then stop the task via the appropriate
			// method

			Thread.sleep(1000);
			runnable.stopTask(dailyThread);

			System.out.println("==========");

			// Deleting companies, customers and coupons (that aren't expired)
			// so the database will be clear for now

			compDAO.deleteCompany(company1);
			compDAO.deleteCompany(company2);
			custDAO.deleteCustomer(customer1);
			coupDAO.deleteCoupon(coupon2);
			compCoupDAO.deleteCoupon(coupId2);
			custCoupDAO.deleteCoupon(coupId2);

		} catch (InterruptedException e) {
			System.out.println(e.getMessage());
			if (e.getCause() != null)
				System.out.println(e.getCause());
		} catch (CouponSystemException e) {
			System.out.println(e.getMessage());
			if (e.getCause() != null)
				System.out.println(e.getCause());
		}

		System.out.println("===== End of main =====");

	}

}