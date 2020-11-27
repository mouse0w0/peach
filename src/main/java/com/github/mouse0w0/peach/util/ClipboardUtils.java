package com.github.mouse0w0.peach.util;


import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.TransferMode;
import org.apache.commons.lang3.SystemUtils;

import java.nio.ByteBuffer;

public final class ClipboardUtils {

    public static final DataFormat TRANSFER_MODE = new DataFormat("transfer-mode");

    /**
     * See <a href="https://docs.microsoft.com/zh-cn/windows/win32/com/dropeffect-constants">DROPEFFECT Constants</a>
     */
    public static final DataFormat WINDOWS_DROPEFFECT = new DataFormat("ms-stuff/preferred-drop-effect");

    /**
     * Transfer mode mask constants.
     */
    public static final int TRANSFER_MODE_NONE = 0;
    public static final int TRANSFER_MODE_COPY = 1;
    public static final int TRANSFER_MODE_MOVE = 2;
    public static final int TRANSFER_MODE_LINK = 4;

    public static ClipboardContent copyOf(Clipboard clipboard) {
        ClipboardContent content = new ClipboardContent();
        for (DataFormat dataFormat : clipboard.getContentTypes()) {
            content.put(dataFormat, clipboard.getContent(dataFormat));
        }
        return content;
    }

    public static int getTransferModeMask(Clipboard clipboard) {
        Object mask = clipboard.getContent(TRANSFER_MODE);
        if (mask != null) {
            return (int) mask;
        }

        if (SystemUtils.IS_OS_WINDOWS) {
            ByteBuffer content = (ByteBuffer) clipboard.getContent(WINDOWS_DROPEFFECT);
            return content != null ? content.getInt(0) : 0;
        } else {
            return 0;
        }
    }

    public static TransferMode[] getTransferMode(Clipboard clipboard) {
        switch (getTransferModeMask(clipboard)) {
            default:
            case 0:
                return TransferMode.NONE;
            case 1:
                return new TransferMode[]{TransferMode.COPY};
            case 2:
                return new TransferMode[]{TransferMode.MOVE};
            case 3:
                return TransferMode.COPY_OR_MOVE;
            case 4:
                return new TransferMode[]{TransferMode.LINK};
            case 5:
                return new TransferMode[]{TransferMode.COPY, TransferMode.LINK};
            case 6:
                return new TransferMode[]{TransferMode.MOVE, TransferMode.LINK};
            case 7:
                return TransferMode.ANY;
        }
    }

    public static boolean hasTransferMode(Clipboard clipboard, TransferMode mode) {
        final int mask = getTransferModeMask(clipboard);
        if (mode == TransferMode.COPY) return (mask & TRANSFER_MODE_COPY) != 0;
        else if (mode == TransferMode.MOVE) return (mask & TRANSFER_MODE_MOVE) != 0;
        else if (mode == TransferMode.LINK) return (mask & TRANSFER_MODE_LINK) != 0;
        else return mask == TRANSFER_MODE_NONE;
    }

    public static boolean hasTransferMode(Clipboard clipboard, int mask) {
        return (mask & getTransferModeMask(clipboard)) == mask;
    }

    private ClipboardUtils() {
    }
}
