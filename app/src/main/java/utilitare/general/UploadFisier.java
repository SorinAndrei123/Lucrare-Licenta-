package utilitare.general;

public class UploadFisier {
    private String fisierUrl;
    private String idSarcina;
    private String numeStudent;
    private int notaAcordata;

    public UploadFisier() {
    }

    public UploadFisier(String fisierUrl, String idSarcina, String numeStudent) {
        this.fisierUrl = fisierUrl;
        this.idSarcina = idSarcina;
        this.numeStudent = numeStudent;
    }

    public String getFisierUrl() {
        return fisierUrl;
    }

    public void setFisierUrl(String fisierUrl) {
        this.fisierUrl = fisierUrl;
    }

    public String getIdSarcina() {
        return idSarcina;
    }

    public void setIdSarcina(String idSarcina) {
        this.idSarcina = idSarcina;
    }

    public String getNumeStudent() {
        return numeStudent;
    }

    public void setNumeStudent(String numeStudent) {
        this.numeStudent = numeStudent;
    }

    public int getNotaAcordata() {
        return notaAcordata;
    }

    public void setNotaAcordata(int notaAcordata) {
        this.notaAcordata = notaAcordata;
    }
}
