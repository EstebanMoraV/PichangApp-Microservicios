package cl.duoc.pichangapp.ui.screens.auth;

import cl.duoc.pichangapp.domain.usecase.RegisterUseCase;
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
public final class RegisterViewModel_Factory implements Factory<RegisterViewModel> {
  private final Provider<RegisterUseCase> registerUseCaseProvider;

  public RegisterViewModel_Factory(Provider<RegisterUseCase> registerUseCaseProvider) {
    this.registerUseCaseProvider = registerUseCaseProvider;
  }

  @Override
  public RegisterViewModel get() {
    return newInstance(registerUseCaseProvider.get());
  }

  public static RegisterViewModel_Factory create(
      javax.inject.Provider<RegisterUseCase> registerUseCaseProvider) {
    return new RegisterViewModel_Factory(Providers.asDaggerProvider(registerUseCaseProvider));
  }

  public static RegisterViewModel_Factory create(
      Provider<RegisterUseCase> registerUseCaseProvider) {
    return new RegisterViewModel_Factory(registerUseCaseProvider);
  }

  public static RegisterViewModel newInstance(RegisterUseCase registerUseCase) {
    return new RegisterViewModel(registerUseCase);
  }
}
