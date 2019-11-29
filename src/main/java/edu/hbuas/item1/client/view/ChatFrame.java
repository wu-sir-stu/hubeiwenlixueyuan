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
import java.util.Date;

public class ChatFrame extends JFrame {

    private ObjectOutputStream out;
    private ObjectInputStream in;
    private JPanel contentPane;
    private ChatUser friend, my;
    private final JScrollPane scrollPane;

    public JTextArea getTextArea() {
        return textArea;
    }

    private final JTextArea textArea;
    private final JScrollPane scrollPane_1;
    private final JTextArea textArea_1;
    private final JButton button;
    private final JButton button_1;
    private final JButton btnFile;
    private final JButton btnEmoj;


    /**
     * Create the frame.
     */
    public ChatFrame(ChatUser friend, ChatUser my, ObjectOutputStream out, ObjectInputStream in) {

        this.friend = friend;
        this.my = my;
        this.out = out;
        this.in = in;
        setTitle("和【" + friend.getNickname() + "】聊天中...");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 450, 445);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);
        setDefaultCloseOperation(HIDE_ON_CLOSE);

        scrollPane = new JScrollPane();
        scrollPane.setBounds(10, 10, 414, 203);
        contentPane.add(scrollPane);

        textArea = new JTextArea();
        scrollPane.setViewportView(textArea);
        textArea.setEditable(false);

        scrollPane_1 = new JScrollPane();
        scrollPane_1.setBounds(10, 253, 414, 103);
        contentPane.add(scrollPane_1);

        textArea_1 = new JTextArea();
        scrollPane_1.setViewportView(textArea_1);

        button = new JButton("\u53D1\u9001");
        button.setBounds(201, 374, 93, 23);
        contentPane.add(button);
        button.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                String editMessage = textArea_1.getText();
                textArea.append(my.getNickname() + "  " + new Date().toString() + ":\r\n" + editMessage + "\r\n\r\n");
                textArea_1.setText("");
                ChatMessage message = new ChatMessage();
                ChatUser mysimple = new ChatUser();
                mysimple.setUsername(my.getUsername());
                mysimple.setNickname(my.getNickname());
                message.setFrom(mysimple);
                message.setTo(friend);
                message.setType(ChatMessageType.TEXT);
                message.setContent(editMessage);
                //5.使用底层的socket流将消息对象写入网络另外一端
                try {
                    out.writeObject(message);
                    out.flush();
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(ChatFrame.this, "消息发送失败！", "温馨提示", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        button_1 = new JButton("\u5173\u95ED");
        button_1.setBounds(319, 374, 93, 23);
        contentPane.add(button_1);

        btnFile = new JButton("file");
        btnFile.setBounds(10, 222, 70, 23);
        contentPane.add(btnFile);

        btnEmoj = new JButton("emoj");
        btnEmoj.setBounds(90, 222, 70, 23);
        contentPane.add(btnEmoj);
    }

}
