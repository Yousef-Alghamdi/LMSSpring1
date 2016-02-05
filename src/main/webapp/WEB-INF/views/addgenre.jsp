<%@include file="include.html"%>
<form action="addgenre" method="post">
	${result}
	<h2>Enter Genre details below:</h2>

	Branch Name: <input type="text" name="genreName">
	<button type="submit" class="btn btn-sm btn-primary">Add Genre</button>
</form>