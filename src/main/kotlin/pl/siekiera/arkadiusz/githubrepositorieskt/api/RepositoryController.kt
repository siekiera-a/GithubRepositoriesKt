package pl.siekiera.arkadiusz.githubrepositorieskt.api

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import pl.siekiera.arkadiusz.githubrepositorieskt.domain.RepositoryService

@RestController
@RequestMapping("/repos")
internal class RepositoryController(
    private val githubRepositoryService: RepositoryService
) {

    @GetMapping("/{name}")
    suspend fun getRepositories(
        @PathVariable(required = true) name: String
    ): ResponseEntity<List<RepositoryDto>> {
        return githubRepositoryService.getRepositories(name)
    }

}