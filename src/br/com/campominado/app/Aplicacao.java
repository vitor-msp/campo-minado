package br.com.campominado.app;

import br.com.campominado.app.modelo.Tabuleiro;
import br.com.campominado.app.visao.TabuleiroConsole;

public class Aplicacao {

	public static void main(String[] args) {
		
		Tabuleiro tabuleiro = new Tabuleiro(6, 6, 6);
		new TabuleiroConsole(tabuleiro);

	}
}
