package servlets;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import c.Facades.CouponClientFacade;
import c.Facades.ClientType;
import e.CouponSystem.CouponSystem;
import e.CouponSystem.CouponSystemException;
import logging.MyLogger;

/**
 * Servlet implementation class LoginServlet. This class handles the login
 * attempted by the user, and operates according to the details provided by the
 * latter (or to existing client cookies), all the while logging each operation.
 */
public class LoginServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = MyLogger.getInstance().getLogger();

	/**
	 * protected void doPost(HttpServletRequest request, HttpServletResponse
	 * response), method handling HTTP POST request. The method checks for any
	 * client cookies. If found, the login is performed accordingly. Otherwise,
	 * the method checks the login details filled out by the user, and either
	 * sets the appropriate CouponClientFacade on the session, as well as plants
	 * cookies with the login details (in case of a proper login attempt),
	 * thereafter redirecting to the URL of the suitable user; or sends to an
	 * error page, indicating a bad request by the user (in case of an incorrect
	 * login attempt).
	 * 
	 * @param HttpServletRequest;
	 * @param HttpServletResponse.
	 * @throws ServletException;
	 * @throws IOException.
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		LOGGER.log(Level.INFO, "Entering the doPost method in LoginServlet.");

		String nameOrId = null;
		String password = null;
		String clientType = null;
		CouponClientFacade couponClientFacade = null;
		String url = null;

		Cookie[] clientCookies = request.getCookies();
		if (clientCookies != null) {
			
			LOGGER.log(Level.INFO, "Client cookies have been identified via LoginServlet.");
			for (int i = 0; i < clientCookies.length; i++) {
				if (clientCookies[i].getName().equals("nameOrId")) {
					nameOrId = clientCookies[i].getValue();
					LOGGER.log(Level.INFO, "Existing cookie: " + clientCookies[i].getName() + ", " + nameOrId);
				} else if (clientCookies[i].getName().equals("password")) {
					password = clientCookies[i].getValue();
					LOGGER.log(Level.INFO, "Existing cookie: " + clientCookies[i].getName() + ", " + password);
				} else if (clientCookies[i].getName().equals("clientType")) {
					clientType = clientCookies[i].getValue();
					LOGGER.log(Level.INFO, "Existing cookie: " + clientCookies[i].getName() + ", " + clientType);
				}
			}
	
		} if (nameOrId == null || password == null || clientType == null) {
			LOGGER.log(Level.INFO, "The login client cookies, identified via LoginServlet, are null.");
			nameOrId = request.getParameter("nameOrId");
			LOGGER.log(Level.INFO, "Setting 'nameOrId' according to the html form as: " + nameOrId);
			password = request.getParameter("password");
			LOGGER.log(Level.INFO, "Setting 'password' according to the html form as: " + password);
			clientType = request.getParameter("clientType");
			LOGGER.log(Level.INFO, "Setting 'clientType' according to the html form as: " + clientType);
		}

		try {
			switch (clientType) {
			case "ADMIN":
				LOGGER.log(Level.INFO, "Checking in LoginServlet if the ADMIN user entered the correct details.");
				couponClientFacade = CouponSystem.getInstance().login(nameOrId, password, ClientType.ADMIN);
				url = response.encodeRedirectURL("http://localhost:8080/project/view/admin/html/admin.html");
				break;
			case "COMPANY":
				LOGGER.log(Level.INFO, "Checking in LoginServlet if the COMPANY user entered the correct details.");
				couponClientFacade = CouponSystem.getInstance().login(Integer.parseInt(nameOrId), password,
						ClientType.COMPANY);
				url = response.encodeRedirectURL("http://localhost:8080/project/view/company/html/company.html");
				break;
			case "CUSTOMER":
				LOGGER.log(Level.INFO, "Checking in LoginServlet if the CUSTOMER user entered the correct details.");
				couponClientFacade = CouponSystem.getInstance().login(Integer.parseInt(nameOrId), password,
						ClientType.CUSTOMER);
				url = response.encodeRedirectURL("http://localhost:8080/project/view/customer/html/customer.html");
				break;
			}
		} catch (CouponSystemException e) {
			LOGGER.log(Level.SEVERE, e.toString() + "\nRedirecting the user to an error page.", e);
			url = response.encodeRedirectURL("http://localhost:8080/project/view/login/html/error400.html");
			response.sendRedirect(url);
		}

		if (couponClientFacade != null) {

			LOGGER.log(Level.INFO,
					"Setting the couponClientFacade on the session via LoginServlet due to successful login.");
			request.getSession().setAttribute("couponClientFacade", couponClientFacade);
			
			LOGGER.log(Level.INFO, "Planting a 'nameOrId' cookie via LoginServlet due to successful login.");
			Cookie cookie1 = new Cookie("nameOrId", nameOrId);
			cookie1.setMaxAge(604800);
			response.addCookie(cookie1);
			LOGGER.log(Level.INFO, "Planting a 'password' cookie via LoginServlet due to successful login.");
			Cookie cookie2 = new Cookie("password", password);
			cookie2.setMaxAge(604800);
			response.addCookie(cookie2);
			LOGGER.log(Level.INFO, "Planting a 'clientType' cookie via LoginServlet due to successful login.");
			Cookie cookie3 = new Cookie("clientType", clientType);
			cookie3.setMaxAge(604800);
			response.addCookie(cookie3);

			LOGGER.log(Level.INFO,
					"Exiting the doPost method in LoginServlet without Exceptions, while redirecting the user to the proper URL.");
			response.sendRedirect(url);

		}

	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

}