/**
 * 
 * @category bluevia
 * @package com.bluevia.schemas.unica.rest.common.v1
 * @copyright Copyright (c) 2010 Telefonica I+D (http://www.tid.es)
 * @author Bluevia <support@bluevia.com>
 * 
 * 
 *          BlueVia is a global iniciative of Telefonica delivered by Movistar
 *          and O2. Please, check out http://www.bluevia.com and if you need
 *          more information contact us at support@bluevia.com
 */

package com.bluevia.java.gap;


public class Creative_Element {
    
    private String type_id;
    private String type_name;
    private String value;
    private String interaction;
    
    public String getType_id() {
    
        return type_id;
    }
    
    public void setType_id(String typeId) {
    
        type_id = typeId;
    }
    
    public String getType_name() {
    
        return type_name;
    }
    
    public void setType_name(String typeName) {
    
        type_name = typeName;
    }
    
    public String getValue() {
    
        return value;
    }
    
    public void setValue(String value) {
    
        this.value = value;
    }
    
    public String getInteraction() {
    
        return interaction;
    }
    
    public void setInteraction(String interaction) {
    
        this.interaction = interaction;
    }

    @Override
    public String toString() {

        return "Creative_Element [interaction=" + interaction + ", type_id=" + type_id + ", type_name=" + type_name + ", value=" + value + "]";
    }
    
   
    
    


}
