package suites;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import servicos.CalculadoraTest;
import servicos.CalculoValorLocacaoTest;
import servicos.LocacaoServiceTest;

@RunWith(Suite.class)
@SuiteClasses({
	CalculadoraTest.class,
	CalculoValorLocacaoTest.class,
	LocacaoServiceTest.class
})
public class SuiteExecucao {
	//Remova se puder!
}
