
package com.araguacaima.gsa.msa.web;

import com.araguacaima.gsa.persistence.utils.JPAEntityManagerUtils;
import io.github.benas.randombeans.EnhancedRandomBuilder;
import io.github.benas.randombeans.api.EnhancedRandom;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;


public class Util {

    private static Set<Class> classes = new HashSet<>();

    public Util() {
    }

    static {
        classes.add(com.araguacaima.gsa.persistence.diagrams.architectural.Model.class);
        classes.add(com.araguacaima.gsa.persistence.diagrams.bpm.Model.class);
        classes.add(com.araguacaima.gsa.persistence.diagrams.er.Model.class);
        classes.add(com.araguacaima.gsa.persistence.diagrams.flowchart.Model.class);
        classes.add(com.araguacaima.gsa.persistence.diagrams.gantt.Model.class);
        classes.add(com.araguacaima.gsa.persistence.diagrams.sequence.Model.class);
        classes.add(com.araguacaima.gsa.persistence.diagrams.classes.Model.class);
    }

    public static void dbPopulation() {
        EnhancedRandom enhancedRandom = EnhancedRandomBuilder.aNewEnhancedRandomBuilder().build();
        for (Class<?> clazz : classes) {
            int next = new Random().nextInt((4 - 1) + 1) + 1;
            for (int i = 0; i < next; i++) {
                JPAEntityManagerUtils.persist(enhancedRandom.nextObject(clazz));
            }
        }
        JPAEntityManagerUtils.close();
    }
}
