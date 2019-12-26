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

public class CountryInfo {
	private String flagUrl;
	private String countryName;
	private String modalFlagDesc;
	private String countryId;

	/**
	 * The Constructor of this class
	 * @param flagUrl the flag image url
	 * @param countryName the name of the country
	 * @param modalFlagDesc the flag description
	 * @param countryId the ID of the Country
	 */
	public CountryInfo(String flagUrl, String countryName, String modalFlagDesc, String countryId) {
		this.flagUrl = flagUrl;
		this.countryName = countryName;
		this.modalFlagDesc = modalFlagDesc;
		this.countryId = countryId;
	}

	/**
	 * Get the Country ID
	 * @return Country ID
	 */
	public String getCountryId() {
		return countryId;
	}

	/**
	 * Set the Country ID
	 * @param countryId the ID of the Country
	 */
	public void setCountryId(String countryId) {
		this.countryId = countryId;
	}

	/**
	 * Get the URL address of the flag image
	 * @return URL
	 */
	public String getFlagUrl() {
		return flagUrl;
	}

	/**
	 * Set the URL address of the flag image
	 * @param flagUrl URL address of the flag image
	 */
	public void setFlagUrl(String flagUrl) {
		this.flagUrl = flagUrl;
	}

	/**
	 * Get the Name of the Country
	 * @return Name of the Country
	 */
	public String getCountryName() {
		return countryName;
	}

	/**
	 * Set the Name of the Country
	 * @param countryName Name of the Country
	 */
	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	/**
	 * Get Descriptions of the Modal Flag
	 * @return Descriptions of the Modal Flag
	 */
	public String getModalFlagDesc() {
		return modalFlagDesc;
	}

	/**
	 * Set the Descriptions of the Modal Flag
	 * @param modalFlagDesc Descriptions of the Modal Flag
	 */
	public void setModalFlagDesc(String modalFlagDesc) {
		this.modalFlagDesc = modalFlagDesc;
	}
}
