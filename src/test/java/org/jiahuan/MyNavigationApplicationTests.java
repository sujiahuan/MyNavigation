package org.jiahuan;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class MyNavigationApplicationTests {

    @Test
    public void testName() {
        MyNavigationApplicationTests myNavigationApplicationTests = new MyNavigationApplicationTests();
        new Thread(()->{
            System.out.println("线程一开始等待");
            test2();
            System.out.println("线程一运行结束");
        }).start();

        new Thread(()->{
            System.out.println("线程二开始等待");
            test2();
            System.out.println("线程二运行结束");
        }).start();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//synchronized ("1"){
//    System.out.println("开始放行");
//    "1".notify();
//}

        while (true){
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    private synchronized void test2() {
            try {
                System.out.println("进入等待");
               this.wait();
//                Thread.sleep(10000);
                System.out.println("等待结束");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
    }

}
