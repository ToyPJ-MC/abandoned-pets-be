package Winter.pets.config

import Winter.pets.domain.jwt.filter.JwtFilter
import Winter.pets.domain.jwt.handler.JwtAccessDeniedHandler
import Winter.pets.domain.jwt.handler.JwtAuthenticationEntryPoint
import Winter.pets.domain.jwt.handler.OAuth2AuthenticationFailureHandler
import Winter.pets.domain.jwt.handler.OAuth2AuthenticationSuccessHandler
import Winter.pets.domain.jwt.service.CustomOauth2Service
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
class SecurityConfig{
    private var userOAuth2Service : CustomOauth2Service
    private var oAuth2AuthenticationSuccessHandler: OAuth2AuthenticationSuccessHandler
    private var oAuth2AuthenticationFailureHandler: OAuth2AuthenticationFailureHandler
    private var jwtFilter: JwtFilter
    private var jwtAccessDeniedHandler: JwtAccessDeniedHandler
    private var jwtAuthenticationEntryPoint: JwtAuthenticationEntryPoint
    @Autowired
    constructor(
        userOAuth2Service: CustomOauth2Service,
        oAuth2AuthenticationSuccessHandler: OAuth2AuthenticationSuccessHandler,
        oAuth2AuthenticationFailureHandler: OAuth2AuthenticationFailureHandler,
        jwtFilter: JwtFilter,
        jwtAuthenticationEntryPoint: JwtAuthenticationEntryPoint,
        jwtAccessDeniedHandler: JwtAccessDeniedHandler
    ) {
        this.userOAuth2Service = userOAuth2Service
        this.oAuth2AuthenticationSuccessHandler = oAuth2AuthenticationSuccessHandler
        this.oAuth2AuthenticationFailureHandler = oAuth2AuthenticationFailureHandler
        this.jwtFilter = jwtFilter
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint
        this.jwtAccessDeniedHandler = jwtAccessDeniedHandler

    }


    @Bean
    fun configure(http: HttpSecurity):SecurityFilterChain{
        http.csrf().disable();
        http.authorizeRequests()
			  .antMatchers("/v3/api-docs/**","/swagger-ui/**","/swagger-ui/**").permitAll()
                // 인가된 사용자만 접근 가능하도록 설정
			  //.antMatchers("/api/member/**").hasRole("ROLE_USER") // 특정 ROLE을 가진 사용자만 접근 가능하도록 설정
            .and()
            .exceptionHandling()
            .authenticationEntryPoint(jwtAuthenticationEntryPoint) //우리가 만든 클래스로 인증 실패 핸들링
            .accessDeniedHandler(jwtAccessDeniedHandler) //커스텀 인가 실패 핸들링
            .and()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS) //session 사용하지 않음
            .and()
            .logout()
            .logoutSuccessUrl("/")
            .and()
            .oauth2Login()
                .successHandler(oAuth2AuthenticationSuccessHandler)
                .failureHandler(oAuth2AuthenticationFailureHandler)
                .userInfoEndpoint()
                .userService(userOAuth2Service)
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter::class.java)
        return http.build()
    }
}
