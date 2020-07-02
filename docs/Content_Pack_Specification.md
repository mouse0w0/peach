# 内容包规范

内容包用于导入额外内容，通常以**zip**压缩文件的形式包装。

## 文件结构
- **content.metadata.json**：用于描述内容包的元数据文件。
- **content**
    - **\<namespace\>**：命名空间。
        - **image**：图片文件文件夹。
            - **item**：物品的图片文件夹。
        - **lang**：语言文件文件夹。
        - **mod.metadata.json**：用于描述模组的元数据文件。
        - **item.json**：储存所有物品。
        - **oreDictionary.json**：储存所有矿物词典。