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
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import b.JavaBeans.Coupon;
import b.JavaBeans.CouponType;
import b.JavaBeans.Customer;
import c.Facades.CustomerFacade;
import e.CouponSystem.CouponSystemException;
import logging.MyLogger;

/**
 * Root resource (exposed at "customer" path). This class invokes the methods
 * related to the customer permissions, after attaining the facade instance from
 * the session, all the while logging each operation. HttpServletRequest,
 * HttpServletResponse and UriInfo are gained from the Context for further use.
 */
@Path("customer")
public class CustomerService {

	private static final Logger LOGGER = MyLogger.getInstance().getLogger();

	@Context
	private HttpServletRequest request;

	@Context
	private HttpServletResponse response;

	@Context
	private UriInfo uriInfo;

	//
	// private CustomerFacade getCustomerFacade(), method serving all other methods in
	// this class for obtaining the CustomerFacade from the session.
	//
	// @throws LoginException.
	// @return CustomerFacade, unless there is no such facade on the session.
	//
	private CustomerFacade getCustomerFacade() throws LoginException {
		LOGGER.log(Level.INFO, "Entering the getCustomerFacade method in CustomerService.");
		if (request.getSession().getAttribute("couponClientFacade") == null) {
			LOGGER.log(Level.INFO, "Checking for the Customer Facade on the session in CustomerService.");
			throw new LoginException("You are attempting to connect without authorization!");
		}
		LOGGER.log(Level.INFO, "Exiting the getCustomerFacade method in CustomerService without Exceptions.");
		return (CustomerFacade) request.getSession().getAttribute("couponClientFacade");
	}

	/**
	 * public Response purchaseCoupon(Coupon coupon), method handling HTTP POST
	 * request, exposed at "coupon" path.
	 * 
	 * @param Coupon,
	 *            delivered in the format of a JSON.
	 * @return Response, consisting of the suitable status code, the appropriate
	 *         return object (i.e. entity), and - if no exception was caught - a
	 *         representative URI (i.e. location) as part of the header.
	 */
	@POST
	@Path("coupon")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response purchaseCoupon(Coupon coupon) {
		LOGGER.log(Level.INFO,
				"Entering the purchaseCoupon method in CustomerService.\nThe method parameter is the coupon: "
						+ coupon.toString());
		boolean purchaseStatus = false;
		try {
			LOGGER.log(Level.INFO, "Summoning the Customer Facade for invoking its purchaseCoupon method.");
			purchaseStatus = this.getCustomerFacade().purchaseCoupon(coupon);
		} catch (CouponSystemException e) {
			LOGGER.log(Level.SEVERE, e.toString(), e);
			return Response.status(403).entity(e.getMessage()).build();
		} catch (LoginException e) {
			LOGGER.log(Level.SEVERE, e.toString(), e);
			return Response.status(401).build();
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, e.toString(), e);
			return Response.status(500).entity(e.getMessage()).build();
		}
		if (purchaseStatus) {
			String purchasedCouponId = String.valueOf(coupon.getId());
			URI uri = uriInfo.getAbsolutePathBuilder().path(purchasedCouponId).build();
			LOGGER.log(Level.INFO, "Exiting the purchaseCoupon method in CustomerService without Exceptions.");
			return Response.status(201).location(uri).entity(purchaseStatus).build();
		}
		String finalPurchaseStatus = String.valueOf(purchaseStatus);
		URI uri = uriInfo.getAbsolutePathBuilder().path(finalPurchaseStatus).build();
		LOGGER.log(Level.WARNING, "Exiting the purchaseCoupon method in CustomerService without purchasing.");
		return Response.status(403).location(uri).entity(purchaseStatus).build();
	}

	/**
	 * public Response readCustomer(), method handling HTTP GET request, exposed
	 * at "customer" path.
	 * 
	 * @return Response, consisting of the suitable status code, the appropriate
	 *         return object (i.e. entity), and - if no exception was caught - a
	 *         representative URI (i.e. location) as part of the header. The
	 *         entity herein is delivered in the format of a JSON.
	 */
	@GET
	@Path("customer")
	@Produces(MediaType.APPLICATION_JSON)
	public Response readCustomer() {
		LOGGER.log(Level.INFO, "Entering the readCustomer method in CustomerService.");
		Customer customer = null;
		try {
			LOGGER.log(Level.INFO, "Summoning the Customer Facade for invoking its readCustomer method.");
			customer = this.getCustomerFacade().readCustomer();
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
		LOGGER.log(Level.INFO, "Exiting the readCustomer method in CustomerService without Exceptions.");
		return Response.status(200).location(uri).entity(customer).build();
	}

	/**
	 * public Response readAllPurchasedCoupons(), method handling HTTP GET
	 * request, exposed at "coupons" path.
	 * 
	 * @return Response, consisting of the suitable status code, the appropriate
	 *         return object (i.e. entity), and - if no exception was caught - a
	 *         representative URI (i.e. location) as part of the header. The
	 *         entity herein is delivered in the format of a JSON.
	 */
	@GET
	@Path("coupons")
	@Produces(MediaType.APPLICATION_JSON)
	public Response readAllPurchasedCoupons() {
		LOGGER.log(Level.INFO, "Entering the readAllPurchasedCoupons method in CustomerService.");
		Collection<Coupon> coupons = null;
		try {
			LOGGER.log(Level.INFO, "Summoning the Customer Facade for invoking its readAllPurchasedCoupons method.");
			coupons = this.getCustomerFacade().readAllPurchasedCoupons();
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
		GenericEntity<Collection<Coupon>> genericCoupons = new GenericEntity<Collection<Coupon>>(coupons) {
		};
		LOGGER.log(Level.INFO, "Exiting the readAllPurchasedCoupons method in CustomerService without Exceptions.");
		return Response.status(200).location(uri).entity(genericCoupons).build();
	}

	/**
	 * public Response readAllPurchasedCouponsByType(@PathParam("type") String
	 * type), method handling HTTP GET request, exposed at "coupons/type/{type}"
	 * path.
	 * 
	 * @param String,
	 *            delivered as a path parameter.
	 * @return Response, consisting of the suitable status code, the appropriate
	 *         return object (i.e. entity), and - if no exception was caught - a
	 *         representative URI (i.e. location) as part of the header. The
	 *         entity herein is delivered in the format of a JSON.
	 */
	@GET
	@Path("coupons/type/{type}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response readAllPurchasedCouponsByType(@PathParam("type") String type) {
		LOGGER.log(Level.INFO,
				"Entering the readAllPurchasedCouponsByType method in CustomerService.\nThe method parameter is the coupon type: "
						+ type);
		Collection<Coupon> coupons = null;
		try {
			LOGGER.log(Level.INFO,
					"Summoning the Customer Facade for invoking its readAllPurchasedCouponsByType method.");
			coupons = this.getCustomerFacade().readAllPurchasedCouponsByType(CouponType.valueOf(type.toUpperCase()));
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
		GenericEntity<Collection<Coupon>> genericCoupons = new GenericEntity<Collection<Coupon>>(coupons) {
		};
		LOGGER.log(Level.INFO,
				"Exiting the readAllPurchasedCouponsByType method in CustomerService without Exceptions.");
		return Response.status(200).location(uri).entity(genericCoupons).build();
	}

	/**
	 * public Response readAllPurchasedCouponsByPrice(@PathParam("price") double
	 * price), method handling HTTP GET request, exposed at
	 * "coupons/price/{price}" path.
	 * 
	 * @param double,
	 *            delivered as a path parameter.
	 * @return Response, consisting of the suitable status code, the appropriate
	 *         return object (i.e. entity), and - if no exception was caught - a
	 *         representative URI (i.e. location) as part of the header. The
	 *         entity herein is delivered in the format of a JSON.
	 */
	@GET
	@Path("coupons/price/{price}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response readAllPurchasedCouponsByPrice(@PathParam("price") double price) {
		LOGGER.log(Level.INFO,
				"Entering the readAllPurchasedCouponsByPrice method in CustomerService.\nThe method parameter is the coupon price: "
						+ price);
		Collection<Coupon> coupons = null;
		try {
			LOGGER.log(Level.INFO,
					"Summoning the Customer Facade for invoking its readAllPurchasedCouponsByPrice method.");
			coupons = this.getCustomerFacade().readAllPurchasedCouponsByPrice(price);
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
		GenericEntity<Collection<Coupon>> genericCoupons = new GenericEntity<Collection<Coupon>>(coupons) {
		};
		LOGGER.log(Level.INFO,
				"Exiting the readAllPurchasedCouponsByPrice method in CustomerService without Exceptions.");
		return Response.status(200).location(uri).entity(genericCoupons).build();
	}

	// The following method will serve the Purchase Coupon page:

	/**
	 * public Response readAllUnpurchasedCoupons(), method handling HTTP GET
	 * request, exposed at "unpurchasedCoupons" path.
	 * 
	 * @return Response, consisting of the suitable status code, the appropriate
	 *         return object (i.e. entity), and - if no exception was caught - a
	 *         representative URI (i.e. location) as part of the header. The
	 *         entity herein is delivered in the format of a JSON.
	 */
	@GET
	@Path("unpurchasedCoupons")
	@Produces(MediaType.APPLICATION_JSON)
	public Response readAllUnpurchasedCoupons() {
		LOGGER.log(Level.INFO, "Entering the readAllUnpurchasedCoupons method in CustomerService.");
		Collection<Coupon> coupons = null;
		try {
			LOGGER.log(Level.INFO, "Summoning the Customer Facade for invoking its readAllUnpurchasedCoupons method.");
			coupons = this.getCustomerFacade().readAllUnpurchasedCoupons();
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
		GenericEntity<Collection<Coupon>> genericCoupons = new GenericEntity<Collection<Coupon>>(coupons) {
		};
		LOGGER.log(Level.INFO, "Exiting the readAllUnpurchasedCoupons method in CustomerService without Exceptions.");
		return Response.status(200).location(uri).entity(genericCoupons).build();
	}

}