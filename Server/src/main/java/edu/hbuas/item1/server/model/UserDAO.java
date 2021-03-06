package edu.hbuas.item1.server.model;

import edu.hbuas.item1.client.model.ChatMessage;
import edu.hbuas.item1.client.model.ChatUser;

import java.sql.*;
import java.util.*;

public class UserDAO {
    private Connection con;
    private Statement sta;

    public Connection getCon() {
        try {
            con = DriverManager.getConnection(properties.get("url").toString(), properties.get("username").toString(), properties.get("password").toString());
            sta = con.createStatement();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return con;
    }

    public Statement getSta() {
        try {
            return getCon().createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public PreparedStatement getPre(String sql) {
        try {
            return getCon().prepareCall(sql);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    private PreparedStatement pre;
    private Properties properties;

    {
        properties = new Properties();
        try {
            System.out.println(getClass().getClassLoader().getResource("jdbc.properties"));
            properties.load(getClass().getClassLoader().getResourceAsStream("jdbc.properties"));
            Class.forName(properties.get("driverClassName").toString());
            con = DriverManager.getConnection(properties.get("url").toString(), properties.get("username").toString(), properties.get("password").toString());
            sta = con.createStatement();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 数据库登陆方法，根据传入的用户名和密码查询返回一个用户对象
     *返回对象中包含传入用户对象的信息和它的好友。
     * @param user
     * @return
     */
    public ChatUser login(ChatUser user) {
        ChatUser u = null;
        PreparedStatement pre = getPre("select * from user where username=? and password=?");
        try {
            pre.setLong(1, user.getUsername());
            pre.setString(2, user.getPassword());
            ResultSet rs = pre.executeQuery();
            if (rs.next()) {
                u = new ChatUser();
                u.setUsername(rs.getLong("username"));
                u.setNickname(rs.getString("nickname"));
                u.setSex(rs.getString("sex"));
                u.setAge(rs.getInt("age"));
                u.setImage(rs.getString("image"));
                u.setSignature(rs.getString("signature"));
                Set<ChatUser> users = getAllUsers(user.getUsername());
                u.setFriends(users);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            return u;
        }
    }

    /**
     * 注册新用户的方法，根据传入的注册用户信息向数据库添加一个新的用户
     *
     * @param user
     * @return
     */
    public boolean register(ChatUser user) {
        boolean added = false;
        PreparedStatement pre = getPre("insert into user(username,password,nickname,sex,age,image,signature) values(?,?,?,?,?,?,?)");
        try {
            pre.setLong(1, user.getUsername());
            pre.setString(2, user.getPassword());
            pre.setString(3, user.getNickname());
            pre.setString(4, user.getSex());
            pre.setInt(5, (int) user.getAge());
            pre.setString(6, user.getImage());
            pre.setString(7, user.getSignature());
            added = pre.executeUpdate() > 0 ? true : false;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return added;
    }

    public boolean upchatuser(ChatUser user) {
        boolean up = false;
        PreparedStatement pre = getPre("update user set nickname=? , age=? , signature=? where username=?");
        try {
            pre.setString(1, user.getNickname());
            pre.setLong(2, user.getAge());
            pre.setString(3, user.getSignature());
            pre.setLong(4, user.getUsername());
            up = pre.executeUpdate() > 0 ? true : false;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return up;
    }
    /**
     * 查询除了自己账号以外的所有聊天用户
     *
     * @param username
     * @return
     */
    public Set<ChatUser> getAllUsers(long username) {
        Set<ChatUser> users = new HashSet<>();
        PreparedStatement pre = getPre("select  * from user where username !=?");
        try {
            pre.setLong(1, username);
            ResultSet rs = pre.executeQuery();
            while (rs.next()) {
                ChatUser u = new ChatUser();
                u.setUsername(rs.getLong("username"));
                u.setNickname(rs.getString("nickname"));
                u.setSex(rs.getString("sex"));
                u.setAge(rs.getInt("age"));
                u.setImage(rs.getString("image"));
                u.setSignature(rs.getString("signature"));
                users.add(u);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return users;
        }
    }


    /**
     * 添加离线消息
     */
    public boolean addload(ChatMessage chatMessage) {
        boolean added = false;
        PreparedStatement pre = getPre("insert into loadmessage(fromusername,tousername,message,time) values(?,?,?,?)");
        try {
            pre.setLong(1, chatMessage.getFrom().getUsername());
            pre.setLong(2, chatMessage.getTo().getUsername());
            pre.setString(3, chatMessage.getContent());
            pre.setString(4, chatMessage.getTime());

            added = pre.executeUpdate() > 0 ? true : false;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return added;
    }


    /**
     * 读取离线消息
     */
    public Queue<ChatMessage> duload(ChatUser user) {
        Queue<ChatMessage> messages = new LinkedList<>();
        ChatMessage chatMessage = null;
        ChatUser c = null;
        PreparedStatement pre = getPre("select * from loadmessage where tousername=? ");
        try {
            pre.setLong(1, user.getUsername());

            ResultSet rs = pre.executeQuery();
            while (rs.next()) {
                c = new ChatUser();
                chatMessage = new ChatMessage();
                c.setUsername(rs.getLong("fromusername"));

                chatMessage.setFrom(chaxun(c));
                chatMessage.setTo(chaxun(user));//登陆用户
                chatMessage.setContent(rs.getString("message"));
                chatMessage.setTime(rs.getString("time"));
                messages.offer(chatMessage);

            }


        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            deload(user);
            return messages;
        }
    }

    /**
     * 删除离线消息
     */
    public void deload(ChatUser user) {
        PreparedStatement pre = getPre("delete  from loadmessage where tousername=? ");
        try {
            pre.setLong(1, user.getUsername());
            pre.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 查询用户
     */
    public ChatUser chaxun(ChatUser user) {
        ChatUser u = null;
        PreparedStatement pre = getPre("select * from user where username=? ");
        try {
            pre.setLong(1, user.getUsername());
            ResultSet rs = pre.executeQuery();
            if (rs.next()) {
                u = new ChatUser();
                u.setUsername(rs.getLong("username"));
                u.setNickname(rs.getString("nickname"));
                u.setSex(rs.getString("sex"));
                u.setAge(rs.getInt("age"));
                u.setImage(rs.getString("image"));
                u.setSignature(rs.getString("signature"));
                Set<ChatUser> users = getAllUsers(user.getUsername());
                u.setFriends(users);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            return u;
        }
    }

}
