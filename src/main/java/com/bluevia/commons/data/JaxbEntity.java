/**
 * \package com.bluevia.commons.data This package contains basic classes for common subtypes used by other entity data of the Bluevia APIs.
 */
package com.bluevia.commons.data;


import com.bluevia.commons.Entity;



/**
 * Class representing JAXB parse/serializer object
 * This class is used to parse/serialize objects in requests.
 *
 * This implementation is not synchronized
 *
 * @author Telefonica R&D
 * 
 *
 */

public class JaxbEntity implements Entity {

    /// @cond private
 
    private String jcInstance;
    private String namespace;
    private String qname;        
    private Object object;
    
    ///@endcond

    /**
     * Instantiates a new UserId type.
     */
    public JaxbEntity(){
        super();
    }

    
    /**
     * Instantiates a new JaxbEntity type.
     */
    public JaxbEntity(String jcInstance, String namespace, String qname, Object object) {
        super();
        this.jcInstance= jcInstance;
        this.namespace= namespace;
        this.qname= qname;        
        this.object= object;
    }



    /* (non-Javadoc)
     * @see com.bluevia.rest.Entity#isValid()
     */

    public String getJcInstance() {
		return jcInstance;
	}


	public void setJcInstance(String jcInstance) {
		this.jcInstance = jcInstance;
	}


	public String getNamespace() {
		return namespace;
	}


	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}


	public String getQname() {
		return qname;
	}


	public void setQname(String qname) {
		this.qname = qname;
	}


	public Object getObject() {
		return object;
	}


	public void setObject(Object object) {
		this.object = object;
	}


	public boolean isValid() {

        if (jcInstance == null)
            return false;
        else if (namespace == null)
            return false;
        else if (qname == null)
            return false;
        else if (object == null)
            return false;

        return true;
    }
    

}
