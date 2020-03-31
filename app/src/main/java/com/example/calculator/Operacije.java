package com.example.calculator;

public class Operacije {

    private int pozicija;
    private String operacija;

    public Operacije(){}

    public Operacije(int pozicija, String operacija) {
        this.pozicija = pozicija;
        this.operacija = operacija;
    }

    public int getPozicija() {
        return pozicija;
    }

    public String getOperacija() {
        return operacija;
    }

    public void setPozicija(int pozicija) {
        this.pozicija = pozicija;
    }

    public void setOperacija(String operacija) {
        this.operacija = operacija;
    }

    @Override
    public String toString() {
        return "Operacije{" +
                "pozicija=" + pozicija +
                '}';
    }
}
