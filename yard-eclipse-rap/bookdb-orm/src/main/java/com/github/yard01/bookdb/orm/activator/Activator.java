package com.github.yard01.bookdb.orm.activator;

import com.github.yard01.bookdb.orm.DBCommandProcessor;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		//System.out.println("Hello World!!");
		//DBCommandProcessor dbcp = new DBCommandProcessor("bob", "123");
		
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		//System.out.println("Goodbye World!!");
	}

}
