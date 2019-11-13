package tula.yard.games.miner.ui;

import java.util.Properties;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Scale;
import org.eclipse.swt.widgets.Shell;

import tula.yard.games.miner.IGame;
import tula.yard.games.miner.gameplay.MinerGameplayDefaults;
import tula.yard.games.miner.service.ServiceOperation;

public class PreferencesWindow extends ChildWindow {
	public static final String TITLE = "PREFERENCES";
	public static final String RADIO_GRP = "LEVELS";
	public static final String FIELD = "FIELD";	
	public static final String MINES = "MINES";	
	
	public static final String BUTTON_OK = "OK";	
	public static final String BUTTON_CANCEL = "CANCEL";
	
	public static final String LEVEL_EASY = "lvEASY";
	public static final String LEVEL_JUNIOR = "lvJUNIOR";
	public static final String LEVEL_PROF = "lvPROF";
	public static final String LEVEL_SPECIFIC = "lvSPECIFIC";
		
	IGame game;
	
	private static final int WINDOW_WIDTH = 420;
	private static final int WINDOW_HEIGHT = 250;	
	private static final int numColumns = 2; 
	private static final int BUTTON_WIDTH = 100;
	private static final int BUTTON_HEIGHT = 28;
	private static final int BUTTON_SPLIT = 10;
	private static final int CELL_PIC_SIZE = 6;
	private static final int BORDER_WIDTH = 4;
	//private static final int WIDGET_HEIGHT = 24;
	
	private Properties local = null;
    private Properties properties = null;
	private int mines = 0;
	private int rows = 0;
	private int columns = 0;
	private Canvas specificField = null;
	private boolean specificFieldSelect = false;
	private Label specificInfo = null;
	private Scale scaleMine = null;
	private double minesAtCell =  0.1934;  
	
	class ControlSelectionListener implements SelectionListener {
		
		private void workingSelect(SelectionEvent event) {
			enableSpecificPreferences((String)event.widget.getData() == LEVEL_SPECIFIC);
			switch ((String)event.widget.getData()) {
			case LEVEL_EASY: 
				mines = MinerGameplayDefaults.EASY_LEVEL_MINE;
				rows = MinerGameplayDefaults.EASY_LEVEL_SIZE.y;
				columns =  MinerGameplayDefaults.EASY_LEVEL_SIZE.x;
				break;
			case LEVEL_JUNIOR: 
				mines = MinerGameplayDefaults.JUNIOR_LEVEL_MINE;
				rows = MinerGameplayDefaults.JUNIOR_LEVEL_SIZE.y;
				columns =  MinerGameplayDefaults.JUNIOR_LEVEL_SIZE.x;
				break;
			case LEVEL_PROF: 
				mines = MinerGameplayDefaults.PROF_LEVEL_MINE;
				rows = MinerGameplayDefaults.PROF_LEVEL_SIZE.y;
				columns =  MinerGameplayDefaults.PROF_LEVEL_SIZE.x;
				break;
			
			case LEVEL_SPECIFIC: break; //specificField.setEnabled(arg0);
				
			case BUTTON_OK: 
				properties.put(MinerGameplayDefaults.MINECOUNT_PROPERTY, String.valueOf(mines));
				properties.put(MinerGameplayDefaults.ROWCOUNT_PROPERTY, String.valueOf(rows));
				properties.put(MinerGameplayDefaults.COLUMNCOUNT_PROPERTY, String.valueOf(columns));
				window.close();
				game.restart(); 				
				break;
				
			case BUTTON_CANCEL: window.close(); break;	
			}
			if (!window.isDisposed()) {
				setSpecificInfo();
				scaleMine.setMaximum((int)(rows * columns * minesAtCell));
				scaleMine.setSelection(mines);
			}
			
			
		}
		
		@Override
		public void widgetDefaultSelected(SelectionEvent event) { workingSelect(event); }

		@Override
		public void widgetSelected(SelectionEvent event) { workingSelect(event); }
		
	}
	
	private void enableSpecificPreferences(boolean enable) {
		specificField.setEnabled(enable);
		scaleMine.setEnabled(enable);
		specificField.redraw();
	}
	
	private Button createButton(Composite box, String textCode, int swtType, boolean selected) {
		Button button = new Button(box, swtType); 
		button.setText(local.getProperty(textCode));
		button.setData(textCode);		
		button.setSelection(selected);
		button.addSelectionListener(new ControlSelectionListener());
		return button;
	}
	
	private void specificSizeToCells(int x, int y) {
		this.rows = y / CELL_PIC_SIZE;
		this.columns = x / CELL_PIC_SIZE; 
		if (this.rows < MinerGameplayDefaults.EASY_LEVEL_SIZE.y) this.rows = MinerGameplayDefaults.EASY_LEVEL_SIZE.y; 
		else if (this.rows > MinerGameplayDefaults.PROF_LEVEL_SIZE.y) this.rows = MinerGameplayDefaults.PROF_LEVEL_SIZE.y;
		if (this.columns < MinerGameplayDefaults.EASY_LEVEL_SIZE.x) this.columns = MinerGameplayDefaults.EASY_LEVEL_SIZE.x;
		else if (this.columns > MinerGameplayDefaults.PROF_LEVEL_SIZE.x) this.columns = MinerGameplayDefaults.PROF_LEVEL_SIZE.x;
		scaleMine.setMaximum((int)(rows * columns * minesAtCell));
		if (this.mines > scaleMine.getMaximum()) this.mines = scaleMine.getMaximum();
		scaleMine.setSelection(this.mines);
		setSpecificInfo();
	}
	
	private void setSpecificInfo() {
		specificInfo.setText(local.getProperty(FIELD) +" [" +this.rows +" x " + this.columns +" ], " + this.mines + " " + local.getProperty(MINES));
	}
	
	private void createSpecificField(Composite parent) {
		parent.setLayout(null);
		specificInfo = new Label(parent, SWT.NONE);
		specificInfo.setLocation(BORDER_WIDTH, BORDER_WIDTH);
		specificInfo.setSize(MinerGameplayDefaults.PROF_LEVEL_SIZE.x * CELL_PIC_SIZE + 1, 24);
		specificInfo.setText(local.getProperty(FIELD));

		specificField = new Canvas(parent, SWT.NO_BACKGROUND); // .NONE);
		//specificField.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_GRAY));		
		specificField.setLocation(BORDER_WIDTH, BORDER_WIDTH * 2 + specificInfo.getSize().y);
		specificField.setSize(MinerGameplayDefaults.PROF_LEVEL_SIZE.x * CELL_PIC_SIZE + 1, MinerGameplayDefaults.PROF_LEVEL_SIZE.y  * CELL_PIC_SIZE + 1);
		
		//specificInfo.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_RED));
		//Rectangle picCell = c * CELL_PIC_SIZE, r * CELL_PIC_SIZE, CELL_PIC_SIZE, CELL_PIC_SIZE);
		scaleMine = new Scale(parent, SWT.NONE);
		//scaleMine.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_BLUE));
		scaleMine.setLocation(BORDER_WIDTH, BORDER_WIDTH + specificField.getLocation().y + specificField.getSize().y);
		scaleMine.setSize(specificField.getSize().x, 50);
		scaleMine.setMinimum(1);  //.setText(local.getProperty(FIELD));
		
		scaleMine.setMaximum((int) (rows * columns * minesAtCell));
		scaleMine.setIncrement(1);
		scaleMine.setSelection(this.mines);
		setSpecificInfo();
		specificField.addPaintListener(new PaintListener(){

			@Override
			public void paintControl(PaintEvent event) {
			// TODO Auto-generated method stub
				Image image = new Image(specificField.getDisplay(), specificField.getBounds());
				GC gc = new GC(image);
				gc.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
				gc.fillRectangle(specificField.getClientArea());
	
				for (int r = 0; r < MinerGameplayDefaults.PROF_LEVEL_SIZE.y; r++) {
					for (int c = 0; c < MinerGameplayDefaults.PROF_LEVEL_SIZE.x; c++) {
						
						//event.gc.drawRectangle(c * CELL_PIC_SIZE, r * CELL_PIC_SIZE, CELL_PIC_SIZE, CELL_PIC_SIZE);
						if (r < MinerGameplayDefaults.EASY_LEVEL_SIZE.y - 1 && c < MinerGameplayDefaults.EASY_LEVEL_SIZE.x - 1)
							gc.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_DARK_GRAY));
						else 
							if (r == rows - 1 & c < columns | c == columns - 1 & r < rows) 
							    gc.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_GREEN));
														  
						     else
						 		if (specificField.getEnabled())									
							       gc.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_DARK_GREEN));
								else
							        gc.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_GRAY));
							
						gc.fillRectangle(c * CELL_PIC_SIZE + 1, r * CELL_PIC_SIZE + 1, CELL_PIC_SIZE - 1, CELL_PIC_SIZE - 1);
						
					}
				}
				event.gc.drawImage(image, 0, 0);

		        image.dispose();
		        gc.dispose();
			}});
		
		specificField.addMouseMoveListener(new MouseMoveListener(){

			@Override
			public void mouseMove(MouseEvent event) {
				// TODO Auto-generated method stub
				if (specificFieldSelect) {
					specificSizeToCells(event.x, event.y);
					specificField.redraw();
				}
			}});
		specificField.addMouseListener(new MouseListener() {

			@Override
			public void mouseDoubleClick(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseDown(MouseEvent event) {
				// TODO Auto-generated method stub
				if (!specificField.getEnabled()) return;
				specificFieldSelect = true;
				specificSizeToCells(event.x, event.y);
				specificField.redraw();
			}

			@Override
			public void mouseUp(MouseEvent arg0) {
				// TODO Auto-generated method stub
				specificFieldSelect = false;
			}});
		
		scaleMine.addSelectionListener(new SelectionListener(){

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub
				mines = scaleMine.getSelection();
			}

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub
				//System.out.println(scaleMine.getSelection());
				mines = scaleMine.getSelection();
				setSpecificInfo();
			}});
		
	}
	
	public PreferencesWindow(IGame game, Shell parentWindow) {
		super(game, parentWindow, WINDOW_WIDTH,  WINDOW_HEIGHT, TITLE);
		this.game = game;
		local = game.getLocale();
		properties = game.getProperties();
		mines = ServiceOperation.getNsetIntProperty(properties, MinerGameplayDefaults.MINECOUNT_PROPERTY, MinerGameplayDefaults.EASY_LEVEL_MINE);
		rows =  ServiceOperation.getNsetIntProperty(properties, MinerGameplayDefaults.ROWCOUNT_PROPERTY, MinerGameplayDefaults.EASY_LEVEL_SIZE.y);
		columns = ServiceOperation.getNsetIntProperty(properties, MinerGameplayDefaults.COLUMNCOUNT_PROPERTY, MinerGameplayDefaults.EASY_LEVEL_SIZE.x);
		//window = new Shell(parentWindow, SWT.APPLICATION_MODAL | SWT.TITLE | SWT.CLOSE);
		//window.setText(local.getProperty(TITLE));
		//Point parentPos = parentWindow.getLocation();
		//Point parentSize= parentWindow.getSize();
		//mineMAX= MinerGameplayDefaults.PROF_LEVEL_MINE / (MinerGameplayDefaults.PROF_LEVEL_SIZE.x * MinerGameplayDefaults.PROF_LEVEL_SIZE.y);
		

		//parentWindow.get
		//Point pos = ServiceOperation.getMiddlePosition(parentPos.x, 
		//parentPos.y, parentSize.x, parentSize.y, WINDOW_WIDTH, WINDOW_HEIGHT);
		//window.setLocation(pos);
		//window.setSize(WINDOW_WIDTH,  WINDOW_HEIGHT);
		GridData gdMain = new GridData(GridData.FILL_BOTH);
		gdMain.grabExcessHorizontalSpace = true;
		gdMain.grabExcessVerticalSpace = true;
		//gdMain.
		GridData gdFooter = new GridData(GridData.FILL_HORIZONTAL);
		gdFooter.grabExcessHorizontalSpace = true;
		gdFooter.horizontalSpan = numColumns;
		
		GridLayout layout = new GridLayout();
		layout.numColumns =  numColumns;
		layout.horizontalSpacing =0;
		layout.marginRight = 0;
		window.setLayout(layout);
		
		//window.setLayoutData(new GridData(2, 2));
		
		Composite composite = new Composite(window, SWT.NONE);
		//composite.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_BLUE));
		composite.setLayoutData(gdMain);
		composite.setLayout(new FillLayout());
	    Group radioGroup = new Group(composite, SWT.SHADOW_IN);
	    radioGroup.setText(local.getProperty(RADIO_GRP));
	    RowLayout rl = new RowLayout(SWT.VERTICAL);
	    rl.spacing = 10;
	    rl.marginTop = 10;
	    radioGroup.setLayout(rl);
	    
	    int level = (( mines == MinerGameplayDefaults.EASY_LEVEL_MINE 
	    		   &    rows == MinerGameplayDefaults.EASY_LEVEL_SIZE.y 
	    		   & columns == MinerGameplayDefaults.EASY_LEVEL_SIZE.x) ? 1 : 0 );
	    if (level == 0) {		   
	        level |=
	    		   ((  mines == MinerGameplayDefaults.JUNIOR_LEVEL_MINE 
	    		   &    rows == MinerGameplayDefaults.JUNIOR_LEVEL_SIZE.y 
	    		   & columns == MinerGameplayDefaults.JUNIOR_LEVEL_SIZE.x) ? 2 : 0);
	        if (level == 0) {
	        level |=
	    		  ((   mines == MinerGameplayDefaults.PROF_LEVEL_MINE 
	    		   &    rows == MinerGameplayDefaults.PROF_LEVEL_SIZE.y 
	    		   & columns == MinerGameplayDefaults.PROF_LEVEL_SIZE.x) ? 4 : 0);
	        }
	    }    
	    createButton(radioGroup, LEVEL_EASY, SWT.RADIO, level == 1);   
	    createButton(radioGroup, LEVEL_JUNIOR, SWT.RADIO, (level & 2) > 0);
	    createButton(radioGroup, LEVEL_PROF, SWT.RADIO, (level & 4) > 0);
	    createButton(radioGroup, LEVEL_SPECIFIC, SWT.RADIO, level == 0);
	    
		composite = new Composite(window, SWT.NONE);
		composite.setLayoutData(gdMain);
		
		createSpecificField(composite);
		enableSpecificPreferences(level == 0);
		
		composite = new Composite(window, SWT.NONE);
		//composite.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_YELLOW));
		composite.setLayoutData(gdFooter);
		composite.setLayout(null);
		Rectangle windowRect = window.getClientArea(); // composite.getClientArea();
		Button btnOK =  createButton(composite, BUTTON_OK, SWT.NONE, false);
		btnOK.setSize(BUTTON_WIDTH, BUTTON_HEIGHT);
		btnOK.setLocation( windowRect.width - (BUTTON_WIDTH + BUTTON_SPLIT) * 2 , 0);
				
		Button btnCancel = createButton(composite, BUTTON_CANCEL, SWT.NONE, false);
		btnCancel.setSize(BUTTON_WIDTH, BUTTON_HEIGHT);
		btnCancel.setLocation(windowRect.width - BUTTON_WIDTH - BUTTON_SPLIT, 0);

		composite.pack();
			
		window.open();
		
		
		 

	}
}
