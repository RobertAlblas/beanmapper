package io.beanmapper.core;

import io.beanmapper.annotations.BeanIgnore;
import io.beanmapper.annotations.BeanProperty;
import io.beanmapper.annotations.BeanUnwrap;
import io.beanmapper.core.inspector.PropertyAccessor;
import io.beanmapper.core.inspector.PropertyAccessors;
import io.beanmapper.exceptions.BeanMissingPathException;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class BeanMatchStore {

    private Map<String, Map<String, BeanMatch>> store = new TreeMap<>();

    public BeanMatch getBeanMatch(Class<?> source, Class<?> target) {
        Map<String, BeanMatch> targetsForSource = getTargetsForSource(source);
        if (targetsForSource == null || !targetsForSource.containsKey(target.getCanonicalName())) {
            return addBeanMatch(determineBeanMatch(source, target));
        }
        return getTarget(targetsForSource, target);
    }

    public BeanMatch addBeanMatch(BeanMatch beanMatch) {
        Map<String, BeanMatch> targetsForSource = getTargetsForSource(beanMatch.getSource());
        if (targetsForSource == null) {
            targetsForSource = new TreeMap<>();
            storeTargetsForSource(beanMatch.getSource(), targetsForSource);
        }
        storeTarget(targetsForSource, beanMatch.getTarget(), beanMatch);
        return beanMatch;
    }

    private void storeTargetsForSource(Class<?> source, Map<String, BeanMatch> targetsForSource) {
        store.put(source.getCanonicalName(), targetsForSource);
    }

    private Map<String, BeanMatch> getTargetsForSource(Class<?> source) {
        return store.get(source.getCanonicalName());
    }

    private BeanMatch getTarget(Map<String, BeanMatch> targetsForSource, Class<?> target) {
        return targetsForSource.get(target.getCanonicalName());
    }

    private void storeTarget(Map<String, BeanMatch> targetsForSource, Class<?> target, BeanMatch beanMatch) {
        targetsForSource.put(target.getCanonicalName(), beanMatch);
    }

    private BeanMatch determineBeanMatch(Class<?> source, Class<?> target) {
        return determineBeanMatch(source, target, new TreeMap<>(), new TreeMap<>());
    }

    private BeanMatch determineBeanMatch(Class<?> source, Class<?> target,
                                         Map<String, BeanField> sourceNode, Map<String, BeanField> targetNode) {
        return new BeanMatch(
                source,
                target,
                getAllFields(sourceNode, targetNode, source, target, null),
                getAllFields(targetNode, sourceNode, target, source, null));
    }

    private Map<String, BeanField> getAllFields(Map<String, BeanField> ourNodes, Map<String, BeanField> otherNodes, Class<?> ourType, Class<?> otherType, BeanField prefixingBeanField) {
        // Get field from super class
        if (ourType.getSuperclass() != null) {
            getAllFields(ourNodes, otherNodes, ourType.getSuperclass(), otherType, null);
        }
        
        List<PropertyAccessor> accessors = PropertyAccessors.getAll(ourType);
        for (PropertyAccessor accessor : accessors) {

            // Ignore fields
            if (accessor.findAnnotation(BeanIgnore.class) != null) {
                continue;
            }

            // BeanProperty allows the field to match with a field from the other side with a different name
            // and even a different nesting level.
            String name = dealWithBeanProperty(otherNodes, otherType, accessor);

            // Unwrap the fields which exist in the unwrap class
            BeanField currentBeanField = null;
            try {
                currentBeanField = BeanField.determineNodesForPath(ourType, accessor.getName(), prefixingBeanField);
            } catch (NoSuchFieldException e) {
                throw new BeanMissingPathException(ourType, accessor.getName(), e);
            }
            
            if (accessor.findAnnotation(BeanUnwrap.class) != null) {
                ourNodes = getAllFields(ourNodes, otherNodes, accessor.getType(), otherType, currentBeanField);
            } else {
                ourNodes.put(name, currentBeanField);
            }
        }
        return ourNodes;
    }

    private String dealWithBeanProperty(Map<String, BeanField> otherNodes, Class<?> otherType, PropertyAccessor accessor) {
        String name = accessor.getName();
        if (accessor.findAnnotation(BeanProperty.class) != null) {
            name = accessor.findAnnotation(BeanProperty.class).name();
            // Get the other field from the location that is specified in the beanProperty annotation.
            // If the field is referred to by a path, store the custom field in the other map
            try {
                otherNodes.put(name, BeanField.determineNodesForPath(otherType, name, null));
            } catch (NoSuchFieldException err) {
                // Acceptable, might have been tagged as @BeanProperty as well
            }
        }
        return name;
    }

}
