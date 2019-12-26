<%@ page language="java" import="java.util.*" import="demo.*"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%
	demo.WorldImagesFlagsSearch search = new demo.WorldImagesFlagsSearch(
			"https://www.cia.gov/library/publications/resources/the-world-factbook/docs/flagsoftheworld.html");
	session.setAttribute("countrys", search.getCountryList());
%>

<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>WorldImagesFlags</title>
</head>
<body>
	<div align="center">
		<form action="WorldImagesFlagsServlet">
			<div align="center">
				<b>Choose country: </b>
			</div>
			<div align="center">
				<select name=countryId>
					<c:forEach items="${sessionScope.countrys}" var="country">
						<option value="${country.countryId}">${country.countryName}</option>
					</c:forEach>
				</select>
			</div>
			<div align="center">
				<input type="submit">
			</div>
		</form>
	</div>
</body>
</html>