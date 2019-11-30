package edu.hbuas.item1.client.view;

import edu.hbuas.item1.client.model.ChatMessage;
import edu.hbuas.item1.client.model.ChatMessageType;
import edu.hbuas.item1.client.model.ChatUser;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class RegisterFrame extends JFrame {


    private JPanel contentPane;
    private JTextField textField;
    private JPasswordField passwordField;
    private JPasswordField passwordField_1;
    private JTextField textField_1;


    private ObjectOutputStream out;
    private ObjectInputStream in;
    private final JButton btnNewButton;
    private final JRadioButton radioButton;
    private final JRadioButton radioButton_1;
    private final JComboBox comboBox;
    private final JTextArea textArea;
    private final ButtonGroup buttonGroup;
    private final JButton button;


    /**
     * Create the frame.
     */
    public RegisterFrame(ObjectOutputStream out, ObjectInputStream in) {
        this.out = out;
        this.in = in;
        setResizable(false);
        setTitle("\u804A\u5929\u6CE8\u518C\u7A97\u53E3");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 301, 500);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);
        setDefaultCloseOperation(HIDE_ON_CLOSE);

        JLabel label = new JLabel("\u7528\u6237\u540D");
        label.setBounds(55, 57, 54, 15);
        contentPane.add(label);

        JLabel label_1 = new JLabel("\u5BC6\u7801");
        label_1.setBounds(55, 106, 54, 15);
        contentPane.add(label_1);

        JLabel label_2 = new JLabel("\u786E\u8BA4\u5BC6\u7801");
        label_2.setBounds(55, 154, 54, 15);
        contentPane.add(label_2);

        JLabel label_3 = new JLabel("\u6635\u79F0");
        label_3.setBounds(55, 205, 54, 15);
        contentPane.add(label_3);

        JLabel label_4 = new JLabel("\u6027\u522B");
        label_4.setBounds(55, 248, 54, 15);
        contentPane.add(label_4);

        JLabel label_5 = new JLabel("\u5E74\u9F84");
        label_5.setBounds(55, 303, 54, 15);
        contentPane.add(label_5);

        JLabel label_6 = new JLabel("\u4E2A\u6027\u7B7E\u540D");
        label_6.setBounds(55, 357, 54, 15);
        contentPane.add(label_6);

        textField = new JTextField();
        textField.setBounds(144, 54, 103, 21);
        contentPane.add(textField);
        textField.setColumns(10);

        passwordField = new JPasswordField();
        passwordField.setBounds(144, 103, 103, 21);
        contentPane.add(passwordField);

        passwordField_1 = new JPasswordField();
        passwordField_1.setBounds(144, 151, 103, 21);
        contentPane.add(passwordField_1);

        textField_1 = new JTextField();
        textField_1.setBounds(144, 202, 103, 21);
        contentPane.add(textField_1);
        textField_1.setColumns(10);

        radioButton = new JRadioButton("\u7537");
        radioButton.setSelected(true);
        radioButton.setBounds(144, 259, 53, 23);
        contentPane.add(radioButton);

        radioButton_1 = new JRadioButton("\u5973");
        radioButton_1.setBounds(199, 259, 48, 23);
        contentPane.add(radioButton_1);
        buttonGroup = new ButtonGroup();
        buttonGroup.add(radioButton);
        buttonGroup.add(radioButton_1);


        comboBox = new JComboBox();
        comboBox.setModel(new DefaultComboBoxModel(new java.lang.String[]{"1", "2", "3", "4"}));
        comboBox.setBounds(144, 300, 103, 21);
        contentPane.add(comboBox);

        textArea = new JTextArea();
        textArea.setBounds(146, 353, 101, 55);
        contentPane.add(textArea);

        btnNewButton = new JButton("\u6CE8\u518C");
        btnNewButton.setBounds(55, 429, 93, 23);
        contentPane.add(btnNewButton);
        btnNewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = textField.getText().trim();
                String password = new String(passwordField.getPassword());
                String password_1 = new String(passwordField_1.getPassword());
                if (!password.equals(password_1)) {
                    JOptionPane.showMessageDialog(RegisterFrame.this, "请在此检查密码，两次输入密码不一致！", "温馨提示", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                String nickname = textField_1.getText();
                String sex;
                if (radioButton.isSelected()) {
                    sex = "男";
                } else {
                    sex = "女";
                }
                long age = Long.parseLong((String) comboBox.getSelectedItem());
                String signature = textArea.getText();
                //把消息封装
                ChatMessage chatMessage = new ChatMessage();
                ChatUser user = new ChatUser();
                user.setUsername(Long.parseLong(username));
                user.setPassword(password);
                user.setNickname(nickname);
                user.setSex(sex);
                user.setAge(age);
                user.setSignature(signature);
                user.setImage("images/2.gif");
                chatMessage.setFrom(user);
                chatMessage.setType(ChatMessageType.REGISTER);
                //用流传输数据
                try {
                    out.writeObject(chatMessage);
                    out.flush();
                } catch (IOException ex) {
                    System.err.println("注册消息发送失败！");

                }

                try {
                    ChatMessage r = (ChatMessage) in.readObject();
                    if (r.getRegister()) {
                        System.out.println("注册成功！");
                        JOptionPane.showMessageDialog(RegisterFrame.this, "注册成功！", "温馨提示", JOptionPane.ERROR_MESSAGE);
                    } else {
                        return;
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                } catch (ClassNotFoundException ex) {
                    ex.printStackTrace();
                }

            }
        });

        button = new JButton("\u8FD4\u56DE\u767B\u5F55");
        button.setBounds(154, 429, 93, 23);
        contentPane.add(button);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                RegisterFrame.this.setVisible(false);
            }
        });
    }
}
