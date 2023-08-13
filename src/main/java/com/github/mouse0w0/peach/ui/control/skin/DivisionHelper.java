package com.github.mouse0w0.peach.ui.control.skin;

import com.github.mouse0w0.peach.ui.control.DividerType;
import com.github.mouse0w0.peach.ui.control.EightPos;
import javafx.geometry.Orientation;
import javafx.geometry.Side;

final class DivisionHelper {
    public static final class Container {
        ViewPaneSkin.Container peer;

        public boolean isManaged() {
            return peer != null && peer.isManaged();
        }

        public double minWidth() {
            return isManaged() ? peer.minWidth(-1) : 0;
        }

        public double minHeight() {
            return isManaged() ? peer.minHeight(-1) : 0;
        }

        public void layoutInArea(double x, double y, double width, double height) {
            if (isManaged()) {
                peer.resizeRelocate(x, y, width, height);
            }
        }
    }

    public static final class Divider {
        ViewPaneSkin.Divider peer;

        private final DividerType type;

        public Divider(DividerType type) {
            this.type = type;
        }

        public boolean isManaged() {
            return peer != null && peer.isManaged();
        }

        public DividerType getType() {
            return type;
        }

        public boolean isVertical() {
            return type.isVertical();
        }

        public double getDividerWidth() {
            return isManaged() ? peer.getDividerWidth() : 0;
        }

        public double getPosition() {
            if (isManaged()) {
                return peer.getPosition();
            }
            return type.getSide() == Side.TOP || type.getSide() == Side.LEFT ? 0 : 1;
        }

        public void validDividerPosition(double size, double min, double max) {
            if (isManaged()) {
                peer.validDividerPosition(size, min, max);
            }
        }

        public void layoutInArea(double x, double y, double width, double height) {
            if (isManaged()) {
                peer.resizeRelocate(x, y, width, height);
            }
        }
    }

    private static final class VerticalSplitter {
        private final Splitter top;
        private final Divider topDivider;
        private final HorizontalSplitter center;
        private final Divider bottomDivider;
        private final Splitter bottom;

        public VerticalSplitter(Splitter top, Divider topDivider, HorizontalSplitter center, Divider bottomDivider, Splitter bottom) {
            this.top = top;
            this.topDivider = topDivider;
            this.center = center;
            this.bottomDivider = bottomDivider;
            this.bottom = bottom;
        }

        public void layoutChildren(double x, double y, double width, double height, boolean snapToPixel) {
            double topDividerWidth = topDivider.getDividerWidth();
            double bottomDividerWidth = bottomDivider.getDividerWidth();

            double halfTopDividerWidth = topDividerWidth / 2;
            double halfBottomDividerWidth = bottomDividerWidth / 2;

            double centerDelta = (halfTopDividerWidth + center.minHeight() + halfBottomDividerWidth) / width;
            topDivider.validDividerPosition(height,
                    (top.minHeight() + halfTopDividerWidth) / height,
                    bottomDivider.getPosition() - centerDelta);
            bottomDivider.validDividerPosition(height,
                    topDivider.getPosition() + centerDelta,
                    1 - (bottom.minHeight() + halfBottomDividerWidth) / height);

            double topDividerY = height * topDivider.getPosition();
            double topHeight = snapToPixel(topDividerY - halfTopDividerWidth, true);

            double bottomDividerY = height * bottomDivider.getPosition();
            double bottomHeight = snapToPixel(height - bottomDividerY - halfBottomDividerWidth, true);

            double centerHeight = height - topHeight - topDividerWidth - bottomHeight - bottomDividerWidth;

            top.layoutChildren(x, y, width, topHeight, snapToPixel);
            y += topHeight;
            topDivider.layoutInArea(x, y, width, topDividerWidth);
            y += topDividerWidth;
            center.layoutChildren(x, y, width, centerHeight, snapToPixel);
            y += centerHeight;
            bottomDivider.layoutInArea(x, y, width, bottomDividerWidth);
            y += bottomDividerWidth;
            bottom.layoutChildren(x, y, width, bottomHeight, snapToPixel);
        }
    }

    private static final class HorizontalSplitter {
        private final Splitter left;
        private final Divider leftDivider;
        private final Container center;
        private final Divider rightDivider;
        private final Splitter right;

        public HorizontalSplitter(Splitter left, Divider leftDivider, Container center, Divider rightDivider, Splitter right) {
            this.left = left;
            this.leftDivider = leftDivider;
            this.center = center;
            this.rightDivider = rightDivider;
            this.right = right;
        }

        public double minHeight() {
            return max(left.minHeight(), center.minHeight(), right.minHeight());
        }

        public void layoutChildren(double x, double y, double width, double height, boolean snapToPixel) {
            double leftDividerWidth = leftDivider.getDividerWidth();
            double rightDividerWidth = rightDivider.getDividerWidth();

            double halfLeftDividerWidth = leftDividerWidth / 2;
            double halfRightDividerWidth = rightDividerWidth / 2;

            double centerDelta = (halfLeftDividerWidth + center.minWidth() + halfRightDividerWidth) / width;
            leftDivider.validDividerPosition(width,
                    (left.minWidth() + halfLeftDividerWidth) / width,
                    rightDivider.getPosition() - centerDelta);
            rightDivider.validDividerPosition(width,
                    leftDivider.getPosition() + centerDelta,
                    1 - (right.minWidth() + halfRightDividerWidth) / width);

            double leftDividerX = width * leftDivider.getPosition();
            double leftWidth = snapToPixel(leftDividerX - halfLeftDividerWidth, true);

            double rightDividerX = width * rightDivider.getPosition();
            double rightWidth = snapToPixel(width - rightDividerX - halfRightDividerWidth, true);

            double centerWidth = width - leftWidth - leftDividerWidth - rightWidth - rightDividerWidth;

            left.layoutChildren(x, y, leftWidth, height, snapToPixel);
            x += leftWidth;
            leftDivider.layoutInArea(x, y, leftDividerWidth, height);
            x += leftDividerWidth;
            center.layoutInArea(x, y, centerWidth, height);
            x += centerWidth;
            rightDivider.layoutInArea(x, y, rightDividerWidth, height);
            x += rightDividerWidth;
            right.layoutChildren(x, y, rightWidth, height, snapToPixel);
        }
    }

    private static final class Splitter {
        private final Container leftTop;
        private final Divider divider;
        private final Container rightBottom;
        private final Orientation orientation;

        public Splitter(Container leftTop, Divider divider, Container rightBottom, Orientation orientation) {
            this.leftTop = leftTop;
            this.divider = divider;
            this.rightBottom = rightBottom;
            this.orientation = orientation;
        }

        public double minWidth() {
            return leftTop.minWidth() + divider.getDividerWidth() + rightBottom.minWidth();
        }

        public double minHeight() {
            return leftTop.minWidth() + divider.getDividerWidth() + rightBottom.minWidth();
        }

        public void layoutChildren(double x, double y, double width, double height, boolean snapToPixel) {
            if (leftTop.isManaged() && rightBottom.isManaged()) {
                double dividerWidth = divider.getDividerWidth();
                double halfDividerWidth = dividerWidth / 2;
                if (orientation == Orientation.HORIZONTAL) {
                    divider.validDividerPosition(width,
                            (leftTop.minWidth() + halfDividerWidth) / width,
                            1 - (rightBottom.minWidth() + halfDividerWidth) / width);

                    double dividerX = width * divider.getPosition();
                    double leftWidth = snapToPixel(dividerX - halfDividerWidth, snapToPixel);
                    leftTop.layoutInArea(x, y, leftWidth, height);
                    divider.layoutInArea(x + leftWidth, y, dividerWidth, height);
                    rightBottom.layoutInArea(x + leftWidth + dividerWidth, y, width - leftWidth - dividerWidth, height);
                } else {
                    divider.validDividerPosition(height,
                            (leftTop.minWidth() + halfDividerWidth) / height,
                            1 - (rightBottom.minWidth() + halfDividerWidth) / height);

                    double dividerY = height * divider.getPosition();
                    double topHeight = snapToPixel(dividerY - halfDividerWidth, snapToPixel);
                    leftTop.layoutInArea(x, y, width, topHeight);
                    divider.layoutInArea(x, y + topHeight, width, dividerWidth);
                    rightBottom.layoutInArea(x, y + topHeight + dividerWidth, width, height - topHeight - dividerWidth);
                }
            } else {
                leftTop.layoutInArea(x, y, width, height);
                rightBottom.layoutInArea(x, y, width, height);
            }
        }
    }

    private final VerticalSplitter root;

    final Container[] containers = new Container[8];
    final Container center = new Container();
    final Divider[] dividers = new Divider[8];

    public DivisionHelper() {
        Splitter top = new Splitter(
                createNode(EightPos.TOP_LEFT),
                createDivider(DividerType.TOP_SECONDARY),
                createNode(EightPos.TOP_RIGHT),
                Orientation.HORIZONTAL);

        Splitter left = new Splitter(
                createNode(EightPos.LEFT_TOP),
                createDivider(DividerType.LEFT_SECONDARY),
                createNode(EightPos.LEFT_BOTTOM),
                Orientation.VERTICAL);

        Splitter bottom = new Splitter(
                createNode(EightPos.BOTTOM_LEFT),
                createDivider(DividerType.BOTTOM_SECONDARY),
                createNode(EightPos.BOTTOM_RIGHT),
                Orientation.HORIZONTAL);

        Splitter right = new Splitter(
                createNode(EightPos.RIGHT_TOP),
                createDivider(DividerType.RIGHT_SECONDARY),
                createNode(EightPos.RIGHT_BOTTOM),
                Orientation.VERTICAL);

        HorizontalSplitter horizontal = new HorizontalSplitter(
                left,
                createDivider(DividerType.LEFT_PRIMARY),
                center,
                createDivider(DividerType.RIGHT_PRIMARY),
                right);

        VerticalSplitter vertical = new VerticalSplitter(
                top,
                createDivider(DividerType.TOP_PRIMARY),
                horizontal,
                createDivider(DividerType.BOTTOM_PRIMARY),
                bottom);

        root = vertical;
    }

    private Container createNode(EightPos pos) {
        return containers[pos.ordinal()] = new Container();
    }

    private Divider createDivider(DividerType type) {
        return dividers[type.ordinal()] = new Divider(type);
    }

    public void layout(double x, double y, double width, double height, boolean snapToPixel) {
        root.layoutChildren(x, y, width, height, snapToPixel);
    }

    private static double snapToPixel(double value, boolean snapToPixel) {
        return snapToPixel ? Math.ceil(value) : value;
    }

    private static double max(double a, double b, double c) {
        return Math.max(Math.max(a, b), c);
    }
}
