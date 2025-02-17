package com.github.mouse0w0.peach.mcmod.index;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.*;
import java.util.function.BiConsumer;

@SuppressWarnings("SuspiciousMethodCalls")
public class IndexImpl<K, V> implements IndexEx<K, V> {
    private final IndexKey<K, V> key;
    private final Map<K, V> map = new HashMap<>();
    private final ObservableList<K> keyList = FXCollections.observableArrayList();
    private final ObservableList<K> unmodifiableKeyList = FXCollections.unmodifiableObservableList(keyList);

    private int projectEntryCount;

    public IndexImpl(IndexKey<K, V> key) {
        this.key = key;
    }

    @Override
    public IndexKey<K, V> getKey() {
        return key;
    }

    @Override
    public int size() {
        return map.size();
    }

    @Override
    public boolean isEmpty() {
        return map.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return map.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return map.containsValue(value);
    }

    @Override
    public V get(Object key) {
        return map.get(key);
    }

    @Override
    public V getOrDefault(Object key, V defaultValue) {
        return map.getOrDefault(key, defaultValue);
    }

    @Override
    public Collection<K> keys() {
        return map.keySet();
    }

    @Override
    public ObservableList<K> keyList() {
        return unmodifiableKeyList;
    }

    @Override
    public Collection<V> values() {
        return map.values();
    }

    @Override
    public Collection<Map.Entry<K, V>> entries() {
        return map.entrySet();
    }

    @Override
    public void forEach(BiConsumer<? super K, ? super V> action) {
        map.forEach(action);
    }

    @Override
    public boolean addProjectEntry(K key, V value) {
        if (map.putIfAbsent(key, Objects.requireNonNull(value)) == null) {
            keyList.add(projectEntryCount++, key);
            return true;
        }
        return false;
    }

    @Override
    public boolean addNonProjectEntry(K key, V value) {
        if (map.putIfAbsent(key, Objects.requireNonNull(value)) == null) {
            keyList.add(key);
            return true;
        }
        return false;
    }

    @Override
    public boolean removeProjectEntry(Object key) {
        int projectEntryIndex = indexOfProjectEntry(key);
        if (projectEntryIndex != -1) {
            map.remove(key);
            keyList.remove(projectEntryIndex);
            projectEntryCount--;
            return true;
        }
        return false;
    }

    private int indexOfProjectEntry(Object key) {
        for (int i = 0; i < projectEntryCount; i++) {
            if (keyList.get(i).equals(key)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public boolean removeNonProjectEntry(Object key) {
        int nonProjectEntryIndex = indexOfNonProjectEntry(key);
        if (nonProjectEntryIndex != -1) {
            map.remove(key);
            keyList.remove(nonProjectEntryIndex);
            return true;
        }
        return false;
    }

    private int indexOfNonProjectEntry(Object key) {
        for (int i = projectEntryCount; i < keyList.size(); i++) {
            if (keyList.get(i).equals(key)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public void clearAllProjectEntries() {
        ListIterator<K> listIterator = keyList.listIterator();
        while (listIterator.hasNext() && listIterator.nextIndex() < projectEntryCount) {
            K next = listIterator.next();
            listIterator.remove();
            map.remove(next);
        }
    }

    @Override
    public void clearAllNonProjectEntries() {
        ListIterator<K> listIterator = keyList.listIterator(projectEntryCount);
        while (listIterator.hasNext()) {
            K next = listIterator.next();
            listIterator.remove();
            map.remove(next);
        }
    }
}
