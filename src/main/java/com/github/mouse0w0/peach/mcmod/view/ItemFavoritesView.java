package com.github.mouse0w0.peach.mcmod.view;

import com.github.mouse0w0.gridview.GridView;
import com.github.mouse0w0.gridview.cell.GridCell;
import com.github.mouse0w0.peach.mcmod.IdMetadata;
import com.github.mouse0w0.peach.mcmod.ui.control.ItemView;
import com.github.mouse0w0.peach.project.Project;
import com.github.mouse0w0.peach.service.PersistentService;
import com.github.mouse0w0.peach.service.Storage;
import com.github.mouse0w0.peach.util.JsonUtils;
import com.github.mouse0w0.peach.view.ViewFactory;
import com.google.gson.JsonElement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.input.TransferMode;
import org.apache.commons.lang3.reflect.TypeUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Storage("itemFavorites.json")
public class ItemFavoritesView implements PersistentService {

    private static ItemFavoritesView getInstance(Project project) {
        return project.getService(ItemFavoritesView.class);
    }

    private final ObservableList<IdMetadata> items = FXCollections.observableArrayList();
    private final Set<IdMetadata> itemSet = new HashSet<>();

    private GridView<IdMetadata> content;

    public Node initViewContent() {
        content = new GridView<>();
        content.setId("item-favorites");
        content.setCellSize(32, 32);
        content.setCellSpacing(0, 0);
        content.setCellFactory(view -> new Cell());
        content.setItems(items);
        content.setOnDragOver(event -> {
            event.consume();
            if (event.getGestureSource() == event.getTarget()) return;

            IdMetadata item = (IdMetadata) event.getDragboard().getContent(ItemView.ITEM);
            if (item == null) return;

            event.acceptTransferModes(TransferMode.LINK);
        });
        content.setOnDragDropped(event -> {
            event.consume();
            IdMetadata item = (IdMetadata) event.getDragboard().getContent(ItemView.ITEM);
            if (itemSet.add(item)) {
                items.add(item);
            }
            event.setDropCompleted(true);
        });
        return content;
    }

    @Override
    public JsonElement saveState() {
        return JsonUtils.toJson(items);
    }

    @Override
    public void loadState(JsonElement state) {
        items.addAll(JsonUtils.<List<IdMetadata>>fromJson(state, TypeUtils.parameterize(List.class, IdMetadata.class)));
        itemSet.addAll(items);
    }

    private static class Cell extends GridCell<IdMetadata> {
        private final ItemView itemView = new ItemView(32);

        public Cell() {
            setGraphic(itemView);
        }

        @Override
        protected void updateItem(IdMetadata item, boolean empty) {
            super.updateItem(item, empty);
            if (empty) {
                itemView.setItem(null);
            } else {
                itemView.setItem(item);
            }
        }
    }

    public static class Factory implements ViewFactory {
        @Override
        public Node createViewContent(Project project) {
            return ItemFavoritesView.getInstance(project).initViewContent();
        }
    }
}
