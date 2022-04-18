package pl.siekiera.arkadiusz.githubrepositorieskt.infrastructure.github

import com.fasterxml.jackson.databind.DeserializationFeature
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.features.defaultRequest
import io.ktor.client.features.json.JacksonSerializer
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.request.headers
import io.ktor.http.HttpHeaders
import io.ktor.http.Url
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
internal class GithubClientConfig(
    @Value("\${github-api.headers.token:#{null}}") private val token: String?,
    @Value("\${github-api.url}") private val apiUrl: String,
    @Value("\${github-api.headers.accept:application/json}") private val acceptHeader: String
) {

    @Bean
    fun githubClient(): HttpClient = HttpClient(CIO) {
        defaultRequest {
            headers {
                append(HttpHeaders.AcceptLanguage, "pl-PL")
                append(HttpHeaders.Accept, acceptHeader)
                token?.takeIf { it.isNotBlank() }?.also {
                    append(HttpHeaders.Authorization, "token $it")
                }
            }
            url {
                val url = Url(apiUrl)
                host = url.host
                protocol = url.protocol
                url.port.takeIf { it != url.protocol.defaultPort }?.also {
                    port = it
                }
            }
        }
        followRedirects = false
        expectSuccess = false
        install(JsonFeature) {
            serializer = JacksonSerializer {
                configure(
                    DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
                    false
                )
            }
        }
    }

}