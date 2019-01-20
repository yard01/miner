package bookdb.ui.windows;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;

import bookdb.widget.BookDBWidget;
import bookdb.widget.BookDBWidgetFilter;

public class BookFilterWindow extends BookFormWindow {

	public BookFilterWindow(Shell parentShell, BookDBWidget bookdb, TableColumn[] tableColumns) {
		super(parentShell, bookdb, tableColumns);
		// TODO Auto-generated constructor stub
		
	}
	protected Control createContents(Composite parent) {
		Composite composite = (Composite) super.createContents(parent);
		this.getShell().setText("Filter");
		this.btnSave.setText("Set Filter");
		this.btnClose.setText("Reset Filter");
		return composite;
	}
	@Override
	public void saveBook() {
		for (Text text : properties) {
			Method method = (Method)text.getData("set");
			try {
				method.invoke(bookdb, text.getText());
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		//PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell().setData(BookDBWidgetFilter.class.getCanonicalName(), bookdb);
		((BookDBWidgetFilter)bookdb).setFilter();
		this.close();
	}
	
	@Override
	public void closeWindow() {
		((BookDBWidgetFilter)bookdb).resetFilter();
		this.close();
	}
}
	
