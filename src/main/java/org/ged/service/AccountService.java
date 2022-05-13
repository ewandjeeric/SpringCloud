package org.ged.service;

import java.util.List;

import org.ged.entities.AppRole;
import org.ged.entities.AppUser;

public interface AccountService {
	public AppUser saveUser(AppUser u);

	public AppRole saveRole(AppRole r);

	public AppUser findByUsername(String username);

	public void AddRoleUser(String username, String role);

	public List<AppUser> listUser();

}
