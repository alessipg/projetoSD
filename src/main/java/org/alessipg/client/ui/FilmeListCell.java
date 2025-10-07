package org.alessipg.client.ui;

import org.alessipg.shared.domain.model.Movie;

import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class FilmeListCell extends ListCell<Movie> {
    private VBox vbox = new VBox(5);
    private Label lblTitulo = new Label();
    private Label lblInfo = new Label();
    private Label lblNota = new Label();

    public FilmeListCell() {
        lblTitulo.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        lblInfo.setStyle("-fx-text-fill: gray;");
        lblNota.setStyle("-fx-text-fill: #006600; -fx-font-weight: bold;");

        vbox.getChildren().addAll(lblTitulo, lblInfo, lblNota);

        HBox.setHgrow(vbox, Priority.ALWAYS);
    }

    @Override
    protected void updateItem(Movie filme, boolean empty) {
        super.updateItem(filme, empty);

        if (empty || filme == null) {
            setText(null);
            setGraphic(null);
        } else {
            lblTitulo.setText(filme.getTitle() + " (" + filme.getYear() + ")");
            lblInfo.setText("Diretor: " + filme.getDirector() + " | Gêneros: " +
                String.join(", ", filme.getGenres().stream().map(Object::toString).toList()));
            lblNota.setText("Nota: " + filme.getScore() + " (" + filme.getRatingCount() + " avaliações)");
            setGraphic(vbox);
        }
    }
}
    
