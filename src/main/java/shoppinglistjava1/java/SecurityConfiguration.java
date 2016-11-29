package shoppinglistjava1.java;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
	//
	// @Override
	// protected void configure(HttpSecurity http) throws Exception {
	// http.apply(stormpath()).and()
	// .authorizeRequests()
	// .antMatchers("/**", "/h2-console/**").permitAll();
	// }

	@Override
	protected void configure(HttpSecurity httpSecurity) throws Exception {
		httpSecurity.authorizeRequests().antMatchers("/h2-console/**").permitAll().and().authorizeRequests()
				.antMatchers("/console/**").permitAll().and().authorizeRequests()
				.antMatchers("/home", "/lists/**", "/list/**", "/events/**", "/event/**").authenticated()
				.anyRequest().permitAll().and().formLogin().loginPage("/login").usernameParameter("username")
				.passwordParameter("password").and().logout().logoutSuccessUrl("/login?logout").and().csrf().disable()
				.headers().frameOptions().disable();

		// //two ** means two levels down it will be block so user/1/edit is
		// blocked * just one wouldn't be
	}

	@Autowired
	private DataSource dataSource;

	//
	//
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.jdbcAuthentication().dataSource(dataSource).rolePrefix("").passwordEncoder(passwordEncoder())
				.usersByUsernameQuery(
						"select email as username, password, active as enabled from shoppingList.users where email = ?")
				.authoritiesByUsernameQuery(
						"select email as username, role as authority from shoppingList.users where email = ?");
	}
	

	@Bean
	public PasswordEncoder passwordEncoder(){
		PasswordEncoder encoder = new BCryptPasswordEncoder();
		return encoder;
	}
}