package tula.yard.games.miner.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

import tula.yard.games.miner.IGame;
import tula.yard.games.miner.service.ServiceOperation;

public class ChildWindow {
	//protected abstract String TITLE = null;
	public static final String CLOSE = "CLOSE";
	public static final String OK = "OK";
	public static final String CANCEL = "CANCEL";
	protected Shell window;
    private Shell parentWindow;
    protected IGame game;
    
	public ChildWindow( IGame game, Shell parentWindow, int width, int height, String titleCode) {
		this.game = game;
		window = new Shell(parentWindow, SWT.APPLICATION_MODAL | SWT.TITLE | SWT.CLOSE);
		window.setText(getLocalizedText(titleCode));
		this.parentWindow = parentWindow;
		setSize(width, height);
	}
	
	protected void setSize(int width, int height) {
		Point parentPos = parentWindow.getLocation();
		Point parentSize= parentWindow.getSize();
		Point pos = ServiceOperation.getMiddlePosition(parentPos.x, 
		parentPos.y, parentSize.x, parentSize.y, width, height);
		window.setLocation(pos);
		window.setSize(width, height);	
	}
	
	protected String getLocalizedText(String code) {
		return game.getLocale().getProperty(code, code);
		
	}
}
