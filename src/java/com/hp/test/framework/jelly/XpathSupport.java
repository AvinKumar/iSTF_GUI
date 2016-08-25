

package com.hp.test.framework.jelly;

import java.util.List;

import org.apache.commons.jelly.JellyContext;
import org.jaxen.JaxenException;
import org.jaxen.XPath;
import org.jaxen.dom4j.Dom4jXPath;

/**
 * XPath support that can be made available in the {@link JellyContext}
 * @author sayedmo
 */
public class XpathSupport {
    public Object selectSingle(Object obj,String xpathExpr) throws JaxenException {
        XPath xpath=getXPath(xpathExpr);
        Object result = xpath.selectSingleNode(obj);
        return result;
    }
    public List select(Object obj,String xpathExpr) throws JaxenException {
        XPath xpath=getXPath(xpathExpr);
        return xpath.selectNodes(obj);
    }
    
    private XPath getXPath(String expr) throws JaxenException {
        return new Dom4jXPath(expr);
    }
}
