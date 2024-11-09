package algo.pro1.controllers;

import algo.pro1.Solution;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.util.Arrays;

public class SolutionTableController {
    @FXML
    private TableView<Row> tableView;

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

        TableColumn<Row, String> labelColumn = new TableColumn<>("Row/Column");
        labelColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getLabel()));

        tableView.getColumns().add(labelColumn);

        for (int col = 0; col < n; col++) {
            TableColumn<Row, Integer> column = new TableColumn<>(coins[col] + " [j = " + col + "] ");
            final int colIndex = col; // Needs a final variable for lambda
            column.setCellValueFactory(cellData -> cellData.getValue().get(colIndex).asObject());
            tableView.getColumns().add(column);
        }

        for (int i = 0; i < table.length; i++) {
            int[] row = table[i];
            tableView.getItems().add(new Row(coins[i] + " [i = " + i + "]", row));
        }
    }

    private static class Row {
        private final String label;
        private final Integer[] values;

        public Row(String label, int[] values) {
            this.label = label;
            this.values = new Integer[values.length];
            for (int i = 0; i < values.length; i++) {
                this.values[i] = values[i];
            }
        }

        public String getLabel() {
            return label;
        }

        public IntegerProperty get(int index) {
            return new SimpleIntegerProperty(values[index]);
        }
    }

}
