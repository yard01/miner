package bookdb.ui;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;
import org.eclipse.ui.PlatformUI;

import bookdb.View;

public class AdminPerspective implements IPerspectiveFactory {
	public static String ID = "bookdb.ui.perspective.dbwriter";
	
	class CommandList implements ICommandList {

		@Override
		public List<ToolBarCommand> getCommandList() {
			// TODO Auto-generated method stub
			List<ToolBarCommand> list = new ArrayList<ToolBarCommand>();
			list.add(new ToolBarCommand(ICommandList.cmdNew, "New", "icons/cmd_new.gif"));
			list.add(new ToolBarCommand(ICommandList.cmdEdit, "Edit", "icons/cmd_edit.gif"));
			list.add(new ToolBarCommand(ICommandList.cmdDelete, "Delete", "icons/cmd_delete.gif"));
			list.add(new ToolBarCommand(ICommandList.cmdView, "View", "icons/cmd_view.gif"));
			list.add(new ToolBarCommand(ICommandList.cmdRefresh, "Refresh", "icons/cmd_refresh.gif"));	
			list.add(new ToolBarCommand(ICommandList.cmdFilter, "Filter", "icons/cmd_filter.gif"));	
	
			return list;
		}
		
	}
	@Override
	public void createInitialLayout(IPageLayout layout) {
		PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell().setData(ICommandList.class.getCanonicalName(),new CommandList());

		// TODO Auto-generated method stub
		String editorArea = layout.getEditorArea();
		layout.setEditorAreaVisible(false);

		layout.setFixed(true);	
		
		IFolderLayout folder = layout.createFolder("booklist", IPageLayout.TOP, 0.5f, editorArea);
		folder.addPlaceholder(View.ID + ":*");
		folder.addView(View.ID);		
		layout.setFixed(false);	
		

	}

}
