<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	<form:form commandName="boardVO" method="post">
		<table border="1">
			<tr>
				<th><form:label path="id">ID</form:label></th>
				<td><form:input path="id" />
					<form:errors path="id" /></td>
			</tr>
			<tr>
				<th><form:label path="ip">IP</form:label></th>
				<td><form:input path="ip" />
					<form:errors path="ip" /></td>
			</tr>
			<tr>
				<th><form:label path="password">비밀번호</form:label></th>
				<td><form:input path="password" />
					<form:errors path="password" /></td>
			</tr>
		</table>
		<div>
			<input type="submit" value="등록"> <a
				href="<c:url value="/board/list" />">목록</a>
		</div>
	</form:form>
</body>
</html>