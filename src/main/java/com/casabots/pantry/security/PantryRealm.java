/*******************************************************************************
 * Copyright (c) CASABOTS 2016
 * All Rights Reserved
 ******************************************************************************/

package com.casabots.pantry.security;

import com.casabots.pantry.model.Admin;
import com.casabots.pantry.model.Owner;
import com.casabots.pantry.model.Sally;
import com.casabots.pantry.model.User;
import com.casabots.pantry.repository.AdminRepository;
import com.casabots.pantry.service.OwnerService;
import com.casabots.pantry.service.SallyService;
import com.casabots.pantry.service.UserService;
import com.casabots.pantry.util.Constants;
import com.casabots.pantry.util.Constants.Roles;
import com.casabots.pantry.util.PasswordHashingUtil;
import com.casabots.pantry.util.SessionUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

public class PantryRealm extends AuthorizingRealm {

    @Autowired
    private UserService userService;
    @Autowired
    private OwnerService ownerService;
    @Autowired
    private SallyService sallyService;
    @Autowired
    private AdminRepository adminRepository;

    public PantryRealm() {
        setName("pantry-realm");
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection pc) {
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        info.addRole(SessionUtils.getUserRole());
        return info;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken at)
            throws AuthenticationException {
        PantryAuthenticationToken token = (PantryAuthenticationToken) at;
        String userId = token.getUsername();
        String role = SessionUtils.getUserRole();

        if (role.equals(Roles.USER_ROLE)) {
            User user = userService.getCompleteUser(userId);
            if (token.getLoginType() == null) {
                return new SimpleAuthenticationInfo(user.getUserId(), user.getHashedPwd(), getName());
            } else if (Constants.UserLoginType.GOOGLEUSER.equals(token.getLoginType())
                    || Constants.UserLoginType.FBUSER.equals(token.getLoginType())){
                return new SimpleAuthenticationInfo(user.getUserId(),
                        PasswordHashingUtil.hash(new String(token.getPassword())), getName());
            }

        } else if (role.equals(Roles.OWNER_ROLE)) {
            Owner owner = ownerService.getCompleteOwner(userId);
            return new SimpleAuthenticationInfo(owner.getOwnerId(), owner.getHashedPwd(), getName());
        } else if (role.equals(Roles.SALLY_ROLE)) {
            Sally sally = sallyService.getCompleteSally(userId);
            return new SimpleAuthenticationInfo(sally.getSallyId(), sally.getHashedPwd(), getName());
        } else if (role.equals(Roles.ADMIN_ROLE)) {
            Admin admin = adminRepository.findOne(userId);
            return new SimpleAuthenticationInfo(admin.getAdminId(), admin.getHashedPwd(), getName());
        }

        return new SimpleAuthenticationInfo();
    }
}
