package org.alessipg.client.ui;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.alessipg.client.infra.session.SessionManager;
import org.alessipg.shared.enums.Genre;
import org.alessipg.shared.enums.StatusTable;
import org.alessipg.shared.records.request.MovieCreateRequest;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory.IntegerSpinnerValueFactory;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.GridPane;
import javafx.scene.Node;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.util.converter.IntegerStringConverter;

public class MovieNewViewController {
    private Map<Genre, CheckBox> checkBoxGeneroMap = new HashMap<>();

    @FXML
    private GridPane gridGenre;

    @FXML
    private TextField tfTitle;

    @FXML
    private TextField tfDirector;

    @FXML
    private TextArea taSynopsis;

    @FXML
    private Spinner<Integer> spYear;

    final int MAX_CHARS = 250;

    @FXML
    public void initialize() {
        taSynopsis.setTextFormatter(
                new TextFormatter<String>(change -> change.getControlNewText().length() <= MAX_CHARS ? change : null));

        // Associa cada CheckBox ao seu Genero
        for (Node node : gridGenre.getChildren()) {
            if (node instanceof CheckBox checkBox) {
                // Use o método getDisplayName para comparar
                for (Genre genero : Genre.values()) {
                    if (genero.toString().equals(checkBox.getText())) {
                        checkBoxGeneroMap.put(genero, checkBox);
                        break;
                    }
                }
            }
        }

        // Configura Spinner de Ano: permitir edição via teclado e restringir a
        // 1900–2025
        if (spYear != null) {
            spYear.setEditable(true);

            IntegerSpinnerValueFactory vf;
            if (spYear.getValueFactory() instanceof IntegerSpinnerValueFactory existing) {
                vf = existing;
                vf.setMin(1900);
                vf.setMax(2025);
                if (vf.getValue() == null) {
                    vf.setValue(2020);
                }
            } else {
                vf = new IntegerSpinnerValueFactory(1900, 2025, 2020);
                spYear.setValueFactory(vf);
            }

            TextFormatter<Integer> formatter = new TextFormatter<>(
                    new IntegerStringConverter(),
                    vf.getValue(),
                    change -> {
                        String newText = change.getControlNewText();
                        if (newText == null)
                            return null;
                        if (newText.isEmpty())
                            return change; // permitir vazio durante digitação
                        if (!newText.matches("\\d{0,4}"))
                            return null; // apenas até 4 dígitos
                        return change;
                    });

            spYear.getEditor().setTextFormatter(formatter);

            formatter.valueProperty().addListener((obs, oldVal, newVal) -> {
                if (newVal == null)
                    return;
                int min = vf.getMin();
                int max = vf.getMax();
                int clamped = Math.min(Math.max(newVal, min), max);
                vf.setValue(clamped);
            });

            spYear.getEditor().focusedProperty().addListener((obs, wasFocused, isFocused) -> {
                if (!isFocused) {
                    String text = spYear.getEditor().getText();
                    int min = vf.getMin();
                    int max = vf.getMax();
                    int value = vf.getValue() != null ? vf.getValue() : 2020;
                    try {
                        if (text != null && !text.isBlank()) {
                            value = Integer.parseInt(text);
                        }
                    } catch (NumberFormatException ignored) {
                    }
                    int clamped = Math.min(Math.max(value, min), max);
                    vf.setValue(clamped);
                    spYear.getEditor().setText(String.valueOf(clamped));
                }
            });
        }
    }

    public List<Genre> getGenerosSelecionados() {
        return checkBoxGeneroMap.entrySet().stream()
                .filter(entry -> entry.getValue().isSelected())
                .map(entry -> entry.getKey())
                .toList();
    }

    @FXML
    private void onSubmit() {
        // ...lógica de cadastro...

        // Se cadastro OK:
        try {
            MovieCreateRequest movie = new MovieCreateRequest(
                    tfTitle.getText(),
                    tfDirector.getText(),
                    spYear.getValue(),
                    getGenerosSelecionados(),
                    taSynopsis.getText(),
                    SessionManager.getInstance().getToken());
            StatusTable res = SessionManager.getInstance()
                    .getMovieClientService().create(movie);
            Alert alert = new Alert(null);
            switch (res) {
                case CREATED:
                    alert.setAlertType(AlertType.INFORMATION);
                    alert.setHeaderText(null);
                    alert.setContentText(null);
                    alert.showAndWait();
                    break;
                case BAD:
                    alert.setAlertType(AlertType.ERROR);
                    alert.setHeaderText("Erro");
                    alert.setContentText("Algo deu errado");
                    alert.showAndWait();
                    break;
                case UNAUTHORIZED:
                    alert.setAlertType(AlertType.ERROR);
                    alert.setHeaderText("Não autorizado");
                    alert.setContentText("Você não está logado");
                    alert.showAndWait();
                    break;
                case ALREADY_EXISTS:
                    alert.setAlertType(AlertType.ERROR);
                    alert.setHeaderText("Já criado");
                    alert.setContentText("O filme " + tfTitle.getText() + " já existe.");
                    alert.showAndWait();
                    break;
                case UNPROCESSABLE_ENTITY:
                    alert.setAlertType(AlertType.ERROR);
                    alert.setHeaderText("Mensagem incorreta");
                    alert.setContentText("Servidor enviou uma mensagem fora dos padrões");
                    alert.showAndWait();
                    break;
                default:
                    alert.setAlertType(AlertType.ERROR);
                    alert.setHeaderText("Erro interno");
                    alert.setContentText("Um erro inesperado ocorreu.");
                    alert.showAndWait();
                    break;
            }
            // Fecha a janela atual
            Stage stage = (Stage) tfTitle.getScene().getWindow();
            stage.close();

            // Abre a tela de admin
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/alessipg/client/ui/AdminView.fxml"));
            Parent root = loader.load();
            Stage adminStage = new Stage();
            adminStage.setTitle("Administração");
            adminStage.setScene(new Scene(root));
            adminStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            // Exiba mensagem de erro se necessário
        }
    }
}
