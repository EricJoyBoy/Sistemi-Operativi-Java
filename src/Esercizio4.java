import java.util.Random;
import java.util.concurrent.Semaphore;
/*Si consideri il classico problema dei produttori e consumatori, con
il buffer implementato con un array di interi di dimensione 100.
Si assuma, per semplicita’, che gli interi prodotti e consumati
siano >=0. Si considerino le seguenti condizioni aggiuntive:
1.Gli interi pari possono occupare solo le posizioni da 0 a 49;
2.Gli interi dispari possono occupare solo le posizioni da 50 a 99;
3.Vi e’ una nuova categoria di processi, i processi ConsPari, che
consumano tutti gli interi pari, se l’array contiene almeno 20
interi pari;
Quanto un processo tenta di effettuare un’operazione al momento non
consentita (per esempio produrre un intero che il buffer non può al
momento ospitare), il processo deve essere messo in attesa.
Programmare il sistema sfruttando i semafori con la semantica
tradizionale.*/
public class Esercizio4 {

    int buffer[] = new int[100];
    int i = 0;
    int j = 0;
    int k = 0;
    int attesaProduttori = 0;
    int attesaConsumatori = 0;
    int attesaConsPari = 0;

    int numPari = 0;

    Semaphore semCons = new Semaphore(0);
    Semaphore semProd = new Semaphore(0);
    Semaphore semConsPari = new Semaphore(0);
    Semaphore mutexPari = new Semaphore(1);

    public void producer() throws InterruptedException {
        while (true) {
            Random r = new Random();
            int item = r.nextInt(100);
            if (item % 2 == 0) {

                    mutexPari.acquire();

                if (numPari == 50) {
                    attesaProduttori++;
                    mutexPari.release();

                        semProd.acquire();

                    mutexPari.acquire();

                }
                buffer[i] = item;
                i = (i + 1) % 50;
                numPari++;
                System.out.println("Produttore: " + item);
                if (attesaConsumatori > 0) {
                    semCons.release();
                    attesaConsumatori--;
                }
                if (numPari == 20 && attesaConsPari > 0) {

                    attesaConsPari--;
                    semConsPari.release();
                }
                mutexPari.release();

            } else {
                buffer[i] = item;
                i = (i + 1) % 50;

            }

        }
    }

    public void consumer(boolean pari) throws InterruptedException {
        while (true) {
            if (pari) {
                mutexPari.acquire();
                while (numPari == 0) {
                    attesaConsumatori++;
                    semCons.release();
                    mutexPari.acquire();
                }
                int item = buffer[j];
                j = (j + 1) % 50;
                numPari--;
                System.out.println("Consumatore: ho consumato l'elemento " + item);
                if (attesaProduttori > 0) {

                    attesaProduttori--;
                    semProd.release();
                }
            }
            mutexPari.release();

        }


    }

    public void consumerPari() throws InterruptedException {
        while (true) {
            mutexPari.acquire();
            while (numPari < 20) {
                attesaConsPari++;
                semConsPari.release();
                mutexPari.acquire();
            }
            int item = buffer[j];
            j = (j + 1) % 50;
            numPari--;
            System.out.println("ConsPari: ho preso l'elemento " + item);
            if (attesaProduttori > 0) {

                attesaProduttori--;
                semProd.release();
            }
            mutexPari.release();
        }
    }

    public static void main(String[] args) {
        Esercizio4 e = new Esercizio4();
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    e.producer();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    e.consumer(false);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        Thread t3 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    e.consumer(true);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        Thread t4 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    e.consumerPari();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        t1.start();
        t2.start();
        t3.start();
        t4.start();
    }
}
