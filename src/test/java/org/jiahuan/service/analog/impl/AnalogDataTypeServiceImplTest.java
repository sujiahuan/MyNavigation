package org.jiahuan.service.analog.impl;

import org.junit.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AnalogDataTypeServiceImplTest {

    @Test
    public void testName() throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        Thread thread = new Thread(() -> {
            System.out.println(Thread.currentThread().getId() + ":开始运行");
            try {
                Thread.sleep(2000);
                System.out.println(Thread.currentThread().getId() + ":运行结束");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        Thread thread2 = new Thread(() -> {
            System.out.println(Thread.currentThread().getId() + ":开始运行");
            try {
                Thread.sleep(3000);
                System.out.println(Thread.currentThread().getId() + ":运行结束");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        executorService.submit(thread);
        executorService.submit(thread2);
        executorService.shutdown();
        System.out.println("第一执行："+executorService.isTerminated()   );
        Thread.sleep(4000);
        System.out.println("第二执行："+executorService.isTerminated()  );
    }

    @Test
    public void testName2() throws Exception {

//        String format = String.format("%0" + 4 + "d", Integer.parseInt("10000") + 1);
//        System.out.println(format);

        Integer a=5;
        for (int i = 0; i < a; i++) {
            if(a==5){
                System.out.println("等于三啦");
            }
            if(a.equals(5)){
                System.out.println("哈哈 等于三啦");
            }
        }
    }

}