package pl.siekiera.arkadiusz.githubrepositorieskt.api

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/repos")
internal class RepositoryController {

    @GetMapping("/{name}")
    suspend fun getRepositories(@PathVariable(required = true) name: String): List<RepositoryDto> {
        return emptyList()
    }

}