import java.util.concurrent.Semaphore;

public class Esercizio9 {
    int runReader= 0;
    int runWriter= 0;
    int totReader= 0;
    int totWriter= 0;

    Semaphore mutex = new Semaphore(1);
    Semaphore semReader = new Semaphore(0);
    Semaphore semWriter = new Semaphore(0);

    public void reader() throws InterruptedException{
        while(true) {
            mutex.acquire();
            totReader++;
            if (runWriter == 0) {
                runReader++;
                semReader.release();
            }
            mutex.release();
            semReader.acquire();
            System.out.println("Read");
            mutex.acquire();
            runReader--;
            totReader--;
            if (runReader == 0 && runWriter < totWriter) {
                runWriter = 1;
                semWriter.release();


            }
            mutex.release();
        }
    }

    public void writer() throws InterruptedException{
        while (true){
            mutex.acquire();
            totWriter++;
            if(runWriter == 0 && runReader == 0) {
                runWriter++;
                semWriter.release();
            }
            mutex.release();
            semWriter.acquire();
            System.out.println("Write");
            mutex.acquire();
            runWriter--;
            totWriter--;
            while (runReader < totReader) {
                runReader++;
                semReader.release();
            }
            if (runReader == 0 && runWriter <totWriter) {
                runWriter=1;
                semWriter.release();
            }
            mutex.release();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Esercizio9 esercizio9 = new Esercizio9();
        Thread reader = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    esercizio9.reader();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        Thread writer = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    esercizio9.writer();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        reader.start();
        writer.start();
    }
}
