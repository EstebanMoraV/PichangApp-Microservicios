package cl.duoc.pichangapp.core.network;

import cl.duoc.pichangapp.core.datastore.TokenDataStore;
import cl.duoc.pichangapp.core.util.SessionManager;
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
public final class AuthInterceptor_Factory implements Factory<AuthInterceptor> {
  private final Provider<TokenDataStore> tokenDataStoreProvider;

  private final Provider<SessionManager> sessionManagerProvider;

  public AuthInterceptor_Factory(Provider<TokenDataStore> tokenDataStoreProvider,
      Provider<SessionManager> sessionManagerProvider) {
    this.tokenDataStoreProvider = tokenDataStoreProvider;
    this.sessionManagerProvider = sessionManagerProvider;
  }

  @Override
  public AuthInterceptor get() {
    return newInstance(tokenDataStoreProvider.get(), sessionManagerProvider.get());
  }

  public static AuthInterceptor_Factory create(
      javax.inject.Provider<TokenDataStore> tokenDataStoreProvider,
      javax.inject.Provider<SessionManager> sessionManagerProvider) {
    return new AuthInterceptor_Factory(Providers.asDaggerProvider(tokenDataStoreProvider), Providers.asDaggerProvider(sessionManagerProvider));
  }

  public static AuthInterceptor_Factory create(Provider<TokenDataStore> tokenDataStoreProvider,
      Provider<SessionManager> sessionManagerProvider) {
    return new AuthInterceptor_Factory(tokenDataStoreProvider, sessionManagerProvider);
  }

  public static AuthInterceptor newInstance(TokenDataStore tokenDataStore,
      SessionManager sessionManager) {
    return new AuthInterceptor(tokenDataStore, sessionManager);
  }
}
