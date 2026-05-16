package cl.duoc.pichangapp.di;

import cl.duoc.pichangapp.data.remote.KarmaApi;
import cl.duoc.pichangapp.data.repository.KarmaRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.Provider;
import dagger.internal.Providers;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

@ScopeMetadata("javax.inject.Singleton")
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
public final class RepositoryModule_ProvideKarmaRepositoryFactory implements Factory<KarmaRepository> {
  private final Provider<KarmaApi> karmaApiProvider;

  public RepositoryModule_ProvideKarmaRepositoryFactory(Provider<KarmaApi> karmaApiProvider) {
    this.karmaApiProvider = karmaApiProvider;
  }

  @Override
  public KarmaRepository get() {
    return provideKarmaRepository(karmaApiProvider.get());
  }

  public static RepositoryModule_ProvideKarmaRepositoryFactory create(
      javax.inject.Provider<KarmaApi> karmaApiProvider) {
    return new RepositoryModule_ProvideKarmaRepositoryFactory(Providers.asDaggerProvider(karmaApiProvider));
  }

  public static RepositoryModule_ProvideKarmaRepositoryFactory create(
      Provider<KarmaApi> karmaApiProvider) {
    return new RepositoryModule_ProvideKarmaRepositoryFactory(karmaApiProvider);
  }

  public static KarmaRepository provideKarmaRepository(KarmaApi karmaApi) {
    return Preconditions.checkNotNullFromProvides(RepositoryModule.INSTANCE.provideKarmaRepository(karmaApi));
  }
}
