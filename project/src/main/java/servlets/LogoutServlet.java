package servlets;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import logging.MyLogger;

/**
 * Servlet implementation class LogoutServlet. This class handles the logout
 * request of the user, all the while logging each operation it involves.
 */
public class LogoutServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = MyLogger.getInstance().getLogger();

	/**
	 * protected void doGet(HttpServletRequest request, HttpServletResponse
	 * response), method handling HTTP GET request. The method deletes all of
	 * the client cookies, invalidates the session (i.e. removes the
	 * CouponClientFacade from the session), and thereafter redirects the user
	 * to the URL of the login page.
	 * 
	 * @param HttpServletRequest;
	 * @param HttpServletResponse.
	 * @throws ServletException;
	 * @throws IOException.
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		LOGGER.log(Level.INFO, "Entering the doGet method in LogoutServlet.");

		Cookie[] clientCookies = request.getCookies();

		if (clientCookies != null) {
			LOGGER.log(Level.INFO, "Client cookies have been identified via LogoutServlet.");
			for (int i = 0; i < clientCookies.length; i++) {
				LOGGER.log(Level.INFO,
						"Deleting the client cookie '" + clientCookies[i].getName() + "' via LogoutServlet.");
				Cookie cookie = clientCookies[i];
				clientCookies[i].setValue(null);
				clientCookies[i].setMaxAge(0);
				response.addCookie(cookie);
			}
		}

		LOGGER.log(Level.INFO, "Removing the couponClientFacade from the session via LogoutServlet.");
		request.getSession().invalidate();
		String url = response.encodeRedirectURL("http://localhost:8080/project/view/login/html/login.html");
		LOGGER.log(Level.INFO,
				"Exiting the doGet method in LogoutServlet, while redirecting the user back to the login page.");
		response.sendRedirect(url);

	}

}