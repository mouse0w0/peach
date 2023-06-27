package com.github.mouse0w0.peach.icon;

public interface Icons {

    Icon Peach = IconManager.getInstance().getIcon("Peach");

    interface File {
        Icon Folder = getIcon("File.Folder");

        Icon File = getIcon("File.File");

        Icon Image = getIcon("File.Image");

        Icon Sound = getIcon("File.Sound");

        Icon Json = getIcon("File.Json");

        Icon Forge = getIcon("File.Forge");

        Icon ModElement = getIcon("File.ModElement");
    }

    static Icon getIcon(String name) {
        return IconManager.getInstance().getIcon(name);
    }
}
