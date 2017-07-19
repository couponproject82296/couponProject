package coupon.project;

import java.net.URI;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
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
import b.JavaBeans.Coupon;
import b.JavaBeans.CouponType;
import c.Facades.CompanyFacade;
import e.CouponSystem.CouponSystemException;
import logging.MyLogger;

/**
 * Root resource (exposed at "company" path). This class invokes the methods
 * related to the company permissions, after attaining the facade instance from
 * the session, all the while logging each operation. HttpServletRequest,
 * HttpServletResponse and UriInfo are gained from the Context for further use.
 */
@Path("company")
public class CompanyService {

	private static final Logger LOGGER = MyLogger.getInstance().getLogger();
	
	@Context
	private HttpServletRequest request;

	@Context
	private HttpServletResponse response;

	@Context
	private UriInfo uriInfo;

	//
	// private CompanyFacade getCompanyFacade(), method serving all other methods in
	// this class for obtaining the CompanyFacade from the session.
	//
	// @throws LoginException.
	// @return CompanyFacade, unless there is no such facade on the session.
	//
	private CompanyFacade getCompanyFacade() throws LoginException {
		LOGGER.log(Level.INFO, "Entering the getCompanyFacade method inCompanyService.");
		if (request.getSession().getAttribute("couponClientFacade") == null) {
			LOGGER.log(Level.INFO, "Checking for the Company Facade on the session in CompanyService.");
			throw new LoginException("You are attempting to connect without authorization!");
		}
		LOGGER.log(Level.INFO, "Exiting the getCompanyFacade method in CompanyService without Exceptions.");
		return (CompanyFacade) request.getSession().getAttribute("couponClientFacade");
	}

	/**
	 * public Response createCoupon(Coupon coupon), method handling HTTP POST
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
	public Response createCoupon(Coupon coupon) {
		LOGGER.log(Level.INFO,
				"Entering the createCoupon method in CompanyService.\nThe method parameter is the coupon: "
						+ coupon.toString());
		long coupId = 0;
		try {
			LOGGER.log(Level.INFO, "Summoning the Company Facade for invoking its createCoupon method.");
			coupId = this.getCompanyFacade().createCoupon(coupon);
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
		String newCouponId = String.valueOf(coupId);
		URI uri = uriInfo.getAbsolutePathBuilder().path(newCouponId).build();
		LOGGER.log(Level.INFO, "Exiting the createCoupon method in CompanyService without Exceptions.");
		return Response.status(201).location(uri).entity(coupId).build();
	}

	/**
	 * public Response readCompany(), method handling HTTP GET request, exposed
	 * at "company" path.
	 * 
	 * @return Response, consisting of the suitable status code, the appropriate
	 *         return object (i.e. entity), and - if no exception was caught - a
	 *         representative URI (i.e. location) as part of the header. The
	 *         entity herein is delivered in the format of a JSON.
	 */
	@GET
	@Path("company")
	@Produces(MediaType.APPLICATION_JSON)
	public Response readCompany() {
		LOGGER.log(Level.INFO, "Entering the readCompany method in CompanyService.");
		Company company = null;
		try {
			LOGGER.log(Level.INFO, "Summoning the Company Facade for invoking its readCompany method.");
			company = this.getCompanyFacade().readCompany();
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
		LOGGER.log(Level.INFO, "Exiting the readCompany method in CompanyService without Exceptions.");
		return Response.status(200).location(uri).entity(company).build();
	}

	/**
	 * public Response readCoupon(@PathParam("couponId") long id), method
	 * handling HTTP GET request, exposed at "coupon/{couponId}" path.
	 * 
	 * @param long,
	 *            delivered as a path parameter.
	 * @return Response, consisting of the suitable status code, the appropriate
	 *         return object (i.e. entity), and - if no exception was caught - a
	 *         representative URI (i.e. location) as part of the header. The
	 *         entity herein is delivered in the format of a JSON.
	 */
	@GET
	@Path("coupon/{couponId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response readCoupon(@PathParam("couponId") long id) {
		LOGGER.log(Level.INFO,
				"Entering the readCoupon method in CompnayService.\nThe method parameter is the coupon id: " + id);
		Coupon coupon = null;
		try {
			LOGGER.log(Level.INFO, "Summoning the Company Facade for invoking its readCoupon method.");
			coupon = this.getCompanyFacade().readCoupon(id);
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
		LOGGER.log(Level.INFO, "Exiting the readCoupon method in CompanyService without Exceptions.");
		return Response.status(200).location(uri).entity(coupon).build();
	}

	/**
	 * public Response readAllCoupons(), method handling HTTP GET request,
	 * exposed at "coupons" path.
	 * 
	 * @return Response, consisting of the suitable status code, the appropriate
	 *         return object (i.e. entity), and - if no exception was caught - a
	 *         representative URI (i.e. location) as part of the header. The
	 *         entity herein is delivered in the format of a JSON.
	 */
	@GET
	@Path("coupons")
	@Produces(MediaType.APPLICATION_JSON)
	public Response readAllCoupons() {
		LOGGER.log(Level.INFO, "Entering the readAllCoupons method in CompanyService.");
		Collection<Coupon> coupons = null;
		try {
			LOGGER.log(Level.INFO, "Summoning the Company Facade for invoking its readAllCoupons method.");
			coupons = this.getCompanyFacade().readAllCoupons();
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
		LOGGER.log(Level.INFO, "Exiting the readAllCoupons method in CompanyService without Exceptions.");
		return Response.status(200).location(uri).entity(genericCoupons).build();
	}

	/**
	 * public Response readCouponByType(@PathParam("type") String type), method
	 * handling HTTP GET request, exposed at "coupons/type/{type}" path.
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
	public Response readCouponByType(@PathParam("type") String type) {
		LOGGER.log(Level.INFO,
				"Entering the readCouponsByType method in CompanyService.\nThe method parameter is the coupon type: "
						+ type);
		Collection<Coupon> coupons = null;
		try {
			LOGGER.log(Level.INFO, "Summoning the Company Facade for invoking its readCouponsByType method.");
			coupons = this.getCompanyFacade().readCouponByType(CouponType.valueOf(type.toUpperCase()));
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
		LOGGER.log(Level.INFO, "Exiting the readCouponsByType method in CompanyService without Exceptions.");
		return Response.status(200).location(uri).entity(genericCoupons).build();
	}

	/**
	 * public Response readCouponByPrice(@PathParam("price") double price),
	 * method handling HTTP GET request, exposed at "coupons/price/{price}"
	 * path.
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
	public Response readCouponByPrice(@PathParam("price") double price) {
		LOGGER.log(Level.INFO,
				"Entering the readCouponsByPrice method in CompanyService.\nThe method parameter is the coupon price: "
						+ price);
		Collection<Coupon> coupons = null;
		try {
			LOGGER.log(Level.INFO, "Summoning the Company Facade for invoking its readCouponsByPrice method.");
			coupons = this.getCompanyFacade().readCouponByPrice(price);
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
		LOGGER.log(Level.INFO, "Exiting the readCouponsByPrice method in CompanyService without Exceptions.");
		return Response.status(200).location(uri).entity(genericCoupons).build();
	}

	/**
	 * public Response readCouponByDate(@PathParam("enddate") String endDate),
	 * method handling HTTP GET request, exposed at "coupons/date/{enddate}"
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
	@Path("coupons/date/{enddate}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response readCouponByDate(@PathParam("enddate") String endDate) {
		LOGGER.log(Level.INFO,
				"Entering the readCouponsByDate method in CompanyService.\nThe method parameter is the coupon's end date: "
						+ endDate);
		Collection<Coupon> coupons = null;
		try {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("ddMMyyyy");
			Date convertedEndDate = simpleDateFormat.parse(endDate);
			java.sql.Date sqlEndDate = new java.sql.Date(convertedEndDate.getTime());
			LOGGER.log(Level.INFO, "Summoning the Company Facade for invoking its readCouponsByDate method.");
			coupons = this.getCompanyFacade().readCouponByDate(sqlEndDate);
		} catch (ParseException | CouponSystemException e) {
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
		LOGGER.log(Level.INFO, "Exiting the readCouponsByDate method in CompanyService without Exceptions.");
		return Response.status(200).location(uri).entity(genericCoupons).build();
	}

	/**
	 * public Response updateCoupon(Coupon coupon, @PathParam("couponId") long
	 * id), method handling HTTP PUT request, exposed at "coupon/{couponId}"
	 * path.
	 * 
	 * @param Coupon,
	 *            delivered in the format of a JSON;
	 * @param long,
	 *            delivered as a path parameter.
	 * @return Response, consisting of the suitable status code, the appropriate
	 *         return object (i.e. entity), and - if no exception was caught - a
	 *         representative URI (i.e. location) as part of the header.
	 */
	@PUT
	@Path("coupon/{couponId}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateCoupon(Coupon coupon, @PathParam("couponId") long id) {
		LOGGER.log(Level.INFO,
				"Entering the updateCoupon method in CompanyService.\nThe method parameters are the coupon: "
						+ coupon.toString() + ", and the coupon id: " + id);
		boolean updateStatus = false;
		if (coupon.getId() == id) {
			try {
				LOGGER.log(Level.INFO, "Summoning the Company Facade for invoking its updateCoupon method.");
				updateStatus = this.getCompanyFacade().updateCoupon(coupon);
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
			LOGGER.log(Level.INFO, "Exiting the updateCoupon method in CompanyService without Exceptions.");
			return Response.status(200).location(uri).entity(updateStatus).build();
		}
		LOGGER.log(Level.WARNING, "Exiting the updateCoupon method in CompanyService without updating.");
		return Response.status(400).location(uri).entity(updateStatus).build();
	}

	/**
	 * public Response deleteCoupon(@PathParam("couponId") long id), method
	 * handling HTTP DELETE request, exposed at "coupon/{couponId}" path.
	 * 
	 * @param long,
	 *            delivered as a path parameter.
	 * @return Response, consisting of the suitable status code, the appropriate
	 *         return object (i.e. entity), and - if no exception was caught - a
	 *         representative URI (i.e. location) as part of the header.
	 */
	@DELETE
	@Path("coupon/{couponId}")
	public Response deleteCoupon(@PathParam("couponId") long id) {
		LOGGER.log(Level.INFO,
				"Entering the deleteCoupon method in CompanyService.\nThe method parameter is the coupon id: " + id);
		boolean deleteStatus = false;
		try {
			Coupon coupon = new Coupon();
			coupon.setId(id);
			LOGGER.log(Level.INFO, "Summoning the Company Facade for invoking its deleteCoupon method.");
			deleteStatus = this.getCompanyFacade().deleteCoupon(coupon);
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
			LOGGER.log(Level.INFO, "Exiting the deleteCoupon method in CompanyService without Exceptions.");
			return Response.status(200).location(uri).entity(deleteStatus).build();
		}
		LOGGER.log(Level.WARNING, "Exiting the deleteCoupon method in CompanyService without deleting.");
		return Response.status(404).location(uri).entity(deleteStatus).build();
	}

}