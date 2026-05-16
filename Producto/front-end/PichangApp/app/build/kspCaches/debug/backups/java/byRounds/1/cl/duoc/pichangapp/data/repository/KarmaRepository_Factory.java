package cl.duoc.pichangapp.data.repository;

import cl.duoc.pichangapp.data.remote.KarmaApi;
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
public final class KarmaRepository_Factory implements Factory<KarmaRepository> {
  private final Provider<KarmaApi> karmaApiProvider;

  public KarmaRepository_Factory(Provider<KarmaApi> karmaApiProvider) {
    this.karmaApiProvider = karmaApiProvider;
  }

  @Override
  public KarmaRepository get() {
    return newInstance(karmaApiProvider.get());
  }

  public static KarmaRepository_Factory create(javax.inject.Provider<KarmaApi> karmaApiProvider) {
    return new KarmaRepository_Factory(Providers.asDaggerProvider(karmaApiProvider));
  }

  public static KarmaRepository_Factory create(Provider<KarmaApi> karmaApiProvider) {
    return new KarmaRepository_Factory(karmaApiProvider);
  }

  public static KarmaRepository newInstance(KarmaApi karmaApi) {
    return new KarmaRepository(karmaApi);
  }
}
