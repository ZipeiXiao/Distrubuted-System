package demo;

/**
 * Author: Zipei (Tina) Xiao
 * Last Modified: Sept. 21, 2019
 *
 * This program builds a web application that searches for images of flags of countries around
 * the world from the “The World Factbook” web site at
 * https://www.cia.gov/library/publications/resources/the-worldfactbook/
 * docs/flagsoftheworld.html
 */

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class WorldImagesFlagsServlet
 */
@WebServlet("/WorldImagesFlagsServlet")
public class WorldImagesFlagsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public WorldImagesFlagsServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		 //response.getWriter().append("Served at: ").append(request.getContextPath());

		// the length of the Country ID is 2 characters
		String countryId = request.getParameter("countryId");

		if (countryId != null) {System.out.println(countryId);
			HttpSession session = request.getSession();
			List<CountryInfo> countries = (List<CountryInfo>) session.getAttribute("countrys");
			System.out.println(countries);
			if (countries != null) {
				for (CountryInfo info : countries) {

					// Matching the country ID
					if (info.getCountryId().equals(countryId)) {

						// set the attributes in the .jsp file, e.g., country name, flag, and introductions
						session.setAttribute("countryName", info.getCountryName());

						System.out.println(info.getCountryName());
						session.setAttribute("flagUrl", info.getFlagUrl());
						session.setAttribute("modalFlagDesc", info.getModalFlagDesc());

						// Redirect to the result page
						response.sendRedirect("result.jsp");
						return;
					}
				}
			}
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
