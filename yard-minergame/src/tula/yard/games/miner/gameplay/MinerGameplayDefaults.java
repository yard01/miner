package tula.yard.games.miner.gameplay;



import java.util.Properties;
import java.util.Random;
import java.util.Vector;

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
/**
 * 
 * @author Дмитрий, Тула
 * Абстракный класс геймплея, в нем сделана его основная параметризация 
 *
 */
public abstract class MinerGameplayDefaults implements IGameplay {
	//имена настроек в .properties-файле свойств////////////////////////////////
	public static final String COLUMNCOUNT_PROPERTY = "COLUMNS"; // колонки
	public static final String ROWCOUNT_PROPERTY = "ROWS"; // строки
	public static final String MINECOUNT_PROPERTY = "MINES"; // количество мин
	////////////////////////////////////////////////////////////////////////////
	
	//размер ячейки в пикселях
	public static final int CELL_WIDTH = 16; // ширина
	public static final int CELL_HEIGHT = 16;// высота
	
	//параметры уровней
	public static final Point EASY_LEVEL_SIZE  = new Point(9, 9); //размер уровня "Новичок" 9 х 9 ячеек
	public static final int EASY_LEVEL_MINE  = 10; // 10 мин
	
	public static final Point JUNIOR_LEVEL_SIZE  = new Point(16, 16); // размер уровня "Любитель" 16 х 16 ячеек
	public static final int JUNIOR_LEVEL_MINE  = 40; // 40 мин

	public static final Point PROF_LEVEL_SIZE  = new Point(32, 16); // размер уровня профессионал 32 х 16 ячеек
	public static final int PROF_LEVEL_MINE  = 99; // 99 мин

	public static final int MAX_TIME  = 999; // максимальное время игры - 999 секунд
	public static final int TIME_INTERVAL  = 1000; // интервал секундомера - 1 секунда

	//состояние ячейки описывается битовыми масками
	//первый байт - это состояние ячейки,
	//а во втором байте будем хранить количество мин в соседних ячейках
	protected static final int MINE_CELL  = 1; // ячейка с миной
	protected static final int OPENED_CELL  = 2; // ячейка открыта
	protected static final int CHECK_CELL  = 4; // ячейка помечена как мина (флажком)
	protected static final int MARK_CELL  = 8; // ячейка помечена как неизвестная (вопросительный знак)
	protected static final int IS_MINE_CELL  = 16; // ячейка, на которой "подорвался" игрок

	protected static final int MIN_MINE_COUNT = -99; // минимальное количество мин, которое может быть отображено на табло
	
	//битовые маски состояния мыши
	public static final int LEFT_BUTTON = 1; //нажата левая кнопка 
	public static final int RIGHT_BUTTON = 2; //нажата правая кнопка 
	
	//битовые маски состояния игрока
	public static final int PLAYER_PLAY = 1; // играет
	public static final int PLAYER_WIN = 2; // победил
	
	//Параметров состояния геймплея не так и много, поэтому не будем создавать отдельный класс для их хранения,
	//а разместим их в значении типа long в разных битах
	//битовые смещения параметров состояния геймплея, содержащихся в значении IGameplay.getState() ////////////////////////////////////////////////////
	public static final int STATE_MOUSE_BITOFFSET = 2; // состояние кнопок мыши, 2 бита, 1-й бит - левая, второй бит - правая, 1 - нажата, 0 - отпущена 
	public static final int STATE_TIMER_BITOFFSET = 4; // показание секундомера, 16 бит, максимум - 999  
	public static final int STATE_REMAIN_MINECOUNT_BITOFFSET = 20; // количество оставшихся мин, 20 бит
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	protected static final int SPRITEBAR_HEIGHT = 28; // высота панели спрайта и табло в пикселях
}
