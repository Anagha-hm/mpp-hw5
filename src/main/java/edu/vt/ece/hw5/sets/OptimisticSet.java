// package edu.vt.ece.hw5.sets;

// public class OptimisticSet<T> implements Set<T> {
//     private final Node<T> head;

//     public OptimisticSet() {
//         head = new Node<>(Integer.MIN_VALUE);
//         head.next = new Node<>(Integer.MAX_VALUE);
//     }

//     private boolean validate(Node<T> pred, Node<T> curr) {
//         /* YOUR IMPLEMENTATION HERE */
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
//     }
// }
package edu.vt.ece.hw5.sets;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class OptimisticSet<T> implements Set<T> {
    private final Node<T> head;

    public OptimisticSet() {
        head = new Node<>(Integer.MIN_VALUE);
        head.next = new Node<>(Integer.MAX_VALUE);
    }

    private boolean validate(Node<T> pred, Node<T> curr) {
        // Check if pred and curr are still in the list and adjacent.
        return pred.next == curr;
    }

    @Override
    public boolean add(T item) {
        int key = item.hashCode();
        while (true) {
            Node<T> pred = head;
            Node<T> curr = head.next;
            // Traverse until finding position for key
            while (curr.key < key) {
                pred = curr;
                curr = curr.next;
            }
            // Lock the predecessor and current nodes
            pred.lock();
            curr.lock();
            try {
                if (validate(pred, curr)) {
                    // If key is already present, don't add
                    if (curr.key == key) {
                        return false;
                    }
                    // Otherwise, insert new node
                    Node<T> newNode = new Node<>(item, curr);
                    pred.next = newNode;
                    return true;
                }
            } finally {
                pred.unlock();
                curr.unlock();
            }
        }
    }

    @Override
    public boolean remove(T item) {
        int key = item.hashCode();
        while (true) {
            Node<T> pred = head;
            Node<T> curr = head.next;
            // Traverse to find position for key
            while (curr.key < key) {
                pred = curr;
                curr = curr.next;
            }
            // Lock the predecessor and current nodes
            pred.lock();
            curr.lock();
            try {
                if (validate(pred, curr)) {
                    // If key is not present, can't remove
                    if (curr.key != key) {
                        return false;
                    }
                    // Otherwise, remove node by skipping over it
                    pred.next = curr.next;
                    return true;
                }
            } finally {
                pred.unlock();
                curr.unlock();
            }
        }
    }

    @Override
    public boolean contains(T item) {
        int key = item.hashCode();
        Node<T> curr = head;
        // Traverse to find key
        while (curr.key < key) {
            curr = curr.next;
        }
        // Check if found key matches the target item key
        return curr.key == key;
    }

    private static class Node<U> {
        int key;
        Node<U> next;
        Lock lock = new ReentrantLock();

        public Node(U item, Node<U> next) {
            this.key = item.hashCode();
            this.next = next;
        }

        public Node(int key) {
            this.key = key;
            next = null;
        }

        public void lock() {
            lock.lock();
        }

        public void unlock() {
            lock.unlock();
        }
    }
}
