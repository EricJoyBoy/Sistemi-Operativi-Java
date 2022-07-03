import java.util.concurrent.Semaphore;

public class Esercizio6 {
   /*Problema dello sleeping barber.
In un barber shop lavora un solo barbiere, vi è una sola sedia adibita al
taglio, e vi sono N sedie per i clienti in attesa. Assumiamo N=20.
Comportamento del barbiere:
• all'apertura del negozio si mette a dormire nella sedia adibita al taglio, in
attesa che un cliente entri e lo svegli;
• quando ci sono clienti in attesa, il barbiere li chiama e li serve uno alla
volta;
• quando non ci sono clienti in attesa, il barbiere si rimette a dormire nella
sedia adibita al taglio.
Comportamento del cliente:
• quando entra nel negozio, se non ci sono sedie libere va a cercarsi un
altro barbiere;
• quando entra nel negozio, se c'è almeno una sedia libera ne occupa una,
svegliando il barbiere se sta dormendo, ed attendendo di essere
chiamato dal barbiere per il taglio.
Programmare il barbiere ed il singolo cliente. */



    Semaphore barbiere_libero = new Semaphore(0);
    Semaphore mutex = new Semaphore(1);
    int clienti_in_attesa = 0;
    final int sedie = 20;
    Semaphore clienti_in_negozio = new Semaphore(0);

    public void barbiere() throws InterruptedException{
        while (true){
            clienti_in_negozio.acquire();
            mutex.acquire();
            clienti_in_attesa--;
            barbiere_libero.release();
            mutex.release();
            System.out.println("Barbiere: ho servito un cliente");


        }
    }
public void cliente() throws InterruptedException{
    while(true){
    mutex.acquire();
    if (clienti_in_attesa < sedie){
    clienti_in_attesa++;
    clienti_in_negozio.release();
    mutex.release();
    barbiere_libero.acquire();
    System.out.println("Cliente: ho chiamato il barbiere");
    }
    else{
        mutex.release();
        System.out.println("Cliente: non ho chiamato il barbiere");
    }
}}

    public static void main(String[] args) throws InterruptedException {
        Esercizio6 e = new Esercizio6();
        Thread barbiere = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    e.barbiere();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        Thread cliente = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    e.cliente();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        barbiere.start();
        cliente.start();
    }

}

