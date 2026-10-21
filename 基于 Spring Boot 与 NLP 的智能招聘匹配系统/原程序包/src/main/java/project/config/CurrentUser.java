package project.config;

import project.entity.UserRole;

public record CurrentUser(Long id, UserRole role, String account)
{
}
