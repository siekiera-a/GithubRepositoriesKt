package pl.siekiera.arkadiusz.githubrepositorieskt.infrastructure.github


import io.ktor.client.HttpClient
import io.ktor.client.call.receive
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpStatusCode
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
internal class GithubApiClient(
    private val githubClient: HttpClient,
    @Value("\${github-api.per-page:30}") val perPage: Int
) {

    suspend fun getRepositories(user: String, page: Int = 1): GithubResponse<List<GithubRepository>> {
        val response: HttpResponse = githubClient.get("/users/$user/repos") {
            parameter("page", page)
            parameter("per_page", perPage)
        }

        val status = response.status

        return GithubResponse(
            status,
            response.headers,
            body = if (status == HttpStatusCode.OK) response.receive() else null
        )
    }

}