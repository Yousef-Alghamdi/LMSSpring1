package com.gcit.training.lms.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;

import com.gcit.training.lms.entity.LibraryBranch;

public class LibraryBranchDAO extends AbstractDAO implements
		ResultSetExtractor<List<LibraryBranch>> {

	@Autowired
	JdbcTemplate template;

	public void create(LibraryBranch lb) throws SQLException {
		template.update(
				"insert into tbl_library_branch (branchName, branchAddress) values (?,?)",
				new Object[] { lb.getBranchName(), lb.getBranchAddress() });
	}

	public void update(LibraryBranch lb) throws SQLException {
		template.update(
				"update tbl_library_branch set branchName = ?, branchAddress = ? where branchId = ?",
				new Object[] { lb.getBranchName(), lb.getBranchAddress(),
						lb.getBranchId() });
	}

	public void delete(LibraryBranch lb) throws SQLException {
		template.update("delete from tbl_library_branch where branchId = ?",
				new Object[] { lb.getBranchId() });
	}

	public LibraryBranch readById(int branchId) throws SQLException {
		List<LibraryBranch> list = (List<LibraryBranch>) template.query(
				"select * from tbl_library_branch where branchId = ?",
				new Object[] { branchId }, this);

		if (list != null && list.size() > 0) {
			return list.get(0);
		} else {
			return null;
		}
	}

	public List<LibraryBranch> readAll(int pageNo, int pageSize)
			throws SQLException {
		setPageNo(pageNo);
		return (List<LibraryBranch>) template.query(
				"select * from tbl_library_branch  LIMIT ?,?", new Object[] {
						(pageNo - 1) * pageSize, pageSize }, this);
	}

	public Integer getCount() throws SQLException {
		return template.queryForObject(
				"select count(*) from tbl_library_branch", Integer.class);
	}

	public List<LibraryBranch> readByName(String searchString, int pageNo,
			int pageSize) throws SQLException {
		setPageNo(pageNo);
		String qString = "%" + searchString + "%";
		return (List<LibraryBranch>) template
				.query("select * from tbl_library_branch where branchName like ?  LIMIT ?,?",
						new Object[] { qString, (pageNo - 1) * pageSize,
								pageSize }, this);
	}
	public int getSearchCount(String searchString) {
		searchString = "%" + searchString + "%";
		return template
				.queryForObject(
						"SELECT count(*) from tbl_library_branch where branchName Like ?",
						new Object[] { searchString }, Integer.class);
	}

	public List<LibraryBranch> readByBook(String searchString, Integer pageNo,
			Integer pageSize) {

		return (List<LibraryBranch>) template
				.query("select * from  library.tbl_book_copies bc,  library.tbl_library_branch lb"
						+ " where (lb.branchId) = (bc.branchId) and (bc.bookId) = ?"
						+ " order by (lb.branchId) LIMIT ?,?",
						new Object[] { searchString, (pageNo - 1) * pageSize,
								pageSize }, this);
	}

	@Override
	public List<LibraryBranch> extractData(ResultSet rs) throws SQLException {
		List<LibraryBranch> lbList = new ArrayList<LibraryBranch>();
		while (rs.next()) {
			LibraryBranch lb = new LibraryBranch();
			lb.setBranchId(rs.getInt("branchId"));
			lb.setBranchName(rs.getString("branchName"));
			lb.setBranchAddress(rs.getString("branchAddress"));

			lbList.add(lb);
		}

		return lbList;
	}
}