<%@include file="include.html"%>
<%@ page import="com.gcit.training.lms.entity.Publisher"%>
<%@ page import="java.util.List"%>
	<form action="addBook" method="post">
	${result}
		<h2>Enter Book details below:</h2>

		Book Title: <input type="text" name="bookTitle"> 
		Publisher: <select name='publisher' id="publisher" required>
				<%
				List<Publisher> publishers = (List<Publisher>) request.getAttribute("publishers");
					for(Publisher publisher:publishers)
					{
				%>
				<option value="<%=publisher.getPublisherId()%>"><%=publisher.getPublisherName() %></option>
				<%
					}
				%>
			</select></br>
		<button type="submit" class="btn btn-sm btn-primary">Add Book</button>
	</form>
<script>
	$(document).ready(function() {
		$(function() {
			$('#modal_form').on('submit', function(e) {
				e.preventDefault();
				$.ajax({
					url : "addBook", //this is the submit URL
					type : 'POST', //or POST
					data : $('#modal_form').serialize(),
					success : function(data) {
						if (data.trim() == "success") {
							alert("Book Successfully added.");
						} else {
							alert("There was a problem adding Book.");

						}
						$("#myModal1").modal("toggle");
					}
				});
			});
		});

	});
</script>