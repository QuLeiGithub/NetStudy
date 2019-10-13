package cn.qulei.uploade.server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author QuLei
 */
public class PicServer {
    public static void main(String[] args) throws Exception {
        ServerSocket serverSocket = new ServerSocket(10086);
        Socket server = serverSocket.accept();
        InputStream inputStream = server.getInputStream();
        FileOutputStream outputStream = new FileOutputStream("src/main/resources/image/qulei.jpg");
        int temp = 0;
        while ((temp = inputStream.read()) != -1) {
            outputStream.write(temp);
        }
        //释放资源
        server.shutdownInput();
        OutputStream output = server.getOutputStream();
        output.write("上传成功".getBytes());
        //释放资源
        server.shutdownOutput();
        //关闭操作
        outputStream.close();
        inputStream.close();
        output.close();
        server.close();
        serverSocket.close();
    }
}
