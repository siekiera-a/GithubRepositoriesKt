package integration.pl.siekiera.arkadiusz.githubrepositorieskt.infrastructure

import integration.pl.siekiera.arkadiusz.githubrepositorieskt.BaseIntegrationTest
import integration.pl.siekiera.arkadiusz.githubrepositorieskt.Config.pageCount
import integration.pl.siekiera.arkadiusz.githubrepositorieskt.Config.repositoriesCount
import integration.pl.siekiera.arkadiusz.githubrepositorieskt.Config.repositoriesPerFile
import integration.pl.siekiera.arkadiusz.githubrepositorieskt.stubGithubRepositories
import integration.pl.siekiera.arkadiusz.githubrepositorieskt.stubGithubUnsuccessfulResponse
import integration.pl.siekiera.arkadiusz.githubrepositorieskt.verifyGithubRepositoriesCalls
import io.ktor.http.HttpStatusCode.Companion.InternalServerError
import io.ktor.http.HttpStatusCode.Companion.NotFound
import kotlinx.coroutines.runBlocking
import org.amshove.kluent.`should be equal to`
import org.amshove.kluent.`should be instance of`
import org.amshove.kluent.`should be null`
import org.amshove.kluent.`should not be null`
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import pl.siekiera.arkadiusz.githubrepositorieskt.infrastructure.github.GithubService
import pl.siekiera.arkadiusz.githubrepositorieskt.infrastructure.github.GithubUnavailableException

internal class GithubServiceTest(
    @Autowired private val githubService: GithubService
) : BaseIntegrationTest() {

    @Test
    fun `should fetch all pages`() {
        // given
        val username = "username123"
        with(server) {
            stubGithubRepositories(username, withPagination = true)
        }

        // when
        val response = runBlocking { githubService.getRepositories(username) }

        // then
        server.verifyGithubRepositoriesCalls(pageCount, username)
        response.`should not be null`()
        with(response) {
            size `should be equal to` repositoriesCount
        }
    }

    @Test
    fun `should call api only once if link header is not present`() {
        // given
        val username = "username123"
        with(server) {
            stubGithubRepositories(username)
        }

        // when
        val response = runBlocking { githubService.getRepositories(username) }

        // then
        server.verifyGithubRepositoriesCalls(1, username)
        response.`should not be null`()
        with(response) {
            size `should be equal to` repositoriesPerFile
        }
    }

    @Test
    fun `should return null if user not found`() {
        // given
        val username = "username123"
        with(server) {
            stubGithubRepositories(username, status = NotFound.value)
        }

        // when
        val response = runBlocking { githubService.getRepositories(username) }

        // then
        server.verifyGithubRepositoriesCalls(1, username)
        response.`should be null`()
    }

    @Test
    fun `should throw exception if github service is unavailable`() {
        // given
        val username = "username123"
        with(server) {
            stubGithubUnsuccessfulResponse(
                username,
                status = InternalServerError.value
            )
        }

        // when
        val response =
            runCatching { runBlocking { githubService.getRepositories(username) } }

        // then
        server.verifyGithubRepositoriesCalls(1, username)
        response.exceptionOrNull() `should be instance of` GithubUnavailableException::class
    }

}