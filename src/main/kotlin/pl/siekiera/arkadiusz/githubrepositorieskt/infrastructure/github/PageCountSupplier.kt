package pl.siekiera.arkadiusz.githubrepositorieskt.infrastructure.github

import io.ktor.http.Headers
import org.springframework.stereotype.Component

@Component
internal class PageCountSupplier {

    private val lastPageRegex = "(?<=[&?]page=)\\d+".toRegex()

    operator fun invoke(headers: Headers): Int =
        headers["Link"]?.split(",")?.firstOrNull {
            it.endsWith("rel=\"last\"")
        }?.let {
            lastPageRegex.find(it)?.value?.toIntOrNull()
        } ?: 1

}