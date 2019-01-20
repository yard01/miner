package bookdb;

import java.net.URL;

import org.apache.log4j.PropertyConfigurator;
import org.eclipse.core.runtime.Platform;
import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.application.WorkbenchAdvisor;
import org.osgi.framework.Bundle;

/**
 * This class controls all aspects of the application's execution and is
 * contributed through the plugin.xml.
 */
public class Application implements IApplication {
	public static final String BUNDLE_NAME = "bookdb";

	@Override
	public Object start(IApplicationContext context) throws Exception {
		try {
			Bundle bundle = Platform.getBundle(Application.BUNDLE_NAME);
			URL fileURL = bundle.getEntry("log4j.properties");
			PropertyConfigurator.configure(fileURL.openStream());

		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		Display display = PlatformUI.createDisplay();
		WorkbenchAdvisor advisor = new ApplicationWorkbenchAdvisor();
		return PlatformUI.createAndRunWorkbench(display, advisor);
	}

	@Override
	public void stop() {
		// Do nothing
	}
}
