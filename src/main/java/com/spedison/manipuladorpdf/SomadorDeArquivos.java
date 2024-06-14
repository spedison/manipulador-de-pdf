package com.spedison.manipuladorpdf;

import org.apache.pdfbox.io.MemoryUsageSetting;
import org.apache.pdfbox.multipdf.PDFMergerUtility;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class SomadorDeArquivos {

    public void somaArquivos(ArquivosParaSomar sps){
        PDFMergerUtility ut = new PDFMergerUtility();
        ut.setDestinationFileName(sps.getArquivoSaida().getAbsolutePath());
        System.out.println("Arquivo de saida criado :" + sps.getArquivoSaida().getAbsolutePath());

        try {
            for (File f: sps ) {
                ut.addSource(f);
                System.out.println("Adicionando o arquivo " + f.getAbsolutePath());
            }
            ut.mergeDocuments();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}