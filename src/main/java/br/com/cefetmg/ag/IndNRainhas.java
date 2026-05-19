package br.com.cefetmg.ag;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class IndNRainhas implements Individuo {
    private double txMutacao = 0.3;
    private double[] genes;
    private int qtdGenes;
    private boolean maximizacao = false;
    private static final Random rd = new Random();

    public IndNRainhas(int qtdGenes) {
        this.genes = new double[qtdGenes];
        for(int i =0; i < qtdGenes; i++) {
            genes[i] = rd.nextInt(0, qtdGenes);
        }
        this.qtdGenes = qtdGenes;
    }

    public IndNRainhas(int qtdGenes, double[] genes) {
        this.qtdGenes = qtdGenes;
        this.genes = genes.clone();
    }

    @Override
    public List<Individuo> recombinar(Individuo outro) {
        List<Individuo> filhos = new ArrayList<>(2);
        int corte = rd.nextInt(1, qtdGenes - 1);

        double[] genesFilho1 = new double[qtdGenes];
        double[] genesFilho2 = new double[qtdGenes];

        double[] genesOutro = outro.getGenes();

        for (int i = 0; i < qtdGenes; i++) {
            if (i < corte) {
                genesFilho1[i] = genes[i];
                genesFilho2[i] = genesOutro[i];
            } else {
                genesFilho1[i] = genesOutro[i];
                genesFilho2[i] = genes[i];
            }
        }

        filhos.add(new IndNRainhas(qtdGenes, genesFilho1));
        filhos.add(new IndNRainhas(qtdGenes, genesFilho2));

        return filhos;
    }

    @Override
    public Individuo mutar() {
        IndNRainhas mutante = new IndNRainhas(qtdGenes, genes);

        for (int i = 0; i < genes.length; i++) {
            if (rd.nextDouble() < txMutacao) {
                mutante.genes[i] = rd.nextInt(0, qtdGenes);
            }
        }

        return mutante;
    }

    @Override
    public double getAvaliacao() {
        int colisoes = 0;

        for (int i = 0; i < genes.length; i++) {
            for (int j = i + 1; j < genes.length; j++) {
                int gi = (int) genes[i];
                int gj = (int) genes[j];
                if (gi == gj) {
                    colisoes++;
                } else if (Math.abs(gi - gj) == Math.abs(i - j)) {
                    colisoes++;
                }
            }
        }
        
        return colisoes;
    }

    @Override
    public boolean isMaximizacao() {
        return maximizacao;
    }

    @Override
    public double[] getGenes() {
        return genes.clone();
    }
}
