package pt.hugo.LusApp;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import pt.hugo.LusApp.model.data.database.DB;
import pt.hugo.LusApp.security.RsaKeysProperties;

@SpringBootApplication
@ConfigurationPropertiesScan
public class LusAppApplication {
	private final RsaKeysProperties rsaKeys;

    public LusAppApplication(RsaKeysProperties rsaKeys) {
        this.rsaKeys = rsaKeys;
    }

    public static void main(String[] args) {
		if (DB.DBConnection()){
			SpringApplication.run(LusAppApplication.class, args);
		} else {
			System.err.println("[ERROR] FAILED TO CONNECT TO THE DATABASE!");
		}

	}



	@Bean
	JwtEncoder jwtEncoder() {
		JWK jwK = new RSAKey.Builder(rsaKeys.publicKey()).privateKey(rsaKeys.privateKey()).build();
		JWKSource<SecurityContext> jwkSource = new ImmutableJWKSet<>(new JWKSet(jwK));
		return new NimbusJwtEncoder(jwkSource);
	}

	@Bean
	JwtDecoder jwtDecoder() {
		return NimbusJwtDecoder.withPublicKey(rsaKeys.publicKey()).build();
	}
}
