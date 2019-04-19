# metartool

This is a tool for converting metadata in Metashare (an Oracle database) to 19115 XML metadata files. Currently being updated to support -3 and the latest Metashare.

The tool uses hibernate3 to talk to the database and JiBX for the mapping between the fields and ISO19115 XML. The code is written in Java. It is compiled and built as a shaded jar using maven and jdk8.
