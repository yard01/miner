package bookdb.ui;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.nebula.widgets.pagination.table.PageableTable;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
//import org.osgi.service.event.Event;
//import org.osgi.service.event.EventHandler;
import bookdb.View;
import bookdb.ui.windows.BookFilterWindow;
import bookdb.ui.windows.BookFormWindow;
import bookdb.widget.BookDBWidget;
import bookdb.widget.BookDBWidgetFilter;

public class ToolBarCommand {
	public static final String MSG_INFO_TYPE = "Information";
	public static final String MSG_QUEST_TYPE = "Question";
	
	public static final String MSG_CONFIRM_DELETE =  "Do You want to delete the entry";
	public static final String MSG_NO_SELECT = "Please select the item!";
	
	private String ID;
	private String name;
	private String icon;
	private PageableTable assignedTable;
	private BookDBWidget bookdb; // = (BookDBWidget)assignedTable.getWidget().getSelection()[0].getData();
	private int viewReportCounter = 1;
	
	
	public ToolBarCommand(String ID, String name, String icon) {
		this.ID = ID;
		this.name = name;
		this.icon = icon;		
	}

	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public PageableTable getAssignedTable() {
		return assignedTable;
	}

	public void setAssignedTable(PageableTable assignedTable) {
		this.assignedTable = assignedTable;
	}
    
	protected void newItem() {
		
		BookFormWindow bf = new BookFormWindow(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell()
                ,new BookDBWidget()
                ,assignedTable.getWidget().getColumns());
		bf.open();

	}
	
	protected void editItem() {
		if (bookdb == null) return;
		BookFormWindow bf = new BookFormWindow(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell()
				                   ,bookdb
				                   ,assignedTable.getWidget().getColumns());
		bf.open();

	}
	
	protected void deleteItem() {
		if (bookdb == null) return;
		if (MessageDialog.openQuestion(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), 
				MSG_QUEST_TYPE, MSG_CONFIRM_DELETE + "\n\r" + bookdb.getAuthor() + ", " +bookdb.getName() + "?")) { 
			bookdb.delete()	;
			refresh();
		}

	}
	
	protected void refresh() {
		((View)PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell().getData(View.class.getCanonicalName())).refresh();
    }
    
	private void filter() {
		// TODO Auto-generated method stub
		BookDBWidgetFilter filter = (BookDBWidgetFilter)PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell().getData(BookDBWidgetFilter.class.getCanonicalName());
		if (filter == null) filter = new BookDBWidgetFilter();
		BookFilterWindow bf = new BookFilterWindow(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell()
                ,filter
                ,assignedTable.getWidget().getColumns());
		bf.open();

	}

	public void view() {
		try {
			if (bookdb == null)		
				MessageDialog.openInformation(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), MSG_INFO_TYPE, MSG_NO_SELECT);
			else {
				ReportView view = (ReportView)PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(ReportView.ID, "" +  viewReportCounter++, 1);
				view.setBook(this.bookdb);
			}	
		} catch (PartInitException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public void execute() {
		// TODO Auto-generated method stub
		if (assignedTable.getWidget().getSelectionIndex() >= 0) 
			bookdb = (BookDBWidget)assignedTable.getWidget().getSelection()[0].getData();

		
		switch (ID) {
		case ICommandList.cmdNew: newItem();
				break;
		case ICommandList.cmdEdit: editItem();
			break;
		case ICommandList.cmdDelete: deleteItem(); 
				break;
		case ICommandList.cmdRefresh: refresh(); break;

		case ICommandList.cmdView: view(); break;
		
		case ICommandList.cmdFilter: filter(); break;
					
		}
		
	}
	
}
