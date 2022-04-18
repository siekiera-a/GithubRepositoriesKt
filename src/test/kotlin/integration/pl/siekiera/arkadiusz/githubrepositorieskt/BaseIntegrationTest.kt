package integration.pl.siekiera.arkadiusz.githubrepositorieskt

import com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig
import com.github.tomakehurst.wiremock.extension.responsetemplating.ResponseTemplateTransformer
import com.github.tomakehurst.wiremock.junit5.WireMockExtension
import org.junit.jupiter.api.extension.RegisterExtension
import org.springframework.boot.test.context.SpringBootTest
import pl.siekiera.arkadiusz.githubrepositorieskt.GithubRepositoriesKtApplication

@SpringBootTest(
    classes = [GithubRepositoriesKtApplication::class],
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    properties = ["spring.profiles.active=integration"]
)
internal abstract class BaseIntegrationTest {

    companion object {
        @RegisterExtension
        val server: WireMockExtension = WireMockExtension.newInstance()
            .options(
                wireMockConfig().port(8081)
                    .extensions(ResponseTemplateTransformer(true))
            )
            .build()
    }

}