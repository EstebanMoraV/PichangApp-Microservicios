package cl.duoc.pichangapp.data.repository;

import cl.duoc.pichangapp.core.datastore.TokenDataStore;
import cl.duoc.pichangapp.data.remote.AuthApi;
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
public final class AuthRepository_Factory implements Factory<AuthRepository> {
  private final Provider<AuthApi> authApiProvider;

  private final Provider<TokenDataStore> tokenDataStoreProvider;

  public AuthRepository_Factory(Provider<AuthApi> authApiProvider,
      Provider<TokenDataStore> tokenDataStoreProvider) {
    this.authApiProvider = authApiProvider;
    this.tokenDataStoreProvider = tokenDataStoreProvider;
  }

  @Override
  public AuthRepository get() {
    return newInstance(authApiProvider.get(), tokenDataStoreProvider.get());
  }

  public static AuthRepository_Factory create(javax.inject.Provider<AuthApi> authApiProvider,
      javax.inject.Provider<TokenDataStore> tokenDataStoreProvider) {
    return new AuthRepository_Factory(Providers.asDaggerProvider(authApiProvider), Providers.asDaggerProvider(tokenDataStoreProvider));
  }

  public static AuthRepository_Factory create(Provider<AuthApi> authApiProvider,
      Provider<TokenDataStore> tokenDataStoreProvider) {
    return new AuthRepository_Factory(authApiProvider, tokenDataStoreProvider);
  }

  public static AuthRepository newInstance(AuthApi authApi, TokenDataStore tokenDataStore) {
    return new AuthRepository(authApi, tokenDataStore);
  }
}
