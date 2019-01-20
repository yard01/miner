package bookdb.widget;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.nebula.widgets.pagination.IPageLoader;
import org.eclipse.nebula.widgets.pagination.PageableController;
import org.eclipse.nebula.widgets.pagination.collections.PageListHelper;
import org.eclipse.nebula.widgets.pagination.collections.PageResult;
import org.eclipse.ui.PlatformUI;

import com.github.yard01.bookdb.orm.DBCommandProcessor;
import com.github.yard01.bookdb.orm.SAT_Book;

public class BookDBWidgetServices implements IPageLoader<PageResult<BookDBWidget>> {
   
    
	//private static final BookDBWidgetServices INSTANCE = new BookDBWidgetServices();
	private final List<BookDBWidget> widgets;
	private DBCommandProcessor dbcp;
	
	public BookDBWidgetServices() {
		this.dbcp =  (DBCommandProcessor)PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell().getData(DBCommandProcessor.class.getCanonicalName());
		this.widgets = new ArrayList<BookDBWidget>();
		load();
	}

	private void load() {
		
		List<SAT_Book> list = dbcp.getBooks();
		for (SAT_Book book:list) {
		// TODO Auto-generated method stub
			this.widgets.add(new BookDBWidget(book, dbcp));
		}	
	}

	@Override
	public PageResult<BookDBWidget> loadPage(PageableController controller) {
		return PageListHelper.createPage(widgets, controller);
		
	}
	

}
