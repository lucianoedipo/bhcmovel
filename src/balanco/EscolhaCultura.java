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
import administrar.CadastroCultura;
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
 * 
 * @author Luciano
 * 
 */
public class EscolhaCultura extends ListActivity {
	public String[] StrCultura;
	public int[] IdsCulturas;
	public String Id_Solo;
	public Intent intSelPeriodo, IttCadCult;
	public SQLiteDatabase dataBase;

	/**
	 * 
	 */
	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		intSelPeriodo = new Intent(this, SelecionaPeriodo.class);
		IttCadCult = new Intent(this, CadastroCultura.class);

		CarregaLista();
		if (StrCultura.length != 0) {
			setListAdapter(new ArrayAdapter<String>(this,
					android.R.layout.simple_list_item_1, StrCultura));
			setTitle("Selecione a Cultura:");
			Id_Solo = getIntent().getExtras().getString("id_solo");
			getListView().setOnItemClickListener(listener);
		} else {
			setListAdapter(new ArrayAdapter<String>(this,
					android.R.layout.simple_list_item_1, StrCultura));
			setTitle("Selecione a Cultura:");
			Id_Solo = getIntent().getExtras().getString("id_solo");
			getListView().setOnItemClickListener(listener);
			showAlert("Não existe nenhum cultura cadastrado!", "Sem Cultura");
		}

	}

	public void onRestart() {
		super.onRestart();
		finish();

	}

	/**
	 * 
	 * @return A quantidade de culura cadastrada no banco de dados do
	 *         aplicativo.
	 */
	public int qtdeCulturas() {
		Cursor query;
		dataBase = openOrCreateDatabase("bhDataBase", MODE_WORLD_READABLE, null);
		query = dataBase.rawQuery("SELECT count(*) FROM cultura", null);
		query.moveToFirst();
		return query.getInt(0);
	}

	/**
	 * Busca todas as culturas cadastradas no banco de dado e lista no vetor de
	 * nomes de culturas.
	 */
	public void CarregaLista() {
		String[] str_cult = new String[qtdeCulturas()];
		int[] ids_cult = new int[qtdeCulturas()];
		Cursor query;
		dataBase = openOrCreateDatabase("bhDataBase", MODE_WORLD_READABLE, null);
		query = dataBase.rawQuery(
				"SELECT * FROM cultura ORDER BY id_cultura ASC", null);
		query.moveToFirst();
		for (int i = 0; i < str_cult.length; i++) {
			str_cult[i] = query.getString(0) + " - " + query.getString(1);
			ids_cult[i] = query.getInt(0);
			query.moveToNext();
		}
		StrCultura = str_cult;
		IdsCulturas = ids_cult;
		// dataBase.close();
	}

	private OnItemClickListener listener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub
			dataBase.close();

			intSelPeriodo.putExtra("id_solo", Id_Solo);
			intSelPeriodo.putExtra("id_cult", IdsCulturas[arg2]);
			startActivity(intSelPeriodo);
		}
	};

	public void showAlert(String msgerro, String titulo) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(msgerro);
		builder.setNeutralButton("CADASTRE", onClickErroCult);
		builder.setIcon(R.drawable.icon);
		AlertDialog dialog = builder.create();
		dialog.setTitle(titulo);
		dialog.show();
	};

	private DialogInterface.OnClickListener onClickErroCult = new DialogInterface.OnClickListener() {

		@Override
		public void onClick(DialogInterface dialog, int which) {
			// TODO Auto-generated method stub

			startActivity(IttCadCult);
		}
	};

}
