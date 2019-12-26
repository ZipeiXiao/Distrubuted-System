package cmu.edu.zipeix;

/**
 * Author: Zipei (Tina) Xiao
 * Last Modified: Sept. 21, 2019
 *
 * This simple program asks the user to enter a string of text data, and to make a
 * choice of two hash functions - MD5 or SHA-256. When the submit button is pressed, the
 * original text will be echoed back to the browser along with the name of the hash, and the
 * hash value. The hash values sent back to the browser should be displayed in two forms: as
 * hexadecimal text and as base 64 notation.
 */

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class ComputeHashes
 */
@WebServlet("/ComputeHashes")
public class ComputeHashes extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ComputeHashes() {
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
		// response.getWriter().append("Served at: ").append(request.getContextPath());

		HttpSession session = request.getSession();

		// String to be encrypted
		String text_data = request.getParameter("text_data");

		if (text_data != null && !text_data.isEmpty()) {

			// The original text will be echoed back to the browser
			String origin = request.getParameter("text_data");
			session.setAttribute("origin", origin);

			// The hash mode that user selected
			String hash_mode = request.getParameter("hash_mode");
			session.setAttribute("hash_mode", hash_mode);

			// The servlet will compute the requested cryptographic hash value (MD5 or SHA-256)
			// from the text transmitted by the browser
			if (hash_mode.equals("MD5")) {
				session.setAttribute("hexadecimal", HashCodeModel.getMD5Hex(text_data));
				session.setAttribute("base64", HashCodeModel.getMD5Str(text_data));
			} else if (hash_mode.equals("SHA-256")) {
				session.setAttribute("hexadecimal", HashCodeModel.getSHA256Hex(text_data));
				session.setAttribute("base64", HashCodeModel.getSHA256Str(text_data));
			}

			// Redirect to the page which shows result
			response.sendRedirect("result.jsp");
		} else {
			// Because the text data is empty
			response.sendRedirect("index.jsp");
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
