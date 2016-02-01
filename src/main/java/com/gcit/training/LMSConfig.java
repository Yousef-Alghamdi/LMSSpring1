package com.gcit.training;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import com.gcit.training.lms.dao.*;
import com.gcit.training.lms.service.AdministrativeService;

@Configuration
public class LMSConfig {
	private static String driverName = "com.mysql.jdbc.Driver";
	private static String url = "jdbc:mysql://localhost:3306/library";
	private static String username = "root";
	private static String password = "admin";

	@Bean
	public BasicDataSource datasource() {
		BasicDataSource ds = new BasicDataSource();
		ds.setDriverClassName(driverName);
		ds.setUrl(url);
		ds.setUsername(username);
		ds.setPassword(password);

		return ds;
	}

	@Bean
	public PlatformTransactionManager txManager() {
		DataSourceTransactionManager tx = new DataSourceTransactionManager();
		tx.setDataSource(datasource());
		return tx;
	}

	@Bean
	public JdbcTemplate template() {
		JdbcTemplate template = new JdbcTemplate();
		template.setDataSource(datasource());

		return template;
	}

	@Bean
	public AdministrativeService adminService() {
		AdministrativeService adminService = new AdministrativeService();
		return adminService;
	}

	// add all DAOS
	@Bean
	public AuthorDAO aDAO() {
		AuthorDAO adao = new AuthorDAO();
		return adao;
	}
	
	@Bean
	public BookCopiesDAO bcDAO() {
		BookCopiesDAO bcdao = new BookCopiesDAO();
		return bcdao;
	}
	
	@Bean
	public BookDAO bDAO() {
		BookDAO bdao = new BookDAO();
		return bdao;
	}
	
	@Bean
	public BookLoansDAO blDAO() {
		BookLoansDAO bldao = new BookLoansDAO();
		return bldao;
	}
	
	@Bean
	public BorrowerDAO borDAO() {
		BorrowerDAO bordao = new BorrowerDAO();
		return bordao;
	}
	
	@Bean
	public GenreDAO gDAO() {
		GenreDAO gdao = new GenreDAO();
		return gdao;
	}

	@Bean
	public LibraryBranchDAO lbDAO() {
		LibraryBranchDAO lbdao = new LibraryBranchDAO();
		return lbdao;
	}
	
	@Bean
	public PublisherDAO pDAO() {
		PublisherDAO pdao = new PublisherDAO();
		return pdao;
	}

}
