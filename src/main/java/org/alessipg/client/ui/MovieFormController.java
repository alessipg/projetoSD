package org.alessipg.client.ui;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.scene.control.*;
import org.alessipg.client.infra.session.SessionManager;
import org.alessipg.shared.enums.Genre;
import org.alessipg.shared.enums.StatusTable;
import org.alessipg.shared.dto.request.MovieCreateRequest;

import javafx.fxml.FXML;
import javafx.scene.control.SpinnerValueFactory.IntegerSpinnerValueFactory;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.GridPane;
import javafx.scene.Node;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.util.converter.IntegerStringConverter;
import org.alessipg.shared.dto.util.MovieRecord;

public class MovieFormController {
    private Map<Genre, CheckBox> checkBoxGeneroMap = new HashMap<>();
    private boolean isEditMode = false;
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
    @FXML
    private Label formTitle;
    @FXML
    private Button btnSubmit;

    final int MAX_CHARS = 250;

    @FXML
    public void initialize() throws IOException {

        taSynopsis.setTextFormatter(new TextFormatter<String>(change -> change.getControlNewText().length() <= MAX_CHARS ? change : null));

        // Associa cada CheckBox ao seu Genero
        for (Node node : gridGenre.getChildren()) {
            if (node instanceof CheckBox checkBox) {
                // Use o métod getDisplayName para comparar
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

            TextFormatter<Integer> formatter = new TextFormatter<>(new IntegerStringConverter(), vf.getValue(), change -> {
                String newText = change.getControlNewText();
                if (newText == null) return null;
                if (newText.isEmpty()) return change; // permitir vazio durante digitação
                if (!newText.matches("\\d{0,4}")) return null; // apenas até 4 dígitos
                return change;
            });

            spYear.getEditor().setTextFormatter(formatter);

            formatter.valueProperty().addListener((obs, oldVal, newVal) -> {
                if (newVal == null) return;
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
        tfTitle.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                newScene.windowProperty().addListener((obsWin, oldWin, newWin) -> {
                    if (newWin != null) {
                        Stage stage = (Stage) newWin;
                        MovieRecord movie = (MovieRecord) stage.getUserData();
                        if (movie != null) {
                            isEditMode = true;
                            btnSubmit.setText("Salvar");
                            formTitle.setText("Editar filme");
                            tfTitle.setText(movie.titulo());
                            tfDirector.setText(movie.diretor());
                            spYear.getValueFactory().setValue(Integer.valueOf(movie.ano()));
                            taSynopsis.setText(movie.sinopse());
                            try {
                                for (String genero : movie.genero()) {
                                    CheckBox cb = checkBoxGeneroMap.get(Genre.from(genero));
                                    if (cb != null) {
                                        cb.setSelected(true);
                                    }
                                }
                            } catch (IllegalArgumentException e) {
                                Alert alert = new Alert(AlertType.ERROR);
                                alert.setHeaderText("Gênero inválido");
                                alert.setContentText("O filme possui um gênero inválido: " + e.getMessage());
                                alert.showAndWait();
                            }
                        }
                    }
                });
            }
        });
    }

    public List<Genre> getGenerosSelecionados() {
        return checkBoxGeneroMap.entrySet().stream().filter(entry -> entry.getValue().isSelected()).map(entry -> entry.getKey()).toList();
    }

    @FXML
    private void onCancel() throws IOException {
        //TODO: Se houver alterações, pedir confirmação
        Stage stage = (Stage) tfTitle.getScene().getWindow();
        stage.close();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/alessipg/client/ui/AdminView.fxml"));
        Parent root = loader.load();
        Stage adminStage = new Stage();
        adminStage.setTitle("Administração");
        adminStage.setScene(new Scene(root));
        adminStage.show();
    }

    @FXML
    private void onSubmit() {
        StatusTable res = null;
        try {
            if (isEditMode) {
                MovieRecord movie = new MovieRecord(
                        ((MovieRecord) tfTitle.getScene().getWindow().getUserData()).id(),
                        tfTitle.getText(),
                        tfDirector.getText(),
                        spYear.getValue().toString(),
                        getGenerosSelecionados().stream().map(Genre::toString).toList(),
                        null,
                        null,
                        taSynopsis.getText()
                );
                res = SessionManager.getInstance().getMovieClientService().edit(movie);
            } else {
                MovieCreateRequest movie = new MovieCreateRequest(tfTitle.getText(), tfDirector.getText(), spYear.getValue(), getGenerosSelecionados(), taSynopsis.getText(), SessionManager.getInstance().getToken());
                res = SessionManager.getInstance().getMovieClientService().create(movie);
            }
            Alert alert = new Alert(null);
            switch (res) {
                case OK:
                case CREATED:
                    alert.setAlertType(AlertType.INFORMATION);
                    alert.setHeaderText("Sucesso");
                    alert.setContentText("Operação realizada com sucesso.");
                    alert.showAndWait();
                    break;
                default:
                    alert.setAlertType(AlertType.ERROR);
                    alert.setHeaderText("Erro");
                    alert.setContentText(res.getMessage());
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
