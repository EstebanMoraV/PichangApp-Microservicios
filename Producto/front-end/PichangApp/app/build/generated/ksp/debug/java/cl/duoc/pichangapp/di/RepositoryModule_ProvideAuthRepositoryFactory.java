package cl.duoc.pichangapp.di;

import cl.duoc.pichangapp.core.datastore.TokenDataStore;
import cl.duoc.pichangapp.data.remote.AuthApi;
import cl.duoc.pichangapp.data.repository.AuthRepository;
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
public final class RepositoryModule_ProvideAuthRepositoryFactory implements Factory<AuthRepository> {
  private final Provider<AuthApi> authApiProvider;

  private final Provider<TokenDataStore> tokenDataStoreProvider;

  public RepositoryModule_ProvideAuthRepositoryFactory(Provider<AuthApi> authApiProvider,
      Provider<TokenDataStore> tokenDataStoreProvider) {
    this.authApiProvider = authApiProvider;
    this.tokenDataStoreProvider = tokenDataStoreProvider;
  }

  @Override
  public AuthRepository get() {
    return provideAuthRepository(authApiProvider.get(), tokenDataStoreProvider.get());
  }

  public static RepositoryModule_ProvideAuthRepositoryFactory create(
      javax.inject.Provider<AuthApi> authApiProvider,
      javax.inject.Provider<TokenDataStore> tokenDataStoreProvider) {
    return new RepositoryModule_ProvideAuthRepositoryFactory(Providers.asDaggerProvider(authApiProvider), Providers.asDaggerProvider(tokenDataStoreProvider));
  }

  public static RepositoryModule_ProvideAuthRepositoryFactory create(
      Provider<AuthApi> authApiProvider, Provider<TokenDataStore> tokenDataStoreProvider) {
    return new RepositoryModule_ProvideAuthRepositoryFactory(authApiProvider, tokenDataStoreProvider);
  }

  public static AuthRepository provideAuthRepository(AuthApi authApi,
      TokenDataStore tokenDataStore) {
    return Preconditions.checkNotNullFromProvides(RepositoryModule.INSTANCE.provideAuthRepository(authApi, tokenDataStore));
  }
}
