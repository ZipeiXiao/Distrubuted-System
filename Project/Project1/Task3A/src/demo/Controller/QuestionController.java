package demo.Controller;

import java.util.ArrayList;
import java.util.List;

import demo.Model.QuestionModel;

/**
 * Author: Zipei (Tina) Xiao
 * Last Modified: Sept. 21, 2019
 *
 * This program demonstrates a very simple web application
 * that implements a simple desktop and mobile “clicker” for
 * class. This app allows users to submit
 * answers to questions posed in class, and
 * provide a separate URL end point for
 * getting the results of the submitted
 * responses.
 */

public class QuestionController {
	// question List
	private List<QuestionModel> questionList = new ArrayList<QuestionModel>();

	// begin with the number zero question
	private int index = 0;

	/**
	* Default constructor
	*/
	public QuestionController() {
	}

	/**
	 * Get the Default Question
	 * @return the number zero question
	 */
	public QuestionModel getDefaultQuestion() {
		return questionList.get(0);
	}

	/**
	 * Get the Next Question
	 * @return the index th question
	 */
	public QuestionModel getNextQuestion() {
		index++;
		return questionList.get(index % questionList.size());
	}

	/**
	 * Initialize all questions
	 * @return a list of questions
	 */
	public List<QuestionModel> QuestionControllerInit() {
		QuestionModel questionModel1000 = new QuestionModel();

		// Set the first question
		questionModel1000.setQuestion("Q1000 - this is the question title");
		String[] option1000 = { "A. xxx", "B. yyy", "C. zzz", "D. zzz" };
		questionModel1000.setOption(option1000);

		// set the default choice as A
		questionModel1000.setAnswer("A");
		questionList.add(questionModel1000);

		// Set the second question
		QuestionModel questionModel2000 = new QuestionModel();
		questionModel2000.setQuestion("Q2000 - this is the question title");
		String[] option2000 = { "A. xxx", "B. yyy", "C. zzz", "D. zzz" };
		questionModel2000.setOption(option2000);

		// set the default choice as D
		questionModel2000.setAnswer("D");
		questionList.add(questionModel2000);

		return questionList;
	}
}
