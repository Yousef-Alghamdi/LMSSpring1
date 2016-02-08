package com.gcit.training.lms.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;

import com.gcit.training.lms.entity.Borrower;

public class BorrowerDAO extends AbstractDAO implements
		ResultSetExtractor<List<Borrower>> {

	@Autowired
	JdbcTemplate template;

	public void create(Borrower bor) throws SQLException {
		template.update(
				"insert into tbl_borrower (name,address,phone) values (?,?,?)",
				new Object[] { bor.getName(), bor.getAddress(), bor.getPhone() });
	}

	public void update(Borrower bor) throws SQLException {
		template.update(
				"update tbl_borrower set name = ? , address = ? , phone = ? where cardNo = ?",
				new Object[] { bor.getName(), bor.getAddress(), bor.getPhone(),
						bor.getCardNo() });
	}

	public void delete(Borrower bor) throws SQLException {
		template.update("delete from tbl_borrower where cardNo = ?",
				new Object[] { bor.getCardNo() });
	}

	public Borrower readById(int cardNo) throws SQLException {
		List<Borrower> list = (List<Borrower>) template.query(
				"select * from tbl_borrower where cardNo = ?",
				new Object[] { cardNo }, this);
		if (list != null && list.size() > 0) {
			return list.get(0);
		} else {
			return null;
		}
	}

	public List<Borrower> readByName(String searchString, int pageNo,
			int pageSize) throws SQLException {
		String qString = "%" + searchString + "%";
		return (List<Borrower>) template.query(
				"select * from tbl_borrower where (name) like ?  LIMIT ?,?",
				new Object[] { qString, (pageNo - 1) * pageSize, pageSize },
				this);
	}

	public List<Borrower> readByAddress(String searchString)
			throws SQLException {
		String qString = "%" + searchString + "%";
		return (List<Borrower>) template.query(
				"select * from tbl_borrower where (address) like ?",
				new Object[] { qString }, this);
	}

	public List<Borrower> readByPhone(String searchString) throws SQLException {
		String qString = "%" + searchString + "%";
		return (List<Borrower>) template.query(
				"select * from tbl_borrower where (phonne) like ?",
				new Object[] { qString }, this);
	}

	public List<Borrower> readAll(int pageNo, int pageSize) throws SQLException {
		setPageNo(pageNo);
		return (List<Borrower>) template.query(
				"select * from tbl_borrower  LIMIT ?,?", new Object[] {
						(pageNo - 1) * pageSize, pageSize }, this);
	}

	public Integer getCount() throws SQLException {
		return template.queryForObject("select count(*) from tbl_borrower",
				Integer.class);
	}

	public int getSearchCount(String searchString) {
		searchString = "%" + searchString + "%";
		return template.queryForObject(
				"SELECT count(*) from tbl_borrower where name Like ?",
				new Object[] { searchString }, Integer.class);
	}

	@Override
	public List<Borrower> extractData(ResultSet rs) throws SQLException {
		List<Borrower> borList = new ArrayList<Borrower>();
		while (rs.next()) {
			Borrower bor = new Borrower();
			bor.setCardNo(rs.getInt("cardNo"));
			bor.setName(rs.getString("name"));
			bor.setAddress(rs.getString("address"));
			bor.setPhone(rs.getString("phone"));

			borList.add(bor);
		}

		return borList;
	}
}
