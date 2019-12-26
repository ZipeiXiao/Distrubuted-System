package demo.Controller;

/**
 * Author: Zipei (Tina) Xiao
 * Last Modified: Sept. 21, 2019
 */

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import demo.Model.QuestionModel;

/**
 * Servlet implementation class QuestionServlet
 */
@WebServlet(urlPatterns = { "/submit", "/getResults" })
public class QuestionServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public QuestionServlet() {
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

		// determine what type of device our user is
		String ua = request.getHeader("User-Agent");

		boolean mobile;
		// prepare the appropriate DOCTYPE for the view pages
		if (ua != null && ((ua.indexOf("Android") != -1) || (ua.indexOf("iPhone") != -1))) {
			mobile = true;
			/*
			 * This is the latest XHTML Mobile doctype. To see the difference it
			 * makes, comment it out so that a default desktop doctype is used
			 * and view on an Android or iPhone.
			 */
			request.setAttribute("doctype", "<!DOCTYPE html PUBLIC \"-//WAPFORUM//DTD XHTML Mobile 1.2//EN\" \"http://www.openmobilealliance.org/tech/DTD/xhtml-mobile12.dtd\">");
		} else {
			mobile = false;
			request.setAttribute("doctype", "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">");
		}

		HttpSession session = request.getSession();
		String servletPath = request.getServletPath();

		// GET method is used to submit questions
		if (servletPath.equals("/submit")) {
			List<QuestionModel> questions = (List<QuestionModel>) session.getAttribute("questions");

			// Get the current question index number
			int questionIndex = (int) session.getAttribute("questionIndex");

			// go to the next question
			questionIndex++;
			System.out.println(questions);
			System.out.println(questionIndex);

			// When the user makes a choice and hits “submit”,
			// their answer should be stored in your MVC model.
			session.setAttribute("questionIndex", questionIndex);

			String answer = request.getParameter("answer");
			String answerFirstChar = answer.substring(0, 1);
			System.out.println(answer);
			// save results
			Integer responseAnswerSave = (Integer) session.getAttribute(answerFirstChar);
			if (responseAnswerSave != null) {

				// save the answer
				session.setAttribute(answerFirstChar, responseAnswerSave + 1);
				System.out.println("0: "+responseAnswerSave + 1+answerFirstChar);
			} else {

				// save
				session.setAttribute(answerFirstChar, 1);
				System.out.println("1: " + responseAnswerSave + answerFirstChar);
			}

			session.setAttribute("YourAnswer", answerFirstChar);
			session.setAttribute("question", questions.get(questionIndex % questions.size()));

			// Redirect to the result page
			response.sendRedirect("next.jsp");
		} else if (servletPath.equals("/getResults")) {

			// answer List
			List<String> results = new ArrayList<String>();

			// the multi-choices options
			String[] answerFirstChars = { "A", "B", "C", "D" };

			for (String a : answerFirstChars) {
				Integer responseAnswerSave = (Integer) session.getAttribute(a);
				System.out.println("yyy: "+responseAnswerSave);
				if (responseAnswerSave != null) {
					results.add(String.format("%s: %d", a, responseAnswerSave.intValue()));

					// After showing "The results from the survey are as follows:"
					// the statistics result should be cleaned
					session.removeAttribute(a);
				}
			}
			if (results.size() > 0) {
				session.setAttribute("results", results);

				// Redirect to the result page
				response.sendRedirect("result.jsp");
			} else {

				// After showing "The results from the survey are as follows:"
				// the statistics result should be cleaned
				session.removeAttribute("results");

				// Redirect to the no-result page
				response.sendRedirect("noresult.jsp");
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
