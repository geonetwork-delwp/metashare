package au.gov.vic.delwp;

import java.util.HashMap;
import java.util.Collection;


class ANZLICSearchWord extends IDnText {

	static protected HashMap TopicCategories = new HashMap( );

	static {
		MapUtils.PopulateMulti( "anzlic-iso.txt", TopicCategories );
		}
	
	public Collection getISOTopicCategories( ){
		return (Collection) TopicCategories.get( super.Text.trim() );
		}
	}
	
