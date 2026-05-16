package cl.duoc.pichangapp.di;

import cl.duoc.pichangapp.data.remote.NotificationApi;
import cl.duoc.pichangapp.data.repository.NotificationRepository;
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
public final class RepositoryModule_ProvideNotificationRepositoryFactory implements Factory<NotificationRepository> {
  private final Provider<NotificationApi> notificationApiProvider;

  public RepositoryModule_ProvideNotificationRepositoryFactory(
      Provider<NotificationApi> notificationApiProvider) {
    this.notificationApiProvider = notificationApiProvider;
  }

  @Override
  public NotificationRepository get() {
    return provideNotificationRepository(notificationApiProvider.get());
  }

  public static RepositoryModule_ProvideNotificationRepositoryFactory create(
      javax.inject.Provider<NotificationApi> notificationApiProvider) {
    return new RepositoryModule_ProvideNotificationRepositoryFactory(Providers.asDaggerProvider(notificationApiProvider));
  }

  public static RepositoryModule_ProvideNotificationRepositoryFactory create(
      Provider<NotificationApi> notificationApiProvider) {
    return new RepositoryModule_ProvideNotificationRepositoryFactory(notificationApiProvider);
  }

  public static NotificationRepository provideNotificationRepository(
      NotificationApi notificationApi) {
    return Preconditions.checkNotNullFromProvides(RepositoryModule.INSTANCE.provideNotificationRepository(notificationApi));
  }
}
