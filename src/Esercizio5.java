import java.util.concurrent.Semaphore;
/*
*Un parcheggio offre 30 posti auto, non tutti uguali:
- 10 posti di tipo "S": possono ospitare solo vetture di tipo "S";
- 10 posti di tipo "M": possono ospitare vetture "S" oppure "M";
- 10 posti di tipo "L": possono ospitare vetture "S" oppure "M"
oppure "L".
("S","M", "L" rappresentano le tre possibili dimensioni di vetture/
posti auto).
Le vetture che non possono accedere al parcheggio vengono bloccate
all'ingresso. In questo caso, quando queste vetture verranno
sbloccate potranno accedere solamente a parcheggi del tipo
corrispondente (vetture "S" in posti "S", vetture "M" in posti "M",
vetture "L" in posti "L").
Una vettura "S" che arriva all'ingresso puo' accedere ad un posto
"M" solo se non ci sono posti liberi "S" e almeno 3 posti "M" sono
occupati da vetture "M".
Una vettura "S" che arriva all'ingresso puo' accedere ad un posto
"L" solo se non ci sono posti liberi "S" e la vettura non puo'
accedere a posti "M";
Una vettura "M" che arriva all'ingresso puo' accedere ad un posto
"L" solo se non ci sono posti liberi "M" e le vetture "M" gia'
presenti nel parcheggio sono meno di 15.
Programmare l'ingresso e l'uscita delle vetture di ogni tipo usando
i semafori con la semantica tradizionale.
* */

public class Esercizio5 {

    int busyL = 0;
    int busyM = 0;
    int busyS = 0;

    int inM = 0;
    int inMM = 0;
    int wL = 0;
    int wM = 0;
    int wS = 0;

    Semaphore mutex = new Semaphore(1);
    Semaphore SemM = new Semaphore(0);
    Semaphore SemL = new Semaphore(0);
    Semaphore SemS = new Semaphore(0);

    boolean canSS() {
        return busyS < 10;
    }

    boolean canMM() {
        return busyM < 10;

    }

    boolean canLL() {
        return busyL < 10;
    }

    boolean canSM() {
        return busyS < 10 && inMM >= 3;
    }

    boolean canSL() {
        return busyS < 10 && !canSM();
    }

    boolean canML() {
        return busyL < 10 && inM < 15;
    }

    void inSS() {
        busyS++;
    }

    void inSM() {
        busyM++;

    }

    void inSL() {
        busyL++;
    }

    void inMM() {
        busyM++;
        inMM++;
        inM++;
    }

    void inML() {
        busyL++;
        inM++;
    }
    void inLL() {
        busyL++;
    }

 void outS() throws InterruptedException {
        if(wS>0){
            wS--; SemS.acquire();
        }
        else{
            busyS--;
        }

 }

 void outM() throws InterruptedException {
        if(wM>0){
            wM--; inM++; inMM++; SemM.acquire();
        }
        else{
            busyM--;
        }

 }

void outL() throws InterruptedException {
        if(wL>0){
            wL--; SemL.acquire();
        }
        else{
            busyL--;
        }

 }

 void carS() throws InterruptedException {
     mutex.acquire();
     if (canSS()) {
         busyS++;
         mutex.release();
         System.out.println("Parcheggio SS: ingresso");
         mutex.acquire();
         outS();
         mutex.release();
         System.out.println("Parcheggio SS: uscita");
     } else {
         if (canSM()) {
             busyM++;
             mutex.release();
             System.out.println("Parcheggio SM: ingresso");
             mutex.acquire();
             outM();
             mutex.release();
             System.out.println("Parcheggio SM: uscita");
         } else {
             if (canSL()) {
                 busyL++;
                 mutex.release();
                 System.out.println("Parcheggio SL: ingresso");
                 mutex.acquire();
                 outL();
                 mutex.release();
                 System.out.println("Parcheggio SL: uscita");
             } else {
                 wS++;
                 mutex.release();
                 SemS.acquire();
                 System.out.println("Parcheggio SS ingresso");
                 mutex.acquire();
                 outS();
                 mutex.release();
                 System.out.println("Parcheggio SS uscita");
             }
         }
     }

 }


 void carM() throws InterruptedException {
     mutex.acquire();
     if (canMM()) {
         busyM++;
         inM++;
         inMM++;
         mutex.release();
         System.out.println("Parcheggio MM: ingresso");
         inM--;
         inMM--;
         mutex.acquire();
         outM();
         mutex.release();
         System.out.println("Parcheggio MM: uscita");
     } else {
         if (canML()) {
             busyL++;
             inM++;
             mutex.release();
             System.out.println("Parcheggio ML: ingresso");
             mutex.acquire();
             outL();
             mutex.release();
             System.out.println("Parcheggio ML: uscita");
         } else {
             wM++;
             mutex.release();
             SemM.acquire();
             System.out.println("Parcheggio MM ingresso");
             mutex.acquire();
             outM();
             mutex.release();
             System.out.println("Parcheggio MM uscita");
         }
     }
 }

 void carL() throws InterruptedException {
        mutex.acquire();
        if (canLL()) {
            busyL++;
            mutex.release();
            System.out.println("Parcheggio LL: ingresso");
            mutex.acquire();
            outL();
            mutex.release();
            System.out.println("Parcheggio LL: uscita");
        } else {
            wL++;
            mutex.release();
            SemL.acquire();
            System.out.println("Parcheggio LL ingresso");
            mutex.acquire();
            outL();
            mutex.release();
            System.out.println("Parcheggio LL uscita");
        }
    }

        public static void main(String[] args) throws InterruptedException {
            Esercizio5 e = new Esercizio5();
            for (int i = 0; i < 10; i++) {
                new Thread(() -> {
                    try {
                        e.carS();
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }).start();
            }
            for (int i = 0; i < 10; i++) {
                new Thread(() -> {
                    try {
                        e.carM();
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }).start();
            }
            for (int i = 0; i < 10; i++) {
                new Thread(() -> {
                    try {
                        e.carL();
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }).start();
            }
        }
 }









































