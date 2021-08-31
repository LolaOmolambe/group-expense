package com.expense.tracker.service;

import com.expense.tracker.dto.RoleDTO;
import com.expense.tracker.service.interfaces.IRoleService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService implements IRoleService {
    @Override
    public List<RoleDTO> getRoles() {
        return null;
    }

    @Override
    public RoleDTO getRoleById(Long roleId) {
        return null;
    }

    @Override
    public void deleteRole(Long roleId) {

    }

    @Override
    public RoleDTO createRole(RoleDTO roleDTO) {
        return null;
    }
}
