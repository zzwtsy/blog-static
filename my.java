public class Main {
    public static Thread t1;
    public static Thread t2;
    public static Thread t3;

    public static void main(String[] args) {
        // 创建三个线程代表不同类型的学员
        t3 = new Thread(new SupremeVIPStudent(), "SupremeVIP");
        t2 = new Thread(new VIPStudent(t3), "VIP");
        t1 = new Thread(new NormalStudent(t3), "Normal");

        //设置叫号概率
        t1.setPriority(1);
        t2.setPriority(3);
        t3.setPriority(6);

        // 启动线程
        t1.start();
        t2.start();
        t3.start();
    }
}

// 普通学员类
class NormalStudent implements Runnable {
    private final Thread supremeVIPStudent;

    public NormalStudent(Thread supremeVIPStudent) {
        this.supremeVIPStudent = supremeVIPStudent;
    }

    @Override
    public void run() {
        int count = 1;
        // 如果还有未处理的学员，则一直循环
        while (count < 25) {
            // 如果是最后一个普通学员则等待至尊学员全部练习完成
            if (count == 24) {
                try {
                    supremeVIPStudent.join();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

            // 休眠一段时间模拟练车时间
            Tools.sleep(Thread.currentThread().getName() + count, 1);
            count++;
        }
    }
}


// VIP学员类
class VIPStudent implements Runnable {

    private final Thread supremeVIPStudent;

    public VIPStudent(Thread supremeVIPStudent) {
        this.supremeVIPStudent = supremeVIPStudent;
    }

    @Override
    public void run() {
        int count = 1;
        // 如果还有未处理的学员，则一直循环
        while (count < 15) {
            // 如果是最后一个 vip 学员则等待至尊学员全部练习完成
            if (count == 14) {
                try {
                    supremeVIPStudent.join();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            // 休眠一段时间模拟练车时间，VIP学员练车时间是普通学生的3倍
            Tools.sleep(Thread.currentThread().getName() + count, 3);
            count++;
        }
    }
}

// 至尊VIP学员类
class SupremeVIPStudent implements Runnable {
    @Override
    public void run() {
        int count = 1;
        // 如果还有未处理的学员，则一直循环
        while (count < 10) {
            synchronized (Tools.class) {
                Tools.sleep(Thread.currentThread().getName() + count, 6);
                count++;
            }
        }
    }
}

class Tools {

    public static void sleep(String name, long seconds) {
        try {
            int i = 0;
            while (i < seconds) {
                System.out.println("学员" + name + "练习" + (i + 1) + "秒");
                Thread.sleep(1000);
                i++;
            }
        } catch (InterruptedException e) {
            System.out.println(Thread.currentThread().getName() + " is interrupted.");
        }
    }
}
