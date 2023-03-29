package com.github.mouse0w0.peach.action;

import com.github.mouse0w0.i18n.I18n;
import com.github.mouse0w0.peach.icon.IconManager;
import com.github.mouse0w0.peach.plugin.ActionDescriptor;
import com.github.mouse0w0.peach.plugin.Plugin;
import com.github.mouse0w0.peach.plugin.PluginManagerCore;
import com.github.mouse0w0.peach.util.StringUtils;
import com.github.mouse0w0.peach.util.Validate;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import javafx.event.Event;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Menu;
import org.dom4j.Element;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class ActionManagerImpl implements ActionManager {
    private static final Logger LOGGER = LoggerFactory.getLogger("Action");

    private static final String ACTION_ELEMENT_NAME = "action";
    private static final String GROUP_ELEMENT_NAME = "group";
    private static final String SEPARATOR_ELEMENT_NAME = "separator";
    private static final String REFERENCE_ELEMENT_NAME = "reference";

    private static final String ID_ATTR_NAME = "id";
    private static final String CLASS_ATTR_NAME = "class";
    private static final String TEXT_ATTR_NAME = "text";
    private static final String DESCRIPTION_ATTR_NAME = "description";
    private static final String ICON_ATTR_NAME = "icon";

//    private static final String ADD_TO_GROUP_ELEMENT_NAME = "add-to-group";
//    private static final String GROUP_ID_ATTR_NAME = "group-id";
//    private static final String ANCHOR_ATTR_NAME = "anchor";
//    private static final String RELATIVE_TO_ACTION_ATTR_NAME = "relative-to-action";

    private final BiMap<String, Action> actions = HashBiMap.create();

    public ActionManagerImpl() {
        loadActions();
    }

    @Override
    public String getActionId(Action action) {
        return actions.inverse().get(action);
    }

    @Override
    public Action getAction(String actionId) {
        return actions.get(actionId);
    }

    @Override
    public void perform(String actionId, Event event) {
        getAction(actionId).perform(new ActionEvent(event));
    }

    @Override
    public Menu createMenu(@NotNull ActionGroup group) {
        return new ActionMenu(group);
    }

    @Override
    public ContextMenu createContextMenu(@NotNull ActionGroup group) {
        return new ActionContextMenu(group);
    }

    @Override
    public Button createButton(@NotNull Action action) {
        Validate.notNull(action);
        if (action instanceof ActionGroup || action instanceof Separator) {
            throw new IllegalArgumentException("action");
        }

        Button button = new Button();
        button.setText(action.getText());
        button.setGraphic(IconManager.getInstance().createNode(action.getIcon()));
        button.setOnAction(event -> action.perform(new ActionEvent(event)));
        return button;
    }

    private void loadActions() {
        for (Plugin plugin : PluginManagerCore.getEnabledPlugins()) {
            for (ActionDescriptor action : plugin.getActions()) {
                processElement(plugin, action.getElement(), null);
            }
        }
    }

    private void processElement(Plugin plugin, Element element, ActionGroup parent) {
        String name = element.getName();
        if (ACTION_ELEMENT_NAME.equals(name)) {
            processActionElement(plugin, element, parent);
        } else if (GROUP_ELEMENT_NAME.equals(name)) {
            processGroupElement(plugin, element, parent);
        } else if (SEPARATOR_ELEMENT_NAME.equals(name)) {
            processSeparatorElement(plugin, element, parent);
        } else if (REFERENCE_ELEMENT_NAME.equals(name)) {
            processReferenceElement(plugin, element, parent);
        } else {
            LOGGER.error("Unknown element `{}`, plugin={}", name, plugin.getId());
        }
    }

    private void processActionElement(Plugin plugin, Element element, ActionGroup parent) {
        String id = element.attributeValue(ID_ATTR_NAME);
        if (StringUtils.isEmpty(id)) {
            LOGGER.error("Missing action id, plugin={}", plugin.getId());
            return;
        }

        String className = element.attributeValue(CLASS_ATTR_NAME);
        if (StringUtils.isEmpty(className)) {
            LOGGER.error("Missing action class, id={}, plugin={}", id, plugin.getId());
            return;
        }

        Action action;
        try {
            Class<?> clazz = plugin.getClassLoader().loadClass(className);
            if (!Action.class.isAssignableFrom(clazz)) {
                LOGGER.error("{} is not subclass of Action, id={}, plugin={}", className, id, plugin.getId());
                return;
            }
            action = (Action) clazz.getConstructor().newInstance();
        } catch (ReflectiveOperationException e) {
            LOGGER.error("Cannot create action, id=" + id + ", class=" + className + ", plugin=" + plugin.getId(), e);
            return;
        }

        processAttribute(plugin, element, id, action);

        if (registerAction(plugin, id, action)) {
            if (parent != null) {
                parent.addChild(action);
            }
        }
    }

    private void processGroupElement(Plugin plugin, Element element, ActionGroup parent) {
        String id = element.attributeValue(ID_ATTR_NAME);
        if (StringUtils.isEmpty(id)) {
            LOGGER.error("Missing group id, plugin={}", plugin.getId());
            return;
        }

        String className = element.attributeValue(CLASS_ATTR_NAME);

        ActionGroup group;
        try {
            Class<?> clazz = className != null ? plugin.getClassLoader().loadClass(className) : ActionGroup.class;
            if (!ActionGroup.class.isAssignableFrom(clazz)) {
                LOGGER.error("{} is not subclass of ActionGroup, id={}, plugin={}", className, id, plugin.getId());
                return;
            }
            group = (ActionGroup) clazz.getConstructor().newInstance();
        } catch (ReflectiveOperationException e) {
            LOGGER.error("Cannot create group, id=" + id + ", class=" + className + ", plugin=" + plugin.getId(), e);
            return;
        }

        processAttribute(plugin, element, id, group);

        if (registerAction(plugin, id, group)) {
            if (parent != null) {
                parent.addChild(group);
            }

            for (Element child : element.elements()) {
                processElement(plugin, child, group);
            }
        }
    }

    private void processSeparatorElement(Plugin plugin, Element element, ActionGroup parent) {
        if (parent != null) {
            parent.addChild(Separator.getInstance());
        }
    }

    private void processReferenceElement(Plugin plugin, Element element, ActionGroup parent) {
        String id = element.attributeValue(ID_ATTR_NAME);
        if (StringUtils.isEmpty(id)) {
            LOGGER.error("Missing reference id, plugin={}", plugin.getId());
            return;
        }

        Action action = getAction(id);
        if (action == null) {
            LOGGER.error("Not found reference action, id={}, plugin={}", id, plugin.getId());
            return;
        }

        if (parent != null) {
            parent.addChild(action);
        }
    }

    private void processAttribute(Plugin plugin, Element element, String id, Action action) {
        String text = localize(element, id, TEXT_ATTR_NAME);
        if (text != null) action.setText(text);
        String icon = element.attributeValue(ICON_ATTR_NAME);
        if (icon != null) action.setIcon(icon);
        String description = localize(element, id, DESCRIPTION_ATTR_NAME);
        if (description != null) action.setDescription(description);
    }

    private boolean registerAction(Plugin plugin, String id, Action action) {
        if (actions.putIfAbsent(id, action) != null) {
            LOGGER.error("Action has been registered, id={}, plugin={}", id, plugin.getId());
            return false;
        }
        return true;
    }

    private String localize(Element element, String id, String attrName) {
        return I18n.translate(element.getName() + "." + id + "." + attrName, element.attributeValue(attrName));
    }
}
