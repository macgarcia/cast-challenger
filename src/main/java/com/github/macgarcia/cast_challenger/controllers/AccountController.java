package com.github.macgarcia.cast_challenger.controllers;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.github.macgarcia.cast_challenger.exceptions.AccountException;
import com.github.macgarcia.cast_challenger.models.Account;
import com.github.macgarcia.cast_challenger.services.AccountService;
import com.github.macgarcia.cast_challenger.utils.Messages;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class AccountController {

	@Autowired
	AccountService service;

	@GetMapping("/castBank")
	public String index() {
		return "index";
	}

	@GetMapping("/login")
	public String login(@RequestParam String document, @RequestParam String account, Model model) {
		Account acc = service.processLogin(UUID.fromString(account), document);
		if (Objects.isNull(acc)) {
			model.addAttribute("error", Messages.ACCOUNT_NOT_FOUND);
			return "index";
		}
		return "redirect:/operations?document=" + acc.getDocument() + "&account=" + acc.getNumberAccount();
	}

	@GetMapping("/operations")
	public String operations(@RequestParam String document, @RequestParam String account, Model model) {
		Account acc = service.processLogin(UUID.fromString(account), document);
		model.addAttribute("acc", acc);
		return "operations";
	}

	@GetMapping("/register")
	public String register() {
		return "register";
	}

	@GetMapping("/create")
	public String create(@RequestParam String name, @RequestParam String document, Model model) {
		UUID numberAccount = service.createAccount(name, document);
		model.addAttribute("success", numberAccount.toString());
		return "register";
	}

	@PostMapping("/credits/add")
	public String addCredits(@RequestParam Long id, @RequestParam BigDecimal value) {
		Account acc = service.addCredits(id, value);
		return "redirect:/operations?document=" + acc.getDocument() + "&account=" + acc.getNumberAccount();
	}

	@PostMapping("/debits/add")
	public String addDebits(@RequestParam Long id, @RequestParam BigDecimal value, Model model) {
		Account acc = service.findById(id);
		try {
			acc = service.addDebits(id, value);
			return "redirect:/operations?document=" + acc.getDocument() + "&account=" + acc.getNumberAccount();
		} catch (AccountException ex) {
			model.addAttribute("acc", acc);
			model.addAttribute("error", ex.getMessage());
			return "operations";
		}
	}

	@PostMapping("/transfers/add")
	public String addTransfer(@RequestParam Long id, @RequestParam BigDecimal value, @RequestParam String numberAccount,
			Model model) {
		Account acc = service.findById(id);
		try {
			service.addTransfer(acc.getNumberAccount(), numberAccount, value);
			return "redirect:/operations?document=" + acc.getDocument() + "&account=" + acc.getNumberAccount();
		} catch (AccountException ex) {
			model.addAttribute("acc", acc);
			model.addAttribute("error", ex.getMessage());
			return "operations";
		}
	}

}
