package com.spedison.manipuladorpdf;

import lombok.AllArgsConstructor;
import lombok.Getter;

/***
 * Enumeração que define as características da senha gerada.
 */
@Getter
@AllArgsConstructor
public enum TipoSenha {

    /*** Tipo de senha padrão.
     * (com 8 letras, 6 números e 2 caracteres especiais).
     */
    PADRAO(8, 6, 2),

    /*** Tipo de senha forte.
     * (com 12 letras, 8 números e 4 caracteres especiais).
     */
    FORTE(12, 8, 4),

    /*** Tipo de senha muito forte.
     * (com 15 letras, 12 números e 10 caracteres especiais).
     */
    MUITO_FORTE(15, 12, 10);

    /*** Quantidade de letras usadas na criação de senhas.
     */
    private int quantidadeLetras;
    /*** Quantidade de números usadas na criação de senhas.
     */
    private int quantidadeNumero;
    /*** Quantidade de simbolos usadas na criação de senhas.
     */
    private int quantidadeSimbolo;
}
