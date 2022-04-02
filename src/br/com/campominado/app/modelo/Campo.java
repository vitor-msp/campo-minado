package br.com.campominado.app.modelo;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import br.com.campominado.app.excecao.ExplosaoException;

public class Campo {

	private final int linha;
	private final int coluna;
	
	private boolean aberto = false;
	private boolean minado = false;
	private boolean marcado = false;	
	
	private List<Campo> vizinhos = new ArrayList<>();
	
	Campo(int linha, int coluna){
		this.linha = linha;
		this.coluna = coluna;
	}
	
	boolean adicionarVizinho(Campo vizinho) {
		boolean linhaDiferente = this.getLinha() != vizinho.getLinha();
		boolean colunaDiferente = this.getColuna() != vizinho.getColuna();
		boolean diagonal = linhaDiferente && colunaDiferente;
		
		int distLinha = Math.abs(vizinho.getLinha() - this.getLinha());
		int distColuna = Math.abs(vizinho.getColuna() - this.getColuna());
		double dist = distLinha + distColuna;
		
		if( (diagonal && dist == 2) || (!diagonal && dist == 1) ) {
			this.vizinhos.add(vizinho);
			vizinho.adicionarVizinhoMutuo(this);
			return true;
		}
		return false;
	}
	
	void adicionarVizinhoMutuo(Campo vizinho) {
		this.vizinhos.add(vizinho);
	}

	int getLinha() {
		return linha;
	}
	
	int getColuna() {
		return coluna;
	}
	
	void minar() {
		this.minado = true;
	}
	
	void naoMinar() {
		this.minado = false;
	}
	
	boolean alternarMarcacao() {
		if(!aberto) {
			marcado = !marcado;
			return true;
		}
		return false;
	}
	
	boolean estaMarcado() {
		return this.marcado;
	}

	boolean estaAberto() {
		return this.aberto;
	}
	
	boolean estaFechado() {
		return !estaAberto();
	}
	
	boolean estaMinado() {
		return minado;
	}
	
	void setAberto(boolean aberto) {
		this.aberto = aberto;
	}

	boolean abrir() {
		if(!aberto && !marcado) {
			aberto = true;
			
			if(minado) {
				throw new ExplosaoException();
			}
			if (vizinhancaSegura()) {
				vizinhos.forEach(v -> v.abrir());
			}
			return true;
		}
		return false;
	}
	
	boolean vizinhancaSegura() {
		return vizinhos.stream().noneMatch(v -> v.minado);
	}
	
	boolean objetivoAlcancado() {
		boolean desvendado = !minado && aberto;
		boolean protegido = minado  && marcado;
		return desvendado || protegido;
	}
	
	long minasNaVizinhanca() {
		return vizinhos.stream().filter(v -> v.estaMinado()).count();
	}
	
	void reiniciar() {
		aberto = false;
		minado = false;
		marcado = false;
	}
	
	@Override
	public String toString() {
		if(marcado) {
			return "X";
			
		}else if(aberto) {	
			
			if(minado) {
				return "*";				
			}
			
			Long minasViz = minasNaVizinhanca();
			
			if(minasViz > 0) {
				return Long.toString(minasViz);
				
			}else {
				return " ";
			}
		}else {
			return "?";
		}
	}

	@Override
	public int hashCode() {
		return Objects.hash(aberto, coluna, linha, marcado, minado, vizinhos);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Campo other = (Campo) obj;
		return aberto == other.aberto && coluna == other.coluna && linha == other.linha && marcado == other.marcado
				&& minado == other.minado && Objects.equals(vizinhos, other.vizinhos);
	}
}
