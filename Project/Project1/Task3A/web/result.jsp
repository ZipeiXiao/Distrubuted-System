<%@ page language="java" import="java.util.*" import="demo.*"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%= request.getAttribute("doctype") %>
<html>
<head>
<meta charset="ISO-8859-1">
<title>DS Clicker</title>
</head>
<body>
	<div align="left">
		<h1>Distributed System Class Clicker</h1>
	</div>
	<div align="left">
		<h3>The results from the survey are as follows:</h3>
		<c:forEach items="${sessionScope.results}" var="result">
			<h3>${result}</h3>
		</c:forEach>
	</div>
	<div align="left">
		<h3>These results have now been cleared.</h3>
	</div>
</body>
</html>