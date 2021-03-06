<%@include file="include.html"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>

<div class="page-header">
        <h1>Welcome Librarian</h1>
        <h2>Pick an option</h2>
        <h3>${status}</h3>
      </div>
      <div class="row">
        <div class="col-sm-4">
          <ul class="list-group">
            <li class="list-group-item"><a href="addgenre"> Add Genre</a></li>
            <li class="list-group-item"><a href="viewgenre"> View Genres</a></li>
            <li class="list-group-item"><a href="addBorrower"> Add Borrower</a></li>
            <li class="list-group-item"><a href="viewBorrower"> View Borrower</a></li>
            <li class="list-group-item"><a href="viewBookCopies"> View Books Copies</a></li>
            <li class="list-group-item"><a href="viewBookLoans"> View Loaned Books</a></li>
          </ul>
        </div>
</div>