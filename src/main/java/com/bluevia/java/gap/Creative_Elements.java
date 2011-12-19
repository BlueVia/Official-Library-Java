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

import java.util.ArrayList;
import java.util.List;


public class Creative_Elements {
    private List<Creative_Element> creative_elements;

    
    public List<Creative_Element> getCreative_elements() {
    
        return creative_elements;
    }

    
    public void setCreative_elements(List<Creative_Element> creativeElements) {
    
        creative_elements = creativeElements;
    }
    
    public void setCreative_element(Creative_Element e){
        if (creative_elements==null){
            creative_elements=new ArrayList<Creative_Element>();
            
        }
        creative_elements.add(e);
    }
}
