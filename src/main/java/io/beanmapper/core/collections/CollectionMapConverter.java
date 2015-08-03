package io.beanmapper.core.collections;

import java.util.Map;
import java.util.TreeMap;

public class CollectionMapConverter extends AbstractCollectionConverter<Map> {

    protected Map createCollection() {
        return new TreeMap();
    }
}
