package com.group3.server.configs;

import java.math.BigDecimal;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.group3.server.models.auth.User;
import com.group3.server.models.saving.SavingType;
import com.group3.server.models.system.Parameter;
import com.group3.server.repositories.auth.UserRepository;
import com.group3.server.repositories.saving.SavingTypeRepository;
import com.group3.server.repositories.system.ParameterRepository;
import com.group3.server.services.auth.UserService;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class AppConfig {
    private final UserRepository userRepository;
    private final ParameterRepository parameterRepository;
    private final SavingTypeRepository savingTypeRepository;
    private final UserService userService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public WebMvcConfigurer configurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(@NonNull CorsRegistry registry) {
                registry.addMapping("/**") // Cho phép tất cả URL
                        .allowedOrigins("*") // Cho phép mọi domain
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Cho phép các HTTP method
                        .allowCredentials(false)
                        .allowedHeaders("*"); // Cho phép tất cả headers

            }
        };
    }

    @Bean
    public AuthenticationProvider provider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userService.userDetailsService());
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    // test
    @Bean
    public CommandLineRunner commandLineRunner() {
        return arg -> {
            User admin = userRepository.findByUsername("admin").orElse(null);
            if (admin == null) {
                User user = User.builder()
                        .username("admin")
                        .password(passwordEncoder().encode("123456"))
                        .fullname("ADMIN")
                        .build();
                Parameter parameter = Parameter.builder()
                        .minAge(15)
                        .minTransactionAmount(new BigDecimal(10000))
                        .maxTransactionAmount(new BigDecimal(1000000000))
                        .minSavingAmount(new BigDecimal(1000000))
                        .build();

                // Tạo các loại tiết kiệm mặc định
                SavingType noTerm = SavingType.builder()
                        .typeName("Không kỳ hạn")
                        .duration(0) // Không kỳ hạn
                        .interestRate(new BigDecimal("0.5"))
                        .build();

                SavingType threeMonths = SavingType.builder()
                        .typeName("3 tháng")
                        .duration(3) // 3 tháng
                        .interestRate(new BigDecimal("5.0"))
                        .build();

                SavingType sixMonths = SavingType.builder()
                        .typeName("6 tháng")
                        .duration(6) // 6 tháng
                        .interestRate(new BigDecimal("5.5"))
                        .build();

                // Lưu các loại tiết kiệm vào cơ sở dữ liệu
                savingTypeRepository.save(noTerm);
                savingTypeRepository.save(threeMonths);
                savingTypeRepository.save(sixMonths);

                userRepository.save(user);
                parameterRepository.save(parameter);
            }
        };
    }
}
