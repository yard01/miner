package tula.yard.games.miner;

/**
 * Интерфейс генймплея 
 * @author Дмитрий
 *
 */
public interface IGameplay {
	public void createContent(); //создаёт контент 
	public IGame getGame(); // возвращает ссылку на игру
	public Object getState(); // возвращает состояние геймплея
	public long getTime(); // возвращает игровое время
}
