package tula.yard.games.miner.ui;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Shell;

import tula.yard.games.miner.IGame;
import tula.yard.games.miner.service.ServiceOperation;

public class AboutWindow extends ChildWindow {
    public static final int WINDOW_WIDTH = 400;
    public static final int WINDOW_HEIGHT = 350;
    public static final String TITLE = "ABOUT";
    public static final String ENCODING = "UTF-8";
    public static final String ABOUT_FILE = "_about.html";
    
	public AboutWindow(IGame game, Shell parentWindow) {		
		super(game, parentWindow, WINDOW_WIDTH, WINDOW_HEIGHT, TITLE);
		GridLayout gl = new GridLayout();
		gl.numColumns = 1;
		this.window.setLayout(gl);
		Browser info = new Browser(this.window, SWT.NONE);
		info.setLayoutData(new GridData(GridData.FILL_BOTH | GridData.GRAB_HORIZONTAL | GridData.GRAB_VERTICAL));
		try {
			info.setText(ServiceOperation.loadTextFromStream(this.getClass().getResourceAsStream("/" + Locale.getDefault() + ABOUT_FILE), StandardCharsets.UTF_8));
		} catch (Exception e) {
			try {
				info.setText(ServiceOperation.loadTextFromStream(this.getClass().getResourceAsStream("/en_EN" + ABOUT_FILE), StandardCharsets.UTF_8));
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				System.exit(1);
			}		
		}		
		Button btnClose = new Button(this.window, SWT.NONE);
		btnClose.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_CENTER));
		btnClose.setSize(60, 24);
		btnClose.setText(game.getLocale().getProperty(CLOSE, CLOSE));
		btnClose.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub
				window.close();				
			}

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub
				window.close();
			}});
		//info.setLayoutData(new RowData());
		this.window.open();
	}

}
