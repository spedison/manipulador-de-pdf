package com.spedison.manipuladorpdf;


import com.spedison.manipuladorpdf.utils.CriadorDeSenhas;

import java.io.File;
import java.io.IOException;

import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.multipdf.PDFCloneUtility;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;


import org.apache.pdfbox.pdmodel.encryption.AccessPermission;
import org.apache.pdfbox.pdmodel.encryption.StandardProtectionPolicy;

/*** Classe que representa o Ponto de entrada para o
 * Processamento de pdfs para adicionar senhas a arquivos.
 */
public final class MainCriptografa {

    private MainCriptografa() {

    }

    /*** Tamanho chave usada para criptografar pdf, de 40 bits.
     */
    private static final int CHAVE_40 = 40;
    /*** Tamanho chave usada para criptografar pdf, de 128 bits.
     */
    private static final int CHAVE_128 = 128;
    /*** Tamanho chave usada para criptografar pdf, de 256 bits.
     */
    private static final int CHAVE_256 = 256;

    /*** Realiza a criptografia de um arquivo pdf.
     *
     * @param dadosParaCriptografarPdf Dados usados para criptografia.
     * @return True, se a senha sugerida for minimamente forte,
     * caso contrário retorna false.
     */
    public static boolean processaCriptografia(
            final DadosParaCriptografarPdf dadosParaCriptografarPdf) {

        if (dadosParaCriptografarPdf.getArquivoSaida() == null) {
            dadosParaCriptografarPdf.setArquivoSaida(
                    dadosParaCriptografarPdf.
                            getArquivoEntrada().
                            replaceFirst(".pdf",
                                    "_cifrado.pdf"));
        }

        File arquivoSaida = new File(
                dadosParaCriptografarPdf.
                        getArquivoSaida());

        CriadorDeSenhas criadorDeSenhas = new CriadorDeSenhas();
        criadorDeSenhas.definePadraoSenha(
                dadosParaCriptografarPdf.getTipoSenha());

        if (dadosParaCriptografarPdf.getSenhaUsuario() == null
                || dadosParaCriptografarPdf.
                getSenhaUsuario().trim().isEmpty()) {
            criadorDeSenhas.gerarSenha();
            dadosParaCriptografarPdf.setSenhaUsuario(
                    criadorDeSenhas.getSenhaGerada().toString());
        }

        if (dadosParaCriptografarPdf.getSenhaDono() == null
                || dadosParaCriptografarPdf.getSenhaDono().trim().isEmpty()) {
            criadorDeSenhas.gerarSenha();
            dadosParaCriptografarPdf.setSenhaDono(
                    criadorDeSenhas.getSenhaGerada().toString());
        }

        File arquivoEntrada = new File(
                dadosParaCriptografarPdf.getArquivoEntrada());

        if (!(arquivoEntrada.exists()
                && arquivoEntrada.isFile()
                && arquivoEntrada.canRead())) {

            System.out.println(
                    "Arquivo " + arquivoEntrada
                            + " não existe ou não tem acesso de leitura.");
            return false;
        }

        try (PDDocument documentoOriginal = PDDocument.load(arquivoEntrada)) {

            try (PDDocument documentoSaida = new PDDocument()) {

                AccessPermission ap = new AccessPermission();

                ap.setCanPrint(false);
                ap.setCanModify(false);
                ap.setCanPrintDegraded(false);
                ap.setCanExtractContent(false);

                StandardProtectionPolicy spp = new StandardProtectionPolicy(
                        dadosParaCriptografarPdf.getSenhaUsuario(),
                        dadosParaCriptografarPdf.getSenhaDono(),
                        ap);
                spp.setEncryptionKeyLength(CHAVE_256);
                spp.setPermissions(ap);
                documentoSaida.protect(spp);
                System.out.println(spp);

                documentoSaida.getDocument().setVersion(
                        documentoOriginal.
                                getDocument().
                                getVersion());

                documentoSaida.setDocumentInformation(
                        documentoOriginal.
                                getDocumentInformation());

                documentoSaida.getDocumentCatalog().
                        setViewerPreferences(
                                documentoOriginal.
                                        getDocumentCatalog().
                                        getViewerPreferences());

                PDFCloneUtility cloner = new PDFCloneUtility(documentoSaida);

                for (PDPage originalPage : documentoOriginal.getPages()) {
                    COSDictionary pageDictionary =
                            (COSDictionary) cloner.
                                    cloneForNewDocument(originalPage);
                    PDPage page = new PDPage(pageDictionary);
                    documentoSaida.addPage(page);
                }

                documentoSaida.save(arquivoSaida);
                documentoSaida.close();
                return true;
            } catch (IOException e) {
                System.out.println(
                        "Problemas ao ler Arquivo :"
                                + dadosParaCriptografarPdf.getArquivoEntrada());
                return false;
            }

        } catch (IOException e) {
            System.out.println(
                    "Problemas ao ler Arquivo :"
                            + dadosParaCriptografarPdf.getArquivoEntrada());
            return false;
        }
    }
}
