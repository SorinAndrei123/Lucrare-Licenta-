package utilitare.general;

import java.io.Serializable;

public class Sarcina implements Serializable {
    private String descriere;
    private String idMaterie;
    private String tipFisierDorit;
    private String dataDeadline;
    private String oraDeadline;
    private int notaAcordata;
    private String nrSaptDeadline;

    public Sarcina() {
    }

    public Sarcina(String descriere, String idMaterie, String tipFisierDorit, String dataDeadline, String oraDeadline, int notaAcordata, String nrSaptDeadline) {
        this.descriere = descriere;
        this.idMaterie = idMaterie;
        this.tipFisierDorit = tipFisierDorit;
        this.dataDeadline = dataDeadline;
        this.oraDeadline = oraDeadline;
        this.notaAcordata = notaAcordata;
        this.nrSaptDeadline = nrSaptDeadline;
    }

    public String getDescriere() {
        return descriere;
    }

    public void setDescriere(String descriere) {
        this.descriere = descriere;
    }

    public String getIdMaterie() {
        return idMaterie;
    }

    public void setIdMaterie(String idMaterie) {
        this.idMaterie = idMaterie;
    }

    public String getTipFisierDorit() {
        return tipFisierDorit;
    }

    public void setTipFisierDorit(String tipFisierDorit) {
        this.tipFisierDorit = tipFisierDorit;
    }

    public String getDataDeadline() {
        return dataDeadline;
    }

    public void setDataDeadline(String dataDeadline) {
        this.dataDeadline = dataDeadline;
    }

    public String getOraDeadline() {
        return oraDeadline;
    }

    public void setOraDeadline(String oraDeadline) {
        this.oraDeadline = oraDeadline;
    }

    public int getNotaAcordata() {
        return notaAcordata;
    }

    public void setNotaAcordata(int notaAcordata) {
        this.notaAcordata = notaAcordata;
    }

    public String getNrSaptDeadline() {
        return nrSaptDeadline;
    }

    public void setNrSaptDeadline(String nrSaptDeadline) {
        this.nrSaptDeadline = nrSaptDeadline;
    }
}
