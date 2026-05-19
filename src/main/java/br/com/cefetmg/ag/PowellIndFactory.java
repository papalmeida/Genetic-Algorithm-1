package br.com.cefetmg.ag;

public class PowellIndFactory implements Factory {
    private final int dimensao;
    private final double min;
    private final double max;
    private final double alpha;
    private final double txMutacao;
    private final double sigma;

    public PowellIndFactory(int dimensao) {
        this(dimensao, -4.0, 5.0, 0.5, 0.3, 0.1 * (5.0 - -4.0));
    }

    public PowellIndFactory(int dimensao, double min, double max, double alpha, double txMutacao, double sigma) {
        this.dimensao = dimensao;
        this.min = min;
        this.max = max;
        this.alpha = alpha;
        this.txMutacao = txMutacao;
        this.sigma = sigma;
    }

    @Override
    public Individuo getInstance() {
        return new PowellInd(dimensao, min, max, alpha, txMutacao, sigma);
    }
}
