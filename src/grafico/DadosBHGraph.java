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

/**
 * @author Luciano
 * 
 */
public class DadosBHGraph {
	public float ValorMax, ValorMin, dados[], px[], py[];
	public int qtde, tipo;
	public String dias[], nome;
	public static final int GRAFICO_UM = 0;
	public static final int GRAFICO_AD = 1;
	public static final int GRAFICO_DF = 2;

	// public int qtde;

	public DadosBHGraph(int tipo_grafico, float vmax, float vmin,
			int qtdpontos, float dado[], String dia[]) {
		this.tipo = tipo_grafico;
		dados = new float[qtdpontos];
		this.dias = new String[qtdpontos];
		this.dados = dado;
		this.qtde = qtdpontos;
		px = new float[qtdpontos];
		py = new float[qtdpontos];
		this.dias = dia;
		this.ValorMax = vmax;
		this.ValorMin = vmin;
		if (tipo == GRAFICO_AD)
			nome = "Água Disponível (mm)";
		if (tipo == GRAFICO_UM)
			nome = "Umidade do Solo (mm)";
		if (tipo == GRAFICO_DF)
			nome = "Deficiência HÍdrica (mm)";

	}

}
