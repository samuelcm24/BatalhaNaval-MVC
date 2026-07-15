package batalhanaval;

import java.io.IOException;
import java.util.Random;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import batalhanaval.control.JogoController;
import batalhanaval.control.RankingController;
import batalhanaval.model.IAAdversaria;
import batalhanaval.view.StartJanela;

public class BatalhaNaval {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(BatalhaNaval::iniciar);
    }

    private static void iniciar() {
        RankingController rankingController = new RankingController();
        try {
            rankingController.carregar();
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null,
                    "Nao foi possivel carregar o ranking. O jogo sera iniciado normalmente.",
                    "Ranking indisponivel",
                    JOptionPane.WARNING_MESSAGE);
        }

        JogoController jogoController = new JogoController(
                new IAAdversaria(),
                rankingController,
                new Random());
        new StartJanela(jogoController, rankingController);
    }
}
