package app.listener;

import app.HibernateUtil;
import org.hibernate.SessionFactory;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Properties;

@WebListener
public class ContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {

        ServletContext servletContext = servletContextEvent.getServletContext();
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        servletContext.setAttribute("sessionFactory", sessionFactory);

    }

}
