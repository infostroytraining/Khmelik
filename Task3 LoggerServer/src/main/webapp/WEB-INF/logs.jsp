<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>All available logs</title>
</head>
<body>

<table>
  <caption>Logs</caption>
  <thead>
    <tr>
      <th>DateTime</th>
      <th>Event</th>
      <th>Message</th>
    </tr>
  </thead>
  <tbody>
    <c:forEach items="${sessionScope.logs}" var="log">
      <tr style="color:${log.color}">
        <td>${log.date}</td>
        <td>${log.type}</td>
        <td>${log.message}</td>
        <td>${log.name}</td>
      </tr>
    </c:forEach>
  </tbody>
</table>

</body>
</html>
