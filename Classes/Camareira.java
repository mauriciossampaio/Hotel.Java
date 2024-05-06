package Classes;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadLocalRandom;

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
        System.out.println("Camareira " + id + " está limpando quarto " + quarto.getNumero()+"\n\t");
        try {
            Thread.sleep(ThreadLocalRandom.current().nextInt(1000, 3000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Camareira " + id + " terminou de limpar quarto " + quarto.getNumero()+"\n\t");
    }

    public int getIdCamareira() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}

