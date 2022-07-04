package sincronizzazione;


import java.util.concurrent.Semaphore;

/*
* In questo problema si hanno:
• un insieme di dati condivisi (ad esempio una classe, un array, ecc.);
• un insieme di processi scrittori (writer), che modificano i dati;
• un insieme di processi lettori (reader), che accedono ai dati in sola lettura
(a differenza dei processi consumatori del problema precedente, non consumano/cancellano ciò che leggono).

* */
public class LettoriScrittori {

    /*
    * Servono 4 variabili condivise per tener traccia dello stato del sistema:
    • runR: il numero di lettori che stanno leggendo;
    • runW: il numero di scrittori che stanno scrivendo;
    • totR: il numero di lettori che stanno leggendo (runR), o che vorrebbero leggere;
    • totW: il numero di lettori che stanno scrivendo (runW), o che vorrebbero scrivere.
    */
    int runR = 0;
    int runW = 0;
    int totR = 0;
    int totW = 0;

    // Semafori per la sincronizzazione

    /*
    *Si usano poi 3 semafori:
        semaphore semR = 0;
        semaphore semW = 0;
        semaphore mutex = 1;

    * */

    Semaphore semR = new Semaphore(0);
    Semaphore semW = new Semaphore(0);
    Semaphore mutex = new Semaphore(1);


    public void reader() throws InterruptedException {
        mutex.acquire();
        totR++;
        if (runW == 0) {
            runR++;
            semR.release();
        }
        mutex.release();
        System.out.println("Reader " + Thread.currentThread().getName() + " Sta leggendo");

        mutex.acquire();
        runR--;
        totR--;
        if (runR == 0 && runW < totW) {
            runW = 1;
            semW.release();
        }
        mutex.release();

        semR.acquire();
        System.out.println("Reader " + Thread.currentThread().getName() + " Ha finito di leggere");
        mutex.acquire();
        runR--;
        totR--;
        while (runR == 0 && runW < totW) {
            runW = 1;
            semW.release();
        }
        mutex.release();
    }


    public void writer() throws InterruptedException {
        while (true) {
            mutex.acquire();
            totW++;
            if (runW == 0 && runR == 0) {
                runW = 1;
                semW.release();
            }
            mutex.release();
            semW.acquire();
            System.out.println("Writer " + Thread.currentThread().getName() + " Sta scrivendo");
            mutex.acquire();
            runW--;
            totW--;
            while (runR < totR) {
                runR++;
                semR.release();
            }
            if (runR == 0 && runW < totW) {
                runW = 1;
                semW.release();
            }
            mutex.release();
        }
    }

    public static void main(String[] args) {
        LettoriScrittori ls = new LettoriScrittori();
        Thread t1 = new Thread(() -> {
            try {
                ls.reader();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        Thread t2 = new Thread(() -> {
            try {
                ls.reader();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        Thread t3 = new Thread(() -> {
            try {
                ls.writer();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        Thread t4 = new Thread(() -> {
            try {
                ls.writer();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        t1.start();
        t2.start();
        t3.start();
        t4.start();
    }
}