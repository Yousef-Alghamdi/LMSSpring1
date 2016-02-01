<div class="modal-content">
	<form id="modal_form">
		<input type="hidden" name="bookId" value=${bookId } />
		<h2>Edit Book details below:</h2>
		Book title: <input type="text" name="BookTitle" value='${BookTitle}'>
		Book publisher: <input type="text" name="publisher" value='${publisher}'>
		<button type="submit" class="btn btn-sm btn-primary">Apply
			Change</button>
	</form>
</div>

<script>
	$(document).ready(function() {
		$(function() {
			$('#modal_form').on('submit', function(e) {
				e.preventDefault();
				$.ajax({
					url : "editBook", //this is the submit URL
					type : 'POST', //or POST
					data : $('#modal_form').serialize(),
					success : function(data) {
						if (data.trim() == "success") {
							$("#SuccessEdit").show();
							alert("Book Successfully edited.");
							$('#SuccessEdit').delay(5000).fadeOut('slow');
						} else {
							$("#ErrorEdit").show();
							alert("There was a problem updating book.");
							$('#ErrorEdit').delay(5000).fadeOut('slow');
						}
						$("#myModal1").modal("toggle");
					}
				});
			});
		});

	});
</script>