/**
 *   Written by Luciano Édipo Pereira da Silva
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

import java.util.Date;
import bhcmovel.balanco.R;
import administrar.Administrar;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;

/**
 * @author Luciano ï¿½dipo Pereira da Silva
 * @version 0.2.1
 * 
 *          Principal Classe
 */
public class BalancoHidrico extends Activity {
	/** Definiï¿½ï¿½o do cï¿½digo do aplicativo para o logs do sistema */
	public static final String CATEGORIA = "BHC";
	/** Intent que chama a funï¿½ï¿½o de coleta e atualizaï¿½ï¿½o */
	public Intent intAtu;
	/** Intent que chama a funï¿½ï¿½o de escolha do solo */
	public Intent intEscSolo;
	/** Intent que chama a funï¿½ï¿½o de administraï¿½ï¿½o */
	public Intent intAdm;
	/** Intent que chama a funï¿½ï¿½o de cï¿½lculo */
	public Intent intCalc;
	/** Botï¿½o Atualizaï¿½ï¿½o e coleta */
	public Button bttAtu;
	/** Botï¿½o Administraï¿½ï¿½o */
	public Button bttAdm;
	/** Botï¿½o de Cï¿½lculo */
	public Button bttCalc;
	/** Menu da tela inicial do aplicativo */
	public Menu menu;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		/**
		 * Mï¿½todo que chama o mï¿½todo da classe pai que instï¿½ncia o aplicativo
		 */
		super.onCreate(savedInstanceState);
		Window janela = getWindow();
		janela.requestFeature(Window.FEATURE_LEFT_ICON);
		setContentView(R.layout.main);
		janela.setFeatureDrawableResource(Window.FEATURE_LEFT_ICON,
				R.drawable.icon);
		GradientDrawable grad = new GradientDrawable(Orientation.TOP_BOTTOM,
				new int[] { Color.BLACK, Color.rgb(0, 100, 0) });
		this.getWindow().setBackgroundDrawable(grad);

		if (databaseList().length == 0) {
			Intent welcome = new Intent(this, administrar.CriarPrimeiroBD.class);
			startActivity(welcome);
		}

		bttAtu = (Button) findViewById(R.id.BttAtu);
		bttAdm = (Button) findViewById(R.id.BttAdm);
		bttCalc = (Button) findViewById(R.id.BttCalc);

		intAtu = new Intent(this, AtualizarBD.class);
		intAdm = new Intent(this, Administrar.class);
		intCalc = new Intent(this, CalcularBH.class);
		intEscSolo = new Intent(this, EscolheSolo.class);

		bttAtu.setOnClickListener(onClickAtu);
		bttAdm.setOnClickListener(onClickAdm);
		bttCalc.setOnClickListener(onClickCalc);

	}

	public boolean onCreateOptionsMenu(Menu menul) {
		menu = menul;
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menumain, menul);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.sair:
			try {
				finalize();
				finish();
				return true;
			} catch (Throwable e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case R.id.graficos:
			try {
				Intent gra = new Intent(this, grafico.GraficoBH.class);

				startActivity(gra);
				return true;
			} catch (Throwable e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		default:
			return true;
		}
		return false;
	}

	private OnClickListener onClickAtu = new Button.OnClickListener() {
		@Override
		public void onClick(View view) {
			// TODO Auto-generated method stub
			startActivity(intAtu);
		}
	};

	private OnClickListener onClickAdm = new Button.OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			startActivity(intAdm);
		}
	};

	private OnClickListener onClickCalc = new Button.OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			startActivity(intEscSolo);
			// startActivity(intCalc);
		}
	};

	/**
	 * Converte data no formato americano para o brasileiro.
	 * 
	 * @param date
	 *            data no formato AAAA-MM-DD
	 * @return data no formato DD/MM/AAAA
	 */
	public static String DateMysqlToDateBr(String date) {

		String dia, mes, ano;

		ano = date.substring(0, 4);
		mes = date.substring(5, 7);
		dia = date.substring(8, 10);

		return dia + "/" + mes + "/" + ano;

	}

	/**
	 * Converte data do formato brasileiro para o americano
	 * 
	 * @param date
	 *            data no formato DD/MM/AAAA
	 * @return data no formato AAAA-MM-DD
	 */
	public static String DateBrToDateMysql(String date) {

		String dia, mes, ano;

		ano = date.substring(6, 10);
		mes = date.substring(3, 5);
		dia = date.substring(0, 2);

		return ano + "-" + mes + "-" + dia;

	}

	/**
	 * Recebe uma String de data, soma e retorna como Objeto java.util.Date
	 * 
	 * @param data
	 *            data no formato AAAA-MM-DD
	 * @param soma_dia
	 *            quantidade de dias ï¿½ somar
	 * @return Objeto java.util.Date com a nova data
	 */
	public static Date StrDateMysqlToDate(String data, int soma_dia) {
		Integer ano = Integer.valueOf(data.substring(0, 4));
		Integer mes = Integer.valueOf(data.substring(5, 7));
		Integer dia = Integer.valueOf(data.substring(8, 10));
		return new Date(ano - 1900, mes - 1, dia + soma_dia);
	}

	/**
	 * 
	 * @param bd
	 *            recebe uma instancia de um SQLite ja aberto.
	 * @return o primeiro dia cadastrado no banco de dados no formato AAAA-MM-DD
	 */
	public static String PrimDia(SQLiteDatabase bd) {
		Cursor query;
		query = bd.rawQuery("SELECT min(dia) FROM clima", null);
		query.moveToFirst();
		return query.getString(0);
	}

	/**
	 * 
	 * @param bd
	 *            recebe uma instancia de um SQLite ja aberto.
	 * @return o ultimo dia cadastrado no banco de dados no formato AAAA-MM-DD
	 */
	public static String UltDia(SQLiteDatabase bd) {
		Cursor query;
		query = bd.rawQuery("SELECT max(dia) FROM clima", null);
		query.moveToFirst();
		return query.getString(0);

	}

}