package demo.Model;

/**
 * Author: Zipei (Tina) Xiao
 * Last Modified: Sept. 21, 2019
 */

public class QuestionModel {

	// question title
	private String question;

	// Multiple options
	private String[] option;

	// user answer
	private String answer;

	/**
	 * Default constructor
	 */
	public QuestionModel() {
	}

	/**
	 * Add new questions to the pool
	 * @param question question title
	 * @param option Multiple options
	 * @param answer user answer
	 */
	public QuestionModel(String question, String[] option, String answer) {
		this.question = question;
		this.option = option;
		this.answer = answer;
	}

	/**
	 * Get the question title
	 * @return question title
	 */
	public String getQuestion() {
		return question;
	}

	/**
	 * Set the Question
	 * @param question question title
	 */
	public void setQuestion(String question) {
		this.question = question;
	}

	/**
	 * Get the multi-choices options
	 * @return multi-choices options
	 */
	public String[] getOption() {
		return option;
	}

	/**
	 * Set the multi-choices options
	 * @param option multi-choices options to be set
	 */
	public void setOption(String[] option) {
		this.option = option;
	}

	/**
	 * Get the answer of the question
	 * @return answer
	 */
	public String getAnswer() {
		return answer;
	}

	/**
	 * Set the user answer to the question
	 * @param answer user answer
	 */
	public void setAnswer(String answer) {
		this.answer = answer;
	}
}
