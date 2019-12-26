/**
 * Author: Zipei (Tina) Xiao
 * Last Modified: Oct. 3rd, 2019
 *
 * This class helps the server to store
 * sum separately for different
 * users with unique IDs.
 * Different ID’s may be
 * presented and each will have its own sum.
 */

public class User {
	private int ID;

	// If the server receives an ID that
	// it has not seen before,
	// that ID will be associated
	// with the sum of 0.
	private int sum = 0;

	/**
	 * default constructor
	 */
	public User() {
	}

	/**
	 * Get the ID of the user
	 * @return ID
	 */
	public int getID() {
		return ID;
	}

	/**
	 * Set the ID of the user
	 * @param iD input ID
	 */
	public void setID(int iD) {
		ID = iD;
	}

	/**
	 * Get the sum for this user
	 * @return sum for current user
	 */
	public int getSum() {
		return sum;
	}


	/**
	 * Set the sum of the current user
	 * @param sum
	 */
	public void setSum(int sum) {
		this.sum = sum;
	}
}
