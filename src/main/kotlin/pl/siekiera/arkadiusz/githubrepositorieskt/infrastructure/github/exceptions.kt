package pl.siekiera.arkadiusz.githubrepositorieskt.infrastructure.github

class GithubUnavailableException : RuntimeException()

class GithubUnprocessableResponse(message: String) : RuntimeException(message)