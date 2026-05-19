package br.com.cefetmg.ag;

public class LangermannIndFactory implements Factory {
    private final int dimensao;
    private final int m;
    private final double[] c;
    private final double[][] a;
    private final double min;
    private final double max;
    private final double alpha;
    private final double txMutacao;
    private final double sigma;

    public LangermannIndFactory(int dimensao) {
        this(dimensao, 5,
                new double[] { 1.0, 2.0, 5.0, 2.0, 3.0 },
                new double[][] {
                        { 3.0, 5.0 },
                        { 5.0, 2.0 },
                        { 2.0, 1.0 },
                        { 1.0, 4.0 },
                        { 7.0, 9.0 }
                },
                0.0, 10.0, 0.5, 0.3, 1.0);
    }

    public LangermannIndFactory(int dimensao, int m, double[] c, double[][] a, double min, double max,
            double alpha, double txMutacao, double sigma) {
        this.dimensao = dimensao;
        this.m = m;
        this.c = c;
        this.a = a;
        this.min = min;
        this.max = max;
        this.alpha = alpha;
        this.txMutacao = txMutacao;
        this.sigma = sigma;
    }

    @Override
    public Individuo getInstance() {
        return new LangermannInd(dimensao, m, c, a, min, max, alpha, txMutacao, sigma);
    }
}
