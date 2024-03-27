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
        Properties properties = new Properties();
        ServletContext servletContext = servletContextEvent.getServletContext();
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        servletContext.setAttribute("sessionFactory", sessionFactory);
//        createTablesIfNotExist(servletContext);
    }

//    private void createTablesIfNotExist(ServletContext servletContext) {
//        try (InputStream inputStream = servletContext.getResourceAsStream("WEB-INF/resources/create-tables.sql")) {
//            if (inputStream == null) return;
//            dataBase.connect();
//            String sql = new String(inputStream.readAllBytes());
//            dataBase.getStatement().execute(sql);
//        } catch (IOException | SQLException e) {
//            e.printStackTrace();
//        } finally {
//            dataBase.disconnect();
//        }
//    }
}
