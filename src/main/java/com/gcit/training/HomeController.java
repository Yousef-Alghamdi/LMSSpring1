package com.gcit.training;

import java.sql.SQLException;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.WebRequest;

import com.gcit.training.lms.dao.BookDAO;
import com.gcit.training.lms.entity.Author;
import com.gcit.training.lms.entity.Book;
import com.gcit.training.lms.entity.BookCopies;
import com.gcit.training.lms.entity.BookLoans;
import com.gcit.training.lms.entity.Borrower;
import com.gcit.training.lms.entity.Genre;
import com.gcit.training.lms.entity.LibraryBranch;
import com.gcit.training.lms.entity.Publisher;
import com.gcit.training.lms.service.AdministrativeService;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {

	private static final Logger logger = LoggerFactory
			.getLogger(HomeController.class);

	@Autowired
	AdministrativeService adminService;
	@Autowired
	BookDAO bookDao;

	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Locale locale, Model model) {
		logger.info("Welcome home! The client locale is {}.", locale);

		Date date = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG,
				DateFormat.LONG, locale);

		String formattedDate = dateFormat.format(date);

		model.addAttribute("serverTime", formattedDate);

		return "home";
	}

	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/about", method = RequestMethod.GET)
	public String about(Locale locale, Model model) throws SQLException {
		return "about";
	}

	@RequestMapping(value = "/admin", method = RequestMethod.GET)
	public String admin() {
		return "admin";
	}

	@RequestMapping(value = "/librarian", method = RequestMethod.GET)
	public String librarian(Locale locale, Model model) throws SQLException {
		return "librarian";
	}

	@RequestMapping(value = "/borrower", method = RequestMethod.GET)
	public String borrower(Locale locale, Model model) throws SQLException {
		return "borrower";
	}

	/*
	 * Authors Mapping
	 */

	@RequestMapping(value = "/viewauthor", method = RequestMethod.GET)
	public String viewAuthor(Locale locale, Model model, WebRequest webRequest)
			throws SQLException {

		int count = adminService.getAllAuthorsCount();
		String s = adminService.pagination("/training/getAuthor", new String(),
				count, 10);

		model.addAttribute("pagination", s);

		return "viewauthor";

	}

	@RequestMapping(value = "/getAuthor", method = RequestMethod.GET)
	public String getAuthorData(Locale locale, Model model,
			WebRequest webRequest) throws SQLException {

		List<Author> authors;

		int pageNo = Integer.parseInt(webRequest.getParameter("pageNo"));
		int pageSize = Integer.parseInt(webRequest.getParameter("pageSize"));
		String searchString = webRequest.getParameter("searchString");

		authors = (List<Author>) adminService.getAllAuthors(pageNo, pageSize,
				searchString);

		StringBuffer sb = new StringBuffer(
				"<table class='table' id='authorsTable'><thead><tr><th>#</th><th>Author Name</th><th>Edit</th><th>Delete</th></tr></thead><tbody>");
		for (Author author : authors)
			sb.append("<tr><td>"
					+ author.getAuthorId()
					+ "</td><td>"
					+ author.getAuthorName()
					+ "</td><td align='center'><button type='button' class='btn btn btn-primary' data-toggle='modal' data-target='#myModal1' href='editAuthor?authorId="
					+ author.getAuthorId()
					+ "'>Edit Author</button></td><td><button type='button' class='btn btn-sm btn-danger' onclick=\"javascript:location.href='deleteAuthor?authorId="
					+ author.getAuthorId()
					+ "';\">Delete Author</button></td></tr>");

		sb.append("<table>");
		model.addAttribute("result", sb.toString());
		return "result";
	}

	@RequestMapping(value = "/searchAuthor", method = RequestMethod.GET)
	public String searchAuthor(Locale locale, Model model, WebRequest webRequest)
			throws SQLException {

		String searchString = webRequest.getParameter("searchString");
		int count = adminService.searchAuthorsCount(searchString);
		String s = adminService.pagination("/training/getAuthor", searchString,
				count, 10);

		model.addAttribute("pagination", s);
		model.addAttribute("searchResult", searchString);
		return "viewauthor";

	}

	@RequestMapping(value = "/editAuthor", method = RequestMethod.GET)
	public String editAuthor(Locale locale, Model model, WebRequest webRequest)
			throws SQLException {

		String authorId = webRequest.getParameter("authorId");
		if (authorId == null) {
			String error = "Please Provide Author Id";
			model.addAttribute("error", error);
			return error;
		}

		try {
			Author author = adminService.getAuthorById(Integer
					.parseInt(authorId));
			model.addAttribute("authorName", author.getAuthorName());
			model.addAttribute("authorId", authorId);
		} catch (Exception E) {
			String error = "Please Provide Author Id";
			model.addAttribute("error", error);
			return error;

		}
		return "editauthor";

	}

	@RequestMapping(value = "/editAuthor", method = RequestMethod.POST)
	public String editAuthorPost(Locale locale, Model model,
			WebRequest webRequest) throws SQLException {

		Integer authorId = Integer
				.parseInt(webRequest.getParameter("authorId"));
		String authorName = webRequest.getParameter("authorName");

		try {
			adminService.updateAuthor(authorId, authorName);
			model.addAttribute("status", "success");
		} catch (Exception E) {
			model.addAttribute("status", "Error");
		}
		return "status";

	}

	@RequestMapping(value = "/addAuthor", method = RequestMethod.GET)
	public String addAuthor(Locale locale, Model model, WebRequest webRequest)
			throws SQLException {
		return "addAuthor";
	}

	@RequestMapping(value = "/addAuthor", method = RequestMethod.POST)
	public String addAuthorPost(Locale locale, Model model,
			WebRequest webRequest) throws SQLException {
		String authorName = webRequest.getParameter("authorName");

		try {
			adminService.addAuthor(authorName);
			model.addAttribute("status", "Author was added successfully");
		} catch (Exception E) {
			model.addAttribute("status", "Error adding new author");
		}
		return "/admin";
	}

	@RequestMapping(value = "/deleteAuthor", method = RequestMethod.GET)
	public String DeleteAuthor(Locale locale, Model model, WebRequest webRequest)
			throws SQLException {
		int authorId = Integer.parseInt(webRequest.getParameter("authorId"));

		try {
			model.addAttribute("authorName",
					adminService.getAuthorById(authorId).getAuthorName());
			String status = adminService.deleteAuthor(authorId);
			model.addAttribute("status", status);

		} catch (Exception E) {
			model.addAttribute("status", "Error Delete Author");
		}
		return "deleteauthor";
	}

	/*
	 * Books Mapping
	 */
	@RequestMapping(value = "/viewBook", method = RequestMethod.GET)
	public String viewBook(Locale locale, Model model, WebRequest webRequest)
			throws SQLException {

		int count = adminService.getAllBooksCount();
		String s = adminService.pagination("/training/getBook", new String(),
				count, 10);

		model.addAttribute("pagination", s);

		return "viewBook";

	}

	@RequestMapping(value = "/getBook", method = RequestMethod.GET)
	public String getBookData(Locale locale, Model model, WebRequest webRequest)
			throws SQLException {

		List<Book> books;

		int pageNo = Integer.parseInt(webRequest.getParameter("pageNo"));
		int pageSize = Integer.parseInt(webRequest.getParameter("pageSize"));
		String searchString = webRequest.getParameter("searchString");

		books = (List<Book>) adminService.getAllBooks(pageNo, pageSize,
				searchString);

		StringBuffer sb = new StringBuffer(
				"<table class='table' id='booksTable'><thead><tr><th>#</th><th>Book Title</th><th>Book Publisher</th><th>Edit</th><th>Delete</th></tr></thead><tbody>");
		for (Book book : books)
			sb.append("<tr><td>"
					+ book.getBookId()
					+ "</td><td>"
					+ book.getTitle()
					+ "</td><td>"
					+ book.getPublisher().getPublisherName()
					+ "</td><td align='center'><button type='button' class='btn btn btn-primary' data-toggle='modal' data-target='#myModal1' href='editBook?bookId="
					+ book.getBookId()
					+ "'>Edit Book</button></td><td><button type='button' class='btn btn-sm btn-danger' onclick=\"javascript:location.href='deleteBook?bookId="
					+ book.getBookId() + "';\">Delete Book</button></td></tr>");

		sb.append("<table>");
		model.addAttribute("result", sb.toString());
		return "result";
	}

	@RequestMapping(value = "/searchBook", method = RequestMethod.GET)
	public String searchBook(Locale locale, Model model, WebRequest webRequest)
			throws SQLException {

		String searchString = webRequest.getParameter("searchString");
		int count = adminService.searchBooksCount(searchString);
		String s = adminService.pagination("/training/getBook", searchString,
				count, 10);

		model.addAttribute("pagination", s);
		model.addAttribute("searchResult", searchString);
		return "viewBook";

	}

	@RequestMapping(value = "/editBook", method = RequestMethod.GET)
	public String editBook(Locale locale, Model model, WebRequest webRequest)
			throws SQLException {

		String bookId = webRequest.getParameter("bookId");
		if (bookId == null) {
			String error = "Please Provide Book Id";
			model.addAttribute("error", error);
			return error;
		}

		try {
			Book book = adminService.getBookById(Integer.parseInt(bookId));
			model.addAttribute("BookTitle", book.getTitle());
			model.addAttribute("publisher", book.getPublisher()
					.getPublisherId());
			model.addAttribute("bookId", bookId);
		} catch (Exception E) {
			String error = "Please Provide Book Id";
			model.addAttribute("error", error);
			return error;

		}
		return "editBook";

	}

	@RequestMapping(value = "/editBook", method = RequestMethod.POST)
	public String editBookPost(Locale locale, Model model, WebRequest webRequest)
			throws SQLException {

		Integer bookId = Integer.parseInt(webRequest.getParameter("bookId"));
		String bookTitle = webRequest.getParameter("BookTitle");

		try {
			adminService.updateBook(bookId, bookTitle);
			model.addAttribute("status", "success");
		} catch (Exception E) {
			model.addAttribute("status", "Error");
		}
		return "status";

	}

	@RequestMapping(value = "/addBook", method = RequestMethod.GET)
	public String addBook(Locale locale, Model model, WebRequest webRequest)
			throws SQLException {
		model.addAttribute("publishers", adminService.getAllPublishers());
		return "addBook";
	}

	@RequestMapping(value = "/addBook", method = RequestMethod.POST)
	public String addBookPost(Locale locale, Model model, WebRequest webRequest)
			throws SQLException {
		String bookTitle = webRequest.getParameter("bookTitle");
		int publisherId = Integer
				.parseInt(webRequest.getParameter("publisher"));
		Publisher p = new Publisher();
		p.setPublisherId(publisherId);

		try {
			adminService.addBook(bookTitle, p);
			model.addAttribute("status", "Book was added successfully");
		} catch (Exception E) {
			model.addAttribute("status", "Error adding new book");
		}

		return "/admin";
	}

	@RequestMapping(value = "/deleteBook", method = RequestMethod.GET)
	public String DeleteBook(Locale locale, Model model, WebRequest webRequest)
			throws SQLException {
		int bookId = Integer.parseInt(webRequest.getParameter("bookId"));

		try {
			model.addAttribute("bookTitle", adminService.getBookById(bookId)
					.getTitle());
			String status = adminService.deleteBook(bookId);
			model.addAttribute("status", status);

		} catch (Exception E) {
			model.addAttribute("status", "Error Deleting Book");
		}
		return "deleteBook";
	}

	/*
	 * Branch Mapping
	 */
	@RequestMapping(value = "/viewBranch", method = RequestMethod.GET)
	public String viewBranch(Locale locale, Model model, WebRequest webRequest)
			throws SQLException {

		int count = adminService.getAllBranchesCount();
		String s = adminService.pagination("/training/getBranch", new String(),
				count, 10);

		model.addAttribute("pagination", s);

		return "viewBranch";

	}

	@RequestMapping(value = "/getBranch", method = RequestMethod.GET)
	public String getBranchData(Locale locale, Model model,
			WebRequest webRequest) throws SQLException {

		List<LibraryBranch> lb;

		int pageNo = Integer.parseInt(webRequest.getParameter("pageNo"));
		int pageSize = Integer.parseInt(webRequest.getParameter("pageSize"));
		String searchString = webRequest.getParameter("searchString");

		lb = (List<LibraryBranch>) adminService.getAllBranches(pageNo,
				pageSize, searchString);

		StringBuffer sb = new StringBuffer(
				"<table class='table' id='booksTable'><thead><tr><th>#</th><th>Branch Name</th><th>Branch Address</th><th>Edit</th><th>Delete</th></tr></thead><tbody>");
		for (LibraryBranch lbs : lb)
			sb.append("<tr><td>"
					+ lbs.getBranchId()
					+ "</td><td>"
					+ lbs.getBranchName()
					+ "</td><td>"
					+ lbs.getBranchAddress()
					+ "</td><td align='center'><button type='button' class='btn btn btn-primary' data-toggle='modal' data-target='#myModal1' href='editBranch?branchId="
					+ lbs.getBranchId()
					+ "'>Edit Branch</button></td><td><button type='button' class='btn btn-sm btn-danger' onclick=\"javascript:location.href='deleteBranch?branchId="
					+ lbs.getBranchId()
					+ "';\">Delete Branch</button></td></tr>");

		sb.append("<table>");
		model.addAttribute("result", sb.toString());
		return "result";
	}

	@RequestMapping(value = "/searchBranch", method = RequestMethod.GET)
	public String searchBranch(Locale locale, Model model, WebRequest webRequest)
			throws SQLException {

		String searchString = webRequest.getParameter("searchString");
		int count = adminService.searchBranchesCount(searchString);
		String s = adminService.pagination("/training/getBranch", searchString,
				count, 10);

		model.addAttribute("pagination", s);
		model.addAttribute("searchResult", searchString);
		return "viewBranch";

	}

	@RequestMapping(value = "/editBranch", method = RequestMethod.GET)
	public String editBranch(Locale locale, Model model, WebRequest webRequest)
			throws SQLException {

		String branchId = webRequest.getParameter("branchId");
		if (branchId == null) {
			String error = "Please Provide Branch Id1";
			model.addAttribute("error", error);
			return error;
		}

		try {
			LibraryBranch lb = adminService.getBranchById(Integer
					.parseInt(branchId));
			model.addAttribute("BranchName", lb.getBranchName());
			model.addAttribute("BranchAddress", lb.getBranchAddress());
			model.addAttribute("branchId", branchId);
		} catch (Exception E) {
			String error = "Please Provide Branch Id2";
			model.addAttribute("error", error);
			return error;

		}
		return "editBranch";

	}

	@RequestMapping(value = "/editBranch", method = RequestMethod.POST)
	public String editBranchPost(Locale locale, Model model,
			WebRequest webRequest) throws SQLException {

		Integer branchId = Integer
				.parseInt(webRequest.getParameter("branchId"));
		String BranchName = webRequest.getParameter("BranchName");
		String BranchAddress = webRequest.getParameter("BranchAddress");

		try {
			adminService.updateBranch(branchId, BranchName, BranchAddress);
			model.addAttribute("status", "success");
		} catch (Exception E) {
			model.addAttribute("status", "Error");
		}
		return "status";

	}

	@RequestMapping(value = "/addBranch", method = RequestMethod.GET)
	public String addBranch(Locale locale, Model model, WebRequest webRequest)
			throws SQLException {
		return "addBranch";
	}

	@RequestMapping(value = "/addBranch", method = RequestMethod.POST)
	public String addBranchPost(Locale locale, Model model,
			WebRequest webRequest) throws SQLException {
		String BranchName = webRequest.getParameter("BranchName");
		String BranchAddress = webRequest.getParameter("BranchAddress");

		try {
			adminService.addBranch(BranchName, BranchAddress);
			model.addAttribute("status", "Branch was added successfully");
		} catch (Exception E) {
			model.addAttribute("status", "Error adding new branch");
		}
		return "/admin";
	}

	@RequestMapping(value = "/deleteBranch", method = RequestMethod.GET)
	public String deleteBranch(Locale locale, Model model, WebRequest webRequest)
			throws SQLException {
		int branchId = Integer.parseInt(webRequest.getParameter("branchId"));

		try {
			model.addAttribute("brancName", adminService
					.getBranchById(branchId).getBranchName());
			String status = adminService.deleteBranch(branchId);
			model.addAttribute("status", status);

		} catch (Exception E) {
			model.addAttribute("status", "Error Deleting Book");
		}
		return "deleteBranch";
	}

	/*
	 * Borrower Mapping
	 */
	@RequestMapping(value = "/viewBorrower", method = RequestMethod.GET)
	public String viewBorrower(Locale locale, Model model, WebRequest webRequest)
			throws SQLException {

		int count = adminService.getAllBorroersCount();
		String s = adminService.pagination("/training/getBorrower",
				new String(), count, 10);

		model.addAttribute("pagination", s);

		return "viewBorrower";

	}

	@RequestMapping(value = "/getBorrower", method = RequestMethod.GET)
	public String getBorrowerData(Locale locale, Model model,
			WebRequest webRequest) throws SQLException {

		List<Borrower> bor;

		int pageNo = Integer.parseInt(webRequest.getParameter("pageNo"));
		int pageSize = Integer.parseInt(webRequest.getParameter("pageSize"));
		String searchString = webRequest.getParameter("searchString");

		bor = (List<Borrower>) adminService.getAllBorrowers(pageNo, pageSize,
				searchString);

		StringBuffer sb = new StringBuffer(
				"<table class='table' id='borrowersTable'><thead><tr><th>#</th><th>Borrower Name</th><th>Borrower Address</th><th>Borrower phone</th><th>Edit</th><th>Delete</th></tr></thead><tbody>");
		for (Borrower bors : bor)
			sb.append("<tr><td>"
					+ bors.getCardNo()
					+ "</td><td>"
					+ bors.getName()
					+ "</td><td>"
					+ bors.getAddress()
					+ "</td><td>"
					+ bors.getPhone()
					+ "</td><td align='center'><button type='button' class='btn btn btn-primary' data-toggle='modal' data-target='#myModal1' href='editBorrower?borId="
					+ bors.getCardNo()
					+ "'>Edit Borrower</button></td><td><button type='button' class='btn btn-sm btn-danger' onclick=\"javascript:location.href='deleteBorrower?borId="
					+ bors.getCardNo()
					+ "';\">Delete Borrower</button></td></tr>");

		sb.append("<table>");
		model.addAttribute("result", sb.toString());
		return "result";
	}

	@RequestMapping(value = "/searchBorrower", method = RequestMethod.GET)
	public String searchBorrower(Locale locale, Model model,
			WebRequest webRequest) throws SQLException {

		String searchString = webRequest.getParameter("searchString");
		int count = adminService.searchBorrowersCount(searchString);
		String s = adminService.pagination("/training/getBorrower",
				searchString, count, 10);

		model.addAttribute("pagination", s);
		model.addAttribute("searchResult", searchString);
		return "viewBorrower";

	}

	@RequestMapping(value = "/editBorrower", method = RequestMethod.GET)
	public String editBorrower(Locale locale, Model model, WebRequest webRequest)
			throws SQLException {

		String borId = webRequest.getParameter("borId");
		if (borId == null) {
			String error = "Please Provide Borrower ID";
			model.addAttribute("error", error);
			return error;
		}

		try {
			Borrower bor = adminService
					.getBorrowerById(Integer.parseInt(borId));
			model.addAttribute("borName", bor.getName());
			model.addAttribute("borAddress", bor.getAddress());
			model.addAttribute("borPhone", bor.getPhone());
			model.addAttribute("borId", borId);
		} catch (Exception E) {
			String error = "Please Provide Borrower Id2";
			model.addAttribute("error", error);
			return error;

		}
		return "editBorrower";

	}

	@RequestMapping(value = "/editBorrower", method = RequestMethod.POST)
	public String editBorrowerPost(Locale locale, Model model,
			WebRequest webRequest) throws SQLException {

		Integer borId = Integer.parseInt(webRequest.getParameter("borId"));
		String borName = webRequest.getParameter("borName");
		String borAddress = webRequest.getParameter("borAddress");
		String borPhone = webRequest.getParameter("borPhone");

		try {
			adminService.updateBorrower(borId, borName, borAddress, borPhone);
			model.addAttribute("status", "success");
		} catch (Exception E) {
			model.addAttribute("status", "Error");
		}
		return "status";

	}

	@RequestMapping(value = "/addBorrower", method = RequestMethod.GET)
	public String addBorrower(Locale locale, Model model, WebRequest webRequest)
			throws SQLException {
		return "addBorrower";
	}

	@RequestMapping(value = "/addBorrower", method = RequestMethod.POST)
	public String addBorrowerPost(Locale locale, Model model,
			WebRequest webRequest) throws SQLException {
		String borName = webRequest.getParameter("borName");
		String borAddress = webRequest.getParameter("borAddress");
		String borPhone = webRequest.getParameter("borPhone");

		try {
			adminService.addBorrower(borName, borAddress, borPhone);
			model.addAttribute("status", "borroewer was added successfully");
		} catch (Exception E) {
			model.addAttribute("status", "Error adding new borrower");
		}
		return "/librarian";
	}

	@RequestMapping(value = "/deleteBorrower", method = RequestMethod.GET)
	public String DeleteBorrower(Locale locale, Model model,
			WebRequest webRequest) throws SQLException {
		int borId = Integer.parseInt(webRequest.getParameter("borId"));

		try {
			model.addAttribute("borName", adminService.getBorrowerById(borId)
					.getName());
			String status = adminService.deleteborrower(borId);
			model.addAttribute("status", status);

		} catch (Exception E) {
			model.addAttribute("status", "Error Deleting Book");
		}
		return "deleteBorrower";
	}

	/*
	 * BookLoans Mapping
	 */
	@RequestMapping(value = "/viewBookLoans", method = RequestMethod.GET)
	public String viewBookLoans(Locale locale, Model model,
			WebRequest webRequest) throws SQLException {

		int count = adminService.getAllLoansCount();
		String s = adminService.pagination("/training/getBookL?Id=",
				new String(), count, 10);

		model.addAttribute("pagination", s);

		return "viewBookLoans";

	}

	@RequestMapping(value = "/getBookL", method = RequestMethod.GET)
	public String getBookLData(Locale locale, Model model, WebRequest webRequest)
			throws SQLException {

		List<BookLoans> books;

		int pageNo = Integer.parseInt(webRequest.getParameter("pageNo"));
		int pageSize = Integer.parseInt(webRequest.getParameter("pageSize"));
		String searchString = webRequest.getParameter("searchString");

		books = (List<BookLoans>) adminService.getAllBookL(pageNo, pageSize,
				searchString);

		StringBuffer sb = new StringBuffer(
				"<table class='table' id='booksTable'><thead><tr><th>Book Title</th><th>Borrower Name</th><th>Branch</th><th>Date Out</th><th>Date In</th><th>Due Date</th><th>Action</th></tr></thead><tbody>");
		for (BookLoans book : books)
			sb.append("<tr><td>"
					+ book.getBook().getTitle()
					+ "</td><td>"
					+ book.getLibraryBranch().getBranchName()
					+ "</td><td>"
					+ book.getBorrower().getName()
					+ "</td><td>"
					+ book.getDateOut()
					+ "</td><td>"
					+ book.getDateIn()
					+ "</td><td>"
					+ book.getDueDate()
					+ "</td><td align='center'><button type='button' class='btn btn btn-primary' data-toggle='modal' data-target='#myModal1' href='editBookL?bookId="
					+ book.getBook().getBookId() + "&branchId="
					+ book.getLibraryBranch().getBranchId() + "&cardNo="
					+ book.getBorrower().getCardNo()
					+ "'>Edit</button></td></tr>");

		sb.append("<table>");
		model.addAttribute("result", sb.toString());
		return "result";
	}

	@RequestMapping(value = "/searchBookL", method = RequestMethod.GET)
	public String searchBookL(Locale locale, Model model, WebRequest webRequest)
			throws SQLException {

		String searchString = webRequest.getParameter("searchString");
		int count = adminService.getAllLoansCount();
		String s = adminService.pagination("/training/getBookL", searchString,
				count, 10);

		model.addAttribute("pagination", s);
		model.addAttribute("searchResult", searchString);
		return "viewBookLoans";

	}

	@RequestMapping(value = "/editBookL", method = RequestMethod.GET)
	public String editBookL(Locale locale, Model model, WebRequest webRequest)
			throws SQLException {

		String bookId = webRequest.getParameter("bookId");
		String cardNo = webRequest.getParameter("cardNo");
		String branchId = webRequest.getParameter("branchId");
		if (bookId == null || cardNo == null || branchId == null) {
			String error = "Please Provide vilid IDs";
			model.addAttribute("error", error);
			return "error";
		}

		BookLoans bl = adminService.getBookLoanByIds(Integer.parseInt(bookId),
				Integer.parseInt(branchId), Integer.parseInt(cardNo));

		model.addAttribute("dIn", bl.getDateIn());
		model.addAttribute("dOut", bl.getDateOut());
		model.addAttribute("dDue", bl.getDueDate());

		model.addAttribute("bookId", bookId);
		model.addAttribute("cardNo", cardNo);
		model.addAttribute("branchId", branchId);

		return "editBookL";

	}

	@RequestMapping(value = "/editBookL", method = RequestMethod.POST)
	public String editBookLPost(Locale locale, Model model,
			WebRequest webRequest) throws SQLException {

		Integer bookId = Integer.parseInt(webRequest.getParameter("bookId"));
		Integer cardNo = Integer.parseInt(webRequest.getParameter("cardNo"));
		Integer branchId = Integer
				.parseInt(webRequest.getParameter("branchId"));
		String dIn = webRequest.getParameter("dIn");
		String dOut = webRequest.getParameter("dOut");
		String dDue = webRequest.getParameter("dDue");

		Book b = adminService.getBookById(bookId);
		Borrower bor = adminService.getBorrowerById(cardNo);
		LibraryBranch lb = adminService.getBranchById(branchId);

		try {
			adminService.editBookloan(b, bor, lb, dIn, dOut, dDue);
			model.addAttribute("status", "success");
		} catch (Exception E) {
			model.addAttribute("status", "Error");
		}
		return "status";

	}

	@RequestMapping(value = "/borrowBookL", method = RequestMethod.GET)
	public String borrowBookL(Locale locale, Model model, WebRequest webRequest)
			throws SQLException {

		String bookId = webRequest.getParameter("bookId");
		if (bookId == null) {
			String error = "Please Provide book ID";
			model.addAttribute("error", error);
			return error;
		}

		try {

		} catch (Exception E) {
			String error = "Please Provide Borrower Id2";
			model.addAttribute("error", error);
			return error;

		}
		return "borrowBookb";

	}

	@RequestMapping(value = "/borrowBookL", method = RequestMethod.POST)
	public String borrowBookLPost(Locale locale, Model model,
			WebRequest webRequest) throws SQLException {

		try {
			model.addAttribute("status", "success");
		} catch (Exception E) {
			model.addAttribute("status", "Error");
		}
		return "status";

	}

	/*
	 * BookCopies Mapping
	 */
	@RequestMapping(value = "/viewBookCopies", method = RequestMethod.GET)
	public String viewBookCopies(Locale locale, Model model,
			WebRequest webRequest) throws SQLException {

		int count = adminService.getAllBooksCount();
		String s = adminService.pagination("/training/getBookCopies",
				new String(), count, 10);

		model.addAttribute("pagination", s);

		return "viewBookCopies";

	}

	@RequestMapping(value = "/getBookCopies", method = RequestMethod.GET)
	public String getBookCopiesData(Locale locale, Model model,
			WebRequest webRequest) throws SQLException {

		List<BookCopies> books;

		int pageNo = Integer.parseInt(webRequest.getParameter("pageNo"));
		int pageSize = Integer.parseInt(webRequest.getParameter("pageSize"));
		String searchString = webRequest.getParameter("searchString");

		books = (List<BookCopies>) adminService.getAllBookb(pageNo, pageSize,
				searchString);

		StringBuffer sb = new StringBuffer(
				"<table class='table' id='booksTable'><thead><tr><th>#</th><th>Book Title</th><th>Branch</th><th>no. of Copies</th><th>Action</th></tr></thead><tbody>");
		for (BookCopies book : books)
			sb.append("<tr><td>"
					+ book.getBook().getBookId()
					+ "</td><td>"
					+ book.getBook().getTitle()
					+ "</td><td>"
					+ book.getLibraryBranch().getBranchName()
					+ "</td><td>"
					+ book.getNoOfCopies()
					+ "</td><td align='center'><button type='button' class='btn btn btn-primary' data-toggle='modal' data-target='#myModal1' href='borrowBookb?bookId="
					+ book.getNoOfCopies() + "'>Borrow Book</button></td></tr>");

		sb.append("<table>");
		model.addAttribute("result", sb.toString());
		return "result";
	}

	@RequestMapping(value = "/searchBookCopies", method = RequestMethod.GET)
	public String searchBookCopies(Locale locale, Model model,
			WebRequest webRequest) throws SQLException {

		String searchString = webRequest.getParameter("searchString");
		int count = adminService.searchBooksCount(searchString);
		String s = adminService.pagination("/training/getBookCopies",
				searchString, count, 10);

		model.addAttribute("pagination", s);
		model.addAttribute("searchResult", searchString);
		return "viewBookCopies";

	}

	@RequestMapping(value = "/borrowBookb", method = RequestMethod.GET)
	public String borrowBookb(Locale locale, Model model, WebRequest webRequest)
			throws SQLException {

		String bookId = webRequest.getParameter("bookId");
		if (bookId == null) {
			String error = "Please Provide book ID";
			model.addAttribute("error", error);
			return error;
		}

		try {

		} catch (Exception E) {
			String error = "Please Provide Borrower Id2";
			model.addAttribute("error", error);
			return error;

		}
		return "borrowBookb";

	}

	@RequestMapping(value = "/borrowBookb", method = RequestMethod.POST)
	public String borrowBookbPost(Locale locale, Model model,
			WebRequest webRequest) throws SQLException {

		try {
			model.addAttribute("status", "success");
		} catch (Exception E) {
			model.addAttribute("status", "Error");
		}
		return "status";

	}

	/*
	 * Genre Mapping
	 */

	@RequestMapping(value = "/viewgenre", method = RequestMethod.GET)
	public String viewGenre(Locale locale, Model model, WebRequest webRequest)
			throws SQLException {

		int count = adminService.getAllGenreCount();
		String s = adminService.pagination("/training/getgenre", new String(),
				count, 10);

		model.addAttribute("pagination", s);

		return "viewgenre";

	}

	@RequestMapping(value = "/getgenre", method = RequestMethod.GET)
	public String getGenreData(Locale locale, Model model, WebRequest webRequest)
			throws SQLException {

		List<Genre> genres;

		int pageNo = Integer.parseInt(webRequest.getParameter("pageNo"));
		int pageSize = Integer.parseInt(webRequest.getParameter("pageSize"));
		String searchString = webRequest.getParameter("searchString");

		genres = (List<Genre>) adminService.getAllGenre(pageNo, pageSize,
				searchString);

		StringBuffer sb = new StringBuffer(
				"<table class='table' id='genresTable'><thead><tr><th>#</th><th>Genre Name</th><th>Books in this genre</th><th>Edit</th><th>Delete</th></tr></thead><tbody>");
		for (Genre genre : genres)
			sb.append("<tr><td>"
					+ genre.getGenreId()
					+ "</td><td>"
					+ genre.getGenreName()
					+ "</td><td align='center'><button type='button' class='btn btn btn-primary' onclick=\"javascript:location.href='viewBookG?genreId="
					+ genre.getGenreId()
					+ "&genreName="
					+ genre.getGenreName()
					+ "';\">View Books</button></td><td align='center'><button type='button' class='btn btn btn-primary' data-toggle='modal' data-target='#myModal1' href='editgenre?genreId="
					+ genre.getGenreId()
					+ "'>Edit Genre</button></td><td><button type='button' class='btn btn-sm btn-danger' onclick=\"javascript:location.href='deletegenre?genreId="
					+ genre.getGenreId()
					+ "';\">Delete Genre</button></td></tr>");

		sb.append("<table>");

		model.addAttribute("result", sb.toString());
		return "result";
	}

	@RequestMapping(value = "/searchgenre", method = RequestMethod.GET)
	public String searchGenre(Locale locale, Model model, WebRequest webRequest)
			throws SQLException {

		String searchString = webRequest.getParameter("searchString");
		int count = adminService.searchGenreCount(searchString);
		String s = adminService.pagination("/training/getgenre", searchString,
				count, 10);

		model.addAttribute("pagination", s);
		model.addAttribute("searchResult", searchString);
		return "viewgenre";

	}

	@RequestMapping(value = "/editgenre", method = RequestMethod.GET)
	public String editGenre(Locale locale, Model model, WebRequest webRequest)
			throws SQLException {

		String genreId = webRequest.getParameter("genreId");
		if (genreId == null) {
			String error = "Please Provide Genre Id";
			model.addAttribute("error", error);
			return error;
		}

		try {
			Genre genre = adminService.getGenreById(Integer.parseInt(genreId));
			model.addAttribute("genreName", genre.getGenreName());
			model.addAttribute("genreId", genreId);
		} catch (Exception E) {
			String error = "Please Provide Genre Id";
			model.addAttribute("error", error);
			return error;

		}
		return "editgenre";

	}

	@RequestMapping(value = "/editgenre", method = RequestMethod.POST)
	public String editGenrePost(Locale locale, Model model,
			WebRequest webRequest) throws SQLException {

		Integer genreId = Integer.parseInt(webRequest.getParameter("genreId"));
		String genreName = webRequest.getParameter("genreName");

		try {
			adminService.updateGenre(genreId, genreName);
			model.addAttribute("status", "success");
		} catch (Exception E) {
			model.addAttribute("status", "Error");
		}
		return "status";

	}

	@RequestMapping(value = "/addgenre", method = RequestMethod.GET)
	public String addGenre(Locale locale, Model model, WebRequest webRequest)
			throws SQLException {
		return "addgenre";
	}

	@RequestMapping(value = "/addgenre", method = RequestMethod.POST)
	public String addGenrePost(Locale locale, Model model, WebRequest webRequest)
			throws SQLException {

		String genreName = webRequest.getParameter("genreName");

		try {
			adminService.addgenre(genreName);
			model.addAttribute("status", "Genre was added successfully");
		} catch (Exception E) {
			model.addAttribute("status", "Error adding new genre");
		}
		return "/admin";
	}

	@RequestMapping(value = "/deletegenre", method = RequestMethod.GET)
	public String DeleteGenre(Locale locale, Model model, WebRequest webRequest)
			throws SQLException {
		int genreId = Integer.parseInt(webRequest.getParameter("genreId"));
		try {
			model.addAttribute("genreName", adminService.getGenreById(genreId)
					.getGenreName());
			String status = adminService.deleteGenre(genreId);
			model.addAttribute("status", status);

		} catch (Exception E) {
			model.addAttribute("status", "Error Delete Genre");
		}
		return "deletegenre";
	}

	@RequestMapping(value = "/viewBookG", method = RequestMethod.GET)
	public String viewBookG(Locale locale, Model model, WebRequest webRequest)
			throws SQLException {

		int count = adminService.getAllBooksCount();
		String s = adminService.pagination("/training/getBookG?genreId="
				+ webRequest.getParameter("genreId"), new String(), count, 10);

		model.addAttribute("pagination", s);
		model.addAttribute("genreName", webRequest.getParameter("genreName"));
		model.addAttribute("genreId", webRequest.getParameter("genreId"));
		return "viewBookG";

	}

	@RequestMapping(value = "/getBookG", method = RequestMethod.GET)
	public String getBookGData(Locale locale, Model model, WebRequest webRequest)
			throws SQLException {

		List<Book> books;

		int pageNo = Integer.parseInt(webRequest.getParameter("pageNo"));
		int pageSize = Integer.parseInt(webRequest.getParameter("pageSize"));
		String searchString = webRequest.getParameter("genreId");
		if (searchString.isEmpty()) {
			searchString = new String();
		}
		books = (List<Book>) adminService.getAllGenreBooks(pageNo, pageSize,
				searchString);

		StringBuffer sb = new StringBuffer(
				"<table class='table' id='booksTable'><thead><tr><th>#</th><th>Book Title</th><th>Book Publisher</th><th>Action</th></tr></thead><tbody>");
		for (Book book : books)
			sb.append("<tr><td>"
					+ book.getBookId()
					+ "</td><td>"
					+ book.getTitle()
					+ "</td><td>"
					+ book.getPublisher().getPublisherName()
					+ "</td><td align='center'><button type='button' class='btn btn btn-primary' onclick=\"javascript:location.href='borrowBookB?bookId="
					+ book.getBookId() + "';\">Select Book</button></td></tr>");

		sb.append("<table>");

		model.addAttribute("result", sb.toString());
		return "result";
	}

	/*
	 * Books borrowing Mapping
	 */
	
	
	
	@RequestMapping(value = "/viewBookB", method = RequestMethod.GET)
	public String viewBookB(Locale locale, Model model, WebRequest webRequest)
			throws SQLException {

		int count = adminService.getAllBooksCount();
		String s = adminService.pagination("/training/getBookB", new String(),
				count, 10);

		model.addAttribute("pagination", s);

		return "viewBookB";

	}

	@RequestMapping(value = "/getBookB", method = RequestMethod.GET)
	public String getBookBData(Locale locale, Model model, WebRequest webRequest)
			throws SQLException {

		List<Book> books;

		int pageNo = Integer.parseInt(webRequest.getParameter("pageNo"));
		int pageSize = Integer.parseInt(webRequest.getParameter("pageSize"));
		String searchString = webRequest.getParameter("searchString");

		books = (List<Book>) adminService.getAllBooks(pageNo, pageSize,
				searchString);

		StringBuffer sb = new StringBuffer(
				"<table class='table' id='booksTable'><thead><tr><th>#</th><th>Book Title</th><th>Book Publisher</th><th>Borrow</th></tr></thead><tbody>");
		for (Book book : books)
			sb.append("<tr><td>"
					+ book.getBookId()
					+ "</td><td>"
					+ book.getTitle()
					+ "</td><td>"
					+ book.getPublisher().getPublisherName()
					+ "</td><td align='center'><button type='button' class='btn btn btn-primary' onclick=\"javascript:location.href='borrowBookB?bookId="
					+ book.getBookId() + "';\">Select Book</button></td></tr>");

		sb.append("<table>");
		model.addAttribute("result", sb.toString());
		return "result";
	}

	@RequestMapping(value = "/searchBookB", method = RequestMethod.GET)
	public String searchBookB(Locale locale, Model model, WebRequest webRequest)
			throws SQLException {

		String searchString = webRequest.getParameter("searchString");
		int count = adminService.searchBooksCount(searchString);
		String s = adminService.pagination("/training/getBookB", searchString,
				count, 10);

		model.addAttribute("pagination", s);
		model.addAttribute("searchResult", searchString);
		return "viewBookB";

	}

	@RequestMapping(value = "/borrowBookB", method = RequestMethod.GET)
	public String borrowBookB(Locale locale, Model model, WebRequest webRequest)
			throws SQLException {

		int count = adminService.getAllBranchesCount();
		String s = adminService.pagination("/training/getBorrowBookB?bookId="
				+ webRequest.getParameter("bookId"), new String(), count, 10);

		model.addAttribute("pagination", s);

		return "viewBorrowBookB";

	}

	@RequestMapping(value = "/getBorrowBookB", method = RequestMethod.GET)
	public String borrowBookBData(Locale locale, Model model,
			WebRequest webRequest) throws SQLException {

		List<LibraryBranch> lb;

		int pageNo = Integer.parseInt(webRequest.getParameter("pageNo"));
		int pageSize = Integer.parseInt(webRequest.getParameter("pageSize"));
		String bookId = webRequest.getParameter("bookId");

		lb = (List<LibraryBranch>) adminService.searchBookBranches(bookId,
				pageNo, pageSize);

		StringBuffer sb = new StringBuffer(
				"<table class='table' id='booksTable'><thead><tr><th>#</th><th>Branch Name</th><th>Branch Address</th><th>Edit</th><th>Delete</th></tr></thead><tbody>");
		for (LibraryBranch lbs : lb)
			sb.append("<tr><td>"
					+ lbs.getBranchId()
					+ "</td><td>"
					+ lbs.getBranchName()
					+ "</td><td>"
					+ lbs.getBranchAddress()
					+ "</td><td align='center'></td><td><button type='button' class='btn btn-sm btn-primary' onclick=\"javascript:location.href='viewReader?branchId="
					+ lbs.getBranchId() + "&bookId=" + bookId
					+ "';\">Select Branch</button></td></tr>");

		sb.append("<table>");
		model.addAttribute("result", sb.toString());
		return "result";
	}

	@RequestMapping(value = "/viewReader", method = RequestMethod.GET)
	public String viewReader(Locale locale, Model model, WebRequest webRequest)
			throws SQLException {

		int count = adminService.getAllBranchesCount();
		String s = adminService.pagination(
				"/training/getReader?branchId="
						+ webRequest.getParameter("branchId") + "&bookId="
						+ webRequest.getParameter("bookId"), new String(),
				count, 10);

		model.addAttribute("pagination", s);

		return "viewReader";

	}

	@RequestMapping(value = "/getReader", method = RequestMethod.GET)
	public String readerData(Locale locale, Model model, WebRequest webRequest)
			throws SQLException {

		List<Borrower> bor;

		int pageNo = Integer.parseInt(webRequest.getParameter("pageNo"));
		int pageSize = Integer.parseInt(webRequest.getParameter("pageSize"));
		String bookId = webRequest.getParameter("bookId");
		String branchId = webRequest.getParameter("branchId");

		bor = (List<Borrower>) adminService.getAllBorrowers(pageNo, pageSize,
				"");

		StringBuffer sb = new StringBuffer(
				"<table class='table' id='borrowersTable'><thead><tr><th>#</th><th>Borrower Name</th><th>Action</th></tr></thead><tbody>");
		for (Borrower bors : bor)
			sb.append("<tr><td>"
					+ bors.getCardNo()
					+ "</td><td>"
					+ bors.getName()
					+ "</td><td><button type='button' class='btn btn btn-primary' onclick=\"javascript:location.href='checkOut?"
					+ "bookId=" + bookId + "&branchId=" + branchId + "&cardNo="
					+ bors.getCardNo() + "';\">Checkout</button></td></tr>");

		sb.append("<table>");
		model.addAttribute("result", sb.toString());
		return "result";
	}

	@RequestMapping(value = "/checkOut", method = RequestMethod.GET)
	public String checkOut(Locale locale, Model model, WebRequest webRequest)
			throws SQLException {

		String bookId = webRequest.getParameter("bookId");
		String cardNo = webRequest.getParameter("cardNo");
		String branchId = webRequest.getParameter("branchId");
		if (bookId == null) {
			System.out.println("Please Provide vilid IDs");
		}

		Book b = adminService.getBookById(Integer.parseInt(bookId));
		Borrower bor = adminService.getBorrowerById(Integer.parseInt(cardNo));
		LibraryBranch lb = adminService.getBranchById(Integer
				.parseInt(branchId));

		adminService.addBookLoan(b, lb, bor);
		adminService.updateBookCopiesSub(b, lb);
		webRequest.setAttribute("status", "the book was borrowed successfully", 0);
		return "borrower";

	}

	/*
	 * returnBook Returning a book
	 */

	@RequestMapping(value = "/viewReturnBook", method = RequestMethod.GET)
	public String ViewReturnBook(Locale locale, Model model,
			WebRequest webRequest) throws SQLException {

		int count = adminService.getAllBorroersCount();
		String s = adminService.pagination("/training/getReturnBook",
				new String(), count, 10);

		model.addAttribute("pagination", s);

		return "viewReturnBook";

	}

	@RequestMapping(value = "/getReturnBook", method = RequestMethod.GET)
	public String getReturnBookData(Locale locale, Model model,
			WebRequest webRequest) throws SQLException {

		List<Borrower> bor;

		int pageNo = Integer.parseInt(webRequest.getParameter("pageNo"));
		int pageSize = Integer.parseInt(webRequest.getParameter("pageSize"));
		String searchString = webRequest.getParameter("searchString");

		bor = (List<Borrower>) adminService.getAllBorrowers(pageNo, pageSize,
				searchString);

		StringBuffer sb = new StringBuffer(
				"<table class='table' id='booksTable'><thead><tr><th>#</th><th>Name</th><th>Action</th></tr></thead><tbody>");
		for (Borrower bors : bor)
			sb.append("<tr><td>"
					+ bors.getCardNo()
					+ "</td><td>"
					+ bors.getName()
					+ "</td><td align='center'><button type='button' class='btn btn btn-primary' onclick=\"javascript:location.href='borrowerReturn?cardNo="
					+ bors.getCardNo()
					+ "';\">View Borrowed Books</button></td></tr>");

		sb.append("<table>");
		model.addAttribute("result", sb.toString());
		return "result";
	}

	@RequestMapping(value = "/borrowerReturn", method = RequestMethod.GET)
	public String borrowerReturn(Locale locale, Model model,
			WebRequest webRequest) throws SQLException {

		int count = adminService.getAllBorroersCount();
		String s = adminService.pagination(
				"/training/getBorrowerReturn?cardNo="
						+ webRequest.getParameter("cardNo"), new String(),
				count, 10);

		model.addAttribute("pagination", s);

		return "borrowerReturn";

	}

	@RequestMapping(value = "/getBorrowerReturn", method = RequestMethod.GET)
	public String getBorrowerReturnData(Locale locale, Model model,
			WebRequest webRequest) throws SQLException {

		List<BookLoans> bl;

		int cardNo = Integer.parseInt(webRequest.getParameter("cardNo"));

		bl = (List<BookLoans>) adminService.readBor(cardNo);
		if (bl.size() > 0) {
			StringBuffer sb = new StringBuffer(
					"<table class='table' id='booksTable'><thead><tr><th>Book Title</th><th>Borrower Name</th><th>Branch</th><th>Date Out</th><th>Date In</th><th>Due Date</th><th>Action</th></tr></thead><tbody>");
			for (BookLoans book : bl)
				sb.append("<tr><td>"
						+ book.getBook().getTitle()
						+ "</td><td>"
						+ book.getLibraryBranch().getBranchName()
						+ "</td><td>"
						+ book.getBorrower().getName()
						+ "</td><td>"
						+ book.getDateOut()
						+ "</td><td>"
						+ book.getDateIn()
						+ "</td><td>"
						+ book.getDueDate()
						+ "</td><td align='center'><button type='button' class='btn btn btn-primary' onclick=\"javascript:location.href='returnBB?bookId="
						+ book.getBook().getBookId() + "&branchId="
						+ book.getLibraryBranch().getBranchId() + "&cardNo="
						+ book.getBorrower().getCardNo()
						+ "';\">Return</button></td></tr>");

			sb.append("<table>");

			model.addAttribute("result", sb.toString());
		} else
			model.addAttribute("result",
					"<h1>You have no borrwed books at the moment</h1>	");
		return "result";
	}

	@RequestMapping(value = "/returnBB", method = RequestMethod.GET)
	public String returnBB(Locale locale, Model model, WebRequest webRequest)
			throws SQLException {

		String bookId = webRequest.getParameter("bookId");
		String cardNo = webRequest.getParameter("cardNo");
		String branchId = webRequest.getParameter("branchId");
		if (bookId == null) {
			System.out.println("Please Provide vilid IDs");
		}

		Book b = adminService.getBookById(Integer.parseInt(bookId));
		Borrower bor = adminService.getBorrowerById(Integer.parseInt(cardNo));
		LibraryBranch lb = adminService.getBranchById(Integer
				.parseInt(branchId));

		adminService.returnBookloan(b, bor, lb);
		adminService.updateBookCopiesAdd(b, lb);
		webRequest.setAttribute("status", "the book was returned successfully", 0);
		return "borrower";
	}
	
	/*
	 * Genre Borrowing mapping
	 */
	
	@RequestMapping(value = "/viewgenreB", method = RequestMethod.GET)
	public String viewGenreB(Locale locale, Model model, WebRequest webRequest)
			throws SQLException {

		int count = adminService.getAllGenreCount();
		String s = adminService.pagination("/training/getgenreB", new String(),
				count, 10);

		model.addAttribute("pagination", s);

		return "viewgenreB";

	}

	@RequestMapping(value = "/getgenreB", method = RequestMethod.GET)
	public String getGenreBData(Locale locale, Model model, WebRequest webRequest)
			throws SQLException {

		List<Genre> genres;

		int pageNo = Integer.parseInt(webRequest.getParameter("pageNo"));
		int pageSize = Integer.parseInt(webRequest.getParameter("pageSize"));
		String searchString = webRequest.getParameter("searchString");

		genres = (List<Genre>) adminService.getAllGenre(pageNo, pageSize,
				searchString);

		StringBuffer sb = new StringBuffer(
				"<table class='table' id='genresTable'><thead><tr><th>#</th><th>Genre Name</th><th>Books in this genre</th></tr></thead><tbody>");
		for (Genre genre : genres)
			sb.append("<tr><td>"
					+ genre.getGenreId()
					+ "</td><td>"
					+ genre.getGenreName()
					+ "</td><td align='center'><button type='button' class='btn btn btn-primary' onclick=\"javascript:location.href='viewBookG?genreId="
					+ genre.getGenreId()
					+ "&genreName="
					+ genre.getGenreName()
					+ "';\">View Books</button></td></tr>");

		sb.append("<table>");

		model.addAttribute("result", sb.toString());
		return "result";
	}


	


}
