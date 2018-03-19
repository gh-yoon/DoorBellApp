<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" 
  content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
  <table border="1">
    <tr>
      <th>NO</th>
      <th>ID</th>
      <th>IP</th>
    </tr>
    <c:forEach var="board" items="${boardList}" 
      varStatus="loop">
      <tr>
        <td>${board.seq}</td>
        <td><a href=
			"<c:url value="/board/read/${board.seq}" />">
          ${board.id}</a></td>
        <td>${board.ip}</td>
      </tr>
    </c:forEach>
  </table>
  <a href="<c:url value="/board/write" />">새 USER</a>
</body>
</html>
