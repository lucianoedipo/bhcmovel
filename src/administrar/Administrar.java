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
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

/**
 * @author Luciano
 * 
 */
public class Administrar extends Activity {

	public Button bttCadSolo;
	public Button bttCriarDB;
	public Button bttCadCultura;
	public Intent intCadSolo;
	public Intent intCadCultura;
	private SQLiteDatabase bancoDados;

	/**
	 * 
	 */

	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.administrar);
		bttCadSolo = (Button) findViewById(R.id.BttCadSolo);
		bttCriarDB = (Button) findViewById(R.id.BttAdmCriarDB);
		bttCadCultura = (Button) findViewById(R.id.BttCadCultura);

		intCadSolo = new Intent(this, CadastroSolo.class);
		intCadCultura = new Intent(this, CadastroCultura.class);

		bttCadSolo.setOnClickListener(onClickCadSolo);
		bttCadCultura.setOnClickListener(onClickCadCultura);
		bttCriarDB.setOnClickListener(onClickCriarDB);
	}

	private OnClickListener onClickCadSolo = new Button.OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			startActivity(intCadSolo);
		}

	};

	private OnClickListener onClickCadCultura = new Button.OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			startActivity(intCadCultura);
		}

	};

	private OnClickListener onClickCriarDB = new Button.OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			CriarBanco();
		}

	};

	/**
	 * Cria as tabelas iniciais do banco de dados
	 */
	public void CriarBanco() {
		bancoDados = openOrCreateDatabase("bhDataBase", MODE_WORLD_WRITEABLE,
				null);
		bancoDados
				.execSQL("CREATE TABLE IF NOT EXISTS solo ( nome char(30) NOT NULL,  espessura int(11) NOT NULL,  cc float NOT NULL,  pm float NOT NULL,  ds float NOT NULL,  PRIMARY KEY (nome))");
		bancoDados
				.execSQL("CREATE TABLE IF NOT EXISTS clima (dia date NOT NULL, temp float NOT NULL, urel int(3) NOT NULL, vento2 float NOT NULL, radia float NOT NULL, chuva_est float, chuva_local float, PRIMARY KEY (dia))");
		bancoDados
				.execSQL("CREATE TABLE IF NOT EXISTS cultura (id_cultura int(3) NOT NULL, nome_cultura char(30) NOT NULL, PRIMARY KEY(id_cultura))");
		bancoDados
				.execSQL("CREATE TABLE IF NOT EXISTS cultura_fases (id_cultura int(3) NOT NULL, fase int(2) NOT NULL, duracao int(3) NOT NULL, kc float NOT NULL, PRIMARY KEY(id_cultura, fase))");

	}
}
