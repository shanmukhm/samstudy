package com.samstudy.security;


import com.samstudy.model.User;
import com.samstudy.service.UserService;
import com.samstudy.util.Constants;
import com.samstudy.util.SessionUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

public class SamStudyRealm extends AuthorizingRealm {

    @Autowired
    private UserService userService;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        info.addRole(SessionUtils.getUserRole());
        return info;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        String userId = token.getUsername();
        String role = SessionUtils.getUserRole();

        if (role.equals(Constants.Roles.USER_ROLE)) {
            User user = userService.getCompleteUser(userId);
            return new SimpleAuthenticationInfo(user.getUserId(), user.getHashedPwd(), getName());
        }

        return new SimpleAuthenticationInfo();
    }
}