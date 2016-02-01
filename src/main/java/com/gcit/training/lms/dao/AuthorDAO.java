package com.gcit.training.lms.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;

import com.gcit.training.lms.entity.Author;

public class AuthorDAO extends AbstractDAO implements ResultSetExtractor<List<Author>>{
	
	@Autowired
	JdbcTemplate template;
	
	@Autowired
	PublisherDAO pdao;

	public void create(Author a) throws SQLException {
		template.update("insert into tbl_author (authorName) values (?)",
				new Object[] { a.getAuthorName() });
	}

	public void update(Author a) throws SQLException {
		template.update("update tbl_author set authorName = ? where authorId = ?",
				new Object[] { a.getAuthorName(), a.getAuthorId() });
	}

	public void delete(Author a) throws SQLException {
		template.update("delete from tbl_author where authorId = ?",
				new Object[] { a.getAuthorId() });
	}

	public Author readOne(int authorId) throws SQLException {
		List<Author> list = (List<Author>) template.query(
				"select * from tbl_author where authorId = ?",
				new Object[] { authorId }, this);

		if (list != null && list.size() > 0) {
			return list.get(0);
		} else {
			return null;
		}
	}

	public List<Author> readAll(int pageNo, int pageSize) throws SQLException {
		setPageNo(pageNo);
		return (List<Author>) template.query("select * from tbl_author  LIMIT ?,?",
				new Object[] { (pageNo-1)*pageSize, pageSize }, this);
	}
	
	public Integer getCount() throws SQLException {
		return template.queryForObject("select count(*) from tbl_author", Integer.class);
	}

	public List<Author> readByName(String searchString, int pageNo, int pageSize) throws SQLException {
		setPageNo(pageNo);
		String qString = "%" + searchString + "%";
		return (List<Author>) template.query(
				"select * from tbl_author where authorName like ?  LIMIT ?,?",
				new Object[] {qString, (pageNo-1)*pageSize, pageSize }, this);
	}

	@Override
	public List<Author> extractData(ResultSet rs) throws SQLException {
		List<Author> aList = new ArrayList<Author>();
		while (rs.next()) {
			Author a = new Author();
			a.setAuthorId(rs.getInt("authorId"));
			a.setAuthorName(rs.getString("authorName"));

			aList.add(a);
		}

		return aList;
	}
}
