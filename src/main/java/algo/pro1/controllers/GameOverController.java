package algo.pro1.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class GameOverController {

    @FXML
    private Label lblName;

    @FXML
    private Label lblWinner;

    public void inject(String name, boolean isDraw) {
        if (isDraw) {
            lblName.setVisible(false);
            lblWinner.setText("It's a draw!");
            return;
        }
        lblName.setText(name);
    }
}
