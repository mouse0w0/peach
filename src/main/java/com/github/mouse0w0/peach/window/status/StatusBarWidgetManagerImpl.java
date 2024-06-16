package com.github.mouse0w0.peach.window.status;

import com.github.mouse0w0.peach.service.PersistentService;
import com.github.mouse0w0.peach.service.Storage;
import com.github.mouse0w0.peach.util.IterableUtils;
import com.github.mouse0w0.peach.util.JsonUtils;
import com.github.mouse0w0.peach.util.TypeUtils;
import com.google.gson.JsonElement;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Storage("statusBar.json")
public class StatusBarWidgetManagerImpl implements StatusBarWidgetManager, PersistentService {
    private final Map<String, StatusBarWidgetProvider> widgetProviderMap = new HashMap<>();
    private final Map<String, Integer> widgetIndexMap = new HashMap<>();
    private final Map<String, Boolean> widgetEnabledMap = new HashMap<>();

    public StatusBarWidgetManagerImpl() {
        IterableUtils.forEachWithIndex(getProviders(), (provider, i) -> {
            widgetProviderMap.put(provider.getId(), provider);
            widgetIndexMap.put(provider.getId(), i);
        });
    }

    @Override
    public List<StatusBarWidgetProvider> getProviders() {
        return StatusBarWidgetProvider.EXTENSION_POINT.getExtensions();
    }

    @Override
    public StatusBarWidgetProvider getProvider(String widgetId) {
        return widgetProviderMap.get(widgetId);
    }

    @Override
    public int getIndex(String widgetId) {
        return widgetIndexMap.get(widgetId);
    }

    @Override
    public boolean isEnabled(StatusBarWidgetProvider provider) {
        Boolean b = widgetEnabledMap.get(provider.getId());
        return b != null ? b : provider.isEnabledByDefault();
    }

    @Override
    public void setEnabled(StatusBarWidgetProvider provider, boolean enabled) {
        if (provider.isEnabledByDefault() == enabled) {
            widgetEnabledMap.remove(provider.getId());
        } else {
            widgetEnabledMap.put(provider.getId(), enabled);
        }

    }

    @Override
    public JsonElement saveState() {
        return JsonUtils.toJson(widgetEnabledMap);
    }

    @Override
    public void loadState(JsonElement state) {
        widgetEnabledMap.putAll(JsonUtils.fromJson(state, TypeUtils.map(String.class, Boolean.class)));
    }
}
