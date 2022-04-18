package pl.siekiera.arkadiusz.githubrepositorieskt.infrastructure.github

import io.ktor.http.HttpStatusCode.Companion.NotFound
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import org.springframework.stereotype.Service

@Service
internal class GithubService(
    private val githubApiClient: GithubApiClient,
    private val pageCountSupplier: PageCountSupplier
) {

    suspend fun getRepositories(user: String): List<GithubRepository>? =
        coroutineScope {
            val response = githubApiClient.getRepositories(user)
            val firstPage =
                unpackResponse(response) ?: return@coroutineScope null
            val pageCount = pageCountSupplier(response.headers)

            if (pageCount == 1) {
                return@coroutineScope firstPage
            }

            val asyncRequests = (2..pageCount).map { page ->
                async {
                    githubApiClient.getRepositories(user, page)
                }
            }

            val responses = asyncRequests.awaitAll()

            responses.takeIf {
                it.all { response -> response.ok }
            }?.flatMap { it.body!! }?.let {
                it + response.body!!
            } ?: throw GithubUnprocessableResponse(
                "${responses.count { !it.ok }}/${responses.size} page requests failed!"
            )
        }

    private fun <T> unpackResponse(response: GithubResponse<T>): T? =
        if (response.ok) {
            response.body!!
        } else {
            when (response.status.value) {
                NotFound.value -> null
                in 500..599 -> throw GithubUnavailableException()
                else -> throw GithubUnprocessableResponse("Github returned with ${response.status.value} status code")
            }
        }

}