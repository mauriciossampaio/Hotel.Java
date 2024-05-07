package Classes;

import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadLocalRandom;

class Camareira extends Thread {
    private int id;
    private Queue<Quarto> quartosParaLimpar;

    public Camareira(int id, Queue<Quarto> quartosParaLimpar) {
        this.id = id;
        this.quartosParaLimpar = quartosParaLimpar;
    }

    @Override
    public synchronized void run() {
        while (true) {
            try {
                Thread.sleep(1);
                if (quartosParaLimpar.peek() != null && quartosParaLimpar.peek().isChaveNaRecepcao() && !quartosParaLimpar.peek().isFoiLimpo()) {
                    Quarto quarto = quartosParaLimpar.poll();
                    limparQuarto(quarto);
                }
            }catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

    private void limparQuarto(Quarto quarto) {
        quarto.setChaveNaRecepcao(false);
        System.out.println("Camareira " + id + " está limpando quarto " + quarto.getNumero()+"\n\t");
        try {
            Thread.sleep(5000);
            quartosParaLimpar.offer(quarto);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        quarto.setFoiLimpo(true);
        quarto.setChaveNaRecepcao(true);
        System.out.println("Camareira " + id + " terminou de limpar quarto " + quarto.getNumero()+"\n\t");
    }

    public int getIdCamareira() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}

