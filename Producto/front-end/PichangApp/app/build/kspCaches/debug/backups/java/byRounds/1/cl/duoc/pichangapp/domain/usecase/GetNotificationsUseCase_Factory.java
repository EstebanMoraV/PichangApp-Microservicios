package cl.duoc.pichangapp.domain.usecase;

import cl.duoc.pichangapp.data.repository.NotificationRepository;
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
public final class GetNotificationsUseCase_Factory implements Factory<GetNotificationsUseCase> {
  private final Provider<NotificationRepository> notificationRepositoryProvider;

  public GetNotificationsUseCase_Factory(
      Provider<NotificationRepository> notificationRepositoryProvider) {
    this.notificationRepositoryProvider = notificationRepositoryProvider;
  }

  @Override
  public GetNotificationsUseCase get() {
    return newInstance(notificationRepositoryProvider.get());
  }

  public static GetNotificationsUseCase_Factory create(
      javax.inject.Provider<NotificationRepository> notificationRepositoryProvider) {
    return new GetNotificationsUseCase_Factory(Providers.asDaggerProvider(notificationRepositoryProvider));
  }

  public static GetNotificationsUseCase_Factory create(
      Provider<NotificationRepository> notificationRepositoryProvider) {
    return new GetNotificationsUseCase_Factory(notificationRepositoryProvider);
  }

  public static GetNotificationsUseCase newInstance(NotificationRepository notificationRepository) {
    return new GetNotificationsUseCase(notificationRepository);
  }
}
