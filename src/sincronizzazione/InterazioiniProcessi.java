package sincronizzazione;

import java.util.Random;
import java.util.concurrent.Semaphore;

public class InterazioiniProcessi {

    /*Esistono 4 tipi di porcessi
    *
    * Data Sharing: i processi si scambiano informazioni tra loro
    * Control Sycnhronization: i processi si sincronizzano tra loro
    * Messaging Passing: i processi  comunicano tra loro
    * Signals: i processi inviano segnali per indicare un particolare stato;
   */

    // Data Sharing
    Semaphore mutex_p = new Semaphore(1);
    Semaphore mutex_c = new Semaphore(1);
    Semaphore full = new Semaphore(0);
    Semaphore empty = new Semaphore(10);
    int buffer[] = new int[10];
     int i = 0;
     int j = 0;


    public void produttore(){
        while (true){
            Random r = new Random();
            int item = r.nextInt(10);
            try {
                empty.acquire();
            } catch (InterruptedException e) {
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
            empty.release();
        }
    }

    public static void main(String[] args) {
        InterazioiniProcessi ip = new InterazioiniProcessi();
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                ip.produttore();
            }
        });
        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                ip.consumatore();
            }
        });
        t1.start();
        t2.start();
    }
}