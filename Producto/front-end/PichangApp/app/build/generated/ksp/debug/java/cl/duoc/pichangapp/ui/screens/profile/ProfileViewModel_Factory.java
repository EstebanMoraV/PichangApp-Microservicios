package cl.duoc.pichangapp.ui.screens.profile;

import cl.duoc.pichangapp.core.datastore.TokenDataStore;
import cl.duoc.pichangapp.domain.usecase.GetUserProfileUseCase;
import cl.duoc.pichangapp.domain.usecase.LogoutUseCase;
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
public final class ProfileViewModel_Factory implements Factory<ProfileViewModel> {
  private final Provider<GetUserProfileUseCase> getUserProfileUseCaseProvider;

  private final Provider<LogoutUseCase> logoutUseCaseProvider;

  private final Provider<TokenDataStore> tokenDataStoreProvider;

  public ProfileViewModel_Factory(Provider<GetUserProfileUseCase> getUserProfileUseCaseProvider,
      Provider<LogoutUseCase> logoutUseCaseProvider,
      Provider<TokenDataStore> tokenDataStoreProvider) {
    this.getUserProfileUseCaseProvider = getUserProfileUseCaseProvider;
    this.logoutUseCaseProvider = logoutUseCaseProvider;
    this.tokenDataStoreProvider = tokenDataStoreProvider;
  }

  @Override
  public ProfileViewModel get() {
    return newInstance(getUserProfileUseCaseProvider.get(), logoutUseCaseProvider.get(), tokenDataStoreProvider.get());
  }

  public static ProfileViewModel_Factory create(
      javax.inject.Provider<GetUserProfileUseCase> getUserProfileUseCaseProvider,
      javax.inject.Provider<LogoutUseCase> logoutUseCaseProvider,
      javax.inject.Provider<TokenDataStore> tokenDataStoreProvider) {
    return new ProfileViewModel_Factory(Providers.asDaggerProvider(getUserProfileUseCaseProvider), Providers.asDaggerProvider(logoutUseCaseProvider), Providers.asDaggerProvider(tokenDataStoreProvider));
  }

  public static ProfileViewModel_Factory create(
      Provider<GetUserProfileUseCase> getUserProfileUseCaseProvider,
      Provider<LogoutUseCase> logoutUseCaseProvider,
      Provider<TokenDataStore> tokenDataStoreProvider) {
    return new ProfileViewModel_Factory(getUserProfileUseCaseProvider, logoutUseCaseProvider, tokenDataStoreProvider);
  }

  public static ProfileViewModel newInstance(GetUserProfileUseCase getUserProfileUseCase,
      LogoutUseCase logoutUseCase, TokenDataStore tokenDataStore) {
    return new ProfileViewModel(getUserProfileUseCase, logoutUseCase, tokenDataStore);
  }
}
