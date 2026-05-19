package br.com.cefetmg.ag;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LangermannInd implements Individuo {
    private static final Random rd = new Random();

    private final int dimensao;
    private final int m;
    private final double[] c;
    private final double[][] a;
    private final double min;
    private final double max;
    private final double alpha;
    private final double txMutacao;
    private final double sigma;
    private final double[] genes;

    public LangermannInd(int dimensao, int m, double[] c, double[][] a, double min, double max,
            double alpha, double txMutacao, double sigma) {
        validarParametros(dimensao, m, c, a);
        this.dimensao = dimensao;
        this.m = m;
        this.c = c.clone();
        this.a = copiarMatriz(a);
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

    public LangermannInd(int dimensao, int m, double[] c, double[][] a, double min, double max,
            double alpha, double txMutacao, double sigma, double[] genes) {
        validarParametros(dimensao, m, c, a);
        this.dimensao = dimensao;
        this.m = m;
        this.c = c.clone();
        this.a = copiarMatriz(a);
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
            double a1 = genes[i];
            double b1 = genesOutro[i];
            double minAB = Math.min(a1, b1);
            double maxAB = Math.max(a1, b1);
            double intervalo = maxAB - minAB;

            double low = minAB - alpha * intervalo;
            double high = maxAB + alpha * intervalo;

            filho1[i] = clamp(randomInRange(low, high), min, max);
            filho2[i] = clamp(randomInRange(low, high), min, max);
        }

        List<Individuo> filhos = new ArrayList<>(2);
        filhos.add(new LangermannInd(dimensao, m, c, a, min, max, alpha, txMutacao, sigma, filho1));
        filhos.add(new LangermannInd(dimensao, m, c, a, min, max, alpha, txMutacao, sigma, filho2));
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
        return new LangermannInd(dimensao, m, c, a, min, max, alpha, txMutacao, sigma, mutantes);
    }

    @Override
    public double getAvaliacao() {
        double soma = 0.0;
        for (int i = 0; i < m; i++) {
            double sum = 0.0;
            for (int j = 0; j < dimensao; j++) {
                double diff = genes[j] - a[i][j];
                sum += diff * diff;
            }
            soma += c[i] * Math.exp(-(1.0 / Math.PI) * sum) * Math.cos(Math.PI * sum);
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

    private static void validarParametros(int dimensao, int m, double[] c, double[][] a) {
        if (dimensao <= 0) {
            throw new IllegalArgumentException("Dimensão deve ser positiva");
        }
        if (m <= 0) {
            throw new IllegalArgumentException("m deve ser positivo");
        }
        if (c == null || c.length != m) {
            throw new IllegalArgumentException("c deve ter tamanho m");
        }
        if (a == null || a.length != m) {
            throw new IllegalArgumentException("A deve ter m linhas");
        }
        for (int i = 0; i < m; i++) {
            if (a[i] == null || a[i].length != dimensao) {
                throw new IllegalArgumentException("A deve ter d colunas");
            }
        }
    }

    private static double[][] copiarMatriz(double[][] a) {
        double[][] copia = new double[a.length][];
        for (int i = 0; i < a.length; i++) {
            copia[i] = a[i].clone();
        }
        return copia;
    }

    private static double randomInRange(double min, double max) {
        return min + rd.nextDouble() * (max - min);
    }

    private static double clamp(double valor, double min, double max) {
        return Math.max(min, Math.min(max, valor));
    }
}
