package cn.qulei.uploade.client;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * @author QuLei
 */
public class PicClient {
    public static void main(String[] args) throws Exception {
        FileInputStream inputStream = new FileInputStream("src/main/resources/image/58.JPG");
        Socket client = new Socket("localhost", 10086);
        OutputStream outputStream = client.getOutputStream();
        int temp = 0;
        while ((temp = inputStream.read()) != -1) {
            outputStream.write(temp);
        }
        client.shutdownOutput();
        InputStream input = client.getInputStream();
        byte [] buf = new byte[1024];
        int length = input.read(buf);
        System.out.println(new String(buf,0,length));
        client.shutdownInput();
        outputStream.close();
        inputStream.close();
        client.close();
    }
}
