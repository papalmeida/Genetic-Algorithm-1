package br.com.cefetmg.ag;

public class AgMain {
    public static void main(String[] args) throws Exception {
        //* Dados para o problema das N rainhas
        // int qtdGenes = 8;
        
        //* Dados para o problema de otimizacao de Powell
        // int dimensao = 4; //! Deve ser múltiplo de 4
        
        //* Dados para o problema de otimizacao de Langermann
        // int dimensao = 2; //! Deve ser 2 para Langermann

        //* Dados para o problema de otimizacao de Dixon-Price
        int dimensao = 10;

        // * Dados comuns
        int numPopulacao = 20;
        int numElite = 4;
        int numGeracoes = 2000;

        // IndNRainhasFactory factory = new IndNRainhasFactory(qtdGenes);
        // PowellIndFactory factory = new PowellIndFactory(dimensao);
        // LangermannIndFactory factory = new LangermannIndFactory(dimensao);
        DixonPriceIndFactory factory = new DixonPriceIndFactory(dimensao);
        Ag algoritmoGenetico = new Ag();
        algoritmoGenetico.executar(factory, numPopulacao, numElite, numGeracoes);
    }
}
