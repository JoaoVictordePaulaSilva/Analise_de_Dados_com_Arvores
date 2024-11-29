import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class LeituraMes {
    public static void main(String[] args) {

        String arquivoCSV = "..\\Projeto ED 2 2 versão 7º FINAL MESMO\\DadosMes.csv"; // Nome do arquivo CSV
        System.out.println("Analisando o arquivo: " + arquivoCSV);

        try (BufferedReader br = new BufferedReader(new FileReader(arquivoCSV))) {
            String linha;
            Scanner entry = new Scanner(System.in);

            // Pula as três primeiras linhas (metadados)
            br.readLine();
            br.readLine();
            br.readLine();

            // Lê o cabeçalho para identificar as colunas
            String headerLine = br.readLine();
            if (headerLine == null) {
                System.out.println("O arquivo está vazio ou não contém cabeçalho.");
                return;
            }

            String[] campos = headerLine.split(";");
            // Remove aspas das colunas
            for (int i = 0; i < campos.length; i++) {
                campos[i] = campos[i].replace("\"", "").trim();
            }

            // Solicitar o ano ao usuário
            System.out.print("Digite o ano que deseja analisar: ");
            String ano = entry.nextLine().trim();

            // Encontrar índices das colunas que correspondem ao ano especificado
            List<Integer> indicesAno = new ArrayList<>();
            for (int i = 1; i < campos.length; i++) {
                String date = campos[i];
                if (date.contains(ano)) {
                    indicesAno.add(i);
                }
            }

            if (indicesAno.isEmpty()) {
                System.out.println("Nenhuma coluna correspondente ao ano " + ano + " foi encontrada.");
                return;
            }

            // Lista para armazenar as linhas de dados
            List<String[]> dadosProcedimentos = new ArrayList<>();

            // Processar cada linha de dados e armazenar na lista
            while ((linha = br.readLine()) != null) {
                String[] dados = linha.split(";");

                // Remove aspas e espaços em branco
                for (int i = 0; i < dados.length; i++) {
                    dados[i] = dados[i].replace("\"", "").trim();
                }

                if (dados.length == 0) continue; // Linha vazia

                dadosProcedimentos.add(dados);
            }

            entry.close();

            // Criar arrays de árvores AVL e ABB para cada procedimento
            int numProcedimentos = dadosProcedimentos.size();
            AVL<String>[] avlArvores = new AVL[numProcedimentos];
            ABB<String>[] abbArvores = new ABB[numProcedimentos];

            // Inicializar as árvores
            for (int i = 0; i < numProcedimentos; i++) {
                avlArvores[i] = new AVL<>();
                abbArvores[i] = new ABB<>();
            }

            // Variável para armazenar o total de exames no ano analisado
            long totalExames = 0;

            // Inserir os dados nas árvores e somar os valores
            for (int i = 0; i < numProcedimentos; i++) {
                String[] dados = dadosProcedimentos.get(i);

                // Extrair e inserir os dados das colunas correspondentes ao ano
                for (int idx : indicesAno) {
                    if (idx < dados.length) {
                        String date = campos[idx];
                        String value = dados[idx];

                        // Combinar data e valor para inserção
                        String valor = date + " " + value;

                        abbArvores[i].inserir(valor);
                        avlArvores[i].inserir(valor);

                        // Converter o valor para número e somar ao total
                        try {
                            long numExames = Long.parseLong(value.replaceAll("[^0-9]", ""));
                            totalExames += numExames;
                        } catch (NumberFormatException e) {
                            System.out.println("Valor inválido encontrado: " + value);
                        }
                    }
                }
            }

            // Exibir os resultados
            for (int i = 0; i < numProcedimentos - 3; i++) {
                String[] dados = dadosProcedimentos.get(i);
                String procedimento = dados[0]; // Nome do procedimento

                System.out.println("\nAnalisando o procedimento: " + procedimento);

                AVL<String> avlTree = avlArvores[i];
                ABB<String> abbTree = abbArvores[i];

                // Exibindo os resultados da ABB
                System.out.println("\nResultado da árvore ABB:");

                System.out.println("\nPré-Ordem:");
                abbTree.preOrdem();
                System.out.println("\nEm Ordem:");
                abbTree.emOrdem();
                System.out.println("\nPós-Ordem:");
                abbTree.posOrdem();

                // Exibindo os resultados da AVL
                System.out.println("\nResultado da árvore AVL:");
                avlTree.exibirAVL(); // Supondo que exista um método emOrdem() na classe AVL

                // Exemplo de obtenção de maior e menor valor
                Node<String> maiorValorABB = abbTree.getMaiorValorABB();
                Node<String> menorValorABB = abbTree.getMenorValorABB();
                long TempExecABB = 0;
                if (maiorValorABB != null && menorValorABB != null) {
                    System.out.println("\nMaior valor da árvore ABB: " + maiorValorABB.getElemento());
                    System.out.println("Menor valor da árvore ABB: " + menorValorABB.getElemento());
                    for(int n = 0; n < abbArvores.length; n++){
                        TempExecABB += abbArvores[n].getTempoExecucao();
                    }
                    System.out.println("Tempo de execução total para árvore ABB nanosegundos:" + TempExecABB);
                } else {
                    System.out.println("\nA árvore ABB está vazia.");
                }

                Node<String> maiorValorAVL = avlTree.getMaiorValorAVL();
                Node<String> menorValorAVL = avlTree.getMenorValorAVL();
                long TempExecAVL = 0;
                if (maiorValorAVL != null && menorValorAVL != null) {
                    System.out.println("\nMaior valor da árvore AVL: " + maiorValorAVL.getElemento());
                    System.out.println("Menor valor da árvore AVL: " + menorValorAVL.getElemento());
                    for(int n = 0; n < avlArvores.length; n++){
                        TempExecAVL += avlArvores[n].getTempoExecucao();
                    }
                    System.out.println("Tempo de execução total para árvore AVL em nanosegundos:" + TempExecAVL);
                } else {
                    System.out.println("\nA árvore AVL está vazia.");
                }
                // Exibir o total de exames no ano analisado
            System.out.println("\nTotal de exames no ano " + ano + ": " + totalExames);
            }

            System.out.println("\nPergunta 3:Os números obtidos durante a pandemia são mais altos e/ou preocupantes?\n");
            System.out.println("Observando os dados, podemos ver que desde 2020 os exames já apresentam números muito altos, apresentando quase 200 milhões de exames, e esses números continuaram aumentando com o passar dos anos, batendo quase 300 milhões no ano de 2023. " +
            "Ou seja, com o passar da pandemia os números foram aumentando cada vez mais, deixando a situação um tanto quanto alarmante. Analisando os dados que temos até o periodo de julho deste ano(2024), temos mais uma alarmante quantidade de 200 milhões de exames " +
            "mas considerando que já foi passado mais de metade do ano, podemos esperar um número menor ou ao menos semelhante ao ano de 2023, mostrando uma estabilidade nos casos e uma queda nos exames.\n");

            System.out.println("Pergunta 4:Quais periodos do ano apresentam maior ou menos número de exames ? quais podem ser as causas ?\n");
            System.out.println("Na análise, podemos ver um padrão que segue por todos os anos, os meses do final e do começo do ano costumam ter uma maioria de exames, meses como dezembro e janeiro possuem mais do que o resto do ano, e novembro e fevereiro seguem logo atrás "+ 
            "enquanto abril é o mês com menor número de casos em todos os anos. Podemos assimilar isso a diversos fatores, um deles seria a época, no começo e no final do ano acontecem diversos eventos e comemorações, e nesses eventos muitas pessoas podem viajar se encontrar com outras " +
            " e fazer coisas que normalmente não fazem no resto do ano, gerando doenças que os fariam realizar algum exame. E também podemos assumir que o clima pode gerar isso, com as mudanças das estações e a mudança de clima, muitas pessoas ficam doentes e precisam ser levadas a hospitais" +
            " o que poderia aumentar bastante os casos.");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
