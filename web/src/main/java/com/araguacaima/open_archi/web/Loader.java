package com.araguacaima.open_archi.web;
/**
 * Copyright (c) 2015
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import de.neuland.jade4j.template.ClasspathTemplateLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.Reader;

/**
 * Load templates from a given directory on the classpath.
 */
public class Loader extends ClasspathTemplateLoader {

    private static Logger log = LoggerFactory.getLogger(Loader.class);
    private String templateRoot;

    /**
     * Construct a classpath loader using the given template root.
     *
     * @param templateRoot the template root directory
     */
    public Loader(String templateRoot) {
        log.error("templateRoot (before): " + templateRoot);
        if (!templateRoot.endsWith(File.separator)) {
            templateRoot += File.separator;
        }
        log.error("templateRoot (after): " + templateRoot);
        this.templateRoot = templateRoot;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Reader getReader(String name) throws IOException {
        log.error("name: " + name);
        Reader reader = null;
        String name1 = templateRoot + name;
        log.error("name1: " + name1);
        try {
            reader = super.getReader(name1);
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return reader;
    }
}
