package edu.hbuas.item1.client.view;

import edu.hbuas.item1.client.model.ChatMessage;
import edu.hbuas.item1.client.model.ChatMessageType;
import edu.hbuas.item1.client.model.ChatUser;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class Updateinformation extends JFrame {

    private ObjectOutputStream out;
    private ChatUser user;
    private JPanel contentPane;
    private JTextField textField;
    private JTextField textField_1;
    private JTextField textField_2;
    private JLabel lblNewLabel;
    private final JButton button;
    private final JButton button_1;

    /**
     * Launch the application.
     *//*
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Updateinformation frame = new Updateinformation();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}*/

    /**
     * Create the frame.
     */
    public Updateinformation(ObjectOutputStream out, ChatUser user) {
        this.out = out;
        this.user = user;
        setTitle("\u4FEE\u6539\u4E2A\u4EBA\u4FE1\u606F");
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setBounds(100, 100, 451, 630);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel label = new JLabel("\u6635\u79F0\uFF1A");
        label.setBounds(37, 41, 63, 15);
        contentPane.add(label);

        textField = new JTextField();
        textField.setBounds(159, 38, 223, 21);
        contentPane.add(textField);
        textField.setColumns(10);

        JLabel label_1 = new JLabel("\u4E2A\u6027\u7B7E\u540D\uFF1A");
        label_1.setBounds(37, 123, 68, 15);
        contentPane.add(label_1);

        textField_1 = new JTextField();
        textField_1.setBounds(162, 116, 220, 21);
        contentPane.add(textField_1);
        textField_1.setColumns(10);

        JLabel label_2 = new JLabel("\u5E74\u9F84\uFF1A");
        label_2.setBounds(39, 219, 54, 15);
        contentPane.add(label_2);

        textField_2 = new JTextField();
        textField_2.setBounds(159, 216, 223, 21);
        contentPane.add(textField_2);
        textField_2.setColumns(10);


        button = new JButton("保存");
        button.setBounds(39, 496, 93, 23);
        contentPane.add(button);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String nickname = textField.getText();
                String signature = textField_1.getText();
                String age = textField_2.getText();
                ChatUser upuser = new ChatUser();
                ChatMessage chatMessage = new ChatMessage();
                upuser.setUsername(user.getUsername());
                upuser.setNickname(nickname);
                upuser.setSignature(signature);
                upuser.setAge(Long.parseLong(age));
                chatMessage.setFrom(upuser);
                chatMessage.setType(ChatMessageType.UPDATE);
                try {
                    out.writeObject(chatMessage);
                    out.flush();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

            }
        });

        button_1 = new JButton("取消");
        button_1.setBounds(249, 496, 93, 23);
        contentPane.add(button_1);
        button_1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Updateinformation.this.setVisible(false);
            }
        });
    }
}
