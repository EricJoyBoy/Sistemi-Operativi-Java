import java.util.Random;
import java.util.concurrent.Semaphore;

public class Esercizio1 {

    int buffer[] = new int [10];
    int i = 0;
    int j = 0;

    Semaphore empty = new Semaphore(10);
    Semaphore full = new Semaphore(0);
    Semaphore mutex_p = new Semaphore(1);
    Semaphore mutex_c = new Semaphore(1);
    Semaphore sem_3 = new Semaphore(1);
    Semaphore sem_5 = new Semaphore(2);

    public void producer(){
        while (true){
            Random r = new Random();
            int item = r.nextInt(10);
            if (item == 3){
                try {
                    sem_3.acquire();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if (item == 5){
                try {
                    sem_5.acquire();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            try {
                empty.acquire();
            }
            catch(InterruptedException e){
                e.printStackTrace();
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
    public void consumer(){
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
            System.out.println("Consumatore: ho preso l'elemento "+item);
            mutex_c.release();
            empty.release();
            if (item == 3){
                sem_3.release();
            }
            if (item == 5){
                sem_5.release();
            }
        }


    }

    public static void main(String[] args) {
        Esercizio1 e = new Esercizio1();
        Thread t1 = new Thread(e::producer);
        Thread t2 = new Thread(e::consumer);
        t1.start();
        t2.start();
    }
}
