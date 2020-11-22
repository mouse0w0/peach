package com.github.mouse0w0.peach.data;

import com.github.mouse0w0.peach.project.Project;

import java.util.List;

public interface DataKeys {
    DataKey<Project> PROJECT = DataKey.create("Project");

    DataKey<Object> SELECTED_ITEM = DataKey.create("SelectedItem");
    DataKey<List<?>> SELECTED_ITEMS = DataKey.create("SelectedItems");
}
