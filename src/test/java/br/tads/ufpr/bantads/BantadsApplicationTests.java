package br.tads.ufpr.bantads;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.modulith.core.ApplicationModules;
import org.springframework.modulith.test.ApplicationModuleTest;

@SpringBootTest
class BantadsApplicationTests {
	ApplicationModules modules = ApplicationModules.of(BantadsApplication.class);

	@Test
	void contextLoads() {
		modules.verify();
	}

}
