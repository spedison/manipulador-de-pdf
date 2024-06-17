echo "Estou no diretório" $PWD
sudo mkdir /opt/pdf
sudo cp -v ../target/manipulador-de-pdf.jar /opt/pdf/.
sudo cp -v -R ../target/lib /opt/pdf/.
sudo cp -v ./soma_arquivos.sh /bin/.
sudo chmod -v a+x /bin/soma_arquivos.sh