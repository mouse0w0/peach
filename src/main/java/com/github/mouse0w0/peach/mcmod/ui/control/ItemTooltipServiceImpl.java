package com.github.mouse0w0.peach.mcmod.ui.control;

import com.github.mouse0w0.peach.mcmod.IdMetadata;
import com.github.mouse0w0.peach.mcmod.Identifier;
import com.github.mouse0w0.peach.mcmod.ItemData;
import com.github.mouse0w0.peach.mcmod.index.Index;
import com.github.mouse0w0.peach.mcmod.index.IndexKeys;
import com.github.mouse0w0.peach.mcmod.index.IndexManager;
import com.github.mouse0w0.peach.mcmod.project.ModProjectService;
import com.github.mouse0w0.peach.project.Project;
import javafx.css.Styleable;
import javafx.scene.control.Tooltip;

import java.util.List;

public class ItemTooltipServiceImpl implements ItemTooltipService {
    private final Tooltip tooltip;

    public ItemTooltipServiceImpl(Project project) {
        ModProjectService modProjectService = ModProjectService.getInstance(project);
        Index<IdMetadata, List<ItemData>> itemIndex = IndexManager.getInstance(project).getIndex(IndexKeys.ITEM);

        tooltip = new Tooltip();
        tooltip.setOnShowing(event -> {
            Styleable parent = tooltip.getStyleableParent();
            if (parent == null) return;

            ItemView itemView = (ItemView) parent;
            IdMetadata idMetadata = itemView.getItem();

            StringBuilder sb = new StringBuilder();
            Identifier id = idMetadata.getId();
            if (!idMetadata.isOreDictionary()) {
                if (id.isProjectNamespace()) {
                    sb.append(modProjectService.getModId());
                } else {
                    sb.append(id.getNamespace());
                }
                sb.append(':');
            }
            sb.append(id.getPath());

            if (idMetadata.isNormal()) {
                sb.append('#').append(idMetadata.getMetadata());
            }

            sb.append("\n--------------------");

            List<ItemData> itemDataList = itemIndex.get(idMetadata);
            if (itemDataList != null) {
                for (ItemData itemData : itemDataList) {
                    sb.append('\n').append(itemData.getName());
                }
            }

            tooltip.setText(sb.toString());
        });
    }

    @Override
    public void install(ItemView itemView) {
        Tooltip.install(itemView, tooltip);
    }

    @Override
    public void uninstall(ItemView itemView) {
        Tooltip.uninstall(itemView, tooltip);
    }
}
