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
 * @author Luciano Édipo Pereira da Silva
 * 
 */
public class Solo {
	private String nome;
	private int espessura;
	private float cc, pm, ds;

	/**
	 * cria um objeto do tipo solo e o instancia.
	 * 
	 * @param nome
	 *            nome do solo.
	 * @param espessura
	 *            espessura do solo.
	 * @param cc
	 *            capacidade de campo do solo.
	 * @param pm
	 *            ponto de murcha do solo.
	 * @param ds
	 *            densidade do solo.
	 */
	public Solo(String nome, int espessura, float cc, float pm, float ds) {
		super();
		this.nome = nome;
		this.espessura = espessura;
		this.cc = cc;
		this.pm = pm;
		this.ds = ds;
	}

	/**
	 * @return retorna o nome de solo.
	 */
	public String getNome() {
		return nome;
	}

	/**
	 * 
	 * @param nome
	 */
	public void setNome(String nome) {
		this.nome = nome;
	}

	/**
	 * 
	 * @return a espessura do solo.
	 */
	public int getEspessura() {
		return espessura;
	}

	public void setEspessura(int espessura) {
		this.espessura = espessura;
	}

	/**
	 * 
	 * @return a capacidade da campo do solo.
	 */
	public float getCc() {
		return cc;
	}

	public void setCc(float cc) {
		this.cc = cc;
	}

	/**
	 * 
	 * @return o ponto de murcha do solo.
	 */
	public float getPm() {
		return pm;
	}

	public void setPm(float pm) {
		this.pm = pm;
	}

	/**
	 * 
	 * @return a densidade do solo.
	 */
	public float getDs() {
		return ds;
	}

	/**
	 * 
	 * @param ds
	 */
	public void setDs(float ds) {
		this.ds = ds;
	}
}
