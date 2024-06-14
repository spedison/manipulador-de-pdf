package com.spedison.manipuladorpdf;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@Getter
@Setter
public class ArquivosParaSomar extends LinkedList<File> {

    private File arquivoSaida;

    public boolean add(String file) {
        return super.add(new File(file));
    }

    public void processaComandLine(CommandLine cmd){
        Arrays
                .stream(cmd.getOptions())
                .filter(c -> c.getOpt().equalsIgnoreCase("ao"))
                .map(Option::getValue)
                .forEach(this::add);

        this.setArquivoSaida(new File(cmd.getOptionValue("as")));
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("ArquivosParaSomar { ");
        sb.append(" \n   arquivoSaida = ").append(arquivoSaida);
        sb.append(" \n   lista = ").append(super.toString());
        sb.append(" \n}");
        return sb.toString();
    }
}
