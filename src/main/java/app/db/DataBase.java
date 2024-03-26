package app.db;

import java.sql.*;


public class DataBase {
    private String className;
    private String url;
    private String user;
    private String password;
    private Connection connection;
    private Statement statement;


    public DataBase(String className, String url, String user, String password) {
        this.className = className;
        this.url = url;
        this.user = user;
        this.password = password;
    }

    public void connect() {
        try {
            Class.forName(className);
            connection = DriverManager.getConnection(url, user, password);
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






