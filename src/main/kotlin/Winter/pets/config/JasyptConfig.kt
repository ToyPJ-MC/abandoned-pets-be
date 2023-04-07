package Winter.pets.config

import org.jasypt.encryption.StringEncryptor
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class JasyptConfig {
    @Bean("jasyptStringEncryptor")
    fun stringEncryptor(): StringEncryptor {
        val config = SimpleStringPBEConfig()
        val key ="YXNkbGtmYWxza2RuZmxrYXNuZGZsa25hc2xrZGZqb2l4am5jbnNhbGtkbWxrenhvaWMK"
        config.setPassword(key)
        config.setAlgorithm("PBEWithMD5AndDES") //암호화 알고리즘
        config.setKeyObtentionIterations("1000") //반복할 해싱 수
        config.setPoolSize("1") //인스턴스 pool
        config.setProviderName("SunJCE")
        config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator") //salt 생성 클래스
        config.setStringOutputType("base64") //인코팅 방식
        val encryptor = PooledPBEStringEncryptor()
        encryptor.setConfig(config)
        return encryptor
    }
}