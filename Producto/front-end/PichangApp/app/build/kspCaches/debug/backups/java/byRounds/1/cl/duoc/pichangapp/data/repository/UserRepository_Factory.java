package cl.duoc.pichangapp.data.repository;

import cl.duoc.pichangapp.data.remote.UserApi;
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
public final class UserRepository_Factory implements Factory<UserRepository> {
  private final Provider<UserApi> userApiProvider;

  public UserRepository_Factory(Provider<UserApi> userApiProvider) {
    this.userApiProvider = userApiProvider;
  }

  @Override
  public UserRepository get() {
    return newInstance(userApiProvider.get());
  }

  public static UserRepository_Factory create(javax.inject.Provider<UserApi> userApiProvider) {
    return new UserRepository_Factory(Providers.asDaggerProvider(userApiProvider));
  }

  public static UserRepository_Factory create(Provider<UserApi> userApiProvider) {
    return new UserRepository_Factory(userApiProvider);
  }

  public static UserRepository newInstance(UserApi userApi) {
    return new UserRepository(userApi);
  }
}
