package unit.pl.siekiera.arkadiusz.githubrepositorieskt.infrastructure.github

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.ktor.http.Headers
import pl.siekiera.arkadiusz.githubrepositorieskt.infrastructure.github.PageCountSupplier

internal class PageCountSupplierTest: FreeSpec({

    val pageCountSupplier = PageCountSupplier()

    val testCases = listOf(
        "header with many relations" to ("<https://api.github.com/user/562236/repos?page=2>; rel=\"next\", <https://api.github.com/user/562236/repos?page=4>; rel=\"last\"" to 4),
        "header with single relation" to ("<https://api.github.com/user/562236/repos?page=4>; rel=\"last\"" to 4),
        "header without last rel" to ("<https://api.github.com/user/562236/repos?page=4>; rel=\"next\"" to 1),
        "header with per_page url param at first position" to (("<https://api.github.com/user/562236/repos?per_page=2&page=4>; rel=\"last\"" to 4)),
        "header with per_page url param at second position" to (("<https://api.github.com/user/562236/repos?page=4&per_page=2>; rel=\"last\"" to 4))
    )

    "should return valid page number" - {
        testCases.forEach {
            it.first {
                // given
                val header = Headers.build {
                    set("Link", it.second.first)
                }

                // when
                val result = pageCountSupplier(header)

                // then
                result shouldBe it.second.second
            }
        }
    }

})