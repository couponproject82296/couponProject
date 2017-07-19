package coupon.project;

import java.net.URI;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.security.auth.login.LoginException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.DELETE;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import b.JavaBeans.Company;
import b.JavaBeans.Customer;
import c.Facades.AdminFacade;
import e.CouponSystem.CouponSystemException;
import logging.MyLogger;

/**
 * Root resource (exposed at "admin" path). This class invokes the methods
 * related to the administrator permissions, after attaining the facade instance
 * from the session, all the while logging each operation. HttpServletRequest,
 * HttpServletResponse and UriInfo are gained from the Context for further use.
 */
@Path("admin")
public class AdminService {

	private static final Logger LOGGER = MyLogger.getInstance().getLogger();

	@Context
	private HttpServletRequest request;

	@Context
	private HttpServletResponse response;

	@Context
	private UriInfo uriInfo;

	//
	// private AdminFacade getAdminFacade(), method serving all other methods in
	// this class for obtaining the AdminFacade from the session.
	//
	// @throws LoginException.
	// @return AdminFacade, unless there is no such facade on the session.
	//
	private AdminFacade getAdminFacade() throws LoginException {
		LOGGER.log(Level.INFO, "Entering the getAdminFacade method in AdminService.");
		if (request.getSession().getAttribute("couponClientFacade") == null) {
			LOGGER.log(Level.INFO, "Checking for the AdminFacade on the session in AdminService.");
			throw new LoginException("You are attempting to connect without authorization!");
		}
		LOGGER.log(Level.INFO, "Exiting the getAdminFacade method in AdminService without Exceptions.");
		return (AdminFacade) request.getSession().getAttribute("couponClientFacade");
	}

	/**
	 * public Response createCompany(Company company), method handling HTTP POST
	 * request, exposed at "company" path.
	 * 
	 * @param Company,
	 *            delivered in the format of a JSON.
	 * @return Response, consisting of the suitable status code, the appropriate
	 *         return object (i.e. entity), and - if no exception was caught - a
	 *         representative URI (i.e. location) as part of the header.
	 */
	@POST
	@Path("company")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response createCompany(Company company) {
		LOGGER.log(Level.INFO,
				"Entering the createCompany method in AdminService.\nThe method parameter is the company: "
						+ company.toString());
		long compId = 0;
		try {
			LOGGER.log(Level.INFO, "Summoning the AdminFacade for invoking its createCompany method.");
			compId = this.getAdminFacade().createCompany(company);
		} catch (CouponSystemException e) {
			LOGGER.log(Level.SEVERE, e.toString(), e);
			return Response.status(400).entity(e.getMessage()).build();
		} catch (LoginException e) {
			LOGGER.log(Level.SEVERE, e.toString(), e);
			return Response.status(401).build();
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, e.toString(), e);
			return Response.status(500).entity(e.getMessage()).build();
		}
		String newCompanyId = String.valueOf(compId);
		URI uri = uriInfo.getAbsolutePathBuilder().path(newCompanyId).build();
		LOGGER.log(Level.INFO, "Exiting the createCompany method in AdminService without Exceptions.");
		return Response.status(201).location(uri).entity(compId).build();
	}

	/**
	 * public Response readCompany(@PathParam("companyId") long id), method
	 * handling HTTP GET request, exposed at "company/{companyId}" path.
	 * 
	 * @param long,
	 *            delivered as a path parameter.
	 * @return Response, consisting of the suitable status code, the appropriate
	 *         return object (i.e. entity), and - if no exception was caught - a
	 *         representative URI (i.e. location) as part of the header. The
	 *         entity herein is delivered in the format of a JSON.
	 */
	@GET
	@Path("company/{companyId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response readCompany(@PathParam("companyId") long id) {
		LOGGER.log(Level.INFO,
				"Entering the readCompany method in AdminService.\nThe method parameter is the company id: " + id);
		Company company = null;
		try {
			LOGGER.log(Level.INFO, "Summoning the AdminFacade for invoking its readCompany method.");
			company = this.getAdminFacade().readCompany(id);
		} catch (CouponSystemException e) {
			LOGGER.log(Level.SEVERE, e.toString(), e);
			return Response.status(404).entity(e.getMessage()).build();
		} catch (LoginException e) {
			LOGGER.log(Level.SEVERE, e.toString(), e);
			return Response.status(401).build();
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, e.toString(), e);
			return Response.status(500).entity(e.getMessage()).build();
		}
		URI uri = uriInfo.getAbsolutePath();
		LOGGER.log(Level.INFO, "Exiting the readCompany method in AdminService without Exceptions.");
		return Response.status(200).location(uri).entity(company).build();
	}

	/**
	 * public Response readAllCompanies(), method handling HTTP GET request,
	 * exposed at "companies" path.
	 * 
	 * @return Response, consisting of the suitable status code, the appropriate
	 *         return object (i.e. entity), and - if no exception was caught - a
	 *         representative URI (i.e. location) as part of the header. The
	 *         entity herein is delivered in the format of a JSON.
	 */
	@GET
	@Path("companies")
	@Produces(MediaType.APPLICATION_JSON)
	public Response readAllCompanies() {
		LOGGER.log(Level.INFO, "Entering the readAllCompanies method in AdminService.");
		Collection<Company> companies = null;
		try {
			LOGGER.log(Level.INFO, "Summoning the AdminFacade for invoking its readAllCompanies method.");
			companies = this.getAdminFacade().readAllCompanies();
		} catch (CouponSystemException e) {
			LOGGER.log(Level.SEVERE, e.toString(), e);
			return Response.status(404).entity(e.getMessage()).build();
		} catch (LoginException e) {
			LOGGER.log(Level.SEVERE, e.toString(), e);
			return Response.status(401).build();
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, e.toString(), e);
			return Response.status(500).entity(e.getMessage()).build();
		}
		URI uri = uriInfo.getAbsolutePath();
		GenericEntity<Collection<Company>> genericCompanies = new GenericEntity<Collection<Company>>(companies) {
		};
		LOGGER.log(Level.INFO, "Exiting the readAllCompanies method in AdminService without Exceptions.");
		return Response.status(200).location(uri).entity(genericCompanies).type(MediaType.APPLICATION_JSON).build();
	}

	/**
	 * public Response updateCompany(Company company, @PathParam("companyId")
	 * long id), method handling HTTP PUT request, exposed at
	 * "company/{companyId}" path.
	 * 
	 * @param Company,
	 *            delivered in the format of a JSON;
	 * @param long,
	 *            delivered as a path parameter.
	 * @return Response, consisting of the suitable status code, the appropriate
	 *         return object (i.e. entity), and - if no exception was caught - a
	 *         representative URI (i.e. location) as part of the header.
	 */
	@PUT
	@Path("company/{companyId}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateCompany(Company company, @PathParam("companyId") long id) {
		LOGGER.log(Level.INFO,
				"Entering the updateCompany method in AdminService.\nThe method parameters are the company: "
						+ company.toString() + ", and the company id: " + id);
		boolean updateStatus = false;
		if (company.getId() == id) {
			try {
				LOGGER.log(Level.INFO, "Summoning the AdminFacade for invoking its updateCompany method.");
				updateStatus = this.getAdminFacade().updateCompany(company);
			} catch (CouponSystemException e) {
				LOGGER.log(Level.SEVERE, e.toString(), e);
				return Response.status(400).entity(e.getMessage()).build();
			} catch (LoginException e) {
				LOGGER.log(Level.SEVERE, e.toString(), e);
				return Response.status(401).build();
			} catch (Exception e) {
				LOGGER.log(Level.SEVERE, e.toString(), e);
				return Response.status(500).entity(e.getMessage()).build();
			}
		}
		String finalUpdateStatus = String.valueOf(updateStatus);
		URI uri = uriInfo.getAbsolutePathBuilder().path(finalUpdateStatus).build();
		if (updateStatus) {
			LOGGER.log(Level.INFO, "Exiting the updateCompany method in AdminService without Exceptions.");
			return Response.status(200).location(uri).entity(updateStatus).build();
		}
		LOGGER.log(Level.WARNING, "Exiting the updateCompany method in AdminService without updating.");
		return Response.status(400).location(uri).entity(updateStatus).build();
	}

	/**
	 * public Response deleteCompany(@PathParam("companyId") long id), method
	 * handling HTTP DELETE request, exposed at "company/{companyId}" path.
	 * 
	 * @param long,
	 *            delivered as a path parameter.
	 * @return Response, consisting of the suitable status code, the appropriate
	 *         return object (i.e. entity), and - if no exception was caught - a
	 *         representative URI (i.e. location) as part of the header.
	 */
	@DELETE
	@Path("company/{companyId}")
	public Response deleteCompany(@PathParam("companyId") long id) {
		LOGGER.log(Level.INFO,
				"Entering the deleteCompany method in AdminService.\nThe method paramete is the company id: " + id);
		boolean deleteStatus = false;
		try {
			Company company = new Company();
			company.setId(id);
			LOGGER.log(Level.INFO, "Summoning the AdminFacade for invoking its deleteCompany method.");
			deleteStatus = this.getAdminFacade().deleteCompany(company);
		} catch (CouponSystemException e) {
			LOGGER.log(Level.SEVERE, e.toString(), e);
			return Response.status(404).entity(e.getMessage()).build();
		} catch (LoginException e) {
			LOGGER.log(Level.SEVERE, e.toString(), e);
			return Response.status(401).build();
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, e.toString(), e);
			return Response.status(500).entity(e.getMessage()).build();
		}
		String finalDeleteStatus = String.valueOf(deleteStatus);
		URI uri = uriInfo.getAbsolutePathBuilder().path(finalDeleteStatus).build();
		if (deleteStatus) {
			LOGGER.log(Level.INFO, "Exiting the deleteCompany method in AdminService without Exceptions.");
			return Response.status(200).location(uri).entity(deleteStatus).build();
		}
		LOGGER.log(Level.WARNING, "Exiting the deleteCompany method in AdminService without deleting.");
		return Response.status(404).location(uri).entity(deleteStatus).build();
	}

	/**
	 * public Response createCustomer(Customer customer), method handling HTTP
	 * POST request, exposed at "customer" path.
	 * 
	 * @param Customer,
	 *            delivered in the format of a JSON.
	 * @return Response, consisting of the suitable status code, the appropriate
	 *         return object (i.e. entity), and - if no exception was caught - a
	 *         representative URI (i.e. location) as part of the header.
	 */
	@POST
	@Path("customer")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response createCustomer(Customer customer) {
		LOGGER.log(Level.INFO,
				"Entering the createCustomer method in AdminService.\nThe method parameter is the customer: "
						+ customer.toString());
		long custId = 0;
		try {
			LOGGER.log(Level.INFO, "Summoning the AdminFacade for invoking its createCustomer method.");
			custId = this.getAdminFacade().createCustomer(customer);
		} catch (CouponSystemException e) {
			LOGGER.log(Level.SEVERE, e.toString(), e);
			return Response.status(400).entity(e.getMessage()).build();
		} catch (LoginException e) {
			LOGGER.log(Level.SEVERE, e.toString(), e);
			return Response.status(401).build();
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, e.toString(), e);
			return Response.status(500).entity(e.getMessage()).build();
		}
		String newCustomerId = String.valueOf(custId);
		URI uri = uriInfo.getAbsolutePathBuilder().path(newCustomerId).build();
		LOGGER.log(Level.INFO, "Exiting the createCustomer method in AdminService without Exceptions.");
		return Response.status(201).location(uri).entity(custId).build();
	}

	/**
	 * public Response readCustomer(@PathParam("customerId") long id), method
	 * handling HTTP GET request, exposed at "customer/{customerId}" path.
	 * 
	 * @param long,
	 *            delivered as a path parameter.
	 * @return Response, consisting of the suitable status code, the appropriate
	 *         return object (i.e. entity), and - if no exception was caught - a
	 *         representative URI (i.e. location) as part of the header. The
	 *         entity herein is delivered in the format of a JSON.
	 */
	@GET
	@Path("customer/{customerId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response readCustomer(@PathParam("customerId") long id) {
		LOGGER.log(Level.INFO,
				"Entering the readCustomer method in AdminService.\nThe method parameter is the customer id: " + id);
		Customer customer = null;
		try {
			LOGGER.log(Level.INFO, "Summoning the AdminFacade for invoking its readCustomer method.");
			customer = this.getAdminFacade().readCustomer(id);
		} catch (CouponSystemException e) {
			LOGGER.log(Level.SEVERE, e.toString(), e);
			return Response.status(404).entity(e.getMessage()).build();
		} catch (LoginException e) {
			LOGGER.log(Level.SEVERE, e.toString(), e);
			return Response.status(401).build();
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, e.toString(), e);
			return Response.status(500).entity(e.getMessage()).build();
		}
		URI uri = uriInfo.getAbsolutePath();
		LOGGER.log(Level.INFO, "Exiting the readCustomer method in AdminService without Exceptions.");
		return Response.status(200).location(uri).entity(customer).build();
	}

	/**
	 * public Response readAllCustomers(), method handling HTTP GET request,
	 * exposed at "customers" path.
	 * 
	 * @return Response, consisting of the suitable status code, the appropriate
	 *         return object (i.e. entity), and - if no exception was caught - a
	 *         representative URI (i.e. location) as part of the header. The
	 *         entity herein is delivered in the format of a JSON.
	 */
	@GET
	@Path("customers")
	@Produces(MediaType.APPLICATION_JSON)
	public Response readAllCustomers() {
		LOGGER.log(Level.INFO, "Entering the readAllCustomers method in AdminService.");
		Collection<Customer> customers = null;
		try {
			LOGGER.log(Level.INFO, "Summoning the AdminFacade for invoking its readAllCustomers method.");
			customers = this.getAdminFacade().readAllCustomers();
		} catch (CouponSystemException e) {
			LOGGER.log(Level.SEVERE, e.toString(), e);
			return Response.status(404).entity(e.getMessage()).build();
		} catch (LoginException e) {
			LOGGER.log(Level.SEVERE, e.toString(), e);
			return Response.status(401).build();
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, e.toString(), e);
			return Response.status(500).entity(e.getMessage()).build();
		}
		URI uri = uriInfo.getAbsolutePath();
		GenericEntity<Collection<Customer>> genericCustomers = new GenericEntity<Collection<Customer>>(customers) {
		};
		LOGGER.log(Level.INFO, "Exiting the readAllCustomers method in AdminService without Exceptions.");
		return Response.status(200).location(uri).entity(genericCustomers).type(MediaType.APPLICATION_JSON).build();
	}

	/**
	 * public Response updateCustomer(Customer
	 * customer, @PathParam("customerId") long id), method handling HTTP PUT
	 * request, exposed at "customer/{customerId}" path.
	 * 
	 * @param Customer,
	 *            delivered in the format of a JSON;
	 * @param long,
	 *            delivered as a path parameter.
	 * @return Response, consisting of the suitable status code, the appropriate
	 *         return object (i.e. entity), and - if no exception was caught - a
	 *         representative URI (i.e. location) as part of the header.
	 */
	@PUT
	@Path("customer/{customerId}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateCustomer(Customer customer, @PathParam("customerId") long id) {
		LOGGER.log(Level.INFO,
				"Entering the updateCustomer method in AdminService.\nThe method parameters are the customer: "
						+ customer.toString() + ", and the customer id: " + id);
		boolean updateStatus = false;
		if (customer.getId() == id) {
			try {
				LOGGER.log(Level.INFO, "Summoning the AdminFacade for invoking its updateCustomer method.");
				updateStatus = this.getAdminFacade().updateCustomer(customer);
			} catch (CouponSystemException e) {
				LOGGER.log(Level.SEVERE, e.toString(), e);
				return Response.status(400).entity(e.getMessage()).build();
			} catch (LoginException e) {
				LOGGER.log(Level.SEVERE, e.toString(), e);
				return Response.status(401).build();
			} catch (Exception e) {
				LOGGER.log(Level.SEVERE, e.toString(), e);
				return Response.status(500).entity(e.getMessage()).build();
			}
		}
		String finalUpdateStatus = String.valueOf(updateStatus);
		URI uri = uriInfo.getAbsolutePathBuilder().path(finalUpdateStatus).build();
		if (updateStatus) {
			LOGGER.log(Level.INFO, "Exiting the updateCustomer method in AdminService without Exceptions.");
			return Response.status(200).location(uri).entity(updateStatus).build();
		}
		LOGGER.log(Level.WARNING, "Exiting the updateCustomer method in AdminService without updating.");
		return Response.status(400).location(uri).entity(updateStatus).build();
	}

	/**
	 * public Response deleteCustomer(@PathParam("customerId") long id), method
	 * handling HTTP DELETE request, exposed at "customer/{customerId}" path.
	 * 
	 * @param long,
	 *            delivered as a path parameter.
	 * @return Response, consisting of the suitable status code, the appropriate
	 *         return object (i.e. entity), and - if no exception was caught - a
	 *         representative URI (i.e. location) as part of the header.
	 */
	@DELETE
	@Path("customer/{customerId}")
	public Response deleteCustomer(@PathParam("customerId") long id) {
		LOGGER.log(Level.INFO,
				"Entering the deleteCustomer method in AdminService.\nThe method path parameter is the customer id: "
						+ id);
		boolean deleteStatus = false;
		try {
			Customer customer = new Customer();
			customer.setId(id);
			LOGGER.log(Level.INFO, "Summoning the AdminFacade for invoking its deleteCustomer method.");
			deleteStatus = this.getAdminFacade().deleteCustomer(customer);
		} catch (CouponSystemException e) {
			LOGGER.log(Level.SEVERE, e.toString(), e);
			return Response.status(404).entity(e.getMessage()).build();
		} catch (LoginException e) {
			LOGGER.log(Level.SEVERE, e.toString(), e);
			return Response.status(401).build();
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, e.toString(), e);
			return Response.status(500).entity(e.getMessage()).build();
		}
		String finalDeleteStatus = String.valueOf(deleteStatus);
		URI uri = uriInfo.getAbsolutePathBuilder().path(finalDeleteStatus).build();
		if (deleteStatus) {
			LOGGER.log(Level.INFO, "Exiting the deleteCustomer method in AdminService without Exceptions.");
			return Response.status(200).location(uri).entity(deleteStatus).build();
		}
		LOGGER.log(Level.WARNING, "Exiting the deleteCustomer method in AdminService without deleting.");
		return Response.status(404).location(uri).entity(deleteStatus).build();
	}

}