package br.com.cefetmg.ag;

public class AgMain {
    public static void main(String[] args) throws Exception {
        //! Dados para o problema das N rainhas
        // IndNRainhasFactory factory = new IndNRainhasFactory(10);
        // int numPopulacao = 4;
        // int numElite = 1;
        // int numGeracoes = 10000;
        
        //! Dados para o problema de otimização de Powell
        int dimensao = 4; // deve ser múltiplo de 4
        int numPopulacao = 20;
        int numElite = 4;
        int numGeracoes = 2000;

        PowellIndFactory factory = new PowellIndFactory(dimensao);
        Ag algoritmoGenetico = new Ag();
        algoritmoGenetico.executar(factory, numPopulacao, numElite, numGeracoes);
    }
}
