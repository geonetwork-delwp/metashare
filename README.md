# metartool

This is a tool for converting metadata in Metashare (an Oracle database) to 19115-3 XML metadata files. 

The tool uses hibernate3 to talk to the database and JiBX for the mapping between the fields and ISO19115 XML. The code is written in Java but uses Java 1.4 language features only due to the age of the hibernate and jibx implementations used. It is compiled and built as a shaded jar using maven and jdk8.

Maven requires a local-repo directory with the oracle ojdbc8 jars in it. These cannot be distributed publicly so please contact @sppigot for details.


