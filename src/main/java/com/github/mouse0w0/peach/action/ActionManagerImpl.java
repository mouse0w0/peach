package com.github.mouse0w0.peach.action;

import com.github.mouse0w0.peach.data.DataManager;
import com.github.mouse0w0.peach.icon.IconManager;
import com.github.mouse0w0.peach.l10n.L10n;
import com.github.mouse0w0.peach.plugin.ActionDescriptor;
import com.github.mouse0w0.peach.plugin.Plugin;
import com.github.mouse0w0.peach.plugin.PluginManagerCore;
import com.github.mouse0w0.peach.util.StringUtils;
import com.github.mouse0w0.peach.util.Validate;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Menu;
import org.dom4j.Element;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class ActionManagerImpl implements ActionManager {
    private static final Logger LOGGER = LoggerFactory.getLogger("Action");

    private static final String ACTION_ELEMENT_NAME = "action";
    private static final String GROUP_ELEMENT_NAME = "group";
    private static final String SEPARATOR_ELEMENT_NAME = "separator";
    private static final String REFERENCE_ELEMENT_NAME = "reference";
    private static final String ADD_TO_GROUP_ELEMENT_NAME = "add-to-group";

    private static final String ID_ATTR_NAME = "id";
    private static final String CLASS_ATTR_NAME = "class";
    private static final String TEXT_ATTR_NAME = "text";
    private static final String DESCRIPTION_ATTR_NAME = "description";
    private static final String ICON_ATTR_NAME = "icon";
    private static final String GROUP_ID_ATTR_NAME = "group-id";
    private static final String ANCHOR_ATTR_NAME = "anchor";

    private final BiMap<String, Action> actions = HashBiMap.create();

    public ActionManagerImpl() {
        loadActions();
    }

    @Override
    public @Nullable String getActionId(@NotNull Action action) {
        return actions.inverse().get(action);
    }

    @Override
    public @Nullable Action getAction(@NotNull String actionId) {
        return actions.get(actionId);
    }

    @Override
    public @Nullable ActionGroup getActionGroup(@NotNull String actionId) {
        Action action = actions.get(actionId);
        return action instanceof ActionGroup ? (ActionGroup) action : null;
    }

    @Override
    public void perform(@NotNull String actionId, @NotNull Object source) {
        Action action = getAction(actionId);
        if (action == null) {
            throw new IllegalArgumentException("Missing action: " + actionId);
        }
        action.perform(new ActionEvent(null, new Presentation(action), DataManager.getInstance().getDataContext(source)));
    }

    @Override
    public @NotNull Menu createMenu(@NotNull ActionGroup group) {
        Validate.notNull(group);
        return new ActionMenu(group);
    }

    @Override
    public @NotNull ContextMenu createContextMenu(@NotNull ActionGroup group) {
        Validate.notNull(group);
        return new ActionContextMenu(group);
    }

    @Override
    public @NotNull Button createButton(@NotNull Action action) {
        Validate.notNull(action);
        if (action instanceof ActionGroup || action instanceof Separator) {
            throw new IllegalArgumentException("The action cannot be ActionGroup and Separator");
        }
        return new ActionButton(action);
    }

    private void loadActions() {
        for (Plugin plugin : PluginManagerCore.getEnabledPlugins()) {
            for (ActionDescriptor action : plugin.getActions()) {
                Element element = action.getElement();
                switch (element.getName()) {
                    case ACTION_ELEMENT_NAME -> processActionElement(plugin, element);
                    case GROUP_ELEMENT_NAME -> processGroupElement(plugin, element);
                    case SEPARATOR_ELEMENT_NAME -> processSeparatorElement(plugin, element);
                    case REFERENCE_ELEMENT_NAME -> processReferenceElement(plugin, element);
                    default -> LOGGER.error("Unknown element {}, plugin={}", element.getName(), plugin.getId());
                }
            }
        }
    }

    private void processElement(Plugin plugin, Element element, ActionGroup parent) {
        switch (element.getName()) {
            case ACTION_ELEMENT_NAME -> processActionElement(plugin, element);
            case GROUP_ELEMENT_NAME -> processGroupElement(plugin, element);
            case SEPARATOR_ELEMENT_NAME -> processSeparatorElement(plugin, element);
            case REFERENCE_ELEMENT_NAME -> processReferenceElement(plugin, element);
            default -> LOGGER.error("Unknown element {}, plugin={}", element.getName(), plugin.getId());
        }
    }

    private Action processActionElement(Plugin plugin, Element element) {
        String id = element.attributeValue(ID_ATTR_NAME);
        if (StringUtils.isEmpty(id)) {
            LOGGER.error("Missing action id, plugin={}", plugin.getId());
            return null;
        }

        String className = element.attributeValue(CLASS_ATTR_NAME);
        if (StringUtils.isEmpty(className)) {
            LOGGER.error("Missing action class, id={}, plugin={}", id, plugin.getId());
            return null;
        }

        Action action;
        try {
            Class<?> clazz = plugin.getClassLoader().loadClass(className);
            if (!Action.class.isAssignableFrom(clazz)) {
                LOGGER.error("{} is not subclass of Action, id={}, plugin={}", className, id, plugin.getId());
                return null;
            }
            action = (Action) clazz.getConstructor().newInstance();
        } catch (ReflectiveOperationException e) {
            LOGGER.error("Cannot create action, class={}, id={}, plugin={}", className, id, plugin.getId(), e);
            return null;
        }

        processAttribute(plugin, element, id, action);
        registerAction(plugin, id, action);

        for (Element child : element.elements()) {
            switch (child.getName()) {
                case ADD_TO_GROUP_ELEMENT_NAME -> processAddToGroupElement(plugin, child, action);
                default -> LOGGER.error("Unknown element {}, plugin={}", child.getName(), plugin.getId());
            }
        }
        return action;
    }

    private Action processGroupElement(Plugin plugin, Element element) {
        String id = element.attributeValue(ID_ATTR_NAME);
        if (StringUtils.isEmpty(id)) {
            LOGGER.error("Missing group id, plugin={}", plugin.getId());
            return null;
        }

        String className = element.attributeValue(CLASS_ATTR_NAME);

        ActionGroup group;
        try {
            Class<?> clazz = className != null ? plugin.getClassLoader().loadClass(className) : ActionGroup.class;
            if (!ActionGroup.class.isAssignableFrom(clazz)) {
                LOGGER.error("{} is not subclass of ActionGroup, id={}, plugin={}", className, id, plugin.getId());
                return null;
            }
            group = (ActionGroup) clazz.getConstructor().newInstance();
        } catch (ReflectiveOperationException e) {
            LOGGER.error("Cannot create group, class={}, id={}, plugin={}", className, id, plugin.getId(), e);
            return null;
        }

        processAttribute(plugin, element, id, group);
        registerAction(plugin, id, group);

        for (Element child : element.elements()) {
            switch (child.getName()) {
                case ACTION_ELEMENT_NAME -> {
                    Action action = processActionElement(plugin, child);
                    if (action != null) {
                        group.addLast(action);
                    }
                }
                case GROUP_ELEMENT_NAME -> {
                    Action action = processGroupElement(plugin, child);
                    if (action != null) {
                        group.addLast(action);
                    }
                }
                case SEPARATOR_ELEMENT_NAME -> {
                    Action action = processSeparatorElement(plugin, child);
                    if (action != null) {
                        group.addLast(action);
                    }
                }
                case REFERENCE_ELEMENT_NAME -> {
                    Action action = processReferenceElement(plugin, child);
                    if (action != null) {
                        group.addLast(action);
                    }
                }
                case ADD_TO_GROUP_ELEMENT_NAME -> processAddToGroupElement(plugin, child, group);
                default -> LOGGER.error("Unknown element {}, plugin={}", child.getName(), plugin.getId());
            }
        }

        return group;
    }

    private Action processSeparatorElement(Plugin plugin, Element element) {
        String id = element.attributeValue(ID_ATTR_NAME);
        if (StringUtils.isEmpty(id)) {
            return Separator.getInstance();
        }

        Separator separator = new Separator();
        registerAction(plugin, id, separator);
        return separator;
    }

    private Action processReferenceElement(Plugin plugin, Element element) {
        String id = element.attributeValue(ID_ATTR_NAME);
        if (StringUtils.isEmpty(id)) {
            LOGGER.error("Missing reference id, plugin={}", plugin.getId());
            return null;
        }

        Action action = getAction(id);
        if (action == null) {
            LOGGER.error("Not found reference action, id={}, plugin={}", id, plugin.getId());
            return null;
        }

        return action;
    }

    private void processAddToGroupElement(Plugin plugin, Element element, Action action) {
        String groupId = element.attributeValue(GROUP_ID_ATTR_NAME);
        if (StringUtils.isEmpty(groupId)) {
            LOGGER.error("Missing add-to-group group-id, action={}, plugin={}", getActionId(action), plugin.getId());
            return;
        }
        ActionGroup group = getParentGroup(plugin, action, groupId);
        if (group == null) {
            return;
        }

        String anchor = element.attributeValue(ANCHOR_ATTR_NAME);
        if (anchor == null || "last".equalsIgnoreCase(anchor)) {
            group.addLast(action);
        } else if ("first".equalsIgnoreCase(anchor)) {
            group.addFirst(action);
        } else if (StringUtils.startsWithIgnoreCase(anchor, "before")) {
            int index = anchor.indexOf(' ');
            if (index == -1) {
                LOGGER.error("Missing anchor relative action id, action={}, plugin={}", getActionId(action), plugin.getId());
                return;
            }
            String anchorActionId = anchor.substring(index + 1);
            Action anchorAction = getAction(anchorActionId);
            if (anchorAction == null) {
                LOGGER.error("Not found anchor relative action, anchor={}, action={}, plugin={}", anchor, getActionId(action), plugin.getId());
                return;
            }
            group.addChild(action, true, anchorAction);
        } else if (StringUtils.startsWithIgnoreCase(anchor, "after")) {
            int index = anchor.indexOf(' ');
            if (index == -1) {
                LOGGER.error("Missing anchor relative action id, anchor={}, action={}, plugin={}", anchor, getActionId(action), plugin.getId());
                return;
            }
            String anchorActionId = anchor.substring(index + 1);
            Action anchorAction = getAction(anchorActionId);
            if (anchorAction == null) {
                LOGGER.error("Not found anchor relative action, anchor={}, action={}, plugin={}", anchor, getActionId(action), plugin.getId());
                return;
            }
            group.addChild(action, false, anchorAction);
        }
    }

    private ActionGroup getParentGroup(Plugin plugin, Action action, String groupId) {
        Action group = getAction(groupId);
        if (group == null) {
            LOGGER.error("Not found action group, id={}, action={}, plugin={}", groupId, getActionId(action), plugin.getId());
            return null;
        }
        if (!(group instanceof ActionGroup)) {
            LOGGER.error("Group is not instance of ActionGroup, id={}, action={}, plugin={}", groupId, getActionId(action), plugin.getId());
            return null;
        }
        return (ActionGroup) group;
    }

    private void processAttribute(Plugin plugin, Element element, String id, Action action) {
        L10n l10n = L10n.getL10n(plugin.getId());
        String text = l10n.localize(element.getName() + "." + id + "." + TEXT_ATTR_NAME);
        if (text != null) action.setText(text);
        String description = l10n.localizeOrNull(element.getName() + "." + id + "." + DESCRIPTION_ATTR_NAME);
        if (description != null) action.setDescription(description);
        String icon = element.attributeValue(ICON_ATTR_NAME);
        if (icon != null) action.setIcon(IconManager.getInstance().getIcon(icon));
    }

    private void registerAction(Plugin plugin, String id, Action action) {
        if (actions.putIfAbsent(id, action) != null) {
            LOGGER.error("Action has been registered, id={}, plugin={}", id, plugin.getId());
        }
    }
}
