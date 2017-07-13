package com.app.holder;

public class General {

    String s1 = "", s2 = "";
    int n1 = 0, n2 = 0;

    public General(String s1, String s2) {
        this.s1 = s1;
        this.s2 = s2;
    }

    public General(String s1, int n1) {
        this.s1 = s1;
        this.n1 = n1;
    }

    public String getS1() {

        return s1;
    }

    public void setS1(String s1) {
        this.s1 = s1;
    }

    public String getS2() {
        return s2;
    }

    public void setS2(String s2) {
        this.s2 = s2;
    }

    public int getN1() {
        return n1;
    }

    public void setN1(int n1) {
        this.n1 = n1;
    }

    public int getN2() {
        return n2;
    }

    public void setN2(int n2) {
        this.n2 = n2;
    }
}
