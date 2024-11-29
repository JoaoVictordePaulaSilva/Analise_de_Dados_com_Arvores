import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LeituraIdade {
    public static void main(String[] args) {

        String arquivoCSV = "..\\Projeto ED 2 2 versão 7º FINAL MESMO\\DadosIdade.csv"; // Nome do arquivo CSV
        System.out.println("Analisando o arquivo: " + arquivoCSV);

        try (BufferedReader br = new BufferedReader(new FileReader(arquivoCSV))) {
            String linha;
            List<String> linhas = new ArrayList<>();

            // Pula para a linha de colunas
            br.readLine();
            br.readLine();
            br.readLine();

            // Lê todas as linhas do arquivo
            while ((linha = br.readLine()) != null) {
                linhas.add(linha);
            }

            // Verifica se o arquivo possui pelo menos 3 linhas para serem ignoradas
            if (linhas.size() > 3) {
                // Remove as últimas 3 linhas do arquivo
                linhas = linhas.subList(0, linhas.size() - 4);
            }

            // Lê o cabeçalho para identificar as colunas
            if (!linhas.isEmpty()) {
                String[] campos = linhas.get(0).split(";");

                System.out.println("Colunas encontradas no arquivo:");
                for (int i = 1; i < campos.length - 1; i++) {
                    System.out.println(i + ": " + campos[i]);
                }

                // Criando árvores para cada coluna
                AVL<String>[] avlArvores = new AVL[campos.length];
                ABB<String>[] abbArvores = new ABB[campos.length];

                for (int i = 0; i < campos.length; i++) {
                    avlArvores[i] = new AVL<>();
                    abbArvores[i] = new ABB<>();
                }

                // Processando os dados
                for (int j = 1; j < linhas.size(); j++) {
                    String[] dados = linhas.get(j).split(";");

                    for (int i = 1; i < campos.length - 1; i++) {
                        if (i < dados.length) {
                            String valor = dados[0] + " " + dados[i];
                            abbArvores[i].inserir(valor);
                            avlArvores[i].inserir(valor);
                        }
                    }
                }

                // Exibindo os resultados
                for (int i = 1; i < campos.length - 1; i++) {
                    System.out.println("\nAnalisando a coluna: " + campos[i]);

                    // Exibindo os resultados da ABB
                    System.out.println("\nResultado da árvore ABB:");
                    System.out.println("\nPré-Ordem:");
                    abbArvores[i].preOrdem();
                    System.out.println("\nEm Ordem:");
                    abbArvores[i].emOrdem();
                    System.out.println("\nPós-Ordem:");
                    abbArvores[i].posOrdem();

                    Node<String> maiorValorABB = abbArvores[i].getMaiorValorABB();
                    Node<String> menorValorABB = abbArvores[i].getMenorValorABB();
                    long TempExecABB = 0;
                    if (maiorValorABB != null) {
                        System.out.println("\nMaior valor da árvore ABB: " + maiorValorABB.getElemento());
                        System.out.println("Menor valor da árvore ABB: " + menorValorABB.getElemento());
                        for(int n = 0; n < abbArvores.length; n++){
                            TempExecABB += abbArvores[n].getTempoExecucao();
                        }
                        System.out.println("Tempo de execução total para árvore ABB nanosegundos:" + TempExecABB);
                    } else {
                        System.out.println("\nA árvore ABB está vazia.");
                    }

                    // Exibindo os resultados da AVL
                    System.out.println("\nResultado da árvore AVL:");
                    avlArvores[i].exibirAVL();

                    Node<String> maiorValorAVL = avlArvores[i].getMaiorValorAVL();
                    Node<String> menorValorAVL = avlArvores[i].getMenorValorAVL();
                    long TempExecAVL = 0;
                    if (maiorValorAVL != null) {
                        System.out.println("\nMaior valor da árvore AVL: " + maiorValorAVL.getElementoAVL());
                        System.out.println("Menor valor da árvore AVL: " + menorValorAVL.getElementoAVL());
                        for(int n = 0; n < avlArvores.length; n++){
                            TempExecAVL += avlArvores[n].getTempoExecucao();
                        }
                        System.out.println("Tempo de execução total para árvore AVL em nanosegundos:" + TempExecAVL);
                    } else {
                        System.out.println("\nA árvore AVL está vazia.");
                    }
                }

                System.out.println("\nPergunta 1: Com base nos dados de saúde pública, quais faixas etárias registraram o maior número de exames realizados nos últimos anos? Quais fatores podem estar contribuindo para esse padrão observado?");
                System.out.println("Observando os dados, constatamos que as faixas etárias de 60 a 64 anos e de 65 a 69 anos se destacam significativamente no número de exames realizados." + 
                "Podemos inferir que indivíduos nesses grupos etários mais avançados realizam exames com maior regularidade devido a problemas de saúde relacionados ao envelhecimento," +
                "especialmente doenças cardiovasculares, que são o foco principal dos exames analisados. Além disso, é plausível afirmar que a frequência das visitas médicas aumenta nessa fase da vida devido à necessidade de" +
                "monitoramento contínuo de condições crônicas e ao seguimento de recomendações médicas para prevenção e detecção precoce de possíveis complicações de saúde.");

                System.out.println("\nPergunta 2: Com base nos dados de saúde pública, quais faixas etárias registraram o menor número de exames realizados nos últimos anos? Quais fatores podem estar contribuindo para esse padrão observado?");
                System.out.println("Analisando os dados do SUS, observa-se que a faixa etária com o menor número de exames realizados é a de pessoas com mais de 80 anos. Esse fenômeno pode ser atribuído a diversos fatores." + 
                "Primeiramente, indivíduos nessa faixa etária podem enfrentar limitações físicas ou cognitivas que dificultam o acesso aos serviços de saúde, como problemas de mobilidade ou dependência de cuidadores." + 
                "Além disso, há a possibilidade de que médicos e familiares optem por um manejo mais conservador, evitando exames e intervenções que possam não trazer benefícios significativos devido à fragilidade associada à idade avançada." + 
                "Questões como menor expectativa de vida, comorbidades complexas e prioridades voltadas para a qualidade de vida em vez de procedimentos diagnósticos também podem influenciar na menor frequência de exames realizados nessa população.");

            } else {
                System.out.println("O arquivo está vazio.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
