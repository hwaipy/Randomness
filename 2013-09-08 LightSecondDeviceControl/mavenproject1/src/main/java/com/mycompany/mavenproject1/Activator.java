package com.mycompany.mavenproject1;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {

    public void start(BundleContext context) throws Exception {
        // TODO add activation code here
        System.out.println(123);
    }

    public void stop(BundleContext context) throws Exception {
        // TODO add deactivation code here
    }

}
