package list;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author krzykrucz
 */
abstract class AbstractConcurrentList<E> implements ConcurrentList<E> {

    int size = 0; //comment
    Node<E> guard = new Node<>();

    @Override
    public int size() {
        return size;
    }

    @Override
    public Boolean isEmpty() {
        return size == 0;
    }

    void unlink(Node<E> prev) {

        Node<E> x = prev.next;
        prev.next = x.next;
        x.next = null;
        x.item = null;
        size--;
    }

    void linkLast(Node<E> last, E e) {
        last.next = new Node<>(e, null);
        size++;
    }

    static class Node<E> {
        E item;
        Node<E> next;
        Lock lock;

        Node(E item, Node<E> next) {
            this.item = item;
            this.next = next;
            lock = new ReentrantLock();
        }

        Node() {
            lock = new ReentrantLock();
        }
    }
}
