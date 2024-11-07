package algo.pro1;

import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Coin extends StackPane {
    private final Circle circle;
    private final Label valueLabel;
    private int value;
    private boolean clickAble;

    public Coin(int value, boolean clickAble) {
        this.value = value;
        this.clickAble = clickAble;

        if (clickAble) this.setStyle("-fx-cursor: hand");

        circle = new Circle(30);
        circle.getStyleClass().add("coin");

        valueLabel = new Label(String.valueOf(value));
        valueLabel.getStyleClass().add("coin-value");

        this.getChildren().addAll(circle, valueLabel);
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
        valueLabel.setText(String.valueOf(value));
    }

    public boolean isClickAble() {
        return clickAble;
    }

    public void setClickAble(boolean clickAble) {
        this.clickAble = clickAble;
        if (isClickAble()) this.setStyle("-fx-cursor: hand");
    }

    public void setCircleColor(Color fill, Color stroke) {
        circle.setFill(fill);
        circle.setStroke(stroke);
    }
}

