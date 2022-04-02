package br.com.campominado.app.modelo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import br.com.campominado.app.excecao.ExplosaoException;

public class Tabuleiro {

	private int linhas;
	private int colunas;
	private int minas;
	
	private final List<Campo> campos = new ArrayList<>();

	public Tabuleiro(int linhas, int colunas, int minas) {
		this.linhas = linhas;
		this.colunas = colunas;
		this.minas = minas;
		
		gerarCampos();
		associarVizinhos();
		sortearMinas();
	}

	private void gerarCampos() {
		for (int l = 0; l < linhas; l++) {
			for (int c = 0; c < colunas; c++) {
				campos.add(new Campo(l, c));
			}
		}
	}
	
	private void associarVizinhos() {
		for (int i = 0; i < (campos.size() - 1); i++) {
			for (int j = i + 1; j < campos.size(); j++) {
				campos.get(i).adicionarVizinho(campos.get(j));
			}
		}
	}
	
	private void sortearMinas() {
		long minasArmadas = 0;
		Predicate<Campo> minados = c -> c.estaMinado();
		
		do {
			int novoMinado = (int) (Math.random() * campos.size());
			campos.get(novoMinado).minar();
			minasArmadas = campos.stream().filter(minados).count();
			
		} while (minasArmadas < minas);
	}
	
	public int getLinhas() {
		return linhas;
	}

	public int getColunas() {
		return colunas;
	}

	public int getMinas() {
		return minas;
	}

	public List<Campo> getCampos() {
		return campos;
	}

	public Optional<Campo> buscarCampo(int linha, int coluna) {
		return campos.parallelStream()
			.filter(c -> c.getLinha() == linha && c.getColuna() == coluna)
			.findFirst();
	}
	
	public void abrir(int linha, int coluna) {
		try {
			buscarCampo(linha, coluna)
				.ifPresent(c -> c.abrir());
		
		} catch (ExplosaoException e) {
			campos.stream()
				.filter(c -> c.estaMinado())
				.forEach(c -> c.setAberto(true));
			throw e;
		}
	}
	
	public void alternarMarcacao(int linha, int coluna) {
		buscarCampo(linha, coluna)
			.ifPresent(c -> c.alternarMarcacao());
	}
	
	public boolean objetivoAlcancado() {
		return campos.stream().allMatch(c -> c.objetivoAlcancado());
	}
	
	public void reiniciar() {
		campos.stream().forEach(c -> c.reiniciar());
		sortearMinas();
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("    ");
		for (int c = 0; c < colunas; c++) {
			sb.append("_");
			sb.append(c);
			sb.append("_");			
		}
		sb.append("\n");
		int i = 0;
		
		for (int l = 0; l < linhas; l++) {
			
			sb.append(" ");
			sb.append(l);
			sb.append(" |");
			
			for (int c = 0; c < colunas; c++) {
				sb.append(" ");
				sb.append(campos.get(i));
				sb.append(" ");
				i++;
			}
			sb.append("\n");
		}
		return sb.toString();
	}
}
