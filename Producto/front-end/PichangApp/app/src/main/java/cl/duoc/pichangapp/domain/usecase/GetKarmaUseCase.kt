package cl.duoc.pichangapp.domain.usecase

import cl.duoc.pichangapp.core.util.Result
import cl.duoc.pichangapp.data.model.KarmaDto
import cl.duoc.pichangapp.data.repository.KarmaRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetKarmaUseCase @Inject constructor(
    private val karmaRepository: KarmaRepository
) {
    operator fun invoke(userId: String): Flow<Result<KarmaDto>> {
        return karmaRepository.getKarma(userId)
    }
}
