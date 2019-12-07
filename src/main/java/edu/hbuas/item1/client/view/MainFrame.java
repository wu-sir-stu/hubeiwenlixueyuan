package edu.hbuas.item1.client.view;

import edu.hbuas.item1.client.model.ChatMessage;
import edu.hbuas.item1.client.model.ChatUser;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class MainFrame extends JFrame {
    private ChatUser user;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private JPanel contentPane;
    private JLabel lblNewLabel;
    private final JLabel label;
    private final JTextArea textArea;
    private final JScrollPane scrollPane;
    private final JTree tree;
    private Map<Long, ChatFrame> allChatFrame = new HashMap<>();

    /**
     * Create the frame.
     */
    public MainFrame(ChatUser user, ObjectOutputStream out, ObjectInputStream in) {
        this.user = user;
        this.out = out;
        this.in = in;
        setTitle("\u804A\u5929\u4E3B\u7A97\u53E3");
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 260, 600);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);


        lblNewLabel = new JLabel("");
        lblNewLabel.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().createImage(getClass().getClassLoader().getResource(user.getImage())).getScaledInstance(80, 80, Image.SCALE_DEFAULT)));
        lblNewLabel.setForeground(Color.LIGHT_GRAY);
        lblNewLabel.setBounds(10, 10, 80, 80);
        contentPane.add(lblNewLabel);

        label = new JLabel(user.getNickname());
        label.setBounds(115, 10, 100, 15);
        contentPane.add(label);

        textArea = new JTextArea();
        textArea.setText(user.getSignature());
        textArea.setEnabled(false);
        textArea.setBounds(115, 35, 100, 80);
        textArea.setLineWrap(true);
        contentPane.add(textArea);

        scrollPane = new JScrollPane();
        scrollPane.setBounds(10, 119, 234, 432);
        contentPane.add(scrollPane);


        //在主窗口的构造器里，动态读取好友列表，然后将好友列表挂载到jtree上，用来显示所有的好友

        DefaultMutableTreeNode root = new DefaultMutableTreeNode("所有好友");
        for (ChatUser u : user.getFriends()) {
            DefaultMutableTreeNode friend = new DefaultMutableTreeNode(u.getUsername() + "(" + u.getNickname() + ")");
            root.add(friend);
        }

        tree = new JTree(root);
        tree.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && e.getButton() == 1) {//判断当用户鼠标左键双击时应该执行代码
                    DefaultMutableTreeNode yourChoice = (DefaultMutableTreeNode) tree.getSelectionPath().getLastPathComponent();//通过jtree方法获取当前鼠标双击的是哪一个节点
                    if (((DefaultMutableTreeNode) tree.getSelectionPath().getLastPathComponent()).isLeaf()) {
                        System.out.println("您点击的好友名称是：" + yourChoice);
                        String username = yourChoice.toString().substring(0, yourChoice.toString().indexOf("("));
                        ChatUser friend = null;
                        for (ChatUser u : user.getFriends()) {
                            if (u.getUsername() == Long.parseLong(username)) {
                                friend = u;
                                break;
                            }
                        }
                        if (allChatFrame.containsKey(Long.parseLong(username))) {
                            allChatFrame.get(Long.parseLong(username)).setVisible(true);
                        } else {
                            System.out.println(friend);
                            ChatFrame c = new ChatFrame(friend, user, out, in);
                            allChatFrame.put(Long.parseLong(username), c);
                            c.setVisible(true);
                        }

                    }
                }
            }
        });
        scrollPane.setViewportView(tree);

        //在主窗口的构造器最后一行开启一个接受服务器转发过来消息的线程（只有线程才能保证接受消息和ui功能互不影响）
        class ReciveThread extends Thread {
            @Override
            public void run() {
                while (true) {
                    try {
                        ChatMessage message = (ChatMessage) in.readObject();//read方法如果能读取到一条消息，说明服务器转发给我了一条别人发给我的消息
                        ChatFrame friendChatFrame = null;
                        long friendUsername = message.getFrom().getUsername();//获取消息发送人的账号

                        if (allChatFrame.containsKey(friendUsername)) {
                            friendChatFrame = allChatFrame.get(message.getFrom().getUsername());//通过对方的账号，获取之前打开过的和该好友的聊天窗口对象
                        } else {
                            friendChatFrame = new ChatFrame(message.getFrom(), user, out, in);
                            allChatFrame.put(friendUsername, friendChatFrame);//如果之前没有打开过，执行上面的打开新聊天窗口的代码，打开后，再将这个窗口存入到集合中
                        }
                        //friendChatFrame.setVisible(true);//如果之前打开过，则直接从集合里获取这个账号对应的chat窗口对象，直接调用setvisiable显示它


                        //上面是接到消息，一定要打开窗口，下面是解析消息类型，不同都消息执行不同的代码
                        switch (message.getType()) {
                            case TEXT: {

                                friendChatFrame.getTextArea().append(message.getFrom().getNickname() + "   " + message.getTime() + ":\r\n" + message.getContent() + "\r\n\r\n");
                                //动态往聊天窗口上添加消息后，文本框不会自动该滚动到底部，执行如下代码滚动条自动滚动到地步
                                friendChatFrame.getTextArea().setSelectionStart(friendChatFrame.getTextArea().getText().length());//设置光标移动到文本框最后一个文字后面
                                friendChatFrame.setVisible(true);//如果之前打开过，则直接从集合里获取这个账号对应的chat窗口对象，直接调用setvisiable显示它
                                break;
                            }
                            case SHAKE: {
                                friendChatFrame.getTextArea().append(message.getFrom().getNickname() + "   " + message.getTime() + ":\r\n对方给您发送了一个窗口抖动...\r\n\r\n");
                                //动态往聊天窗口上添加消息后，文本框不会自动该滚动到底部，执行如下代码滚动条自动滚动到地步
                                friendChatFrame.getTextArea().setSelectionStart(friendChatFrame.getTextArea().getText().length());//设置光标移动到文本框最后一个文字后面
                                friendChatFrame.shakeWindow();//调用窗口的shake方法，执行抖动
                                friendChatFrame.setVisible(true);//如果之前打开过，则直接从集合里获取这个账号对应的chat窗口对象，直接调用setvisiable显示它
                                break;
                            }
                            case FILE: {
                                System.out.println("接收到文件！");

                                System.out.println(Arrays.toString(message.getBs()));
                                JFileChooser jFileChooser = new JFileChooser();
                                jFileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
                                jFileChooser.showSaveDialog(null);
                                String savefileurl = jFileChooser.getSelectedFile().getPath();
                                File savefile = new File(savefileurl);
                                BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(savefile));
                                outputStream.write(message.getBs(), 0, message.getBs().length);
                                outputStream.close();
                                //FileTransFrame fileTransFrame=new FileTransFrame(message.getFrom(),user,out);
                                //fileTransFrame.setVisible(true);
                                break;
                            }
                            case UPDATE: {
                                System.out.println("更新用户信息");
                                if (message.getRegister()) {
                                    label.setText(message.getFrom().getNickname());
                                    textArea.setText(message.getFrom().getSignature());
                                    JOptionPane.showMessageDialog(MainFrame.this, "用户信息更新成功！", "温馨提示", JOptionPane.ERROR_MESSAGE);

                                }
                            }
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        ReciveThread recive = new ReciveThread();
        recive.start();


    }
}
