<%@ page language="java" import="java.util.*" import="demo.*"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>WorldImagesFlags</title>
</head>
<body>
	<div align="center">
		<b>The flag of ${sessionScope.countryName}</b>
	</div>
	<div align="center">
		<img src="${sessionScope.flagUrl}" alt="" />
	</div>
	<div align="center">
		<b>About this flag: </b>${sessionScope.modalFlagDesc}</div>
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