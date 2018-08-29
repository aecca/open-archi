package com.araguacaima.open_archi.web;

import org.pac4j.core.context.HttpConstants;
import org.pac4j.sparkjava.DefaultHttpActionAdapter;
import org.pac4j.sparkjava.SparkWebContext;
import spark.ModelAndView;
import spark.TemplateEngine;

import java.util.HashMap;
import java.util.Map;

public class HttpActionAdapter extends DefaultHttpActionAdapter {

    private final TemplateEngine templateEngine;

    public HttpActionAdapter(final TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    @Override
    public Object adapt(int code, SparkWebContext context) {
        Map map = new HashMap();
        if (code == HttpConstants.UNAUTHORIZED) {
            map.put("title", HttpConstants.UNAUTHORIZED);
            map.put("message", "UNAUTHORIZED");
            stop(401, templateEngine.render(new ModelAndView(map, "error")));
        } else if (code == HttpConstants.FORBIDDEN) {
            map.put("title", HttpConstants.FORBIDDEN);
            map.put("message", "FORBIDDEN");
            stop(403, templateEngine.render(new ModelAndView(map, "error")));
        } else {
            return super.adapt(code, context);
        }
        return null;
    }
}
