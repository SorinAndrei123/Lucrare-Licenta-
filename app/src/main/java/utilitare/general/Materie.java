package utilitare.general;

public class Materie {
    private String nume;
    private String profesor;
    private String tip;
    private String zi;
    private String ora;
    private  String grupa;
    private String serie;
    private String an;

    public Materie() {
    }

    public Materie(String nume, String profesor, String tip, String zi, String ora, String grupa, String serie,String an) {
        this.nume = nume;
        this.profesor = profesor;
        this.tip = tip;
        this.zi = zi;
        this.ora = ora;
        this.grupa = grupa;
        this.serie = serie;
        this.an=an;
    }

    public Materie(String nume, String profesor, String tip, String zi, String ora, String grupa,String an) {
        this.nume = nume;
        this.profesor = profesor;
        this.tip = tip;
        this.zi = zi;
        this.ora = ora;
        this.grupa = grupa;
        this.an=an;
    }


    public String getAn() {
        return an;
    }

    public void setAn(String an) {
        this.an = an;
    }

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public String getProfesor() {
        return profesor;
    }

    public void setProfesor(String profesor) {
        this.profesor = profesor;
    }

    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }

    public String getZi() {
        return zi;
    }

    public void setZi(String zi) {
        this.zi = zi;
    }

    public String getOra() {
        return ora;
    }

    public void setOra(String ora) {
        this.ora = ora;
    }

    public String getGrupa() {
        return grupa;
    }

    public void setGrupa(String grupa) {
        this.grupa = grupa;
    }

    public String getSerie() {
        return serie;
    }

    public void setSerie(String serie) {
        this.serie = serie;
    }

    @Override
    public String toString() {
        return "Materie{" +
                "nume='" + nume + '\'' +
                ", profesor='" + profesor + '\'' +
                ", tip='" + tip + '\'' +
                ", zi='" + zi + '\'' +
                ", ora='" + ora + '\'' +
                '}';
    }
}
