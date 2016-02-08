package com.gcit.training.lms.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;

import com.gcit.training.lms.entity.BookLoans;

public class BookLoansDAO extends AbstractDAO implements
		ResultSetExtractor<List<BookLoans>> {

	@Autowired
	JdbcTemplate template;

	@Autowired
	BookDAO bdao;

	@Autowired
	LibraryBranchDAO lbdao;

	@Autowired
	BorrowerDAO bordao;

	public void create(BookLoans bl) throws SQLException {
		template.update(
				"insert into tbl_book_loans (bookId, branchId, cardNo, dateOut, dateIn, dueDate) values (?,?,?,?,?,?)",
				new Object[] { bl.getBook().getBookId(),
						bl.getLibraryBranch().getBranchId(),
						bl.getBorrower().getCardNo(), bl.getDateOut(),
						bl.getDateIn(), bl.getDueDate() });
	}

	public void update(BookLoans bl) throws SQLException {
		template.update(
				"update tbl_book_loans set dateOut = ? , dateIn = ? , dueDate = ? where bookId = ? and branchId = ? and cardNo= ?",
				new Object[] { bl.getDateOut(), bl.getDateIn(),
						bl.getDueDate(), bl.getBook().getBookId(),
						bl.getLibraryBranch().getBranchId(),
						bl.getBorrower().getCardNo() });
	}

	public void delete(BookLoans bl) throws SQLException {
		template.update(
				"delete from tbl_book_loans where bookId = ? and branchId = ? and cardNo= ?",
				new Object[] { bl.getBook().getBookId(),
						bl.getLibraryBranch().getBranchId(),
						bl.getBorrower().getCardNo() });
	}

	public BookLoans readOne(int bookId, int branchId, int cardNo)
			throws SQLException {
		List<BookLoans> list = (List<BookLoans>) template
				.query("select * from tbl_book_loans where bookId = ? and branchId = ? and cardNo= ?",
						new Object[] { bookId, branchId, cardNo }, this);

		if (list != null && list.size() > 0) {
			return list.get(0);
		} else {
			return null;
		}
	}

	public List<BookLoans> readByBor(int cardNo) throws SQLException {
		return (List<BookLoans>) template.query(
				"select * from tbl_book_loans where cardNo= ? and dateIn is null",
				new Object[] { cardNo }, this);

	}

	public List<BookLoans> readAll(int pageNo, int pageSize)
			throws SQLException {
		setPageNo(pageNo);
		return (List<BookLoans>) template
				.query("Select * "
						+ "From library.tbl_library_branch lb, library.tbl_book b, library.tbl_book_loans bl, library.tbl_borrower bor"
						+ " where (lb.branchId) = (bl.branchId) and (bl.bookId) = (b.bookId) and (bl.cardNo) = (bor.cardNo)"
						+ " order by (b.bookId) LIMIT ?,? ", new Object[] {
						(pageNo - 1) * pageSize, pageSize }, this);
	}

	public List<BookLoans> readAll(String searchString, int pageSize, int pageNo) {
		setPageNo(pageNo);
		String qString = "%" + searchString + "%";
		return (List<BookLoans>) template
				.query("Select * "
						+ "From library.tbl_library_branch lb, library.tbl_book b, library.tbl_book_loans bl, library.tbl_borrower bor"
						+ " where (b.title) like ? and  (lb.branchId) = (bl.branchId) and (bl.bookId) = (b.bookId) and (bl.cardNo) = (bor.cardNo)"
						+ " order by (b.bookId) LIMIT ?,? ", new Object[] {
						qString, (pageNo - 1) * pageSize, pageSize }, this);
	}

	public Integer getCount() throws SQLException {
		return template.queryForObject("select count(*) from tbl_book_loans",
				Integer.class);
	}

	@Override
	public List<BookLoans> extractData(ResultSet rs) throws SQLException {

		List<BookLoans> blList = new ArrayList<BookLoans>();
		while (rs.next()) {
			BookLoans bl = new BookLoans();

			bl.setBook(bdao.readOne(rs.getInt("bookId")));
			bl.setLibraryBranch(lbdao.readById(rs.getInt("branchId")));
			bl.setBorrower(bordao.readById(rs.getInt("cardNo")));
			bl.setDateOut(rs.getString("dateOut"));
			bl.setDateIn(rs.getString("dateIn"));
			bl.setDueDate(rs.getString("dueDate"));

			blList.add(bl);
		}

		return blList;

	}

	public void borrowB(BookLoans bl) throws SQLException {
		template.update(
				"insert into tbl_book_loans (bookId, branchId, cardNo, dateOut, dueDate) values (?,?,?,CURDATE(),DATE_ADD(CURDATE(), INTERVAL 30 DAY))",
				new Object[] { bl.getBook().getBookId(),
						bl.getLibraryBranch().getBranchId(),
						bl.getBorrower().getCardNo() });
	}
	
	public void returnB(BookLoans bl) throws SQLException {
		template.update(
				"update tbl_book_loans set dateIn = CURDATE() where bookId = ? and branchId = ? and cardNo= ?",
				new Object[] { bl.getBook().getBookId(),
						bl.getLibraryBranch().getBranchId(),
						bl.getBorrower().getCardNo() });
	}

}
