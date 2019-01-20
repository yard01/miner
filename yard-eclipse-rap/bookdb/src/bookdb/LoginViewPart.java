package bookdb;

import org.apache.log4j.Logger;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import com.github.yard01.bookdb.orm.DBCommandProcessor;

public class LoginViewPart extends ViewPart {
	Logger logger = Logger.getLogger(LoginViewPart.class);
	private Label errorInfo;
	public static final String ID = "bookdb.viewLOGIN";
	public static final String AUTHONTIFICATION_ERROR = "Your Login or Password was wrong";

	public LoginViewPart() {
		// TODO Auto-generated constructor stub
		// PropertyConfigurator pc;

	}

	@Override
	public void createPartControl(Composite parent) {

		// TODO Auto-generated method stub
		parent.setLayout(null);
		Composite top = new Composite(parent, SWT.NONE);
		top.setSize(320, 200);
		// top.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_GRAY));

		GridLayout layout = new GridLayout();
		layout.marginHeight = 5;
		layout.marginWidth = 10;
		layout.numColumns = 2;

		top.setLayout(layout);
		Font boldFont = JFaceResources.getFontRegistry().getBold(JFaceResources.DEFAULT_FONT);

		errorInfo = new Label(top, SWT.NONE);
		errorInfo.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_RED));
		GridData g = new GridData();
		g.horizontalSpan = 2;
		errorInfo.setLayoutData(g);

		Label l = new Label(top, SWT.NONE);

		l.setText("Login:");
		l.setFont(boldFont);

		Text loginText = new Text(top, SWT.BORDER);
		loginText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		l = new Label(top, SWT.WRAP);
		l.setText("Password:");
		l.setFont(boldFont);
		Text passwordText = new Text(top, SWT.PASSWORD | SWT.BORDER);

		passwordText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		Button btn = new Button(top, SWT.FLAT);

		btn.setText("Sign in");
		// btn.setLayoutData(gd);
		parent.pack();

		btn.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event e) {
				switch (e.type) {
				case SWT.Selection:

					login(loginText.getText(), passwordText.getText());
					break;
				}
			}
		});
		// btn.addListener(, listener);

	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

	public void login(String login, String password) {
		logger.info("User " + login + ": authentification attempt");
		IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();

		try {
			DBCommandProcessor dbcp = new DBCommandProcessor(login, password);
			page.hideView(page.findView(ID));
			window.getShell().setData(dbcp.getClass().getCanonicalName(), dbcp);
			String fullPerspectiveID = Perspective.PERSPECTIVE_PREFIX + dbcp.getRole();
			PlatformUI.getWorkbench().showPerspective(fullPerspectiveID, window);
			logger.info("User " + login + ": authorization is successful");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			errorInfo.setText(AUTHONTIFICATION_ERROR);
			errorInfo.pack();
			e.printStackTrace();
			logger.error("User " + login + ": Authorization error");
		}
	}
}
