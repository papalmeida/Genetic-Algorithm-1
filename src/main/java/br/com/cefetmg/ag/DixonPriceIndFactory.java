package br.com.cefetmg.ag;

public class DixonPriceIndFactory implements Factory {
    private final int dimensao;
    private final double min;
    private final double max;
    private final double alpha;
    private final double txMutacao;
    private final double sigma;

    public DixonPriceIndFactory(int dimensao) {
        this(dimensao, -10.0, 10.0, 0.5, 0.3, 0.1 * (10.0 - -10.0));
    }

    public DixonPriceIndFactory(int dimensao, double min, double max, double alpha, double txMutacao, double sigma) {
        this.dimensao = dimensao;
        this.min = min;
        this.max = max;
        this.alpha = alpha;
        this.txMutacao = txMutacao;
        this.sigma = sigma;
    }

    @Override
    public Individuo getInstance() {
        return new DixonPriceInd(dimensao, min, max, alpha, txMutacao, sigma);
    }
}
