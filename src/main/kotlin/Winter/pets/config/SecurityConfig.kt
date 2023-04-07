package Winter.pets.config

import Winter.pets.domain.jwt.filter.JwtFilter
import Winter.pets.domain.jwt.handler.OAuth2AuthenticationFailureHandler
import Winter.pets.domain.jwt.handler.OAuth2AuthenticationSuccessHandler
import Winter.pets.domain.jwt.service.CustomOauth2Service
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
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
    @Autowired
    constructor(
        userOAuth2Service: CustomOauth2Service,
        oAuth2AuthenticationSuccessHandler: OAuth2AuthenticationSuccessHandler,
        oAuth2AuthenticationFailureHandler: OAuth2AuthenticationFailureHandler,
        jwtFilter: JwtFilter
    ) {
        this.userOAuth2Service = userOAuth2Service
        this.oAuth2AuthenticationSuccessHandler = oAuth2AuthenticationSuccessHandler
        this.oAuth2AuthenticationFailureHandler = oAuth2AuthenticationFailureHandler
        this.jwtFilter = jwtFilter
    }


    @Bean
    fun configure(http: HttpSecurity):SecurityFilterChain{
        http.csrf().disable();
        http.authorizeRequests()
            .anyRequest().permitAll()
			  //.antMatchers("/**") // 인가된 사용자만 접근 가능하도록 설정
//			  .antMatchers("게시물등").hasRole(Role.USER.name()) // 특정 ROLE을 가진 사용자만 접근 가능하도록 설정
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
    /*fun passwordEncoder() : PasswordEncoder{

    }*/
}
