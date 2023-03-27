package com.github.mouse0w0.peach.extension;

import com.github.mouse0w0.peach.plugin.ExtensionDescriptor;
import com.github.mouse0w0.peach.plugin.Plugin;
import com.github.mouse0w0.peach.util.StringUtils;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Multimap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

final class ExtensionPointImpl<T> implements ExtensionPoint<T> {
    private static final Logger LOGGER = LoggerFactory.getLogger("Extension");

    private static final VarHandle EXTENSIONS;

    static {
        try {
            EXTENSIONS = MethodHandles.lookup().findVarHandle(ExtensionPointImpl.class, "extensions", List.class);
        } catch (ReflectiveOperationException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    private final Plugin plugin;
    private final String name;
    private final Class<T> extensionClass;
    private final boolean bean;

    private final List<ExtensionWrapper<T>> wrappers = new ArrayList<>();

    private volatile List<T> extensions;

    ExtensionPointImpl(Plugin plugin, String name, Class<T> extensionClass, boolean bean) {
        this.plugin = plugin;
        this.name = name;
        this.extensionClass = extensionClass;
        this.bean = bean;
    }

    @Override
    public Plugin getPlugin() {
        return plugin;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<T> getExtensions() {
        List<T> extensions;
        extensions = (List<T>) EXTENSIONS.getAcquire(this);
        if (extensions == null) {
            synchronized (this) {
                extensions = (List<T>) EXTENSIONS.getAcquire(this);
                if (extensions == null) {
                    List<ExtensionWrapper<T>> wrappers = getSortedWrappers();
                    ImmutableList.Builder<T> builder = ImmutableList.builderWithExpectedSize(wrappers.size());
                    for (ExtensionWrapper<T> wrapper : wrappers) {
                        builder.add(wrapper.getExtension());
                    }
                    extensions = builder.build();
                    EXTENSIONS.setRelease(this, extensions);
                }
            }
        }
        return extensions;
    }

    private List<ExtensionWrapper<T>> getSortedWrappers() {
        List<ExtensionWrapper<T>> wrappers = this.wrappers;
        if (wrappers.size() >= 2) {
            wrappers = new ArrayList<>(wrappers); // Copy list.
            Map<String, ExtensionWrapper<T>> idMap = new HashMap<>();
            Multimap<String, ExtensionWrapper<T>> duplicateIdMap = ArrayListMultimap.create();
            for (ExtensionWrapper<T> wrapper : wrappers) {
                String id = wrapper.getId();
                if (StringUtils.isEmpty(id)) continue;

                if (duplicateIdMap.containsKey(id)) {
                    duplicateIdMap.put(id, wrapper);
                } else {
                    ExtensionWrapper<T> duplicateWrapper = idMap.put(id, wrapper);
                    if (duplicateWrapper != null) {
                        duplicateIdMap.put(id, duplicateWrapper);
                        duplicateIdMap.put(id, wrapper);
                        idMap.remove(id);
                    }
                }
            }
            ExtensionOrder.sort(wrappers, idMap);

            for (String id : duplicateIdMap.keySet()) {
                StringBuilder messageBuilder = new StringBuilder("Duplicate extension with id `").append(id).append("`, as follows:");
                for (ExtensionWrapper<?> wrapper : duplicateIdMap.get(id)) {
                    messageBuilder.append("\n    - implementation=").append(wrapper.getImplementation()).append(", plugin=").append(wrapper.getPlugin().getId());
                }
                LOGGER.error(messageBuilder.toString());
            }
        }
        return wrappers;
    }

    void register(Plugin plugin, ExtensionDescriptor descriptor) {
        if (bean) {
            wrappers.add(new ExtensionWrapper<>(extensionClass, plugin, descriptor.getId(), descriptor.getOrder(), descriptor.getElement()));
            descriptor.freeElement();
        } else {
            descriptor.freeElement();
            wrappers.add(new ExtensionWrapper<>(descriptor.getImplementation(), plugin, descriptor.getId(), descriptor.getOrder(), null));
        }
    }
}
