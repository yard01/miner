package tula.yard.games.miner.gameplay.spritebar;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

import tula.yard.games.miner.IGameplay;
import tula.yard.games.miner.gameplay.MinerGameplayDefaults;
import tula.yard.games.miner.service.ServiceOperation;

public class MinerSpriteBar {
    
	private Composite spriteBar; 
	private IGameplay gameplay;
	private Image SPRITE;
	private int SPRITE_WIDTH = 24;
	private int SPRITE_HEIGHT = 24;	
	private int NUMBER_WIDTH  = 13;
	private int NUMBER_HEIGHT = 23;
	private boolean spriteClick = false;
	private Image NUMBERS;
	private Device device;
	public static final String SPRITE_RES_DIR = "/graph/"; 
	private Point spriteLocation = null;
	
	class SpriteBarPainter implements PaintListener {
		private GC gc;
		private long gameplayState = 0;
	    private static final String numberTemplate = "***";
		private int minecount = 0; //-2;
		private int time = 0;//123;
		private int numbers_y = 0;
			
		private void drawNumber(char number, int x, int y) {
			int offset_y = 0;
			switch (number) {
			case '*' : offset_y += NUMBER_HEIGHT; break;
			case '9' : offset_y += NUMBER_HEIGHT * 2; break;
			case '8' : offset_y += NUMBER_HEIGHT * 3; break;
			case '7' : offset_y += NUMBER_HEIGHT * 4; break;
			case '6' : offset_y += NUMBER_HEIGHT * 5; break;
			case '5' : offset_y += NUMBER_HEIGHT * 6; break;
			case '4' : offset_y += NUMBER_HEIGHT * 7; break;
			case '3' : offset_y += NUMBER_HEIGHT * 8; break;
			case '2' : offset_y += NUMBER_HEIGHT * 9; break;
			case '1' : offset_y += NUMBER_HEIGHT * 10; break;
			case '0' : offset_y += NUMBER_HEIGHT * 11; break;
							
			}
			gc.drawImage(NUMBERS, 0, offset_y, NUMBER_WIDTH, NUMBER_HEIGHT, x, y, NUMBER_WIDTH, NUMBER_HEIGHT);
			
		}
		
		public String format(int number) {
			String numberstr = String.valueOf(number);
			int len = numberstr.length();
			if (len < numberTemplate.length()) return numberTemplate.substring(numberstr.length()) + numberstr;
			else return numberstr;
			
		}
		
		/**
		 * рисует табло с количеством мин 
		 */		
		private void drawMineCount() { 
			String minecountstr = format(minecount);
			drawNumber(minecountstr.charAt(0), spriteBar.getClientArea().width  - NUMBER_WIDTH * 3, numbers_y);
			drawNumber(minecountstr.charAt(1), spriteBar.getClientArea().width  - NUMBER_WIDTH * 2, numbers_y);
			drawNumber(minecountstr.charAt(2), spriteBar.getClientArea().width  - NUMBER_WIDTH, numbers_y);
			
		};	

		/**
		 * рисует табло с показаниями секундомера 
		 */			
		private void drawTimer() {		
			String timestr = format(time); //отформатировали строку - 3 символа
			drawNumber(timestr.charAt(0), 0, numbers_y); // нарисовали первый символ
			drawNumber(timestr.charAt(1), NUMBER_WIDTH, numbers_y); // нарисовали второй символ
			drawNumber(timestr.charAt(2), NUMBER_WIDTH * 2, numbers_y); // нарисовали третий символ
		};
		
		private void drawSprite(){
			int imageOffset = 4;
	        if (spriteClick) imageOffset = 0; else		
			if ((gameplayState & MinerGameplayDefaults.PLAYER_WIN) != 0) imageOffset = 1;
			else
			if ((gameplayState & (MinerGameplayDefaults.PLAYER_WIN | MinerGameplayDefaults.PLAYER_PLAY)) == 0) imageOffset = 2;	
			   if (((gameplayState >> MinerGameplayDefaults.STATE_MOUSE_BITOFFSET) & MinerGameplayDefaults.LEFT_BUTTON) != 0) imageOffset = 3;
			
			
			spriteLocation = ServiceOperation.getMiddlePosition(0, 0, spriteBar.getBounds().width, spriteBar.getBounds().height, SPRITE_WIDTH, SPRITE_HEIGHT);
			gc.drawImage(SPRITE, 0, SPRITE_HEIGHT * imageOffset, SPRITE_WIDTH, SPRITE_HEIGHT, spriteLocation.x, spriteLocation.y, SPRITE_WIDTH, SPRITE_HEIGHT);
		};
		
		
				
		private void drawAll() {
			gameplayState = (long)gameplay.getState();
			
			minecount = (int)(gameplayState >> MinerGameplayDefaults.STATE_REMAIN_MINECOUNT_BITOFFSET);
			time = (int) ((gameplayState >> MinerGameplayDefaults.STATE_TIMER_BITOFFSET) & ServiceOperation.BIT_MASK_16);
			
			gc.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
			gc.fillRectangle(spriteBar.getClientArea());

			drawTimer();
			drawSprite();			
			drawMineCount();
			
		}
		
		@Override
		public void paintControl(PaintEvent event) {
			Image image = new Image(spriteBar.getDisplay(), spriteBar.getBounds()); //создали теневой буфер 
			gc = new GC(image); //создали графический контекст
			numbers_y = ((spriteBar.getClientArea().height - NUMBER_HEIGHT) >> 1); // центрируем по высоте табло
			drawAll(); //нарисовали содержимое в теневом буфере
			event.gc.drawImage(image, 0, 0); //отобразили теневой буфер 
			image.dispose(); 
			gc.dispose();
		}
	
	}
	private boolean isSpriteButton(int x, int y) {
		return x >= spriteLocation.x && x <= spriteLocation.x + SPRITE_WIDTH && y >= spriteLocation.y && y <= spriteLocation.y + SPRITE_WIDTH; 
	}
	
    public MinerSpriteBar(final Composite spriteBar, final IGameplay gameplay) {
		this.spriteBar = spriteBar;
		this.gameplay = gameplay;
		spriteBar.addPaintListener(new SpriteBarPainter()); 
		SPRITE =  new Image(device, this.getClass().getResourceAsStream(SPRITE_RES_DIR + "sprite.bmp"));
		NUMBERS = new Image(device, this.getClass().getResourceAsStream(SPRITE_RES_DIR + "numbers.bmp"));
		spriteBar.addMouseListener(new MouseListener(){

			@Override
			public void mouseDoubleClick(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseDown(MouseEvent event) {
				spriteClick = isSpriteButton(event.x, event.y);
				if (spriteClick) spriteBar.redraw();
				// TODO Auto-generated method stub
			}

			@Override
			public void mouseUp(MouseEvent event) {
				// TODO Auto-generated method stub
				if (isSpriteButton(event.x, event.y)) { 
					spriteBar.redraw();
					gameplay.getGame().restart();
				    
				}
				spriteClick = false;
				
			}});

	}
    
    
}
