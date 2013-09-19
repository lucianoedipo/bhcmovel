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

import java.util.Calendar;

import bhcmovel.balanco.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

/**
 * @author Luciano Édipo Pereira da Silva
 * 
 */
public class AtualizaManual extends Activity {
	public final int DIA = 0;
	public int ano, mes, dia;
	public SQLiteDatabase bd;
	public TextView txtPrimDia, txtUltiDia, txtChuvaDia;
	public EditText EdtAtuDia, EdtAtuChuva;
	public Button bttOk;

	/**
 * 
 */
	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.atualizaman);
		zerar();
		bd = openOrCreateDatabase("bhDataBase", MODE_WORLD_WRITEABLE, null);
		txtPrimDia = (TextView) findViewById(R.id.TxtPrimDiaMan);
		txtUltiDia = (TextView) findViewById(R.id.TxtUltDiaMan);
		txtChuvaDia = (TextView) findViewById(R.id.txtAtuManChuvaNet);
		EdtAtuDia = (EditText) findViewById(R.id.EdtAtuManDia);
		EdtAtuChuva = (EditText) findViewById(R.id.EdtAtuManChuva);
		bttOk = (Button) findViewById(R.id.bttAtuManOk);
		bttOk.setEnabled(false);

		EdtAtuDia.setOnClickListener(seClickDataIni);
		bttOk.setOnClickListener(seClickOk);

		txtPrimDia.setText(txtPrimDia.getText().toString()
				+ BalancoHidrico.DateMysqlToDateBr(BalancoHidrico.PrimDia(bd)));
		txtUltiDia.setText(txtUltiDia.getText().toString()
				+ BalancoHidrico.DateMysqlToDateBr(BalancoHidrico.UltDia(bd)));
	}

	/**
	 * Ação do botão Data inicial, se clicado mostra caixa de dialogo de seleção
	 * de data.
	 */
	private OnClickListener seClickDataIni = new EditText.OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			showDialog(DIA);
		}
	};
	/**
	 * Ação do botão Ok, se clicado chama o método que insere a chuva no dia
	 * selecionado.
	 */
	private OnClickListener seClickOk = new EditText.OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			InsereChuvaManual();
		}
	};

	/**
	 * Ao criar uma caixa de dialogo, verificar qual o tipo e acionar o método
	 * adequado. neste caso seleção de dia.
	 * 
	 * @param id
	 *            identificação do dia.
	 * @return instância da caixa de dialogo.
	 */
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DIA:
			return new DatePickerDialog(this, mDateSetListenerIni, ano, mes,
					dia);
		}
		return null;
	}

	/**
	 * Prepara a caixa de diálogo, iniciando com as datas do dia atual.
	 * 
	 * @param id
	 *            identificação do tipo da caixa de dialogo.
	 * @param dialog
	 *            objeto da caixa de dialogo.
	 * 
	 */
	protected void onPrepareDialog(int id, Dialog dialog) {
		switch (id) {
		case DIA:
			((DatePickerDialog) dialog).updateDate(ano, mes, dia);
			break;
		}
	}

	private DatePickerDialog.OnDateSetListener mDateSetListenerIni = new DatePickerDialog.OnDateSetListener() {

		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			ano = year;
			mes = monthOfYear;
			dia = dayOfMonth;
			updateDisplay();
		}
	};

	/**
	 * atualiza os campos de datas com a data escolhida pelo componente de
	 * datadialog
	 * 
	 * @param type
	 *            identifica se é data final ou inicial.
	 */
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

		EdtAtuDia.setText(new StringBuilder().append(udia).append("/").append(
				umes).append("/").append(ano));
		zerar();
		try {
			txtChuvaDia.setText("A Chuva na Estação neste dia foi de: "
					+ PegarChuvaDoDia(BalancoHidrico
							.DateBrToDateMysql(EdtAtuDia.getText().toString()))
					+ "(mm)");
			bttOk.setEnabled(true);
		} catch (Exception e) {
			// TODO: handle exception
			showAlert("Data não existe no banco de dados", "Erro");
		}
	}

	/**
	 * Zera as datas iniciais do calendario, assim mostrando sempre a data do
	 * dia atual no datapick
	 */
	public void zerar() {
		final Calendar c = Calendar.getInstance();
		ano = c.get(Calendar.YEAR);
		mes = c.get(Calendar.MONTH);
		dia = c.get(Calendar.DAY_OF_MONTH);
	}

	/**
	 * 
	 * @param dia
	 *            dia o qual se quer saber a chuva da estação
	 * @return a chuva ocorrida neste dia.
	 */
	public float PegarChuvaDoDia(String dia) {
		Cursor c;
		c = bd.rawQuery(
				"SELECT chuva_est FROM clima WHERE dia = '" + dia + "'", null);
		c.moveToFirst();
		return c.getFloat(0);
	}

	/**
	 * Mostra um alerta
	 * 
	 * @param msgerro
	 *            mensagem de erro
	 * @param titulo
	 *            título do dialogo da mensagem de erro
	 */
	public void showAlert(String msgerro, String titulo) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(msgerro);
		builder.setNeutralButton("OK", null);
		builder.setIcon(R.drawable.icon);
		AlertDialog dialog = builder.create();
		dialog.setTitle(titulo);

		dialog.show();
	};

	public void InsereChuvaManual() {

		try {

			bd.execSQL("UPDATE clima SET chuva_local = "
					+ EdtAtuChuva.getText().toString()
					+ " WHERE dia = '"
					+ BalancoHidrico.DateBrToDateMysql(EdtAtuDia.getText()
							.toString()) + "' ");

			showAlert("Dia atualizado com sucesso!", "Aviso");

		} catch (Exception e) {
			// TODO: handle exception
			showAlert("Valor de chuva invalido", "Erro");
		}

	}
}
