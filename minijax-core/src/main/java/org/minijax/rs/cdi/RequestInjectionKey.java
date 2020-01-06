//package org.minijax.rs.cdi;
//
//import java.lang.annotation.Annotation;
//import java.util.Set;
//
//import org.minijax.cdi.Key;
//import org.minijax.cdi.MinijaxInjector;
//import org.minijax.cdi.MinijaxProvider;
//
//public class RequestInjectionKey<T> extends Key<T> {
////    private final String name;
////    private final DefaultValue defaultValue;
//    private final MinijaxProvider<T> provider;
//
//    public RequestInjectionKey(
//            final MinijaxInjector injector,
//            final Class<T> type,
//            final Annotation[] annotations,
////            final String name,
////            final DefaultValue defaultValue,
//            final MinijaxProvider<T> provider) {
//
//        super(injector, type, annotations);
////        this.name = name;
////        this.defaultValue = defaultValue;
//        this.provider = provider;
//    }
//
////    public String getName() {
////        return name;
////    }
////
////    public DefaultValue getDefaultValue() {
////        return defaultValue;
////    }
//
//    @Override
//    public MinijaxProvider<T> getProvider(final Set<Key<?>> chain) {
////        throw new UnsupportedOperationException();
//        return provider;
//    }
//
//}
