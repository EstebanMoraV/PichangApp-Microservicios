package cl.duoc.pichangapp.ui.screens.auth;

import cl.duoc.pichangapp.data.remote.AuthApi;
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
public final class VerifyCodeViewModel_Factory implements Factory<VerifyCodeViewModel> {
  private final Provider<AuthApi> authApiProvider;

  public VerifyCodeViewModel_Factory(Provider<AuthApi> authApiProvider) {
    this.authApiProvider = authApiProvider;
  }

  @Override
  public VerifyCodeViewModel get() {
    return newInstance(authApiProvider.get());
  }

  public static VerifyCodeViewModel_Factory create(javax.inject.Provider<AuthApi> authApiProvider) {
    return new VerifyCodeViewModel_Factory(Providers.asDaggerProvider(authApiProvider));
  }

  public static VerifyCodeViewModel_Factory create(Provider<AuthApi> authApiProvider) {
    return new VerifyCodeViewModel_Factory(authApiProvider);
  }

  public static VerifyCodeViewModel newInstance(AuthApi authApi) {
    return new VerifyCodeViewModel(authApi);
  }
}
