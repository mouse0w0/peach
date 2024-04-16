package com.github.mouse0w0.peach.mcmod.ui;

import com.github.mouse0w0.peach.mcmod.GameData;
import com.github.mouse0w0.peach.mcmod.index.Index;
import javafx.util.StringConverter;

public class GameDataConverter<T> extends StringConverter<T> {
    private final Index<T, GameData> index;

    public static <T> GameDataConverter<T> create(Index<T, GameData> index) {
        return new GameDataConverter<>(index);
    }

    private GameDataConverter(Index<T, GameData> index) {
        this.index = index;
    }

    @Override
    public String toString(T object) {
        GameData gameData = index.get(object);
        if (gameData != null) {
            return gameData.getName();
        } else {
            return object.toString();
        }
    }

    @Override
    public T fromString(String string) {
        throw new UnsupportedOperationException();
    }
}
