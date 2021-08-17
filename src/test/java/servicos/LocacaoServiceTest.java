package servicos;


import static buiders.FilmeBuilder.umFilme;
import static buiders.FilmeBuilder.umFilmeSemEstoque;
import static buiders.UsuarioBuilder.umUsuario;
import static matchers.MatchersProprios.caiNumaSegunda;
import static matchers.MatchersProprios.ehHoje;
import static matchers.MatchersProprios.ehHojeComDiferencaDias;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assume.assumeFalse;
import static org.junit.Assume.assumeTrue;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;

import buiders.FilmeBuilder;
import buiders.UsuarioBuilder;
import buildermaster.BuilderMaster;
import entidades.Filme;
import entidades.Locacao;
import entidades.Usuario;
import exceptions.FilmeSemLocacaoException;
import exceptions.LocadoraException;
import utils.DataUtils;

public class LocacaoServiceTest {
	
	private LocacaoService service;
	
	@Rule
	public ErrorCollector error = new ErrorCollector();
	
	@Rule
	public ExpectedException exception = ExpectedException.none();
	
	@Before
	public void setup() {
		service = new LocacaoService();
	}
	
	@Test
	public void deveAlugarFilme() throws Exception {
		assumeFalse(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY));
		
		//cenario
		Usuario usuario = umUsuario().agora();
		List<Filme> filmes = Arrays.asList(umFilme().comValor(5.0).agora());
		
		//açao
		Locacao locacao = service.alugarFilme(usuario, filmes);
			
		//verificaçao
		error.checkThat(locacao.getValor(), is(CoreMatchers.equalTo(5.0)));
		error.checkThat(locacao.getDataLocacao(), ehHoje());
		error.checkThat(locacao.getDataRetorno(), ehHojeComDiferencaDias(1));
	}
	
	@Test(expected = FilmeSemLocacaoException.class)
	public void naoDeveAlugarFilmeSemEstoque() throws Exception {
		//cenario
		Usuario usuario = umUsuario().agora();
		List<Filme> filmes = Arrays.asList(umFilmeSemEstoque().agora());
				
		//açao
		service.alugarFilme(usuario, filmes);
	}
	
	@Test
	public void naoDeveAlugarFilmeSemUsuario() throws FilmeSemLocacaoException {
		//cenario
		List<Filme> filmes = Arrays.asList(umFilme().agora());
		
		//acao
			try {
				service.alugarFilme(null, filmes);
				Assert.fail();
			} catch (LocadoraException e) {
				assertThat(e.getMessage(), is("Usuário vazio"));
			}
	}
	
	@Test
	public void naoDeveAlugarFilmeSemFilme() throws FilmeSemLocacaoException, LocadoraException {
		//cenario
		Usuario usuario = umUsuario().agora();
		
		exception.expect(LocadoraException.class);
		exception.expectMessage("Filme vazio");
		
		//acao
		service.alugarFilme(usuario, null);
	}
	
	@Test
	public void deveDevolverNaSegundaAoAlugarNoSabado() throws FilmeSemLocacaoException, LocadoraException {
		
		assumeTrue(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY));
		
		//cenario
		Usuario usuario = umUsuario().agora();
		List<Filme> filmes = Arrays.asList(umFilme().agora());
		
		//acao
		Locacao retorno = service.alugarFilme(usuario, filmes);
		
		//verificacao
		assertThat(retorno.getDataRetorno(), caiNumaSegunda());
	}
	
	public static void main(String[] args) {
		new BuilderMaster().gerarCodigoClasse(Locacao.class);
	}
}
