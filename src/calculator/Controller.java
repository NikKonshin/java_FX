package calculator;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class Controller {
    int sumA = 0;
    int sumB = 0;
    char sign;

    public TextField textField;

    public void readProcessing(ActionEvent actionEvent) {
        String a = ((Button) actionEvent.getSource()).getText();
        if (a.equals("C")) {
            textField.clear();
            sumA = 0;
            sumB = 0;
        }
        if (!a.equals("/") && !a.equals("*") && !a.equals("-") && !a.equals("+") && !a.equals("=") && !a.equals("C")) {
            textField.appendText(a);

        }
        if (a.equals("/")) {
            sumA = Integer.parseInt(textField.getText());
            textField.clear();
            sign = '/';
        }
        if (a.equals("*")) {
            sumA = Integer.parseInt(textField.getText());
            textField.clear();
            sign = '*';
        }
        if (a.equals("-")) {
            sumA = Integer.parseInt(textField.getText());
            textField.clear();
            sign = '-';
        }
        if (a.equals("+")) {
            sumA = Integer.parseInt(textField.getText());
            textField.clear();
            sign = '+';
        }

        if (a.equals("=")) {
            if (sign == '/') {
                sumB = Integer.parseInt(textField.getText());
                textField.setText(String.valueOf((double) sumA / sumB));
            }
            if (sign == '+') {
                sumB = Integer.parseInt(textField.getText());
                textField.setText(String.valueOf((double) sumA + sumB));
            }
            if (sign == '*') {
                sumB = Integer.parseInt(textField.getText());
                textField.setText(String.valueOf((double) sumA * sumB));
            }
            if (sign == '-') {
                sumB = Integer.parseInt(textField.getText());
                textField.setText(String.valueOf((double) sumA - sumB));

            }
        }


    }
}

