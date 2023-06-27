package com.github.mouse0w0.peach.icon;

import com.github.mouse0w0.peach.util.StringUtils;
import org.apache.batik.anim.dom.SAXSVGDocumentFactory;
import org.apache.batik.anim.dom.SVGOMDocument;
import org.apache.batik.bridge.BridgeContext;
import org.apache.batik.bridge.GVTBuilder;
import org.apache.batik.bridge.UserAgentAdapter;
import org.apache.batik.bridge.ViewBox;
import org.apache.batik.bridge.svg12.SVG12BridgeContext;
import org.apache.batik.ext.awt.RenderingHintsKeyExt;
import org.apache.batik.gvt.CanvasGraphicsNode;
import org.apache.batik.gvt.CompositeGraphicsNode;
import org.apache.batik.gvt.GraphicsNode;
import org.apache.batik.util.SVGConstants;
import org.apache.batik.util.XMLResourceDescriptor;
import org.w3c.dom.svg.SVGSVGElement;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Dimension2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

class SVGRasterizer {
    private static final AffineTransform IDENTITY_TRANSFORM = new AffineTransform();

    public static BufferedImage load(Path path, int width, int height) throws IOException {
        try (InputStream inputStream = Files.newInputStream(path)) {
            return load(path.toUri().toString(), inputStream, width, height);
        }
    }

    public static BufferedImage load(File file, int width, int height) throws IOException {
        try (InputStream inputStream = new FileInputStream(file)) {
            return load(file.toURI().toString(), inputStream, width, height);
        }
    }

    public static BufferedImage load(URL url, int width, int height) throws IOException {
        try (InputStream inputStream = url.openStream()) {
            return load(url.toExternalForm(), inputStream, width, height);
        }
    }

    public static BufferedImage load(String uri, InputStream inputStream, int width, int height) throws IOException {
        SAXSVGDocumentFactory factory = new SAXSVGDocumentFactory(XMLResourceDescriptor.getXMLParserClassName());
        SVGOMDocument document = (SVGOMDocument) factory.createSVGDocument(uri, inputStream);
        return render(document, width, height);
    }

    private static BufferedImage render(SVGOMDocument document, int width, int height) {
        MyUserAgent userAgent = new MyUserAgent();
        BridgeContext context = document.isSVG12() ? new SVG12BridgeContext(userAgent) : new BridgeContext(userAgent);
        GraphicsNode gvtRoot = new GVTBuilder().build(context, document);
        userAgent.setViewportSize(context.getDocumentSize(), width, height);
        userAgent.setTransform(computeTransform(document, context, gvtRoot, userAgent.width, userAgent.height));
        return render(gvtRoot, userAgent.width, userAgent.height);
    }

    private static BufferedImage render(GraphicsNode gvtRoot, int width, int height) {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = image.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
        g.setRenderingHint(RenderingHintsKeyExt.KEY_BUFFERED_IMAGE, new WeakReference<>(image));
        gvtRoot.paint(g);
        g.dispose();
        return image;
    }

    private static AffineTransform computeTransform(SVGOMDocument document, BridgeContext context, GraphicsNode gvtRoot, int width, int height) {
        AffineTransform result;
        SVGSVGElement root = document.getRootElement();
        String viewBox = root.getAttributeNS(null, SVGConstants.SVG_VIEW_BOX_ATTRIBUTE);
        if (StringUtils.isNotEmpty(viewBox)) {
            String preserveAspectRatio = root.getAttributeNS(null, SVGConstants.SVG_PRESERVE_ASPECT_RATIO_ATTRIBUTE);
            result = ViewBox.getPreserveAspectRatioTransform(root, viewBox, preserveAspectRatio, width, height, context);
        } else {
            Dimension2D documentSize = context.getDocumentSize();
            result = AffineTransform.getScaleInstance(width / documentSize.getWidth(), height / documentSize.getHeight());
        }

        if (gvtRoot instanceof CompositeGraphicsNode cgnRoot) {
            List<?> children = cgnRoot.getChildren();
            if (children.size() != 0 && children.get(0) instanceof CanvasGraphicsNode cgn) {
                cgn.setViewingTransform(result);
                return IDENTITY_TRANSFORM;
            }
        }
        return result;
    }

    private static class MyUserAgent extends UserAgentAdapter {
        private int width;
        private int height;
        private AffineTransform transform;

        @Override
        public Dimension2D getViewportSize() {
            return new Dimension(width, height);
        }

        public void setViewportSize(Dimension2D docSize, int requestedWidth, int requestedHeight) {
            width = requestedWidth > 0 ? requestedWidth : (int) (docSize.getWidth() + 0.5);
            height = requestedHeight > 0 ? requestedHeight : (int) (docSize.getHeight() + 0.5);
        }

        @Override
        public AffineTransform getTransform() {
            return transform;
        }

        @Override
        public void setTransform(AffineTransform transform) {
            this.transform = transform;
        }
    }
}
