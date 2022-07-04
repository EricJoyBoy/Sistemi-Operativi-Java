package sincronizzazione;
/*
* I diversi prrocessi della nostra classe accedono alla memoria condivisa
* le (variabili globali della nostra classe)
* */
public class MemoriaCondivisa {

    private String MemoriaCondivisa; // Questa è una variabile condivisa tra i diversi metodi della classe


    void printMemoriaCondivisa(){
        System.out.println(MemoriaCondivisa);
    }

    public static void main(String[] args) {
        MemoriaCondivisa m = new MemoriaCondivisa();

        m.printMemoriaCondivisa();
    }
}
