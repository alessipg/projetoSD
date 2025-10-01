package org.alessipg.client.ui;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.alessipg.client.app.clientservice.FilmeClientService;
import org.alessipg.client.infra.session.SessionManager;
import org.alessipg.shared.enums.Genero;
import org.alessipg.shared.enums.StatusTable;
import org.alessipg.shared.records.MovieCreateRequest;
import org.hibernate.internal.SessionOwnerBehavior;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.SpinnerValueFactory.IntegerSpinnerValueFactory;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.GridPane;
import javafx.scene.Node;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.util.converter.IntegerStringConverter;

public class FilmeNovoViewController {
    private Map<Genero, CheckBox> checkBoxGeneroMap = new HashMap<>();

    @FXML
    private GridPane gridGenero;

    @FXML
    private javafx.scene.control.TextField tfTitulo;

    @FXML
    private javafx.scene.control.TextField tfDiretor;

    @FXML
    private javafx.scene.control.TextField tfSinopse;

    @FXML
    private javafx.scene.control.Spinner<Integer> spAno;

    @FXML
    public void initialize() {
        // Associa cada CheckBox ao seu Genero
        for (Node node : gridGenero.getChildren()) {
            if (node instanceof CheckBox checkBox) {
                // Use o método getDisplayName para comparar
                for (Genero genero : Genero.values()) {
                    if (genero.getDisplayName().equals(checkBox.getText())) {
                        checkBoxGeneroMap.put(genero, checkBox);
                        break;
                    }
                }
            }
        }

        // Configura Spinner de Ano: permitir edição via teclado e restringir a 1900–2025
        if (spAno != null) {
            spAno.setEditable(true);

            IntegerSpinnerValueFactory vf;
            if (spAno.getValueFactory() instanceof IntegerSpinnerValueFactory existing) {
                vf = existing;
                vf.setMin(1900);
                vf.setMax(2025);
                if (vf.getValue() == null) {
                    vf.setValue(2020);
                }
            } else {
                vf = new IntegerSpinnerValueFactory(1900, 2025, 2020);
                spAno.setValueFactory(vf);
            }

            TextFormatter<Integer> formatter = new TextFormatter<>(
                new IntegerStringConverter(),
                vf.getValue(),
                change -> {
                    String newText = change.getControlNewText();
                    if (newText == null) return null;
                    if (newText.isEmpty()) return change; // permitir vazio durante digitação
                    if (!newText.matches("\\d{0,4}")) return null; // apenas até 4 dígitos
                    return change;
                }
            );

            spAno.getEditor().setTextFormatter(formatter);

            formatter.valueProperty().addListener((obs, oldVal, newVal) -> {
                if (newVal == null) return;
                int min = vf.getMin();
                int max = vf.getMax();
                int clamped = Math.min(Math.max(newVal, min), max);
                vf.setValue(clamped);
            });

            spAno.getEditor().focusedProperty().addListener((obs, wasFocused, isFocused) -> {
                if (!isFocused) {
                    String text = spAno.getEditor().getText();
                    int min = vf.getMin();
                    int max = vf.getMax();
                    int value = vf.getValue() != null ? vf.getValue() : 2020;
                    try {
                        if (text != null && !text.isBlank()) {
                            value = Integer.parseInt(text);
                        }
                    } catch (NumberFormatException ignored) { }
                    int clamped = Math.min(Math.max(value, min), max);
                    vf.setValue(clamped);
                    spAno.getEditor().setText(String.valueOf(clamped));
                }
            });
        }
    }

    public List<String> getGenerosSelecionados() {
        return checkBoxGeneroMap.entrySet().stream()
                .filter(entry -> entry.getValue().isSelected())
                .map(entry -> entry.getKey().getDisplayName())
                .toList();
    }

    @FXML
    private void onSubmit() {
        // ...lógica de cadastro...

        // Se cadastro OK:
        try {
            MovieCreateRequest movie = new MovieCreateRequest(
                tfTitulo.getText(),
                tfDiretor.getText(),
                spAno.getValue(),
                getGenerosSelecionados(),
                tfSinopse.getText(),
                SessionManager.getInstance().getToken()
            );
            StatusTable res = SessionManager.getInstance()
                    .getFilmeClientService().criar(movie);

            // Fecha a janela atual
            Stage stage = (Stage) tfTitulo.getScene().getWindow();
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
