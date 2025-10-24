package org.alessipg.client.ui;

import org.alessipg.shared.records.util.MovieRecord;

import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class MovieListCell extends ListCell<MovieRecord> {
    private VBox vbox = new VBox(5);
    private Label lblTitulo = new Label();
    private Label lblInfo = new Label();
    private Label lblNota = new Label();

    public MovieListCell() {
        lblTitulo.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        lblInfo.setStyle("-fx-text-fill: gray;");
        this.selectedProperty().addListener((obs, wasSelected, isNowSelected) -> {
            lblInfo.setStyle(isNowSelected ? "-fx-text-fill: white;" : "-fx-text-fill: gray;");
        });
        lblNota.setStyle("-fx-text-fill: #006600; -fx-font-weight: bold;");

        vbox.getChildren().addAll(lblTitulo, lblInfo, lblNota);

        HBox.setHgrow(vbox, Priority.ALWAYS);
    }

    @Override
    protected void updateItem(MovieRecord filme, boolean empty) {
        super.updateItem(filme, empty);

        if (empty || filme == null) {
            setText(null);
            setGraphic(null);
        } else {
            lblTitulo.setText(filme.titulo() + " (" + filme.ano() + ")");
            lblInfo.setText("Diretor: " + filme.diretor() + " | Gêneros: " +
                    String.join(", ", filme.genero().stream().map(Object::toString).toList()));
            lblNota.setText("Nota: " + filme.nota() + " (" + filme.qtd_avaliacoes() + " avaliações)");
            setGraphic(vbox);
        }
    }
}
    
