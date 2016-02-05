<%@include file="include.html"%>

<div class="page-header">
        <h1>Welcome Admin</h1>
        <h2>Pick an option</h2>
        ${status}
      </div>
      <div class="row">
        <div class="col-sm-4">
          <ul class="list-group">
            <li class="list-group-item"><a href='addAuthor'> Add Author</a></li>
            <li class="list-group-item"><a href="viewauthor"> View Authors</a></li>
            <li class="list-group-item"><a href="addBook"> Add Book</a></li>
            <li class="list-group-item"><a href="viewBook"> View Books</a></li>
            <li class="list-group-item"><a href="addBranch"> Add Branch</a></li>
            <li class="list-group-item"><a href="viewBranch"> View Branches</a></li>
            <li class="list-group-item"><a href="addgenre"> Add Genre</a></li>
            <li class="list-group-item"><a href="viewgenre"> View Genres</a></li>
          </ul>
        </div><!-- /.col-sm-4 -->
</div>

<div id="myModal1" class="modal fade" tabindex="-1" role="dialog"
	aria-labelledby="myLargeModalLabel">
	<div class="modal-dialog modal-lg">
		<div class="modal-content"></div>
	</div>
</div>