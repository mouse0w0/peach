package com.github.mouse0w0.peach.ui.control;

import com.sun.javafx.stage.PopupWindowHelper;

public class PopupControl extends javafx.scene.control.PopupControl {
    public PopupControl() {
        bridge = new CSSBridge();
        PopupWindowHelper.getContent(this).setAll(bridge);
    }

    public String getUserAgentStylesheet() {
        return null;
    }

    public class CSSBridge extends javafx.scene.control.PopupControl.CSSBridge {
        @Override
        public String getUserAgentStylesheet() {
            return PopupControl.this.getUserAgentStylesheet();
        }
    }
}
