package sincronizzazione;

public class RaceConditionSbagliata extends Thread {

   static int x = 0; // variabile condivisa

    @Override
    public void run() {
        while (true){
            x=10;
                System.out.println("y="+x);
        }
    }

    public static void main(String[] args) {
        Thread t1 = new RaceConditionSbagliata();
        t1.start();

        while (true){
            int y = x+5;
                System.out.println("y è uguale" +y);
        }


    }
}
