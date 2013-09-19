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

import grafico.DadosBHGraph;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import bhcmovel.balanco.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

/**
 * 
 * @author Luciano
 * 
 */
public class CalcularBH extends Activity {
	static final double MAX_CHUVA_EFETIVA = 25;
	public ProgressDialog pd;
	public Runnable runnable;
	public Button BttCalcOk;
	public Button BttCalcAnt;
	public Button BttCalcProx;
	public Button BttCalcPrim;
	public Button BttCalcUlt;
	public Button BttGraph;

	public TextView TextCalcTeste;
	// text results

	public TextView ValDia;
	public TextView ValEt0;
	public TextView ValEtr;
	public TextView ValUSolo;
	public TextView ValDefH;
	public TextView ValExcH;
	public TextView ValAguaD;
	public TextView ValPe;
	public int LinhaDia, Id_cult;
	// fim text result

	public Menu menu;

	public String diaA, mesA, anoA, Id_solo, Per_Ini, Per_Fim;

	public Date data;

	public SQLiteDatabase database;

	public Intent intGraph;

	public ArrayList<DiaBh> Dias = new ArrayList<DiaBh>();

	public Solo solo;
	public Cultura cultura;

	private DecimalFormat format2;

	public void onCreate(Bundle bundle) {

		runnable = new Runnable() {
			public void run() {
				Message msg;
				msg = new Message();
				database = openOrCreateDatabase("bhDataBase",
						MODE_WORLD_READABLE, null);
				SelDataBH(Per_Ini, Per_Fim);
				msg.arg1 = 0;
				handmsg.sendMessage(msg);
				SelSolo(Id_solo);

				Calcular();
				handler.sendEmptyMessage(0);
			}
		};

		super.onCreate(bundle);
		setContentView(R.layout.calcularbh);
		format2 = new DecimalFormat("0.0");

		Id_cult = getIntent().getExtras().getInt("id_cult");
		Id_solo = getIntent().getExtras().getString("id_solo");

		Per_Ini = getIntent().getExtras().getString("dia_ini");
		Per_Fim = getIntent().getExtras().getString("dia_fim");

		cultura = new Cultura(Id_cult, this);
		this.ConnBanco();
		setTitle("Balanço Hídrico - " + cultura.nome_cultura);
		data = new Date();
		BttCalcOk = (Button) findViewById(R.id.BttCalcOk);
		BttCalcAnt = (Button) findViewById(R.id.BttCalcAnt);
		BttCalcProx = (Button) findViewById(R.id.BttCalcProx);
		BttCalcPrim = (Button) findViewById(R.id.BttCalcPrim);
		BttCalcUlt = (Button) findViewById(R.id.BttCalcUlt);
		BttGraph = (Button) findViewById(R.id.BttCalGraph);

		TextCalcTeste = (TextView) findViewById(R.id.TextCalcTeste);
		TextCalcTeste.setText(TextCalcTeste.getText(),
				TextView.BufferType.EDITABLE);
		// text results
		// text results

		ValDia = (TextView) findViewById(R.id.TextCalcValueDia);
		ValEt0 = (TextView) findViewById(R.id.TextCalcValueET0);
		ValEtr = (TextView) findViewById(R.id.TextCalcValueETR);
		ValUSolo = (TextView) findViewById(R.id.TextCalcValueUmin);
		ValDefH = (TextView) findViewById(R.id.TextCalcValueDH);
		ValExcH = (TextView) findViewById(R.id.TextCalcValueEH);
		ValAguaD = (TextView) findViewById(R.id.TextCalcValueAD);
		ValPe = (TextView) findViewById(R.id.TextCalcValuePE);
		// fim text result

		// fim text results

		// Botôes de navegação
		BttCalcOk.setOnClickListener(onClickCalcOk);
		BttCalcAnt.setOnClickListener(onClickCalcAnt);
		BttCalcProx.setOnClickListener(onClickCalcProx);
		BttCalcPrim.setOnClickListener(onClickCalcPrim);
		BttCalcUlt.setOnClickListener(onClickCalcUlt);
		BttGraph.setOnClickListener(onClickGraph);
		BttCalcAnt.setEnabled(false);
		BttCalcUlt.setEnabled(false);
		BttCalcProx.setEnabled(false);
		BttCalcPrim.setEnabled(false);
		// fim btts navegação

		intGraph = new Intent(this, grafico.GraficoBH.class);

	}

	private void ConnBanco() {
		try {
			database = openOrCreateDatabase("bhDataBase", MODE_WORLD_READABLE,
					null);
		} catch (final Exception e) {
			// TODO: handle exception
			TextCalcTeste.setText("Erro ao abrir banco");

		}
	}

	public void Calcular() {
		int i, iant, umidade;
		double temp, vento2, radia, chuva;
		for (i = 0; i < Dias.size(); i++) {

			iant = i - 1;
			temp = Dias.get(i).getTemp();
			vento2 = Dias.get(i).getVento2();
			radia = Dias.get(i).getRadia();
			umidade = Dias.get(i).getUrel();
			if (Dias.get(i).getChuva_local() != 0) {
				Log.i(BalancoHidrico.CATEGORIA, "DIA " + Dias.get(i).getData()
						+ "Usando chuva locall");
				chuva = Dias.get(i).getChuva_local();
			} else {
				chuva = Dias.get(i).getChuva();
			}

			// Inicio do processamento do cálculo de balanço hídrico

			// Calculando Et0
			Dias.get(i).setEt0(CalcEt0(temp, umidade, vento2, radia));

			// Calculando Cta do Solo
			Dias.get(i).setCta(CalcCta(solo));

			// Calculando Preciptação Efetiva
			if (i == 0) {
				Dias.get(i).setPe(
						CalcPrecEfet(chuva, Dias.get(i).getCta(), Dias.get(i)
								.getCta()));

			} else {
				Dias.get(i).setPe(
						CalcPrecEfet(chuva, Dias.get(iant).getAd(), Dias.get(i)
								.getCta()));
			}
			// Fim calculo do Preciptação Efetiva
			// Calculando KS
			if (i == 0) {
				Dias.get(i).setKs(
						CalcKs(solo.getCc(), solo.getCc(), solo.getPm()));

			} else {
				if (Dias.get(iant).getEh() > 0) {
					Dias.get(i).setKs(1);

				} else {
					Dias.get(i).setKs(
							CalcKs(Dias.get(iant).getUm(), solo.getCc(),
									solo.getPm()));

				}
			}

			// inserindo kc
			Dias.get(i).setKc(cultura.PegarFase(i).kc);

			// Calculando Etr
			Dias.get(i).setEtr(
					CalcEtr(Dias.get(i).getEt0(), cultura.PegarFase(i).kc, Dias
							.get(i).getKs()));

			// Calculando Água Disponível
			if (i == 0) {
				Dias.get(i).setAd(
						CalcAguaDisp(Dias.get(i).getCta(), Dias.get(i).getPe(),
								Dias.get(i).getEtr(), Dias.get(i).getCta()));
			} else {
				if (Dias.get(iant).getEh() > 0) {

					Dias.get(i).setAd(Dias.get(i).getCta());

				} else {

					Dias.get(i).setAd(
							CalcAguaDisp(Dias.get(iant).getAd(), Dias.get(i)
									.getPe(), Dias.get(i).getEtr(), Dias.get(i)
									.getCta()));
				}
			}

			// Calculando DeltaAd(Variação de Água Disponível)
			if (i == 0) {
				Dias.get(i).setDelta_ad(
						Dias.get(i).getAd() - Dias.get(i).getCta());
			} else {
				Dias.get(i).setDelta_ad(
						Dias.get(i).getAd() - Dias.get(iant).getAd());

			}

			// Calculando Umidade do Solo
			if (i == 0) {

				Dias.get(i).setUm(
						CalcUmSolo(Dias.get(i).getAd(), solo.getDs(),
								solo.getEspessura(), solo.getPm()));

			} else {
				if (Dias.get(iant).getEh() > 0) {

					Dias.get(i).setUm(solo.getCc());

				} else {
					Dias.get(i).setUm(
							CalcUmSolo(Dias.get(i).getAd(), solo.getDs(),
									solo.getEspessura(), solo.getPm()));

				}
			}

			// Calculando Deficiencia Hídrica

			if (i == 0) {
				Dias.get(i).setDh(
						CalcDefHidr(Dias.get(i).getCta(), Dias.get(i).getAd()));
			} else {
				if (Dias.get(iant).getEh() > 0) {

					Dias.get(i).setDh(0);

				} else {
					Dias.get(i).setDh(
							CalcDefHidr(Dias.get(i).getCta(), Dias.get(i)
									.getAd()));

				}
			}

			// Calculando Excesso Hidrico

			if (i == 0) {

				Dias.get(i).setEh(0);

			} else {
				Dias.get(i).setEh(
						CalcExcHidr(Dias.get(i).getPe(), Dias.get(i).getEtr(),
								Dias.get(i).getDelta_ad(), Dias.get(i).getAd(),
								Dias.get(i).getCta()));

			}
			Log.i("BHC",
					BalancoHidrico.DateMysqlToDateBr(Dias.get(i).getData())
							+ ":" + format2.format(Dias.get(i).getPe()) + ":"
							+ format2.format(Dias.get(i).getEt0()) + ":"
							+ format2.format(Dias.get(i).getEtr()) + ":"
							+ format2.format(Dias.get(i).getAd()) + ":"
							+ format2.format(Dias.get(i).getUm()) + ":"
							+ format2.format(Dias.get(i).getDh()) + ":"
							+ format2.format(Dias.get(i).getEh()));
		}

	}

	public void FinalizaBH() {
		LinhaDia = Dias.size() - 1;
		BttCalcAnt.setEnabled(true);
		BttCalcUlt.setEnabled(true);
		BttCalcProx.setEnabled(true);
		BttCalcPrim.setEnabled(true);
		setResultadoDados();
		Log.v(BalancoHidrico.CATEGORIA, "Total de dias:" + Dias.size());

	}

	/**
	 * Faz a seleção dos dados de clima, e carrega os dados no Array de Dias.
	 * 
	 * @param dt_inicial
	 * @param dt_final
	 */
	public void SelDataBH(String dt_inicial, String dt_final) {
		Cursor c;
		String formato_sql = "yyyy-MM-dd";
		SimpleDateFormat dateFormatSql = new SimpleDateFormat(formato_sql);

		Date prev_fim_cultura, entrada_fim_cultura;

		prev_fim_cultura = BalancoHidrico.StrDateMysqlToDate(dt_inicial,
				cultura.duracao);
		entrada_fim_cultura = BalancoHidrico.StrDateMysqlToDate(dt_final, 0);

		if (entrada_fim_cultura.after(prev_fim_cultura)) {
			Log.v(BalancoHidrico.CATEGORIA, "hehData criada: "
					+ prev_fim_cultura.toString());
			dt_final = dateFormatSql.format(prev_fim_cultura);
			Log.v(BalancoHidrico.CATEGORIA, dt_final);
		} else {
			Log.v(BalancoHidrico.CATEGORIA, "hehData criada: "
					+ entrada_fim_cultura.toString());
		}

		c = database.rawQuery("SELECT * FROM clima WHERE dia >='" + dt_inicial
				+ "' AND dia <= '" + dt_final + "'", null);
		c.moveToFirst();

		do {

			Dias.add(c.getPosition(),
					new DiaBh(c.getString(c.getColumnIndex("dia"))));
			Dias.get(c.getPosition()).setChuva(
					c.getDouble(c.getColumnIndex("chuva_est")));
			Dias.get(c.getPosition()).setChuva_local(
					c.getDouble(c.getColumnIndex("chuva_local")));
			Dias.get(c.getPosition()).setRadia(
					c.getDouble(c.getColumnIndex("radia")));
			Dias.get(c.getPosition()).setTemp(
					c.getDouble(c.getColumnIndex("temp")));
			Dias.get(c.getPosition()).setVento2(
					c.getDouble(c.getColumnIndex("vento2")));
			Dias.get(c.getPosition()).setUrel(
					c.getInt(c.getColumnIndex("urel")));
			c.moveToNext();
		} while (!c.isLast());

	}

	/**
	 * 
	 * @param nome_solo
	 *            recebe o nome do solo selecionado
	 */
	public void SelSolo(String nome_solo) {
		float cc, ds, pm;
		int espessura;
		String nome;

		Cursor query;

		String[] col = { "*" };
		query = database.query("solo", col, "nome = '" + nome_solo + "'", null,
				null, null, null);
		query.moveToFirst();
		cc = (query.getFloat(query.getColumnIndex("cc")));
		ds = (query.getFloat(query.getColumnIndex("ds")));
		espessura = (query.getInt(query.getColumnIndex("espessura")));
		nome = (query.getString(query.getColumnIndex("nome")));
		pm = (query.getFloat(query.getColumnIndex("pm")));
		solo = new Solo(nome, espessura, cc, pm, ds);
	}

	/**
	 * Calcula a evapotranspiração pelo método de Penman-Monteith
	 * 
	 * @param temp
	 *            Temperatura média diária em ºC.
	 * @param umidade
	 *            Umidade média diária em %.
	 * @param vento2
	 *            velocidade média do vento a 2 metros de altura em m/s.
	 * @param radia
	 *            saldo de radiação líquida do dia em MJ/dia.
	 * @return Evapostranspiração do dia.
	 */
	public double CalcEt0(double temp, int umidade, double vento2, double radia) {
		double et0, es, ea, delta;
		es = Math.exp((17.27 * temp) / (temp + 237.3)) * 0.6108;
		ea = es * umidade / 100;
		delta = (es * 4098) / Math.pow((temp + 237), 2);
		et0 = ((0.408 * delta * radia) + (0.0639 * 900 / (temp + 273) * vento2 * (es - ea)))
				/ ((0.34 * vento2 + 1) * 0.0639 + delta);
		return et0;
	}

	/**
	 * Calcula a capacidade total de armazenamento do solo.
	 * 
	 * @param solo
	 *            Objeto solo.
	 * @return capacidade total de armazenamento de água.
	 */
	public double CalcCta(Solo solo) {
		double cta;
		cta = (solo.getCc() - solo.getPm()) / 10 * solo.getDs()
				* solo.getEspessura();
		return cta;
	}

	/**
	 * Calcula a precipitação efetiva pelo método do escorrimento superficial.
	 * 
	 * @param chuva
	 *            quantidade d chuva em mm.
	 * @param aguaDisp
	 *            quantidade de água disponível.
	 * @param cta
	 *            capacidade máxima de armazenamento de água.
	 * @return quantidade real de chuva ocirrido e considerado pelo BHC.
	 */
	public double CalcPrecEfet(double chuva, double aguaDisp, double cta) {
		double esc_super, constante = 25.4, aux;
		aux = chuva / constante;
		if (chuva >= MAX_CHUVA_EFETIVA) {

			esc_super = (aux - (0.9177 + 1.8111 * Math.log10(aux) - 0.97
					* Math.log10(aux) * aguaDisp / cta))
					* constante;
			return chuva - esc_super;
		} else {
			return chuva;
		}

	}

	/**
	 * Ajusta a evapotranspiração de acordo com o solo selecionado
	 * 
	 * @param umAnt
	 *            umidade do solo no dia anterior.
	 * @param cc
	 *            capacidade de campo do solo.
	 * @param pm
	 *            ponto de murcha do solo.
	 * @return Coeficiente de ajuste de solo.
	 */
	public double CalcKs(double umAnt, double cc, double pm) {
		return (umAnt - pm) / (cc - pm);
	}

	/**
	 * Ajusta a evapotraspiração de acordo com o solo e a cultura.
	 * 
	 * @param et0
	 *            evapotrabspiração de referência.
	 * @param kc
	 *            coeficiente de ajuste de cultura.
	 * @param ks
	 *            coeficiente de ajuste do solo.
	 * @return evapotranspiração real.
	 */
	public double CalcEtr(double et0, double kc, double ks) {
		return et0 * kc * ks;
	}

	/**
	 * Calcula a quantidade de água disponivel no solo.
	 * 
	 * @param aguaDisp
	 *            quantidade de água disponivel no dia anterior em mm.
	 * @param precEfetiva
	 *            quantidade de chuva efetiva no dia em mm.
	 * @param etr
	 *            quantidade de evapotranspiração real do dia em mm.
	 * @param cta
	 *            capacidade total de armazenamento do solo em mm.
	 * @return a quantidade de água disponivel no solo em mm
	 */
	public double CalcAguaDisp(double aguaDisp, double precEfetiva, double etr,
			double cta) {
		double aux;
		aux = aguaDisp + precEfetiva - etr;
		if (aux > cta) {
			return cta;
		} else {
			return aux;
		}
	}

	/**
	 * Calcula a umidade do solo.
	 * 
	 * @param ad
	 *            quantidade d água disponível em mm.
	 * @param ds
	 *            densidade do solo.
	 * @param z_esp
	 *            espessura do solo em cm.
	 * @param pm
	 *            ponto de murcha.
	 * @return umidade do solo.
	 */
	public double CalcUmSolo(double ad, double ds, double z_esp, double pm) {
		double umSolo;
		umSolo = ((ad / (ds * z_esp)) * 10) + pm;
		return umSolo;

	}

	/**
	 * Calcula a deficiência hídrica do solo.
	 * 
	 * @param cta
	 *            capacidade total de armazenamento.
	 * @param ad
	 *            quantidade de água disponpivel no solo.
	 * @return deficiencia hídrica do solo em mm.
	 */
	public double CalcDefHidr(double cta, double ad) {

		return cta - ad;

	}

	/**
	 * Calcula a quantidade de água que excedeu do solo durante o dia.
	 * 
	 * @param precEfetiva
	 *            quantidade de chuva efetiva em mm.
	 * @param etr
	 *            evapotranspiração real em mm.
	 * @param delta_ad
	 *            variação de água disponível no solo em mm.
	 * @param ad
	 *            quantidade de água disponível no solo em mm.
	 * @param cta
	 *            capacidade total de armazenamento.
	 * @return Excedente hídrico do solo.
	 */
	public double CalcExcHidr(double precEfetiva, double etr, double delta_ad,
			double ad, double cta) {
		double eh_hidr;
		if (ad == cta) {
			eh_hidr = precEfetiva - etr - delta_ad;
			if (eh_hidr < 0) {
				return 0;
			} else {
				return eh_hidr;
			}

		} else {
			return 0;
		}
	}

	/**
	 * 
	 * @return retorna dados do dia de acordo com a indexação.
	 */
	public ArrayList<DiaBh> getDias() {
		return Dias;
	}

	/**
	 * 
	 * @param dias
	 */
	public void setDias(ArrayList<DiaBh> dias) {
		Dias = dias;
	}

	/**
	 * 
	 * @return solo selecionado
	 */
	public Solo getSolo() {
		return solo;
	}

	/**
	 * 
	 * @param solo
	 */
	public void setSolo(Solo solo) {
		this.solo = solo;
	}

	/**
	 * Mostra os resultados dos cálculos nos campos em modo texto.
	 */
	public void setResultadoDados() {
		if ((LinhaDia >= 1) && (LinhaDia <= (Dias.size() - 2))) {
			BttCalcAnt.setEnabled(true);
			BttCalcProx.setEnabled(true);
			BttCalcPrim.setEnabled(true);
			BttCalcUlt.setEnabled(true);
		} else {
			if (LinhaDia == 0) {
				BttCalcAnt.setEnabled(false);
				BttCalcPrim.setEnabled(false);
				BttCalcProx.setEnabled(true);
				BttCalcUlt.setEnabled(true);
			}
			if (LinhaDia == (Dias.size() - 1)) {
				BttCalcProx.setEnabled(false);
				BttCalcUlt.setEnabled(false);
				BttCalcPrim.setEnabled(true);
				BttCalcAnt.setEnabled(true);
			}
		}

		// text results
		ValDia.setText(BalancoHidrico.DateMysqlToDateBr(Dias.get(LinhaDia)
				.getData()));
		ValEt0.setText(format2.format(Dias.get(LinhaDia).getEt0()));
		ValEtr.setText(format2.format(Dias.get(LinhaDia).getEtr()));

		ValUSolo.setText(format2.format(Dias.get(LinhaDia).getUm()));

		ValDefH.setText(format2.format(Dias.get(LinhaDia).getDh()));
		ValExcH.setText(format2.format(Dias.get(LinhaDia).getEh()));
		ValAguaD.setText(format2.format(Dias.get(LinhaDia).getAd()));
		ValPe.setText(format2.format(Dias.get(LinhaDia).getPe()));
		// fim text result

	}

	/*
	 * public double UmidadeSoloPorCento(double cta, double pm, double ad) {
	 * double umsolopercent;
	 * 
	 * umsolopercent = (100 * ad) / (cta - pm);
	 * 
	 * return umsolopercent; }
	 */

	private OnClickListener onClickGraph = new Button.OnClickListener() {
		@Override
		public void onClick(View view) {
			// TODO Auto-generated method stub
			openOptionsMenu();
		}

	};

	private OnClickListener onClickCalcOk = new Button.OnClickListener() {

		@Override
		public void onClick(View view) {
			// TODO Auto-generated method stub
			Init_Thread_Calc();
		}

	};

	private OnClickListener onClickCalcAnt = new Button.OnClickListener() {

		@Override
		public void onClick(View view) {
			// TODO Auto-generated method stub
			LinhaDia--;
			setResultadoDados();
		}
	};

	private OnClickListener onClickCalcProx = new Button.OnClickListener() {

		@Override
		public void onClick(View view) {
			// TODO Auto-generated method stub
			LinhaDia++;
			setResultadoDados();
		}

	};

	private OnClickListener onClickCalcPrim = new Button.OnClickListener() {

		@Override
		public void onClick(View view) {
			// TODO Auto-generated method stub
			LinhaDia = 0;
			setResultadoDados();
		}

	};

	private OnClickListener onClickCalcUlt = new Button.OnClickListener() {

		@Override
		public void onClick(View view) {
			// TODO Auto-generated method stub
			LinhaDia = Dias.size() - 1;
			setResultadoDados();
		}

	};

	/**
 * 
 */
	public boolean onCreateOptionsMenu(Menu menul) {
		menu = menul;
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menugraficos, menul);
		return true;
	}

	/**
 * 
 */
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.GAd:
			try {

				float um[] = new float[Dias.size()];
				String dia[] = new String[Dias.size()];
				for (int i = 0; i < Dias.size(); i++) {
					um[i] = (float) Dias.get(i).getAd();
					dia[i] = Dias.get(i).getData();
					Log.v("DADOS", "" + um[i]);
				}
				intGraph.putExtra("min", 0);
				intGraph.putExtra("max", (float) Dias.get(0).getCta());
				intGraph.putExtra("dia", dia);
				intGraph.putExtra("qtde", Dias.size());
				intGraph.putExtra("dados", um);
				intGraph.putExtra("tipo", DadosBHGraph.GRAFICO_AD);
				startActivity(intGraph);
				return true;
			} catch (Throwable e) {
				// TODO Auto-generated catch block
				showAlert("Simule um balanço hídrico antes de ver o gráfico",
						"Erro");
				e.printStackTrace();
			}
			break;
		case R.id.GDef:
			try {
				float um[] = new float[Dias.size()];
				String dia[] = new String[Dias.size()];
				for (int i = 0; i < Dias.size(); i++) {
					um[i] = (float) Dias.get(i).getDh();
					dia[i] = Dias.get(i).getData();
					Log.v("DADOS", "" + um[i]);
				}
				intGraph.putExtra("min", 0);
				intGraph.putExtra("max", (float) Dias.get(0).getCta());
				intGraph.putExtra("dia", dia);
				intGraph.putExtra("qtde", Dias.size());
				intGraph.putExtra("dados", um);
				intGraph.putExtra("tipo", DadosBHGraph.GRAFICO_DF);
				startActivity(intGraph);

				return true;
			} catch (Throwable e) {
				// TODO Auto-generated catch block
				showAlert("Simule um balanço hídrico antes de ver o gráfico",
						"Erro");
				e.printStackTrace();
			}
			break;
		case R.id.GUmin:
			try {
				float um[] = new float[Dias.size()];
				String dia[] = new String[Dias.size()];
				for (int i = 0; i < Dias.size(); i++) {
					um[i] = (float) Dias.get(i).getUm();
					dia[i] = Dias.get(i).getData();
					Log.v("DADOS", "" + um[i]);
				}
				intGraph.putExtra("min", (float) solo.getPm());
				intGraph.putExtra("max", (float) solo.getCc());
				intGraph.putExtra("dia", dia);
				intGraph.putExtra("qtde", Dias.size());
				intGraph.putExtra("dados", um);
				intGraph.putExtra("tipo", DadosBHGraph.GRAFICO_UM);
				startActivity(intGraph);

				return true;
			} catch (Throwable e) {
				// TODO Auto-generated catch block
				showAlert("Simule um balanço hídrico antes de ver o gráfico",
						"Erro");
				e.printStackTrace();
			}
			break;

		default:
			return true;
		}
		return false;
	}

	/**
	 * 
	 * @param msgerro
	 * @param titulo
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

	public void Init_Thread_Calc() {
		pd = ProgressDialog.show(this, "Aguarde",
				"Processando dados de Clima, Solo e Cultura", true);
		Thread thread = new Thread(runnable);
		thread.start();

	}

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message message) {
			pd.dismiss();
			FinalizaBH();
		}
	};
	private Handler handmsg = new Handler() {
		@Override
		public void handleMessage(Message message) {
			if (message.arg1 == 0) {
				pd.setMessage("Realizando cálculo do BHC");
			}

		}
	};

}
