package io.beanmapper.config;

import java.util.ArrayList;
import java.util.List;

import io.beanmapper.BeanMapper;
import io.beanmapper.annotations.BeanCollectionUsage;
import io.beanmapper.annotations.LogicSecuredCheck;
import io.beanmapper.core.collections.CollectionHandler;
import io.beanmapper.core.collections.ListCollectionHandler;
import io.beanmapper.core.collections.MapCollectionHandler;
import io.beanmapper.core.collections.SetCollectionHandler;
import io.beanmapper.core.constructor.BeanInitializer;
import io.beanmapper.core.converter.BeanConverter;
import io.beanmapper.core.converter.collections.CollectionConverter;
import io.beanmapper.core.converter.impl.AnyToEnumConverter;
import io.beanmapper.core.converter.impl.NumberToNumberConverter;
import io.beanmapper.core.converter.impl.ObjectToStringConverter;
import io.beanmapper.core.converter.impl.PrimitiveConverter;
import io.beanmapper.core.converter.impl.StringToBigDecimalConverter;
import io.beanmapper.core.converter.impl.StringToBooleanConverter;
import io.beanmapper.core.converter.impl.StringToIntegerConverter;
import io.beanmapper.core.converter.impl.StringToLongConverter;
import io.beanmapper.core.unproxy.BeanUnproxy;

public class BeanMapperBuilder {

    private static final List<CollectionHandler> DEFAULT_COLLECTION_HANDLERS =
            new ArrayList<CollectionHandler>() {{
        add(new MapCollectionHandler());
        add(new SetCollectionHandler());
        add(new ListCollectionHandler());
    }};

    private final Configuration configuration;

    private List<BeanConverter> customBeanConverters = new ArrayList<BeanConverter>();

    private List<CollectionHandler> customCollectionHandlers = new ArrayList<>();

    public BeanMapperBuilder() {
        this.configuration = new CoreConfiguration();
    }

    public BeanMapperBuilder(Configuration configuration) {
        this.configuration = new OverrideConfiguration(configuration);
    }

    public BeanMapperBuilder withoutDefaultConverters() {
        this.configuration.withoutDefaultConverters();
        return this;
    }

    public BeanMapperBuilder addConverter(BeanConverter converter) {
        this.customBeanConverters.add(converter);
        return this;
    }

    public BeanMapperBuilder addCollectionHandler(CollectionHandler handler) {
        this.customCollectionHandlers.add(handler);
        return this;
    }

    public BeanMapperBuilder addBeanPairWithStrictSource(Class<?> sourceClass, Class<?> targetClass) {
        this.configuration.addBeanPairWithStrictSource(sourceClass, targetClass);
        return this;
    }

    public BeanMapperBuilder addBeanPairWithStrictTarget(Class<?> sourceClass, Class<?> targetClass) {
        this.configuration.addBeanPairWithStrictTarget(sourceClass, targetClass);
        return this;
    }

    public BeanMapperBuilder addProxySkipClass(Class<?> clazz) {
        this.configuration.addProxySkipClass(clazz);
        return this;
    }

    public BeanMapperBuilder addPackagePrefix(Class<?> clazz) {
        this.configuration.addPackagePrefix(clazz);
        return this;
    }

    public BeanMapperBuilder addPackagePrefix(String packagePrefix) {
        this.configuration.addPackagePrefix(packagePrefix);
        return this;
    }

    public BeanMapperBuilder addAfterClearFlusher(AfterClearFlusher afterClearFlusher) {
        this.configuration.addAfterClearFlusher(afterClearFlusher);
        return this;
    }

    public BeanMapperBuilder addLogicSecuredCheck(LogicSecuredCheck logicSecuredCheck) {
        this.configuration.addLogicSecuredCheck(logicSecuredCheck);
        return this;
    }

    public BeanMapperBuilder setSecuredPropertyHandler(RoleSecuredCheck roleSecuredCheck) {
        this.configuration.setRoleSecuredCheck(roleSecuredCheck);
        return this;
    }

    public BeanMapperBuilder setEnforcedSecuredProperties(Boolean enforcedSecuredProperties) {
        this.configuration.setEnforceSecuredProperties(enforcedSecuredProperties);
        return this;
    }

    public BeanMapperBuilder setBeanInitializer(BeanInitializer beanInitializer) {
        this.configuration.setBeanInitializer(beanInitializer);
        return this;
    }

    public BeanMapperBuilder setBeanUnproxy(BeanUnproxy beanUnproxy) {
        this.configuration.setBeanUnproxy(beanUnproxy);
        return this;
    }

    public BeanMapperBuilder downsizeSource(List<String> includeFields) {
        this.configuration.setApplyStrictMappingConvention(false);
        this.configuration.downsizeSource(includeFields);
        return this;
    }

    public BeanMapperBuilder downsizeTarget(List<String> includeFields) {
        this.configuration.setApplyStrictMappingConvention(false);
        this.configuration.downsizeTarget(includeFields);
        return this;
    }

    public BeanMapperBuilder setCollectionClass(Class collectionClass) {
        this.configuration.setCollectionClass(collectionClass);
        return this;
    }

    public BeanMapperBuilder setTargetClass(Class targetClass) {
        this.configuration.setTargetClass(targetClass);
        return this;
    }

    public BeanMapperBuilder setTarget(Object target) {
        this.configuration.setTarget(target);
        return this;
    }

    public BeanMapperBuilder setParent(Object parent) {
        this.configuration.setParent(parent);
        return this;
    }

    public BeanMapperBuilder setConverterChoosable(boolean converterChoosable) {
        this.configuration.setConverterChoosable(converterChoosable);
        return this;
    }

    public BeanMapperBuilder setStrictSourceSuffix(String strictSourceSuffix) {
        this.configuration.setStrictSourceSuffix(strictSourceSuffix);
        return this;
    }
    
    public BeanMapperBuilder setStrictTargetSuffix(String strictTargetSuffix) {
        this.configuration.setStrictTargetSuffix(strictTargetSuffix);
        return this;
    }
    
    public BeanMapperBuilder setApplyStrictMappingConvention(Boolean applyStrictMappingConvention) {
        this.configuration.setApplyStrictMappingConvention(applyStrictMappingConvention);
        return this;
    }

    public BeanMapperBuilder setCollectionUsage(BeanCollectionUsage collectionUsage) {
        this.configuration.setCollectionUsage(collectionUsage);
        return this;
    }

    public BeanMapperBuilder setPreferredCollectionClass(Class<?> preferredCollectionClass) {
        this.configuration.setPreferredCollectionClass(preferredCollectionClass);
        return this;
    }

    public BeanMapperBuilder setFlushAfterClear(boolean flushAfterClear) {
        this.configuration.setFlushAfterClear(flushAfterClear);
        return this;
    }

    public BeanMapperBuilder setFlushEnabled(boolean flushEnabled) {
        this.configuration.setFlushEnabled(flushEnabled);
        return this;
    }


    public BeanMapperBuilder setUseNullValue() {
        this.configuration.setUseNullValue(true);
        return this;
    }

    public BeanMapperBuilder setUseNullValue(boolean useNullValue) {
        this.configuration.setUseNullValue(useNullValue);
        return this;
    }

    public BeanMapperBuilder setUseCollectionNullValue(boolean useCollectionNullValue) {
        this.configuration.setUseCollectionNullValue(useCollectionNullValue);
        return this;
    }

    public BeanMapper build() {
        BeanMapper beanMapper = new BeanMapper(configuration);
        // Custom collection handlers must be registered before default ones
        addCollectionHandlers(customCollectionHandlers);
        addCollectionHandlers(DEFAULT_COLLECTION_HANDLERS);
        // Custom bean converters must be registered before default ones
        addCustomConverters();
        if (configuration.isAddDefaultConverters()) {
            addDefaultConverters();
        }
        // Make sure all strict bean classes have matching properties on the other side
        configuration.getBeanMatchStore().validateStrictBeanPairs(configuration.getBeanPairs());
        return beanMapper;
    }

    private void addCollectionHandlers(List<CollectionHandler> collectionHandlers) {
        for (CollectionHandler collectionHandler : collectionHandlers) {
            attachCollectionHandler(collectionHandler);
        }
    }

    private void addCustomConverters() {
        for (BeanConverter customBeanConverter : customBeanConverters) {
            attachConverter(customBeanConverter);
        }
    }

    private void addDefaultConverters() {
        attachConverter(new PrimitiveConverter());
        attachConverter(new StringToBooleanConverter());
        attachConverter(new StringToIntegerConverter());
        attachConverter(new StringToLongConverter());
        attachConverter(new StringToBigDecimalConverter());
        attachConverter(new AnyToEnumConverter());
        attachConverter(new NumberToNumberConverter());
        attachConverter(new ObjectToStringConverter());

        for (CollectionHandler collectionHandler : configuration.getCollectionHandlers()) {
            attachConverter(new CollectionConverter(collectionHandler));
        }
    }

    private void attachCollectionHandler(CollectionHandler collectionHandler) {
        configuration.addCollectionHandler(collectionHandler);
    }

    private void attachConverter(BeanConverter customBeanConverter) {
        configuration.addConverter(customBeanConverter);
    }

}
