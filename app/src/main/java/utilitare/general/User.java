package utilitare.general;

import java.io.Serializable;

public class User implements Serializable {
    private String nume;
    private int an;
    private int grupa;
    private String serie;
    private String tipCont;
public User(){

}
    public User(String nume, int an, int grupa, String serie, String tipCont) {
        this.nume = nume;
        this.an = an;
        this.grupa = grupa;
        this.serie = serie;
        this.tipCont = tipCont;
    }

    public User(String nume, String tipCont) {
        this.nume = nume;
        this.tipCont = tipCont;
    }

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public int getAn() {
        return an;
    }

    public void setAn(int an) {
        this.an = an;
    }

    public int getGrupa() {
        return grupa;
    }

    public void setGrupa(int grupa) {
        this.grupa = grupa;
    }

    public String getSerie() {
        return serie;
    }

    public void setSerie(String serie) {
        this.serie = serie;
    }

    public String getTipCont() {
        return tipCont;
    }

    public void setTipCont(String tipCont) {
        this.tipCont = tipCont;
    }

    @Override
    public String toString() {
        return "User{" +
                "nume='" + nume + '\'' +
                ", an=" + an +
                ", grupa=" + grupa +
                ", serie='" + serie + '\'' +
                ", tipCont='" + tipCont + '\'' +
                '}';
    }

}
