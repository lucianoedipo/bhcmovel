/**
 *  Written by Luciano Édipo Pereira da Silva
 * 	Copyright (C) 2008 Luciano Édipo Pereira da Silva, 2010.
 *
 *   This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package balanco;

import java.io.IOException;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.TextView;

/**
 * @author Luciano
 * 
 */
public class XmlDom {
	/**
	 * Faz a busca dos dados da internet e envia para o método que o insero no
	 * banco de dados
	 * 
	 * @param text
	 * @param iniAtuNet
	 *            dia inicial para a busca na internet
	 * @param fimAtuNet
	 *            dia final para a busca na internet
	 * @param database
	 *            nome do banco de dados
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	public void AbrirXml(TextView text, String iniAtuNet, String fimAtuNet,
			SQLiteDatabase database) throws ParserConfigurationException,
			SAXException, IOException {
		URL url = new URL(
				"http://www.cpao.embrapa.br/clima/android/index2.php?ini="
						+ iniAtuNet + "&fim=" + fimAtuNet);
		text.setText("");
		// Editable texto = (Editable) text.getText();
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document arqNet = db.parse(new InputSource(url.openStream()));

		Element elem = arqNet.getDocumentElement();

		NodeList tag = elem.getElementsByTagName("DIA");

		for (int i = 0; i < tag.getLength(); i++) {

			org.w3c.dom.Element tagDados = (org.w3c.dom.Element) tag.item(i);
			try {
				text.append(InserirClimaNet(database, tagDados
						.getAttribute("id"), tagDados.getAttribute("temp"),
						tagDados.getAttribute("urel"), tagDados
								.getAttribute("vento2"), tagDados
								.getAttribute("radia"), tagDados
								.getAttribute("chuva")));
			} catch (Exception e) {
				// TODO: handle exception
				text.append("Erro ao executar a função InserirClimaNet");
			}

		}
	}

	/**
	 * recebe os dados buscados pela internet e os insera na tabela de clima
	 * 
	 * @param database
	 *            nome do banco de dados
	 * @param dia
	 *            dia de inserção dos dados.
	 * @param temp
	 *            temperatuda média do dia
	 * @param urel
	 *            umidade relativo média do dia
	 * @param vento2
	 *            velocidade média do dia
	 * @param radia
	 *            radiação global do dia
	 * @param chuva
	 *            quantidade de chuva no dia
	 * @return o aviso de inserção, se correto ou ocorrencia de erros.
	 */
	public String InserirClimaNet(SQLiteDatabase database, String dia,
			String temp, String urel, String vento2, String radia, String chuva) {

		String[] col = { "dia" };
		Cursor c = database.query("clima", col, "dia =" + dia, null, null,
				null, null);
		if (c.getCount() == 0) {
			try {
				database
						.execSQL("INSERT INTO clima (dia, temp, urel, vento2, radia, chuva_est) VALUES ('"
								+ dia
								+ "', "
								+ temp
								+ ", "
								+ urel
								+ ", "
								+ vento2 + ", " + radia + ", " + chuva + ")");
				return "Dia: " + dia + " Inserido com Sucesso!\n";
			} catch (Exception e) {
				// TODO: handle exception
				return "Dia: " + dia + " Já Está Inserido!\n";
			}
		} else
			return "Erro na Inserção do dia: " + dia + "\n";
	}
}
