package com.spedison.manipuladorpdf;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.HelpFormatter;

import java.util.Arrays;

/*** Ponto de entrada para a aplicação.
 */
public final class Main {

    private Main() {

    }

    /*** Lista de opções na linha de comando.
     */
    private static Options opcoesDefinidasNaLinhaDeComando =
            new Options();

    /*** Parser para extrair os dados da linha de comando.
     */
    private static CommandLineParser parserDaLinhaDeComando =
            new DefaultParser();

    /*** Representa a Linha "parseada".
     */
    private static CommandLine linhaDeComando = null;
    /*** Parâmetros extraídos para criptografar os pdfs.
     */
    private static DadosParaCriptografarPdf dadosParaCriptografia;

    /*** Inicializa estruturas de parse da linha de comando.
     * Faz o parse e verifica se é consistente.
     *
     * @param args Argumentos da linha de comando.
     * @return true: Extraído com sucesso
     *         false: Problemas na extração.
     */
    static boolean inicializaOptions(final String[] args) {
        // add h option
        opcoesDefinidasNaLinhaDeComando.addOption("h", false, "Mostra Ajuda");

        opcoesDefinidasNaLinhaDeComando.addOption("c", "cript", false,
                "Coloca senha no PDF");

        opcoesDefinidasNaLinhaDeComando.addOption("s", "sum", false,
                "Soma arquivos PDF");

        opcoesDefinidasNaLinhaDeComando.addOption("ao", "arquivo-original",
                true, "Arquivo(s) PDF de entrada");

        opcoesDefinidasNaLinhaDeComando.addOption("as", "arquivo-saida", true,
                "Arquivo PDF de destino, se não definir será "
                        + "<arquivo_de_entrada>_out.pdf");

        opcoesDefinidasNaLinhaDeComando.addOption("su", "senha-usuario", true,
                "Senha de usuário do arquivo, se não usar, será criado");

        opcoesDefinidasNaLinhaDeComando.addOption("sd", "senha-dono", true,
                "Senha de dono do arquivo, se não usar, será criado");

        opcoesDefinidasNaLinhaDeComando.addOption("ts", "tipo-senha", true,
                "Tipo de Senha gerada : "
                        + "(p) Padrão, (f) Forte, (mf) Muito Forte");

        linhaDeComando = null;
        try {
            linhaDeComando = parserDaLinhaDeComando.
                    parse(opcoesDefinidasNaLinhaDeComando, args);
            return true;
        } catch (UnrecognizedOptionException e) {
            imprimeHelp();
        } catch (ParseException e) {
            imprimeHelp();
        }
        return false;
    }

    /*** Ponto de entrada da aplicação.
     * @param args Argumentos da linha de comando.
     */
    public static void main(final String[] args) {


        Arrays.stream(args).map(s->"parametro :: %s".formatted(s)).forEach(System.out::println);

        if (!inicializaOptions(args)) {
            System.exit(0);
        }

        if (imprimeHelp(true)) {
            System.exit(0);
        }

        if (linhaDeComando.getOptions()[0].getOpt().equals("s")) {
            ArquivosParaSomar aps = new ArquivosParaSomar();
            aps.processaComandLine(linhaDeComando);
            SomadorDeArquivos sda = new SomadorDeArquivos();
            sda.somaArquivos(aps);
            System.out.println("Processamento terminado");
        }

        if (linhaDeComando.getOptions()[0].getOpt().equals("c")) {
            dadosParaCriptografia = new DadosParaCriptografarPdf();

            extraiDadosParaCriptografia();

            if (MainCriptografa.processaCriptografia(
                    Main.dadosParaCriptografia)) {
                System.out.println("Processamento realizado com sucesso.");
                System.out.printf(
                        "Lendo arquivo : [%s] e gravando no arquivo [%s]\n",
                        dadosParaCriptografia.getArquivoEntrada(),
                        dadosParaCriptografia.getArquivoSaida());
                System.out.printf(
                        "Senhas usadas usuário [%s] - dono [%s]\n",
                        dadosParaCriptografia.getSenhaUsuario(),
                        dadosParaCriptografia.getSenhaDono());
            }

            System.out.println(dadosParaCriptografia);
            System.exit(1);
        }
    }

    /*** Extrai dados do parse coloca na estrutura para processamento.
     *
     */
    private static void extraiDadosParaCriptografia() {
        for (int i = 1; i < linhaDeComando.getOptions().length; i++) {

            if (linhaDeComando.getOptions()[i].getOpt().equals("su")) {
                dadosParaCriptografia.setSenhaUsuario(
                        linhaDeComando.getOptions()[i].getValue());
            }

            if (linhaDeComando.getOptions()[i].getOpt().equals("sd")) {
                dadosParaCriptografia.setSenhaDono(
                        linhaDeComando.getOptions()[i].getValue());
            }

            if (linhaDeComando.getOptions()[i].getOpt().equals("ao")) {
                dadosParaCriptografia.setArquivoEntrada(
                        linhaDeComando.getOptions()[i].getValue());
            }

            if (linhaDeComando.getOptions()[i].getOpt().equals("as")) {
                dadosParaCriptografia.setArquivoSaida(
                        linhaDeComando.getOptions()[i].getValue());
            }

            if (linhaDeComando.getOptions()[i].getOpt().equals("ts")) {
                String parametro = linhaDeComando.getOptions()[i].
                        getValue().toLowerCase();

                switch (parametro) {
                    case "p":
                        dadosParaCriptografia.setTipoSenha(
                                TipoSenha.PADRAO);
                        break;
                    case "f":
                        dadosParaCriptografia.setTipoSenha(
                                TipoSenha.FORTE);
                        break;
                    case "mf":
                        dadosParaCriptografia.setTipoSenha(
                                TipoSenha.MUITO_FORTE);
                        break;
                    default:
                        System.out.println("Parâmetro não definido : "
                                + parametro);
                        break;
                }
            }
        }
    }

    private static void imprimeHelp() {
        imprimeHelp(false);
    }

    private static boolean imprimeHelp(final boolean verificaComandLine) {
        if (!verificaComandLine
                || linhaDeComando.getOptions().length == 0
                || (
                linhaDeComando.getOptions().length == 1
                        && linhaDeComando.getOptions()[0].getOpt().equals("h")
        )) {
            // automatically generate the help statement
            HelpFormatter formatter = new HelpFormatter();

            formatter.printHelp("java -jar manipulador-de-pdf  ",
                    opcoesDefinidasNaLinhaDeComando);

            return true;
        }
        return false;
    }
}
