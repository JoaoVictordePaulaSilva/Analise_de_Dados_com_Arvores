import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AVL<E extends Comparable<E>> {
    
    private Node<E> raiz;
    private int contadorInsercao;
    private int contadorRemocao;
    private int contadorBusca;
    private long tempoExecucao;

    public AVL() {
        raiz = null;
        contadorInsercao = 0;
        contadorRemocao = 0;
        contadorBusca = 0;
    }

    public Node<E> getRaiz() {
        return raiz;
    }
    
    public void setRaiz(Node<E> novaRaiz) {
        this.raiz = novaRaiz;
    }

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

    public void inserir(E valor) {
        long inicio = System.nanoTime(); // Início da medição de tempo
        raiz = inserir(valor, raiz);
        long fim = System.nanoTime(); // Fim da medição de tempo
        tempoExecucao += (fim - inicio);
    }

    private Node<E> inserir(E valor, Node<E> node) {
        contadorInsercao++; // Incrementa a contagem de inserção
        if (node == null) {
            return new Node<>(valor);
        }

        // Comparação personalizada para strings contendo números
        int valorInt = extrairNumero((String) valor);
        int nodeValueInt = extrairNumero((String) node.getValue());

        if (valorInt < nodeValueInt) {
            node.setFilhoEsquerdo(inserir(valor, node.getFilhoEsquerdo()));
        } else if (valorInt > nodeValueInt) {
            node.setFilhoDireito(inserir(valor, node.getFilhoDireito()));
        }

        node.setAltura(1 + Math.max(getAltura(node.getFilhoEsquerdo()), getAltura(node.getFilhoDireito())));
        return balancear(node);
    }

    private Node<E> balancear(Node<E> node) {
        int balanceamento = getFatorBalanceamento(node);

        if (balanceamento > 1 && getFatorBalanceamento(node.getFilhoEsquerdo()) >= 0) {
            return rotacaoDireita(node);
        }

        if (balanceamento < -1 && getFatorBalanceamento(node.getFilhoDireito()) <= 0) {
            return rotacaoEsquerda(node);
        }

        if (balanceamento > 1 && getFatorBalanceamento(node.getFilhoEsquerdo()) < 0) {
            node.setFilhoEsquerdo(rotacaoEsquerda(node.getFilhoEsquerdo()));
            return rotacaoDireita(node);
        }

        if (balanceamento < -1 && getFatorBalanceamento(node.getFilhoDireito()) > 0) {
            node.setFilhoDireito(rotacaoDireita(node.getFilhoDireito()));
            return rotacaoEsquerda(node);
        }

        return node; 
    }

    private int getFatorBalanceamento(Node<E> node) {
        if (node == null) return 0;
        return getAltura(node.getFilhoEsquerdo()) - getAltura(node.getFilhoDireito());
    }

    private int getAltura(Node<E> node) {
        if (node == null) return 0;
        return node.getAltura();
    }

    private Node<E> rotacaoDireita(Node<E> y) {
        Node<E> x = y.getFilhoEsquerdo();
        Node<E> T2 = x.getFilhoDireito();

        x.setFilhoDireito(y);
        y.setFilhoEsquerdo(T2);
        y.setAltura(Math.max(getAltura(y.getFilhoEsquerdo()), getAltura(y.getFilhoDireito())) + 1);
        x.setAltura(Math.max(getAltura(x.getFilhoEsquerdo()), getAltura(x.getFilhoDireito())) + 1);

        return x;
    }

    private Node<E> rotacaoEsquerda(Node<E> x) {
        Node<E> y = x.getFilhoDireito();
        Node<E> T2 = y.getFilhoEsquerdo();
        y.setFilhoEsquerdo(x);
        x.setFilhoDireito(T2);

        x.setAltura(Math.max(getAltura(x.getFilhoEsquerdo()), getAltura(x.getFilhoDireito())) + 1);
        y.setAltura(Math.max(getAltura(y.getFilhoEsquerdo()), getAltura(y.getFilhoDireito())) + 1);

        return y;
    }

    public boolean buscar(E valor) {
        return buscar(raiz, valor);
    }

    private boolean buscar(Node<E> node, E valor) {
        contadorBusca++; // Incrementa a contagem de busca
        if (node == null) {
            return false;
        }
        if (valor.compareTo(node.getValue()) == 0) {
            return true;
        } else if (valor.compareTo(node.getValue()) < 0) {
            return buscar(node.getFilhoEsquerdo(), valor);
        } else {
            return buscar(node.getFilhoDireito(), valor);
        }
    }

    public boolean eliminar(E valor) {
        if (isEmpty()) return false;
        raiz = eliminar(valor, raiz);
        return true;
    }

    private Node<E> eliminar(E valor, Node<E> node) {
        contadorRemocao++; // Incrementa a contagem de remoção
        if (node == null) {
            return null;
        }
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

        node.setAltura(Math.max(getAltura(node.getFilhoEsquerdo()), getAltura(node.getFilhoDireito())) + 1);
        return balancear(node);
    }

    private Node<E> getMax(Node<E> node) {
        while (node.getFilhoDireito() != null) {
            node = node.getFilhoDireito(); 
        }
        return node;
    }

    public int getContadorInsercao() {
        return contadorInsercao;
    }

    public int getContadorRemocao() {
        return contadorRemocao;
    }

    public int getContadorBusca() {
        return contadorBusca;
    }

    public long getTempoExecucao(){
        return tempoExecucao;
    }

    public void exibirAVL() {
        long inicio = System.nanoTime();
        exibirPreOrdem(raiz);
        long fim = System.nanoTime();
        tempoExecucao += (fim - inicio);
    }

    private void exibirPreOrdem(Node<E> node) {
        if (node == null) {
            return;
        }
        System.out.println("Valor: " + node.getValue() + ", Altura: " + node.getAltura());
        exibirPreOrdem(node.getFilhoEsquerdo());
        exibirPreOrdem(node.getFilhoDireito());
    }

    public Node<E> getElementoAVL(E valor) {
        return buscarElementoAVL(valor, raiz);
    }
    
    private Node<E> buscarElementoAVL(E valor, Node<E> node) {
        long inicio = System.nanoTime();
        if (node == null) {
            return null; // Retorna null se o nó não for encontrado
        }
    
        if (valor.compareTo(node.getValue()) < 0) {
            long fim = System.nanoTime();
            tempoExecucao += (fim - inicio);
            return buscarElementoAVL(valor, node.getFilhoEsquerdo()); // Busca à esquerda
        } else if (valor.compareTo(node.getValue()) > 0) {
            return buscarElementoAVL(valor, node.getFilhoDireito()); // Busca à direita
        } else {
            long fim = System.nanoTime();
            tempoExecucao += (fim - inicio);
            return node; // Retorna o nó se o valor for encontrado
        }
    }
    public Node<E> getMaiorValorAVL() {
        return getMaxAVL(raiz);
    }
    
    private Node<E> getMaxAVL(Node<E> node) {
        long inicio = System.nanoTime();
        if (node == null) {
            return null; // Caso a árvore esteja vazia
        }
        while (node.getFilhoDireito() != null) {
            node = node.getFilhoDireito(); // Vai para o filho direito até chegar ao final
        }
        long fim = System.nanoTime();
        tempoExecucao += (fim - inicio);
        return node; // Retorna o nó mais à direita (o maior valor)
    }

    public Node<E> getMenorValorAVL() {
        return getMinAVL(raiz);
    }
    
    private Node<E> getMinAVL(Node<E> node) {
        long inicio = System.nanoTime();
        if (node == null) {
            return null; // Caso a árvore esteja vazia
        }
        while (node.getFilhoEsquerdo() != null) {
            node = node.getFilhoEsquerdo(); // Vai para o filho direito até chegar ao final
        }
        long fim = System.nanoTime();
        tempoExecucao += (fim - inicio);
        return node; // Retorna o nó mais à direita (o maior valor)
    }
}
