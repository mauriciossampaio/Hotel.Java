package Classes;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

class Quarto {
    private int numero;
    private List<Hospede> hospedes;
    private boolean chaveNaRecepcao;
    private Queue<Quarto> quartos;
    private boolean foiLimpo = true;
    private boolean reservado = false;

    public Quarto(int numero, Queue<Quarto> quartos) {
        this.numero = numero;
        this.hospedes = new ArrayList<>();
        this.chaveNaRecepcao = true;
        this.quartos = quartos;
    }

    public synchronized boolean checkIn(Hospede hospede) {
        if (hospedes.size() < 4) {
            hospedes.add(hospede);
            return true;
        }
        chaveNaRecepcao= true;
        return false;
    }

    public synchronized void checkOut(Hospede hospede) {
            hospedes.remove(hospede);
            if (hospedes.size() == 0) {
                chaveNaRecepcao = true;
                quartos.offer(this);
                System.out.println("checkout no quarto "+numero);
            }


    }

    public boolean getChekin(Hospede hospede){
        if (hospedes.contains(hospede)){
            return true;
        }
        return false;
    }

    public boolean isReservado() {
        return reservado;
    }

    public void setReservado(boolean reservado) {
        this.reservado = reservado;
    }

    public boolean isFoiLimpo() {
        return foiLimpo;
    }

    public void setFoiLimpo(boolean foiLimpo) {
        this.foiLimpo = foiLimpo;
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

