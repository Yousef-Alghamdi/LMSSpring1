<%@include file="include.html"%>
	<form action="addBranch" method="post">
	${result}
		<h2>Enter Branch details below:</h2>

		Branch Name: <input type="text" name="BranchName"> 
		Branch Address: <input type="text" name="BranchAddress"> 
		<button type="submit" class="btn btn-sm btn-primary">Add Branch</button>
	</form>
<script>
	$(document).ready(function() {
		$(function() {
			$('#modal_form').on('submit', function(e) {
				e.preventDefault();
				$.ajax({
					url : "addBranch", //this is the submit URL
					type : 'POST', //or POST
					data : $('#modal_form').serialize(),
					success : function(data) {
						if (data.trim() == "success") {
							alert("Branch Successfully added.");
						} else {
							alert("There was a problem adding branch.");

						}
						$("#myModal1").modal("toggle");
					}
				});
			});
		});

	});
</script>