package utilitare.general;

import java.io.Serializable;
import java.util.List;

public class Intrebare implements Serializable {
    private String cerinta;
    private List<String> varianteRaspuns;
    private int raspunsCorect;
    private String materie;

    public Intrebare() {
    }

    public Intrebare(String cerinta, List<String> varianteRaspuns, int raspunsCorect,String materie) {
        this.cerinta = cerinta;
        this.varianteRaspuns = varianteRaspuns;
        this.raspunsCorect = raspunsCorect;
        this.materie=materie;
    }

    public String getMaterie() {
        return materie;
    }

    public void setMaterie(String materie) {
        this.materie = materie;
    }

    public String getCerinta() {
        return cerinta;
    }

    public void setCerinta(String cerinta) {
        this.cerinta = cerinta;
    }

    public List<String> getVarianteRaspuns() {
        return varianteRaspuns;
    }

    public void setVarianteRaspuns(List<String> varianteRaspuns) {
        this.varianteRaspuns = varianteRaspuns;
    }

    public int getRaspunsCorect() {
        return raspunsCorect;
    }

    public void setRaspunsCorect(int raspunsCorect) {
        this.raspunsCorect = raspunsCorect;
    }

}
