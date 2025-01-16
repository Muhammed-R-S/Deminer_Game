 package projet_jeu_demineur;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FenetreDeLogin extends JFrame {
    private String playerName;

    public FenetreDeLogin() {
        setTitle("Démineur - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel loginPanel = new JPanel(new GridLayout(4, 2));

        JLabel gameTitleLabel = new JLabel("Démineur", SwingConstants.CENTER);
        JLabel nameLabel = new JLabel("  Nom d'utilisateur:");
        JTextField nameField = new JTextField();
        JButton playButton = new JButton("Jouer");

        playButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                playerName = nameField.getText().trim();
                if (playerName.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Veuillez entrer un nom d'utilisateur.", "Erreur", JOptionPane.ERROR_MESSAGE);
                } else if (playerName.startsWith(" ")) {
                    JOptionPane.showMessageDialog(null, "Le nom d'utilisateur ne peut pas commencer par un espace.", "Erreur", JOptionPane.ERROR_MESSAGE);
                } else {
                    closeLoginWindow();
                    openGameWindow();
                }
            }
        });

        loginPanel.add(gameTitleLabel);
        loginPanel.add(new JLabel()); // Espacement
        loginPanel.add(nameLabel);
        loginPanel.add(nameField);
        loginPanel.add(new JLabel()); // Espacement
        loginPanel.add(playButton);

        add(loginPanel);

        // Définissez la taille du panneau pour remplir l'écran (ajustez selon vos besoins)
        setSize(600, 300);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void closeLoginWindow() {
        dispose(); // Ferme la fenêtre de login
    }

    private void openGameWindow() {
        // Spécifiez la taille de la grille et le nombre de bombes ici (ajustez selon vos besoins)
        new JeuDemineur(playerName, 16, 50);
    }


}

