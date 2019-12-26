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
		<h3>Your "${sessionScope.YourAnswer}" response has been
			registered</h3>
	</div>
	<div align="left">
		<h3>Submit your answer to next question:</h3>
		<h3>${sessionScope.question.question}</h3>
	</div>
	<div align="left">
		<form action="submit" method="get">
			<c:forEach items="${sessionScope.question.option}" var="option">
				<label><input type="radio" name="answer" value="${option}"
					checked>${option}<br></label>
			</c:forEach>
			<div align="left">
				<input type="submit">
			</div>
		</form>
	</div>
</body>
</html>