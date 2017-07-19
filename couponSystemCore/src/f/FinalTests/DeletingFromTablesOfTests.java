package f.FinalTests;

import java.util.Collection;

import b.JavaBeans.Company;
import b.JavaBeans.Customer;
import c.Facades.AdminFacade;
import c.Facades.FacadeException;

/**
 * This class could be used to delete companies, customers and coupons that were
 * created in the database tables in the counter-file AddingToTablesForTests.
 */

public class DeletingFromTablesOfTests {

	public static void main(String[] args) {

		try {

			AdminFacade adminFacade = new AdminFacade();

			Collection<Company> companies = adminFacade.readAllCompanies();
			for (Company company : companies) {
				adminFacade.deleteCompany(company);
			}

			Collection<Customer> customers = adminFacade.readAllCustomers();
			for (Customer customer : customers) {
				adminFacade.deleteCustomer(customer);
			}

		} catch (FacadeException e) {
			System.out.println(e.getMessage());
		}

		System.out.println("===== End of main =====");

	}

}