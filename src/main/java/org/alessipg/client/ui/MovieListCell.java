package org.alessipg.client.ui;

import org.alessipg.shared.dto.util.MovieRecord;

import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class MovieListCell extends ListCell<MovieRecord> {
    private VBox vbox = new VBox(5);
    private Label lblTitle = new Label();
    private Label lblInfo = new Label();
    private Label lblScore = new Label();
    private Label lblSynopsis = new Label();

    public MovieListCell() {
        lblTitle.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        lblInfo.setStyle("-fx-text-fill: gray;");
        this.selectedProperty().addListener((obs, wasSelected, isNowSelected) -> {
            lblInfo.setStyle(isNowSelected ? "-fx-text-fill: white;" : "-fx-text-fill: gray;");
            lblSynopsis.setStyle(isNowSelected ? "-fx-text-fill: white; -fx-font-style: italic;" : "-fx-text-fill: gray; -fx-font-style: italic;");
        });
        lblScore.setStyle("-fx-text-fill: #006600; -fx-font-weight: bold;");
        lblSynopsis.setStyle("-fx-text-fill: gray; -fx-font-style: italic;");
        lblSynopsis.setWrapText(true);

        vbox.getChildren().addAll(lblTitle, lblInfo, lblScore, lblSynopsis);

        HBox.setHgrow(vbox, Priority.ALWAYS);
    }

    @Override
    protected void updateItem(MovieRecord filme, boolean empty) {
        super.updateItem(filme, empty);

        if (empty || filme == null) {
            setText(null);
            setGraphic(null);
        } else {
            lblTitle.setText(filme.titulo() + " (" + filme.ano() + ")");
            lblInfo.setText("Diretor: " + filme.diretor() + " | Gêneros: " +
                    String.join(", ", filme.genero().stream().map(Object::toString).toList()));
            lblScore.setText("Nota: " + filme.nota() + " (" + filme.qtd_avaliacoes() + " avaliações)");
            lblSynopsis.setText(filme.sinopse() != null ? filme.sinopse() : "");
            setGraphic(vbox);
        }
    }
}
    
