package cl.duoc.pichangapp.ui.screens.home;

import cl.duoc.pichangapp.core.datastore.TokenDataStore;
import cl.duoc.pichangapp.domain.usecase.GetKarmaUseCase;
import cl.duoc.pichangapp.domain.usecase.GetUserProfileUseCase;
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
public final class HomeViewModel_Factory implements Factory<HomeViewModel> {
  private final Provider<GetUserProfileUseCase> getUserProfileUseCaseProvider;

  private final Provider<GetKarmaUseCase> getKarmaUseCaseProvider;

  private final Provider<TokenDataStore> tokenDataStoreProvider;

  public HomeViewModel_Factory(Provider<GetUserProfileUseCase> getUserProfileUseCaseProvider,
      Provider<GetKarmaUseCase> getKarmaUseCaseProvider,
      Provider<TokenDataStore> tokenDataStoreProvider) {
    this.getUserProfileUseCaseProvider = getUserProfileUseCaseProvider;
    this.getKarmaUseCaseProvider = getKarmaUseCaseProvider;
    this.tokenDataStoreProvider = tokenDataStoreProvider;
  }

  @Override
  public HomeViewModel get() {
    return newInstance(getUserProfileUseCaseProvider.get(), getKarmaUseCaseProvider.get(), tokenDataStoreProvider.get());
  }

  public static HomeViewModel_Factory create(
      javax.inject.Provider<GetUserProfileUseCase> getUserProfileUseCaseProvider,
      javax.inject.Provider<GetKarmaUseCase> getKarmaUseCaseProvider,
      javax.inject.Provider<TokenDataStore> tokenDataStoreProvider) {
    return new HomeViewModel_Factory(Providers.asDaggerProvider(getUserProfileUseCaseProvider), Providers.asDaggerProvider(getKarmaUseCaseProvider), Providers.asDaggerProvider(tokenDataStoreProvider));
  }

  public static HomeViewModel_Factory create(
      Provider<GetUserProfileUseCase> getUserProfileUseCaseProvider,
      Provider<GetKarmaUseCase> getKarmaUseCaseProvider,
      Provider<TokenDataStore> tokenDataStoreProvider) {
    return new HomeViewModel_Factory(getUserProfileUseCaseProvider, getKarmaUseCaseProvider, tokenDataStoreProvider);
  }

  public static HomeViewModel newInstance(GetUserProfileUseCase getUserProfileUseCase,
      GetKarmaUseCase getKarmaUseCase, TokenDataStore tokenDataStore) {
    return new HomeViewModel(getUserProfileUseCase, getKarmaUseCase, tokenDataStore);
  }
}
