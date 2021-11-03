package com.spedison.manipuladorpdf.utils;

import com.spedison.manipuladorpdf.TipoSenha;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.util.Random;

@Data
public class CriadorDeSenhas {


    private int quantidadeNumeros;
    private int quantidadeLetras;
    private int quantidadeSimbolos;

    @Setter(AccessLevel.PRIVATE)
    private StringBuilder senhaGerada;

    @Setter(AccessLevel.PRIVATE)
    private int quantidadeTotal;

    private static final String letrasMaiusculas = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String letrasMinusculas = letrasMaiusculas.toLowerCase();
    private static final char[] letras = (letrasMaiusculas + letrasMinusculas).toCharArray();
    private static final char[] numeros = "0123456789".toCharArray();
    private static final char[] simbolos = "?!@#$%&*()+-*=_".toCharArray();
    private static Random geradorAleatorio = new Random();

    public void definePadraoSenha(TipoSenha tipoSenha) {
        setQuantidadeNumeros(tipoSenha.getQuantidadeNumero());
        setQuantidadeLetras(tipoSenha.getQuantidadeLetras());
        setQuantidadeSimbolos(tipoSenha.getQuantidadeSimbolo());
    }

    private boolean senhaEhSegura() {
        int contaZeros = 0;
        contaZeros += getQuantidadeLetras() == 0 ? 1 : 0;
        contaZeros += getQuantidadeNumeros() == 0 ? 1 : 0;
        contaZeros += getQuantidadeSimbolos() == 0 ? 1 : 0;
        return quantidadeTotal > 7 && contaZeros < 2;
    }

    public boolean gerarSenha() {

        quantidadeTotal = getQuantidadeLetras() + getQuantidadeNumeros() + getQuantidadeSimbolos();

        if (!senhaEhSegura())
            return false;

        StringBuilder senhaTmp = new StringBuilder();
        quantidadeTotal = getQuantidadeLetras() + getQuantidadeNumeros() + getQuantidadeSimbolos();

        for (int i = 0; i < getQuantidadeSimbolos(); i++)
            senhaTmp.append(geraSimbolo());

        for (int i = 0; i < getQuantidadeLetras(); i++)
            senhaTmp.append(geraLetra());

        for (int i = 0; i < getQuantidadeNumeros(); i++)
            senhaTmp.append(geraNumero());

        // Permuta a senha
        senhaGerada = new StringBuilder();
        while (senhaTmp.length() > 0) {
            int pos = geradorAleatorio.nextInt(senhaTmp.length());
            senhaGerada.append(senhaTmp.substring(pos, pos + 1));
            senhaTmp.deleteCharAt(pos);
        }

        return true;
    }

    private Character geraLetra() {
        return letras[geradorAleatorio.nextInt(letras.length)];
    }

    private Character geraNumero() {
        return numeros[geradorAleatorio.nextInt(numeros.length)];
    }

    private Character geraSimbolo() {
        return simbolos[geradorAleatorio.nextInt(simbolos.length)];
    }
}
