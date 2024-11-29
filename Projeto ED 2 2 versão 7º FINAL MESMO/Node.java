public class Node<E> {
    private E value;
    private Node<E> filhoEsquerdo;
    private Node<E> filhoDireito;
    private int altura;  // Altura do nó

    // Construtor
    public Node(E value) {
        this.value = value;
        this.filhoEsquerdo = null;
        this.filhoDireito = null;
        this.altura = 0; // Altura inicial do nó é 0
    }
    public E getElemento() {
        return this.value;
    }

    public E getElementoAVL() {
        return this.value;
    }

    // Getters e Setters
    public E getValue() {
        return value;
    }

    public void setValue(E value) {
        this.value = value;
    }

    public Node<E> getFilhoEsquerdo() {
        return filhoEsquerdo;
    }

    public void setFilhoEsquerdo(Node<E> filhoEsquerdo) {
        this.filhoEsquerdo = filhoEsquerdo;
    }

    public Node<E> getFilhoDireito() {
        return filhoDireito;
    }

    public void setFilhoDireito(Node<E> filhoDireito) {
        this.filhoDireito = filhoDireito;
    }

    public int getAltura() {
        return altura;
    }

    public void setAltura(int altura) {
        this.altura = altura;
    }
}
