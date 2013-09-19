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
package administrar;

import bhcmovel.balanco.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

/**
 * @author Luciano
 * 
 */
public class CadastroSolo extends Activity {

	public EditText nome, esp, cc, pm, ds;
	public Button bttCad;
	private SQLiteDatabase bancoDados;


	/**
	 * 
	 */
	public void onCreate(Bundle bundle) {
		// TODO Auto-generated constructor stub
		super.onCreate(bundle);
		setContentView(R.layout.cadsolo);
		nome = (EditText) findViewById(R.id.EditCadSoloNome);
		esp = (EditText) findViewById(R.id.EditCadSoloEsp);
		cc = (EditText) findViewById(R.id.EditCadSoloCC);
		pm = (EditText) findViewById(R.id.EditCadSoloPM);
		ds = (EditText) findViewById(R.id.EditCadSoloDS);
		bttCad = (Button) findViewById(R.id.BttCadSoloCad);
		bttCad.setOnClickListener(onClickCadSolo);
	}

	private OnClickListener onClickCadSolo = new Button.OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			CadastrarSolo();
		}
	};

	/**
	 * Cadastra os valores informados nos campos na tabela de solos.
	 */
	public void CadastrarSolo() {

		if (nome.getText().toString().equalsIgnoreCase("")) {
			showAlert("Informe um nome para o solo!", "Erro");
			nome.requestFocus();
		} else if (esp.getText().toString().length() == 0) {
			showAlert("Informe a espessura do solo!", "Erro");
			esp.requestFocus();
		} else if (cc.getText().toString().length() == 0) {
			showAlert("Informe a capacidade de campo do solo!", "Erro");
			cc.requestFocus();
		} else if (pm.getText().toString().length() == 0) {
			showAlert("Informe o ponto de murcha do solo!", "Erro");
			pm.requestFocus();
		} else if (ds.getText().toString().length() == 0) {
			showAlert("Informe a densidade do solo!", "Erro");
			ds.requestFocus();
		} else {
			bancoDados = openOrCreateDatabase("bhDataBase",
					MODE_WORLD_WRITEABLE, null);
			bancoDados
					.execSQL("INSERT INTO solo (nome, espessura, cc, pm, ds) VALUES ('"
							+ nome.getText().toString()
							+ "', '"
							+ esp.getText().toString()
							+ "', '"
							+ cc.getText().toString()
							+ "', '"
							+ pm.getText().toString()
							+ "', '"
							+ ds.getText().toString() + "')");
			showAlert("Solo cadastrado com sucesso!", "Confirmação");
		}
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
		builder.setNeutralButton("OK", onClickDialogEsc);
		builder.setIcon(R.drawable.icon);
		AlertDialog dialog = builder.create();
		dialog.setTitle(titulo);
		dialog.show();
	};

	private DialogInterface.OnClickListener onClickDialogEsc = new DialogInterface.OnClickListener() {

		@Override
		public void onClick(DialogInterface dialog, int which) {
			// TODO Auto-generated method stub
			finish();
		}
	};
}
