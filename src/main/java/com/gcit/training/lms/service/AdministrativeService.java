package com.gcit.training.lms.service;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.gcit.training.lms.dao.AuthorDAO;
import com.gcit.training.lms.dao.BookDAO;
import com.gcit.training.lms.dao.BookLoansDAO;
import com.gcit.training.lms.dao.BorrowerDAO;
import com.gcit.training.lms.dao.GenreDAO;
import com.gcit.training.lms.dao.LibraryBranchDAO;
import com.gcit.training.lms.dao.PublisherDAO;
import com.gcit.training.lms.entity.Author;
import com.gcit.training.lms.entity.Book;
import com.gcit.training.lms.entity.Borrower;
import com.gcit.training.lms.entity.Genre;
import com.gcit.training.lms.entity.LibraryBranch;
import com.gcit.training.lms.entity.Publisher;

public class AdministrativeService {

	@Autowired
	AuthorDAO adao;

	@Autowired
	BookDAO bdao;

	@Autowired
	LibraryBranchDAO lbdao;

	@Autowired
	BookLoansDAO bldao;

	@Autowired
	BorrowerDAO bordao;

	@Autowired
	GenreDAO gdao;

	@Autowired
	PublisherDAO publisherDao;

	@Autowired
	JdbcTemplate template;

	@Transactional
	/*
	 * Book transactions
	 */
	public void addBook(String bookTitle, Publisher publisher)
			throws SQLException {
		Book b = new Book();
		b.setTitle(bookTitle);
		b.setPublisher(publisher);
		bdao.create(b);
	}

	public List<Book> getAllBooks(int pageNo, int pageSize, String searchString)
			throws SQLException {
		if (StringUtils.hasLength(searchString))
			return bdao.readByName(searchString, pageNo, pageSize);
		else
			return bdao.readAll(pageNo, pageSize);
	}

	public int getBooksCountByTitle(String searchString) throws SQLException {
		return bdao.getCount();
	}

	@Transactional
	public String deleteBook(Integer bookId) throws SQLException {
		Book b = new Book();
		b.setBookId(bookId);
		bdao.delete(b);

		return "success";
	}

	public void updateBook(Integer BookId, String BookTitle)
			throws SQLException {
		Book b = new Book();
		b.setBookId(BookId);
		b.setTitle(BookTitle);
		bdao.update(b);
	}

	public List<Book> searchBooks(String searchString, Integer pageNo,
			Integer pageSize) throws SQLException {
		return bdao.readByName(searchString, pageNo, pageSize);

	}

	public int searchBooksCount(String searchString) throws SQLException {
		return bdao.getCount();
	}

	public Book getBookById(int bookId) throws SQLException {
		// TODO Auto-generated method stub
		return bdao.readOne(bookId);
	}

	public int getAllBooksCount() throws SQLException {

		return bdao.getCount();
	}

	/*
	 * Author transactions
	 */
	public void addAuthor(String authorName) throws SQLException {
		Author author = new Author();
		author.setAuthorName(authorName);
		adao.create(author);
	}

	public List<Author> getAllAuthors(int pageNo, int pageSize,
			String searchString) throws SQLException {
		if (StringUtils.hasLength(searchString))
			return adao.readByName(searchString, pageNo, pageSize);
		else
			return adao.readAll(pageNo, pageSize);
	}

	public int getAllAuthorsCount() throws SQLException {

		return adao.getCount();

	}

	@Transactional
	public String deleteAuthor(Integer authorId) throws SQLException {
		Author author = new Author();
		author.setAuthorId(authorId);

		adao.delete(author);

		return "success";
	}

	@Transactional
	public void updateAuthor(Integer authorId, String authorName)
			throws SQLException {
		Author author = new Author();
		author.setAuthorId(authorId);
		author.setAuthorName(authorName);
		adao.update(author);
	}

	public List<Author> searchAuthors(String searchString, Integer pageNo,
			Integer pageSize) throws SQLException {
		return adao.readByName(searchString, pageNo, pageSize);

	}

	public int searchAuthorsCount(String searchString) throws SQLException {
		return adao.getCount();
	}

	public String pagination(String url, String searchString, int count,
			int pageSize) {
		StringBuffer sb = new StringBuffer(
				"<script src=\"./resources/template_files/pagination.js\"></script>");

		int totalPage = ((count) / pageSize);

		if (count % pageSize != 0)
			totalPage++;
		sb.append("<nav><ul class='pagination'>");
		if(!url.contains("?"))
		url = url + "?";

		if (StringUtils.hasLength(searchString))
			url = url + "&searchString=" + searchString + "&";

		for (int i = 1; i <= totalPage; i++)
			sb.append("<li><a   class='paginationClass' data-href='" + url
					+ "&pageNo=" + i + "&pageSize=" + pageSize + "' >" + i
					+ "</a></li>");

		sb.append("</ul></nav>");

		return sb.toString();

	}

	public Author getAuthorById(int authorId) throws SQLException {
		// TODO Auto-generated method stub
		return adao.readOne(authorId);
	}

	@Transactional
	/*
	 * Branch transactions
	 */
	public void addBranch(String BranchName, String BranchAddress)
			throws SQLException {
		LibraryBranch lb = new LibraryBranch();
		lb.setBranchName(BranchName);
		lb.setBranchAddress(BranchAddress);
		lbdao.create(lb);
	}

	public List<LibraryBranch> getAllBranches(int pageNo, int pageSize,
			String searchString) throws SQLException {
		if (StringUtils.hasLength(searchString))
			return lbdao.readByName(searchString, pageNo, pageSize);
		else
			return lbdao.readAll(pageNo, pageSize);
	}

	public int getBranchesCountByTitle(String searchString) throws SQLException {
		return lbdao.getCount();
	}

	@Transactional
	public String deleteBranch(Integer branchId) throws SQLException {
		LibraryBranch lb = new LibraryBranch();
		lb.setBranchId(branchId);
		lbdao.delete(lb);

		return "success";
	}

	public void updateBranch(Integer branchId, String branchName,
			String branchAddress) throws SQLException {
		LibraryBranch lb = new LibraryBranch();
		lb.setBranchId(branchId);
		lb.setBranchName(branchName);
		lb.setBranchAddress(branchAddress);
		lbdao.update(lb);
	}

	public List<LibraryBranch> searchBranches(String searchString,
			Integer pageNo, Integer pageSize) throws SQLException {
		return lbdao.readByName(searchString, pageNo, pageSize);

	}

	public int searchBranchesCount(String searchString) throws SQLException {
		return lbdao.getCount();
	}

	public LibraryBranch getBranchById(int branchId) throws SQLException {
		// TODO Auto-generated method stub
		return lbdao.readById(branchId);
	}

	public int getAllBranchesCount() throws SQLException {

		return lbdao.getCount();
	}

	/*
	 * Borrower transactions
	 */
	public void addBorrower(String borName, String borAddress, String borPhone)
			throws SQLException {
		Borrower bor = new Borrower();
		bor.setName(borName);
		bor.setAddress(borAddress);
		bor.setPhone(borPhone);
		bordao.create(bor);
	}

	public List<Borrower> getAllBorrowers(int pageNo, int pageSize,
			String searchString) throws SQLException {
		if (StringUtils.hasLength(searchString))
			return bordao.readByName(searchString, pageNo, pageSize);
		else
			return bordao.readAll(pageNo, pageSize);
	}

	public int getAllBorroersCount() throws SQLException {

		return adao.getCount();

	}

	@Transactional
	public String deleteborrower(Integer borId) throws SQLException {
		Borrower bor = new Borrower();
		bor.setCardNo(borId);

		bordao.delete(bor);

		return "success";
	}

	@Transactional
	public void updateBorrower(Integer borId, String borName,
			String borAddress, String borPhone) throws SQLException {
		Borrower bor = new Borrower();
		bor.setName(borName);
		bor.setAddress(borAddress);
		bor.setPhone(borPhone);
		bordao.update(bor);
	}

	public List<Borrower> searchBorowers(String searchString, Integer pageNo,
			Integer pageSize) throws SQLException {
		return bordao.readByName(searchString, pageNo, pageSize);

	}

	public int searchBorrowersCount(String searchString) throws SQLException {
		return adao.getCount();
	}

	public Borrower getBorrowerById(int borId) throws SQLException {
		// TODO Auto-generated method stub
		return bordao.readById(borId);
	}

	/*
	 * Genre Transactions
	 */

	public int getAllGenreCount() throws SQLException {
		return gdao.getCount();
	}

	public List<Genre> getAllGenre(int pageNo, int pageSize, String searchString)
			throws SQLException {
		if (StringUtils.hasLength(searchString))
			return gdao.readByName(searchString, pageNo, pageSize);
		else
			return gdao.readAll(pageNo, pageSize);
	}

	public int searchGenreCount(String searchString) throws SQLException {
		return gdao.getCount();
	}

	public Genre getGenreById(int genreId) throws SQLException {
		return gdao.readOne(genreId);
	}

	public String addgenre(String genreName) throws SQLException {
		Genre g = new Genre();
		g.setGenreName(genreName);

		gdao.create(g);
		return "success";
	}

	public void updateGenre(Integer genreId, String genreName)
			throws SQLException {

		Genre a = new Genre();
		a.setGenreId(genreId);
		a.setGenreName(genreName);
		gdao.update(a);

	}

	public String deleteGenre(int genreId) throws SQLException {
		Genre a = new Genre();
		a.setGenreId(genreId);

		gdao.delete(a);
		return "success";
	}

	public List<Book> getAllGenreBooks(int pageNo, int pageSize, String searchString)
			throws SQLException {
		return bdao.readAllGenre(searchString, pageNo, pageSize);
	}
}
