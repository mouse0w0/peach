package com.github.mouse0w0.peach.mcmod.ui;

import com.github.mouse0w0.peach.mcmod.GameData;
import com.github.mouse0w0.peach.mcmod.index.Index;
import javafx.util.StringConverter;

public class GameDataConverter extends StringConverter<String> {
    private final Index<String, GameData> index;

    public static GameDataConverter create(Index<String, GameData> index) {
        return new GameDataConverter(index);
    }

    private GameDataConverter(Index<String, GameData> index) {
        this.index = index;
    }

    @Override
    public String toString(String object) {
        GameData gameData = index.get(object);
        if (gameData != null) {
            return gameData.getName();
        } else {
            return object;
        }
    }

    @Override
    public String fromString(String string) {
        return null;
    }
}
