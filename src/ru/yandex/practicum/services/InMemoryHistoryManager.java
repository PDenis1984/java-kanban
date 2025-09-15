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


        public void linkLast(int index, K task) {

            if (internalMap.containsKey(index)) {
                this.remove(index);
            }
            Node<K> oldTail = tail;
            Node<K> newNode = new Node<>(oldTail, task, null);

            if (head != null) {

                 oldTail.next = newNode;
            } else {                 // Первый элемент

                this.head = newNode;
            }
            this.tail = newNode;
            internalMap.put(index, newNode);
            size++;
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

                if (size == 1) { //Единственная запись

                    tail = null;
                    head = null;
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
                    }
                }
                size--;
                internalMap.remove(index);
            }
        }

        public void clear() {

            internalMap.clear();
            size = 0;

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

}

