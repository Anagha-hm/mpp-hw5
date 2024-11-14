// package edu.vt.ece.hw5.sets;

// public class LazySet<T> implements Set<T> {
//     private final Node<T> head;

//     public LazySet() {
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
//         private boolean marked;

//         public Node(U item, Node<U> next) {
//             this.key = item.hashCode();
//             this.next = next;
//             this.marked = false;
//         }

//         public Node(int key) {
//             this.key = key;
//             next = null;
//             this.marked = false;
//         }

//         public void mark() {
//             marked = true;
//         }

//         public boolean isMarked() {
//             return marked;
//         }
//     }
// }

package edu.vt.ece.hw5.sets;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LazySet<T> implements Set<T> {
    private final Node<T> head;

    public LazySet() {
        head = new Node<>(Integer.MIN_VALUE);
        head.next = new Node<>(Integer.MAX_VALUE);
    }

    private boolean validate(Node<T> pred, Node<T> curr) {
        return !pred.isMarked() && !curr.isMarked() && pred.next == curr;
    }

    @Override
    public boolean add(T item) {
        int key = item.hashCode();
        while (true) {
            Node<T> pred = head;
            Node<T> curr = head.next;

            while (curr.key < key) {
                pred = curr;
                curr = curr.next;
            }

            pred.lock();
            curr.lock();
            try {
                if (validate(pred, curr)) {
                    if (curr.key == key) {
                        return false; // Item is already in the set
                    } else {
                        Node<T> newNode = new Node<>(item, curr);
                        pred.next = newNode;
                        return true;
                    }
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

            while (curr.key < key) {
                pred = curr;
                curr = curr.next;
            }

            pred.lock();
            curr.lock();
            try {
                if (validate(pred, curr)) {
                    if (curr.key != key) {
                        return false; // Item not found
                    } else {
                        curr.mark(); // Logically mark the node as removed
                        pred.next = curr.next; // Physically remove the node
                        return true;
                    }
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

        while (curr.key < key) {
            curr = curr.next;
        }
        
        return curr.key == key && !curr.isMarked();
    }

    private static class Node<U> {
        int key;
        Node<U> next;
        private boolean marked;
        private final Lock lock = new ReentrantLock();

        public Node(U item, Node<U> next) {
            this.key = item.hashCode();
            this.next = next;
            this.marked = false;
        }

        public Node(int key) {
            this.key = key;
            this.next = null;
            this.marked = false;
        }

        public void mark() {
            marked = true;
        }

        public boolean isMarked() {
            return marked;
        }

        public void lock() {
            lock.lock();
        }

        public void unlock() {
            lock.unlock();
        }
    }
}
