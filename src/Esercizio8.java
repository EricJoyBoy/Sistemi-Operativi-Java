import java.util.concurrent.Semaphore;

public class Esercizio8 {

    /*
    * Un cinema dispone di una sala con 30 posti, divisi in 3 file:
Fila 1: posti da  0 a  9; Fila 2: posti da 10 a 19; diFila 3: posti da 20 a 29.

Vogliamo gestire le prenotazioni per lo spettacolo del 14 dicembre, ore 18.00. Assumiamo un array B di 30 boolean, dove B[i] = T se il posto i e' libero, e B[i] = F se il posto i e' occupato. Inizialmente tutti i posti sono liberi (B[0] = B[1] = ... = B[29] = T).

Esistono le seguenti classi di processi:

- processi "prenotatori": selezionano un numero naturale casuale F tale che 1 <= F <= 3 ed un numero naturale casuale N tale che 1 <= N <= 10, controllano se esistono almeno N posti consecutivi liberi nella file F e, in caso affermativo, N posti liberi consecutivi della fila F vengono prenotati.

- processi "ispezionatori": iterativamente calcolano il numero massimo di posti liberi consecutivi, vale a dire il massimo N tale che esiste un i tale che B[i] = B[i+1] = ... = B[i+N-1] = T e 0 <= i,i+1,...,i+N-1 <= 9, oppure 10 <= i,i+1,...,i+N-1 <= 19, oppure 20 <= i,i+1,...,i+N-1 <= 29.

Oltre all'assenza di race condition, deve essere garantito quanto segue:

- quando uno o piu' ispezionatori stanno svolgendo il loro compito ed un prenotatore che tenta di prenotare va in waiting, al piu' altri 20 ispezionatori possono svolgere il loro compito prima che venga svegliato un prenotatore.

- quando uno o piu' prenotatori stanno svolgendo il loro compito ed un ispezionatore va in waiting, allora al piu' due prenotatori che intendono prenotare posti nella fila 1, al piu' due prenotatori che intendono prenotare posti nella fila 2 ed al piu' due prenotatori che intendono prenotare posti nella fila 3 possono svolgere il loro compito prima che almeno un ispezionatore venga svegliato.

    * */




}
