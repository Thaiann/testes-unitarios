package servicos;

import static buiders.FilmeBuilder.umFilme;
import static buiders.UsuarioBuilder.umUsuario;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import entidades.Filme;
import entidades.Locacao;
import entidades.Usuario;
import exceptions.FilmeSemLocacaoException;
import exceptions.LocadoraException;

@RunWith(Parameterized.class)
public class CalculoValorLocacaoTest {

	private LocacaoService service;
	
	@Parameter
	public List<Filme> filmes;
	
	@Parameter(value=1)
	public Double valroLocacao;
	
	@Parameter(value=2)
	public String cenario;
	
	@Before
	public void setup() {
		service = new LocacaoService();
	}
	
	private static Filme filme1 = umFilme().agora();
	private static Filme filme2 = umFilme().agora();
	private static Filme filme3 = umFilme().agora();
	private static Filme filme4 = umFilme().agora();
	private static Filme filme5 = umFilme().agora();
	private static Filme filme6 = umFilme().agora();
	private static Filme filme7 = umFilme().agora();
	
	@Parameters(name="{2}")
	public static Collection<Object[]> getParametros(){
		return Arrays.asList(new Object[][] {
			{Arrays.asList(filme1, filme2), 8.0, "2 Filmes: Sem Desconto"},
			{Arrays.asList(filme1, filme2, filme3), 11.0, "3 Filmes: 25%"},
			{Arrays.asList(filme1, filme2, filme3, filme4), 13.0, "4 Filmes: 50%"},
			{Arrays.asList(filme1, filme2, filme3, filme4, filme5), 14.0, "5 Filmes: 75%"},
			{Arrays.asList(filme1, filme2, filme3, filme4, filme5, filme6), 14.0, "6 Filmes: 100%"},
			{Arrays.asList(filme1, filme2, filme3, filme4, filme5, filme6, filme7), 18.0, "7 Filmes: Sem Desconto"}
		});
	}
	
	@Test
	public void deveCalcularValorLocacaoConciderandoDescontos() throws FilmeSemLocacaoException, LocadoraException {
		//cenario
		Usuario usuario = umUsuario().agora();
		
		
		//acao
		Locacao resultado = service.alugarFilme(usuario, filmes);
		
		//verificacao
		assertThat(resultado.getValor(), is(valroLocacao));
	}
}