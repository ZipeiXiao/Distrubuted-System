<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="demo.*" %>

<%
  String mongodb_uri = "mongodb+srv://tina:distributedSystem@cluster0-ggb1k.mongodb.net/test?retryWrites=true&w=majority";
  List<Record> photos = demo.Model.mongoDBCloudFetch(mongodb_uri);
  session.setAttribute("photos", photos);
  session.setAttribute("sum", photos.size());
  session.setAttribute("mostIP", photos.get(0).getMost_IP());
  session.setAttribute("mostDate", photos.get(0).getMost_Date());
%>

<html>
<head>
  <title>MarsPhotos</title>
</head>
<body>
<div align="center">
  <div align="left">
    <b>sum: ${sessionScope.sum}</b>
  </div>
  <div align="left">
    <b>mostIP: ${sessionScope.mostIP}</b>
  </div>
  <div align="left">
    <b>mostDate: ${sessionScope.mostDate}</b>
  </div>
  <table border="1">
    <tr>
      <td bgcolor="red">user</td>
      <td bgcolor="red">id</td>
      <td bgcolor="red">sol</td>
      <td bgcolor="red">earth_date</td>
      <td bgcolor="red">camera</td>
      <td bgcolor="red">rover</td>
      <td bgcolor="red">img_src</td>
      <td bgcolor="red">latency</td>
      <td bgcolor="red">local</td>
    </tr>
    <c:forEach items="${sessionScope.photos}" var="photo">
      <tr>
        <td>IP: ${photo.user_ip}<br>${photo.request_time}</td>
        <td>${photo.id}</td>
        <td>${photo.sol}</td>
        <td>${photo.earth_date}</td>
        <td>${photo.camera}</td>
        <td>${photo.rover}</td>
        <td>${photo.img_src}</td>
        <td>${photo.latency}</td>
        <td>${photo.local}</td>
      </tr>
    </c:forEach>
  </table>
  <div align="left">
    <b>sum: ${sessionScope.sum}</b>
  </div>
</div>
</body>
</html>
