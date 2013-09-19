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

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * @author Luciano Édipo Pereira da Silva
 * 
 */
public class Cultura extends android.content.ContextWrapper {
	public int id_cultura, qtde_fase, duracao;
	public String nome_cultura;
	public int[] dia_fase;
	public float[] kc;
	public FaseCultura[] fase;
	public SQLiteDatabase bd;
	public Cursor query;

	/**
	 * Instância da classe cultra
	 * 
	 * @param id_cultura
	 *            identificação da cultura no banco de dados
	 * @param context
	 *            contexto pelo qual a classe foi chamada, usado para se
	 *            utilizar a conexão com o banco de dados
	 */
	public Cultura(int id_cultura, Context context) {
		super(context);
		this.id_cultura = id_cultura;
		bd = openOrCreateDatabase("bhDataBase",
				android.content.Context.MODE_WORLD_READABLE, null);
		duracao = DurCult();
		nome_cultura = NomeCultura();
		fase = new FaseCultura[QtdeFases()];
		CarregarFase();
	}

	/**
	 * Cálculo da duração total de dias do cultivo.
	 * 
	 * @return a duração total, em dias, do cultivo.
	 */
	public int DurCult() {
		Cursor query;

		query = bd.rawQuery(
				"SELECT sum(duracao) FROM cultura_fases WHERE id_cultura = '"
						+ id_cultura + "'", null);
		query.moveToFirst();
		return query.getInt(0);
	}

	/**
	 * Método que retorna no nome da cultura selecionado.
	 * 
	 * @return nome da cultura.
	 */
	public String NomeCultura() {
		Cursor query;

		query = bd.rawQuery("SELECT * FROM cultura WHERE id_cultura = '"
				+ id_cultura + "'", null);
		query.moveToFirst();
		return query.getString(1);
	}

	/**
	 *Carrega as fases da cultura selecionada, e armazena no vetor de fases
	 */
	public void CarregarFase() {
		Cursor q;
		q = bd.rawQuery("SELECT * FROM cultura_fases WHERE id_cultura = '"
				+ id_cultura + "'", null);
		q.moveToFirst();
		for (int i = 0; i < q.getCount(); i++) {
			fase[i] = new FaseCultura(q.getInt(q.getColumnIndex("fase")), q
					.getInt(q.getColumnIndex("duracao")), q.getFloat(q
					.getColumnIndex("kc")));
			Log.v(BalancoHidrico.CATEGORIA, "Add Fase:" + fase[i].fase
					+ " Dur:" + fase[i].duracao + " KC:" + fase[i].kc);
			q.moveToNext();
		}
	}

	/**
	 * @return retorna a quantidade de fases que o cultura seleciona possui
	 */
	public int QtdeFases() {
		Cursor q;

		q = bd.rawQuery(
				"SELECT count(*) FROM cultura_fases WHERE id_cultura = '"
						+ id_cultura + "'", null);
		q.moveToFirst();
		return q.getInt(0);
	}

	/**
	 * 
	 * @param n_dias
	 *            numero do dia em que o calculo se encontra (Incrementado em
	 *            mais um porque começa em zero)
	 * @return fase[i] o fase correpondente ao dia que está culculando
	 */
	public FaseCultura PegarFase(int n_dias) {
		int soma_dias = 0;
		for (int i = 0; i < fase.length; i++) {
			soma_dias = soma_dias + fase[i].duracao;
			if (n_dias <= soma_dias) {
				return fase[i];
			}
		}
		return null;
	}
}
