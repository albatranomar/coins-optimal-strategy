package algo.pro1;

import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;

// Encapsulate all the logic for displaying a coin on the screen
public class Coin extends StackPane {
    private final int value;
    private boolean clickable;

    public Coin(int value, boolean clickable) {
        this.value = value;
        this.clickable = clickable;

        if (clickable) this.setStyle("-fx-cursor: hand");

        // Creates new circle with radius 30
        Circle circle = new Circle(30);
        circle.getStyleClass().add("coin");

        Label valueLabel = new Label(String.valueOf(value));
        valueLabel.getStyleClass().add("coin-value");

        this.getChildren().addAll(circle, valueLabel);
    }

    public int getValue() {
        return value;
    }

    public boolean isClickable() {
        return clickable;
    }

    public void setClickable(boolean clickable) {
        this.clickable = clickable;
        if (clickable) this.setStyle("-fx-cursor: hand");
    }
}

