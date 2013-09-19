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
import android.database.Cursor;
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
public class CadastroCultura extends Activity {
	public SQLiteDatabase banco;
	public EditText nome;
	public Button fase[];
	public EditText duracao[], kc[];
	public Button bttCadCult;

	// public OnClickListener clickListener[];

	/**
	 * 
	 */
	public void onCreate(Bundle bundle) {
		// TODO Auto-generated constructor stub
		super.onCreate(bundle);
		setContentView(R.layout.cadcultura);

		fase = new Button[5];
		duracao = new EditText[6];
		kc = new EditText[5];
		// clickListener = new OnClickListener[5];

		nome = (EditText) findViewById(R.id.EditCadCulturaNome);
		fase[0] = (Button) findViewById(R.id.EditCadCulturaFase1);
		fase[0].setOnClickListener(clickListener0);

		fase[1] = (Button) findViewById(R.id.EditCadCulturaFase2);
		fase[1].setOnClickListener(clickListener1);

		fase[2] = (Button) findViewById(R.id.EditCadCulturaFase3);
		fase[2].setOnClickListener(clickListener2);

		fase[3] = (Button) findViewById(R.id.EditCadCulturaFase4);
		fase[3].setOnClickListener(clickListener3);

		fase[4] = (Button) findViewById(R.id.EditCadCulturaFase5);
		fase[4].setOnClickListener(clickListener4);

		duracao[0] = (EditText) findViewById(R.id.EditCadCulturaDur1);
		duracao[1] = (EditText) findViewById(R.id.EditCadCulturaDur2);
		duracao[2] = (EditText) findViewById(R.id.EditCadCulturaDur3);
		duracao[3] = (EditText) findViewById(R.id.EditCadCulturaDur4);
		duracao[4] = (EditText) findViewById(R.id.EditCadCulturaDur5);
		duracao[5] = new EditText(this);
		duracao[5].setEnabled(false);
		kc[0] = (EditText) findViewById(R.id.EditCadCulturaKC1);
		kc[1] = (EditText) findViewById(R.id.EditCadCulturaKC2);
		kc[2] = (EditText) findViewById(R.id.EditCadCulturaKC3);
		kc[3] = (EditText) findViewById(R.id.EditCadCulturaKC4);
		kc[4] = (EditText) findViewById(R.id.EditCadCulturaKC5);
		for (int i = 0; i < 5; i++) {
			duracao[i].setEnabled(false);
			kc[i].setEnabled(false);
		}
		bttCadCult = (Button) findViewById(R.id.BttCadCulturaCad);

		bttCadCult.setOnClickListener(CadCultura);
		setTitle(" " + GetUltimoIdCult());

	}

	private OnClickListener clickListener0 = new Button.OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			fase[0].setText("1");
			duracao[0].setEnabled(true);
			kc[0].setEnabled(true);
		}
	};
	private OnClickListener clickListener1 = new Button.OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			fase[1].setText("2");
			duracao[1].setEnabled(true);
			kc[1].setEnabled(true);
		}
	};
	private OnClickListener clickListener2 = new Button.OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			fase[2].setText("3");
			duracao[2].setEnabled(true);
			kc[2].setEnabled(true);
		}
	};
	private OnClickListener clickListener3 = new Button.OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			fase[3].setText("4");
			duracao[3].setEnabled(true);
			kc[3].setEnabled(true);
		}
	};
	private OnClickListener clickListener4 = new Button.OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			fase[4].setText("5");
			duracao[4].setEnabled(true);
			kc[4].setEnabled(true);
		}
	};

	private OnClickListener CadCultura = new Button.OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			int id_cult;
			id_cult = GetUltimoIdCult() + 1;
			banco.close();
			banco = openOrCreateDatabase("bhDataBase", MODE_WORLD_WRITEABLE,
					null);
			try {
				int i = 0;
				for (; i < 5;) {
					if (duracao[i].isEnabled()) {
						if (duracao[i].getText().toString().length() == 0) {
							showAlert("A duração da fase deve ser informada",
									"Erro");
							duracao[i].requestFocus();
							break;
						} else if (kc[i].getText().toString().length() == 0) {
							showAlert(
									"O Coeficiente de cultura (KC) deve ser informado",
									"Erro");
							kc[i].requestFocus();
							break;
						} else {
							if (duracao[i + 1].isEnabled()) {
								i++;
							} else {
								banco
										.execSQL("INSERT INTO cultura(id_cultura, nome_cultura) VALUES('"
												+ id_cult
												+ "', '"
												+ nome.getText() + "')");
								for (int f = 0; f < i + 1; f++) {
									banco
											.execSQL("INSERT INTO cultura_fases(id_cultura, fase, duracao, kc) VALUES ('"
													+ id_cult
													+ "', '"
													+ fase[f].getText()
													+ "', '"
													+ duracao[f].getText()
													+ "', '"
													+ kc[f].getText()
													+ "')");
								}
								showComfirma("Cultura cadastrada com sucesso!",
										"Confirmação");
								break;
							}
						}
					}
				}
			} catch (Exception e) {
				// TODO: handle exception
				showAlert(e.getMessage(), "Erro");
			}
		}
	};

	/**
	 * Busca no banco de dados a identificação da última cultura cadastrada no
	 * banco de dados
	 * 
	 * @return último id cadastrado no banco de dados de cultura.
	 */
	public int GetUltimoIdCult() {
		Cursor q;
		banco = openOrCreateDatabase("bhDataBase", MODE_WORLD_WRITEABLE, null);
		q = banco.rawQuery("SELECT max(id_cultura) as ultimo FROM cultura",
				null);
		if (q.getCount() == 0) {
			return 0;
		} else {
			q.moveToFirst();
			return q.getInt(0);
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
		builder.setNeutralButton("OK", null);
		builder.setIcon(R.drawable.icon);
		AlertDialog dialog = builder.create();
		dialog.setTitle(titulo);
		dialog.show();
	};

	public void showComfirma(String msgerro, String titulo) {
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
