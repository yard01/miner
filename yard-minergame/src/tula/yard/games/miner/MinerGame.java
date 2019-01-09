/**
 * 
 */
package tula.yard.games.miner;

import java.io.IOException;
import java.util.Locale;
import java.util.Properties;

import tula.yard.games.miner.service.ServiceOperation;
import tula.yard.games.miner.ui.GameWindow;

/**
 * @author User
 *
 */
public class MinerGame implements IGame {
    protected static final byte SYSTEM_ERROR = 1;
    protected static final String CONFIG_DIR = "configuration";
	protected static final String CONFIG_FILE = "/config.properties";
	protected static final String DEFAULT_CONFIG_RES = "/default.properties";
	protected static final String LOCAL_RES_DIR = "/localization/";
	protected static final String LOCAL_FILE_EXT = ".loc";
	
	protected static final String EN_LOCALE_CODE = "en_EN";
	
	private Properties defaultProperties = new Properties();
	
	private Properties properties = null;
	
	private Properties locale = new Properties();
	private Properties en_locale = new Properties();
	private IGameplay gameplay;
	/* (non-Javadoc)
	 * @see tula.yard.games.miner.IGame#init()
	 */
	
	@Override
	public void init() {
		// TODO Auto-generated method stub
		try {
			defaultProperties.load(this.getClass().getResourceAsStream(DEFAULT_CONFIG_RES));
			en_locale.load(this.getClass().getResourceAsStream(LOCAL_RES_DIR + EN_LOCALE_CODE + LOCAL_FILE_EXT));
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(SYSTEM_ERROR);
		}
		
		//System.
		properties = ServiceOperation.loadPropertiesFromFile(CONFIG_DIR + CONFIG_FILE, defaultProperties);
		
		String localCode = Locale.getDefault().toString();
		
		try {
			locale.load(this.getClass().getResourceAsStream(LOCAL_RES_DIR + localCode + LOCAL_FILE_EXT));
			
		} catch (Exception e) {
			locale = en_locale;
		}
		
	}

	/* (non-Javadoc)
	 * @see tula.yard.games.miner.IGame#start()
	 */
	@Override
	public void start() {
		// TODO Auto-generated method stub
		new GameWindow(this);
	}

	/* (non-Javadoc)
	 * @see tula.yard.games.miner.IGame#stop()
	 */
	@Override
	public void stop() {
		// TODO Auto-generated method stub
		ServiceOperation.savePropertiesToFile(CONFIG_DIR, CONFIG_FILE, properties);
	}

	/* (non-Javadoc)
	 * @see tula.yard.games.miner.IGame#getProperties()
	 */
	@Override
	public Properties getProperties() {
		// TODO Auto-generated method stub
		return properties;
	}

	/* (non-Javadoc)
	 * @see tula.yard.games.miner.IGame#saveProperties()
	 */
	protected void saveProperties() {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see tula.yard.games.miner.IGame#loadProperties()
	 */
	
	protected void loadProperties() {
		// TODO Auto-generated method stub

	}
	
	public static void main(String[] args) {
		IGame game = new MinerGame();
		game.init();
		game.start();
		
	}

	@Override
	public Properties getDefaultProperties() {
		// TODO Auto-generated method stub
		return defaultProperties;
	}

	@Override
	public Properties getLocale() {
		// TODO Auto-generated method stub
		return locale;
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void restart() {
		// TODO Auto-generated method stub
		this.gameplay.createContent();
	}

	@Override
	public void setMode(int mode) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getMode() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void assignGameplay(IGameplay gameplay) {
		// TODO Auto-generated method stub
		this.gameplay = gameplay;
		 
	}
}
