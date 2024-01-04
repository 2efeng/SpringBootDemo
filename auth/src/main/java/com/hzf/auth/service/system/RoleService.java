package com.hzf.auth.service.system;

import com.hzf.auth.models.system.Role;
import com.hzf.auth.repository.system.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService {

    @Autowired
    RoleRepository roleRepository;

    public void createRole(Role role) {
        roleRepository.save(role);
    }

    public List<Role> getRoles() {
        return roleRepository.findAll();
    }
}
