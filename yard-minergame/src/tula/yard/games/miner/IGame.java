/**
 * 
 */
package tula.yard.games.miner;

import java.util.Properties;

/**
 * @author Yard
 *
 */

public interface IGame {
	
	public void init(); // инициализация игры
	public void start(); // запуск
	public void stop(); // остановка
	public void pause();
	public void restart();
	public void assignGameplay(IGameplay gameplay);
	//public void createContent();
	public void setMode(int mode);
	public int getMode();		
	public Properties getProperties(); // возвращает настройки игры
	public Properties getDefaultProperties(); // возвращает настройки по умолчанию
	public Properties getLocale(); //возвращает текущую локализацию
	
}
