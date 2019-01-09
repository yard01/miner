package tula.yard.games.miner.ui;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Locale;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import tula.yard.games.miner.IGame;
import tula.yard.games.miner.service.ServiceOperation;
import tula.yard.games.miner.ui.StatisticWindow.Hero;

public class HelpWindow extends ChildWindow {
    public static final int WINDOW_WIDTH = 400;
    public static final int WINDOW_HEIGHT = 500;
    public static final String ENCODING = "UTF-8";
    public static final String HELP_FILE = "_help.html";
    public static final String TITLE = "HELP";
    
	public HelpWindow(IGame game, Shell parentWindow) {		
		super(game, parentWindow, WINDOW_WIDTH, WINDOW_HEIGHT, TITLE);
		GridLayout gl = new GridLayout();
		gl.numColumns = 1;
		this.window.setLayout(gl);
		Browser info = new Browser(this.window, SWT.NONE);
		info.setLayoutData(new GridData(GridData.FILL_BOTH | GridData.GRAB_HORIZONTAL | GridData.GRAB_VERTICAL));
		try {
			info.setText(ServiceOperation.loadTextFromStream(this.getClass().getResourceAsStream("/" + Locale.getDefault() + HELP_FILE), StandardCharsets.UTF_8));
		} catch (Exception e) {
			try {
				info.setText(ServiceOperation.loadTextFromStream(this.getClass().getResourceAsStream("/en_EN" + HELP_FILE), StandardCharsets.UTF_8));
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
