package com.example.quanlynhasach.model;

public class debtModel {
    String id;
    int debt;
    int preDebt;

    public debtModel(String id, int debt, int preDebt) {
        this.id = id;
        this.debt = debt;
        this.preDebt = preDebt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getDebt() {
        return debt;
    }

    public void setDebt(int debt) {
        this.debt = debt;
    }

    public int getPreDebt() {
        return preDebt;
    }

    public void setPreDebt(int preDebt) {
        this.preDebt = preDebt;
    }
}
