package cl.duoc.pichangapp.ui.screens.notifications;

import cl.duoc.pichangapp.core.datastore.TokenDataStore;
import cl.duoc.pichangapp.domain.usecase.GetNotificationsUseCase;
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
public final class NotificationsViewModel_Factory implements Factory<NotificationsViewModel> {
  private final Provider<GetNotificationsUseCase> getNotificationsUseCaseProvider;

  private final Provider<TokenDataStore> tokenDataStoreProvider;

  public NotificationsViewModel_Factory(
      Provider<GetNotificationsUseCase> getNotificationsUseCaseProvider,
      Provider<TokenDataStore> tokenDataStoreProvider) {
    this.getNotificationsUseCaseProvider = getNotificationsUseCaseProvider;
    this.tokenDataStoreProvider = tokenDataStoreProvider;
  }

  @Override
  public NotificationsViewModel get() {
    return newInstance(getNotificationsUseCaseProvider.get(), tokenDataStoreProvider.get());
  }

  public static NotificationsViewModel_Factory create(
      javax.inject.Provider<GetNotificationsUseCase> getNotificationsUseCaseProvider,
      javax.inject.Provider<TokenDataStore> tokenDataStoreProvider) {
    return new NotificationsViewModel_Factory(Providers.asDaggerProvider(getNotificationsUseCaseProvider), Providers.asDaggerProvider(tokenDataStoreProvider));
  }

  public static NotificationsViewModel_Factory create(
      Provider<GetNotificationsUseCase> getNotificationsUseCaseProvider,
      Provider<TokenDataStore> tokenDataStoreProvider) {
    return new NotificationsViewModel_Factory(getNotificationsUseCaseProvider, tokenDataStoreProvider);
  }

  public static NotificationsViewModel newInstance(GetNotificationsUseCase getNotificationsUseCase,
      TokenDataStore tokenDataStore) {
    return new NotificationsViewModel(getNotificationsUseCase, tokenDataStore);
  }
}
