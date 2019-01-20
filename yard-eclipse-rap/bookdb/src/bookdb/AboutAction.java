package bookdb;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IWorkbenchWindow;

public class AboutAction extends Action {

	private final IWorkbenchWindow window;

	public AboutAction(IWorkbenchWindow window) {
		super("About");
		setId(this.getClass().getName());
		this.window = window;
	}

	@Override
	public void run() {
		if (window != null) {
			String title = "About Book Database";
			String msg = "This application is a portfolio for the position of a Java programmer\r\n"
					+ "Powered by Eclipse Remote Application Platform and Tomcat\r\n" + "Tula, 2018\r\n" + "YarD";
			MessageDialog.openInformation(window.getShell(), title, msg);
		}
	}
}
