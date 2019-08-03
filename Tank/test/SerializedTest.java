import org.junit.jupiter.api.Test;

import java.io.*;

public class SerializedTest {
    @Test
    public void testLoadImage(){

        try {
            T t = new T();
            t.setX(13);
            t.setY(14);
            File file = new File("F:/test/s.dat");
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            ObjectOutputStream outputStream = new ObjectOutputStream(fileOutputStream);
            outputStream.writeObject(t);
            outputStream.flush();
            outputStream.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void loadObject(){

        try {
            File f = new File("F:/test/s.dat");
            FileInputStream inputStream = new FileInputStream(f);
            ObjectInputStream stream = new ObjectInputStream(inputStream);
            T t = (T) stream.readObject();
            System.out.println(t);
            stream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

class T implements Serializable {
    private static final long serialVersionUID = 2314018074884265063L;
    private int x;
    private int y;
    private Apple apple = new Apple();

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    @Override
    public String toString() {
        return "T{" +
                "x=" + x +
                ", y=" + y +
                ", apple=" + apple +
                '}';
    }
}


class Apple implements Serializable{
    int weight = 8;
}
