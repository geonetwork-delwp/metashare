<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="2.0"
  xmlns:gml="http://www.opengis.net/gml/3.2"
  xmlns:srv="http://standards.iso.org/iso/19115/-3/srv/2.0"
  xmlns:gcx="http://standards.iso.org/iso/19115/-3/gcx/1.0"
  xmlns:gco="http://standards.iso.org/iso/19115/-3/gco/1.0"
  xmlns:mdb="http://standards.iso.org/iso/19115/-3/mdb/2.0"
  xmlns:mcc="http://standards.iso.org/iso/19115/-3/mcc/1.0"
  xmlns:mrc="http://standards.iso.org/iso/19115/-3/mrc/2.0"
  xmlns:lan="http://standards.iso.org/iso/19115/-3/lan/1.0"
  xmlns:cit="http://standards.iso.org/iso/19115/-3/cit/2.0"
  xmlns:dqm="http://standards.iso.org/iso/19157/-2/dqm/1.0"
  xmlns:gfc="http://standards.iso.org/iso/19110/gfc/1.1"
  xmlns:delwp="https://github.com/geonetwork-delwp/iso19115-3.2018"
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xmlns:gn-fn-iso19115-3="http://geonetwork-opensource.org/xsl/functions/profiles/iso19115-3"
  xmlns:xlink="http://www.w3.org/1999/xlink"
  xmlns:xs="http://www.w3.org/2001/XMLSchema"
  xmlns:gn="http://www.fao.org/geonetwork"
  exclude-result-prefixes="#all">

  <xsl:output method="html" indent="yes"/>

  <xsl:template match="/">
    <tr>
      <th>Object</th>
      <th>Type</th>
      <th>Field</th>
      <th>XPath</th>
    </tr>
    <xsl:apply-templates select="@*|node()"/>
  </xsl:template>

  <xsl:template match="value">
    <xsl:variable name="field" select="@constant|@get-method|@field"/> 
    <xsl:variable name="mappingname" select="if (ancestor::mapping/@abstract) then ''
                                             else concat(ancestor::mapping/@name,'/')"/>
    <xsl:variable name="constantOrField" select="if (@constant!='') then 'Constant'
                                                 else 'Field'"/>
    <xsl:message><xsl:value-of select="concat(ancestor::mapping/@class,' ',$constantOrField,': ',$field,' XPath: ',$mappingname,gn:getXPath(.))"/></xsl:message>
    <tr>
      <td><xsl:value-of select="ancestor::mapping/@class"/></td>
      <td><xsl:value-of select="$constantOrField"/></td>
      <td><xsl:value-of select="$field"/></td>
      <td><xsl:value-of select="concat($mappingname,gn:getXPath(.))"/></td>
    </tr>
  </xsl:template>

  <!-- match everything else as is -->

  <xsl:template match="@*|node()">
    <xsl:apply-templates select="@*|node()"/>
  </xsl:template>

  <xsl:function name="gn:getXPath" >
    <xsl:param name="target" as="element()"/>
    <xsl:variable name="xpath">
      <xsl:for-each select="$target/ancestor-or-self::*[name()='structure' or name()='value']">
        <xsl:if test="@name!=''">
          <xsl:variable name="ns" select="@ns"/>
          <xsl:variable name="name" select="if ($ns!='') then 
                          concat(/binding/namespace[@uri=$ns]/@prefix,':',@name)
                      else if (@style='attribute') then concat('@',@name)
                      else concat('mdb:',@name)"/>
          <xsl:value-of select="if (position() = last()) then $name 
                                else concat($name,' ')"/> 
        </xsl:if>
      </xsl:for-each>
    </xsl:variable>
    <xsl:value-of select="replace($xpath,' ','/')"/>
  </xsl:function>

</xsl:stylesheet>
