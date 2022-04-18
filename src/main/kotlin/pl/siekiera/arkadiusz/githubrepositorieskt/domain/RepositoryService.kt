package pl.siekiera.arkadiusz.githubrepositorieskt.domain

import org.springframework.http.ResponseEntity
import pl.siekiera.arkadiusz.githubrepositorieskt.api.RepositoryDto

internal interface RepositoryService {

    suspend fun getRepositories(user: String): ResponseEntity<List<RepositoryDto>>

}