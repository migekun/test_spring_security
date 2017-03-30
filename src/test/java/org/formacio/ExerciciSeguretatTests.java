package org.formacio;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ExerciciSeguretatTests {

	@Autowired
	private MockMvc mockMvc;

	/**
	 * Observacio:
	 * 
	 * El seguent test fara invocacions a les urls de la vostra aplicacio i comprovara que se cumpleixen
	 * les condicions (com sempre). Pero ...
	 * Les dades d'autanticacio no los agafara de la vostra aplicacio. Aixo vol dir que el test no emprara
	 * el vostre domini de seguretat i, per tant, els usuaris que hagueu creat alla.
	 * Aixo vol dir que la part de configuracio amb el inMemoryAuthentication()... no es necessaria per el test.
	 * La podeu tenir si voleu provar l'aplicacio directament des del navegador, pero, repeteixo, NO es necessaria per el test.
	 * 
	 * Quan un metode de test esta anotat amb @WithMockUser(username="jose",roles="CLIENT"), vol dir que la peticio
	 * se fara amb un usuari que te username "jose" i rol "CLIENT", independentment de com hagueu configurat voltros
	 * el vostre domini de seguretat (i, per tant, encara que no hi hagui cap usuari "jose"!!)
	 */
	
	// Creau al controller un metode mapeijat a /info que retorni el nombre de persones que hi ha a la llista
	@Test
	public void test_public() throws Exception {
		mockMvc.perform(get("/info"))
		.andExpect(status().isOk())
		.andExpect(content().string("2"));
	}

	// Creau un metode que atengui a /admin/llista que retorni la llista de persones (com List<String>).
	// No fa falta que poseu cap atribut produces ni faceu res de xml o json. Facil, facil
	// Heu de fer que nomes un usuari amb perfil ADMIN hi pugui accedir
	
	// obs: els dos tests seguents proven el mateix metode !! (ofertes 2x1)
	@Test
	public void test_admin_no_permes() throws Exception {
		mockMvc.perform(get("/admin/llista"))
		.andExpect(status().is4xxClientError());
	}
	
	@Test
	@WithMockUser(roles="ADMIN")
	public void test_admin_ok() throws Exception {
		mockMvc.perform(get("/admin/llista"))
		.andExpect(status().isOk())
		.andExpect(content().string("[\"kiko\",\"belen\"]"));
	}
	
	/**
	 *  Creau un metode que atengui a /intranet/hiSom i retorni un boolean indicat si la persona
	 *  conectada es a la llista
	 *  
	 * Nomes hi ha de poder accedir un rol "CLIENT" o "ADMIN" i ha de retornar 
	 * true si la persona conectada esta a la llista de persones i false si no.
	 * Suposarem que el username es el nom de la persona.
	 */
	@Test
	public void test_intranet_no_permes() throws Exception {
		mockMvc.perform(get("/intranet/hiSom"))
		.andExpect(status().is4xxClientError());
	}
	
	@Test
	@WithMockUser(username="kiko",roles="CLIENT")
	public void test_intranet_hi_es() throws Exception {
		mockMvc.perform(get("/intranet/hiSom"))
		.andExpect(status().isOk())
		.andExpect(content().string("true"));
	}
	
	@Test
	@WithMockUser(username="jose",roles="CLIENT")
	public void test_intranet_no_hi_es() throws Exception {
		mockMvc.perform(get("/intranet/hiSom"))
		.andExpect(status().isOk())
		.andExpect(content().string("false"));
	}

}
