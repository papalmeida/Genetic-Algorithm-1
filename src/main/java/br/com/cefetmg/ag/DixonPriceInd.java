package br.com.cefetmg.ag;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DixonPriceInd implements Individuo {
    private static final Random rd = new Random();

    private final int dimensao;
    private final double min;
    private final double max;
    private final double alpha;
    private final double txMutacao;
    private final double sigma;
    private final double[] genes;

    public DixonPriceInd(int dimensao, double min, double max, double alpha, double txMutacao, double sigma) {
        if (dimensao <= 0) {
            throw new IllegalArgumentException("Dimensão deve ser positiva");
        }
        this.dimensao = dimensao;
        this.min = min;
        this.max = max;
        this.alpha = alpha;
        this.txMutacao = txMutacao;
        this.sigma = sigma;
        this.genes = new double[dimensao];

        for (int i = 0; i < dimensao; i++) {
            genes[i] = randomInRange(min, max);
        }
    }

    public DixonPriceInd(int dimensao, double min, double max, double alpha, double txMutacao, double sigma, double[] genes) {
        if (dimensao <= 0) {
            throw new IllegalArgumentException("Dimensão deve ser positiva");
        }
        this.dimensao = dimensao;
        this.min = min;
        this.max = max;
        this.alpha = alpha;
        this.txMutacao = txMutacao;
        this.sigma = sigma;
        this.genes = genes.clone();
    }

    @Override
    public List<Individuo> recombinar(Individuo outro) {
        double[] genesOutro = outro.getGenes();
        double[] filho1 = new double[dimensao];
        double[] filho2 = new double[dimensao];

        for (int i = 0; i < dimensao; i++) {
            double a = genes[i];
            double b = genesOutro[i];
            double minAB = Math.min(a, b);
            double maxAB = Math.max(a, b);
            double intervalo = maxAB - minAB;

            double low = minAB - alpha * intervalo;
            double high = maxAB + alpha * intervalo;

            filho1[i] = clamp(randomInRange(low, high), min, max);
            filho2[i] = clamp(randomInRange(low, high), min, max);
        }

        List<Individuo> filhos = new ArrayList<>(2);
        filhos.add(new DixonPriceInd(dimensao, min, max, alpha, txMutacao, sigma, filho1));
        filhos.add(new DixonPriceInd(dimensao, min, max, alpha, txMutacao, sigma, filho2));
        return filhos;
    }

    @Override
    public Individuo mutar() {
        double[] mutantes = genes.clone();
        for (int i = 0; i < mutantes.length; i++) {
            if (rd.nextDouble() < txMutacao) {
                mutantes[i] = clamp(mutantes[i] + rd.nextGaussian() * sigma, min, max);
            }
        }
        return new DixonPriceInd(dimensao, min, max, alpha, txMutacao, sigma, mutantes);
    }

    @Override
    public double getAvaliacao() {
        double soma = (genes[0] - 1.0) * (genes[0] - 1.0);
        for (int i = 1; i < dimensao; i++) {
            double xi = genes[i];
            double xPrev = genes[i - 1];
            double termo = 2.0 * xi * xi - xPrev;
            soma += (i + 1) * (termo * termo);
        }
        return soma;
    }

    @Override
    public boolean isMaximizacao() {
        return false;
    }

    @Override
    public double[] getGenes() {
        return genes.clone();
    }

    private static double randomInRange(double min, double max) {
        return min + rd.nextDouble() * (max - min);
    }

    private static double clamp(double valor, double min, double max) {
        return Math.max(min, Math.min(max, valor));
    }
}
