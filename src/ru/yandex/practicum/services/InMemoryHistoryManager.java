package ru.yandex.practicum.services;

import ru.yandex.practicum.intf.HistoryManagerIntf;
import ru.yandex.practicum.models.Task;
import ru.yandex.practicum.utils.Node;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManagerIntf {

    // Утилитный класс для работы с историей
    static class IndexedHashMap<K> {


        private final Map<Integer, Node<K>> internalMap;
        private Node<K> head; // Начало списка
        private Node<K> tail; //Конец списка
        private int size = 0;

        public IndexedHashMap() {

            internalMap = new HashMap<>();
        }

        public void add(int index, K body) {

            final Node<K> oldHead = head;
            Node<K> newNode;

            if (oldHead == null) { // Первый элемент
                newNode = new Node<>(null, body, null);
                this.tail = newNode;
                this.head = newNode;
                newNode.prev = null;
                newNode.next = null;
                internalMap.put(index, newNode);
                size++;
            }
        }

        public void linkLast(int index, K task) {

            if (internalMap.containsKey(index)) {
                this.remove(index);
            }
            Node<K> oldHead = head;
            Node<K> oldTail = tail;

            if (oldHead != null) {
                Node<K> newNode = new Node<>(oldTail, task, null);
                this.tail = newNode;
                oldTail.next = newNode;
                newNode.prev = oldTail;
                newNode.next = null;
                internalMap.put(index, newNode);
                size++;
            } else {
                this.add(index, task);
            }
        }

        public List<K> getHistory() {

            List<K> result = new ArrayList<>();
            Node<K> current = head;
            while (current != null) {
                result.add(current.data);
                current = current.next;
            }
            return result;
        }

        public void remove(int index) {

            if (!internalMap.containsKey(index)) {
                System.out.println("Задачи с индексом " + index + " нет в истории");
            } else {
                Node<K> nodeToDelete = internalMap.get(index);
                Node<K> oldPrev = nodeToDelete.prev;
                Node<K> oldNext = nodeToDelete.next;
                if (oldPrev != null && oldNext != null) { // Если в центре списка
                    oldPrev.next = oldNext;
                    oldNext.prev = oldPrev;
                } else if (oldPrev == null && oldNext != null) { // если начало списка, а не одна запись
                    head = nodeToDelete.next;
                    head.prev = null;
                } else if (oldPrev != null) { // если конец списка
                    oldPrev.next = null;
                    tail = oldPrev;
                } else { // Это единственная запись в истории
                    tail = null;
                    head = null;
                }
                size--;
                internalMap.remove(index);
            }
        }

        public void clear() {

            internalMap.clear();

        }
    }


    // Новая структура для истории
    private final IndexedHashMap<Task> historyMap;

    public InMemoryHistoryManager() {

        this.historyMap = new IndexedHashMap<Task>();
    }

    @Override
    public void add(Task task) {

        //Используем утилитный класс
        historyMap.linkLast(task.getID(), task);

    }

    @Override
    public List<Task> getHistory() {

        return historyMap.getHistory();
    }

    @Override
    public void clearHistory() {

        historyMap.clear();

    }

    @Override
    public void remove(int mId) {

        historyMap.remove(mId);
    }

    public void removeNode(int index) {
        historyMap.remove(index);
    }

}

