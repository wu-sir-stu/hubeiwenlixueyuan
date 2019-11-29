package edu.hbuas.item1.client.view;

import java.sql.*;

public class test {
    public static void main(String[] args) {
        Connection con;
        String driver = "com.mysql.jdbc.Driver";
        //这里我的数据库是cgjr
        String url = "jdbc:mysql://localhost:3306/littlechat?useUnicode=true&characterEncoding=utf-8&useSSL=false";
        String user = "root";
        String password = "123456";
        try {
            Class.forName(driver);
            con = DriverManager.getConnection(url, user, password);
            if (!con.isClosed()) {
                System.out.println("数据库连接成功");
            }
            Statement statement = con.createStatement();

//            执行查询语句
            String sql = "select * from user;";//我的表格叫persons
            ResultSet resultSet = statement.executeQuery(sql);


//            打印查询出来的东西
            String username;
            String name;
            String password1;
            while (resultSet.next()) {
                username = resultSet.getString("username");
                name = resultSet.getString("name");
                password1 = resultSet.getString("password");
                System.out.println(username + '\t' + name + '\t' + password1);
            }


            //            执行插入语句
            //String sql2="INSERT INTO `persons` (`name`, `num`) VALUES ('徐志摩', '45');";
            //statement.executeUpdate(sql2);

//                       执行更新语句
            //String sql3="UPDATE persons set num=66 WHERE `name`=\"徐志摩\"";
            //statement.executeUpdate(sql3);


//               执行删除语句
            //String sql4="delete from persons WHERE `name`=\"徐志摩\"";
            //statement.executeUpdate(sql4);

//            执行调用存储过程

            //String sql5="call add_student(3)";
            //statement.executeUpdate(sql5);


//            关闭连接
            resultSet.close();
            con.close();
            System.out.println("数据库已关闭连接");
        } catch (ClassNotFoundException e) {
            System.out.println("数据库驱动没有安装");

        } catch (SQLException e) {
            System.out.println("数据库连接失败");
        }
    }
}
