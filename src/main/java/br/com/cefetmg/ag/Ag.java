package br.com.cefetmg.ag;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

public class Ag {
    private static final Random rd = new Random();

    public Individuo executar(Factory factory, int numPopulacao, int numElite, int qtdGeracoes) {
        List<Individuo> populacaoInicial = new ArrayList<>(numPopulacao);
        for (int i = 0; i < numPopulacao; i++) {
            populacaoInicial.add(factory.getInstance());
        }

        for (int geracao = 1; geracao <= qtdGeracoes; geracao++) {
            List<Individuo> filhos = aplicarRecombinacao(populacaoInicial);
            List<Individuo> mutantes = aplicarMutacao(populacaoInicial);

            List<Individuo> todaPopulacao = new ArrayList<>(numPopulacao * 3);
            todaPopulacao.addAll(populacaoInicial);
            todaPopulacao.addAll(filhos);
            todaPopulacao.addAll(mutantes);

            List<Individuo> candidatos = new ArrayList<>(todaPopulacao);
            List<Individuo> novaPopulacao = new ArrayList<>(numPopulacao);
            novaPopulacao.addAll(aplicarElitismo(numElite, candidatos));
            novaPopulacao.addAll(aplicarRoleta(candidatos, numPopulacao - numElite));

            populacaoInicial.clear();
            populacaoInicial.addAll(novaPopulacao);

            Individuo melhor = melhorIndividuo(populacaoInicial);
            imprimirIndividuo(geracao, melhor);

            if (estaOtimizado(melhor)) {
                break;
            }
        }

        return melhorIndividuo(populacaoInicial);
    }

    private List<Individuo> aplicarRecombinacao(List<Individuo> populacao) {
        int tamanhoPopulacao = populacao.size();
        List<Individuo> filhos = new ArrayList<>(tamanhoPopulacao);

        while (filhos.size() < tamanhoPopulacao) {
            Individuo pai1 = populacao.get(rd.nextInt(tamanhoPopulacao));
            Individuo pai2 = populacao.get(rd.nextInt(tamanhoPopulacao));

            List<Individuo> filhosGerados = pai1.recombinar(pai2);
            for (Individuo filho : filhosGerados) {
                if (filhos.size() < tamanhoPopulacao) {
                    filhos.add(filho);
                }
            }
        }

        return filhos;
    }

    private List<Individuo> aplicarMutacao(List<Individuo> populacao) {
        List<Individuo> mutantes = new ArrayList<>(populacao.size());

        for (Individuo individuo : populacao) {
            mutantes.add(individuo.mutar());
        }
        return mutantes;
    }

    private List<Individuo> aplicarElitismo(int numElite, List<Individuo> individuos) {
        List<Individuo> ordenados = new ArrayList<>(individuos);
        ordenados.sort(obterComparador(individuos.get(0).isMaximizacao()));

        List<Individuo> elite = new ArrayList<>(Math.min(numElite, ordenados.size()));
        for (int i = 0; i < numElite && i < ordenados.size(); i++) {
            Individuo escolhido = ordenados.get(i);
            elite.add(escolhido);
            individuos.remove(escolhido);
        }

        return elite;
    }

    private List<Individuo> aplicarRoleta(List<Individuo> individuos, int quantidade) {
        List<Individuo> selecionados = new ArrayList<>(quantidade);

        while (selecionados.size() < quantidade && !individuos.isEmpty()) {
            double somaPesos = 0.0;
            for (Individuo individuo : individuos) {
                somaPesos += obterPesoRoleta(individuo);
            }

            double alvo = rd.nextDouble() * somaPesos;
            double acumulado = 0.0;
            int indiceEscolhido = individuos.size() - 1;

            for (int i = 0; i < individuos.size(); i++) {
                acumulado += obterPesoRoleta(individuos.get(i));
                if (acumulado >= alvo) {
                    indiceEscolhido = i;
                    break;
                }
            }

            selecionados.add(individuos.remove(indiceEscolhido));
        }

        return selecionados;
    }

    private Individuo melhorIndividuo(List<Individuo> individuos) {
        if (individuos.isEmpty()) {
            return null;
        }

        Comparator<Individuo> comparador = obterComparador(individuos.get(0).isMaximizacao());
        Individuo melhor = individuos.get(0);

        for (int i = 1; i < individuos.size(); i++) {
            if (comparador.compare(individuos.get(i), melhor) < 0) {
                melhor = individuos.get(i);
            }
        }

        return melhor;
    }

    private boolean estaOtimizado(Individuo melhor) {
        if (melhor == null) {
            return false;
        }

        if (melhor.isMaximizacao()) {
            return false;
        }

        return melhor.getAvaliacao() == 0.0;
    }

    private void imprimirIndividuo(int geracao, Individuo melhor) {
        if (melhor == null) {
            return;
        }

        System.out.println("Geracao " + geracao + " | Melhor: " + Arrays.toString(melhor.getGenes()) + " | Avaliacao: " + melhor.getAvaliacao());
    }

    private Comparator<Individuo> obterComparador(boolean isMaximizacao) {
        if (isMaximizacao) {
            return (a, b) -> Double.compare(b.getAvaliacao(), a.getAvaliacao());
        }
        return Comparator.comparingDouble(Individuo::getAvaliacao);
    }

    private double obterPesoRoleta(Individuo individuo) {
        double avaliacao = individuo.getAvaliacao();

        if (individuo.isMaximizacao()) {
            return Math.max(0.0, avaliacao);
        }
        double peso = 1.0 / (1.0 + Math.max(0.0, avaliacao));

        return peso;
    }
}
