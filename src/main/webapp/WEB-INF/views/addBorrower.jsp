<%@include file="include.html"%>
	<form action="addBorrower" method="post">
	${result}
		<h2>Enter Borrower details below:</h2>

		Borrower Name: <input type="text" name="borName"> 
		Borrower Address: <input type="text" name="borAddress"> 
		Borrower Phone: <input type="text" name="borPhone"> 
		<button type="submit" class="btn btn-sm btn-primary">Add Borrower</button>
	</form>
<script>
	$(document).ready(function() {
		$(function() {
			$('#modal_form').on('submit', function(e) {
				e.preventDefault();
				$.ajax({
					url : "addBorrower", //this is the submit URL
					type : 'POST', //or POST
					data : $('#modal_form').serialize(),
					success : function(data) {
						if (data.trim() == "success") {
							alert("Borrower Successfully added.");
						} else {
							alert("There was a problem adding borrower.");

						}
						$("#myModal1").modal("toggle");
					}
				});
			});
		});

	});
</script>