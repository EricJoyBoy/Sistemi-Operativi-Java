package sincronizzazione;

public class RaceConditionGiusta extends Thread{

    static int  x = 0;

    @Override
    public void run() {
        for (int i = 0; i < 100; i++) {
            x=10;
                System.out.println("y="+x);
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new RaceConditionGiusta();
        t1.start();
        t1.join();
        for (int i = 0; i < 100; i++) {
            int y = x+5;
                System.out.println("y è uguale" +y);
        }
    }
}
