package com.gcit.training.lms.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;

import com.gcit.training.lms.entity.Genre;

public class GenreDAO extends AbstractDAO implements
		ResultSetExtractor<List<Genre>> {

	@Autowired
	JdbcTemplate template;

	public void create(Genre g) throws SQLException {
		template.update("insert into tbl_genre (genre_name) values (?)",
				new Object[] { g.getGenreName() });
	}

	public void update(Genre g) throws SQLException {
		template.update(
				"update tbl_genre set genre_name = ? where genre_id = ?",
				new Object[] { g.getGenreName(), g.getGenreId() });
	}

	public void delete(Genre g) throws SQLException {
		template.update("delete from tbl_genre where genre_id = ?",
				new Object[] { g.getGenreId() });
	}

	public Genre readOne(int genre_id) throws SQLException {
		List<Genre> list = (List<Genre>) template.query(
				"select * from tbl_genre where genre_id = ?",
				new Object[] { genre_id }, this);

		if (list != null && list.size() > 0) {
			return list.get(0);
		} else {
			return null;
		}
	}

	public List<Genre> readAll(int pageNo, int pageSize) throws SQLException {

		return (List<Genre>) template.query("select * from tbl_genre  LIMIT ?,?",
				new Object[] { (pageNo-1)*pageSize, pageSize }, this);
	}
	
	public Integer getCount() throws SQLException {
		return template.queryForObject("select count(*) from tbl_genre", Integer.class);
	}

	public List<Genre> readByName(String searchString, int pageNo, int pageSize) throws SQLException {
		String qString = "%" + searchString + "%";
		return (List<Genre>) template.query(
				"select * from tbl_genre where (genre_name) like ?  LIMIT ?,?",
				new Object[] {qString, (pageNo-1)*pageSize, pageSize }, this);
	}

	@Override
	public List<Genre> extractData(ResultSet rs) throws SQLException {
		List<Genre> gList = new ArrayList<Genre>();
		while (rs.next()) {
			Genre g = new Genre();
			g.setGenreId(rs.getInt("genre_id"));
			g.setGenreName(rs.getString("genre_name"));

			gList.add(g);
		}

		return gList;
	}
}