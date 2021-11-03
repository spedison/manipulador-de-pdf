package com.spedison.manipuladorpdf;

import lombok.Data;

/*** Classe de dados para processamento do PDF.
 * Parâmetro obrigatório é o nome do arquivo de entrada.
 */
@Data
public class DadosParaCriptografarPdf {
    /*** Nome do arquivo de entrada.
     */
    private String arquivoEntrada = null;
    /*** Nome do arquivo de saída.
     */
    private String arquivoSaida = null;
    /*** Senha de usuário utilizada (se null, será gerado).
     */
    private String senhaUsuario = null;
    /*** Senha de dono do arquivo.
     * Com essa senha é possível abrir o arquivo para edição.
     */
    private String senhaDono = null;
    /*** Tipo de senha utilizada.
     *  Esse tipo está relacionado com o tamanha da senha.
     *  Tipos:
     *  PADRAO, FORTE, MUITO_FORTE.
     */
    private TipoSenha tipoSenha = TipoSenha.PADRAO;
}
