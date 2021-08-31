package com.expense.tracker.service.interfaces;

import com.expense.tracker.dto.RoleDTO;

import java.util.List;

public interface IRoleService {
    List<RoleDTO> getRoles();

    RoleDTO getRoleById(Long roleId);

    void deleteRole(Long roleId);

    RoleDTO createRole(RoleDTO roleDTO);

}
