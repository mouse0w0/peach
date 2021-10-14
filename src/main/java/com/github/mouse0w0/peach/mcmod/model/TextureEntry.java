package com.github.mouse0w0.peach.mcmod.model;

public class TextureEntry {
    private String key;
    private int column;
    private int row;
    private String translationKey;

    public TextureEntry(String key) {
        this.key = key;
    }

    public TextureEntry(String key, int column, int row, String translationKey) {
        this.key = key;
        this.column = column;
        this.row = row;
        this.translationKey = translationKey;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public String getTranslationKey() {
        return translationKey;
    }

    public void setTranslationKey(String translationKey) {
        this.translationKey = translationKey;
    }
}
