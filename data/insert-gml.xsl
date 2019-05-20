<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="2.0"
  xmlns:gml="http://www.opengis.net/gml/3.2"
  xmlns:gex="http://standards.iso.org/iso/19115/-3/gex/1.0"
  xmlns:gco="http://standards.iso.org/iso/19115/-3/gco/1.0"
  xmlns:mdb="http://standards.iso.org/iso/19115/-3/mdb/2.0"
  xmlns:mri="http://standards.iso.org/iso/19115/-3/mri/1.0"
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xmlns:xlink="http://www.w3.org/1999/xlink"
  xmlns:xs="http://www.w3.org/2001/XMLSchema"
  xmlns:oldgml="http://www.opengis.net/gml"
  xmlns:fme="http://www.safe.com/gml/fme"
  exclude-result-prefixes="fme xs oldgml">

  <xsl:param name="anzlicid"/>

  <xsl:output method="xml" indent="yes"/>

  <xsl:variable name="gmlDocument" select="document('geographic_extent_poly.gml')"/>

  <xsl:variable name="gml32">
    <xsl:apply-templates mode="togml32" select="$gmlDocument//oldgml:featureMember/fme:GEOGRAPHIC_EXTENT_POLYGON[fme:ANZLIC_ID=$anzlicid]/oldgml:*"/>
  </xsl:variable>

  <xsl:template mode="togml32" match="oldgml:*">
    <xsl:variable name="newgmlname" select="concat('gml:',local-name())"/> 
    <xsl:element name="{$newgmlname}">
      <xsl:apply-templates mode="togml32" select="@*|node()"/>
    </xsl:element>
  </xsl:template>

  <xsl:template mode="togml32" match="@oldgml:*">
    <xsl:variable name="newgmlname" select="concat('gml:',local-name())"/> 
    <xsl:variable name="content" select="."/>
    <xsl:attribute name="{$newgmlname}"><xsl:value-of select="$content"/></xsl:attribute>
  </xsl:template>

  <xsl:template match="mri:MD_DataIdentification">
    <!-- match and copy everything before mri:extent with geographicExtent, which must
         be replaced by the new gml32 content n the variable $gml32 --> 
    <xsl:copy>
      <xsl:copy-of select="@*"/>
      <xsl:apply-templates select="mri:citation"/>
      <xsl:apply-templates select="mri:abstract"/>
      <xsl:apply-templates select="mri:purpose"/>
      <xsl:apply-templates select="mri:credit"/>
      <xsl:apply-templates select="mri:status"/>
      <xsl:apply-templates select="mri:pointOfContact"/>
      <xsl:apply-templates select="mri:spatialRepresentationType"/>
      <xsl:apply-templates select="mri:spatialResolution"/>
      <xsl:apply-templates select="mri:temporalResolution"/>
      <xsl:apply-templates select="mri:topicCategory"/>

      <xsl:choose>
        <xsl:when test="count($gml32/*) > 0">
          <xsl:apply-templates select="mri:extent[count(descendant::gex:geographicElement) = 0]"/>
          <!-- Now copy in the new gml -->
          <xsl:call-template name="fill"/>
        </xsl:when>
        <xsl:otherwise>
          <xsl:apply-templates select="mri:extent"/>
        </xsl:otherwise>
      </xsl:choose>
          
      <xsl:apply-templates select="mri:additionalDocumentation"/>
      <xsl:apply-templates select="mri:processingLevel"/>
      <xsl:apply-templates select="mri:resourceMaintenance"/>

      <xsl:apply-templates select="mri:graphicOverview"/>

      <xsl:apply-templates select="mri:resourceFormat"/>
      <xsl:apply-templates select="mri:descriptiveKeywords"/>
      <xsl:apply-templates select="mri:resourceSpecificUsage"/>
      <xsl:apply-templates select="mri:resourceConstraints"/>
      <xsl:apply-templates select="mri:associatedResource"/>

      <xsl:apply-templates select="mri:defaultLocale"/>
      <xsl:apply-templates select="mri:otherLocale"/>
      <xsl:apply-templates select="mri:environmentDescription"/>
      <xsl:apply-templates select="mri:supplementalInformation"/>
    </xsl:copy>
  </xsl:template>

  <xsl:template name="fill">
      <!-- <xsl:message><xsl:copy-of select="$gml32"/></xsl:message> -->
      <mri:extent>
        <gex:EX_Extent>
          <gex:geographicElement>
            <gex:EX_BoundingPolygon>
    <xsl:for-each select="$gml32//gml:PolygonPatch">
              <gex:polygon>
                <gml:Polygon gml:id="{generate-id()}">
                  <xsl:copy-of select="./*"/>
                </gml:Polygon>
              </gex:polygon>
    </xsl:for-each>
            </gex:EX_BoundingPolygon>
          </gex:geographicElement>
        </gex:EX_Extent>
      </mri:extent>
  </xsl:template>

  <!-- copy everything else as is -->

  <xsl:template match="@*|node()">
    <xsl:copy>
      <xsl:apply-templates select="@*|node()"/>
    </xsl:copy>
  </xsl:template>

</xsl:stylesheet>
