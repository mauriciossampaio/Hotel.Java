package Classes;

import java.util.concurrent.BlockingQueue;

class Recepcionista extends Thread {
    private int id;
    private BlockingQueue<Hospede> filaDeEspera;
    private BlockingQueue<Quarto> quartosDisponiveis;

    public Recepcionista(int id, BlockingQueue<Hospede> filaDeEspera, BlockingQueue<Quarto> quartosDisponiveis) {
        this.id = id;
        this.filaDeEspera = filaDeEspera;
        this.quartosDisponiveis = quartosDisponiveis;
    }

    @Override
    public void run() {
        while (true) {
            try {
                //System.out.println("Recepcionista " + id + " iniciando...");
                Quarto quarto = quartosDisponiveis.take();
                for (int i = 0 ; i < 4;i++) {
                    Hospede hospede = filaDeEspera.take();
                    alocarHospede(hospede, quarto);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void alocarHospede(Hospede hospede, Quarto quarto) {
        synchronized (quarto) {
            if (quarto.checkIn(hospede)) {
                System.out.println("Recepcionista " + id + " está alocando hóspede " + hospede.getIdHospede() + " no quarto " + quarto.getNumero()+"\n\t");
                hospede.setQuarto(quarto);
                quartosDisponiveis.offer(quarto); // Retorna o quarto após o check-in
            }
        }
    }

    public int getIdRecepcionista() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}

