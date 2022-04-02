package br.com.campominado.app.visao;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Scanner;

import br.com.campominado.app.excecao.EntradaInvalidaException;
import br.com.campominado.app.excecao.ExplosaoException;
import br.com.campominado.app.excecao.SairException;
import br.com.campominado.app.modelo.Tabuleiro;

public class TabuleiroConsole {

	private Tabuleiro tabuleiro;
	private Scanner entrada = new Scanner(System.in);
	
	public TabuleiroConsole(Tabuleiro tabuleiro) {
		this.tabuleiro =  tabuleiro;
		
		executarJogo();
	}

	private void executarJogo() {
		try {
			
			System.out.println("##############################################");
			System.out.println("################ CAMPO MINADO ################");
			System.out.println("##############################################");
			
			while(true) {
				executarPartida();

				System.out.println("\nJogar novamente? (S/n)");
				String digitado = entrada.next();
				
				if("n".equalsIgnoreCase(digitado)) {
					throw new SairException();
				}
				tabuleiro.reiniciar();
			}
			
		} catch (SairException e) {
			System.out.println("\nAté logo!!!");
		
		} finally {
			entrada.close();
		}
	}
	
	private void executarPartida() {
		try {
			
			while (!tabuleiro.objetivoAlcancado()) {
				
				try {
					System.out.println();
					System.out.println(tabuleiro);
					
					System.out.println("Digite 'sair' para abandonar o jogo.\n");
					String digitado = capturarEntrada("Posição do campo (linha, coluna):");

					Iterator<Integer> posicoes = Arrays.stream(digitado.split(","))
							.map(e -> {
								try {
									return Integer.parseInt(e.trim());
								} catch (NumberFormatException e1) {
									return null;
								}
							}).iterator();
					
					int linha = 0, coluna = 0;
					try {
						if(posicoes.hasNext()) {
							linha = posicoes.next();
							
							if(posicoes.hasNext()){
								coluna = posicoes.next();						
							}else {
								throw new EntradaInvalidaException();			
							}
						}else {
							throw new EntradaInvalidaException();
						}
					} catch (Exception e1) {
						throw new EntradaInvalidaException();
					}
					
					digitado = capturarEntrada("1 - abrir campo, 2 - (des)marcar campo:");

					if("1".equalsIgnoreCase(digitado)) {
						tabuleiro.abrir(linha, coluna);
						
					} else if("2".equalsIgnoreCase(digitado)) {
						tabuleiro.alternarMarcacao(linha, coluna);
						
					} else {
						throw new EntradaInvalidaException();
					}
					
				} catch (EntradaInvalidaException e) {
					System.out.println("");
					System.out.println("####################");
					System.out.println("Entrada inválida ;(");
					System.out.println("####################");
				}
			}
			System.out.println();
			System.out.println(tabuleiro);
			System.out.println("#############################");
			System.out.println("Parabéns!!! Você ganhou!!!");
			System.out.println("#############################");
			
		} catch (ExplosaoException e) {

			System.out.println();
			System.out.println(tabuleiro);
			System.out.println("#############################");
			System.out.println("Você perdeu!!!");
			System.out.println("#############################");
		}
	}
	
	private String capturarEntrada(String texto) {
		System.out.print(texto);
		String digitado = entrada.next();
		
		if("sair".equalsIgnoreCase(digitado)) {
			throw new SairException();
		}
		return digitado;
	}
}
