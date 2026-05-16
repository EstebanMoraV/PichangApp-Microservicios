package cl.duoc.pichangapp.ui.screens.karma;

import cl.duoc.pichangapp.core.datastore.TokenDataStore;
import cl.duoc.pichangapp.domain.usecase.GetKarmaUseCase;
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
public final class KarmaViewModel_Factory implements Factory<KarmaViewModel> {
  private final Provider<GetKarmaUseCase> getKarmaUseCaseProvider;

  private final Provider<TokenDataStore> tokenDataStoreProvider;

  public KarmaViewModel_Factory(Provider<GetKarmaUseCase> getKarmaUseCaseProvider,
      Provider<TokenDataStore> tokenDataStoreProvider) {
    this.getKarmaUseCaseProvider = getKarmaUseCaseProvider;
    this.tokenDataStoreProvider = tokenDataStoreProvider;
  }

  @Override
  public KarmaViewModel get() {
    return newInstance(getKarmaUseCaseProvider.get(), tokenDataStoreProvider.get());
  }

  public static KarmaViewModel_Factory create(
      javax.inject.Provider<GetKarmaUseCase> getKarmaUseCaseProvider,
      javax.inject.Provider<TokenDataStore> tokenDataStoreProvider) {
    return new KarmaViewModel_Factory(Providers.asDaggerProvider(getKarmaUseCaseProvider), Providers.asDaggerProvider(tokenDataStoreProvider));
  }

  public static KarmaViewModel_Factory create(Provider<GetKarmaUseCase> getKarmaUseCaseProvider,
      Provider<TokenDataStore> tokenDataStoreProvider) {
    return new KarmaViewModel_Factory(getKarmaUseCaseProvider, tokenDataStoreProvider);
  }

  public static KarmaViewModel newInstance(GetKarmaUseCase getKarmaUseCase,
      TokenDataStore tokenDataStore) {
    return new KarmaViewModel(getKarmaUseCase, tokenDataStore);
  }
}
