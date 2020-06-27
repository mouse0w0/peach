package com.github.mouse0w0.peach;

import org.apache.commons.lang3.SystemUtils;

import java.nio.file.Path;
import java.nio.file.Paths;

public interface PeachDirectories {

    Path APPLICATION_USER_PROPERTIES = Paths.get(SystemUtils.USER_HOME, ".peach");
}
