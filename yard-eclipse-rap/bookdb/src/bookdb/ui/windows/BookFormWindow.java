package bookdb.ui.windows;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;

import bookdb.View;
import bookdb.widget.BookDBWidget;

public class BookFormWindow extends ApplicationWindow {

	protected BookDBWidget bookdb;
	protected TableColumn[] tableColumns;
	protected List<Text> properties = new ArrayList<Text>();
	protected Button btnClose;
	protected Button btnSave;

	public BookFormWindow(Shell parentShell, BookDBWidget bookdb, TableColumn[] tableColumns) {
		super(parentShell);
		// TODO Auto-generated constructor stub
		this.bookdb = bookdb;
		this.tableColumns = tableColumns;
		this.setShellStyle(SWT.APPLICATION_MODAL | SWT.DIALOG_TRIM);

	}

	@Override
	protected Control createContents(Composite parent) {
		Composite composite = new Composite(parent, SWT.FILL);
		GridLayout gl = new GridLayout();
		gl.numColumns = 2;
		composite.setLayout(gl);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		for (TableColumn tc : tableColumns) {
			Label label = new Label(composite, SWT.NONE);
			label.setText(tc.getText());
			Text text = new Text(composite, SWT.BORDER);
			properties.add(text);
			text.setLayoutData(gd);
			text.setData("set", tc.getData("set"));
			Method get = (Method) tc.getData("get");

			if (get != null)
				try {
					text.setText((String) get.invoke(bookdb));
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
			// text.setText(text);
		}
		btnSave = new Button(composite, SWT.FLAT);
		btnSave.setText("Save");
		btnSave.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				saveBook();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub

			}
		});

		btnClose = new Button(composite, SWT.FLAT);
		btnClose.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				closeWindow();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub

			}
		});
		btnClose.setText("Close");

		this.getShell().setSize(550, 600);
		return composite;
	}

	protected void saveBook() {
		for (Text text : properties) {
			Method method = (Method) text.getData("set");
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
		bookdb.save();
		((View) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell().getData(View.class.getCanonicalName()))
				.refresh();
		closeWindow();
	}

	protected void closeWindow() {
		close();
	}

}
