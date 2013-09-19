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
import java.util.Calendar;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import bhcmovel.balanco.R;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

/**
 * @author Luciano
 * 
 */
public class AtualizaNet extends Activity {

	public TextView text;
	public EditText iniAtuNet;
	public EditText fimAtuNet;
	public Button ok;
	public XmlDom xmldom;
	public SQLiteDatabase database;

	static final int DATE_INI = 0;

	public int dia, mes, ano;

	/**
	 * 
	 */
	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.atualizanet);

		text = (TextView) findViewById(R.id.Dias);
		text.setText(text.getText(), TextView.BufferType.EDITABLE);
		iniAtuNet = (EditText) findViewById(R.id.IniAtuNet);
		iniAtuNet.setOnClickListener(seClickDataIni);

		fimAtuNet = (EditText) findViewById(R.id.FimAtuNet);

		ok = (Button) findViewById(R.id.BttAtuNet);
		ok.setOnClickListener(seClicarAtuNet);

		final Calendar c = Calendar.getInstance();
		ano = c.get(Calendar.YEAR);
		mes = c.get(Calendar.MONTH);
		dia = c.get(Calendar.DAY_OF_MONTH);
	}

	private OnClickListener seClicarAtuNet = new Button.OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub

			try {
				database = openOrCreateDatabase("bhDataBase",
						MODE_WORLD_WRITEABLE, null);

			} catch (Exception e) {
				// TODO: handle exception
				text.setText("Problemas em Abrir o Banco");
			}

			xmldom = new XmlDom();
			try {
				xmldom.AbrirXml(text, iniAtuNet.getText().toString(), fimAtuNet
						.getText().toString(), database);
			} catch (ParserConfigurationException e) {
				// TODO Auto-generated catch block
				text.setText("ParserConfigurationException");
			} catch (SAXException e) {
				// TODO Auto-generated catch block
				text.setText("SAXException");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				text.setText("IOException");
			}

		}
	};

	private OnClickListener seClickDataIni = new EditText.OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub

			showDialog(DATE_INI);

		}

	};

	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DATE_INI:
			return new DatePickerDialog(this, mDateSetListener, ano, mes, dia);

		}
		return null;
	}

	protected void onPrepareDialog(int id, Dialog dialog) {
		switch (id) {
		case DATE_INI:
			((DatePickerDialog) dialog).updateDate(ano, mes, dia);
			break;

		}
	}

	private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {

		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			ano = year;
			mes = monthOfYear;
			dia = dayOfMonth;
			updateDisplay();
		}
	};

	private void updateDisplay() {
		mes = mes + 1;
		String udia, umes;
		if (dia <= 9)
			udia = "0" + dia;
		else
			udia = "" + dia;
		if (mes <= 9)
			umes = "0" + mes;
		else
			umes = "" + mes;

		iniAtuNet.setText(new StringBuilder()
		// Month is 0 based so add 1
				.append(udia).append("/").append(umes).append("/").append(ano));
	}

}
