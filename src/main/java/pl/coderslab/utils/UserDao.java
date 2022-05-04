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

    public static String[] getInfo(){
        String[] infoArr = new String[3];
        Scanner scanner = new Scanner(System.in);
        System.out.println("Podaj email:");
        infoArr[0] = scanner.nextLine();
        System.out.println("Podaj username:");
        infoArr[1] = scanner.nextLine();
        System.out.println("Podaj hasło:");
        infoArr[2] = scanner.nextLine();
        return infoArr;
    }

    public static int getId(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Podaj numer id");
        while (!scanner.hasNextInt()){
            System.out.println("Podaj poprawny numer id");
            scanner.next();
        }
        return scanner.nextInt();
    }
    //uzywamy
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

    public static void update(String[]info,int id){
        try(Connection connection = DbUtil.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_USERS_QUERY);
            preparedStatement.setString(1,info[0]);
            preparedStatement.setString(2,info[1]);
            preparedStatement.setString(3,info[2]);
            preparedStatement.setInt(4,id);
            preparedStatement.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public static void readUser(int id){
        try(Connection connection = DbUtil.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_USER_QUERY);
            preparedStatement.setInt(1,id);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                System.out.println("Email=" + resultSet.getString("email"));
                System.out.println("UserName=" + resultSet.getString("username"));
                System.out.println("Password=" + resultSet.getString("password"));
            }
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
