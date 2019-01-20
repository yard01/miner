package bookdb.widget;

import org.eclipse.ui.PlatformUI;
import com.github.yard01.bookdb.orm.filter.SAT_BookFilter;

import bookdb.View;

public class BookDBWidgetFilter extends BookDBWidget {
	
	public BookDBWidgetFilter() {
		this.initDBCommandProcessor();
		this.book = new SAT_BookFilter();
	}
	public void setFilter() {
		PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell().setData(BookDBWidgetFilter.class.getCanonicalName(), this);
		this.initAssignedObject();
		this.dbcp.setBookFilter((SAT_BookFilter)this.book);
		((View)PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell().getData(View.class.getCanonicalName())).refresh();
	}
	
	public void resetFilter() {
		this.dbcp.setBookFilter(null);
		((View)PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell().getData(View.class.getCanonicalName())).refresh();

	}
}	