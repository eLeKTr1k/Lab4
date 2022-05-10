public class Lab4 {
    public static void main (String [] args) throws InterruptedException{
        Chronometr my=new Chronometr();
        Runnable t1=new Messenger(7, my);
        Runnable t2=new Messenger(5, my);
        Runnable t3=new Messenger(1, my);
        new Thread(t1, "t1").start();
        new Thread(t2, "t2").start();
        new Thread(t3, "t3").start();
        my.countTime((Messenger)t1, (Messenger)t2, (Messenger)t3, 100);
    }
}

class Chronometr {
    public int time=0;
    public void countTime (Messenger m, Messenger m1, Messenger m2, int period) {
        for (int i=0; i<period; i++){
            synchronized(this) {
                time+=1;
                System.out.println(time);
                if (i==period-1) Messenger.finish=true;
                m.flag=false;
                this.notify();
                m1.flag=false;
                this.notify();
                m2.flag=false;
                this.notify();
            }
            try {   Thread.sleep(1000);}
            catch (InterruptedException e) {};
        }
    }
}

class Messenger implements Runnable{
    private int time;
    public Chronometr ch;
    public static boolean finish=false;
    public boolean flag=true;
    Messenger(int time, Chronometr ch) {
        this.time=time;
        this.ch=ch;
    }

    public void waitForTime() {
        while (true) {
            synchronized (ch) {
                try {
                    if (ch.time % this.time == 0)
                        System.out.println("Thread " + this.time);
                    ch.wait();
                    if (finish) return;
                } catch (InterruptedException e) {
                }

            }
        }
    }

    public void run()  {
        waitForTime();
        System.out.println("The end");
    }
}