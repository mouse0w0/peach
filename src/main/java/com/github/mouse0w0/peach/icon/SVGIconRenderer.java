package com.github.mouse0w0.peach.icon;

import com.github.mouse0w0.peach.Peach;
import javafx.scene.image.Image;

import java.net.URL;

public interface SVGIconRenderer {
    static SVGIconRenderer getInstance() {
        return Peach.getInstance().getService(SVGIconRenderer.class);
    }

    Image render(URL url);
}
