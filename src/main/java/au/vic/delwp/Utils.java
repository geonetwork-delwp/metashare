package au.gov.vic.delwp;

import org.apache.commons.lang3.StringUtils;

public class Utils {

   public static boolean isBlank(String value) {
     if (StringUtils.isBlank(value) || 
         value.trim().equalsIgnoreCase("unknown") || 
         value.trim().equalsIgnoreCase("notentered") ||
         value.trim().equalsIgnoreCase("null") ||
         value.trim().equalsIgnoreCase("na") ||
         value.trim().equalsIgnoreCase("not documented") ||
         value.trim().equalsIgnoreCase("none") ||
         value.trim().equalsIgnoreCase("n/a")) {
       return true;
     } else {
       return false;
     }
   }
}
