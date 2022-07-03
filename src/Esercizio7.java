import java.util.concurrent.Semaphore;

public class Esercizio7 {
/*
* Un parcheggio ha 30 posti, due ingressi con sbarra, A e B, ed un'uscita.
Quando un veicolo si presenta ad uno dei due ingressi, se c'è almeno un
posto libero entra, parcheggia ed esce dal parcheggio, altrimenti prenota
l'ingresso ed attende di poter entrare.
Se ci sono veicoli in attesa ad entrambi gli ingressi, vengono fatti entrare
quando altri veicoli escono dal parcheggio, aprendo le due sbarre
alternativamente.
Quando un veicolo esce dal parcheggio, se ci sono veicoli in attesa ad
almeno uno dei due ingressi, ne fa entrare uno.
* */

    Semaphore mutex = new Semaphore(1);
    Semaphore gateA = new Semaphore(0);
    Semaphore gateB = new Semaphore(0);
    int bookA = 0;
    int bookB = 0;
    int freeSlots = 30;
    int turn = 0;

    public void enteringA() throws InterruptedException {
        mutex.acquire();
        if (freeSlots > 0) {
            freeSlots--;
            mutex.release();
            System.out.println("Entered A");
        } else {
            bookA++;
            mutex.release();
            gateA.acquire();
            System.out.println("Entered A");
        }
    }

    public void enteringB() throws InterruptedException {
        mutex.acquire();
        if (freeSlots > 0) {
            freeSlots--;
            mutex.release();
            System.out.println("Entered B");
        } else {
            bookB++;
            mutex.release();
            gateB.acquire();
            System.out.println("Entered B");
        }
    }

    public void exiting() throws InterruptedException {
        mutex.acquire();
        if (freeSlots == 0) {
            if (turn == 0 || bookB == 0 && bookA > 0) {
                bookA--;
                turn = 1;
                gateA.release();

            } else {
                if (bookB > 0) {
                    bookB--;
                    turn = 0;
                    gateB.release();
                } else {
                    freeSlots++;
                }
            }
        } else {
            freeSlots++;
        }
        mutex.release();
    }

    public static void main(String[] args) {
        Esercizio7 e = new Esercizio7();
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                try {
                    e.enteringA();
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }).start();
        }
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                try {
                    e.enteringB();
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }).start();
        }
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                try {
                    e.exiting();
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }).start();
        }
    }












}