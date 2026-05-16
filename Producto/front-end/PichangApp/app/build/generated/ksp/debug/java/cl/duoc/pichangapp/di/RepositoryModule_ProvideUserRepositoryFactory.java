package cl.duoc.pichangapp.di;

import cl.duoc.pichangapp.data.remote.UserApi;
import cl.duoc.pichangapp.data.repository.UserRepository;
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
public final class RepositoryModule_ProvideUserRepositoryFactory implements Factory<UserRepository> {
  private final Provider<UserApi> userApiProvider;

  public RepositoryModule_ProvideUserRepositoryFactory(Provider<UserApi> userApiProvider) {
    this.userApiProvider = userApiProvider;
  }

  @Override
  public UserRepository get() {
    return provideUserRepository(userApiProvider.get());
  }

  public static RepositoryModule_ProvideUserRepositoryFactory create(
      javax.inject.Provider<UserApi> userApiProvider) {
    return new RepositoryModule_ProvideUserRepositoryFactory(Providers.asDaggerProvider(userApiProvider));
  }

  public static RepositoryModule_ProvideUserRepositoryFactory create(
      Provider<UserApi> userApiProvider) {
    return new RepositoryModule_ProvideUserRepositoryFactory(userApiProvider);
  }

  public static UserRepository provideUserRepository(UserApi userApi) {
    return Preconditions.checkNotNullFromProvides(RepositoryModule.INSTANCE.provideUserRepository(userApi));
  }
}
