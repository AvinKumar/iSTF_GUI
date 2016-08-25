

package com.hp.test.framework.jelly;

import java.security.SecureRandom;

import org.apache.commons.jelly.JellyTagException;
import org.apache.commons.jelly.MissingAttributeException;
import org.apache.commons.jelly.TagSupport;
import org.apache.commons.jelly.XMLOutput;

/**
 * TODO: Class Description
 * @author sayedmo
 */
public class ChristenTag extends TagSupport {
    private SecureRandom secureRandom=new SecureRandom();
    private String[] names={"Yoda","Anakin","Luke","Lea","Chewbacca","Solo"};
    private String var;

    public String getVar() {
        return var;
    }
    public void setVar(String var) {
        this.var = var;
    }
    @Override
    public void doTag(XMLOutput arg0) throws MissingAttributeException, JellyTagException {
        String name=names[secureRandom.nextInt(names.length-1)];
        name=name+secureRandom.nextInt(9999);
        context.setVariable(var, name);
    }
}
