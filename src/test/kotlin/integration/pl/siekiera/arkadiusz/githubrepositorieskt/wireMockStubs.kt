package integration.pl.siekiera.arkadiusz.githubrepositorieskt

import com.github.tomakehurst.wiremock.client.WireMock.aResponse
import com.github.tomakehurst.wiremock.client.WireMock.exactly
import com.github.tomakehurst.wiremock.client.WireMock.get
import com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor
import com.github.tomakehurst.wiremock.client.WireMock.matching
import com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo
import com.github.tomakehurst.wiremock.junit5.WireMockExtension

fun WireMockExtension.stubGithubRepositories(username: String = Defaults.username, status: Int = 200) {
    stubFor(
        get(urlPathEqualTo(userRepositoriesPath(username)))
            .withQueryParam("page", matching("\\d+"))
            .withQueryParam("per_page", matching("\\d+"))
            .willReturn(
                aResponse().withStatus(status)
                    .withHeader("Content-Type", "application/json;charset=UTF-8")
                    .withBodyFile("repositories-page-{{request.query.page}}.json")
            )
    )
}

fun WireMockExtension.stubGithubUnsuccessfulResponse(username: String = Defaults.username, status: Int) {
    stubFor(
        get(urlPathEqualTo(userRepositoriesPath(username)))
            .withQueryParam("page", matching("\\d+"))
            .withQueryParam("per_page", matching("\\d+"))
            .willReturn(aResponse().withStatus(status))
    )
}

fun WireMockExtension.verifyGithubRepositoriesCalls(
    count: Int,
    username: String = Defaults.username
) {
    verify(
        exactly(count),
        getRequestedFor(urlPathEqualTo(userRepositoriesPath(username)))
            .withQueryParam("page", matching("\\d+"))
            .withQueryParam("per_page", matching("\\d+"))
    )
}

private fun userRepositoriesPath(username: String) = "/users/$username/repos"

internal object Defaults {
    const val username = "username"
}