package Classes;

import java.util.Queue;
import java.util.SimpleTimeZone;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class Recepcionista extends Thread {
    private int id;
    private Queue<Hospede> filaDeEspera;
    private Queue<Quarto> quartosDisponiveis;
    private static Lock locke;
    //private static Condition condition = locke.newCondition();

    public Recepcionista(int id, Queue<Hospede> filaDeEspera, Queue<Quarto> quartosDisponiveis, Lock locke) {
        this.id = id;
        this.filaDeEspera = filaDeEspera;
        this.quartosDisponiveis = quartosDisponiveis;
        this.locke = locke;
    }

    @Override
    public void run() {
        while (true) {
            if (filaDeEspera.size() > 0){
                if (quartosDisponiveis.size()>0){
                    System.out.println("Recepcionista " + id + " iniciando...");
                }
                if (filaDeEspera.size() == 0) {
                    break;
                }
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                if (quartosDisponiveis.peek() != null && quartosDisponiveis.peek().isFoiLimpo() && !quartosDisponiveis.peek().isReservado()) {
                    Quarto quarto = quartosDisponiveis.poll();
                    for (int i = 0; i < 4; i++) {
                        Hospede hospede = filaDeEspera.remove();
                        alocarHospede(hospede, quarto);
                    }
                } else {
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }

            }else {
                break;
            }
        }
        System.out.println("Todos os hospedes na fila de esperam foram hospedados!");
    }

    private void alocarHospede(Hospede hospede, Quarto quarto) {
        if(quarto != null){
            synchronized (quarto) {
                if (quarto.checkIn(hospede)) {
                    System.out.println("Recepcionista " + id + " está alocando hóspede " + hospede.getIdHospede() + " no quarto " + quarto.getNumero()+"\n\t");
                    hospede.setQuarto(quarto);
                    //quartosDisponiveis.offer(quarto); // Retorna o quarto após o check-in
                }
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

