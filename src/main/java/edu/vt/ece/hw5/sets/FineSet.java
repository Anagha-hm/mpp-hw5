// package edu.vt.ece.hw5.sets;

// import java.util.concurrent.locks.Lock;
// import java.util.concurrent.locks.ReentrantLock;

// public class FineSet<T> implements Set<T>{
//     private final Node<T> head;

//     public FineSet() {
//         head = new Node<>(Integer.MIN_VALUE);
//         head.next = new Node<>(Integer.MAX_VALUE);
//     }

//     @Override
//     public boolean add(T item) {
//         /* YOUR IMPLEMENTATION HERE */
//     }

//     @Override
//     public boolean remove(T item) {
//         /* YOUR IMPLEMENTATION HERE */
//     }

//     @Override
//     public boolean contains(T item) {
//         /* YOUR IMPLEMENTATION HERE */
//     }

//     private static class Node<U> {
//         private final Lock lock = new ReentrantLock();
//         int key;
//         Node<U> next;

//         public Node(U item, Node<U> next) {
//             this.key = item.hashCode();
//             this.next = next;
//         }

//         public Node(int key) {
//             this.key = key;
//             next = null;
//         }

//         public void lock() {
//             lock.lock();
//         }

//         public void unlock() {
//             lock.unlock();
//         }
//     }
// }

package edu.vt.ece.hw5.sets;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class FineSet<T> implements Set<T> {
    private final Node<T> head;

    public FineSet() {
        head = new Node<>(Integer.MIN_VALUE);
        head.next = new Node<>(Integer.MAX_VALUE);
    }

    @Override
    public boolean add(T item) {
        int key = item.hashCode();
        while (true) {
            Node<T> pred = head;
            Node<T> curr = head.next;

            pred.lock();
            try {
                curr.lock();
                try {
                    while (curr.key < key) {
                        pred.unlock();
                        pred = curr;
                        curr = curr.next;
                        curr.lock();
                    }
                    if (curr.key == key) {
                        return false; // Item already in set
                    } else {
                        Node<T> newNode = new Node<>(item, curr);
                        pred.next = newNode;
                        return true;
                    }
                } finally {
                    curr.unlock();
                }
            } finally {
                pred.unlock();
            }
        }
    }

    @Override
    public boolean remove(T item) {
        int key = item.hashCode();
        while (true) {
            Node<T> pred = head;
            Node<T> curr = head.next;

            pred.lock();
            try {
                curr.lock();
                try {
                    while (curr.key < key) {
                        pred.unlock();
                        pred = curr;
                        curr = curr.next;
                        curr.lock();
                    }
                    if (curr.key != key) {
                        return false; // Item not found in set
                    } else {
                        pred.next = curr.next;
                        return true;
                    }
                } finally {
                    curr.unlock();
                }
            } finally {
                pred.unlock();
            }
        }
    }

    @Override
    public boolean contains(T item) {
        int key = item.hashCode();
        Node<T> curr = head;

        while (curr.key < key) {
            curr = curr.next;
        }

        return curr.key == key;
    }

    private static class Node<U> {
        private final Lock lock = new ReentrantLock();
        int key;
        Node<U> next;

        public Node(U item, Node<U> next) {
            this.key = item.hashCode();
            this.next = next;
        }

        public Node(int key) {
            this.key = key;
            this.next = null;
        }

        public void lock() {
            lock.lock();
        }

        public void unlock() {
            lock.unlock();
        }
    }
}
