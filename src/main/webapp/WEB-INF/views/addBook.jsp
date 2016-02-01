<%@include file="include.html"%>
	<form action="addBook" method="post">
	${result}
		<h2>Enter Book details below:</h2>

		Book Title: <input type="text" name="bookTitle"> 
		Publisher: <input type="text" name="publisher"> 
		<button type="submit" class="btn btn-sm btn-primary">Add Author</button>
	</form>
