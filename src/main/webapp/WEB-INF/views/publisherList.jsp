<%@page import="java.util.List"%>
<%@ page import="com.gcit.training.lms.service.AdministrativeService"%>
<%@ page import="com.gcit.training.lms.entity.Publisher"%>
<%@ page import="com.gcit.training.lms.dao.PublisherDAO"%>
<%@ page import="com.gcit.training.lms.dao.AbstractDAO"%>
<%

List<Publisher> P = new PublisherDAO().readAll(0, 10);
for (int i=0;i<P.size();i++){
	
	

%>
<select name="publID">

	<option value="${P.get(i).getPublisherId() }">${P.get(i).getPublisherName(); }</option>
<%} %>
</select>
