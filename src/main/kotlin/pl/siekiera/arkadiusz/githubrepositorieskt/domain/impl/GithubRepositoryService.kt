package pl.siekiera.arkadiusz.githubrepositorieskt.domain.impl

import org.springframework.stereotype.Service
import pl.siekiera.arkadiusz.githubrepositorieskt.api.RepositoryDto
import pl.siekiera.arkadiusz.githubrepositorieskt.domain.RepositoryService

@Service
internal class GithubRepositoryService : RepositoryService {

    override suspend fun getRepositories(user: String): List<RepositoryDto> {
        TODO("Not yet implemented")
    }

}