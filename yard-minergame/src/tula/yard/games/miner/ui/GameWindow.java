package tula.yard.games.miner.ui;

import java.util.Properties;
import java.util.Vector;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MenuEvent;
import org.eclipse.swt.events.MenuListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;

import tula.yard.games.miner.IGame;
import tula.yard.games.miner.IGameplay;
import tula.yard.games.miner.gameplay.MinerGameplay;
import tula.yard.games.miner.service.ServiceOperation;

public class GameWindow {
	
	protected static final String X_PROPERTY = "X";
	protected static final String Y_PROPERTY = "Y";
	
	protected static final String GAME_MENUKEY = "mnGAME";
	protected static final String NEWGAME_MENUKEY = "mnNEWGAME";
	protected static final String PREFERENCES_MENUKEY = "mnPREFERENCES";
	protected static final String STATISTIC_MENUKEY = "mnSTATISTIC";	
	protected static final String EXIT_MENUKEY = "mnEXIT";
	protected static final String ABOUT_MENUKEY = "mnABOUT";	
	protected static final String HELP_MENUKEY = "mnHELP";
	protected static final String GETHELP_MENUKEY = "mnGETHELP";
	protected static final String TITLE_KEY = "TITLE";
	
	private Shell shell;
	private boolean flClose = false;
	protected IGame game; 
	protected IGameplay gameplay;
	
	private Menu menuBar, 
	             gameMenu, 
	             helpMenu;
	
	private MenuItem gameMenuHeader, 
	                 helpMenuHeader,
	                 someMenuItem,
	                 gameExitItem, 
	                 helpGetHelpItem;
	
	private Properties properties;
	private Properties defProperties;
	private Properties locale; 
	
    class menuSelectionListener implements SelectionListener {
    	
   	public void actionWork(SelectionEvent event) {
   		switch ((String)event.widget.getData()) {
   		case EXIT_MENUKEY : flClose = true; break;
   		case NEWGAME_MENUKEY : game.restart(); break;
   		case PREFERENCES_MENUKEY : new PreferencesWindow(game, shell); break;
  		case STATISTIC_MENUKEY : new StatisticWindow(game, shell); break;
 		case ABOUT_MENUKEY : new AboutWindow(game, shell); break;
 		case GETHELP_MENUKEY : new HelpWindow(game, shell); break;  		   		
   		}
   	}
				@Override
				public void widgetDefaultSelected(SelectionEvent arg0) {
					// TODO Auto-generated method stub
					actionWork(arg0);
					//System.out.println("default");
				}

				@Override
				public void widgetSelected(SelectionEvent arg0) {
					// TODO Auto-generated method stub
					//System.out.println("widget");
					actionWork(arg0);

					
				}

    }
	
    menuSelectionListener menuListener = new menuSelectionListener();  
    
    private void addMenuItem(Menu parent, int style, String menuKey, Menu dropDownMenu, int accelerator) {
    	MenuItem item = new MenuItem(parent, style);
    	if (menuKey != null) {
    		item.setText(locale.getProperty(menuKey));
    	   	if (style != SWT.CASCADE) {
    	   		item.setData(menuKey);
        	   	item.addSelectionListener(menuListener);
        	   	item.setAccelerator(accelerator);
    	   	}
    	   	if (dropDownMenu !=null) item.setMenu(dropDownMenu);
    	}
    }
    
	protected void createMenu() {
		 menuBar = new Menu(shell, SWT.BAR);
		 gameMenu = new Menu(shell, SWT.DROP_DOWN);
		 helpMenu = new Menu(shell, SWT.DROP_DOWN);
		 //Vector<MenuItem> menuIDs = new Vector<MenuItem>();
		 
		 //gameMenuHeader = new MenuItem(menuBar, SWT.CASCADE);
		 //gameMenuHeader.setText(locale.getProperty(GAME_MENUKEY));
		 addMenuItem(menuBar, SWT.CASCADE, GAME_MENUKEY, gameMenu, SWT.NONE);
		 
		 addMenuItem(gameMenu, SWT.PUSH, NEWGAME_MENUKEY, null, SWT.F2);
		 
		 //gameMenuHeader.setMenu(gameMenu);
		 addMenuItem(gameMenu, SWT.SEPARATOR, null, null, SWT.NONE);
		 
		 addMenuItem(gameMenu, SWT.PUSH, PREFERENCES_MENUKEY, null, SWT.F4);
		 addMenuItem(gameMenu, SWT.PUSH, STATISTIC_MENUKEY, null, SWT.F5);

		 addMenuItem(gameMenu, SWT.SEPARATOR, null, null, SWT.NONE);
 
		 addMenuItem(gameMenu, SWT.PUSH, EXIT_MENUKEY, null, SWT.NONE);
		 		 		 
		 addMenuItem(menuBar, SWT.CASCADE, HELP_MENUKEY, helpMenu, SWT.NONE);
		 addMenuItem(helpMenu, SWT.PUSH, GETHELP_MENUKEY, null, SWT.NONE);
		 addMenuItem(helpMenu, SWT.PUSH, ABOUT_MENUKEY, null, SWT.NONE);

		 shell.setMenuBar(menuBar);
		 

	     
	     
	}
 
	protected void createContent() {
		game.assignGameplay(new MinerGameplay(shell, game));
	}
	
	public GameWindow(IGame game) {
		this.game = game;
		Display display = new Display();
		 shell = new Shell(display, SWT.CLOSE | SWT.TITLE | SWT.MIN);
		 this.properties = game.getProperties(); //.getProperty(MinerGameplay.COLUMNCOUNT_PROPERTY);
		 this.defProperties = game.getDefaultProperties();
		 this.locale = game.getLocale();
		 FillLayout gridLayout = new FillLayout();
		 createMenu();
		 createContent();
		 shell.pack();
		 shell.setLocation(
				 ServiceOperation.intFromString(properties.getProperty(X_PROPERTY), Integer.valueOf(defProperties.getProperty(X_PROPERTY)).intValue()),
				 ServiceOperation.intFromString(properties.getProperty(Y_PROPERTY), Integer.valueOf(defProperties.getProperty(Y_PROPERTY)).intValue())
				 );
		 
 		 shell.addListener(SWT.Move, new Listener() {		
				@Override
				public void handleEvent(Event arg0) {
					// TODO Auto-generated method stub
					 properties.put(X_PROPERTY, Integer.toString(shell.getLocation().x));
					 properties.put(Y_PROPERTY, Integer.toString(shell.getLocation().y));					
				}
		    });
		 
	
		 shell.setText(locale.getProperty(TITLE_KEY));
		 
		 shell.open();
		 
		 while (!(shell.isDisposed() | flClose)) {
		      if (!display.readAndDispatch())
		        display.sleep();
		 }
		 display.dispose();
		 
		 game.stop();
		 System.out.println("Bye!");
		 
	}
	
	protected void AddMoveListener() {
		shell.addListener(SWT.Move, new Listener() {

	
			@Override
			public void handleEvent(Event arg0) {
				// TODO Auto-generated method stub
				
			}

	    });
	}
	
	public void close() {
		this.flClose = true;
	}
	
	public static void main(String[] args) {
		//new GameWindow();
	}
}
