package com.araguacaima.gsa.msa.web.wrapper;

import cz.jirutka.rsql.parser.RSQLParser;
import cz.jirutka.rsql.parser.ast.Node;

import java.io.IOException;

public abstract class RsqlJsonFilter {

    public static Object rsql(final String query, final String json)
            throws IOException {
        Node rootNode = new RSQLParser().parse(query);
        return rootNode.accept(new JsonPathRsqlVisitor(json));
    }
}