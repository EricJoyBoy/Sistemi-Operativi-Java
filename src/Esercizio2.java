import java.util.Random;
import java.util.concurrent.Semaphore;

public class Esercizio2 {

    int buffer[] = new int [10];
    int i = 0;
    int j = 0;

    Semaphore mutex_p = new Semaphore(1);
    Semaphore mutex_c = new Semaphore(1);
    Semaphore sem_Pos = new Semaphore(7);
    Semaphore sem_Neg = new Semaphore(8);
    Semaphore sem_Zero = new Semaphore(1);
    Semaphore full = new Semaphore(0);
    Semaphore empty = new Semaphore(10);

    public void produttore(){
        while (true){
            Random r = new Random();
            int item = r.nextInt(10);
            if (item == 0){
                try {
                    sem_Zero.acquire();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if (item > 0){
                try {
                    sem_Pos.acquire();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if (item < 0){
                try {
                    sem_Neg.acquire();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            try {
                mutex_p.acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            buffer[i] = item;
            i = (i+1)%10;
            System.out.println("Produttore: ho inserito l'elemento "+item);
            mutex_p.release();
           full.release();
        }
    }


public void consumatore(){
        while (true){
            try {
                full.acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                mutex_c.acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            int item = buffer[j];
            j = (j+1)%10;
            System.out.println("Consumatore: ho letto l'elemento "+item);
            mutex_c.release();

            if (item == 0){
              sem_Zero.release();
            }
            if (item > 0){
                sem_Pos.release();
            }
            if (item < 0){
                sem_Neg.release();
            }
            empty.release();
        }
}

    public static void main(String[] args) {
        Esercizio2 e = new Esercizio2();
        Thread t1 = new Thread(e::produttore);
        Thread t2 = new Thread(e::consumatore);
        t1.start();
        t2.start();
    }
}
