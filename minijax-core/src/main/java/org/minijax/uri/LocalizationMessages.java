package org.minijax.uri;

/**
 * Defines string formatting method for each constant in the resource file.
 */
public final class LocalizationMessages {

    public static String ERRORS_AND_WARNINGS_DETECTED(final Object arg0) {
        return String.format("Following issues have been detected: {0}");
    }

    public static String COMMITTING_STREAM_BUFFERING_ILLEGAL_STATE() {
        return String.format("Cannot setup buffering as bytes have already been written to the output stream. The entity buffering can be initialized only before first bytes are written to the entity output stream.");
    }

    public static String LOCALE_IS_NULL() {
        return String.format("Locale is null.");
    }

    public static String SSL_KMF_PROVIDER_NOT_REGISTERED() {
        return String.format("Error initializing key manager factory (provider not registered).");
    }

    public static String URI_COMPONENT_ENCODED_OCTET_INVALID_DIGIT(final Object arg0, final Object arg1) {
        return String.format("Malformed percent-encoded octet at index {0}, invalid hexadecimal digit ''{1}''.");
    }

    public static String URI_PARSER_COMPONENT_DELIMITER(final Object arg0, final Object arg1) {
        return String.format("Component does not end by a delimiter ''{0}'' at index {1}.");
    }

    public static String SSL_KMF_ALGORITHM_NOT_SUPPORTED() {
        return String.format("Error initializing key manager factory (algorithm not supported).");
    }

    public static String ERROR_MBR_ISREADABLE(final Object arg0) {
        return String.format("MesssageBodyReader {0} threw exception in isReadable - skipping.");
    }

    public static String SSL_KMF_INIT_FAILED() {
        return String.format("Error initializing key manager factory (operation failed).");
    }

    public static String OVERRIDING_METHOD_CANNOT_BE_FOUND(final Object arg0, final Object arg1) {
        return String.format("Method that overrides {0} cannot be found on class {1}.");
    }

    public static String ERROR_INTERCEPTOR_READER_PROCEED() {
        return String.format("Last reader interceptor in the chain called the method proceed.");
    }

    public static String MEDIA_TYPE_IS_NULL() {
        return String.format("Media type is null.");
    }

    public static String URI_COMPONENT_ENCODED_OCTET_MALFORMED(final Object arg0) {
        return String.format("Malformed percent-encoded octet at index {0}.");
    }

    public static String SSL_KS_INTEGRITY_ALGORITHM_NOT_FOUND() {
        return String.format("Error initializing key store (algorithm to check key store integrity not found).");
    }

    public static String MESSAGE_CONTENT_BUFFER_RESET_FAILED() {
        return String.format("Error resetting the buffered message content input stream.");
    }

    public static String TEMPLATE_PARAM_NULL() {
        return String.format("One or more of template keys or values are null.");
    }

    public static String SSL_TMF_INIT_FAILED() {
        return String.format("Error initializing trust manager factory (operation failed).");
    }

    public static String URI_BUILDER_CLASS_PATH_ANNOTATION_MISSING(final Object arg0) {
        return String.format("The class, {0} is not annotated with @Path.");
    }

    public static String UNHANDLED_EXCEPTION_DETECTED(final Object arg0) {
        return String.format("Unhandled exception detected on thread {0}.");
    }

    public static String NOT_SUPPORTED_ON_OUTBOUND_MESSAGE() {
        return String.format("Method not supported on an outbound message.");
    }

    public static String UNABLE_TO_PARSE_HEADER_VALUE(final Object arg0, final Object arg1) {
        return String.format("Unable to parse \"{0}\" header value: \"{1}\"");
    }

    public static String ERROR_PROVIDER_CONSTRAINED_TO_WRONG_RUNTIME(final Object arg0, final Object arg1, final Object arg2) {
        return String.format("A provider {0} registered in {2} runtime is constrained (via @ConstrainedTo) to {1} runtime.");
    }

    public static String SSL_KMF_NO_PASSWORD_SET(final Object arg0) {
        return String.format("Neither key password nor key store password has been set for {0} key store. Ignoring the key store configuration and skipping the key manager factory initialization. Key manager factory will not be configured in the current SSL context.");
    }

    public static String PARAM_NULL(final Object arg0) {
        return String.format("\"{0}\" parameter is null.");
    }

    public static String HTTP_HEADER_UNBALANCED_QUOTED() {
        return String.format("Unbalanced quoted string.");
    }

    public static String LINK_IS_NULL() {
        return String.format("Link is null.");
    }

    public static String ERROR_TEMPLATE_PARSER_ILLEGAL_CHAR_PART_OF_NAME(final Object arg0, final Object arg1, final Object arg2) {
        return String.format("Illegal character \"{0}\" at position {1} is not allowed as a part of a name in a path template \"{2}\".");
    }

    public static String PROPERTIES_HELPER_DEPRECATED_PROPERTY_NAME(final Object arg0, final Object arg1) {
        return String.format("Deprecated property name \"{0}\" usage was found. Please use \"{1}\" instead.");
    }

    public static String COMPONENT_CANNOT_BE_NULL() {
        return String.format("Registered component instance cannot be null.");
    }

    public static String URI_BUILDER_ANNOTATEDELEMENT_PATH_ANNOTATION_MISSING(final Object arg0) {
        return String.format("The annotated element \"{0}\" is not annotated with @Path.");
    }

    public static String ERROR_SERVICE_LOCATOR_PROVIDER_INSTANCE_FEATURE_CONTEXT(final Object arg0) {
        return String.format("Incorrect type of feature context instance {0}. Parameter must be a default Jersey FeatureContext implementation.");
    }

    public static String ERROR_TEMPLATE_PARSER_ILLEGAL_CHAR_START_NAME(final Object arg0, final Object arg1, final Object arg2) {
        return String.format("Illegal character \"{0}\" at position {1} is not allowed as a start of a name in a path template \"{2}\".");
    }

    public static String CONFIGURATION_NOT_MODIFIABLE() {
        return String.format("The configuration is not modifiable in this context.");
    }

    public static String SSL_TS_CERT_LOAD_ERROR() {
        return String.format("Cannot load trust store certificates.");
    }

    public static String ERROR_FINDING_EXCEPTION_MAPPER_TYPE(final Object arg0) {
        return String.format("Could not find exception type for given ExceptionMapper class: {0}.");
    }

    public static String ERROR_NEWCOOKIE_EXPIRES(final Object arg0) {
        return String.format("NewCookie Expires header value ({0}) cannot be read.");
    }

    public static String ILLEGAL_INITIAL_CAPACITY(final Object arg0) {
        return String.format("Illegal initial capacity: {0}.");
    }

    public static String SSL_KS_CERT_LOAD_ERROR() {
        return String.format("Cannot load key store certificates.");
    }

    public static String ERROR_READING_ENTITY_FROM_INPUT_STREAM() {
        return String.format("Error reading entity from input stream.");
    }

    public static String ERROR_PROVIDER_CONSTRAINED_TO_IGNORED(final Object arg0) {
        return String.format("Due to constraint configuration problems the provider {0} will be ignored.");
    }

    public static String HTTP_HEADER_WHITESPACE_NOT_ALLOWED() {
        return String.format("White space not allowed.");
    }

    public static String ILLEGAL_CONFIG_SYNTAX() {
        return String.format("Illegal configuration-file syntax.");
    }

    public static String SSL_TS_FILE_NOT_FOUND(final Object arg0) {
        return String.format("Cannot find trust store file \"{0}\".");
    }

    public static String ERROR_CAUGHT_WHILE_LOADING_SPI_PROVIDERS() {
        return String.format("Error caught while loading SPI providers.");
    }

    public static String MULTIPLE_MATCHING_CONSTRUCTORS_FOUND(final Object arg0, final Object arg1, final Object arg2, final Object arg3) {
        return String.format("Found {0} constructors with {1} parameters in {2} class. Selecting the first found constructor: {3}");
    }

    public static String METHOD_NOT_GETTER_NOR_SETTER() {
        return String.format("Method is neither getter nor setter.");
    }

    public static String ERROR_PARSING_ENTITY_TAG(final Object arg0) {
        return String.format("Error parsing entity tag: {0}");
    }

    public static String SSL_CTX_ALGORITHM_NOT_SUPPORTED() {
        return String.format("Error creating SSL context (algorithm not supported).");
    }

    public static String ERROR_TEMPLATE_PARSER_ILLEGAL_CHAR_AFTER_NAME(final Object arg0, final Object arg1, final Object arg2) {
        return String.format("Illegal character \"{0}\" at position {1} is not allowed after a name in a path template \"{2}\".");
    }

    public static String INJECTION_MANAGER_FACTORY_NOT_FOUND() {
        return String.format("InjectionManagerFactory not found.");
    }

    public static String OUTPUT_STREAM_CLOSED() {
        return String.format("The output stream has already been closed.");
    }

    public static String ENTITY_TAG_IS_NULL() {
        return String.format("Entity tag is null.");
    }

    public static String INPUT_STREAM_CLOSED() {
        return String.format("Input stream has been closed.");
    }

    public static String COOKIE_IS_NULL() {
        return String.format("Cookie is null.");
    }

    public static String NEW_COOKIE_IS_NULL() {
        return String.format("New cookie is null.");
    }

    public static String INJECTION_ERROR_LOCAL_CLASS_NOT_SUPPORTED(final Object arg0) {
        return String.format("Cannot instantiate {0} class. Instantiation of local classes is not supported.");
    }

    public static String SSL_TS_PROVIDERS_NOT_REGISTERED() {
        return String.format("Error initializing trust store (provider not registered).");
    }

    public static String INJECTION_ERROR_NONSTATIC_MEMBER_CLASS_NOT_SUPPORTED(final Object arg0) {
        return String.format("Cannot instantiate {0} class. Instantiation of non-static member classes is not supported.");
    }

    public static String UNKNOWN_DESCRIPTOR_TYPE(final Object arg0) {
        return String.format("Unable to register a service because of unknown descriptor type: {0}.");
    }

    public static String URI_BUILDER_SCHEME_PART_NULL() {
        return String.format("Supplied scheme-specific part parameter is null.");
    }

    public static String MATRIX_PARAM_NULL() {
        return String.format("One or more of matrix value parameters are null.");
    }

    public static String WARNINGS_DETECTED(final Object arg0) {
        return String.format("The following warnings have been detected: {0}");
    }

    public static String HINT_MSG(final Object arg0) {
        return String.format("HINT: {0}");
    }

    public static String SSL_TS_LOAD_ERROR(final Object arg0) {
        return String.format("Error loading trust store from file \"{0}\".");
    }

    public static String ERROR_PROVIDER_REGISTERED_WRONG_RUNTIME(final Object arg0, final Object arg1) {
        return String.format("A provider {0} registered in {1} runtime does not implement any provider interfaces applicable in the {1} runtime.");
    }

    public static String SSL_KMF_NO_PASSWORD_FOR_PROVIDER_BASED_KS() {
        return String.format("\"provider based\"");
    }

    public static String URI_PARSER_SCHEME_EXPECTED(final Object arg0, final Object arg1) {
        return String.format("Expected scheme name at index {0}: ''{1}''.");
    }

    public static String THREAD_POOL_EXECUTOR_PROVIDER_CLOSED() {
        return String.format("Thread pool executor provider has been closed.");
    }

    public static String MBW_TRYING_TO_CLOSE_STREAM(final Object arg0) {
        return String.format("Message body writer ({0}) is trying to close the entity output stream. Not closing.");
    }

    public static String COMPONENT_CONTRACTS_EMPTY_OR_NULL(final Object arg0) {
        return String.format("Attempt to register component of type {0} to null or empty array of contracts  is ignored.");
    }

    public static String PROVIDER_NOT_FOUND(final Object arg0, final Object arg1) {
        return String.format("The class {0} implementing the provider {1} is not found. The provider implementation is ignored.");
    }

    public static String TOO_MANY_HEADER_VALUES(final Object arg0, final Object arg1) {
        return String.format("Too many \"{0}\" header values: \"{1}\"");
    }

    public static String CACHE_CONTROL_IS_NULL() {
        return String.format("Cache control is null.");
    }

    public static String HTTP_HEADER_END_OF_HEADER() {
        return String.format("End of header.");
    }

    public static String USING_SCHEDULER_PROVIDER(final Object arg0, final Object arg1) {
        return String.format("Selected ScheduledExecutorServiceProvider implementation [{0}] to be used for injection of scheduler qualified by [{1}] annotation.");
    }

    public static String HTTP_HEADER_COMMENTS_NOT_ALLOWED() {
        return String.format("Comments are not allowed.");
    }

    public static String COMPONENT_CLASS_CANNOT_BE_NULL() {
        return String.format("Registered component class cannot be null.");
    }

    public static String URI_BUILDER_SCHEMA_PART_OPAQUE() {
        return String.format("Schema specific part is opaque.");
    }

    public static String NO_ERROR_PROCESSING_IN_SCOPE() {
        return String.format("There is no error processing in scope.");
    }

    public static String CONTRACT_NOT_SUPPORTED(final Object arg0, final Object arg1) {
        return String.format("Contract {0} can not be registered for component {1}: Contract type not supported.");
    }

    public static String INVALID_SPI_CLASSES(final Object arg0, final Object arg1) {
        return String.format("Supplied provider class(es) do not implement the expected {0} SPI: [{1}]");
    }

    public static String PROVIDER_COULD_NOT_BE_CREATED(final Object arg0, final Object arg1, final Object arg2) {
        return String.format("The class {0} implementing provider {1} could not be instantiated: {2}");
    }

    public static String ERROR_NOTFOUND_MESSAGEBODYREADER(final Object arg0, final Object arg1, final Object arg2) {
        return String.format("MessageBodyReader not found for media type={0}, type={1}, genericType={2}.");
    }

    public static String ERROR_SERVICE_LOCATOR_PROVIDER_INSTANCE_FEATURE_READER_INTERCEPTOR_CONTEXT(final Object arg0) {
        return String.format("Incorrect type of reader interceptor context instance {0}. Parameter must be a default Jersey ReaderInterceptorContext implementation.");
    }

    public static String USING_EXECUTOR_PROVIDER(final Object arg0, final Object arg1) {
        return String.format("Selected ExecutorServiceProvider implementation [{0}] to be used for injection of executor qualified by [{1}] annotation.");
    }

    public static String ERROR_SERVICE_LOCATOR_PROVIDER_INSTANCE_FEATURE_WRITER_INTERCEPTOR_CONTEXT(final Object arg0) {
        return String.format("Incorrect type of writer interceptor context instance {0}. Parameter must be a default Jersey WriterInterceptorContext implementation.");
    }

    public static String IGNORED_SCHEDULER_PROVIDERS(final Object arg0, final Object arg1) {
        return String.format("Multiple ScheduledExecutorServiceProvider registrations found for qualifier annotation [{1}]. The following provider registrations will be ignored: [{0}]");
    }

    public static String DEPENDENT_CLASS_OF_PROVIDER_NOT_FOUND(final Object arg0, final Object arg1, final Object arg2) {
        return String.format("A dependent class, {0}, of the class {1} implementing the provider {2} is not found. The provider implementation is ignored.");
    }

    public static String HTTP_HEADER_NO_END_SEPARATOR(final Object arg0) {
        return String.format("No end separator \"{0}\".");
    }

    public static String SSL_KS_IMPL_NOT_FOUND() {
        return String.format("Error initializing key store (implementation not available).");
    }

    public static String ERROR_PROVIDER_AND_RESOURCE_CONSTRAINED_TO_IGNORED(final Object arg0) {
        return String.format("Due to constraint configuration problems the implementations of providers in the resource class {0} will be ignored. However, the resource class will be loaded and used as an resource.");
    }

    public static String INVALID_PORT() {
        return String.format("Invalid port value.");
    }

    public static String ERROR_INTERCEPTOR_WRITER_PROCEED() {
        return String.format("Last writer interceptor in the chain called the method proceed.");
    }

    public static String HTTP_HEADER_NO_CHARS_BETWEEN_SEPARATORS(final Object arg0, final Object arg1) {
        return String.format("No characters between the separators \"{0}\" and \"{1}\".");
    }

    public static String ILLEGAL_LOAD_FACTOR(final Object arg0) {
        return String.format("Illegal load factor: {0}.");
    }

//    /**
//     * There are some request headers that have not been sent by connector [{0}]. Probably you added those headers in WriterInterceptor or MessageBodyWriter. That feature is not supported by the connector. Please, do not modify headers in WriterInterceptor or MessageBodyWriter or use default HttpUrlConnector instead.
//     * Unsent header changes: {1}
//     *
//     */
//    public static String SOME_HEADERS_NOT_SENT(final Object arg0, final Object arg1) {
//        return localizer.localize(localizableSOME_HEADERS_NOT_SENT(arg0, arg1));
//    }

    public static String QUERY_PARAM_NULL() {
        return String.format("One or more of query value parameters are null.");
    }

    public static String ILLEGAL_PROVIDER_CLASS_NAME(final Object arg0) {
        return String.format("Illegal provider-class name: {0}.");
    }

    public static String STREAM_PROVIDER_NULL() {
        return String.format("Stream provider is not defined. It must be set before writing first bytes to the entity output stream.");
    }

    public static String INJECTION_MANAGER_STRATEGY_NOT_SUPPORTED(final Object arg0) {
        return String.format("InjectionManagerStrategy is not supported: {0}.");
    }

    public static String SSL_TMF_PROVIDER_NOT_REGISTERED() {
        return String.format("Error initializing trust manager factory (provider not registered).");
    }

    public static String NO_CONTAINER_AVAILABLE() {
        return String.format("No container available.");
    }

    public static String ERROR_ENTITY_PROVIDER_BASICTYPES_CONSTRUCTOR(final Object arg0) {
        return String.format("Error converting entity to {0} type by single String constructor.");
    }

    public static String ERROR_NOTFOUND_MESSAGEBODYWRITER(final Object arg0, final Object arg1, final Object arg2) {
        return String.format("MessageBodyWriter not found for media type={0}, type={1}, genericType={2}.");
    }

    public static String CONTRACT_NOT_ASSIGNABLE(final Object arg0, final Object arg1) {
        return String.format("Contract {0} can not be registered for component {1}: Contract not assignable to component.");
    }

    public static String SSL_TMF_ALGORITHM_NOT_SUPPORTED() {
        return String.format("Error initializing trust manager factory (algorithm not supported).");
    }

    public static String OSGI_REGISTRY_ERROR_OPENING_RESOURCE_STREAM(final Object arg0) {
        return String.format("Unable to open an input stream for resource {0}.");
    }

    public static String MBR_TRYING_TO_CLOSE_STREAM(final Object arg0) {
        return String.format("Message body reader ({0}) is trying to close the entity input stream. Not closing.");
    }

    public static String IGNORED_EXECUTOR_PROVIDERS(final Object arg0, final Object arg1) {
        return String.format("Multiple ExecutorServiceProvider registrations found for qualifier annotation [{1}]. The following provider registrations will be ignored: [{0}]");
    }

    public static String URI_PARSER_NOT_EXECUTED() {
        return String.format("The parser was not executed yet. Call the parse() method first.");
    }

    public static String MESSAGE_CONTENT_BUFFERING_FAILED() {
        return String.format("Failed to buffer the message content input stream.");
    }

    public static String RESPONSE_CLOSED() {
        return String.format("Response is closed.");
    }

    public static String SSL_KS_LOAD_ERROR(final Object arg0) {
        return String.format("Error loading key store from file \"{0}\".");
    }

    public static String COMMITTING_STREAM_ALREADY_INITIALIZED() {
        return String.format("Stream provider has already been initialized.");
    }

    public static String ERROR_ENTITY_PROVIDER_BASICTYPES_CHARACTER_MORECHARS() {
        return String.format("A single character expected in the entity input stream.");
    }

    public static String ERROR_ENTITY_STREAM_CLOSED() {
        return String.format("Entity input stream has already been closed.");
    }

    public static String MESSAGE_CONTENT_INPUT_STREAM_CLOSE_FAILED() {
        return String.format("Error closing message content input stream.");
    }

    public static String ERROR_PROVIDER_CONSTRAINED_TO_WRONG_PACKAGE(final Object arg0, final Object arg1) {
        return String.format("A registered provider {0} is constrained (via @ConstrainedTo) to {1} runtime but does not implement any provider interface usable in the runtime.");
    }

    public static String SSL_KS_PROVIDERS_NOT_REGISTERED() {
        return String.format("Error initializing key store (provider not registered).");
    }

    public static String PROPERTIES_HELPER_GET_VALUE_NO_TRANSFORM(final Object arg0, final Object arg1, final Object arg2) {
        return String.format("There is no way how to transform value \"{0}\" [{1}] to type [{2}].");
    }

    public static String ERROR_TEMPLATE_PARSER_INVALID_SYNTAX_TERMINATED(final Object arg0) {
        return String.format("Invalid syntax in the template \"{0}\". Check if a path parameter is terminated with a \"}\".");
    }

    public static String URI_BUILDER_URI_PART_FRAGMENT(final Object arg0, final Object arg1) {
        return String.format("Supplied scheme-specific URI part \"{0}\" contains URI Fragment component: {1}.");
    }

    public static String ERROR_MBW_ISWRITABLE(final Object arg0) {
        return String.format("MesssageBodyWriter {0} threw exception in isWritable - skipping.");
    }

    public static String ERROR_READING_ENTITY_MISSING() {
        return String.format("Missing entity.");
    }

    public static String INVALID_HOST() {
        return String.format("Invalid host name.");
    }

    public static String DEPENDENT_CLASS_OF_PROVIDER_FORMAT_ERROR(final Object arg0, final Object arg1, final Object arg2) {
        return String.format("{0}. A dependent class of the class {1} implementing the provider {2} is malformed. The provider implementation is ignored. Check if the malformed class is part of a stubbed jar that used for compiling only.");
    }

    public static String EXCEPTION_MAPPER_SUPPORTED_TYPE_UNKNOWN(final Object arg0) {
        return String.format("Unable to retrieve the supported exception type for a registered exception mapper service class \"{0}\".");
    }

    public static String INJECTION_MANAGER_NOT_PROVIDED() {
        return String.format("InjectionManager is not provided.");
    }

    public static String SSL_KMF_NO_PASSWORD_FOR_BYTE_BASED_KS() {
        return String.format("\"byte array based\"");
    }

    public static String SLOW_SUBSCRIBER(final Object arg0) {
        return String.format("Slow Subscriber. Subscription will be canceled. Item {0} and all the items sent after will not be received.");
    }

    public static String TYPE_TO_CLASS_CONVERSION_NOT_SUPPORTED(final Object arg0) {
        return String.format("Type-to-class conversion not supported for: {0}.");
    }

    public static String UNKNOWN_SUBSCRIBER() {
        return String.format("Unknown subscriber.");
    }

    public static String FEATURE_HAS_ALREADY_BEEN_PROCESSED(final Object arg0) {
        return String.format("Feature [{0}] has already been processed.");
    }

    public static String SSL_CTX_INIT_FAILED() {
        return String.format("Error initializing SSL context (operation failed).");
    }

    public static String ERROR_TEMPLATE_PARSER_INVALID_SYNTAX(final Object arg0, final Object arg1, final Object arg2) {
        return String.format("Invalid syntax for the expression \"{0}\" associated with the name \"{1}\" in a path template \"{2}\".");
    }

    public static String URI_BUILDER_SCHEME_PART_UNEXPECTED_COMPONENT(final Object arg0, final Object arg1) {
        return String.format("Supplied scheme-specific URI part \"{0}\" contains unexpected URI Scheme component: {1}.");
    }

    public static String SSL_TS_IMPL_NOT_FOUND() {
        return String.format("Error initializing trust store (implementation not available).");
    }

    public static String WARNING_MSG(final Object arg0) {
        return String.format("WARNING: {0}");
    }

    public static String WARNING_PROVIDER_CONSTRAINED_TO_WRONG_PACKAGE(final Object arg0, final Object arg1, final Object arg2, final Object arg3) {
        return String.format("A registered provider {0} constrained (via @ConstrainedTo) to {1} runtime implements interface {2} which is only usable in a {3} runtime context.");
    }

    public static String HINTS_DETECTED(final Object arg0) {
        return String.format("The following hints have been detected: {0}");
    }

    public static String HTTP_HEADER_UNBALANCED_COMMENTS() {
        return String.format("Unbalanced comments.");
    }

    public static String URI_BUILDER_METHODNAME_NOT_SPECIFIED(final Object arg0, final Object arg1) {
        return String.format("The method named \"{0}\" is not specified by {1}.");
    }

    public static String SSL_KMF_UNRECOVERABLE_KEY() {
        return String.format("Error initializing key manager factory (unrecoverable key).");
    }

    public static String INJECTION_ERROR_SUITABLE_CONSTRUCTOR_NOT_FOUND(final Object arg0) {
        return String.format("Could not find a suitable constructor in {0} class.");
    }

    public static String AUTODISCOVERABLE_CONFIGURATION_FAILED(final Object arg0) {
        return String.format("Configuration of {0} auto-discoverable failed.");
    }

    public static String URI_COMPONENT_INVALID_CHARACTER(final Object arg0, final Object arg1, final Object arg2, final Object arg3) {
        return String.format("The string \"{0}\" for the URI component {1} contains an invalid character, ''{2}'', at index {3}.");
    }

    public static String SSL_KS_FILE_NOT_FOUND(final Object arg0) {
        return String.format("Cannot find key store file \"{0}\".");
    }

    public static String EXCEPTION_CAUGHT_WHILE_LOADING_SPI_PROVIDERS() {
        return String.format("Exception caught while loading SPI providers.");
    }

    public static String ERROR_MSG(final Object arg0) {
        return String.format("WARNING: {0}");
    }

    public static String URI_IS_NULL() {
        return String.format("Uri is null.");
    }

    public static String OSGI_REGISTRY_ERROR_PROCESSING_RESOURCE_STREAM(final Object arg0) {
        return String.format("Unexpected error occurred while processing resource stream {0}.");
    }

    public static String PROVIDER_CLASS_COULD_NOT_BE_LOADED(final Object arg0, final Object arg1, final Object arg2) {
        return String.format("The class {0} implementing provider {1} could not be loaded: {2}");
    }

    public static String COMPONENT_TYPE_ALREADY_REGISTERED(final Object arg0) {
        return String.format("Cannot create new registration for component type {0}: Existing previous registration found for the type.");
    }

    public static String ERROR_ENTITY_PROVIDER_BASICTYPES_UNKWNOWN(final Object arg0) {
        return String.format("Unsupported basic type {0}.");
    }

    public static String STRING_IS_NULL() {
        return String.format("String is null.");
    }

    public static String DATE_IS_NULL() {
        return String.format("Date is null.");
    }

    public static String ERROR_RESOLVING_GENERIC_TYPE_VALUE(final Object arg0, final Object arg1) {
        return String.format("Unable to resolve generic type value of {0} for an instance of {1}.");
    }

    public static String ERROR_TEMPLATE_PARSER_NAME_MORE_THAN_ONCE(final Object arg0, final Object arg1) {
        return String.format("The name \"{0}\" is declared more than once with different regular expressions in a path template \"{1}\".");
    }

    public static String SSL_TS_INTEGRITY_ALGORITHM_NOT_FOUND() {
        return String.format("Error initializing trust store (algorithm to check trust store integrity not found).");
    }
}
