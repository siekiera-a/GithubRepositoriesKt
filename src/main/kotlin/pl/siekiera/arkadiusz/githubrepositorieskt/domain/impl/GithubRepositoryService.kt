package pl.siekiera.arkadiusz.githubrepositorieskt.domain.impl

import kotlin.time.ExperimentalTime
import kotlin.time.measureTimedValue
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import pl.siekiera.arkadiusz.githubrepositorieskt.api.RepositoryDto
import pl.siekiera.arkadiusz.githubrepositorieskt.domain.RepositoryService
import pl.siekiera.arkadiusz.githubrepositorieskt.infrastructure.github.GithubService

@Service
@OptIn(ExperimentalTime::class)
internal class GithubRepositoryService(
    private val githubService: GithubService
) : RepositoryService {

    private val logger = LoggerFactory.getLogger(this::class.java)

    override suspend fun getRepositories(user: String): ResponseEntity<List<RepositoryDto>> {
        val measurement = measureTimedValue {
            githubService.getRepositories(user)
        }

        val duration = measurement.duration

        val repositories = measurement.value
        return if (repositories != null) {
            logger.info("Fetched ${repositories.size} $user's repositories in ${duration.inWholeMilliseconds}")
            ResponseEntity.ok(repositories.map { (name, stars) ->
                RepositoryDto(name, stars)
            })
        } else {
            logger.info("User '$user' not found!")
            ResponseEntity.notFound().build()
        }
    }

}