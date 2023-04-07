public class Test {
    public static void main(String[] args) {

        //创建一个SupVip学员的Thread对象
        SuperVipStudent SS = new SuperVipStudent();
        Thread SupVip = new Thread(SS, "SupVip");

        //创建一个vip学员的Thread对象
        VipStudent vs = new VipStudent(SupVip);
        Thread vip = new Thread(vs, "vip");

        //创建一个normal学员的Thread对象
        NormalStudent ns = new NormalStudent(vip);
        Thread normal = new Thread(ns, "normal");

        //设置SupVip学员、VIP学员、普通学员叫号概率
        SupVip.setPriority(10);
        vip.setPriority(5);
        normal.setPriority(2);

        //开启多线程
        SupVip.start();
        vip.start();
        normal.start();

    }

}

class Sleep {
    public void study(int n, String name) {
        if (name.startsWith("SupVip学员")) {
            synchronized (this) {
                try {
                    int i = 1;
                    while (i < n) {
                        System.out.println(name + i + "秒");
                        Thread.sleep(1000L);
                        i++;
                    } // 假设至尊vip练车是vip的2倍
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } else {
            try {
                int i = 1;
                while (i < n) {
                    System.out.println(name + i + "秒");
                    Thread.sleep(1000L);
                    i++;
                } // 假设至尊vip练车是vip的2倍
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

//至尊VIP学生练车
class SuperVipStudent implements Runnable {


    @Override
    public void run() {
        for (int i = 1; i <= 10; i++) {//代表10个vip学生学车
            //表示学车的状态
//            System.out.println("SupVip学员" + i + "号正在练车");
            new Sleep().study(6, "SupVip学员" + i + "号");
            if (i == 10) {
                System.out.println("------SupVip全部完成练车----------\n");
            }
        }
    }
}

//vip学生练车
class VipStudent implements Runnable {
    Thread SupVip;

    VipStudent(Thread SupVip) {
        this.SupVip = SupVip;
    }


    @Override
    public void run() {
        for (int i = 1; i <= 15; i++) {//代表15个vip学生学车

            //表示学车的状态
//            System.out.println("Vip学员" + i + "号正在练车");
            if (i == 14) {
                try {
                    SupVip.join();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            new Sleep().study(3, "Vip学员" + i + "号");
            if (i == 15) {
                System.out.println("------vip全部完成练车----------\n");
            }
        }
    }
}

//普通学生练车
class NormalStudent implements Runnable {

    Thread vip;

    NormalStudent(Thread vip) {
        this.vip = vip;
    }

    @Override
    public void run() {
        for (int i = 1; i <= 25; i++) {//代表25个人普通学生学车
            //表示学车的状态
//            System.out.println("普通学员" + i + "号正在练车");
            if (i == 24) {
                try {
                    vip.join();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            new Sleep().study(1, "学员" + i + "号");

            if (i == 25) {
                System.out.println("------普通学生全部完成练车----------\n");
            }
        }
    }
}
