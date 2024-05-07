package Classes;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class HotelMain {
    public static void main(String[] args) {
        int numQuartos = 10;
        int numCamareiras = 10;
        int numRecepcionistas = 5;
        int numHospedes = 50;

        boolean ativar = false;

        Queue<Quarto> quartosDisponiveis = new LinkedList<>();
        Queue<Hospede> filaDeEspera = new LinkedList<>();
        Lock lock = new ReentrantLock();

        for (int i = 0; i < numQuartos; i++) {
            lock.lock();
            try {
                Quarto quarto = new Quarto(i + 1, quartosDisponiveis);
                quartosDisponiveis.offer(quarto);
                System.out.println("Quarto " + quarto.getNumero() + " disponivel. \n");
            }finally {
                System.out.println("Quartos criados");
                lock.unlock();
            }
        }
        for (int i = 0; i < numCamareiras; i++) {
                Camareira camareira = new Camareira(i + 1, quartosDisponiveis);
                camareira.start();
                System.out.println("Camareira " + camareira.getIdCamareira() + " iniciando o trabalho. \n");
        }
        for (int i = 0; i < numHospedes; i++) {
                Hospede hospede = new Hospede(i + 1, null, lock); // O quarto será atribuído pela recepcionista
                filaDeEspera.offer(hospede);
                hospede.start();
                System.out.println("Hospede "+hospede.getIdHospede()+" adicionado na fila de espera.\n");
        }
        for (int i = 0; i < numRecepcionistas; i++) {
                Recepcionista recepcionista = new Recepcionista(i + 1, filaDeEspera, quartosDisponiveis, lock);
                recepcionista.start();
                System.out.println("Recepcionista " + recepcionista.getIdRecepcionista() + " iniciando o trabalho. \n");
        }
    }
}
