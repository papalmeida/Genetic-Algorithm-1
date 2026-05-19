package br.com.cefetmg.ag;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PowellInd implements Individuo {
    private static final Random rd = new Random();

    private final int dimensao;
    private final double min;
    private final double max;
    private final double alpha;
    private final double txMutacao;
    private final double sigma;
    private final double[] genes;

    public PowellInd(int dimensao, double min, double max, double alpha, double txMutacao, double sigma) {
        if (dimensao % 4 != 0) {
            throw new IllegalArgumentException("Dimensao deve ser múltiplo de 4");
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

    public PowellInd(int dimensao, double min, double max, double alpha, double txMutacao, double sigma, double[] genes) {
        if (dimensao % 4 != 0) {
            throw new IllegalArgumentException("Dimensao deve ser múltiplo de 4");
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
        filhos.add(new PowellInd(dimensao, min, max, alpha, txMutacao, sigma, filho1));
        filhos.add(new PowellInd(dimensao, min, max, alpha, txMutacao, sigma, filho2));
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
        return new PowellInd(dimensao, min, max, alpha, txMutacao, sigma, mutantes);
    }

    @Override
    public double getAvaliacao() {
        double soma = 0.0;
        for (int i = 0; i < dimensao; i += 4) {
            double x1 = genes[i];
            double x2 = genes[i + 1];
            double x3 = genes[i + 2];
            double x4 = genes[i + 3];

            double t1 = x1 + (10.0 * x2);
            double t2 = x3 - x4;
            double t3 = x2 - (2.0 * x3);
            double t4 = x1 - x4;

            soma += (t1 * t1) + (5.0 * (t2 * t2)) + Math.pow(t3, 4.0) + (10.0 * Math.pow(t4, 4.0));
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
