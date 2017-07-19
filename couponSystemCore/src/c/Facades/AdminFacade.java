package c.Facades;

import java.util.Collection;

import b.DAOs.CompanyDAO;
import b.DAOs.CustomerDAO;
import b.DAOs.DAOException;
import b.DBDAOs.CompanyDBDAO;
import b.DBDAOs.CustomerDBDAO;
import b.JavaBeans.Company;
import b.JavaBeans.Customer;

/**
 * This class will serve to determine administrator permissions
 */

public class AdminFacade implements CouponClientFacade {

	// Attributes (the DAO instances themselves will be created via the CTOR)

	private CompanyDAO companyDAO;
	private CustomerDAO customerDAO;

	// CTOR (creates both the AdminFacade and the DAO instances)

	public AdminFacade() throws FacadeException {
		try {
			companyDAO = new CompanyDBDAO();
			customerDAO = new CustomerDBDAO();
		} catch (DAOException e) {
			throw new FacadeException("Problem in creating the administrator access display.", e);
		}
	}

	// CRUD Methods related to operations on companies in the database

	/**
	 * public long createCompany(Company company)
	 * 
	 * @param Company
	 *            checked according to company name to make sure it doesn't
	 *            already exist in the database, in addition to verifying that
	 *            all company fields of type String aren't null
	 * @throws FacadeException
	 * @return long, the new id number given for the company by the system
	 */

	public long createCompany(Company company) throws FacadeException {

		long id = 0;

		try {

			// Firstly, we need to make sure that the company fields of type
			// String don't include any null values

			if (company.getCompName() == null || company.getCompName().length() == 0 || company.getPassword() == null
					|| company.getPassword().length() == 0 || company.getEmail() == null
					|| company.getEmail().length() == 0) {
				throw new FacadeException(
						"The company was not created since the name/password/email of the company can not be null.");
			}

			// Secondly, we need to make sure that the company name doesn't
			// already exist in the database

			if (companyDAO.checkCompanyName(company.getCompName())) {
				throw new FacadeException("The company was not created since the company " + company.getCompName()
						+ " already exists under this name.");
			}

			// Thirdly, if the company name doesn't already exist, and all the
			// fields are properly defined, we need to create the new company

			id = companyDAO.createCompany(company);

		} catch (DAOException e) {
			throw new FacadeException("A problem arose in the attempt to create the company.", e);
		}
		return id;

	}

	/**
	 * public Company readCompany(long id)
	 * 
	 * @param Company
	 * @throws FacadeException
	 * @return Company, the company whose id was given as a parameter
	 */

	public Company readCompany(long id) throws FacadeException {

		Company company = null;

		try {
			company = companyDAO.readCompany(id);
		} catch (DAOException e) {
			throw new FacadeException("A problem arose in the attempt to read the company.", e);
		}
		return company;

	}

	/**
	 * public Collection<Company> readAllCompanies()
	 * 
	 * @throws FacadeException
	 * @return Collection<Company>, all the companies in the database
	 */

	public Collection<Company> readAllCompanies() throws FacadeException {

		Collection<Company> companies = null;

		try {
			companies = companyDAO.readAllCompanies();
		} catch (DAOException e) {
			throw new FacadeException("A problem arose in the attempt to read the companies.", e);
		}
		return companies;

	}

	/**
	 * public boolean updateCompany(Company company) updates only the password
	 * and email of the company
	 * 
	 * @param Company
	 * @throws FacadeException
	 * @return boolean, relating to the success of the updating process
	 */

	public boolean updateCompany(Company company) throws FacadeException {

		try {

			// Firstly, we need to read the current company from the database
			// (an Exception will be thrown if the company doesn't exist)

			Company currentCompany = companyDAO.readCompany(company.getId());

			// Secondly, we need to make sure that the company fields designated
			// for update (both of type String) don't include any null values

			if (company.getPassword() == null || company.getPassword().length() == 0 || company.getEmail() == null
					|| company.getEmail().length() == 0) {
				throw new FacadeException(
						"The company was not updated since the password/email of the company can not be null.");
			}

			// Thirdly, we need to update the fields of the password and email

			currentCompany.setPassword(company.getPassword());
			currentCompany.setEmail(company.getEmail());

			// Next, we need to update the altered company in the database

			companyDAO.updateCompany(currentCompany);

			// Lastly, we need to update the name of the company instance, given
			// as a parameter, in case a name-update was attempted as well

			company.setCompName(currentCompany.getCompName());

		} catch (DAOException e) {
			throw new FacadeException("A problem arose in the attempt to update the company.", e);
		}
		return true; // No Exceptions thrown

	}

	/**
	 * public boolean deleteCompany(Company company) deletes the coupons of the
	 * company from all of the database tables (Coupon, Company_Coupon and
	 * Customer_Coupon) and subsequently the company itself from table Company
	 * 
	 * @param Company
	 * @throws FacadeException
	 * @return boolean, relating to the success of the entire deleting process
	 */

	public boolean deleteCompany(Company company) throws FacadeException {

		try {
			companyDAO.deleteCompanyCoupons(company.getId());
			companyDAO.deleteCompany(company);
		} catch (DAOException e) {
			throw new FacadeException("A problem arose in the attempt to delete the company and/or its coupons.", e);
		}
		return true; // No Exceptions thrown

	}

	// CRUD Methods related to operations on customers in the database

	/**
	 * public long createCustomer(Customer customer)
	 * 
	 * @param Customer
	 *            checked according to customer name to make sure it doesn't
	 *            already exist in the database, , in addition to verifying that
	 *            all customer fields of type String aren't null
	 * @throws FacadeException
	 * @return long, the new id number given for the customer by the system
	 */

	public long createCustomer(Customer customer) throws FacadeException {

		long id = 0;

		try {

			// Firstly, we need to make sure that the customer fields of type
			// String don't include any null values

			if (customer.getCustName() == null || customer.getCustName().length() == 0 || customer.getPassword() == null
					|| customer.getPassword().length() == 0) {
				throw new FacadeException(
						"The customer was not created since the name/password of the customer can not be null.");
			}

			// Secondly, we need to make sure that the customer name doesn't
			// already exist in the database

			if (customerDAO.checkCustomerName(customer.getCustName())) {
				throw new FacadeException("The customer was not created since the customer " + customer.getCustName()
						+ " already exists under this name.");
			}

			// Thirdly, if the customer name doesn't already exist, and all the
			// fields are properly defined, we need to create the new customer

			id = customerDAO.createCustomer(customer);

		} catch (DAOException e) {
			throw new FacadeException("A problem arose in the attempt to create the customer.", e);
		}
		return id;

	}

	/**
	 * public Customer readCustomer(long id)
	 * 
	 * @param Customer
	 * @throws FacadeException
	 * @return Customer, the customer whose id was given as a parameter
	 */

	public Customer readCustomer(long id) throws FacadeException {

		Customer customer = null;

		try {
			customer = customerDAO.readCustomer(id);
		} catch (DAOException e) {
			throw new FacadeException("A problem arose in the attempt to read the customer.", e);
		}
		return customer;

	}

	/**
	 * public Collection<Customer> readAllCustomers()
	 * 
	 * @throws FacadeException
	 * @return Collection<Customer>, all the customers in the database
	 */

	public Collection<Customer> readAllCustomers() throws FacadeException {

		Collection<Customer> customers = null;

		try {
			customers = customerDAO.readAllCustomers();
		} catch (DAOException e) {
			throw new FacadeException("A problem arose in the attempt to read the customers.", e);
		}
		return customers;

	}

	/**
	 * public boolean updateCustomer(Customer customer) updates only the
	 * password of the customer
	 * 
	 * @param Customer
	 * @throws FacadeException
	 * @return boolean, relating to the success of the updating process
	 */

	public boolean updateCustomer(Customer customer) throws FacadeException {

		try {

			// Firstly, we need to read the current customer from the database
			// (an Exception will be thrown if the customer doesn't exist)

			Customer currentCustomer = customerDAO.readCustomer(customer.getId());

			// Secondly, we need to make sure that the customer field designated
			// for update (being of type String) doesn't contain a null value

			if (customer.getPassword() == null || customer.getPassword().length() == 0) {
				throw new FacadeException(
						"The customer was not updated since the password of the customer can not be null.");
			}

			// Thirdly, we need to update the field of the password

			currentCustomer.setPassword(customer.getPassword());

			// Next, we need to update the altered customer in the database

			customerDAO.updateCustomer(currentCustomer);

			// Lastly, we need to update the name of the customer instance,
			// given as a parameter, in case a name-update was attempted as well

			customer.setCustName(currentCustomer.getCustName());

		} catch (DAOException e) {
			throw new FacadeException("A problem arose in the attempt to update the customer.", e);
		}
		return true; // No Exceptions thrown

	}

	/**
	 * public boolean deleteCustomer(Customer customer) deletes the customer's
	 * history of coupon purchase from table Customer_Coupon, and subsequently
	 * the customer itself from table Customer
	 * 
	 * @param Customer
	 * @throws FacadeException
	 * @return boolean, relating to the success of the entire deleting process
	 */

	public boolean deleteCustomer(Customer customer) throws FacadeException {

		try {
			customerDAO.deleteCustomerCoupons(customer.getId());
			customerDAO.deleteCustomer(customer);
		} catch (DAOException e) {
			throw new FacadeException("A problem arose in the attempt to delete the customer and/or its coupons.", e);
		}
		return true; // No Exceptions thrown
	}

}