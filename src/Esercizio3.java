import java.util.Random;
import java.util.concurrent.Semaphore;

public class Esercizio3 {
    int buffer[] = new int[10];
    int iP=0;
    int iN = 1;
    int jP = 0;
    int jN = 1;

    Semaphore fullPos = new Semaphore(0);
    Semaphore fullNeg = new Semaphore(0);
    Semaphore emptyPos = new Semaphore(5);
    Semaphore emptyNeg = new Semaphore(5);
    Semaphore mutexPPos = new Semaphore(1);
    Semaphore mutexPNeg = new Semaphore(1);
    Semaphore mutexCPos = new Semaphore(1);
    Semaphore mutexCNeg = new Semaphore(1);




    public void produttore(){
        while (true){
            Random r = new Random();
            int item = r.nextInt(10);
            if(item >= 0){
                try {
                    emptyPos.acquire();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                try {
                    mutexPPos.acquire();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                buffer[iP] = item;
                iP = (iP+1)%10;
                System.out.println("Produttore: ho inserito l'elemento "+item);
                mutexPPos.release();
                fullPos.release();
            }
            else{
                try {
                    emptyNeg.acquire();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                try {
                    mutexPNeg.acquire();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                buffer[iN] = item;
                iN = (iN+1)%10;
                System.out.println("Produttore: ho inserito l'elemento "+item);
                mutexPNeg.release();
                fullNeg.release();
            }
        }
    }


    public void consumatore(boolean b){
        while (true){
            if(b){
                try {
                    fullPos.acquire();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                try {
                    mutexCPos.acquire();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                int item = buffer[jP];
                jP = (jP+1)%10;
                System.out.println("Consumatore: ho letto l'elemento "+item);
                mutexCPos.release();
                emptyPos.release();
            }
            else{
                try {
                    fullNeg.acquire();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                try {
                    mutexCNeg.acquire();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                int item = buffer[jN];
                jN = (jN+1)%10;
                System.out.println("Consumatore: ho letto l'elemento "+item);
                mutexCNeg.release();
                emptyNeg.release();
            }
        }
    }

    public static void main(String[] args) {
        Esercizio3 e = new Esercizio3();
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                e.produttore();
            }
        });
        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                e.consumatore(true);
            }
        });
        Thread t3 = new Thread(new Runnable() {
            @Override
            public void run() {
                e.consumatore(false);
            }
        });
        t1.start();
        t2.start();
        t3.start();
    }
}
