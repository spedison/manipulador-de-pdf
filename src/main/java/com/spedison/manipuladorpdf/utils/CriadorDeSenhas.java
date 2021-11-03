package com.spedison.manipuladorpdf.utils;

import com.spedison.manipuladorpdf.TipoSenha;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.util.Random;

@Data
public class CriadorDeSenhas {


    /*** Quantidade de números na senha gerada.
     */
    private int quantidadeNumeros;
    /*** Quantidade de letras (sem acentros) na senha gerada.
     */
    private int quantidadeLetras;
    /*** Quantidade de simbolos na senha gerada.
     */
    private int quantidadeSimbolos;

    /**
     * Senha gerada. (somente para leitura)
     */
    @Setter(AccessLevel.PRIVATE)
    private StringBuilder senhaGerada;

    /*** Retorna a quantidade total de caracteres da senha que será gerada.
     * @return a tamanho total da senha que será gerada.
     */
    public int getQuantidadeTotal() {
        return getQuantidadeLetras() + geraNumero() + getQuantidadeSimbolos();
    }

    /*** Quais caracteres são considerados letras maiusculas.
     */
    private static final
    String LETRAS_MAIUSCULAS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    /*** Quais caracteres são letras minusculas, baseado nas maiúscilas.
     */
    private static final
    String LETRAS_MINUSCULAS = LETRAS_MAIUSCULAS.toLowerCase();

    /*** Caracteres considerados letras. (Independente do caixa alta ou baixa.
     */
    private static final
    char[] LETRAS = (LETRAS_MAIUSCULAS + LETRAS_MINUSCULAS).toCharArray();

    /*** Caracteres que são números.
     */
    private static final
    char[] NUMEROS = "0123456789".toCharArray();

    /*** Caracteres usadas como símbolos ou caracteres especiais nas senhas.
     */
    private static final char[] SIMBOLOS = "?!@#$%&*()+-*=_".toCharArray();


    /*** Tamanho mínimo da senha.
     */
    private static final int MINIMO_TAMANHO_SENHA = 7;

    /*** Quantidade máxima de tipos que podem ser zerados.
     */
    private static final int MAXIMO_TIPOS_ZERADOS = 1;

    /*** gerador usados para sortear e misturar os caracteres da senha.
     */
    private static Random geradorAleatorio = new Random();

    /*** Define tipo de senha que será gerada, baseada na enum TipoSenha.
     * @param tipoSenha Qual tipo de senha que será gerada.
     */
    public void definePadraoSenha(final TipoSenha tipoSenha) {
        setQuantidadeNumeros(tipoSenha.getQuantidadeNumero());
        setQuantidadeLetras(tipoSenha.getQuantidadeLetras());
        setQuantidadeSimbolos(tipoSenha.getQuantidadeSimbolo());
    }

    private boolean senhaEhSegura() {
        int contaZeros = 0;
        int tamanhoTotal = getQuantidadeTotal();
        contaZeros += getQuantidadeLetras() == 0 ? 1 : 0;
        contaZeros += getQuantidadeNumeros() == 0 ? 1 : 0;
        contaZeros += getQuantidadeSimbolos() == 0 ? 1 : 0;

        return tamanhoTotal > MINIMO_TAMANHO_SENHA
                && contaZeros <= MAXIMO_TIPOS_ZERADOS;
    }

    /*** Pede para gerar senha e a guarda na variável chamada senhaGerada.
     *
     * @return retorna true se os parâmetros da senha estão bons
     *         e false se a senha for muito fraca.
     */
    public boolean gerarSenha() {

        if (!senhaEhSegura()) {
            return false;
        }

        StringBuilder senhaTmp = new StringBuilder();

        for (int i = 0; i < getQuantidadeSimbolos(); i++) {
            senhaTmp.append(geraSimbolo());
        }

        for (int i = 0; i < getQuantidadeLetras(); i++) {
            senhaTmp.append(geraLetra());
        }

        for (int i = 0; i < getQuantidadeNumeros(); i++) {
            senhaTmp.append(geraNumero());
        }

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
        return LETRAS[geradorAleatorio.nextInt(LETRAS.length)];
    }

    private Character geraNumero() {
        return NUMEROS[geradorAleatorio.nextInt(NUMEROS.length)];
    }

    private Character geraSimbolo() {
        return SIMBOLOS[geradorAleatorio.nextInt(SIMBOLOS.length)];
    }
}
