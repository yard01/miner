package com.github.yard01.bookdb.orm;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.jdbc.Work;

import com.github.yard01.bookdb.orm.filter.SAT_BookFilter;
import com.github.yard01.bookdb.orm.security.BookDBRole;

public class DBCommandProcessor {
	Logger logger = Logger.getLogger(DBCommandProcessor.class);

	public final static String LOGIN_PROPERTY = "hibernate.connection.username";
	public final static String PASSWORD_PROPERTY = "hibernate.connection.password";
	private SessionFactory sessionFactory;
	private String dbrole = "dbreader";
	private Session currentSession;
	private String DEFAULT_BOOK_CODE = "B00001";
	private String filterString = "";
	private SAT_BookFilter bookFilter;
	private String login;
	
	public String getLogin() {
		return login;
	}
	
	public DBCommandProcessor() {
		
	}
	
	public DBCommandProcessor(String login, String password) {
		this.login = login;
		
		Configuration configuration = new Configuration();
		configuration.setProperty(LOGIN_PROPERTY, login);
		configuration.setProperty(PASSWORD_PROPERTY, password);
		configuration.configure();
		//
		sessionFactory = configuration.buildSessionFactory();
		Session session = openSession(); 		
		logger.info("User " + login+ ": auhtentification is successful");
		logger.info("User " + login+ ": auhthorization attempt");

		BookDBRole role = (BookDBRole)session.createQuery("select dbuser.role from BookDBUser dbuser where dbuser.name = :login").setParameter("login", login).list().get(0);
		dbrole = role.getPerspectiveID(); // user.getRole().getPerspectiveID();
		logger.info("User " + login + ": the user has been auhthorized as " + dbrole);
		session.close();
		
	}
	public Session openSession() {
		if (currentSession != null && currentSession.isOpen()) return currentSession; 
		currentSession =  sessionFactory.openSession();
		currentSession.doWork(new Work() {
		    @Override
		    public void execute(Connection connection) throws SQLException {		       
		    	connection.createStatement().execute("set role " + dbrole);
		    }
		});
		logger.info("User " + login +" ("+ dbrole +")"+": the new session is open");

		return currentSession;
	}
	public void execute() {
	  	Configuration configuration = (new Configuration()).configure();		    	
    	SessionFactory sessionFactory = configuration.buildSessionFactory();

	}
	public List<SAT_Book> getBooks() {
		currentSession = openSession();			
		List list = currentSession.createQuery("select book from SAT_Book book"
				+ " where 1=1 " + filterString).list();
		logger.info("User " + login +" ("+ dbrole +"): "+list.size()+" items was selected from SAT_BOOK");

		return list;
	}
	
	public List getBooksWithHub() {
		openSession();
			
		List list = currentSession.createQuery("select book, book.hub_item from SAT_Book book").list();
		logger.info("User " + login +" ("+ dbrole +"): "+list.size()+" items was saved from SAT_BOOK with HUB_ITEM");

		return list;
	}

	
	public void closeCurrentSession() {
		if (currentSession.isOpen()) 
			currentSession.close();
		logger.info("User " + login +" ("+ dbrole +")"+": the current session is closed");

		
	}
	
	protected HUB_Item getDefaultHUB_Item() {
		openSession();
		List list = currentSession.createQuery("select item from HUB_Item item where item.code = :code").setParameter("code", DEFAULT_BOOK_CODE).list();
		return (HUB_Item)list.get(0);
	}
	
	public void saveBook(SAT_Book book) {
		// TODO Auto-generated method stub
		openSession();
		if (book.hub_Item == null) book.setHub_Item(getDefaultHUB_Item());
		//currentSession.save(book);
		currentSession.beginTransaction();
		currentSession.save(book);
		currentSession.getTransaction().commit();
		logger.info("User " + login +" ("+ dbrole +"): the item was saved into SAT_BOOK");
		closeCurrentSession();
	}

	public void delete(SAT_Book book) {
		// TODO Auto-generated method stub
		openSession();
		currentSession.beginTransaction();
		currentSession.delete(book);
		currentSession.getTransaction().commit();
		logger.info("User " + login +" ("+ dbrole +"): the item (" + book.ID + ") was deleted from SAT_BOOK");
		
	
		closeCurrentSession();
		
	}
	 public String getRole() {
		 return dbrole;
	 }

	public void setBookFilter(SAT_BookFilter filter) {
		this.bookFilter = filter;
		if (filter == null) {
			this.filterString = "";
			logger.info("User " + login +" ("+ dbrole +"): the filter is reset");
		}
		else {
			this.filterString = filter.getFilterString("book");
			logger.info("User " + login +" ("+ dbrole +"):  set the filter, " + this.filterString);
		}
	}
}
