package app.db;

import java.sql.*;


public class DataBase {
    private String className;
    private String URL;
    private String USER;
    private String PASSWORD;
    private Connection connection;
    private Statement statement;


    public DataBase(String className, String URL, String USER, String PASSWORD) {
        this.className = className;
        this.URL = URL;
        this.USER = USER;
        this.PASSWORD = PASSWORD;
    }

    public void connect() {
        try {
            Class.forName(className);
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Connection successful");
            statement = connection.createStatement();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void disconnect() {
        try {
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public PreparedStatement getPreparedStatement(String query) throws SQLException {
        return connection.prepareStatement(query);
    }

    public PreparedStatement getPreparedStatement(String query, int constant) throws SQLException {
        return connection.prepareStatement(query, constant);
    }

    public Statement getStatement() {
        return statement;
    }
}






