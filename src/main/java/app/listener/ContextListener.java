package app.listener;

import app.db.DataBase;

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
    private DataBase dataBase;
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        Properties properties = new Properties();
        ServletContext servletContext = servletContextEvent.getServletContext();
        try (InputStream input = servletContext.getResourceAsStream("/WEB-INF/resources/db.properties")) {
            if (input != null) {
                properties.load(input);

                dataBase = new DataBase(
                        properties.getProperty("db.className"),
                        properties.getProperty("db.url"),
                        properties.getProperty("db.user"),
                        properties.getProperty("db.password")
                );

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        servletContext.setAttribute("dataBase", dataBase);
        createTablesIfNotExist(servletContext);
    }

    private void createTablesIfNotExist(ServletContext servletContext) {
        try (InputStream inputStream = servletContext.getResourceAsStream("WEB-INF/resources/create-tables.sql")) {
            if (inputStream == null) return;
            dataBase.connect();
            String sql = new String(inputStream.readAllBytes());
            dataBase.getStatement().execute(sql);
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        } finally {
            dataBase.disconnect();
        }
    }
}
