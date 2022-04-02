package br.com.campominado.app.modelo;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import br.com.campominado.app.excecao.ExplosaoException;

class CampoTestes {

	private Campo campo;
	
	@BeforeEach
	void iniciarCampo() {
		this.campo = new Campo(3, 3);
	}
	
	boolean adicionarVizinhoTeste(int linha, int coluna) {
		Campo vizinho = new Campo(linha, coluna);
		return campo.adicionarVizinho(vizinho);
	}
	
	@Test
	void testeVizinhoSuperior() {
		assertTrue(adicionarVizinhoTeste(2, 3));
	}
	
	@Test
	void testeVizinhoInferior() {
		assertTrue(adicionarVizinhoTeste(4, 3));
	}
	
	@Test
	void testeVizinhoDireita() {
		assertTrue(adicionarVizinhoTeste(3, 4));
	}
	
	@Test
	void testeVizinhoEsquerda() {
		assertTrue(adicionarVizinhoTeste(3, 2));
	}
	
	@Test
	void testeVizinhoSuperiorDireita() {
		assertTrue(adicionarVizinhoTeste(2, 4));
	}
	
	@Test
	void testeVizinhoSuperiorEsquerda() {
		assertTrue(adicionarVizinhoTeste(2, 2));
	}
	
	@Test
	void testeVizinhoInferiorDireita() {
		assertTrue(adicionarVizinhoTeste(4, 4));
	}
	
	@Test
	void testeVizinhoInferiorEsquerda() {
		assertTrue(adicionarVizinhoTeste(4, 2));
	}
	
	@Test
	void testeNaoVizinhoLinha() {
		assertFalse(adicionarVizinhoTeste(3, 5));
	}
	
	@Test
	void testeNaoVizinhoColuna() {
		assertFalse(adicionarVizinhoTeste(5, 3));
	}
	
	@Test
	void testeNaoVizinhoDiagonal() {
		assertFalse(adicionarVizinhoTeste(1, 1));
	}
	
	@Test
	void testeMarcadoPadrao() {
		assertFalse(campo.estaMarcado());
	}
	
	@Test
	void testeAlternarMarcacao() {
		campo.alternarMarcacao();
		assertTrue(campo.estaMarcado());
	}
	
	@Test
	void testeAlternarMarcacaoDuasVezes() {
		campo.alternarMarcacao();
		campo.alternarMarcacao();
		assertFalse(campo.estaMarcado());
	}
	
	@Test
	void testeNaoAlternarMarcacao() {
		campo.abrir();
		assertFalse(campo.alternarMarcacao());
	}
	
	@Test
	void testeAbrirNaoMinadoNaoMarcado() {
		assertTrue(campo.abrir());
	}
	
	@Test
	void testeAbrirNaoMinadoMarcado() {
		campo.alternarMarcacao();
		assertFalse(campo.abrir());
	}
	
	@Test
	void testeAbrirMinadoMarcado() {
		campo.alternarMarcacao();
		campo.minar();
		assertFalse(campo.abrir());
	}
	
	@Test
	void testeAbrirMinadoNaoMarcado() {
		campo.minar();
		assertThrows(ExplosaoException.class, () -> {
			campo.abrir();
		});		
	}
	
	@Test
	void testeAbrirVizinhos() {
		Campo campo11 = new Campo(1, 1);
		Campo campo22 = new Campo(2, 2);
		
		campo.adicionarVizinho(campo22);
		campo22.adicionarVizinho(campo11);
		
		campo.abrir();
		assertTrue(campo11.estaAberto() && campo22.estaAberto());
	}
	
	@Test
	void testeAbrirVizinhosComMinado() {
		Campo campo11 = new Campo(1, 1);
		Campo campo12 = new Campo(1, 2);
		campo12.minar();
		Campo campo22 = new Campo(2, 2);
		
		campo.adicionarVizinho(campo22);
		campo22.adicionarVizinho(campo11);
		campo22.adicionarVizinho(campo12);
		campo11.adicionarVizinho(campo12);
		
		campo.abrir();
		assertTrue(campo22.estaAberto() && campo11.estaFechado() && campo12.estaFechado());
	}
	
	@Test
	void testeReiniciarMarcacao() {
		campo.alternarMarcacao();
		campo.reiniciar();
		assertFalse(campo.estaMarcado());
	}
	
	@Test
	void testeReiniciarMinar() {
		campo.minar();
		campo.reiniciar();
		assertFalse(campo.estaMinado());
	}
	
	@Test
	void testeReiniciarAbrir() {
		campo.abrir();
		campo.reiniciar();
		assertTrue(campo.estaFechado());
	}
	
	@Test
	void testeVizinhnoMinado() {
		Campo vizinho = new Campo(2, 2);
		vizinho.minar();
		campo.adicionarVizinho(vizinho);
		assertEquals(1, campo.minasNaVizinhanca());
	}
	
	@Test
	void testeVizinhnoNaoMinado() {
		Campo vizinho = new Campo(2, 2);
		campo.adicionarVizinho(vizinho);
		assertEquals(0, campo.minasNaVizinhanca());
	}
	
	@Test
	void testeNaoVizinhnoMinado() {
		Campo vizinho = new Campo(1, 1);
		vizinho.minar();
		campo.adicionarVizinho(vizinho);
		assertEquals(0, campo.minasNaVizinhanca());
	}
	
	@Test
	void testeNaoVizinhnoNaoMinado() {
		Campo vizinho = new Campo(1, 1);
		campo.adicionarVizinho(vizinho);
		assertEquals(0, campo.minasNaVizinhanca());
	}
	
	@Test
	void testeObjetivoAlcancadoDesvendado() {
		campo.abrir();
		assertTrue(campo.objetivoAlcancado());
	}
	
	@Test
	void testeObjetivoAlcancadoProtegido() {
		campo.minar();
		campo.alternarMarcacao();
		assertTrue(campo.objetivoAlcancado());
	}
	
	@Test
	void testeObjetivoNaoAlcancadoExplosao() {
		campo.minar();
		assertThrows(ExplosaoException.class, () -> {
			campo.abrir();
		});	
		assertFalse(campo.objetivoAlcancado());
	}
	
	@Test
	void testeObjetivoNaoAlcancadoFechado() {
		assertFalse(campo.objetivoAlcancado());
	}
	
	@Test
	void testeImprimirMarcado() {
		campo.alternarMarcacao();
		assertEquals("X", campo.toString());
	}
	
	@Test
	void testeImprimirFechado() {
		assertEquals("?", campo.toString());
	}
	
	@Test
	void testeImprimirAbertoMinado() {
		campo.minar();
		assertThrows(ExplosaoException.class, () -> {
			campo.abrir();
		});	
		assertEquals("*", campo.toString());
	}
	
	@Test
	void testeImprimirAbertoVizinhancaSegura() {
		Campo vizinho = new Campo(2, 2);
		campo.adicionarVizinho(vizinho);
		campo.abrir();
		assertEquals(" ", campo.toString());
	}
	
	@Test
	void testeImprimirAbertoVizinhancaNaoSegura() {
		Campo vizinho = new Campo(2, 2);
		vizinho.minar();
		campo.adicionarVizinho(vizinho);
		campo.abrir();
		assertEquals("1", campo.toString());
	}
	
	@Test
	void testeNaoEstaFechado() {
		campo.abrir();
		assertFalse(campo.estaFechado());
	}
	
	@Test
	void testeNaoMinar() {
		campo.minar();
		campo.naoMinar();
		assertFalse(campo.estaMinado());
	}
	
	@Test
	void testeCamposIguais() {
		assertTrue(campo.equals(campo));
	}
	
	@Test
	void testeCamposDiferentes() {
		Campo outroCampo = new Campo(2, 2);
		outroCampo.minar();
		outroCampo.adicionarVizinho(new Campo(1, 1)); 
		assertFalse(campo.equals(outroCampo));
	}
	
	@Test
	void testeCamposDiferentesNull() {
		assertFalse(campo.equals(null));
	}
	
	@Test
	void testeCamposDiferentesOutraClasse() {
		assertFalse(campo.equals(new StringBuilder()));
	}
	
	@Test
	void testeSetAberto() {
		campo.setAberto(true);
		assertTrue(campo.estaAberto());
	}
}
