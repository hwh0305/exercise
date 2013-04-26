package org.hao.study.jetty;

import java.lang.reflect.Field;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;

import org.mortbay.jetty.Connector;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.nio.SelectChannelConnector;
import org.mortbay.jetty.webapp.WebAppContext;
import org.mortbay.thread.QueuedThreadPool;

import sun.misc.Launcher;
import sun.misc.URLClassPath;

public class JettyStart {

    /**
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        URLClassLoader cl = (URLClassLoader) Launcher.getLauncher().getClassLoader();

        Field ucp = URLClassLoader.class.getDeclaredField("ucp");
        ucp.setAccessible(true);
        URLClassPath urlClassPath = (URLClassPath) ucp.get(cl);

        Field pathField = urlClassPath.getClass().getDeclaredField("path");
        pathField.setAccessible(true);
        List<?> path = (List<?>) pathField.get(urlClassPath);

//        File tools = new File("/usr/alibaba/install/jdk1.6.0_25/lib/tools.jar");
//        URL toolsJar = tools.toURI().toURL();
//        path.add(toolsJar);

        URL[] urls = urlClassPath.getURLs();
        System.out.println(path);
        for (int i = 0; i < urls.length; ++i)
            System.out.println(i + ": " + urls[i]);

        Server server = new Server();
        Connector connector = new SelectChannelConnector();
        connector.setPort(8080);

        QueuedThreadPool pool = new QueuedThreadPool();

        server.setThreadPool(pool);
        server.addConnector(connector);

        WebAppContext context = new WebAppContext();
        context.setContextPath("/");
        context.setResourceBase("/home/hao/webroot/test");

        server.addHandler(context);
        server.start();
    }

}
