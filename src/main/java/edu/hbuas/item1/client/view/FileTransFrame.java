package edu.hbuas.item1.client.view;

import edu.hbuas.item1.client.model.ChatMessage;
import edu.hbuas.item1.client.model.ChatMessageType;
import edu.hbuas.item1.client.model.ChatUser;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;


public class FileTransFrame extends JFrame {
    // private ObjectInputStream in;

    private ObjectOutputStream out;
    private ChatUser friend, my;
    private JPanel contentPane;
    private JFileChooser chooser;
    private final JButton button;
    private final JLabel label;
    private JLabel label_1;
    private JTextField jTextField_1;
    private final JButton button_1;
    private final JButton button_2;
    private byte[] bs;

    /**
     * Launch the application.
     *//*
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    FileTransFrame frame = new FileTransFrame();
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
    public FileTransFrame(ChatUser friend, ChatUser my, ObjectOutputStream out) {
        this.friend = friend;
        this.my = my;
        this.out = out;
        //this.in=in;
        chooser = new JFileChooser(".");
        setTitle("\u6587\u4EF6\u4F20\u8F93\u7A97\u53E3");
        setResizable(false);
        setDefaultCloseOperation(HIDE_ON_CLOSE);
        setBounds(100, 100, 254, 176);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        button = new JButton("选择文件");
        button.setBounds(10, 10, 93, 23);
        contentPane.add(button);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int result = chooser.showOpenDialog(null);
                if (result == JFileChooser.APPROVE_OPTION) {
                    String fileurl = chooser.getSelectedFile().getPath();
                    jTextField_1.setText(fileurl);
                    System.out.println(fileurl);
                    //label_1.setIcon(new ImageIcon(name));
                }
            }
        });

        label = new JLabel("文件路径");
        label.setBounds(10, 49, 54, 15);
        contentPane.add(label);

        jTextField_1 = new JTextField();
        jTextField_1.setBounds(65, 49, 180, 20);
        //ChatMessage filemessage=(ChatMessage) in.readObject();

        //jTextField_1.setEditable(false);
        contentPane.add(jTextField_1);


        JProgressBar progressBar = new JProgressBar();
        progressBar.setBounds(69, 87, 169, 14);
        contentPane.add(progressBar);

        JLabel lblNewLabel = new JLabel("\u4F20\u8F93\u8FDB\u5EA6");
        lblNewLabel.setBounds(10, 87, 54, 15);
        contentPane.add(lblNewLabel);

        button_1 = new JButton("发送");
        button_1.setBounds(20, 115, 93, 23);
        contentPane.add(button_1);
        button_1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String fileurl = jTextField_1.getText();
                ChatMessage message = new ChatMessage();
                ChatUser mysimple = new ChatUser();
                mysimple.setUsername(my.getUsername());
                mysimple.setNickname(my.getNickname());
                message.setFrom(mysimple);
                message.setTo(friend);
                message.setType(ChatMessageType.FILE);

                File file = new File(fileurl);
                bs = new byte[(int) file.length()];
                BufferedInputStream inputStream = null;
                try {
                    inputStream = new BufferedInputStream(new FileInputStream(file));
                    inputStream.read(bs);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                message.setBs(bs);
                try {
                    out.writeObject(message);
                    out.flush();

                } catch (IOException ex) {
                    ex.printStackTrace();
                }


            }
        });


        button_2 = new JButton("关闭");
        button_2.setBounds(123, 115, 93, 23);
        contentPane.add(button_2);
        button_2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                FileTransFrame.this.setVisible(false);
            }
        });
    }
}
