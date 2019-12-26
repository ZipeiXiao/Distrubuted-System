<%@ page language="java" import="java.util.*" import="cmu.edu.zipeix.*"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Compute Hashes</title>
</head>
<body>
	<div align="left">
		<b>original text:</b>
		<b>${sessionScope.origin}</b>
	</div>
	<div align="center">
		<b>${sessionScope.hash_mode} encrypted hexadecimal text:</b>
		<b>${sessionScope.hexadecimal}</b>
	</div>
	<div align="center">
		<b>${sessionScope.hash_mode} encrypted base64 text:</b>
		<b>${sessionScope.base64}</b>
	</div>
</body>
</html>