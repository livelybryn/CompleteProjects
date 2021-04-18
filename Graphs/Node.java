package assignment07;

/**
 * TODO: A [what] that does [what]
 *
 * Created by Bryn Koldewyn (bryn.koldewyn@utah.edu) on 2020/11/30
 */
public class Node<E> {

    private E data;
    private Node<E> cameFrom;
    private Node<E> top;
    private Node<E> bottom;
    private Node<E> left;
    private Node<E> right;

    public Node(E data) {
        this.data = data;
        this.top = null;
        this.bottom = null;
        this.left = null;
        this.right = null;
        this.cameFrom = null;
    }

    public void setData(E data) {
        this.data = data;
    }

    public Node<E> cameFrom() {
        return cameFrom;
    }

    public void setCameFrom(Node<E> cameFrom) {
        this.cameFrom = cameFrom;
    }

    public E data() {
        return data;
    }

    public Node<E> top() {
        return top;
    }

    public void setTop(Node<E> top) {
        this.top = top;
    }

    public Node<E> bottom() {
        return bottom;
    }

    public void setBottom(Node<E> bottom) {
        this.bottom = bottom;
    }

    public Node<E> left() {
        return left;
    }

    public void setLeft(Node<E> left) {
        this.left = left;
    }

    public Node<E> right() {
        return right;
    }

    public void setRight(Node<E> right) {
        this.right = right;
    }
}
