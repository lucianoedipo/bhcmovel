
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
package grafico;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class GraficoBH extends Activity {
	public DadosBHGraph dados;
	public float max, min, dado[];
	public int qtde, tipo;
	public String dia[];

	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		BHGraph tela;
		max = getIntent().getExtras().getFloat("max");
		min = getIntent().getExtras().getFloat("min");
		qtde = getIntent().getExtras().getInt("qtde");
		dado = getIntent().getExtras().getFloatArray("dados");
		dia = getIntent().getExtras().getStringArray("dia");
		tipo = getIntent().getExtras().getInt("tipo");
		for (int i = 0; i < dado.length; i++) {
			Log.v("GDADOS", "" + dado[i] + " TAM:" + qtde);
		}
		dados = new DadosBHGraph(tipo, max, min, qtde, dado, dia);
		tela = new BHGraph(this, dados);
		setContentView(tela);

	}
}
