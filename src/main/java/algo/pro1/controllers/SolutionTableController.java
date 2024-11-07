package algo.pro1.controllers;

import algo.pro1.Solution;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.util.Arrays;

public class SolutionTableController {
    @FXML
    private TableView tableView;

    @FXML
    private TextField tfCoins;

    @FXML
    private TextField tfExpectedResult;

    public void inject(int[][] table, int[] coins) {
        int n = coins.length;
        int expectedVale = table[0][n-1];
        int[] expectedValueCoins = Solution.coinsTrace(coins, table);

        tfExpectedResult.setText(String.valueOf(expectedVale));
        tfCoins.setText(Arrays.toString(expectedValueCoins));

        for (int col = 0; col < n; col++) {
            TableColumn<Row, Integer> column = new TableColumn<>(coins[col] + " [j = " + col + "] ");
            final int colIndex = col; // Needs a final variable for lambda
            column.setCellValueFactory(cellData -> cellData.getValue().getValue(colIndex).asObject());
            tableView.getColumns().add(column);
        }

        for (int[] row: table) {
            tableView.getItems().add(new Row(row));
        }
    }

    private static class Row {
        private final Integer[] values;

        public Row(int[] values) {
            this.values = new Integer[values.length];
            for (int i = 0; i < values.length; i++) {
                this.values[i] = values[i];
            }
        }

        public IntegerProperty getValue(int index) {
            return new SimpleIntegerProperty(values[index]);
        }
    }

}
