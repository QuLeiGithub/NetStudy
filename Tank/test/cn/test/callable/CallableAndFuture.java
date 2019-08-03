package cn.test.callable;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class CallableAndFuture {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService service = Executors.newCachedThreadPool();
        //把任务交给线程池去处理
        Future<Long> future = service.submit(new MyTask());

        long l = future.get();//wait until there's a result ---blocked
        System.out.println(l);
        System.out.println("go on！！");
    }
}
