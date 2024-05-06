package Classes;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Hotel {
    public static void main(String[] args) {
        int numQuartos = 10;
        int numCamareiras = 10;
        int numRecepcionistas = 5;
        int numHospedes = 20;

        BlockingQueue<Quarto> quartosDisponiveis = new ArrayBlockingQueue<>(numQuartos);
        BlockingQueue<Hospede> filaDeEspera = new ArrayBlockingQueue<>(numHospedes);

        for (int i = 0; i < numQuartos; i++) {
            Quarto quarto = new Quarto(i + 1);
            quartosDisponiveis.offer(quarto);
            System.out.println("Quarto " + quarto.getNumero() +" disponivel. \n");
        }
        for (int i = 0; i < numCamareiras; i++) {
            Camareira camareira = new Camareira(i + 1, quartosDisponiveis);
            camareira.start();
            System.out.println("Camareira "+camareira.getIdCamareira()+" iniciando o trabalho. \n");
        }

        for (int i = 0; i < numRecepcionistas; i++) {
            Recepcionista recepcionista = new Recepcionista(i + 1, filaDeEspera, quartosDisponiveis);
            recepcionista.start();
            System.out.println("Recepcionista "+recepcionista.getIdRecepcionista()+" iniciando o trabalho. \n");
        }

        for (int i = 0; i < numHospedes; i++) {
            Hospede hospede = new Hospede(i + 1, null); // O quarto será atribuído pela recepcionista
            filaDeEspera.offer(hospede);
            hospede.start();
            System.out.println("Hospede "+hospede.getIdHospede()+" adicionado na fila de espera.\n");
        }
    }
}
