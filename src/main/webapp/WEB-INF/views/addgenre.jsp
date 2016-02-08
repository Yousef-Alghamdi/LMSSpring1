<%@include file="include.html"%>
<form action="addgenre" method="post">
	${result}
	<h2>Enter Genre details below:</h2>

	Genre Name: <input type="text" name="genreName">
	<button type="submit" class="btn btn-sm btn-primary">Add Genre</button>
</form>
<script>
	$(document).ready(function() {
		$(function() {
			$('#modal_form').on('submit', function(e) {
				e.preventDefault();
				$.ajax({
					url : "addgenre", //this is the submit URL
					type : 'POST', //or POST
					data : $('#modal_form').serialize(),
					success : function(data) {
						if (data.trim() == "success") {
							alert("Genre Successfully added.");
						} else {
							alert("There was a problem adding genre.");

						}
						$("#myModal1").modal("toggle");
					}
				});
			});
		});

	});
</script>