package com.github.mouse0w0.peach.forge.element;

public class Element {
    private String id;
    private String translationKey;
    private Class<?> type;

    public String getId() {
        return id;
    }

    public String getTranslationKey() {
        return translationKey;
    }

    public Class<?> getType() {
        return type;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private String id;
        private String translationKey;
        private Class<?> type;

        private Builder() {
        }

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder translationKey(String translationKey) {
            this.translationKey = translationKey;
            return this;
        }

        public Builder type(Class<?> type) {
            this.type = type;
            return this;
        }

        public Element build() {
            Element element = new Element();
            element.id = this.id;
            element.translationKey = this.translationKey;
            element.type = this.type;
            return element;
        }
    }
}
