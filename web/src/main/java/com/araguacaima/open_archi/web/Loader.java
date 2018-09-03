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
 * Unless required by applicable law or agreed to in writing, system
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import de.neuland.jade4j.template.ClasspathTemplateLoader;
import org.apache.commons.lang3.SystemUtils;

import java.io.File;
import java.io.IOException;
import java.io.Reader;

/**
 * Load templates from a given directory on the classpath.
 */
public class Loader extends ClasspathTemplateLoader {

    private String templateRoot;

    /**
     * Construct a classpath loader using the given template root.
     *
     * @param templateRoot the template root directory
     */
    public Loader(String templateRoot) {
        if (templateRoot.endsWith(File.separator)) {
            templateRoot = templateRoot.substring(0, templateRoot.length() - 1);
        }
        if (SystemUtils.IS_OS_WINDOWS) {
            templateRoot = templateRoot.replaceAll("/", File.separator + File.separator);
        } else {
            templateRoot = templateRoot.replaceAll("\\\\", File.separator);
        }
        this.templateRoot = templateRoot;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Reader getReader(String name) throws IOException {
        Reader reader = null;
        if (!name.startsWith("/") && !name.startsWith("\\")) {
            name = File.separator + name;
        }
        if (SystemUtils.IS_OS_WINDOWS) {
            name = name.replaceAll("/", File.separator + File.separator);
        } else {
            name = name.replaceAll("\\\\", File.separator);
        }
        String name1 = templateRoot + name.split("\\?")[0];
        try {
            reader = super.getReader(name1);
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return reader;
    }
}
