package com.gcit.training.lms.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;

import com.gcit.training.lms.entity.Publisher;

public class PublisherDAO extends AbstractDAO implements
		ResultSetExtractor<List<Publisher>> {

	@Autowired
	JdbcTemplate template;

	public void create(Publisher p) throws SQLException {
		template.update(
				"insert into tbl_publisher (publisherName, publisherAddress) values (?)",
				new Object[] { p.getPublisherName(), p.getPublisherAddress() });
	}

	public void update(Publisher p) throws SQLException {
		template.update(
				"update tbl_publisher set publisherName = ?, publisherAddress = ? where publisherId = ?",
				new Object[] { p.getPublisherName(), p.getPublisherAddress(),
						p.getPublisherId() });
	}

	public void delete(Publisher p) throws SQLException {
		template.update("delete from tbl_publisher where publisherId = ?",
				new Object[] { p.getPublisherId() });
	}

	public Publisher readOne(int publisherId) throws SQLException {
		List<Publisher> pubList = (List<Publisher>) template.query(
				"select * from tbl_publisher where publisherId = ?",
				new Object[] { publisherId }, this);

		if (pubList != null && pubList.size() > 0) {
			return pubList.get(0);
		} else {
			return null;
		}
	}

	public List<Publisher> readAll() throws SQLException {
		
		return (List<Publisher>) template.query("select * from tbl_publisher",
				this);
	}
	public Integer getCount() throws SQLException {
		return template.queryForObject("select count(*) from tbl_author", Integer.class);
	}

	public List<Publisher> readByName(String searchString, int pageNo) throws SQLException {
		setPageNo(pageNo);
		String qString = "%" + searchString + "%";
		return (List<Publisher>) template.query(
				"select * from tbl_publisher where publisherName like ?",
				new Object[] { qString }, this);
	}

	@Override
	public List<Publisher> extractData(ResultSet rs) throws SQLException {
		List<Publisher> aList = new ArrayList<Publisher>();
		while (rs.next()) {
			Publisher a = new Publisher();
			a.setPublisherId(rs.getInt("publisherId"));
			a.setPublisherName(rs.getString("publisherName"));
			a.setPublisherAddress(rs.getString("publisherAddress"));

			aList.add(a);
		}

		return aList;
	}

}