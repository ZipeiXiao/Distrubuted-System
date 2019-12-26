<%@ page language="java" import="java.util.*" import="demo.*"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%
	
%>

<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Compute Hashes</title>
</head>
<body>
	<div align="left">
		<h1>Compute Hashes</h1>
	</div>
	<div align="left">
		<form action="ComputeHashes" method="post">
			<div>
				String to be encrypted: <input type="text" name="text_data">
			</div>
			<div>
				encrypted mode: <label><input type="radio" name="hash_mode"
					value="MD5" checked>MD5</label> <label><input type="radio"
					name="hash_mode" value="SHA-256">SHA-256</label>
			</div>
			<div align="left">
				<input type="submit">
			</div>
		</form>
	</div>
</body>
</html>