package com.github.mouse0w0.peach.mcmod.language;

import java.util.Map;

public interface Localizable {

    void getLocalizedText(String namespace, Map<String, String> localizedTexts);
}
