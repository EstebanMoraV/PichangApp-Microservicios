package cl.duoc.pichangapp.ui.screens.splash;

import cl.duoc.pichangapp.core.datastore.TokenDataStore;
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
public final class SplashViewModel_Factory implements Factory<SplashViewModel> {
  private final Provider<TokenDataStore> tokenDataStoreProvider;

  public SplashViewModel_Factory(Provider<TokenDataStore> tokenDataStoreProvider) {
    this.tokenDataStoreProvider = tokenDataStoreProvider;
  }

  @Override
  public SplashViewModel get() {
    return newInstance(tokenDataStoreProvider.get());
  }

  public static SplashViewModel_Factory create(
      javax.inject.Provider<TokenDataStore> tokenDataStoreProvider) {
    return new SplashViewModel_Factory(Providers.asDaggerProvider(tokenDataStoreProvider));
  }

  public static SplashViewModel_Factory create(Provider<TokenDataStore> tokenDataStoreProvider) {
    return new SplashViewModel_Factory(tokenDataStoreProvider);
  }

  public static SplashViewModel newInstance(TokenDataStore tokenDataStore) {
    return new SplashViewModel(tokenDataStore);
  }
}
