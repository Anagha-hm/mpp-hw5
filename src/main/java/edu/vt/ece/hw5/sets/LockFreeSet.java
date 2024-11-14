// package edu.vt.ece.hw5.sets;

// import java.util.concurrent.atomic.AtomicMarkableReference;

// public class LockFreeSet<T> implements Set<T> {
//     private final Node<T> head;

//     public LockFreeSet() {
//         Node<T> tail = new Node<>(Integer.MAX_VALUE);
//         head = new Node<>(Integer.MIN_VALUE, tail);
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
//         AtomicMarkableReference<Node<U>> next;

//         public Node(U item, Node<U> next) {
//             this.key = item.hashCode();
//             this.next = new AtomicMarkableReference<>(next, false);
//         }

//         public Node(int key, Node<U> next) {
//             this.key = key;
//             this.next = new AtomicMarkableReference<>(next, false);
//         }

//         public Node(int key) {
//             this(key, null);
//         }
//     }
// }


package edu.vt.ece.hw5.sets;

import java.util.concurrent.atomic.AtomicMarkableReference;

public class LockFreeSet<T> implements Set<T> {
    private final Node<T> head;

    public LockFreeSet() {
        Node<T> tail = new Node<>(Integer.MAX_VALUE);
        head = new Node<>(Integer.MIN_VALUE, tail);
    }

    private Window<T> find(Node<T> head, int key) {
        Node<T> pred = null, curr = null, succ = null;
        boolean[] marked = {false};
        retry: while (true) {
            pred = head;
            curr = pred.next.getReference();
            while (true) {
                succ = curr.next.get(marked);
                while (marked[0]) { // If curr is marked, remove it
                    if (!pred.next.compareAndSet(curr, succ, false, false)) {
                        continue retry;
                    }
                    curr = succ;
                    succ = curr.next.get(marked);
                }
                if (curr.key >= key) {
                    return new Window<>(pred, curr);
                }
                pred = curr;
                curr = succ;
            }
        }
    }

    @Override
    public boolean add(T item) {
        int key = item.hashCode();
        while (true) {
            Window<T> window = find(head, key);
            Node<T> pred = window.pred;
            Node<T> curr = window.curr;

            if (curr.key == key) {
                return false; // Key already exists, do nothing
            } else {
                Node<T> newNode = new Node<>(item, curr);
                if (pred.next.compareAndSet(curr, newNode, false, false)) {
                    return true;
                }
            }
        }
    }

    @Override
    public boolean remove(T item) {
        int key = item.hashCode();
        while (true) {
            Window<T> window = find(head, key);
            Node<T> pred = window.pred;
            Node<T> curr = window.curr;

            if (curr.key != key) {
                return false; // Key not found, do nothing
            } else {
                Node<T> succ = curr.next.getReference();
                if (!curr.next.compareAndSet(succ, succ, false, true)) {
                    continue; // Another thread marked it, retry
                }
                pred.next.compareAndSet(curr, succ, false, false);
                return true;
            }
        }
    }

    @Override
    public boolean contains(T item) {
        int key = item.hashCode();
        boolean[] marked = {false};
        Node<T> curr = head;
        while (curr.key < key) {
            curr = curr.next.getReference();
            curr.next.get(marked);
        }
        return (curr.key == key && !marked[0]);
    }

    private static class Node<U> {
        int key;
        AtomicMarkableReference<Node<U>> next;

        public Node(U item, Node<U> next) {
            this.key = item.hashCode();
            this.next = new AtomicMarkableReference<>(next, false);
        }

        public Node(int key, Node<U> next) {
            this.key = key;
            this.next = new AtomicMarkableReference<>(next, false);
        }

        public Node(int key) {
            this(key, null);
        }
    }

    private static class Window<T> {
        public Node<T> pred, curr;

        public Window(Node<T> pred, Node<T> curr) {
            this.pred = pred;
            this.curr = curr;
        }
    }
}
