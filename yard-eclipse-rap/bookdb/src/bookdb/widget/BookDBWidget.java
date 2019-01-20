package bookdb.widget;

import org.eclipse.ui.PlatformUI;

import com.github.yard01.bookdb.orm.DBCommandProcessor;
import com.github.yard01.bookdb.orm.SAT_Book;

public class BookDBWidget {
	protected DBCommandProcessor dbcp;
	protected SAT_Book book;
	String 	author = "";
	String	name = "";
	String 	series = "";
	String 	genre = "";
	String 	publisher = "";
	String 	note = "";

	public SAT_Book getBook() {
		return book;
	}
	public void setBook(SAT_Book book) {
		this.book = book;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public BookDBWidget(SAT_Book book, DBCommandProcessor dbcp) {
		this.author = book.getAuthor();
		this.name = book.getName();
		this.series = book.getSeries();
		this.genre = book.getGenre();
		this.publisher = book.getPublisher();
		this.note = book.getNote();
		this.book = book;
		this.dbcp = dbcp;
	}
	
	protected void initDBCommandProcessor() {
		this.dbcp = (DBCommandProcessor)PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell().getData(DBCommandProcessor.class.getCanonicalName());

	}
	
	public BookDBWidget() {
		this.book = new SAT_Book();
		initDBCommandProcessor();	
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	protected void initAssignedObject() {
		book.setAuthor(this.author);
		book.setName(this.name);
		book.setSeries(this.series);
		book.setGenre(this.genre);
		book.setPublisher(this.publisher);
		book.setNote(this.note);
	}
	
	public String getSeries() {
		return series;
	}
	public void setSeries(String series) {
		this.series = series;
	}
	public String getGenre() {
		return genre;
	}
	public void setGenre(String genre) {
		this.genre = genre;
	}
	public String getPublisher() {
		return publisher;
	}
	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public void save() {
		// TODO Auto-generated method stub
		initAssignedObject();
		dbcp.saveBook(this.book);
	}
	public void delete() {
		// TODO Auto-generated method stub
		dbcp.delete(this.book);
	}
	
}
