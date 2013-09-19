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

import java.text.DecimalFormat;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.Paint.Align;
import android.view.View;

/**
 * @author Luciano
 * 
 */
public class BHGraph extends View {

	public final int INIFIGUREX = 0;
	public final int INIFIGUREY = 10;
	public final int FIMFIHUREX = 10;
	public final int FIMFIHUREY = 90;
	public final int DESLOCAMENTOLINHA = 20;
	public Point LinhaYIni, LinhaYFim, LinhaXIni, LinhaXFim;
	public DadosBHGraph dadosBHGraph;
	public DecimalFormat format;

	/**
	 * 
	 * @param context
	 * @param dadosBHGraph
	 */
	public BHGraph(Context context, DadosBHGraph dadosBHGraph) {
		super(context);
		this.dadosBHGraph = dadosBHGraph;
		format = new DecimalFormat("0.00");

		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onDraw(Canvas canvas) {

		Paint p = new Paint();
		int DESLOCX = 40;
		LinhaYIni = new Point(INIFIGUREX + DESLOCX, INIFIGUREY + 30);
		LinhaYFim = new Point(INIFIGUREX + DESLOCX, canvas.getHeight()
				- FIMFIHUREY - 10);
		LinhaXIni = new Point(INIFIGUREX + DESLOCX, canvas.getHeight()
				- FIMFIHUREY - 10);
		LinhaXFim = new Point(canvas.getWidth() - FIMFIHUREX - 10, canvas
				.getHeight()
				- FIMFIHUREY - 10);

		p.setColor(Color.WHITE);
		canvas.drawRect(INIFIGUREX, INIFIGUREY, canvas.getWidth() - FIMFIHUREX
				+ 10, canvas.getHeight() - FIMFIHUREY + 40, p);

		NomearGrafico(canvas, dadosBHGraph.nome);
		setGrade(canvas);
		LinhaPMCCCTA(canvas);
		LinhasDdos(canvas);
		DesenharLeganda(canvas);

	}

	public void LinhasDdos(Canvas canvas) {
		float vponto, vgponto, pontoplotagem, dist_hori, dist_ini;
		int mes_ini;
		mes_ini = PegarMesStr(dadosBHGraph.dias[0]);
		Paint p = new Paint();
		dist_ini = LinhaXIni.x;
		dist_hori = (LinhaXFim.x - LinhaXIni.x) / dadosBHGraph.dados.length;
		// dist_hori = dist_hori + (dist_hori / dadosBHGraph.dados.length);

		vgponto = (LinhaXIni.y - DESLOCAMENTOLINHA)
				- (LinhaYIni.y + DESLOCAMENTOLINHA);

		for (int i = 0; i < dadosBHGraph.dados.length; i++) {

			vponto = 100 * (dadosBHGraph.dados[i] - dadosBHGraph.ValorMax)
					/ (dadosBHGraph.ValorMin - dadosBHGraph.ValorMax);

			pontoplotagem = (LinhaYIni.y + DESLOCAMENTOLINHA) + vponto
					* (vgponto / 100);
			dadosBHGraph.px[i] = dist_ini;
			dadosBHGraph.py[i] = pontoplotagem;
			if ((i < dadosBHGraph.qtde)
					&& (PegarMesStr(dadosBHGraph.dias[i]) != mes_ini)) {
				mes_ini = PegarMesStr(dadosBHGraph.dias[i]);
				p.setColor(Color.RED);
				LinhaMes(canvas, dist_ini, mes_ini, Color.GRAY);
				canvas.drawCircle(dist_ini, pontoplotagem, 2, p);
			} else {
				if ((i == 0) && (PegarDiaStr(dadosBHGraph.dias[i]) <= 15)) {
					LinhaMes(canvas, dist_ini, mes_ini, Color.BLACK);
				}
				p.setColor(Color.BLUE);
				// canvas.drawCircle(dist_ini, pontoplotagem, 1, p);
			}

			if (dadosBHGraph.qtde < 90 && dadosBHGraph.qtde > 50) {
				dist_ini += dist_hori + 0.5;
			} else if (dadosBHGraph.qtde < 120 && dadosBHGraph.qtde > 90) {
				dist_ini += dist_hori + 0.3;
			} else if (dadosBHGraph.qtde > 120) {
				dist_ini += dist_hori + 0.8;
			} else {
				dist_ini += dist_hori + 0.7;
			}

		}
		p.reset();

		p.setColor(Color.BLUE);
		p.setAntiAlias(true);

		for (int i = 0; i < (dadosBHGraph.dados.length - 1); i++) {

			canvas.drawLine(dadosBHGraph.px[i], dadosBHGraph.py[i],
					dadosBHGraph.px[(i + 1)], dadosBHGraph.py[(i + 1)], p);
		}

	}

	public void LinhaMes(Canvas canvas, float x, int mes, int cor) {

		Paint p = new Paint();
		p.setAntiAlias(true);
		p.setColor(cor);
		canvas.drawLine(x, LinhaYIni.y, x, LinhaYFim.y + 10, p);
		p.setColor(Color.BLACK);
		p.setFakeBoldText(true);
		canvas.drawText(NomeMes(mes), x + 5, LinhaYFim.y - 5, p);

	}

	public void LinhaPMCCCTA(Canvas canvas) {
		Paint p = new Paint();
		Paint pn = new Paint();
		float PloPonto, PloIni, valordesl;
		pn.setAntiAlias(true);
		pn.setColor(Color.BLACK);

		PloPonto = ((LinhaXIni.y - DESLOCAMENTOLINHA) - LinhaYIni.y + DESLOCAMENTOLINHA) / 100;
		valordesl = (dadosBHGraph.ValorMax - dadosBHGraph.ValorMin) / 100;
		PloIni = (LinhaYIni.y + DESLOCAMENTOLINHA);

		p.setColor(Color.GRAY);
		canvas.drawText(format.format(valordesl * 50 + dadosBHGraph.ValorMin),
				INIFIGUREX + 5, PloIni + PloPonto * (100 - 50) + 5, pn);
		canvas.drawLine(LinhaXIni.x, PloIni + PloPonto * (100 - 50),
				LinhaXFim.x, PloIni + PloPonto * (100 - 50), p);

		canvas.drawText(format.format(valordesl * 25 + dadosBHGraph.ValorMin),
				INIFIGUREX + 5, PloIni + PloPonto * (100 - 25) + 5, pn);
		canvas.drawLine(LinhaXIni.x, PloIni + PloPonto * (100 - 25),
				LinhaXFim.x, PloIni + PloPonto * (100 - 25), p);

		canvas.drawText(format.format(valordesl * 75 + dadosBHGraph.ValorMin),
				INIFIGUREX + 5, PloIni + PloPonto * (100 - 75) + 5, pn);
		canvas.drawLine(LinhaXIni.x, PloIni + PloPonto * (100 - 75),
				LinhaXFim.x, PloIni + PloPonto * (100 - 75), p);

		canvas.drawText(format.format(dadosBHGraph.ValorMin), INIFIGUREX + 5,
				LinhaXIni.y + 5 - DESLOCAMENTOLINHA, pn);
		p.setColor(Color.RED);
		canvas.drawLine(LinhaXIni.x, LinhaXIni.y - DESLOCAMENTOLINHA,
				LinhaXFim.x, LinhaXIni.y - DESLOCAMENTOLINHA, p);

		canvas.drawText(format.format(dadosBHGraph.ValorMax), INIFIGUREX + 5,
				LinhaYIni.y + 5 + DESLOCAMENTOLINHA, pn);
		p.setColor(Color.GREEN);
		canvas.drawLine(LinhaYIni.x, LinhaYIni.y + DESLOCAMENTOLINHA,
				LinhaXFim.x, LinhaYIni.y + DESLOCAMENTOLINHA, p);
	}

	public void setGrade(Canvas canvas) {
		Paint p = new Paint();

		p.setColor(Color.BLACK);

		canvas.drawLine(LinhaYIni.x, LinhaYIni.y, LinhaYFim.x, LinhaYFim.y, p);
		canvas.drawLine(LinhaXIni.x - 10, LinhaXIni.y, LinhaXFim.x,
				LinhaXFim.y, p);
	}

	public int PegarMesStr(String data) {
		String aux;
		Integer i;

		aux = data.substring(5, 7);
		i = Integer.valueOf(aux);

		return i;
	}

	public int PegarDiaStr(String data) {
		String aux;
		Integer i;

		aux = data.substring(8, 10);
		i = Integer.valueOf(aux);

		return i;
	}

	public String NomeMes(int mes) {
		String nmes[] = { "", "Jan", "Fev", "Mar", "Abr", "Mai", "Jun", "Jul",
				"Ago", "Set", "Out", "Nov", "Dez" };
		return nmes[mes];
	}

	public void NomearGrafico(Canvas canvas, String string) {
		Paint p = new Paint();
		p.setColor(Color.BLACK);
		p.setTextAlign(Align.CENTER);
		p.setTextSize(14);
		p.setFakeBoldText(true);
		p.setTypeface(Typeface.SANS_SERIF);
		p.setAntiAlias(true);
		p.setTextScaleX((float) 1.3);
		canvas.drawText(string, 150, INIFIGUREY + 20, p);
	}

	public void DesenharLeganda(Canvas canvas) {

		Paint p = new Paint();
		p.setAntiAlias(true);

		if (dadosBHGraph.tipo == DadosBHGraph.GRAFICO_UM) {
			p.setColor(Color.BLUE);
			canvas.drawRect(10, canvas.getHeight() - FIMFIHUREY + 18, 20,
					canvas.getHeight() - FIMFIHUREY + 20, p);

			p.setColor(Color.BLACK);
			canvas.drawText("Umidade do Solo", 25, canvas.getHeight()
					- FIMFIHUREY + 20, p);

			p.setColor(Color.GREEN);
			canvas.drawRect(135, canvas.getHeight() - FIMFIHUREY + 18, 145,
					canvas.getHeight() - FIMFIHUREY + 20, p);

			p.setColor(Color.BLACK);
			canvas.drawText("CC", 150, canvas.getHeight() - FIMFIHUREY + 20, p);

			p.setColor(Color.RED);
			canvas.drawRect(180, canvas.getHeight() - FIMFIHUREY + 18, 190,
					canvas.getHeight() - FIMFIHUREY + 20, p);

			p.setColor(Color.BLACK);
			canvas.drawText("PM", 195, canvas.getHeight() - FIMFIHUREY + 20, p);
		} else if (dadosBHGraph.tipo == DadosBHGraph.GRAFICO_AD) {

			p.setColor(Color.BLUE);
			canvas.drawRect(10, canvas.getHeight() - FIMFIHUREY + 18, 20,
					canvas.getHeight() - FIMFIHUREY + 20, p);

			p.setColor(Color.BLACK);
			canvas.drawText("Água Disponível", 25, canvas.getHeight()
					- FIMFIHUREY + 20, p);

			p.setColor(Color.GREEN);
			canvas.drawRect(135, canvas.getHeight() - FIMFIHUREY + 18, 145,
					canvas.getHeight() - FIMFIHUREY + 20, p);

			p.setColor(Color.BLACK);
			canvas
					.drawText("CTA", 150, canvas.getHeight() - FIMFIHUREY + 20,
							p);

		} else {

			p.setColor(Color.BLUE);
			canvas.drawRect(10, canvas.getHeight() - FIMFIHUREY + 18, 20,
					canvas.getHeight() - FIMFIHUREY + 20, p);

			p.setColor(Color.BLACK);
			canvas.drawText("Deficiência Hídrica", 25, canvas.getHeight()
					- FIMFIHUREY + 20, p);
		}
	}
}
