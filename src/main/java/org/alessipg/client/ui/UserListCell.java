package org.alessipg.client.ui;

import javafx.scene.control.ListCell;
import org.alessipg.shared.dto.util.UserRecord;
import org.alessipg.shared.dto.util.UserView;

public class UserListCell extends ListCell<UserView> {
    @Override
    protected void updateItem(UserView item, boolean empty) {
        super.updateItem(item, empty);
        if (empty || item == null) {
            setText(null);
        } else {
            setText("ID: " + item.id() + " | Nome: " + item.nome());
        }
    }
}
