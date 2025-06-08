
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class extends  SmartCalculatorfx Application {

    private TextField display = new TextField();
    private double num1 = 0;
    private String operator = "";
    private boolean operatorPressed = false;

    @Override
    public void start(Stage stage) {
        display.setEditable(false);
        display.setStyle("-fx-font-size: 18px;");
        display.setPrefHeight(50);

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10));
        grid.setHgap(10);
        grid.setVgap(10);

        String[] buttons = {
                "7", "8", "9", "/",
                "4", "5", "6", "*",
                "1", "2", "3", "-",
                "0", ".", "C", "+",
                "="
        };

        int row = 1;
        int col = 0;

        for (String text : buttons) {
            Button btn = new Button(text);
            btn.setPrefSize(80, 80);
            btn.setStyle("-fx-font-size: 16px;");
            if ("+-*/".contains(text)) {
                btn.setStyle("-fx-background-color: #f0ad4e; -fx-font-size: 16px;");
            }
            btn.setOnAction(e -> handleInput(text));
            grid.add(btn, col, row);

            col++;
            if (col > 3) {
                col = 0;
                row++;
            }
        }

        grid.add(display, 0, 0, 4, 1);

        Scene scene = new Scene(grid, 270, 400);
        stage.setTitle("JavaFX Calculator");
        stage.setScene(scene);
        stage.show();
    }

    private void handleInput(String text) {
        switch (text) {
            case "C":
                display.clear();
                num1 = 0;
                operator = "";
                operatorPressed = false;
                break;
            case "=":
                if (operator.isEmpty() || operatorPressed) {
                    display.setText("Error");
                    return;
                }
                calculateResult();
                break;
            case "+": case "-": case "*": case "/":
                if (!operatorPressed && !display.getText().isEmpty()) {
                    num1 = Double.parseDouble(display.getText());
                    operator = text;
                    display.appendText(text);
                    operatorPressed = true;
                }
                break;
            case ".":
                // Prevent multiple decimals in a single number (after operator)
                if (operatorPressed) {
                    String[] parts = display.getText().split("[" + operator + "]");
                    if (parts.length == 2 && parts[1].contains(".")) return;
                } else {
                    if (display.getText().contains(".")) return;
                }
                display.appendText(".");
                break;
            default:
                display.appendText(text);
        }
    }

    private void calculateResult() {
        try {
            String[] parts = display.getText().split("[" + operator + "]");
            if (parts.length != 2) {
                display.setText("Error");
                return;
            }

            double num2 = Double.parseDouble(parts[1]);
            double result = switch (operator) {
                case "+" -> num1 + num2;
                case "-" -> num1 - num2;
                case "*" -> num1 * num2;
                case "/" -> {
                    if (num2 == 0) {
                        display.setText("Error");
                        return;
                    }
                    yield num1 / num2;
                }
                default -> 0;
            };

            display.setText(display.getText() + "=" + result);
            operator = "";
            operatorPressed = false;
        } catch (Exception e) {
            display.setText("Error");
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
} 
