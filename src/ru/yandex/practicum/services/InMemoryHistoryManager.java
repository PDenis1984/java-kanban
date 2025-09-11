package ru.yandex.practicum.services;

import ru.yandex.practicum.intf.HistoryManagerIntf;
import ru.yandex.practicum.models.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManagerIntf {

    // В мапе слева - ID задачи, справа Индекс в истории просмотров
    private final HashMap<Integer, Integer> historyMap;

    public InMemoryHistoryManager() {

        this.historyMap = new HashMap<>();
    }
    @Override
    public void add(Task task) {

        //Вычисляем, есть ли задача, если нет - добавляем в конец, обновляем historyMap <IDзадачи, size -1>?
        // если есть, то historyMap<ID задачи, >
        historyMap.removeFirst();
        historyMap.addLast(task);
    }
    @Override
    public List<Task> getHistory(){

        return  new ArrayList<>(this.historyList);
    }

    @Override
    public void clearHistory(){

        historyList.clear();

    }

    @Override
    public void remove(int mId) {


    }
    static class IndexHashMap<K,V> extends LinkedHashMap<K,V> {
        @Override
        public V put(K key, V value) {
            System.out.println("Сохранение значения " + value + " в ключе " + key);
            return super.put(key, value);
        }
    }
}

