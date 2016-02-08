package com.gcit.training.lms.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;

import com.gcit.training.lms.entity.BookCopies;

public class BookCopiesDAO extends AbstractDAO implements
		ResultSetExtractor<List<BookCopies>> {

	@Autowired
	JdbcTemplate template;

	@Autowired
	BookDAO bdao;

	@Autowired
	LibraryBranchDAO lbdao;

	public void create(BookCopies nc) throws SQLException {
		template.update(
				"insert into tbl_book_copies (bookId,branchId, noOfCopies) values (?,?,?)",
				new Object[] { nc.getBook().getBookId(),
						nc.getLibraryBranch().getBranchId(), nc.getNoOfCopies() });
	}

	public void update(BookCopies nc) throws SQLException {
		template.update(
				"update tbl_book_copies set noOfCopies = ? where bookId = (?) and branchId = (?)",
				new Object[] { nc.getNoOfCopies(), nc.getBook().getBookId(),
						nc.getLibraryBranch().getBranchId() });
	}

	public void delete(BookCopies nc) throws SQLException {
		template.update(
				"delete from tbl_book_copies where where bookId = (?) and branchId = (?)",
				new Object[] { nc.getBook().getBookId(),
						nc.getLibraryBranch().getBranchId() });
	}

	public BookCopies readOne(int bookId, int branchId) throws SQLException {
		List<BookCopies> list = (List<BookCopies>) template
				.query("select * from tbl_book_copies where bookId = ? and branchId = (?)",
						new Object[] { bookId, branchId }, this);

		if (list != null && list.size() > 0) {
			return list.get(0);
		} else {
			return null;
		}
	}

	public List<BookCopies> readAll(int pageNo, int pageSize)
			throws SQLException {
		setPageNo(pageNo);
		return (List<BookCopies>) template
				.query("Select * "
						+ "From tbl_library_branch lb, tbl_book b, tbl_book_copies bc"
						+ " where (lb.branchId) = (bc.branchId) and (bc.bookId) = (b.bookId)"
						+ " order by (b.title) LIMIT ?,? ", new Object[] {
						(pageNo - 1) * pageSize, pageSize }, this);
	}

	public List<BookCopies> readAll(String searchString, int pageSize,
			int pageNo) {
		setPageNo(pageNo);
		String qString = "%" + searchString + "%";
		return (List<BookCopies>) template
				.query("Select *"
						+ " From tbl_library_branch lb, tbl_book b, tbl_book_copies bc"
						+ " Where (b.title) like ? and (lb.branchId) = (bc.branchId) and (bc.bookId) = (b.bookId)"
						+ " order by (b.title) LIMIT ?,? ", new Object[] {
						qString, (pageNo - 1) * pageSize, pageSize }, this);
	}

	public Integer getCount() throws SQLException {
		return template.queryForObject("select count(*) from tbl__book_copies",
				Integer.class);
	}

	@Override
	public List<BookCopies> extractData(ResultSet rs) throws SQLException {
		List<BookCopies> bcList = new ArrayList<BookCopies>();
		while (rs.next()) {
			BookCopies bc = new BookCopies();
			bc.setBook(bdao.readOne(rs.getInt("bookId")));
			bc.setLibraryBranch(lbdao.readById(rs.getInt("branchId")));
			bc.setNoOfCopies(rs.getInt("noOfCopies"));

			bcList.add(bc);
		}

		return bcList;
	}
}
