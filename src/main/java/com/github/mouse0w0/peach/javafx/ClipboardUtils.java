package com.github.mouse0w0.peach.javafx;


import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import org.apache.commons.lang3.SystemUtils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

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
    public static final int TRANSFER_MODE_COPY_OR_MOVE = 3;
    public static final int TRANSFER_MODE_LINK = 4;
    public static final int TRANSFER_MODE_COPY_OR_LINK = 5;
    public static final int TRANSFER_MODE_MOVE_OR_LINK = 6;
    public static final int TRANSFER_MODE_ANY = 7;

    public static ClipboardContent copyOf(Clipboard clipboard) {
        ClipboardContent content = new ClipboardContent();
        for (DataFormat dataFormat : clipboard.getContentTypes()) {
            content.put(dataFormat, clipboard.getContent(dataFormat));
        }
        return content;
    }

    public static int getTransferMode(Clipboard clipboard) {
        Object mode = clipboard.getContent(TRANSFER_MODE);
        if (mode != null) {
            return (int) mode;
        }

        if (SystemUtils.IS_OS_WINDOWS) {
            ByteBuffer buffer = (ByteBuffer) clipboard.getContent(WINDOWS_DROPEFFECT);
            return buffer != null ? buffer.order(ByteOrder.LITTLE_ENDIAN).getInt(0) : 0;
        }

        return 0;
    }

    public static boolean hasTransferMode(Clipboard clipboard, int mode) {
        return (mode & getTransferMode(clipboard)) != 0;
    }

    public static void setTransferMode(ClipboardContent content, int mode) {
        content.put(TRANSFER_MODE, mode);

        if (SystemUtils.IS_OS_WINDOWS) {
            content.put(WINDOWS_DROPEFFECT, ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(0, mode));
        }
    }

    private ClipboardUtils() {
    }
}
