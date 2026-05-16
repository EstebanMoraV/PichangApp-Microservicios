package cl.duoc.pichangapp.ui.screens.events;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
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
public final class EventsViewModel_Factory implements Factory<EventsViewModel> {
  @Override
  public EventsViewModel get() {
    return newInstance();
  }

  public static EventsViewModel_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static EventsViewModel newInstance() {
    return new EventsViewModel();
  }

  private static final class InstanceHolder {
    static final EventsViewModel_Factory INSTANCE = new EventsViewModel_Factory();
  }
}
