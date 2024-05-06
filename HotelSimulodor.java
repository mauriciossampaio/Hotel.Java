import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadLocalRandom;

class Quarto {
    private int numero;
    private List<Hospede> hospedes;
    private boolean chaveNaRecepcao;

    public Quarto(int numero) {
        this.numero = numero;
        this.hospedes = new ArrayList<>();
        this.chaveNaRecepcao = true;
    }

    public synchronized boolean checkIn(Hospede hospede) {
        if (hospedes.size() < 4) {
            hospedes.add(hospede);
            return true;
        }
        return false;
    }

    public synchronized void checkOut(Hospede hospede) {
        hospedes.remove(hospede);
        if (hospedes.size() == 0) {
            chaveNaRecepcao = true;
        }
    }

    public synchronized boolean isChaveNaRecepcao() {
        return chaveNaRecepcao;
    }

    public synchronized void setChaveNaRecepcao(boolean chaveNaRecepcao) {
        this.chaveNaRecepcao = chaveNaRecepcao;
    }
        public int getNumero() {
        return numero;
    }

}

class Hospede extends Thread {
    private int id;
    private Quarto quarto;

    public Hospede(int id, Quarto quarto) {
        this.id = id;
        this.quarto = quarto;
    }
      public void setQuarto(Quarto quarto) {
        this.quarto = quarto;
    }


    @Override
    public void run() {
        while (true) {
            passear();
            fazerCheckIn();
            ficarNoQuarto();
            quarto.checkOut(this);
            if (ThreadLocalRandom.current().nextBoolean()) {
                passear();
            } else {
                break;
            }
        }
    }

    private void passear() {
        try {
            Thread.sleep(ThreadLocalRandom.current().nextInt(1000, 5000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void fazerCheckIn() {
        boolean checkInFeito = false;
        while (!checkInFeito) {
            try {
                Thread.sleep(ThreadLocalRandom.current().nextInt(500, 2000));
                if (quarto.isChaveNaRecepcao()) {
                    synchronized (quarto) {
                        if (quarto.isChaveNaRecepcao()) {
                            checkInFeito = quarto.checkIn(this);
                            if (checkInFeito) {
                                quarto.setChaveNaRecepcao(false);
                                System.out.println("Hóspede " + id + " fez check-in no quarto " + quarto.getNumero());
                            }
                        }
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void ficarNoQuarto() {
        try {
            Thread.sleep(ThreadLocalRandom.current().nextInt(2000, 5000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class Camareira extends Thread {
    private int id;
    private BlockingQueue<Quarto> quartosParaLimpar;

    public Camareira(int id, BlockingQueue<Quarto> quartosParaLimpar) {
        this.id = id;
        this.quartosParaLimpar = quartosParaLimpar;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Quarto quarto = quartosParaLimpar.take();
                limparQuarto(quarto);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void limparQuarto(Quarto quarto) {
        System.out.println("Camareira " + id + " está limpando quarto " + quarto.getNumero());
        try {
            Thread.sleep(ThreadLocalRandom.current().nextInt(1000, 3000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Camareira " + id + " terminou de limpar quarto " + quarto.getNumero());
    }
}

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
                Hospede hospede = filaDeEspera.take();
                Quarto quarto = quartosDisponiveis.take();
                alocarHospede(hospede, quarto);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void alocarHospede(Hospede hospede, Quarto quarto) {
        synchronized (quarto) {
            if (quarto.checkIn(hospede)) {
                System.out.println("Recepcionista " + id + " está alocando hóspede " + hospede.getId() + " no quarto " + quarto.getNumero());
                hospede.setQuarto(quarto);
                quartosDisponiveis.offer(quarto); // Retorna o quarto após o check-in
            }
        }
    }
}

public class HotelSimulador {
    public static void main(String[] args) {
        int numQuartos = 10;
        int numCamareiras = 10;
        int numRecepcionistas = 5;
        int numHospedes = 50;

        BlockingQueue<Quarto> quartosDisponiveis = new ArrayBlockingQueue<>(numQuartos);
        BlockingQueue<Hospede> filaDeEspera = new ArrayBlockingQueue<>(numHospedes);

        // Inicialização dos quartos
        for (int i = 0; i < numQuartos; i++) {
            Quarto quarto = new Quarto(i + 1);
            quartosDisponiveis.offer(quarto);
        }

        // Inicialização das camareiras
        for (int i = 0; i < numCamareiras; i++) {
            Camareira camareira = new Camareira(i + 1, quartosDisponiveis);
            camareira.start();
        }

        // Inicialização dos recepcionistas
        for (int i = 0; i < numRecepcionistas; i++) {
            Recepcionista recepcionista = new Recepcionista(i + 1, filaDeEspera, quartosDisponiveis);
            recepcionista.start();
        }

        // Inicialização dos hóspedes
        for (int i = 0; i < numHospedes; i++) {
            Hospede hospede = new Hospede(i + 1, null); // O quarto será atribuído pela recepcionista
            filaDeEspera.offer(hospede);
            hospede.start();
        }
    }
}
