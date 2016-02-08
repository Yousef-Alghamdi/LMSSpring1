<div class="modal-content">
	<form id="modal_form">
		<input type="hidden" name="bookId" value=${bookId } />
		<input type="hidden" name="cardNo" value=${cardNo } />
		<input type="hidden" name="branchId" value=${branchId } />
		<h2>Edit the loan details below:</h2>
		Date Out: <input type="text" name="dOut" value='${dOut}'/>
		Date In: <input type="text" name="dIn" value='${dIn}'/>
		Due Date: <input type="text" name="dDue" value='${dDue}'/>
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
					url : "editBookL", //this is the submit URL
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