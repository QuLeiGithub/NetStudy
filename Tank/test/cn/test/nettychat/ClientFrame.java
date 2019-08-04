package cn.test.nettychat;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * @Description: 实现简单的聊天页面
 * * @Author:      QuLei
 * @CreateDate: 2019-08-04 12:15
 * @Version: 1.0
 */
public class ClientFrame extends Frame {
    public static final ClientFrame INSTANCE = new ClientFrame();
    private TextArea textArea = new TextArea();
    private TextField textField = new TextField();
    private Client client = null;

    private ClientFrame() {
        this.setSize(300, 400);
        this.setLocation(100, 20);
        this.add(textArea, BorderLayout.CENTER);
        this.add(textField, BorderLayout.SOUTH);
        this.setTitle("chatFrame");

        textField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //send to server
                client.send(textField.getText());
                textField.setText("");
            }
        });

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                client.closeConnection();
                System.exit(0);
            }
        });
    }

    public static void main(String[] args) {
        ClientFrame frame = ClientFrame.INSTANCE;
        frame.setVisible(true);
        frame.connectToServer();
    }

    /**
     * 设置窗口中的信息
     *
     * @param str
     */
    public void updateText(String str) {
        textArea.setText(textArea.getText() + str + "\r\n");
    }

    /**
     * 连接到服务器
     */
    public void connectToServer() {
        client = new Client();
        client.connect();
    }

}
