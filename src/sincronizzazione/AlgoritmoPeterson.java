package sincronizzazione;

public class AlgoritmoPeterson {
    static boolean[] flag = {false, false};
    static int turn = 0;
    static int N = 4;
    // flag: ith process wants to enter CS?
    // turn: whose turn to enter CS
    // N: number of loops


    // 1. I want to enter CS.
    // 2. Its the other process' turn.
    // 3. I wait if you want too and your turn.
    // 4. I enter CS (sleep).
    // 5. I am done with CS.
    static Thread process(int i) {
        return new Thread(() -> {
            int j = 1 - i;
            for (int n=0; n<N; n++) {
                log(i+": want CS"); // LOCK
                flag[i] = true; // 1
                turn = j;       // 2
                while (flag[j] && turn == j) Thread.yield(); // 3

                log(i+": in CS"+n);
                sleep(1000 * Math.random()); // 4

                log(i+": done CS"); // UNLOCK
                flag[i] = false; // 5
            }
        });
    }


    public static void main(String[] args) {
        try {
            log("Starting 2 processes (threads) ...");
            Thread p0 = process(0);
            Thread p1 = process(1);
            p0.start();
            p1.start();
            p0.join();
            p1.join();
        }
        catch (InterruptedException e) {}
    }

    static void sleep(double t) {
        try { Thread.sleep((long)t); }
        catch (InterruptedException e) {}
    }

    static void log(String x) {
        System.out.println(x);
    }
}
