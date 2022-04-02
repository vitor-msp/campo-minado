package br.com.campominado.app.modelo;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import br.com.campominado.app.excecao.ExplosaoException;

class TabuleiroTestes {

	private Tabuleiro tabuleiro;
	
	@BeforeEach
	void iniciarTabuleiro() {
		this.tabuleiro = new Tabuleiro(6, 6, 6);
	}
	
	@Test
	void testeBuscarCampoLinha() {
		Campo campo = tabuleiro.buscarCampo(1, 1).get();
		assertEquals(1, campo.getLinha());
	}
	
	@Test
	void testeBuscarCampoColuna() {
		Campo campo = tabuleiro.buscarCampo(1, 1).get();
		assertEquals(1, campo.getColuna());
	}
	
	@Test
	void testeAbrir() {
		tabuleiro.buscarCampo(1, 1).get().naoMinar();
		tabuleiro.abrir(1, 1);
		assertTrue(tabuleiro.buscarCampo(1, 1).get().estaAberto());
	}
	
	@Test
	void testeAlternarMarcacao() {
		tabuleiro.alternarMarcacao(1, 1);
		assertTrue(tabuleiro.buscarCampo(1, 1).get().estaMarcado());
	}
	
	@Test
	void testeObjetivoAlcancado() {
		tabuleiro.getCampos().stream().forEach(c -> {
			c.naoMinar();
			c.abrir();
		});
		assertTrue(tabuleiro.objetivoAlcancado());
	}
	
	@Test
	void testeReiniciar() {
		tabuleiro.alternarMarcacao(1, 1);
		tabuleiro.reiniciar();
		assertFalse(tabuleiro.buscarCampo(1, 1).get().estaMarcado());
	}

	@Test
	void testeGetLinhas() {
		assertEquals(6, tabuleiro.getLinhas());
	}

	@Test
	void testeGetColunas() {
		assertEquals(6, tabuleiro.getColunas());
	}

	@Test
	void testeGetMinas() {
		assertEquals(6, tabuleiro.getMinas());
	}
	
	@Test
	void testeAbrirExplosao() {
		tabuleiro.buscarCampo(1, 1).get().minar();
		assertThrows(ExplosaoException.class, () -> {
			tabuleiro.abrir(1, 1);
		});	
	}
	
	@Test
	void testeToString() {
		assertTrue(tabuleiro.toString().contains("?"));
	}
}
