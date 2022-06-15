package com.github.mouse0w0.peach.mcmod.compiler.task;

import com.github.mouse0w0.peach.mcmod.compiler.Context;

public interface Task {
    void run(Context context) throws Exception;
}
