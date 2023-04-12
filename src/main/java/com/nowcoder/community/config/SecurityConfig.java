package com.nowcoder.community.config;

import com.nowcoder.community.entity.User;
import com.nowcoder.community.service.UserService;
import com.nowcoder.community.util.CommunityConstant;
import com.nowcoder.community.util.CommunityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter implements CommunityConstant {

    @Autowired
    private UserService userService;

    @Override
    public void configure(WebSecurity web) throws Exception {
        //忽略静态资源的访问
        web.ignoring().antMatchers("/resourcces/**");
    }

    //AuthenticationManager：认证的核心接口
    //AuthenticationManagerBuilder：用于构建AuthenticationManager对象的工具
    //ProviderManager:AuthenticationManager接口的默认实现类
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //内置的认证规则
//        auth.userDetailsService(userService).passwordEncoder(new Pbkdf2PasswordEncoder("12345"));

        //自定义认证规则
        //ProviderManager持有一组AuthenticationProvider，每个AuthenticationProvider提供一种认证
        //委托模式：ProviderManager 将认证委托给了AuthenticationProvider
        auth.authenticationProvider(new AuthenticationProvider() {

            //Authentication用于封装认证信息的接口，不同的实现类代表不同类型的认证信息。
            @Override
            public Authentication authenticate(Authentication authentication) throws AuthenticationException {
                String username = authentication.getName();
                String password = (String) authentication.getCredentials();

                User user = userService.findUserByName(username);
                if (user == null) {
                    throw new UsernameNotFoundException("账号不存在!");
                }
                password = CommunityUtil.md5(password + user.getSalt());
                if (!user.getPassword().equals(password)) {
                    throw new BadCredentialsException("密码不正确!");
                }
                //principal:主要信息; credentials:证书; authorities:权限;
                return new UsernamePasswordAuthenticationToken(user, user.getPassword(), user.getAuthorities());
            }

            //当前的AuthenticationProvider支持哪种类型的认证
            @Override
            public boolean supports(Class<?> aClass) {
                /*Authentication接口的常见实现类*/
                return UsernamePasswordAuthenticationToken.class.equals(aClass);
            }
        });
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //登录相关配置
        http.formLogin()
                .loginPage("/loginpage")
                .loginProcessingUrl("/login")
                .successHandler(new AuthenticationSuccessHandler() {
                    @Override
                    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
                        httpServletResponse.sendRedirect(httpServletRequest.getContextPath() + "/index");
                    }
                }).failureHandler(new AuthenticationFailureHandler() {
            @Override
            public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
                httpServletRequest.setAttribute("error", e.getMessage());
                httpServletRequest.getRequestDispatcher("loginpage").forward(httpServletRequest, httpServletResponse);
            }
        });
        //退出相关配置
        http.logout()
                .logoutUrl("/logout")
                .logoutSuccessHandler(new LogoutSuccessHandler() {
                    @Override
                    public void onLogoutSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
                        httpServletResponse.sendRedirect(httpServletRequest.getContextPath() + "/index");
                    }
                });
        //授权配置
        http.authorizeRequests()
                .antMatchers("/letter").hasAnyAuthority("USER", "ADMIN")
                .antMatchers("/admin").hasAnyAuthority("ADMIN")
                .and().exceptionHandling().accessDeniedPage("/denied");

    }
}
