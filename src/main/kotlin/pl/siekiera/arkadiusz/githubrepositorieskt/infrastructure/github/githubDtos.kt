package pl.siekiera.arkadiusz.githubrepositorieskt.infrastructure.github

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import io.ktor.http.Headers
import io.ktor.http.HttpStatusCode

internal data class GithubResponse<T>(
    val status: HttpStatusCode,
    val headers: Headers,
    val body: T?
) {
    val ok get() = body != null
}

internal data class GithubRepository @JsonCreator constructor(
    @JsonProperty(value = "name", required = true) val name: String,
    @JsonProperty(value = "stargazers_count", required = true) val stars: Long,
)