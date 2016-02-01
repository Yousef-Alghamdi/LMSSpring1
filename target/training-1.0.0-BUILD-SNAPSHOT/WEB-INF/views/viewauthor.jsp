<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@include file="include.html"%>
<%@ page import="com.gcit.training.lms.service.AdministrativeService"%>
<%@ page import="com.gcit.training.lms.service.ConnectionUtil"%>
<%@ page import="com.gcit.training.lms.entity.Author"%>
<%@ page import="com.gcit.training.lms.dao.AuthorDAO"%>
<%@ page import="java.util.List"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
	AdministrativeService adminService = new AdministrativeService();
	List<Author> authors = null;
	if(request.getAttribute("authors")!=null){
		authors = (List<Author>)request.getAttribute("authors");
	}else{
		authors = adminService.getAllAuthors(0, 10,"");
	}
%>

<div class="page-header">
	<h1>List of Authors in LMS Application</h1>
	${result }
</div>
<form action="searchAuthor" method="post">
<div class="input-group">
  <span class="input-group-addon" id="basic-addon1">Search</span>
  <input type="text" class="form-control" placeholder="Username" aria-describedby="basic-addon1" name="searchString">
</div>
<button type="submit" class="btn btn-sm btn-primary">Search!</button>
</form>
<nav>
  <ul class="pagination">
    <li>
      <a href="#" aria-label="Previous">
        <span aria-hidden="true">&laquo;</span>
      </a>
    </li>
    <li><a href="pageAuthor?pageNo=1">1</a></li>
    <li><a href="pageAuthor?pageNo=2">2</a></li>
    <li><a href="pageAuthor?pageNo=3">3</a></li>
    <li><a href="pageAuthor?pageNo=4">4</a></li>
    <li><a href="pageAuthor?pageNo=5">5</a></li>
    <li>
      <a href="#" aria-label="Next">
        <span aria-hidden="true">&raquo;</span>
      </a>
    </li>
  </ul>
</nav>
<div class="row">
	<div class="col-md-6">
		<table class="table">
			<thead>
				<tr>
					<th>#</th>
					<th>Author Name</th>
					<th>Edit</th>
					<th>Delete</th>
				</tr>
			</thead>
			<tbody>
				<%
					for (Author author : authors) {
			
				%>
				<tr>
					<td><%=author.getAuthorId()%></td>
					<td><%=author.getAuthorName()%></td>
					<td><button type="button" class="btn btn-sm btn-primary">Edit Author</button>
					<td><button type="button" class="btn btn-sm btn-danger" onclick="javascript:location.href='deleteAuthor?authorId=<%=author.getAuthorId()%>';">Delete Author</button>
				</tr>
				<%
					}
				%>
			</tbody>
		</table>
	</div>
</div>