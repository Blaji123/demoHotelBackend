package com.dev.blaji.DemoHotel.service;

import com.dev.blaji.DemoHotel.model.Role;
import com.dev.blaji.DemoHotel.model.User;

import java.util.List;

public interface RoleService {
    List<Role> getRoles();
    Role createRole(Role theRole);
    void deleteRole(Long id);
    Role findByName(String name);
    User removeRoleFromUser(Long userId, Long roleId);
    User assignRoleToUser(Long userId, Long roleId);
    Role removeAllUsersFromRole(Long roleId);
}
