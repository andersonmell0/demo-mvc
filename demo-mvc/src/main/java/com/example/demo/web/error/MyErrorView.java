package com.example.demo.web.error;

import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.boot.autoconfigure.web.servlet.error.ErrorViewResolver;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

@Component
class MyErrorView implements ErrorViewResolver{

	@Override
	public ModelAndView resolveErrorView(HttpServletRequest request, HttpStatus status, Map<String, Object> map) {
		
		//map.forEach((k,v) -> System.out.println(k +":" + v +"\n"));
		ModelAndView model = new ModelAndView("error");
		model.addObject("status", status.value());
		switch (status.value()) {
			case 404:
				model.addObject("error", "Página não encontrada.");
				model.addObject("timestamp", map.get("timestamp"));
				model.addObject("message", "A url para a página " + map.get("path") + " não existe");
				break;
			case 500:
				model.addObject("error","Ocorreu um error interno no servidor");
				model.addObject("timestamp", map.get("timestamp"));
				model.addObject("message"," Ocorreu um erro inesperado, tente novamente mais tarde");
				break;
			default:
				model.addObject("error", map.get("error"));
				model.addObject("message", map.get("message"));
				break;
		}
		
		
		return model;
	}

}