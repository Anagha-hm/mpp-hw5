// package edu.vt.ece.hw5.sets;

// public class CoarseSet<T> implements Set<T>{
//     private final Node<T> head;

//     public CoarseSet() {
//         head = new Node<>(Integer.MIN_VALUE);
//         head.next = new Node<>(Integer.MAX_VALUE);
//     }

//     @Override
//     public boolean add(T item) {
//         /* YOUR IMPLEMENTATION HERE */
        
//         /* Hint: Look into object type's hashCode() method. */
//         /* Hint: Look into synchronized methods. */
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

public class CoarseSet<T> implements Set<T> {
    private final Node<T> head;
    private final Object lock = new Object(); // Single lock for the entire set

    public CoarseSet() {
        head = new Node<>(Integer.MIN_VALUE);
        head.next = new Node<>(Integer.MAX_VALUE);
    }

    @Override
    public boolean add(T item) {
        int key = item.hashCode();
        synchronized (lock) { // Locking the entire list
            Node<T> pred = head;
            Node<T> curr = head.next;
            while (curr.key < key) {
                pred = curr;
                curr = curr.next;
            }
            if (curr.key == key) {
                return false; // Item already in set
            } else {
                Node<T> newNode = new Node<>(item, curr);
                pred.next = newNode;
                return true;
            }
        }
    }

    @Override
    public boolean remove(T item) {
        int key = item.hashCode();
        synchronized (lock) { // Locking the entire list
            Node<T> pred = head;
            Node<T> curr = head.next;
            while (curr.key < key) {
                pred = curr;
                curr = curr.next;
            }
            if (curr.key != key) {
                return false; // Item not found in set
            } else {
                pred.next = curr.next; // Removing the node
                return true;
            }
        }
    }

    @Override
    public boolean contains(T item) {
        int key = item.hashCode();
        synchronized (lock) { // Locking the entire list
            Node<T> curr = head;
            while (curr.key < key) {
                curr = curr.next;
            }
            return curr.key == key;
        }
    }

    private static class Node<U> {
        int key;
        Node<U> next;

        public Node(U item, Node<U> next) {
            this.key = item.hashCode();
            this.next = next;
        }

        public Node(int key) {
            this.key = key;
            next = null;
        }
    }
}
