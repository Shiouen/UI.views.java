<?xml version="1.0" encoding="UTF-8" ?>

<xsl:stylesheet 
	version="2.0" 
	xmlns="http://schemas.microsoft.com/winfx/2006/xaml/presentation" 
	xmlns:x="http://schemas.microsoft.com/winfx/2006/xaml" 
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:output method="xml" indent="yes"/>

	<xsl:template match="/">
		<xsl:element name="Canvas">
			<xsl:attribute name="Width" select="500"/>
			<xsl:attribute name="Height" select="500"/>	
			<xsl:call-template name="getCanvasGradientBackground">
				<xsl:with-param name="startColor" select="'Steelblue'"/>
				<xsl:with-param name="endColor" select="'Darkslategray'"/>
				<xsl:with-param name="offset" select="1"/>
			</xsl:call-template>

			<xsl:call-template name="getTextBlock">
				<xsl:with-param name="text" select="'hihihi'"/>
				<xsl:with-param name="fontSize" select="25"/>
				<xsl:with-param name="foreground" select="'White'"/>
			</xsl:call-template>
		</xsl:element>
	</xsl:template>

	<xsl:template name="getCanvas">
		<xsl:param name="width"/>
		<xsl:param name="height"/>
		<xsl:element name="Canvas">
			<xsl:attribute name="Width" select="$width"/>
			<xsl:attribute name="Height" select="$height"/>
		</xsl:element>
	</xsl:template>

	<xsl:template name="getCanvasSolidBackground">
		<xsl:param name="color"/>

		<xsl:element name="Canvas.Background">
			<xsl:call-template name="getSolidColorBrush">
				<xsl:with-param name="color" select="$color"/>
			</xsl:call-template>
		</xsl:element>
	</xsl:template>

	<xsl:template name="getSolidColorBrush">
		<xsl:param name="color"/>

		<xsl:element name="SolidColorBrush">
			<xsl:attribute name="Color" select="$color"/>
		</xsl:element>
	</xsl:template>

	<xsl:template name="getCanvasGradientBackground">
		<xsl:param name="startColor"/>
		<xsl:param name="endColor"/>
		<xsl:param name="offset"/>

		<xsl:element name="Canvas.Background">
			<xsl:call-template name="getLinearGradientBrush">
				<xsl:with-param name="startColor" select="$startColor"/>
				<xsl:with-param name="endColor" select="$endColor"/>
				<xsl:with-param name="offset" select="$offset"/>
			</xsl:call-template>
		</xsl:element>
	</xsl:template>

	<xsl:template name="getLinearGradientBrush">
		<xsl:param name="startColor"/>
		<xsl:param name="endColor"/>
		<xsl:param name="offset"/>

		<xsl:element name="LinearGradientBrush">
			<xsl:call-template name="getGradientStop">
				<xsl:with-param name="color" select="$startColor"/>
				<xsl:with-param name="offset" select="0"/>
			</xsl:call-template>
			<xsl:call-template name="getGradientStop">
				<xsl:with-param name="color" select="$endColor"/>
				<xsl:with-param name="offset" select="$offset"/>
			</xsl:call-template>
		</xsl:element>
	</xsl:template>

	<xsl:template name="getGradientStop">
		<xsl:param name="color"/>
		<xsl:param name="offset"/>

		<xsl:element name="GradientStop">
			<xsl:attribute name="Color" select="$color"/>
			<xsl:attribute name="Offset" select="$offset"/>
		</xsl:element>
	</xsl:template>

	<xsl:template name="getTextBlock">
		<xsl:param name="text"/>
		<xsl:param name="fontSize"/>
		<xsl:param name="fontStyle"/>
		<xsl:param name="fontWeight"/>
		<xsl:param name="horAlign"/>
		<xsl:param name="vertAlign"/>
		<xsl:param name="foreground"/>

		<xsl:element name="TextBlock">
			<xsl:attribute name="FontSize" select="$fontSize"/>
			<xsl:attribute name="FontStyle" select="$fontStyle"/>
			<xsl:attribute name="FontWeight" select="$fontWeight"/>
			<xsl:attribute name="HorizontalAlignment" select="$horAlign"/>
			<xsl:attribute name="VerticalAlignment" select="$vertAlign"/>
			<xsl:attribute name="Foreground" select="$foreground"/>
			<xsl:value-of select="$text"/>
		</xsl:element>
	</xsl:template>
</xsl:stylesheet>
