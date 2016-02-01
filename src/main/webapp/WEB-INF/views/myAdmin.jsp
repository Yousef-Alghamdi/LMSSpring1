<%@include file="include.html"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>

<div class="page-header">
        <h1>Welcome Admin</h1>
        <h2>Pick an option</h2>
      </div>
      <div class="row">
        <div class="col-sm-4">
          <ul class="list-group">
            <li class="list-group-item"><a href="addAuthor"> Add Author</a></li>
            <li class="list-group-item"><a href="viewauthor"> View Authors</a></li>
            <li class="list-group-item"><a href="addbook"> Add Book</a></li>
            <li class="list-group-item"><a href="viewbook"> View Books</a></li>
            <li class="list-group-item"><a href="addbranch"> Add Branch</a></li>
            <li class="list-group-item"><a href="viewbranch"> View Branches</a></li>
            <li class="list-group-item"><a href="addgenre"> Add Genre</a></li>
            <li class="list-group-item"><a href="viewgenre"> View Genres</a></li>
            <li class="list-group-item"><a href="test">sql test</a></li>
          </ul>
        </div><!-- /.col-sm-4 -->
</div>