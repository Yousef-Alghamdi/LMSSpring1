package com.gcit.training.lms.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;

import com.gcit.training.lms.entity.Book;

public class BookDAO extends AbstractDAO implements
		ResultSetExtractor<List<Book>> {

	@Autowired
	JdbcTemplate template;

	@Autowired
	PublisherDAO pdao;

	public void create(Book b) throws SQLException {
		template.update(
				"insert into tbl_book (title, pubId) values (?, ?)",
				new Object[] { b.getTitle(), b.getPublisher().getPublisherId() });
	}

	public void update(Book b) throws SQLException {
		template.update("update tbl_book set title = (?) where bookId = (?)",
				new Object[] { b.getTitle(), b.getBookId() });
	}

	public void delete(Book b) throws SQLException {
		template.update("delete from tbl_book where bookId =?",
				new Object[] { b.getBookId() });
	}

	public Book readOne(int bookId) throws SQLException {
		List<Book> list = (List<Book>) template.query(
				"select * from tbl_book where bookId = ?",
				new Object[] { bookId }, this);

		if (list != null && list.size() > 0) {
			return list.get(0);
		} else {
			return null;
		}
	}

	public List<Book> readAll(int pageNo, int pageSize) throws SQLException {
		setPageNo(pageNo);
		return (List<Book>) template.query("select * from tbl_book  LIMIT ?,?",
				new Object[] { (pageNo - 1) * pageSize, pageSize }, this);
	}

	public Integer getCount() throws SQLException {
		return template.queryForObject("select count(*) from tbl_book",
				Integer.class);
	}

	public int getSearchCount(String searchString) {
		searchString = "%" + searchString + "%";
		return template.queryForObject(
				"SELECT count(*) from tbl_book where title Like ?",
				new Object[] { searchString }, Integer.class);

	}

	public List<Book> readByName(String searchString, int pageNo, int pageSize)
			throws SQLException {
		setPageNo(pageNo);
		String qString = "%" + searchString + "%";
		return (List<Book>) template.query(
				"select * from tbl_book where (title) like ?  LIMIT ?,?",
				new Object[] { qString, (pageNo - 1) * pageSize, pageSize },
				this);
	}

	public List<Book> readAllGenre(String searchString, int pageNo, int pageSize)
			throws SQLException {
		return (List<Book>) template
				.query("SELECT * FROM library.tbl_book b, library.tbl_book_genres g where g.genre_id = ? AND b.bookId=g.bookId  LIMIT ?,?",
						new Object[] { Integer.parseInt(searchString),
								(pageNo - 1) * pageSize, pageSize }, this);
	}

	@Override
	public List<Book> extractData(ResultSet rs) throws SQLException {
		List<Book> bList = new ArrayList<Book>();
		while (rs.next()) {
			Book b = new Book();
			b.setBookId(rs.getInt(1));
			b.setTitle(rs.getString(2));
			b.setPublisher(pdao.readOne(rs.getInt(3)));

			bList.add(b);
		}

		return bList;
	}
}