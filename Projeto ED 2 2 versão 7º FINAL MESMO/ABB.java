import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ABB<E extends Comparable<E>> {

    private Node<E> raiz;
    private long tempoExecucao; // Tempo total de execução das operações
    private int operacoes; // Contagem das operações realizadas

    // Construtores
    public ABB() {
        this.raiz = null;
        this.tempoExecucao = 0;
        this.operacoes = 0;
    }

    public Node<E> getRaiz() {
        return raiz;
    }

    public void setRaiz(Node<E> novaRaiz) {
        this.raiz = novaRaiz;
    }

    // Verifica se a árvore está vazia
    public boolean isEmpty() {
        return (raiz == null);
    }

     // Método que extrai o número de uma string para comparações
    private int extrairNumero(String valor) {
        String[] faixa_etaria = valor.split(" ");
        Pattern pattern = Pattern.compile("\\d+"); // Encontra sequências de dígitos
        Matcher matcher = pattern.matcher(faixa_etaria[1]);
        if (matcher.find()) {
            return Integer.parseInt(matcher.group()); // Retorna o primeiro número encontrado
        }
        return 0; // Retorna 0 se não encontrar um número
    }

    // Insere um novo valor na árvore
    public void inserir(E valor) {
        long inicio = System.nanoTime(); // Início da medição de tempo
        raiz = inserir(valor, raiz);
        long fim = System.nanoTime(); // Fim da medição de tempo
        tempoExecucao += (fim - inicio);
    }

    private Node<E> inserir(E valor, Node<E> node) {
        incrementarOperacoes(); // Incrementa o número de operações
        if (node == null) {
            return new Node<>(valor); // Cria um novo nó se a posição estiver vazia
        }

        // Cast para String para extrair o número e comparar corretamente
        String valorStr = (String) valor;
        String nodeValueStr = (String) node.getValue();
        int valorInt = extrairNumero(valorStr);
        int nodeValueInt = extrairNumero(nodeValueStr);

        if (valorInt < nodeValueInt) {
            node.setFilhoEsquerdo(inserir(valor, node.getFilhoEsquerdo())); // Insere à esquerda
        } else if (valorInt > nodeValueInt) {
            node.setFilhoDireito(inserir(valor, node.getFilhoDireito())); // Insere à direita
        } else {
            // Valores iguais: decida se quer permitir duplicatas ou não.
            // Aqui estamos retornando o node sem inserir duplicatas
            return node;
        }
        return node;
    }

    // Percorre a árvore em ordem
    public void emOrdem() {
        long inicio = System.nanoTime();
        emOrdem(raiz);
        long fim = System.nanoTime();
        tempoExecucao += (fim - inicio);
    }

    private void emOrdem(Node<E> node) {
        if (node != null) {
            emOrdem(node.getFilhoEsquerdo());
            System.out.print(node.getValue() + " ");
            incrementarOperacoes();
            emOrdem(node.getFilhoDireito());
        }
    }

    // Percorre a árvore em pré-ordem
    public void preOrdem() {
        long inicio = System.nanoTime();
        preOrdem(raiz);
        long fim = System.nanoTime();
        tempoExecucao += (fim - inicio);
    }

    private void preOrdem(Node<E> node) {
        if (node != null) {
            System.out.print(node.getValue() + " ");
            incrementarOperacoes();
            preOrdem(node.getFilhoEsquerdo());
            preOrdem(node.getFilhoDireito());
        }
    }

    // Percorre a árvore em pós-ordem
    public void posOrdem() {
        long inicio = System.nanoTime();
        posOrdem(raiz);
        long fim = System.nanoTime();
        tempoExecucao += (fim - inicio);
    }

    private void posOrdem(Node<E> node) {
        if (node != null) {
            posOrdem(node.getFilhoEsquerdo());
            posOrdem(node.getFilhoDireito());
            System.out.print(node.getValue() + " ");
            incrementarOperacoes();
        }
    }

    // Percorre a árvore em nível
    public void emNivel() {
        long inicio = System.nanoTime();
        LinkedList<Node<E>> fila = new LinkedList<>();
        fila.add(raiz);

        while (!fila.isEmpty()) {
            Node<E> atual = fila.removeFirst();
            if (atual != null) {
                incrementarOperacoes();
                System.out.print(atual.getValue() + " ");
                fila.add(atual.getFilhoEsquerdo());
                fila.add(atual.getFilhoDireito());
            }
        }
        long fim = System.nanoTime();
        tempoExecucao += (fim - inicio);
    }

    // Método para eliminar um valor
    public boolean eliminar(E valor) {
        long inicio = System.nanoTime();
        if (isEmpty()) return false;
        raiz = eliminar(valor, raiz);
        long fim = System.nanoTime();
        tempoExecucao += (fim - inicio);
        return true;
    }

    private Node<E> eliminar(E valor, Node<E> node) {
        if (node == null) {
            return null;
        }
        incrementarOperacoes();
        if (valor.compareTo(node.getValue()) < 0) {
            node.setFilhoEsquerdo(eliminar(valor, node.getFilhoEsquerdo()));
        } else if (valor.compareTo(node.getValue()) > 0) {
            node.setFilhoDireito(eliminar(valor, node.getFilhoDireito()));
        } else {
            if (node.getFilhoEsquerdo() == null) {
                return node.getFilhoDireito();
            } else if (node.getFilhoDireito() == null) {
                return node.getFilhoEsquerdo();
            } else {
                Node<E> maior = getMax(node.getFilhoEsquerdo());
                node.setValue(maior.getValue());
                node.setFilhoEsquerdo(eliminar(maior.getValue(), node.getFilhoEsquerdo()));
            }
        }
        return node;
    }

    private Node<E> getMax(Node<E> node) {
        while (node.getFilhoDireito() != null) {
            node = node.getFilhoDireito();
            incrementarOperacoes();
        }
        return node;
    }

    // Métodos para obter o número de operações e o tempo de execução
    public int getOperacoes() {
        return operacoes;
    }

    public long getTempoExecucao() {
        return tempoExecucao;
    }

    // Incrementa o contador de operações
    private void incrementarOperacoes() {
        this.operacoes++;
    }
    // Retorna o maior nó da árvore (último nó à direita)
    public Node<E> getMaiorValorABB() {
        long inicio = System.nanoTime();
        if (raiz == null) {
            return null; // Retorna null se a árvore estiver vazia
        }
        
        Node<E> atual = raiz;
        while (atual.getFilhoDireito() != null) {
            atual = atual.getFilhoDireito();
            incrementarOperacoes(); // Incrementa operações para estatísticas
        }
        
        long fim = System.nanoTime(); // Fim da medição de tempo
        tempoExecucao += (fim - inicio);
        return atual; // Retorna o maior nó
    }

    public Node<E> getMenorValorABB() {
        return getMinABB(raiz);
    }
    
    private Node<E> getMinABB(Node<E> node) {
        long inicio = System.nanoTime();
        if (node == null) {
            return null; // Caso a árvore esteja vazia
        }
        while (node.getFilhoEsquerdo() != null) {
            node = node.getFilhoEsquerdo(); // Vai para o filho direito até chegar ao final
        }
        long fim = System.nanoTime(); // Fim da medição de tempo
        tempoExecucao += (fim - inicio);
        return node; // Retorna o nó mais à direita (o maior valor)
    }

    public Node<E> getElemento(E valor) {
        return buscarElemento(valor, raiz);
    }
    
    private Node<E> buscarElemento(E valor, Node<E> node) {
        long inicio = System.nanoTime();
        if (node == null) {
            return null; // Retorna null se o nó não for encontrado
        }
    
        if (valor.compareTo(node.getValue()) < 0) {
            long fim = System.nanoTime(); // Fim da medição de tempo
        tempoExecucao += (fim - inicio);
            return buscarElemento(valor, node.getFilhoEsquerdo()); // Busca à esquerda
        } else if (valor.compareTo(node.getValue()) > 0) {
            return buscarElemento(valor, node.getFilhoDireito()); // Busca à direita
        } else {
            long fim = System.nanoTime(); // Fim da medição de tempo
            tempoExecucao += (fim - inicio);
            return node; // Retorna o nó se o valor for encontrado
        }
    }
    
}