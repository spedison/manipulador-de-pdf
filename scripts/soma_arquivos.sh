#!/bin/bash
export JAR_DIR=/opt/pdf

arquivos=""

i=0;
for arquivo in "$@"
do
    arquivos=$arquivos" -ao \""$arquivo"\" "
    echo "Arquivos Atual :: " $arquivos
    i=$((i + 1));
done

echo "Uniremos " $i " arquivos."

$JAVA_HOME/bin/java -jar $JAR_DIR/manipulador-de-pdf.jar -s $arquivos -as "./saida.pdf"