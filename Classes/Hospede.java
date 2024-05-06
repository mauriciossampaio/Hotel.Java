package Classes;

import java.util.concurrent.ThreadLocalRandom;

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
                if (quarto != null){
                    if (quarto.isChaveNaRecepcao()) {
                        synchronized (quarto) {
                            if (quarto.isChaveNaRecepcao()) {
                                checkInFeito = quarto.checkIn(this);
                                if (checkInFeito) {
                                    quarto.setChaveNaRecepcao(false);
                                    System.out.println("Hóspede " + id + " fez check-in no quarto " + quarto.getNumero()+"\n\t\t\t");
                                }
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
