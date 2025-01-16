package projet_jeu_demineur;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


public class GestionDeScores {

    private Connection connection;
    private String url = "jdbc:mysql://localhost:3306/jeu_demineur";
    private String userName = "root";
    private String password = "";

    public GestionDeScores() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(url, userName, password);
            System.out.println("Connexion à la base de données ... OK!");
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("Erreur lors de la connexion à la base de données : " + e.getMessage());
            // Gérer l'erreur d'une manière appropriée
        }

        System.out.println("Constructeur terminé : La gestion des scores est prête !");
    }

    public void ajouterScore(String joueur, int score) {
        String query = "INSERT INTO scores (joueur, score) VALUES (?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, joueur);
            preparedStatement.setInt(2, score);
            preparedStatement.executeUpdate();
            System.out.println("Score ajouté avec succès !");
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout du score : " + e.getMessage());
            // Gérer l'erreur d'une manière appropriée
        }
    }

    public void afficherScores() {
        String query = "SELECT joueur, score FROM scores";

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            System.out.println("Scores actuels :");
            while (resultSet.next()) {
                String joueur = resultSet.getString("joueur");
                int score = resultSet.getInt("score");
                System.out.println("Joueur : " + joueur + ", Score : " + score);
            }

        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des scores : " + e.getMessage());
            // Gérer l'erreur d'une manière appropriée
        }
    }
    
    public List<EntrerScore> recupererMeilleursScores() {
        String query = "SELECT joueur, score FROM scores ORDER BY score DESC LIMIT 10";

        List<EntrerScore> meilleursScores = new ArrayList<>();

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                String joueur = resultSet.getString("joueur");
                int score = resultSet.getInt("score");
                EntrerScore scoreEntry = new EntrerScore(joueur, score);
                meilleursScores.add(scoreEntry);
            }

        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des meilleurs scores : " + e.getMessage());
        }

        return meilleursScores;
    }


}

