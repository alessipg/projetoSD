package org.alessipg.client.ui;

import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.alessipg.shared.dto.util.ReviewRecord;

public class ReviewListCell extends ListCell<ReviewRecord> {
    private VBox vbox = new VBox(5);
    private Label lblTitulo = new Label();
    private Label lblInfo = new Label();
    private Label lblNota = new Label();

    public ReviewListCell() {
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
    protected void updateItem(ReviewRecord review, boolean empty) {
        super.updateItem(review, empty);

        if (empty || review == null) {
            setText(null);
            setGraphic(null);
        } else {
//            lblTitulo.setText(review.titulo() + " (" + review.ano() + ")");
//            lblInfo.setText("Diretor: " + review.diretor() + " | Gêneros: " +
//                    String.join(", ", review.genero().stream().map(Object::toString).toList()));
//            lblNota.setText("Nota: " + review.nota() + " (" + review.qtd_avaliacoes() + " avaliações)");
//            setGraphic(vbox);
        }
    }
}

