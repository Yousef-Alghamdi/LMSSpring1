<%@include file="include.html"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>

<div class="page-header">
        <h1>Welcome Borrower</h1>
        <h2>Pick an option</h2>
        <h2>${status}</h2>
      </div>
      <div class="row">
        <div class="col-sm-4">
          <ul class="list-group">
            <li class="list-group-item"><a href="viewBookB"> View Books by Name</a></li>
            <li class="list-group-item"><a href="viewgenreB"> View Books by Genre</a></li>
            <li class="list-group-item"><a href="viewReturnBook"> Return a Book</a></li>
          </ul>
        </div><!-- /.col-sm-4 -->
</div>