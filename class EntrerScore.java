package projet_jeu_demineur;

public class EntrerScore {
    private String joueur;
    private int score;
    
 // Constructeur, getters, setters, etc.
	public EntrerScore() {
		super();
	}

	public EntrerScore(String joueur, int score) {
		super();
		this.joueur = joueur;
		this.score = score;
	}

	public String getJoueur() {
		return joueur;
	}

	public void setJoueur(String joueur) {
		this.joueur = joueur;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	@Override
	public String toString() {
		return "ScoreEntry [joueur=" + joueur + ", score=" + score + "]";
	}
    
}

