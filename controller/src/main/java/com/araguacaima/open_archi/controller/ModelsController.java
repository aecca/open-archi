package com.araguacaima.open_archi.controller;

import com.araguacaima.open_archi.persistence.diagrams.architectural.*;
import com.araguacaima.open_archi.persistence.diagrams.architectural.System;
import com.araguacaima.open_archi.persistence.diagrams.core.Item;
import com.araguacaima.orpheusdb.utils.OrpheusDbJPAEntityManagerUtils;
import org.apache.commons.collections4.CollectionUtils;

import java.util.*;

public class ModelsController {
    public static Collection findUsagesOf(String modelName) {
        Collection<Item> result = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        params.put("name", modelName);
        List<Item> items = OrpheusDbJPAEntityManagerUtils.executeQuery(Item.class, Item.GET_ITEMS_BY_NAME, params);
        final Collection<String> elementsList = extractIds(items);
        result.addAll(findElementsRecursivelly(elementsList));
        return result;
    }


    private static Collection<? extends Item> findElementsRecursivelly(Collection<String> elementsList) {
        Collection temp = findElements(elementsList);
        if (CollectionUtils.isNotEmpty(temp)) {
            Collection<String> filteredElementsList = extractIds(temp);
            Collection<String> subtractedIds = CollectionUtils.subtract(filteredElementsList, elementsList);
            Collection<? extends Item> elementsRecursivelly = findElementsRecursivelly(subtractedIds);
            temp.addAll(elementsRecursivelly);
        }
        return temp;
    }

    private static Collection<? extends Item> findElements(Collection<String> elementsList) {
        Map<String, Object> params = new HashMap<>();
        Collection<Item> result = new ArrayList<>();
        params.clear();
        params.put(Item.ELEMENTS_USAGE_PARAM, elementsList);

        //find element directly on any model
        Collection<? extends Item> elementsInModels = OrpheusDbJPAEntityManagerUtils.executeQuery(Item.class, Model.GET_MODELS_USAGE_BY_ELEMENT_ID_LIST, params);
        result.addAll(elementsInModels);

        //find element directly on any layer
        Collection<? extends Item> elementsInLayers = OrpheusDbJPAEntityManagerUtils.executeQuery(Item.class, Layer.GET_LAYERS_USAGE_BY_ELEMENT_ID_LIST, params);
        result.addAll(elementsInLayers);

        //find element directly on any system
        Collection<? extends Item> elementsInSystems = OrpheusDbJPAEntityManagerUtils.executeQuery(Item.class, System.GET_SYSTEMS_USAGE_BY_ELEMENT_ID_LIST, params);
        result.addAll(elementsInSystems);

        //find element directly on any container
        Collection<? extends Item> elementsInContainers = OrpheusDbJPAEntityManagerUtils.executeQuery(Item.class, Container.GET_CONTAINERS_USAGE_BY_ELEMENT_ID_LIST, params);
        result.addAll(elementsInContainers);

        //find element directly on any component
        Collection<? extends Item> elementsInComponents = OrpheusDbJPAEntityManagerUtils.executeQuery(Item.class, Component.GET_COMPONENTS_USAGE_BY_ELEMENT_ID_LIST, params);
        result.addAll(elementsInComponents);

        return result;
    }

    private static Collection<String> extractIds(Collection<? extends Item> items) {
        final List<String> elementsList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(items)) {
            items.forEach(model -> {
                elementsList.add(model.getId());
            });
        }
        return elementsList;
    }
}
