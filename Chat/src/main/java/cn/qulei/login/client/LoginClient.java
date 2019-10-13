package cn.qulei.login.client;

import cn.qulei.login.pojo.User;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

/**
 * @author QuLei
 */
public class LoginClient {
    public static void main(String[] args) {
        Socket client = null;
        OutputStream outputStream = null;
        DataInputStream inputStream = null;
        ObjectOutputStream objectOutputStream = null;
        try {
            while (true) {
                client = new Socket("localhost", 10001);
                outputStream = client.getOutputStream();
                //网络传输需要序列化

                inputStream = new DataInputStream(client.getInputStream());
                User user = getUser();
                objectOutputStream = new ObjectOutputStream(outputStream);
                objectOutputStream.writeObject(user);
                //接收服务端响应
                client.shutdownOutput();
                String str = inputStream.readUTF();
                System.out.println(str);
                client.shutdownInput();
                if ("登录成功".equals(str)) {
                    break;
                }
                inputStream.close();
                objectOutputStream.close();
                outputStream.close();
                client.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                //调用shutdownOutput关闭资源
                inputStream.close();
                objectOutputStream.close();
                outputStream.close();
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static User getUser() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入用户名：");
        String username = scanner.nextLine();
        System.out.println("请输入密码：");
        String password = scanner.nextLine();
        return new User(username, password);
    }
}
