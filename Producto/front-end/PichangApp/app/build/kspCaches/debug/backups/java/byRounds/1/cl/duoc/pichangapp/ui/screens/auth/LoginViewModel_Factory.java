package cl.duoc.pichangapp.ui.screens.auth;

import cl.duoc.pichangapp.domain.usecase.LoginUseCase;
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
public final class LoginViewModel_Factory implements Factory<LoginViewModel> {
  private final Provider<LoginUseCase> loginUseCaseProvider;

  public LoginViewModel_Factory(Provider<LoginUseCase> loginUseCaseProvider) {
    this.loginUseCaseProvider = loginUseCaseProvider;
  }

  @Override
  public LoginViewModel get() {
    return newInstance(loginUseCaseProvider.get());
  }

  public static LoginViewModel_Factory create(
      javax.inject.Provider<LoginUseCase> loginUseCaseProvider) {
    return new LoginViewModel_Factory(Providers.asDaggerProvider(loginUseCaseProvider));
  }

  public static LoginViewModel_Factory create(Provider<LoginUseCase> loginUseCaseProvider) {
    return new LoginViewModel_Factory(loginUseCaseProvider);
  }

  public static LoginViewModel newInstance(LoginUseCase loginUseCase) {
    return new LoginViewModel(loginUseCase);
  }
}
