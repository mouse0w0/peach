package com.github.mouse0w0.peach.icon;

import javafx.scene.image.Image;

import java.net.URL;

public interface Icons {

    Image Peach_16x = load("/icon/peach-16x.png");

    interface Action {
        Image NewProject = load("/icon/plus-thick.png");

        Image OpenProject = load("/icon/folder-open-outline.png");

        Image ProjectSettings = load("/icon/tune.png");

        Image Export = load("/icon/export.png");

        Image Donate = load("/icon/currency-usd-circle-outline.png");
    }

    interface View {
        Image Project = load("/icon/file-cabinet.png");

        Image ItemFavorites = load("/icon/bookmark.png");
    }

    interface File {
        Image Folder = load("/icon/file/folder.png");

        Image File = load("/icon/file/file.png");

        Image Json = load("/icon/file/code-json.png");

        Image Sound = load("/icon/file/file-music.png");

        Image Image = load("/icon/file/image.png");

        Image Forge = load("/icon/file/forge.png");

        Image ModElement = load("/icon/file/alpha-e-box.png");
    }

    static Image load(String name) {
        URL resource = Icons.class.getResource(name);
        if (resource == null) {
            throw new NullPointerException("Not found resource: " + name);
        }
        return new Image(resource.toExternalForm());
    }
}
