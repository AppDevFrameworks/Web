package com.phillies.service;

import com.phillies.domain.Account;

public interface AccountService {
	public Account login(String name, String pass);
}