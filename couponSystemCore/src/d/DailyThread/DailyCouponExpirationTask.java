package d.DailyThread;

import java.util.Calendar;
import java.util.Date;

import b.DAOs.CouponDAO;
import b.DAOs.DAOException;
import b.DBDAOs.CouponDBDAO;
import e.CouponSystem.CouponSystemException;

/**
 * This daily thread will run every 24 hours (or when loading the coupon system)
 * in order to delete any expired coupons from the database. The task can be
 * stopped manually upon need.
 */

public class DailyCouponExpirationTask implements Runnable {

	// Attributes (the DAO instances themselves will be created via the CTOR)

	private CouponDAO couponDAO;
	private boolean quit;

	// CTOR (creates both the DailyCouponExpirationTask and the DAO instances)

	public DailyCouponExpirationTask() throws CouponSystemException {
		try {
			couponDAO = new CouponDBDAO();
		} catch (DAOException e) {
			throw new CouponSystemException("Problem in creating the DailyCouponExpirationTask.", e);
		}
	}

	/**
	 * public void run() runs the daily thread and delete any expired coupons
	 * from the database tables: Coupon, Company_Coupon and Customer_Coupon
	 */

	@Override
	public void run() {

		System.out.println(Thread.currentThread().getName() + " is now running.");

		try {

			// Firstly, we need to delete all expired coupons as the system
			// initially goes up (since the system might have been down for a
			// while): from tables Coupon, Company_Coupon and Customer_Coupon

			this.deleteExpiredCoupons();

			// Secondly, we can commence an infinite loop (continuing as long
			// as the system is still up), making the deletion of expired
			// coupons a daily task, which is performed each time at midnight

			quit = false;
			while (!quit) {

				Calendar calendar = Calendar.getInstance();
				calendar.setTime(new Date());
				calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + 1);
				calendar.set(Calendar.HOUR_OF_DAY, 0);
				calendar.set(Calendar.MINUTE, 0);
				calendar.set(Calendar.SECOND, 0);
				calendar.set(Calendar.MILLISECOND, 0);
				Date midnightTonight = calendar.getTime();
				Date currentDate = new Date(); // Date before midnight

				Thread.sleep(midnightTonight.getTime() - currentDate.getTime());

				this.deleteExpiredCoupons();

			}

		} catch (InterruptedException e) {
			System.out.println(Thread.currentThread().getName() + ": " + e.getMessage() + ".");
			if (e.getCause() != null)
				System.out.println(e.getCause());
		} catch (DAOException e) {
			System.out.println(Thread.currentThread().getName() + ": " + e.getMessage() + ".");
			if (e.getCause() != null)
				System.out.println(e.getCause());
		}
	}

	/**
	 * public void deleteExpiredCoupons()
	 * 
	 * @throws DAOException
	 */

	public void deleteExpiredCoupons() throws DAOException {

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) - 1);
		Date yesterday = calendar.getTime();
		couponDAO.deleteExpiredCoupons(yesterday);
		System.out.println(Thread.currentThread().getName() + ": all expired coupons up till " + yesterday
				+ " have been deleted.");

	}

	/**
	 * public void stopTask(Thread dailyThread) manually stops the daily thread
	 */

	public void stopTask(Thread dailyThread) {
		quit = true;
		dailyThread.interrupt();
	}

}