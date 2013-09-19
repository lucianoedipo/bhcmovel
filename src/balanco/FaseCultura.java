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

/**
 * @author Luciano
 * 
 */
public class FaseCultura {
	public int fase, duracao;
	public float kc;

	/**
	 * as culturas possuem fases que são intanciadas por essa classe
	 * 
	 * @param fase
	 *            numero de fase da cultura.
	 * @param duracao
	 *            duração em dias que a fase possui.
	 * @param kc
	 *            coeficiente de ajuste de cultura.
	 */
	public FaseCultura(int fase, int duracao, float kc) {

		this.fase = fase;
		this.duracao = duracao;
		this.kc = kc;
	}
}