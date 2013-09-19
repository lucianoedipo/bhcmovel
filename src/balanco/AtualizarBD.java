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
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

/**
 * @author Luciano Édipo <h4>
 *         Classe atividade, que cria uma intenção de executar algo, e renderiza
 *         o layout n tela.</h4>
 */
public class AtualizarBD extends Activity {
	public Intent intAtuNet, intAtuMan;
	public Button bttAtuNet, bttAtuManual;

	/**
	 *Método que cria a atividade.
	 */
	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.atualizar);
		setTitle("Atualização Clima:");

		intAtuNet = new Intent(this, AtualizaNet.class);
		intAtuMan = new Intent(this, AtualizaManual.class);
		bttAtuNet = (Button) findViewById(R.id.BttInternet);
		bttAtuManual = (Button) findViewById(R.id.BttManual);
		bttAtuNet.setOnClickListener(seClickInternet);
		bttAtuManual.setOnClickListener(seClickManual);
	}

	private OnClickListener seClickInternet = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			startActivity(intAtuNet);
		}
	};

	private OnClickListener seClickManual = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			startActivity(intAtuMan);
		}
	};
}
