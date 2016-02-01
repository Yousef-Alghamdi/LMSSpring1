package com.gcit.training.lms.dao;

import java.sql.SQLException;

public class JdbcDemo {
	

	public static void main(String[] args) throws SQLException {

		/*
		 * 
		 * 
		 * a.setTitle("Sound of Silnce"); A.create(a);
		 * 
		 * 
		 * BookLoansDAO BC = new BookLoansDAO(); BookLoans bc = new BookLoans();
		 * 
		 * 
		 * BookCopiesDAO BC = new BookCopiesDAO(); BookCopies bc = new
		 * BookCopies();
		 * 
		 * BookLoansDAO BL = new BookLoansDAO(); BookLoans bl = new BookLoans();
		 * 
		 * BookDAO B = new BookDAO(); Book b = new Book();
		 * 
		 * 
		 * for (int i = 0; i < B.readAll().size(); i++) { Publisher p = new
		 * Publisher(); b = B.readAll().get(i); System.out.println(b.getBookId()
		 * + ": " + b.getTitle() + ": " + (b.getPublisher()).getPublisherName()
		 * + ": " + (b.getPublisher()).getPublisherAddress()); }
		 * System.out.println("\n");
		 * 
		 * for (int i = 0; i < BL.readAll().size(); i++) { bl =
		 * BL.readAll().get(i);
		 * 
		 * System.out.println(bl.getBook().getTitle() + ": " +
		 * bl.getLibraryBranch().getBranchName() + ": " +
		 * bl.getBorrower().getName() + ": " + bl.getDateOut() + ": " +
		 * bl.getDateIn() + ": " + bl.getDueDate()); }
		 * 
		 * 
		 * System.out.printf("%-10s %-10s %-10s %-10s %-10s %-10s\n", "tile",
		 * "|branch", "|borrower", "|dateOut", "|dateIN", "|dueDate");
		 * List<BookLoans> bll = new BookLoansDAO().readAll(); for (int i = 0; i
		 * < bll.size(); i++) {
		 * 
		 * System.out.printf("%-10s %-10s %-10s %-10s %-10s %-10s\n",
		 * bll.get(i).getBook().getTitle(), bll.get(i)
		 * .getLibraryBranch().getBranchName(), bll.get(i)
		 * .getBorrower().getName(), bll.get(i).getDateOut(),
		 * bll.get(i).getDateIn(), bll.get(i).getDueDate()+"\n"); }
		 * 
		 * for (int i = 0; i < BC.readAll().size(); i++) {
		 * 
		 * bc = BC.readAll().get(i);
		 * 
		 * System.out.println((bc.getBook()).getTitle() + ": " +
		 * (bc.getLibraryBranch()).getBranchName() + ": " + bc.getNoOfCopies());
		 * }
		 */

	}
}
