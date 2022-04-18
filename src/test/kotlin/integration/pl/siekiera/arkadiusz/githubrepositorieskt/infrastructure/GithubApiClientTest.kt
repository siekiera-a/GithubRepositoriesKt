package integration.pl.siekiera.arkadiusz.githubrepositorieskt.infrastructure

import integration.pl.siekiera.arkadiusz.githubrepositorieskt.BaseIntegrationTest
import integration.pl.siekiera.arkadiusz.githubrepositorieskt.stubGithubRepositories
import integration.pl.siekiera.arkadiusz.githubrepositorieskt.stubGithubUnsuccessfulResponse
import integration.pl.siekiera.arkadiusz.githubrepositorieskt.verifyGithubRepositoriesCalls
import io.ktor.http.HttpStatusCode.Companion.NotFound
import io.ktor.http.HttpStatusCode.Companion.NotModified
import io.ktor.http.HttpStatusCode.Companion.OK
import kotlinx.coroutines.runBlocking
import org.amshove.kluent.`should be equal to`
import org.amshove.kluent.`should be false`
import org.amshove.kluent.`should be greater than`
import org.amshove.kluent.`should be null`
import org.amshove.kluent.`should be true`
import org.amshove.kluent.`should not be null`
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import pl.siekiera.arkadiusz.githubrepositorieskt.infrastructure.github.GithubApiClient

internal class GithubApiClientTest(
    @Autowired private val githubClient: GithubApiClient
) : BaseIntegrationTest() {

    @Test
    fun `should fetch user repositories`() {
        // given
        val username = "user"

        with(server) {
            stubGithubRepositories(username)
        }

        // when
        val response = runBlocking { githubClient.getRepositories(username) }

        // then
        server.verifyGithubRepositoriesCalls(1, username)
        with(response) {
            ok.`should be true`()
            body.`should not be null`()
            status `should be equal to` OK
            body.size `should be greater than` 0
        }
    }

    @Test
    fun `should return null if response status is Not Modified`() {
        // given
        val username = "user"

        with(server) {
            stubGithubUnsuccessfulResponse(username, NotModified.value)
        }

        // when
        val response = runBlocking { githubClient.getRepositories(username) }

        // then
        server.verifyGithubRepositoriesCalls(1, username)
        with(response) {
            ok.`should be false`()
            body.`should be null`()
            status `should be equal to` NotModified
        }
    }

    @Test
    fun `should return null if response status is Not Found`() {
        // given
        val username = "user"

        with(server) {
            stubGithubUnsuccessfulResponse(username, NotFound.value)
        }

        // then
        val response = runBlocking { githubClient.getRepositories(username) }

        // then
        server.verifyGithubRepositoriesCalls(1, username)
        with(response) {
            ok.`should be false`()
            body.`should be null`()
            status `should be equal to` NotFound
        }
    }

}