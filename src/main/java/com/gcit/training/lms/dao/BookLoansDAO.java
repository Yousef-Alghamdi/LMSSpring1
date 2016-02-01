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
				"insert into tbl_book_loans (bookId, branchId, cardId, dateOut, dateIn, dueDate) values (?,?,?,?,?,?)",
				new Object[] { bl.getBook().getBookId(),
						bl.getLibraryBranch().getBranchId(),
						bl.getBorrower().getCardNo(), bl.getDateOut(),
						bl.getDateIn(), bl.getDueDate() });
	}

	public void update(BookLoans bl) throws SQLException {
		template.update(
				"update tbl_book_loans set (dateOut,dateIn,dueDate) (?,?,?) where bookId = ? and branchId = ? and cardId= ?",
				new Object[] { bl.getDateOut(), bl.getDateIn(),
						bl.getDueDate(), bl.getBook().getBookId(),
						bl.getLibraryBranch().getBranchId(),
						bl.getBorrower().getCardNo() });
	}

	public void delete(BookLoans bl) throws SQLException {
		template.update(
				"delete from tbl_book_loans where bookId = ? and branchId = ? and cardId= ?",
				new Object[] { bl.getBook().getBookId(),
						bl.getLibraryBranch().getBranchId(),
						bl.getBorrower().getCardNo() });
	}

	public BookLoans readOne(int bookId, int branchId, int cardId)
			throws SQLException {
		List<BookLoans> list = (List<BookLoans>) template
				.query("select * from tbl_book_loans where bookId = ? and branchId = ? and cardId= ?",
						new Object[] { bookId, branchId, cardId }, this);

		if (list != null && list.size() > 0) {
			return list.get(0);
		} else {
			return null;
		}
	}

	public List<BookLoans> readAll(int pageNo, int pageSize)
			throws SQLException {
		setPageNo(pageNo);
		return (List<BookLoans>) template.query("select * from tbl_book_loans",
				this);
	}

	public Integer getCount() throws SQLException {
		return template.queryForObject("select count(*) from tbl__book_loans",
				Integer.class);
	}

	@Override
	public List<BookLoans> extractData(ResultSet rs) throws SQLException {

		List<BookLoans> blList = new ArrayList<BookLoans>();
		while (rs.next()) {
			BookLoans bl = new BookLoans();

			bl.setBook(bdao.readOne(rs.getInt("bookId")));
			bl.setLibraryBranch(lbdao.readById(rs.getInt("branchId")));
			bl.setBorrower(bordao.readById(rs.getInt("branchId")));
			bl.setDateOut(rs.getDate("dateOut"));
			bl.setDateIn(rs.getDate("dateIn"));
			bl.setDueDate(rs.getDate("dueDate"));

			blList.add(bl);
		}

		return blList;

	}

}
