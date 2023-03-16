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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;

public class ActionManagerImpl implements ActionManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(ActionManagerImpl.class);

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

    private final BiMap<String, Action> registeredActions = HashBiMap.create();

    public ActionManagerImpl() {
        loadActions();
    }

    @Override
    public String getActionId(Action action) {
        return registeredActions.inverse().get(action);
    }

    @Override
    public Action getAction(String actionId) {
        return registeredActions.get(actionId);
    }

    @Override
    public void perform(String actionId, Event event) {
        getAction(actionId).perform(new ActionEvent(event));
    }

    @Override
    public Menu createMenu(@Nonnull ActionGroup group) {
        return new ActionMenu(group);
    }

    @Override
    public ContextMenu createContextMenu(@Nonnull ActionGroup group) {
        return new ActionContextMenu(group);
    }

    @Override
    public Button createButton(@Nonnull Action action) {
        Validate.notNull(action);
        if (action instanceof ActionGroup || action instanceof Separator) {
            throw new IllegalArgumentException("action");
        }

        Button button = new Button();
        Appearance appearance = action.getAppearance();
        button.setText(appearance.getText());
        button.setGraphic(IconManager.getInstance().createNode(appearance.getIcon()));
        button.setOnAction(event -> action.perform(new ActionEvent(event)));
        return button;
    }

    private void loadActions() {
        for (Plugin plugin : PluginManagerCore.getEnabledPlugins()) {
            for (ActionDescriptor action : plugin.getActions()) {
                processElement(null, action.getElement());
            }
        }
    }

    private void processElement(ActionGroup group, Element element) {
        String name = element.getName();
        if (ACTION_ELEMENT_NAME.equals(name)) {
            processActionElement(group, element);
        } else if (GROUP_ELEMENT_NAME.equals(name)) {
            processGroupElement(group, element);
        } else if (SEPARATOR_ELEMENT_NAME.equals(name)) {
            processSeparatorElement(group, element);
        } else if (REFERENCE_ELEMENT_NAME.equals(name)) {
            processReferenceElement(group, element);
        } else {
            LOGGER.error("Unknown element `{}`.", name);
        }
    }

    private void processActionElement(ActionGroup group, Element element) {
        String id = element.attributeValue(ID_ATTR_NAME);

        String className = element.attributeValue(CLASS_ATTR_NAME);
        if (StringUtils.isEmpty(className)) {
            LOGGER.error("The `class` attribute of action `{}` should be specified.", id);
            return;
        }

        if (id == null) {
            id = StringUtils.substringAfterLast(className, '.');
        }

        Action action;
        try {
            action = (Action) Class.forName(className).getDeclaredConstructor().newInstance();
        } catch (ReflectiveOperationException e) {
            LOGGER.error("Failed to create an action instance of " + className, e);
            return;
        }

        if (group != null) {
            group.addChild(action);
        }

        processAppearance(element, id, action.getAppearance());

        registerAction(id, action);
    }

    private void processGroupElement(ActionGroup group, Element element) {
        String className = element.attributeValue(CLASS_ATTR_NAME, "");

        ActionGroup action;
        try {
            Class<?> clazz = StringUtils.isEmpty(className) ? ActionGroup.class : Class.forName(className);
            action = (ActionGroup) clazz.getDeclaredConstructor().newInstance();
        } catch (ReflectiveOperationException e) {
            LOGGER.error("Failed to create an action instance of " + className, e);
            return;
        }

        if (group != null) {
            group.addChild(action);
        }

        String id = element.attributeValue(ID_ATTR_NAME, StringUtils.substringAfterLast(className, '.'));

        processAppearance(element, id, action.getAppearance());

        registerAction(id, action);

        element.elements().forEach(child -> processElement(action, child));
    }

    private void processSeparatorElement(ActionGroup group, Element element) {
        Separator separator = Separator.getInstance();
        if (group != null) {
            group.addChild(separator);
        }
    }

    private void processReferenceElement(ActionGroup group, Element element) {
        String id = element.attributeValue(ID_ATTR_NAME);
        if (id == null) {
            LOGGER.error("The id of reference is null.");
            return;
        }

        Action action = getAction(id);
        if (action == null) {
            LOGGER.error("Not found action by id `{}`.", id);
            return;
        }

        if (group != null) {
            group.addChild(action);
        }
    }

    private void processAppearance(Element element, String id, Appearance appearance) {
        appearance.setText(localize(element, id, TEXT_ATTR_NAME));
        appearance.setDescription(localize(element, id, DESCRIPTION_ATTR_NAME));

        String icon = element.attributeValue(ICON_ATTR_NAME);
        if (icon != null && !icon.isEmpty()) {
            appearance.setIcon(icon);
        }
    }

    private void registerAction(String actionId, Action action) {
        registeredActions.put(actionId, action);
    }

    private String localize(Element element, String id, String attrName) {
        return I18n.translate(element.getName() + "." + id + "." + attrName, element.attributeValue(attrName, ""));
    }
}
