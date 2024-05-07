package Classes;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.locks.Lock;

class Hospede extends Thread {
    private int id;
    private Quarto quarto;
    private Lock locke;
    public Hospede(int id, Quarto quarto, Lock locke) {
        this.id = id;
        this.quarto = quarto;
        this.locke = locke;
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

            if (ThreadLocalRandom.current().nextBoolean()) {
                passear();
            }
            quarto.checkOut(this);
            System.out.println("chekout");

        }
    }

    private void passear() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private synchronized void fazerCheckIn() {
        boolean checkInFeito = false;
        while (!checkInFeito) {
                if (quarto != null){

                        if (quarto.isChaveNaRecepcao()) {
                            synchronized (quarto) {

                                checkInFeito = quarto.getChekin(this);

                                if (checkInFeito) {
                                        System.out.println("Hóspede " + id + " fez check-in no quarto " + quarto.getNumero()+"\n\t\t\t");
                                    }
                            }
                        }
                }

        }
    }

    private void ficarNoQuarto() {
        try {
            Thread.sleep(ThreadLocalRandom.current().nextInt(500, 1000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public int getIdHospede() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Quarto getQuarto() {
        return quarto;
    }
}
