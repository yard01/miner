package tula.yard.games.miner.gameplay;



import java.util.Random;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.Drawable;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;

import tula.yard.games.miner.IGame;
import tula.yard.games.miner.IGameplay;
import tula.yard.games.miner.gameplay.spritebar.MinerSpriteBar;
import tula.yard.games.miner.service.ServiceOperation;
import tula.yard.games.miner.ui.StatisticWindow;


/**
 * 
 * @author Дмитрий Ярославцев, Тула 
 * Класс геймплейя. Геймплей игры - логика, дизайн, анимация и т.п. Всё, что делает игру игрой.
 *
 */
public class MinerGameplay extends MinerGameplayDefaults {
	public static final String GRAPHIC_RES_DIR = "/graph/"; // путь к графическим файлам

	private Device device;
	private Image CELLS;
	
	protected Canvas mineFieldCanvas; // канва игрового поля
	protected Canvas spriteBarCanvas; // канва панели спрайта
		
	protected Composite graphArea;
	protected IGame game;
	protected int rows = 0; // количество строк в выбранном уровне
	protected int columns = 0; // количество колонок в выбранном уровне 
	
	protected int mines = 0; // количество мин в выбранном уровне 	
	protected int checkedMineCount = 0; // количество отмеченных флажками ячеек
	protected int detectMineCount = 0; // количество правильно определенных мин
	protected volatile int time = 0; // время на секундомере в секундах
	
	protected int[][] mineField; // само минное поле
	private int clickedColumn = -1; // колонка выбранной ячейки
	private int clickedRow = -1; // строка выбранной ячейки
	protected int MOUSE_STATE = 0; // состояние мыши: первый бит - левая кнопка, второй бит - правая кнопка,
	                               // 1 - кнопка нажата, 0 - кнопка отпущена
	protected volatile int PLAYER_STATE = PLAYER_PLAY; // состояние игрока
	protected Clock clock; // объект "Секундомер"
	
	//матрица для вычисления смежных ячеек
	private static final int[][] adjancedCell = new int[][]{
													{-1, -1}, {-1, 0}, {-1, 1}, 
													{ 0, -1},		   { 0, 1},
													{ 1, -1}, { 1, 0}, { 1, 1}
													};
	//[1][1][1]
	//[1][*][1]
	//[1][1][1]		
	//1 1 1 1 1 1 1 1 = 255	
	private static final int AROUND_MASK = 255; // битовая маска разрешенных смежных ячеек - все
	
	//[0][0][0]
	//[1][*][1]
	//[1][1][1]		
	//1 1 1 1 1 0 0 0 = 248	
	private static final int WITHOUT_TOP_MASK = 248; // все, кроме верхних
	
	//[1][1][1]
	//[1][*][1]
	//[0][0][0]		
	//0 0 0 1 1 1 1 1 = 31
	private static final int WITHOUT_BOTTOM_MASK = 31;  // все, кроме нижних
	 
    //[0][1][1]
	//[0][*][1]
	//[0][1][1]		
	//1 1 0 1 0 1 1 0 = 214      
	private static final int WITHOUT_LEFT_MASK = 214; // все, кроме  левых
	
	//[1][1][0]
	//[1][*][0]
	//[1][1][0]		
	//0 1 1 0 1 0 1 1 = 107      	
	private static final int WITHOUT_RIGHT_MASK = 107; // все, кроме  правых
	
	
	/**
	 * Класс Секундомера
	 * @author Дмитрий Ярославцев
	 * секундомер ежесекундно TIME_INTERVAL увеличивет на 1 поле time и перерисовывает панель с табло
	 */
	class Clock extends Thread {
		private boolean isReset = false;
		public void run() {			
			while (!mineFieldCanvas.isDisposed()) {
				try {
					spriteBarCanvas.getDisplay().asyncExec(								
							new Runnable(){
							@Override
								public void run() {
									if (!spriteBarCanvas.isDisposed()) spriteBarCanvas.redraw();
							    }
							}	
					);		
		
					Thread.sleep(TIME_INTERVAL);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				//если игра продолжается, время не превышает максимум, а секундомер не сброшен
				//то увеличиваем time
				if ((PLAYER_STATE & PLAYER_PLAY) != 0 && time < MAX_TIME && !isReset) time++;

			}
		}
		//переопределенный метод start
		@Override
		public void start() {		
			// если секундомер был сброшен, переводим его в рабочее состояние 
			if (isReset) isReset  = false; else	if (!this.isAlive()) super.start(); // а если поток секундомера не запущен - запускаем его
		}
		//сброс секундомера
		public void reset() {
			//сброс секундомера
			time = 0; // обнуление показания
			isReset = true; // переводим в состояние "Сброшен"
		}
				
	}
	
	/**
	 * Класс Обработчика перерисовки игрового поля,
	 * в нем рисуются ячейки
	 * @author Дмитрий Ярославцев
	 *
	 */
 	class Painter implements PaintListener {
		/**
		 * Метод отрисовки ячейки, отображает ячеку в зависимости от её состояния				
		 * @param event - событие отрисовки
		 * @param r - строка
		 * @param c - колонка
		 */
		private void redrawCell(PaintEvent event, int r, int c) {
			if ((mineField[r][c] & IS_MINE_CELL) != 0) //мина, на которой подорвался игрок
		       	event.gc.drawImage(CELLS, 0, CELL_HEIGHT * 3, CELL_WIDTH, CELL_HEIGHT, c * CELL_WIDTH , r * CELL_HEIGHT,  CELL_WIDTH, CELL_HEIGHT);
			else if ((PLAYER_STATE & (PLAYER_PLAY | PLAYER_WIN)) == 0 && (mineField[r][c] & MINE_CELL) == 0 && (mineField[r][c] & CHECK_CELL) != 0) //проиграл и неверно поставлен флажок
			  	event.gc.drawImage(CELLS, 0, CELL_HEIGHT * 4, CELL_WIDTH, CELL_HEIGHT, c * CELL_WIDTH , r * CELL_HEIGHT,  CELL_WIDTH, CELL_HEIGHT);										
			else if ((mineField[r][c] & OPENED_CELL) != 0) //открытая ячейка 
	        	event.gc.drawImage(CELLS, 0, CELL_HEIGHT * (7 + 8 - (mineField[r][c]>>8)), CELL_WIDTH, CELL_HEIGHT, c * CELL_WIDTH , r * CELL_HEIGHT,  CELL_WIDTH, CELL_HEIGHT);
			else if ((mineField[r][c] & CHECK_CELL) != 0) //ячейка помечена флажком
	        	event.gc.drawImage(CELLS, 0, CELL_HEIGHT, CELL_WIDTH, CELL_HEIGHT, c * CELL_WIDTH , r * CELL_HEIGHT,  CELL_WIDTH, CELL_HEIGHT);
			else if ((mineField[r][c] & MARK_CELL) != 0) // сомнительная ячейка
	        	event.gc.drawImage(CELLS, 0, CELL_HEIGHT * 2, CELL_WIDTH, CELL_HEIGHT, c * CELL_WIDTH , r * CELL_HEIGHT,  CELL_WIDTH, CELL_HEIGHT);
			else if ((MOUSE_STATE & LEFT_BUTTON) != 0 && r == clickedRow && c == clickedColumn) //кликнутая ячейка
	        	event.gc.drawImage(CELLS, 0, CELL_HEIGHT * 15, CELL_WIDTH, CELL_HEIGHT, c * CELL_WIDTH , r * CELL_HEIGHT,  CELL_WIDTH, CELL_HEIGHT);
			else if ((PLAYER_STATE & (PLAYER_PLAY | PLAYER_WIN)) == 0 && (mineField[r][c] & MINE_CELL) != 0) //проиграл, ячейка с миной и не флажок
				event.gc.drawImage(CELLS, 0, CELL_HEIGHT * 5, CELL_WIDTH, CELL_HEIGHT, c * CELL_WIDTH , r * CELL_HEIGHT,  CELL_WIDTH, CELL_HEIGHT);			
			else // пустая ячейка
				event.gc.drawImage(CELLS, 0, 0, CELL_WIDTH, CELL_HEIGHT, c * CELL_WIDTH , r * CELL_HEIGHT,  CELL_WIDTH, CELL_HEIGHT);
			
		}
			
		/**
		 * вызывается при перерисовке канвы
		 */
		@Override
		public void paintControl(PaintEvent event) {
			//рисуем в цикле все ячейки минного поля
			//поскольку используется режим двойной буферизации, рисовать нужно всё поле, а не только измененные ячейки
			for (int r = 0; r < rows; r++) 
		    	for (int c = 0; c < columns; c++) redrawCell(event, r, c);					    
		}		
	}
 		
	/**
	 * класс Обработчика кликов мыши
	 * @author Дмитрий Ярославцев
	 *
	 */
	class Clicker implements MouseListener {
		@Override		
		public void mouseDoubleClick(MouseEvent arg0) { }

		@Override
		public void mouseDown(MouseEvent event) {
			// обработчик события "нажата кнопка мыши"
			if (setMouseLocation(event)) {//определили кликнутую ячейку				
				switch (event.button) { // установка состояния кнопок мыши
				case 1: MOUSE_STATE |= LEFT_BUTTON; break; 
				case 3: MOUSE_STATE |= RIGHT_BUTTON; break;			
				}		
				redraw(); //перерисовка
			}
		}

		@Override
		public void mouseUp(MouseEvent event) {		
			//обработчик события "отпущена кнопка мыши"
			if (setMouseLocation(event))
				switch (event.button) {
				case 1: // первая кнопка (левая)
					if ((MOUSE_STATE & LEFT_BUTTON) != 0) touch(); // если была отпущена левая кнопка - "трогаем" ячейку
					MOUSE_STATE &= ~LEFT_BUTTON; //сбрасываем состояние "нажата левая кнопка"
					break;
		 
				case 3: //третья кнопка (правая)
					if ((MOUSE_STATE & RIGHT_BUTTON) != 0) check(); // если была отпущена правая кнопка - помечаем ячейку флажком				
					MOUSE_STATE &= ~RIGHT_BUTTON; // сбрасываем состояние "нажата правая кнопка"
					break;			
				}
			redraw(); //перерисовываем поле
		}		
	}
	
	/**
	 * Класс Обработчика события "перемещение мыши"
	 * @author Дмитрий Ярославцев
	 *
	 */
	class Mover implements MouseMoveListener {
		@Override
		public void mouseMove(MouseEvent event) {
			setMouseLocation(event); // получили ячейку
			mineFieldCanvas.redraw(); // перерисовали поле (надо перерисовывать, т.к. игрок может двигать кликнутой мышью по полю и надо отображать нажатую ячейку)
		}
	}
/**
 * Конструктор геймплея
 * как и в "Сапёре" под WinXP, геймплей состоит из игрового поля с ячейками 
 * и панели табло с анимированным смайликом (спрайтом)	
 * @param graphArea - область графики, ссылка на графический компонент, на котором будет отрисован геймлей, в данном случае на Shell нашего окна
 * @param game - игра, ссылка на объект класса "Игровое приложение"
 */
	public MinerGameplay(Composite graphArea, IGame game) {
		
		this.graphArea = graphArea; // установили переменные
		this.game = game; // игра

		CELLS =   new Image(device, this.getClass().getResourceAsStream(GRAPHIC_RES_DIR + "cells.bmp")); // загрузили набор картинок с ячейками
		this.spriteBarCanvas = new Canvas(this.graphArea, SWT.NO_BACKGROUND); //создали канву для спрайта и табло
		this.mineFieldCanvas = new Canvas(this.graphArea, SWT.NO_BACKGROUND); //создали канву для игрового поля, SWT.NO_BACKGROUND - использование двойной буферизации при отрисовке
		this.mineFieldCanvas.addPaintListener(new Painter()); // добавили обработчик событий отрисовки на канву поля
		this.mineFieldCanvas.addMouseListener(new Clicker()); // добавили обработчик событий нажатий кнопок мыши
		this.mineFieldCanvas.addMouseMoveListener(new Mover()); // добавили обработчки перемещений мыши
		
		new MinerSpriteBar(this.spriteBarCanvas, this); //создали панель спрайта
		this.clock = new Clock(); // создали секундомер
		createContent(); //создали контент геймплея
	}
	/**
	 * Создание контента гейплея
	 */
	@Override
	public void createContent() {
		//Загрузили значения параметров игрового поля
		this.rows = ServiceOperation.getNsetIntProperty(game.getProperties(), ROWCOUNT_PROPERTY, EASY_LEVEL_SIZE.y);
		this.columns = ServiceOperation.getNsetIntProperty(game.getProperties(), COLUMNCOUNT_PROPERTY, EASY_LEVEL_SIZE.x);			
		this.mines = ServiceOperation.getNsetIntProperty(game.getProperties(), MINECOUNT_PROPERTY, EASY_LEVEL_MINE);	
		if (this.time > 0) this.clock.reset(); //если имел место перезапуск игры, т.е. секундомер уже что-то отсчитал, то сбрасываем его 
		this.checkedMineCount = 0; // число помеченных ячеек как Мина - 0
		this.detectMineCount = 0; // число правильно помеченных игроком мин - 0
		this.mineFieldCanvas.setSize(CELL_WIDTH * columns, CELL_HEIGHT * rows); // задали размер канвы поля
		this.spriteBarCanvas.setSize(this.mineFieldCanvas.getSize().x, SPRITEBAR_HEIGHT); //задали размер канвы панели спрайта
		this.mineFieldCanvas.setLocation(0, SPRITEBAR_HEIGHT); //расположили канву поля под канвой панели спрайта
		this.mineField = new int[rows][columns]; //создали минное поле - массив ячеек
		this.PLAYER_STATE = PLAYER_PLAY; //установили состояние игрока в "Играет"
		
		//заполнение минного поля
	    int m = 0; //счетчик установленных мин
	     
	    Random random = new Random(); //датчик случайных чисел
	    for (int r = 0; r < this.rows; r++) {
	    	for (int c = 0; c < this.columns; c++) {	    		
	    		if (m < this.mines) { //если число установленных мин меньше заданного, ставим мину в случайную ячейку
	    			//int enabledMask = 
	    			m++;
	    			//получаем случайную ячейку
	    			int rand = random.nextInt(this.rows * this.columns - this.mines) + this.mines;
	    			int rand_c = rand % this.columns; //получили колонку
	    			int rand_r = rand / this.columns; //получили строку
	    			if ((this.mineField[rand_r][rand_c] & MINE_CELL) != 0) { // если в случайной ячейке уже есть мина
	    				rand_c = c; rand_r = r; //тогда оставим мину в текущей ячейке [r, c]
	    			};
	    			
	    			this.mineField[rand_r][rand_c] |= MINE_CELL;
	    				    			
	    			//увеличим на 1 количество смежных мин в соседних ячейках
	    			int enabled_mask = getEnabledCells(rand_r, rand_c);
	    			for (int i = 0; i < 8; i++)
	    				if (((enabled_mask >> i) & 1) == 1) 
	    					this.mineField[rand_r + adjancedCell[i][0]][rand_c + adjancedCell[i][1]] += 256;
	    			
	    		}
	    	}
	    }
	    
	    redraw(); //отобразили геймплей
	    this.mineFieldCanvas.getParent().pack(); //подогднали размер окна под размер геймлея
	    
	}
	
	/**
	 * метод "Победа" устанавливает состояние игры в "выиграл", 
	 * предлагает записать результат в статистику
	 */	
	public void win(){
		PLAYER_STATE = PLAYER_WIN;	//установили состояние игрока в "выиграл"
		redraw(); //перерисовали поле и панель
		StatisticWindow.saveHero(this); // предложили записаться в статистику
		
	};
	
	/**
	 * метод "Поражение" устанавливает состояние игры в "проиграл", 
	 */	
	public void fail() {
		PLAYER_STATE &= ~PLAYER_PLAY; //сбросили состояние "играет", если игрок не играет и не выиграл, значит он проиграл		
		this.mineField[clickedRow][clickedColumn] |= IS_MINE_CELL; //установили мине, на которой подорвался игрок, соответсвующий статус
		redraw();

	};

	/**
	 * Возвращает битовую маску разрешенных ячеек
	 * r - строка, c - колонка 
	 * @param r
	 * @param c
	 * @return
	 */
	protected int getEnabledCells(int r, int c) {
		int result = AROUND_MASK; 
			
        if (r == 0) result &= WITHOUT_TOP_MASK;
        else if (r == this.rows - 1) result &= WITHOUT_BOTTOM_MASK;
        
        if (c == 0) result &= WITHOUT_LEFT_MASK;
        else if (c == this.columns - 1) result &= WITHOUT_RIGHT_MASK;
        
		return result;
	}
	
	/**
	 * Определяет позицию (строку и колонку) ячейки, над которой произошло событие мыши event
	 * @param event
	 * @return
	 */
    protected boolean setMouseLocation(MouseEvent event) {
    	if ((PLAYER_STATE & PLAYER_PLAY) == 0) {
    		MOUSE_STATE = 0;
    		return false;
    		
    	} else {
    	
    	this.clickedRow = event.y / CELL_HEIGHT;    	
    	this.clickedColumn = event.x / CELL_WIDTH;    	
    	
    	if (this.clickedRow < 0) this.clickedRow = 0; else if (this.clickedRow > this.rows - 1) this.clickedRow = this.rows - 1; 
    	if (this.clickedColumn < 0) this.clickedColumn = 0; else if (this.clickedColumn > this.columns - 1) this.clickedColumn = this.columns - 1;// this.MOUSE_STATE
    	return true;
    	}
    }
	/**
	 * метод Открыть ячейки
	 * открывает кликнутую ячейку и соседние, кроме заминированных
	 * r - строка, c - колонка
	 * @param r
	 * @param c
	 */
    protected void openCell(int r, int c) {
		this.mineField[r][c] |= OPENED_CELL;		
		int enabled_mask = getEnabledCells(r, c); // получили битовую матрицу разрешенных ячеек
		for (int i = 0; i < 8; i++)
			if (((enabled_mask >> i) & 1) == 1) {
				int next_r = r + adjancedCell[i][0];
				int next_c = c + adjancedCell[i][1];
				if ((this.mineField[next_r][next_c] & (OPENED_CELL | CHECK_CELL) ) == 0) { //открываем только не открытые и не помеченные ячейки
					this.mineField[next_r][next_c] |= OPENED_CELL; 
					if ((this.mineField[next_r][next_c] >> 8) == 0) openCell(next_r, next_c); 	
				}
			}
	}
    
    /**
     * метод "Тронуть"
     */
    public void touch() {
    	if ((PLAYER_STATE & PLAYER_PLAY) == 0) return;
       	if ((this.mineField[clickedRow][clickedColumn] & (OPENED_CELL | CHECK_CELL | MARK_CELL) ) != 0) return;
       	
       	if (time == 0) this.clock.start(); 
       	
       	PLAYER_STATE = PLAYER_PLAY;
       	
       	if ((this.mineField[clickedRow][clickedColumn] & MINE_CELL) != 0) {
       		fail();
       		return;
       	}
       	
       	if (this.PLAYER_STATE == PLAYER_PLAY && this.mines - this.detectMineCount == 0) win();
       	     	
       	this.mineField[clickedRow][clickedColumn] |= OPENED_CELL;
       	if ((this.mineField[clickedRow][clickedColumn]>>8) == 0) openCell(clickedRow, clickedColumn);
       	
    }
    
    /**
     * метод "Поставить Флажок"
     */    
    public void check() {
    	// если ячейка уже открыта - ничего не делаем, выходим
    	if ((this.mineField[clickedRow][clickedColumn] & OPENED_CELL) != 0) return;
    	
     	if ((this.mineField[clickedRow][clickedColumn] & MARK_CELL) != 0) // если ячейка помечена как сомнительная - снимаем эту пометку
     		this.mineField[clickedRow][clickedColumn] &= ~MARK_CELL;	// сбрасываем бит
     	else if ((this.mineField[clickedRow][clickedColumn] & CHECK_CELL) != 0) { // если на ячейке уже стоит флажок 
    		/*if (this.checkedMineCount > MIN_MINE_COUNT) */ 
     		this.checkedMineCount--; //else return; //уменьшаем количество помеченных мин     		
     		this.mineField[clickedRow][clickedColumn] &= ~CHECK_CELL; //сбрасываем бит флажка
    		this.mineField[clickedRow][clickedColumn] |= MARK_CELL; //помечаем ячейку как сомнительную
    		if ((this.mineField[clickedRow][clickedColumn] & MINE_CELL) !=0) this.detectMineCount--; //если в той ячейке была мина, то уменьшаем количество правильно найденных мин  
     	}
    	else {//если флажок не установлен
      		if (this.checkedMineCount < PROF_LEVEL_MINE) this.checkedMineCount++; else return; // если количество помеченных мин меньше максимально возможного, то увеличиваем счетчик установленных мин
    		this.mineField[clickedRow][clickedColumn] |= CHECK_CELL; // устанавливаем флажок
    		if ((this.mineField[clickedRow][clickedColumn] & MINE_CELL) !=0) this.detectMineCount++; //если в ячейке мина - увеличиваем счетчик правильно помеченных мин 
    	   	//если игрок играет, а общее количество мин рано количеству правильно помеченных, то выигрыш
    		if (this.PLAYER_STATE == PLAYER_PLAY && this.mines - this.detectMineCount == 0) win();
    	    
    	}
    }
	@Override
	public IGame getGame() {
		// TODO Auto-generated method stub
		return game;
	}
	@Override
	public Object getState() {
		return (long)((this.mines - this.checkedMineCount) << MinerGameplayDefaults.STATE_REMAIN_MINECOUNT_BITOFFSET)
				| (this.time << MinerGameplayDefaults.STATE_TIMER_BITOFFSET ) 
				| (MOUSE_STATE << MinerGameplayDefaults.STATE_MOUSE_BITOFFSET) 
				| PLAYER_STATE;
	}
    /**
     * перерисовка геймлея
     */
	public void redraw() {
		mineFieldCanvas.redraw();
		spriteBarCanvas.redraw();
	}
	
	/**
	 * возвращает игровое время
	 */
	@Override
	public long getTime() {
		return time;
	}
    
}
