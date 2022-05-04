package pl.coderslab.utils;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class UserDao {
    private static final String INSERT_INTO_USERS_QUERY = "INSERT INTO users(email, username, password) values (?,?,?);";
    private static final String UPDATE_USERS_QUERY = "update users set email = ? , username = ?, password = ? where id = ?";
    private static final String SELECT_USER_QUERY = "select * from users where id = ?";
    private static final String SELECT_ALL_USERS_QUERY = "SELECT * FROM users";
    private static final String DELETE_USER_QUERY = "DELETE from users where id = ?;";

    public static void addUser(String username , String email , String password){
        try(Connection connection = DbUtil.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT_INTO_USERS_QUERY,PreparedStatement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, email);
            preparedStatement.setString(2,username);
            preparedStatement.setString(3, password);
            preparedStatement.executeUpdate();
            Users users = new Users(email,username,password);
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if (!resultSet.next()){
                users.setId(resultSet.getInt(1));
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
//email username password // 4 jest id bo po nim szukamy
    public static void update(String email , String username, String password ,int id){
        try(Connection connection = DbUtil.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_USERS_QUERY);
            preparedStatement.setString(1,email);
            preparedStatement.setString(2,username);
            preparedStatement.setString(3,password);
            preparedStatement.setInt(4,id);
            preparedStatement.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public static String[] readUser(int id){
        try(Connection connection = DbUtil.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_USER_QUERY);
            preparedStatement.setInt(1,id);
            ResultSet resultSet = preparedStatement.executeQuery();
            String[] emailNamePass = new String[3];
            while (resultSet.next()){
                emailNamePass[0] = resultSet.getString("email");
                emailNamePass[1] = resultSet.getString("username");
                emailNamePass[2] = resultSet.getString("password");
            }
            return emailNamePass;
        }catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    public static void delete(int id){
        try (Connection connection = DbUtil.getConnection()){
            PreparedStatement preparedStatement = connection.prepareStatement(DELETE_USER_QUERY);
            preparedStatement.setInt(1,id);
            preparedStatement.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    //klasa uzywana do pobierania wszystkich użytkowników
    public static List<Users> readAllUsers(){
        try(Connection connection = DbUtil.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_USERS_QUERY);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Users> users = new ArrayList<>();
            while (resultSet.next()){
                String idStr = resultSet.getString("id");
                int id = Integer.parseInt(idStr);
                Users user = new Users(resultSet.getString("email"),resultSet.getString("username"),resultSet.getString("password"));
                user.setId(id);
                users.add(user);
            }
            return users;
        }catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

}
