package cl.duoc.pichangapp.di;

import cl.duoc.pichangapp.data.remote.KarmaApi;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.Provider;
import dagger.internal.Providers;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import retrofit2.Retrofit;

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
public final class NetworkModule_ProvideKarmaApiFactory implements Factory<KarmaApi> {
  private final Provider<Retrofit> retrofitProvider;

  public NetworkModule_ProvideKarmaApiFactory(Provider<Retrofit> retrofitProvider) {
    this.retrofitProvider = retrofitProvider;
  }

  @Override
  public KarmaApi get() {
    return provideKarmaApi(retrofitProvider.get());
  }

  public static NetworkModule_ProvideKarmaApiFactory create(
      javax.inject.Provider<Retrofit> retrofitProvider) {
    return new NetworkModule_ProvideKarmaApiFactory(Providers.asDaggerProvider(retrofitProvider));
  }

  public static NetworkModule_ProvideKarmaApiFactory create(Provider<Retrofit> retrofitProvider) {
    return new NetworkModule_ProvideKarmaApiFactory(retrofitProvider);
  }

  public static KarmaApi provideKarmaApi(Retrofit retrofit) {
    return Preconditions.checkNotNullFromProvides(NetworkModule.INSTANCE.provideKarmaApi(retrofit));
  }
}
