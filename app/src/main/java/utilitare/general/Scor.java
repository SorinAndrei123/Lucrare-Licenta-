package utilitare.general;

public class Scor {
    private int highscore;
    private String numeStudent;
    private String numeMaterie;

    public Scor() {
    }

    public Scor(int highscore, String numeStudent, String numeMaterie) {
        this.highscore = highscore;
        this.numeStudent = numeStudent;
        this.numeMaterie = numeMaterie;
    }

    public int getHighscore() {
        return highscore;
    }

    public void setHighscore(int highscore) {
        this.highscore = highscore;
    }

    public String getNumeStudent() {
        return numeStudent;
    }

    public void setNumeStudent(String numeStudent) {
        this.numeStudent = numeStudent;
    }

    public String getNumeMaterie() {
        return numeMaterie;
    }

    public void setNumeMaterie(String numeMaterie) {
        this.numeMaterie = numeMaterie;
    }
}
