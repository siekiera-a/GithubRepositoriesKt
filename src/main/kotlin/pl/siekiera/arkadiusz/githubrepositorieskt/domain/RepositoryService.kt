package pl.siekiera.arkadiusz.githubrepositorieskt.domain

import pl.siekiera.arkadiusz.githubrepositorieskt.api.RepositoryDto

internal interface RepositoryService {

    suspend fun getRepositories(user: String): List<RepositoryDto>

}