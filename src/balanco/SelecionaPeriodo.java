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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import bhcmovel.balanco.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
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
public class SelecionaPeriodo extends Activity {
	static final int DATE_INI = 0;
	static final int DATE_FIM = 1;
	public Intent intCalcOK, ittColeta;
	public int dia, mes, ano, Id_cult;
	public String Id_solo;
	public TextView txtcultsel, txtprimdia, txtultdia, txtsolosel;
	public EditText EdtDataIni, EdtDataFim;
	public Button BttCalcOk;
	public SQLiteDatabase bd;

	/**
	 * 
	 */
	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setTitle("Entre com o Período:");
		intCalcOK = new Intent(this, CalcularBH.class);
		ittColeta = new Intent(this, AtualizarBD.class);
		setContentView(R.layout.selperiodo);
		final Calendar c = Calendar.getInstance();
		ano = c.get(Calendar.YEAR);
		mes = c.get(Calendar.MONTH);
		dia = c.get(Calendar.DAY_OF_MONTH);
		try {
			bd = openOrCreateDatabase("bhDataBase", MODE_WORLD_READABLE, null);
		} catch (Exception e) {
			// TODO: handle exception
			showMessage("Erro ao ler banco de dados", "ERRO!");
		}
		Id_cult = getIntent().getExtras().getInt("id_cult");
		Id_solo = getIntent().getExtras().getString("id_solo");

		txtcultsel = (TextView) findViewById(R.id.TxtCultSel);
		txtsolosel = (TextView) findViewById(R.id.TxtSoloSel);
		txtprimdia = (TextView) findViewById(R.id.TxtPrimDia);
		txtultdia = (TextView) findViewById(R.id.TxtUltDia);
		EdtDataFim = (EditText) findViewById(R.id.EdtSelPerFim);
		// EdtDataFim.setText("2009-08-08");
		// EdtDataIni.setText("2009-01-01");
		EdtDataIni = (EditText) findViewById(R.id.EdtSelPerIni);
		BttCalcOk = (Button) findViewById(R.id.BttSelPerOk);

		EdtDataIni.setOnClickListener(seClickDataIni);
		EdtDataFim.setOnClickListener(seClickDataFim);
		BttCalcOk.setOnClickListener(seClickCalcOk);

		txtcultsel.setText(txtcultsel.getText() + " " + NomeCultura(Id_cult));
		txtsolosel.setText(txtsolosel.getText().toString() + " " + Id_solo);
		try {
			txtprimdia.setText(txtprimdia.getText()
					+ " "
					+ BalancoHidrico.DateMysqlToDateBr(BalancoHidrico
							.PrimDia(bd)));
			txtultdia.setText(txtultdia.getText()
					+ " "
					+ BalancoHidrico.DateMysqlToDateBr(BalancoHidrico
							.UltDia(bd)));
		} catch (Exception e) {
			// TODO: handle exception
			showAlert("Não Existe dados climáticos", "Sem Dados Climáticos");
		}

	}

	public void onRestart() {
		super.onRestart();
		finish();
	}

	/**
	 * 
	 * @param id_cult
	 *            identificação da cultura
	 * @return o nome da cultura, cadastrada com a identificação fornacida.
	 */
	public String NomeCultura(int id_cult) {
		Cursor query;
		query = bd.rawQuery(
				"SELECT nome_cultura FROM cultura WHERE id_cultura = '"
						+ id_cult + "'", null);
		query.moveToFirst();
		return query.getString(0);

	}

	/**
	 * Verifica se a data inicial inserida está entre as datas que existem no
	 * banco de dados.
	 * 
	 * @return se estiver entre as datas cadastradas retorna true, senão mostra
	 *         uma mensagem de erro correspondente e retorna false.
	 */
	public boolean VerificaDataInicial() {
		Integer dia_d = 0, mes_d = 0, ano_d = 0, dia_b = 0, mes_b = 0, ano_b = 0, dia_u = 0, mes_u = 0, ano_u = 0;
		Date data_d, data_b, data_u;
		String dia_primeiro, dia_ultimo;
		dia_d = Integer.parseInt(EdtDataIni.getText().toString()
				.substring(0, 2));

		mes_d = Integer.parseInt(EdtDataIni.getText().toString()
				.substring(3, 5));
		ano_d = Integer.parseInt(EdtDataIni.getText().toString().substring(6,
				10));
		dia_primeiro = BalancoHidrico.PrimDia(bd);
		ano_b = Integer.parseInt(dia_primeiro.substring(0, 4));
		mes_b = Integer.parseInt(dia_primeiro.substring(5, 7));
		dia_b = Integer.parseInt(dia_primeiro.substring(8, 10));

		dia_ultimo = BalancoHidrico.UltDia(bd);
		ano_u = Integer.parseInt(dia_ultimo.substring(0, 4));
		mes_u = Integer.parseInt(dia_ultimo.substring(5, 7));
		dia_u = Integer.parseInt(dia_ultimo.substring(8, 10));

		data_d = new Date(ano_d - 1900, mes_d - 1, dia_d);
		data_b = new Date(ano_b - 1900, mes_b - 1, dia_b);
		data_u = new Date(ano_u - 1900, mes_u - 1, dia_u);
		if (data_d.before(data_b)) {// se a data digitada for anterior a do
			// banco

			showMessage("A data inicial não está inserida no celular",
					"Atenção");
			return false;
		} else {
			if (data_d.after(data_u)) {
				showMessage(
						"A data inicial não pode ser maior que a última inserida no programa",
						"Atenção");
				return false;
			} else {
				return true;
			}
		}

	}

	private OnClickListener seClickDataIni = new EditText.OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			showDialog(DATE_INI);
		}
	};

	private OnClickListener seClickCalcOk = new EditText.OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			String formato = "yyyy-MM-dd";
			SimpleDateFormat dateFormat = new SimpleDateFormat(formato);
			if (!EdtDataIni.getText().toString().equalsIgnoreCase("")) {
				intCalcOK.putExtra("id_solo", Id_solo);
				intCalcOK.putExtra("id_cult", Id_cult);
				intCalcOK.putExtra("dia_ini", BalancoHidrico
						.DateBrToDateMysql(EdtDataIni.getText().toString()));
				if (EdtDataFim.getText().toString().equalsIgnoreCase("")) {
					intCalcOK
							.putExtra("dia_fim", dateFormat.format(new Date()));
				} else {
					intCalcOK
							.putExtra("dia_fim", BalancoHidrico
									.DateBrToDateMysql(EdtDataFim.getText()
											.toString()));
				}
				Log.v(BalancoHidrico.CATEGORIA, "SEL DT FIM:"
						+ dateFormat.format(new Date()));
				if (VerificaDataInicial()) {
					startActivity(intCalcOK);
				}

			} else {
				showMessage("A data inicial deve ser inserida!", "Atenção");
			}
		}
	};

	private OnClickListener seClickDataFim = new EditText.OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			showDialog(DATE_FIM);
		}
	};

	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DATE_INI:
			return new DatePickerDialog(this, mDateSetListenerIni, ano, mes,
					dia);
		case DATE_FIM:
			return new DatePickerDialog(this, mDateSetListenerFim, ano, mes,
					dia);
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

	private DatePickerDialog.OnDateSetListener mDateSetListenerIni = new DatePickerDialog.OnDateSetListener() {

		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			ano = year;
			mes = monthOfYear;
			dia = dayOfMonth;
			updateDisplay(DATE_INI);
		}
	};

	private DatePickerDialog.OnDateSetListener mDateSetListenerFim = new DatePickerDialog.OnDateSetListener() {

		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			ano = year;
			mes = monthOfYear;
			dia = dayOfMonth;
			updateDisplay(DATE_FIM);
		}
	};

	/**
	 * atualiza os campos de datas com a data escolhida pelo componente de
	 * datadialog
	 * 
	 * @param type
	 *            identifica se é data final ou inicial.
	 */
	private void updateDisplay(int type) {
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
		if (type == DATE_INI)
			EdtDataIni.setText(new StringBuilder().append(udia).append("/")
					.append(umes).append("/").append(ano));
		if (type == DATE_FIM)
			EdtDataFim.setText(new StringBuilder().append(udia).append("/")
					.append(umes).append("/").append(ano));
	}

	/**
	 * Mostra caixa de dialogo.
	 * 
	 * @param msgerro
	 *            mensagem de erro
	 * @param titulo
	 *            título do dialogo da mensagem de erro
	 */
	public void showMessage(String msgerro, String titulo) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(msgerro);
		builder.setNeutralButton("OK", onClickDialogOk);
		builder.setNegativeButton("SAIR", onClickDialogEsc);
		AlertDialog dialog = builder.create();
		dialog.setTitle(titulo);
		dialog.show();
	};

	public void showAlert(String msgerro, String titulo) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(msgerro);
		builder.setNeutralButton("COLETAR", onClickDialogColeta);
		AlertDialog dialog = builder.create();
		dialog.setTitle(titulo);
		dialog.show();
	};

	private DialogInterface.OnClickListener onClickDialogOk = new DialogInterface.OnClickListener() {

		@Override
		public void onClick(DialogInterface dialog, int which) {
			// TODO Auto-generated method stub
			setTitle("Cliquei OK!");
		}
	};

	private DialogInterface.OnClickListener onClickDialogEsc = new DialogInterface.OnClickListener() {

		@Override
		public void onClick(DialogInterface dialog, int which) {
			// TODO Auto-generated method stub
			setTitle("Cliquei CANCEL!");
		}
	};

	private DialogInterface.OnClickListener onClickDialogColeta = new DialogInterface.OnClickListener() {

		@Override
		public void onClick(DialogInterface dialog, int which) {
			// TODO Auto-generated method stub
			startActivity(ittColeta);
		}
	};

}
