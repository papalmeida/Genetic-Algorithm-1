package br.com.cefetmg.ag;

import java.util.List;

public interface Individuo {
    List<Individuo> recombinar(Individuo outro);

    Individuo mutar();

    double getAvaliacao();

    boolean isMaximizacao();

    double[] getGenes();
}
