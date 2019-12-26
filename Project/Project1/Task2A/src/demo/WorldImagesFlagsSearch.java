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
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class WorldImagesFlagsSearch {

	// The country List
	private List<CountryInfo> countryList = new ArrayList<CountryInfo>();

	/**
	 * Get the Country Information list
	 * @return Country Information list
	 */
	public List<CountryInfo> getCountryList() {
		return countryList;
	}

	/**
	 * Use the Jsoup to get all the information needed from the URL
	 * @param htmlUrl the website address that includes information we need
	 */
	public WorldImagesFlagsSearch(String htmlUrl) {
		//Before using Jsoup, open the website page to prohibit 404 error
		// The WorldImagesFlagsSearch Class, as the Model Part of MVC, addresses information from the Controller
		try {
			// Get the web content as document type
			Document document = Jsoup.connect(htmlUrl)
					.ignoreHttpErrors(true)// 404 not found fix
					.userAgent(
							"Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.117 Safari/537.36")
					.referrer("https://www.cia.gov/library/publications/resources/the-world-factbook/index.html")
					.get();

			// get the dialog box code which includes information we need
			Elements wfbs = document.getElementsByClass("wfb-modal-dialog");

			// the wfbs elements is stored as an array
			for (Element wfb : wfbs) {
				String flagUrl = "";

				// get the country name
				Elements countryName = wfb.getElementsByClass("region_name1 countryName");

				// get the introduction
				Elements photogallery_captiontext = wfb.getElementsByClass("photogallery_captiontext");
				Elements flags = wfb.select("a[href]");
				for (Element flag : flags) {

					// get the attributes of that flag
					flagUrl = flag.absUrl("href");
				}

				if (!flagUrl.isEmpty()) {
					int last = flagUrl.lastIndexOf("/");
					if (last != -1) {

						// get rid of the "/" character
						last++;
						// https://www.cia.gov/library/publications/resources/the-world-factbook/attachments/flags/AQ-flag.gif

						// AF-flag.gif, get the first 2 characters of the flag (Country ID)
						String countryId = flagUrl.substring(last, last + 2);

						// New CountryInfo object includes all the information of a country flag
						CountryInfo info = new CountryInfo(flagUrl, countryName.html(), photogallery_captiontext.html(),
								countryId);

						// Add the info to the list
						countryList.add(info);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
