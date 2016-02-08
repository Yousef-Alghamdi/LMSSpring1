<%@include file="include.html"%>
	<form action="addAuthor" method="post">
	${result}
		<h2>Enter Author details below:</h2>

		Author Name: <input type="text" name="authorName"> 
		<button type="submit" class="btn btn-sm btn-primary">Add Author</button>
	</form>
<script>
	$(document).ready(function() {
		$(function() {
			$('#modal_form').on('submit', function(e) {
				e.preventDefault();
				$.ajax({
					url : "addAuthor", //this is the submit URL
					type : 'POST', //or POST
					data : $('#modal_form').serialize(),
					success : function(data) {
						if (data.trim() == "success") {
							alert("Author Successfully added.");
						} else {
							alert("There was a problem adding author.");

						}
						$("#myModal1").modal("toggle");
					}
				});
			});
		});

	});
</script>