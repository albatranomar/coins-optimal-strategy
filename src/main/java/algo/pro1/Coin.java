package algo.pro1;

import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;

// Encapsulate all the logic for displaying a coin on the screen
public class Coin extends StackPane {
    private final int value;
    private boolean clickAble;

    public Coin(int value, boolean clickAble) {
        this.value = value;
        this.clickAble = clickAble;

        if (clickAble) this.setStyle("-fx-cursor: hand");

        Circle circle = new Circle(30);
        circle.getStyleClass().add("coin");

        Label valueLabel = new Label(String.valueOf(value));
        valueLabel.getStyleClass().add("coin-value");

        this.getChildren().addAll(circle, valueLabel);
    }

    public int getValue() {
        return value;
    }

    public boolean isClickAble() {
        return clickAble;
    }

    public void setClickAble(boolean clickAble) {
        this.clickAble = clickAble;
        if (isClickAble()) this.setStyle("-fx-cursor: hand");
    }
}

