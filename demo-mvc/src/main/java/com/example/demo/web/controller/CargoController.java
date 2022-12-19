package com.example.demo.web.controller;

import java.util.List;
import java.util.Optional;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.domain.Cargo;
import com.example.demo.domain.Departamento;
import com.example.demo.service.CargoService;
import com.example.demo.service.DepartamentoService;
import com.example.demo.util.PaginacaoUtil;

@Controller
@RequestMapping("/cargos")
public class CargoController {
	
	@Autowired
	private CargoService service;
	
	@Autowired
	private DepartamentoService departamentoService;
	
	@GetMapping("/cadastrar")
	public String cadastrar(Cargo cargo) {
		return "cargo/cadastro";
	}
	
	@GetMapping("/listar")
	public String listar(ModelMap model, 
						@RequestParam("page") Optional<Integer> page,
						@RequestParam("dir") Optional<String> dir) {
		
		int paginaAtual = page.orElse(1);
		String ordem = dir.orElse("asc");
		
		PaginacaoUtil<Cargo> pageCargo = service.buscarPorPagina(paginaAtual, ordem);
		
		model.addAttribute("pageCargo", pageCargo);		
		return "cargo/lista";
	}
	
	@PostMapping("salvar")
	public String salvar(@Valid Cargo cargo, BindingResult result, RedirectAttributes attr) {
		if(result.hasErrors()) {
			return "cargo/cadastro";
		}
		service.salvar(cargo);
		attr.addFlashAttribute("success", "Cargo inserido");
		return "redirect:/cargos/listar";		
	}
	
	@ModelAttribute("departamentos")
	public List<Departamento> listDepartamentos(){
		return departamentoService.buscarTodos();
	}
	
	@GetMapping("/editar/{id}")
	public String preEditar(@PathVariable("id") Long id, ModelMap model) {
		model.addAttribute("cargo", service.buscarPorId(id));
		return "cargo/cadastro";
	}
	
	@PostMapping("/editar")
	public String editar(@Valid Cargo cargo, BindingResult result, RedirectAttributes attr) {
		if(result.hasErrors()) {
			return "cargo/cadastro/";
		}
		
		service.editar(cargo);
		attr.addFlashAttribute("success", "Cargo editado.");
		return "redirect:/cargos/listar";		
	}
	
	@GetMapping("/excluir/{id}")
	public String excluir(@PathVariable("id") Long id, ModelMap model) {
		
		if	(service.cargoTemFuncionarios(id)) {
			model.addAttribute("fail", "Cargo não removido. Possui Funcionário(s) vinculado(s).");
		}else {
			service.excluir(id);
			model.addAttribute("success", "Cargo removido.");
		}		
		return "redirect:/cargos/listar";
	}
}