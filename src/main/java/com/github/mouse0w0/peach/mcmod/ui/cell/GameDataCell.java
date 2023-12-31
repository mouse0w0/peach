package com.github.mouse0w0.peach.mcmod.ui.cell;

import com.github.mouse0w0.peach.mcmod.GameData;
import com.github.mouse0w0.peach.mcmod.index.Index;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

public class GameDataCell extends ListCell<String> {
    private final Index<String, GameData> index;

    public static Callback<ListView<String>, ListCell<String>> factory(Index<String, GameData> index) {
        return $ -> new GameDataCell(index);
    }

    public static ListCell<String> create(Index<String, GameData> index) {
        return new GameDataCell(index);
    }

    private GameDataCell(Index<String, GameData> index) {
        this.index = index;
    }

    @Override
    protected void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);
        if (empty) {
            setText(null);
        } else {
            GameData gameData = index.get(item);
            if (gameData != null) {
                setText(gameData.getName());
            } else {
                setText(item);
            }
        }
    }
}
