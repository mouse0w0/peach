package com.github.mouse0w0.peach.icon;

import com.github.weisj.jsvg.SVGDocument;
import com.github.weisj.jsvg.geometry.size.FloatSize;
import com.github.weisj.jsvg.parser.SVGLoader;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.URL;

public final class SVGIconRendererImpl implements SVGIconRenderer {
    private final SVGLoader loader = new SVGLoader();

    @Override
    public Image render(URL url) {
        SVGDocument document = loader.load(url);
        if (document == null) {
            throw new RuntimeException("Cannot load svg document. url=" + url);
        }
        FloatSize size = document.size();
        int width = (int) size.width;
        int height = (int) size.height;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = image.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
        document.render(null, g);
        g.dispose();
        return SwingFXUtils.toFXImage(image, new WritableImage(width, height));
    }
}
