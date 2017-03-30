package org.formacio.mvc;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class AppController {

	private List<String> persones = new ArrayList<>();
	
	@PostConstruct
	public void init() {
		persones.add("kiko");
		persones.add("belen");
	}
	
	@RequestMapping(path="/info")
	@ResponseBody
	public int nombrePersonesLlista(){
		return persones.size();
	}
	
	@RequestMapping(path="/admin/llista")
	@ResponseBody
	public List<String> nombrePersonesLlistaAdmin() {
		return persones;
	}
	
	@RequestMapping(path="/intranet/hiSom")
	@ResponseBody
	public boolean hiSom(@AuthenticationPrincipal User user) {
		return persones.contains(user.getUsername());
	}
	
}
