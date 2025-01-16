package projet_jeu_demineur;

import java.util.List;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class FenetreFinJeu extends JFrame {
	private String playerName;
    private List<EntrerScore> meilleursScores;

    public FenetreFinJeu(String playerName, List<EntrerScore> meilleursScores) {
        this.playerName = playerName;
        this.meilleursScores = meilleursScores;

        setTitle("BEST SCORES!");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel();
        for (int i = 0; i < Math.min(10, meilleursScores.size()); i++) {
            EntrerScore scoreEntry = meilleursScores.get(i);
            String labelText = (i + 1) + ".  Joueur : " + scoreEntry.getJoueur() +
                               " Score : " + scoreEntry.getScore();
            JLabel label = new JLabel(labelText);
            mainPanel.add(label);
        }

        add(mainPanel);

        setSize(275, 300);
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
