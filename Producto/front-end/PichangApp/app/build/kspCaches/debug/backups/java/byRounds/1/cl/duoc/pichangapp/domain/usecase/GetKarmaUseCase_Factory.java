package cl.duoc.pichangapp.domain.usecase;

import cl.duoc.pichangapp.data.repository.KarmaRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Provider;
import dagger.internal.Providers;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

@ScopeMetadata
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava",
    "cast",
    "deprecation",
    "nullness:initialization.field.uninitialized"
})
public final class GetKarmaUseCase_Factory implements Factory<GetKarmaUseCase> {
  private final Provider<KarmaRepository> karmaRepositoryProvider;

  public GetKarmaUseCase_Factory(Provider<KarmaRepository> karmaRepositoryProvider) {
    this.karmaRepositoryProvider = karmaRepositoryProvider;
  }

  @Override
  public GetKarmaUseCase get() {
    return newInstance(karmaRepositoryProvider.get());
  }

  public static GetKarmaUseCase_Factory create(
      javax.inject.Provider<KarmaRepository> karmaRepositoryProvider) {
    return new GetKarmaUseCase_Factory(Providers.asDaggerProvider(karmaRepositoryProvider));
  }

  public static GetKarmaUseCase_Factory create(Provider<KarmaRepository> karmaRepositoryProvider) {
    return new GetKarmaUseCase_Factory(karmaRepositoryProvider);
  }

  public static GetKarmaUseCase newInstance(KarmaRepository karmaRepository) {
    return new GetKarmaUseCase(karmaRepository);
  }
}
