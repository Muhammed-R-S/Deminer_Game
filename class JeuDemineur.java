package projet_jeu_demineur;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class JeuDemineur extends JFrame {
    private String playerName;
    private int SIZE = 16;
    private int MINES = 50;
    private ImageIcon bombIcon = new ImageIcon(new ImageIcon("image/mine.png").getImage().getScaledInstance(30, 30, Image.SCALE_DEFAULT));
    //private ImageIcon flagIcon = new ImageIcon(new ImageIcon("image/flag.jpg").getImage().getScaledInstance(30, 30, Image.SCALE_DEFAULT));

    private JButton[][] buttons;
    private char[][] solution;
    private boolean[][] revealed;
    private boolean firstClick = true;
    private int remainingMines = MINES;
    private int score = 0;

    private JLabel scoreLabel;
    private JLabel remainingMinesLabel;

    public JeuDemineur(String playerName, int size, int mines) {
        this.playerName = playerName;
        SIZE = size;
        MINES = mines;
        buttons = new JButton[SIZE][SIZE];
        solution = new char[SIZE][SIZE];
        revealed = new boolean[SIZE][SIZE];

        initializeBoard();
        calculateNumbers();

        initComponents();
    }

    private void initializeBoard() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                solution[i][j] = '-';
                revealed[i][j] = false;
            }
        }
    }

    private void calculateNumbers() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (solution[i][j] != 'X') {
                    int count = countAdjacentMines(i, j);
                    solution[i][j] = (count > 0) ? (char) (count + '0') : ' ';
                      //SI COUNT>0 , AFFICHER COUNT + 0 ELSE AFFICHER ' '
                }
            }
        }
    }

    private int countAdjacentMines(int row, int col) {
        int count = 0;
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                int newRow = row + i;
                int newCol = col + j;
                if (newRow >= 0 && newRow < SIZE && newCol >= 0 && newCol < SIZE && solution[newRow][newCol] == 'X') {
                    count++;
                }
            }
        }
        return count;
    }

    private void initComponents() {
        setTitle("Démineur -- " + playerName);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Créer le conteneur principal avec BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setPreferredSize(new Dimension(575, 600));

        // Panneau du haut pour afficher le score et le nombre de bombes restantes
        JPanel topPanel = new JPanel();
        scoreLabel = new JLabel("Score: " + score);
        remainingMinesLabel = new JLabel("               Mines restantes: " + remainingMines + "/" + MINES);
        topPanel.add(scoreLabel);
        topPanel.add(remainingMinesLabel);

        // Panneau du centre pour le jeu
        JPanel centerPanel = new JPanel(new GridLayout(SIZE, SIZE));
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                buttons[i][j] = new JButton();
                buttons[i][j].setMargin(new Insets(0, 0, 0, 0)); // Taille du text dans les boutons
                buttons[i][j].setBackground(Color.LIGHT_GRAY);

                final int row = i;
                final int col = j;
    
                buttons[i][j].addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (firstClick) {
                            placeMines(row, col);
                            calculateNumbers();
                            firstClick = false;
                        }
                        revealCell(row, col);
                    }
                });

                buttons[i][j].addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        if (SwingUtilities.isRightMouseButton(e)) {
                            markCell(row, col);
                        }
                    }
                });

                centerPanel.add(buttons[i][j]);
            }
        }

        // Panneau du bas pour le bouton "Quitter"
        JPanel bottomPanel = new JPanel();
        JButton quitteBoutton = new JButton("Quitter");
        quitteBoutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        bottomPanel.add(quitteBoutton);

        //Bouton meilleeurs scores 
        JButton meilleursScoresBoutton = new JButton("Meilleurs Scores");
        meilleursScoresBoutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                afficherMeilleursScores();
            }
        });
        bottomPanel.add(meilleursScoresBoutton);
        
        // Bouton réessayer
        JButton retryButton = new JButton("Réessayer");
        retryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                relancerPartie();
            }
        });
         bottomPanel.add(retryButton);

        // Ajouter les panneaux au conteneur principal
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        // Ajouter le conteneur principal à la JFrame
        add(mainPanel);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void revealCell(int row, int col) {
        if (revealed[row][col]) {
            return; // Si la bombe est revelé ça va sortir tout semplement
        }

        revealed[row][col] = true;
        buttons[row][col].setEnabled(false);

        if (solution[row][col] == 'X') {
            JOptionPane.showMessageDialog(this, "BOOM ! Vous avez perdu !");
            ajouterScoreDansBaseDeDonnees(playerName, score);
            revealAll();
        } else if (solution[row][col] == ' ') {
            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    int newRow = row + i;
                    int newCol = col + j;
                    if (newRow >= 0 && newRow < SIZE && newCol >= 0 && newCol < SIZE) {
                        revealCell(newRow, newCol);
                    }
                }
            }
            score++;  // Incrémenter le score pour chaque case vide révélée
            updateLabels();
        } else {
            buttons[row][col].setText(String.valueOf(solution[row][col]));
            buttons[row][col].setBackground(new Color(173, 216, 230));
            score++;  // Incrémenter le score pour chaque clic réussi sur une case non vide
            updateLabels();
        }

        if (solution[row][col] == ' ') {
            buttons[row][col].setBackground(new Color(173, 216, 230));
        }
    }

    private void markCell(int row, int col) {
       
        if (solution[row][col] == 'X') {
            buttons[row][col].setText("M");
            score += 2;
            buttons[row][col].setEnabled(false);
            buttons[row][col].setBackground(new Color(0, 128, 0));
            remainingMines--; 
        }
        else {
            JOptionPane.showMessageDialog(this, "Pas une Boome! Vous avez perdu !");
            revealAll();
        }
        updateLabels();
    }

    private void updateLabels() {
        scoreLabel.setText("Score: " + score);
        remainingMinesLabel.setText("            Mines restantes: " + remainingMines + "/" + MINES);
    }

    private void placeMines(int initialRow, int initialCol) {
        List<Integer> positions = new ArrayList<>();
        for (int i = 0; i < SIZE * SIZE; i++) { //This creates a list with all possible positions on the board, assuming the board is SIZE by SIZE
            positions.add(i);
        }

        Collections.shuffle(positions);

        int minesPlaced = 0;
        int currentIndex = 0;
        while (minesPlaced < MINES) {
            int position = positions.get(currentIndex);
            int row = position / SIZE;
            int col = position % SIZE;

            if (solution[row][col] != 'X' && (row != initialRow || col != initialCol)) {
                solution[row][col] = 'X';
                minesPlaced++;
            }

            currentIndex++;
        }
    }

    private void revealAll() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                buttons[i][j].setEnabled(false);
                if (solution[i][j] == 'X') {
                    buttons[i][j].setIcon(bombIcon);
                } else if (solution[i][j] == ' ') {
                    buttons[i][j].setText("");
                } else {
                    buttons[i][j].setText(String.valueOf(solution[i][j]));
                }
            }
        }
    }

    private void afficherMeilleursScores() {
        // Utilize the class GestionScores to retrieve the top 10 scores
        GestionDeScores gestionScores = new GestionDeScores();
        List<EntrerScore> meilleursScores = gestionScores.recupererMeilleursScores();

        // Display the best scores in the FenetreFinJeu window
        new FenetreFinJeu(playerName, meilleursScores);
    }

    private void ajouterScoreDansBaseDeDonnees(String joueur, int score) {
        GestionDeScores gestionScores = new GestionDeScores();
        gestionScores.ajouterScore(joueur, score);
    }
    
    private void relancerPartie() {
        // Logique pour relancer une nouvelle partie
        this.dispose();
        new JeuDemineur(playerName, 16, 50);
    }
}