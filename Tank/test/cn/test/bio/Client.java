package cn.test.bio;

import java.io.*;
import java.net.Socket;

public class Client {

    public static void main(String[] args) throws IOException {
        //连接服务器的地址和端口（底层使用的是TCP三次握手已经被Java封装好啦）

        Socket socket = new Socket("localhost", 8888);
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        bw.write("你好！服务器");
        bw.newLine();
        bw.flush();


        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        String str = reader.readLine();
        System.out.println(str);
        bw.close();

    }
}
