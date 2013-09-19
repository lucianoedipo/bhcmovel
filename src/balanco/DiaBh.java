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
 * @author Luciano ï¿½dipo Pereira da Silva
 * 
 */
public class DiaBh {
	private String data;
	private double chuva, chuva_local;
	private int urel;
	private double radia, temp, vento2;
	private double et0;
	private double cta;
	private double pe;
	private double ks;
	private double etr;
	private double ad;
	private double Delta_ad;
	private double um;
	private double dh;
	private double eh;
	private double kc;
	private double cc;
	private double pm;

	/**
	 * Construtor do objeto DiaBh com dia obrigatï¿½rio.
	 * 
	 * @param dia
	 *            dia para instï¿½ncia do objeto
	 */
	public DiaBh(String dia) {
		this.data = dia;
	}

	/**
	 * Construtor do objeto sem nenhum parï¿½metro.
	 */
	public DiaBh() {

	}

	/**
	 * 
	 * @return retorna o dia.
	 */
	public String getData() {
		return data;
	}

	/**
	 * 
	 * @return retorna umidade relativa em %.
	 */
	public int getUrel() {
		return urel;
	}

	/**
	 * 
	 * @param urel
	 *            umidade relativa em %.
	 */
	public void setUrel(int urel) {
		this.urel = urel;
	}

	/**
	 * 
	 * @return radiaï¿½ï¿½o lï¿½quida em MJ/dia.
	 */
	public double getRadia() {
		return radia;
	}

	/**
	 * 
	 * @param radia
	 *            radiaï¿½ï¿½o lï¿½quida em MJ/dia.
	 */
	public void setRadia(double radia) {
		this.radia = radia;
	}

	/**
	 * 
	 * @return temperatura mï¿½dia em ï¿½C.
	 */
	public double getTemp() {
		return temp;
	}

	/**
	 * 
	 * @param temp
	 *            temperatura mï¿½dia em ï¿½C.
	 */
	public void setTemp(double temp) {
		this.temp = temp;
	}

	/**
	 * 
	 * @return velocidade mï¿½dia do vento a 2 metros de altura em m/s.
	 */
	public double getVento2() {
		return vento2;
	}

	/**
	 * 
	 * @param vento2
	 *            velocidade mï¿½dia do vento a 2 metros de altura em m/s.
	 */
	public void setVento2(double vento2) {
		this.vento2 = vento2;
	}

	/**
	 * 
	 * @param data
	 *            dia;
	 */
	public void setData(String data) {
		this.data = data;
	}

	/**
	 * 
	 * @return evapotranspiraï¿½ï¿½o em mm.
	 */
	public double getEt0() {
		return et0;
	}

	/**
	 * 
	 * @param et0
	 *            evapotranspiraï¿½ï¿½o em mm.
	 */
	public void setEt0(double et0) {
		this.et0 = et0;
	}

	/**
	 * 
	 * @return capacidade total de armazenamento.
	 */
	public double getCta() {
		return cta;
	}

	/**
	 * 
	 * @param cta
	 *            capacidade total d armazenamento.
	 */
	public void setCta(double cta) {
		this.cta = cta;
	}

	/**
	 * 
	 * @return quantidade de chuva em mm.
	 */
	public double getChuva() {
		return chuva;
	}

	/**
	 * 
	 * @param chuva
	 *            quantidade de chuva em mm.
	 */
	public void setChuva(double chuva) {
		this.chuva = chuva;
	}

	/**
	 * 
	 * @return quantidade de chuva efetiva em mm.
	 */
	public double getPe() {
		return pe;
	}

	/**
	 * 
	 * @param pe
	 *            quantidade de chuva efetiva em mm.
	 */
	public void setPe(double pe) {
		this.pe = pe;
	}

	/**
	 * 
	 * @return coeficiente de ajuste do solo.
	 */
	public double getKs() {
		return ks;
	}

	/**
	 * 
	 * @param ks
	 *            coeficiente de ajuste do solo.
	 */
	public void setKs(double ks) {
		this.ks = ks;
	}

	/**
	 * 
	 * @return evapotranspiraï¿½ï¿½o real em mm.
	 */
	public double getEtr() {
		return etr;
	}

	/**
	 * 
	 * @param etr
	 *            evapotranspiraï¿½ï¿½o real em mm.
	 */
	public void setEtr(double etr) {
		this.etr = etr;
	}

	/**
	 * 
	 * @return quantidade de ï¿½gua disponï¿½vel em mm.
	 */
	public double getAd() {
		return ad;
	}

	/**
	 * 
	 * @param ad
	 *            quantidade de ï¿½gua disponï¿½vel em mm.
	 */
	public void setAd(double ad) {
		this.ad = ad;
	}

	/**
	 * pega quantidade de ï¿½gua do dia atual menos a quantidade de ï¿½gua do dia
	 * anterior.
	 * 
	 * @return variaï¿½ï¿½o de ï¿½gua disponï¿½vel em mm.
	 */
	public double getDelta_ad() {
		return Delta_ad;
	}

	/**
	 * seta a quantidade de ï¿½gua do dia atual menos a quantidade de ï¿½gua do dia
	 * anterior.
	 * 
	 * @param delta_ad
	 *            variaï¿½ï¿½o de ï¿½gua disponï¿½vel em mm.
	 */
	public void setDelta_ad(double delta_ad) {
		Delta_ad = delta_ad;
	}

	/**
	 * pega a umidade do solo.
	 * 
	 * @return umidade do solo em mm.
	 */
	public double getUm() {
		return um;
	}

	/**
	 * seta a umidade do solo no dia.
	 * 
	 * @param um
	 *            umidade do solo no dia em mm.
	 */
	public void setUm(double um) {
		this.um = um;
	}

	/**
	 * 
	 * @return defificï¿½ncia hï¿½drica em mm.
	 */
	public double getDh() {
		return dh;
	}

	public void setDh(double dh) {
		this.dh = dh;
	}

	public double getEh() {
		return eh;
	}

	public void setEh(double eh) {
		this.eh = eh;
	}

	public double getKc() {
		return kc;
	}

	public void setKc(double kc) {
		this.kc = kc;
	}

	public double getCc() {
		return cc;
	}

	public void setCc(double cc) {
		this.cc = cc;
	}

	public double getPm() {
		return pm;
	}

	public void setPm(double pm) {
		this.pm = pm;
	}

	public void setChuva_local(double chuva_local) {
		this.chuva_local = chuva_local;
	}

	public double getChuva_local() {
		return chuva_local;
	}

}
