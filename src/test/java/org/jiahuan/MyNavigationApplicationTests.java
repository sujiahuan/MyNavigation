package org.jiahuan;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class MyNavigationApplicationTests {

    Map<Integer,Boolean> sendStatus=new HashMap<>();

    @Test
    public void testName() {
        sendStatus.put(1,true);
        new Thread(()->{
            while (sendStatus.get(1)){
                System.out.println("当前为true，继续循环");
                try{
                    Thread.sleep(1000);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            System.out.println("当前为false，跳出循环");
        }).start();

        sendStatus.put(1,true);
        new Thread(()->{
            try{
                Thread.sleep(5000);
                System.out.println("开始将true改成false");
                sendStatus.put(1, false);
            }catch (Exception e){
                e.printStackTrace();
            }

        }).start();

        while (true){
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

}
