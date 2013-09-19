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

import bhcmovel.balanco.R;
import administrar.CadastroSolo;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AdapterView.OnItemClickListener;

/**
 * @author Luciano
 * 
 */
public class EscolheSolo extends ListActivity {
	public String[] StrSolo;
	public Intent intEscCult, ittCadSolo;
	public SQLiteDatabase dataBase;

	/**
	 * 
	 */
	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setTitle("Selecione o Solo:");
		intEscCult = new Intent(this, EscolhaCultura.class);
		ittCadSolo = new Intent(this, CadastroSolo.class);
		CarregaLista();
		if (StrSolo.length != 0) {
			setListAdapter(new ArrayAdapter<String>(this,
					android.R.layout.simple_list_item_1, StrSolo));
			getListView().setOnItemClickListener(listener);
		} else {
			setListAdapter(new ArrayAdapter<String>(this,
					android.R.layout.simple_list_item_1, StrSolo));
			getListView().setOnItemClickListener(listener);
			showAlert("Não existe nenhum solo cadastrado", "Sem Solo");

		}
	}

	public void onRestart() {
		super.onRestart();
		finish();

	}

	/**
	 * Busca no número de solos cadastrados no banco de dados.
	 * 
	 * @return a quantidade de solos cadastrados no banco.
	 */
	public int qtdeSolo() {
		Cursor query;
		dataBase = openOrCreateDatabase("bhDataBase", MODE_WORLD_READABLE, null);
		query = dataBase.rawQuery("SELECT count(*) FROM solo", null);
		query.moveToFirst();
		return query.getInt(0);
	}

	/**
	 * Busca os solos cadastrados no banco de dados e lista no vetor de nomes de
	 * solo.
	 */
	public void CarregaLista() {
		String[] str_solo = new String[qtdeSolo()];
		Cursor query;
		dataBase = openOrCreateDatabase("bhDataBase", MODE_WORLD_READABLE, null);
		query = dataBase.rawQuery("SELECT * FROM solo", null);

		query.moveToFirst();
		for (int i = 0; i < str_solo.length; i++) {
			str_solo[i] = query.getString(0);
			query.moveToNext();
		}
		StrSolo = str_solo;

	}

	private OnItemClickListener listener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub
			intEscCult.putExtra("id_solo", StrSolo[arg2]);
			dataBase.close();
			startActivity(intEscCult);

		}
	};

	public void showAlert(String msgerro, String titulo) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(msgerro);
		builder.setNeutralButton("CADASTRE", onClickErroSolo);
		builder.setIcon(R.drawable.icon);
		AlertDialog dialog = builder.create();
		dialog.setTitle(titulo);
		dialog.show();
	};

	private DialogInterface.OnClickListener onClickErroSolo = new DialogInterface.OnClickListener() {

		@Override
		public void onClick(DialogInterface dialog, int which) {
			// TODO Auto-generated method stub

			startActivity(ittCadSolo);
		}
	};

}
