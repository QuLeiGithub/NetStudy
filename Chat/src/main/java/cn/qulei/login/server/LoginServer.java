package cn.qulei.login.server;

import cn.qulei.login.pojo.User;
import com.sun.org.apache.bcel.internal.generic.NEW;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author QuLei
 */
public class LoginServer {
    public static void main(String[] args) throws Exception {
        ServerSocket server = new ServerSocket(10001);
        while (true) {
            Socket socket = server.accept();
            LoginServerThread serverThread = new LoginServerThread(socket);
            new Thread(serverThread).start();
        }

        //server.close();

    }
}

@AllArgsConstructor
@NoArgsConstructor
class LoginServerThread implements Runnable {
    private Socket socket;

    public void run() {
        //获取输入流
        InputStream inputStream = null;
        DataOutputStream outputStream = null;
        try {
            inputStream = socket.getInputStream();
            ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
            User user = (User) objectInputStream.readObject();
            String str = "";
            if ("qulei".equals(user.getUsername()) && "123".equals(user.getPassword())) {
                System.out.println("欢迎您：" + user.getUsername());
                str = "登录成功";
            } else {
                str = "登录失败";
            }
            socket.shutdownInput();
            //截断输入
            outputStream = new DataOutputStream(socket.getOutputStream());
            outputStream.writeUTF(str);
            socket.shutdownOutput();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                outputStream.close();
                inputStream.close();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
