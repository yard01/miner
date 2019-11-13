package tula.yard.games.miner.ui;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import tula.yard.games.miner.IGame;
import tula.yard.games.miner.IGameplay;
import tula.yard.games.miner.gameplay.MinerGameplayDefaults;
import tula.yard.games.miner.service.ServiceOperation;
/**
 * Класс Окна статистики 
 * @author Дмитрий Ярославцев
 *
 */
public class StatisticWindow extends ChildWindow {
	
	public static final String TITLE = "STATISTIC";
	public static final String PLAYER_NAME = "clPLAYERNAME";
	public static final String FIELD_SIZE = "clFIELDSIZE";
	public static final String TIME = "clTIME";
	public static final String MINES_AT_LEVEL = "clMINESATLEVEL";
	
	public static final String SAVERESULT = "dlgSAVERESULT";
	public static final String ENTERNAME = "dlgENTERNAME";
	
	public static final int WINDOW_WIDTH = 240;
	public static final int WINDOW_HEIGHT = 300;	
	
	public static final String STATISTIC_FILE = "configuration/statistic.miners";
	
	private Properties statistic = new Properties(); 
	
	static class HeroRecord implements Serializable {
		protected String name;
		protected long data = 0;
		
		public HeroRecord(String name, long data) {
			this.name = name;
			this.data = data;
		}	
	}
	/**
	 * Класс Героя - запись в статистике о победившем игроке
	 * реализует интерфейс Comparable для сортировки
	 * @author Дмитрий Ярославцев
	 *
	 */
	static class Hero implements Comparable<Hero> {
		//2 байта - время, 2 байта - строки, 2 байта - колонки, 1 байт - число мин		
		private static final byte ROWS_OFFSET = 24; // смещение в битах числа строк игрового поля 
		private static final byte COLUMNS_OFFSET = 8; // смещение в битах числа столбцов игрового поля
		private static final byte TIME_OFFSET = 40; // смещение в битах времени, затраченного на игру
		private String name; // имя игрока
		private long data = 0; //сводная характеристика выигранной игры (байты показателей, см. выше)
		
		/**
		 * Весовая функция результата игры
		 * получена эмпирически, нужна для правильной сортировки игроков в списке статистики
		 * таким образом, чтобы сверху оказывались игроки, выигравшие наиболее сложные уровни за меньшее время
		 * чем больше плотность мин, тем сложнее игра
		 */
		public double getWeight() {
			double cells = getRows() * getColumns(); 
			
			return Math.exp(1000 * getMines() / cells) / (1 + getTime());
			
		}
		/**
		 * Конструктор объекта Герой
		 * @param name
		 * @param data
		 */
		public Hero(String name, long data) {
				this.name = name;
				this.data = data;
		}
		/**
		 * Еще один конструктор 	
		 * @param gameplay
		 * @param name
		 */
		public Hero(IGameplay gameplay, String name) {
			this.name = name;
			this.data =  ServiceOperation.getNsetIntProperty(gameplay.getGame().getProperties(), MinerGameplayDefaults.MINECOUNT_PROPERTY, 0) 
			          + (ServiceOperation.getNsetIntProperty(gameplay.getGame().getProperties(), MinerGameplayDefaults.COLUMNCOUNT_PROPERTY, 0) << COLUMNS_OFFSET) 
			          + (ServiceOperation.getNsetIntProperty(gameplay.getGame().getProperties(), MinerGameplayDefaults.ROWCOUNT_PROPERTY, 0) << ROWS_OFFSET) 
				      + (gameplay.getTime() << TIME_OFFSET);   
		}
		
		public int getRows() {
			return (int) ((data >> ROWS_OFFSET) & ServiceOperation.BIT_MASK_16);
		}
		
		public int getColumns() {
			return (int) ((data >> COLUMNS_OFFSET) & ServiceOperation.BIT_MASK_16);			
		}

		public int getTime() {
			return (int) ((data >> TIME_OFFSET) & ServiceOperation.BIT_MASK_16);			
			
		}

		public int getMines() {
			return (int) (data & ServiceOperation.BIT_MASK_8);						
		}

		
		public void setData(long data) {
			this.data = data;
		}

		public long getData() {
			return data;
		}
		
		public String getName() {
			return name;
		}

		
		@Override
		public int compareTo(Hero hero) {
			// TODO Auto-generated method stub
			return (int)( hero.getWeight() - this.getWeight());
		}
		
		

	}
	/**
	 * Читает статистику из файла, возвращает массив объектов класса Hero
	 * данные в файле расположены последовательно - сначала имя победителя, на следующей строке его результаты, и т.д.
	 * @return
	 */
	public Hero[] readStatistic() {
		ArrayList<Hero> result = new ArrayList();
				
		File file = new File(STATISTIC_FILE);
		if (file.exists()) 
			try {
				FileReader fileReader = new FileReader(file);
				BufferedReader bufferedReader = new BufferedReader(fileReader);
				String line;
				while ((line = bufferedReader.readLine()) != null) {
					String data = bufferedReader.readLine();
					result.add(new Hero(line, Long.parseLong(data))); //Long.getLong(data)));
				}
				fileReader.close();
			} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			}
		
		return (Hero[])result.toArray(new Hero[result.size()]);
				
		
	}
/**
 * Класс Окна статистики
 * game - ссылка на игру, parentWindow - ссылка на родительское окно	
 * @param game
 * @param parentWindow
 */
	public StatisticWindow(IGame game, Shell parentWindow) {		
		super(game, parentWindow, WINDOW_WIDTH, WINDOW_HEIGHT, TITLE);
		
		GridLayout gl = new GridLayout();
		gl.numColumns = 1;
		this.window.setLayout(gl);
		//GridData gd = new GridData(GridData.FILL_BOTH | GridData.GRAB_HORIZONTAL | GridData.GRAB_VERTICAL);
		Table info = new Table(this.window, SWT.NONE);
		info.setLayoutData(new GridData(GridData.FILL_BOTH | GridData.GRAB_HORIZONTAL | GridData.GRAB_VERTICAL));
		info.setHeaderVisible(true);		
		TableColumn clName = new TableColumn(info, SWT.NONE);
		clName.setWidth(90); clName.setText(this.getLocalizedText(PLAYER_NAME));
		TableColumn clField = new TableColumn(info, SWT.NONE);
		clField.setWidth(50); clField.setText(this.getLocalizedText(FIELD_SIZE));		
		TableColumn clMines = new TableColumn(info, SWT.NONE);
		clMines.setWidth(40); clMines.setText(this.getLocalizedText(MINES_AT_LEVEL));
		TableColumn clTime = new TableColumn(info, SWT.NONE);
		clTime.setWidth(50);  clTime.setText(this.getLocalizedText(TIME));
		Hero[] heroes = readStatistic();
		Arrays.sort(heroes);
		for (Hero hero : heroes) {
			TableItem ti = new TableItem(info, SWT.NONE);
			ti.setText(0, hero.getName());			
			ti.setText(1, hero.getRows() +" x " + hero.getColumns());
			ti.setText(2, String.valueOf(hero.getMines()));
			ti.setText(3, String.valueOf(hero.getTime()));
					
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
		this.window.open();
		
	}
	/**
	 * Сохраняет запись о победителе в файл статистики
	 * @param gameplay
	 */
	public static void saveHero(IGameplay gameplay) {
		InputDialog dlg = new InputDialog(gameplay.getGame(), SAVERESULT,
				ENTERNAME);
		String input = dlg.open();
		if (input != null) {
			Hero hero = new Hero(gameplay, input);
			try {
				File file = new File(STATISTIC_FILE);
				if (!file.exists()) {
					file.getParentFile().mkdirs();
					if (!file.createNewFile())
						return;
				}	
				PrintWriter out = new PrintWriter(new BufferedWriter(
						new FileWriter(STATISTIC_FILE, true)));
				out.println(hero.getName());
				out.println(hero.getData());
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}
}
