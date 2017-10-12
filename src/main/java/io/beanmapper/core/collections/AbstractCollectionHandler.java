package io.beanmapper.core.collections;

import io.beanmapper.BeanMapper;
import io.beanmapper.annotations.BeanCollectionUsage;
import io.beanmapper.core.constructor.DefaultBeanInitializer;
import io.beanmapper.exceptions.BeanCollectionUnassignableTargetCollectionTypeException;
import io.beanmapper.utils.Classes;

public abstract class AbstractCollectionHandler<C> implements CollectionHandler<C> {

    private final Class<C> type;
    private final DefaultBeanInitializer beanInitializer = new DefaultBeanInitializer();

    public AbstractCollectionHandler() {
        this.type = (Class<C>)Classes.getParameteredTypes(getClass())[0];
    }

    /**
     * Calls the clear method on the target collection
     * @param target the collection to call clear() on
     */
    abstract protected void clear(C target);

    /**
     * Creates a new instance of the collection class
     * @return
     */
    abstract protected C create();

    public Object mapItem(
            BeanMapper beanMapper,
            Class collectionElementClass,
            Object source) {

        return beanMapper
                .wrapConfig()
                .setTargetClass(collectionElementClass)
                .setConverterChoosable(true)
                .build()
                .map(source);
    }

    @Override
    public C getTargetCollection(
            BeanCollectionUsage collectionUsage,
            Class<C> preferredCollectionClass,
            C targetCollection) {

        C useTargetCollection = collectionUsage.mustConstruct(targetCollection) ?
                createCollection(preferredCollectionClass) :
                targetCollection;

        if (collectionUsage.mustClear()) {
            clear(useTargetCollection);
        }

        return useTargetCollection;
    }

    @Override
    public Class<C> getType() {
        return type;
    }

    private C createCollection(Class<C> preferredCollectionClass) {
        if (preferredCollectionClass == null) {
            return create();
        } else if (!type.isAssignableFrom(preferredCollectionClass)) {
            throw new BeanCollectionUnassignableTargetCollectionTypeException(type, preferredCollectionClass);
        }
        return beanInitializer.instantiate(preferredCollectionClass, null);
    }

    @Override
    public boolean isMatch(Class<?> clazz) {
        return getType().isAssignableFrom(clazz);
    }

}