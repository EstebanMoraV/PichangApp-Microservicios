package cl.duoc.pichangapp.data.repository;

import cl.duoc.pichangapp.data.remote.NotificationApi;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
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
public final class NotificationRepository_Factory implements Factory<NotificationRepository> {
  private final Provider<NotificationApi> notificationApiProvider;

  public NotificationRepository_Factory(Provider<NotificationApi> notificationApiProvider) {
    this.notificationApiProvider = notificationApiProvider;
  }

  @Override
  public NotificationRepository get() {
    return newInstance(notificationApiProvider.get());
  }

  public static NotificationRepository_Factory create(
      javax.inject.Provider<NotificationApi> notificationApiProvider) {
    return new NotificationRepository_Factory(Providers.asDaggerProvider(notificationApiProvider));
  }

  public static NotificationRepository_Factory create(
      Provider<NotificationApi> notificationApiProvider) {
    return new NotificationRepository_Factory(notificationApiProvider);
  }

  public static NotificationRepository newInstance(NotificationApi notificationApi) {
    return new NotificationRepository(notificationApi);
  }
}
