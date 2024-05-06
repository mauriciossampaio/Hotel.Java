package Classes;

import java.util.ArrayList;
import java.util.List;

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

