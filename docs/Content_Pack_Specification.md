# 内容包规范

内容包用于导入额外内容，通常以**zip**压缩文件的形式包装。

## 文件结构
- **content.metadata.json**：用于描述内容包的元数据文件。
- **content**
    - **\<namespace\>**：命名空间。
        - **image**：图片文件文件夹。
            - **item**：物品的图片文件夹。
        - **lang**：语言文件文件夹。
        - **item.json**：物品数据。
        - **itemGroup.json**：创造标签数据。
        - **oreDictionary.json**：矿物词典数据。
        - **sound.json**：声音数据。
        - **enchantment.json**：附魔数据。