<?xml version="1.0" encoding="UTF-8"?>

<xsl:stylesheet
    version="2.0"
    xmlns="http://schemas.microsoft.com/winfx/2006/xaml/presentation"
    xmlns:fn="http://www.w3.org/2005/xpath-functions"
    xmlns:x="http://schemas.microsoft.com/winfx/2006/xaml"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    <xsl:output method="xml" indent="yes"/>
    
    <xsl:variable name="Students" select="fn:distinct-values(//Result/@Student)"/>
    <xsl:variable name="Courses" select="//Course/@CourseName"/>
    <xsl:variable name="StudentAmount" select="count($Students)"/>
    <xsl:variable name="CourseAmount" select="count($Courses)"/>
    
    <xsl:variable name="RowHeight" select="$StudentAmount"/>
    <xsl:variable name="ColumnWidth" select="concat(20, '*')"/>

    <xsl:template match="/">
        <xsl:element name="Grid">
            <xsl:attribute name="ShowGridLines" select="'False'"/>

            <xsl:call-template name="setRowDefs">
                <xsl:with-param name="H0" select="'36'"/>
                <xsl:with-param name="H1" select="'8*'"/>
                <xsl:with-param name="H2" select="'36'"/>
            </xsl:call-template>
            <xsl:call-template name="setColumnDefs">
                <xsl:with-param name="W0" select="'0.1*'"/>
                <xsl:with-param name="W1" select="'8*'"/>
                <xsl:with-param name="W2" select="'0.1*'"/>
            </xsl:call-template>

            <xsl:call-template name="getGradientBackground">
                <xsl:with-param name="Color1" select="'Purple'"/>
                <xsl:with-param name="Color2" select="'DarkCyan'"/>
                <xsl:with-param name="ParentElement" select="'Grid'"/>
            </xsl:call-template>

            <xsl:call-template name="getTextBlock">
                <xsl:with-param name="Text" select="//@Class"/>
                <xsl:with-param name="FontSize" select="'32'"/>
                <xsl:with-param name="FontWeight" select="'DemiBold'"/>
                <xsl:with-param name="Foreground" select="'White'"/>
                <xsl:with-param name="HorAlign" select="'Center'"/>
                <xsl:with-param name="VertAlign" select="'Center'"/>
                <xsl:with-param name="GridRow" select="'0'"/>
                <xsl:with-param name="GridColumn" select="'1'"/>
            </xsl:call-template>
            
            <xsl:element name="ScrollViewer">
                <xsl:attribute name="Grid.Row" select="'1'"/>
                <xsl:attribute name="Grid.Column" select="'1'"/>

                <xsl:element name="Grid">
                    <xsl:attribute name="ShowGridLines" select="'False'"/>

                    <xsl:call-template name="genRowDefs">
                        <xsl:with-param name="Amount" select="$Students"/>
                        <xsl:with-param name="Height" select="$RowHeight"/>
                    </xsl:call-template>
                    <xsl:call-template name="genColumnDefs">
                        <xsl:with-param name="Amount" select="$Courses"/>
                        <xsl:with-param name="Width" select="$ColumnWidth"/>
                    </xsl:call-template>

                    <xsl:call-template name="fillStudents"/>
                    <xsl:call-template name="fillCourses"/>
                    <xsl:call-template name="fillScores"/>
                </xsl:element>
            </xsl:element>

            <xsl:call-template name="fillStatistics"/>
        </xsl:element>
    </xsl:template>

    <!--############-->
    <!-- BACKGROUND -->
    <!--############-->
    <xsl:template name="getGradientBackground">
        <xsl:param name="Color1" />
        <xsl:param name="Color2" />
        <xsl:param name="ParentElement"/>

        <xsl:element name="{concat($ParentElement, '.Background')}">
            <xsl:element name="LinearGradientBrush">
                <xsl:element name="GradientStop">
                    <xsl:attribute name="Color" select="$Color1"/>
                </xsl:element>
                <xsl:element name="GradientStop">
                    <xsl:attribute name="Color" select="$Color2"/>
                    <xsl:attribute name="Offset">1</xsl:attribute>
                </xsl:element>
            </xsl:element>
        </xsl:element>
    </xsl:template>

    <xsl:template name="getSolidBackground">
        <xsl:param name="Color" />
        <xsl:param name="ParentElement"/>

        <xsl:element name="{concat($ParentElement, '.Background')}">
            <xsl:element name="SolidColorBrush">
                <xsl:attribute name="Color" select="$Color"/>
            </xsl:element>
        </xsl:element>
    </xsl:template>
    <!--############-->
    <!-- BACKGROUND -->
    <!--############-->

    <!--########-->
    <!-- BORDER -->
    <!--########-->
    <xsl:template name="getBorderedTextBlock">
        <xsl:param name="Background"/>
        <xsl:param name="BorderThickness" />
        <xsl:param name="FontSize"/>
        <xsl:param name="FontWeight"/>
        <xsl:param name="Foreground"/>
        <xsl:param name="GridColumn"/>
        <xsl:param name="GridRow"/>
        <xsl:param name="HorAlign"/>
        <xsl:param name="Margin"/>
        <xsl:param name="Opacity"/>
        <xsl:param name="Text"/>
        <xsl:param name="TextAlignment"/>
        <xsl:param name="VertAlign"/>

        <xsl:element name="Border">
            <xsl:attribute name="Background" select="$Background"/>
            <xsl:attribute name="BorderThickness" select="$BorderThickness"/>
            <xsl:attribute name="Grid.Column" select="$GridColumn"/>
            <xsl:attribute name="Grid.Row" select="$GridRow"/>
            <xsl:attribute name="Margin" select="$Margin"/>
            <xsl:attribute name="Opacity" select="$Opacity"/>

            <xsl:element name="TextBlock">
                <xsl:attribute name="FontSize" select="$FontSize"/>
                <xsl:attribute name="FontWeight" select="$FontWeight"/>
                <xsl:attribute name="Foreground" select="$Foreground"/>
                <xsl:attribute name="HorizontalAlignment" select="$HorAlign"/>
                <xsl:attribute name="TextAlignment" select="$TextAlignment"/>
                <xsl:attribute name="VerticalAlignment" select="$VertAlign"/>
                <xsl:value-of select="$Text"/>
            </xsl:element>
        </xsl:element>
    </xsl:template>
    <!--########-->
    <!-- BORDER -->
    <!--########-->

    <!--######-->
    <!-- GRID -->
    <!--######-->
    <xsl:template name="setRowDefs">
        <xsl:param name="H0"/>
        <xsl:param name="H1"/>
        <xsl:param name="H2"/>

        <xsl:element name="Grid.RowDefinitions">
            <xsl:call-template name="setRowSize">
                <xsl:with-param name="Height" select="$H0"/>
            </xsl:call-template>
            <xsl:call-template name="setRowSize">
                <xsl:with-param name="Height" select="$H1"/>
            </xsl:call-template>
            <xsl:call-template name="setRowSize">
                <xsl:with-param name="Height" select="$H2"/>
            </xsl:call-template>
        </xsl:element>
    </xsl:template>

    <xsl:template name="setRowSize">
        <xsl:param name="Height"/>

        <xsl:element name="RowDefinition">
            <xsl:attribute name="Height" select="$Height"/>
        </xsl:element>
    </xsl:template>

    <xsl:template name="setColumnDefs">
        <xsl:param name="W0"/>
        <xsl:param name="W1"/>
        <xsl:param name="W2"/>

        <xsl:element name="Grid.ColumnDefinitions">
            <xsl:call-template name="setColumnSize">
                <xsl:with-param name="Width" select="$W0"/>
            </xsl:call-template>
            <xsl:call-template name="setColumnSize">
                <xsl:with-param name="Width" select="$W1"/>
            </xsl:call-template>
            <xsl:call-template name="setColumnSize">
                <xsl:with-param name="Width" select="$W2"/>
            </xsl:call-template>
        </xsl:element>
    </xsl:template>

    <xsl:template name="setColumnSize">
        <xsl:param name="Width"/>

        <xsl:element name="ColumnDefinition">
            <xsl:attribute name="Width" select="$Width"/>
        </xsl:element>
    </xsl:template>

    <xsl:template name="genRowDefs">
        <xsl:param name="Amount"/>
        <xsl:param name="Height"/>

        <xsl:element name="Grid.RowDefinitions">
            <xsl:for-each select="$Amount">
                <xsl:call-template name="setRowSize">
                    <xsl:with-param name="Height" select="$Height"/>
                </xsl:call-template>
            </xsl:for-each>
            <xsl:call-template name="setRowSize">
                <xsl:with-param name="Height" select="$Height"/>
            </xsl:call-template>
        </xsl:element>
    </xsl:template>

    <xsl:template name="genColumnDefs">
        <xsl:param name="Amount"/>
        <xsl:param name="Width"/>

        <xsl:element name="Grid.ColumnDefinitions">
            <xsl:for-each select="$Amount">
                <xsl:call-template name="setColumnSize">
                    <xsl:with-param name="Width" select="$Width"/>
                </xsl:call-template>
            </xsl:for-each>
            <xsl:call-template name="setColumnSize">
                <xsl:with-param name="Width" select="$Width"/>
            </xsl:call-template>
        </xsl:element>
    </xsl:template>
    <!--######-->
    <!-- GRID -->
    <!--######-->

    <!--###########-->
    <!-- TEXTBLOCK -->
    <!--###########-->
    <xsl:template name="getStatisticTextBlock">
        <xsl:param name="Text"/>
        <xsl:param name="FontSize"/>
        <xsl:param name="FontWeight"/>
        <xsl:param name="Foreground"/>
        <xsl:param name="HorAlign"/>
        <xsl:param name="VertAlign"/>
        <xsl:param name="TextAlign"/>

        <xsl:element name="TextBlock">
            <xsl:attribute name="FontSize" select="$FontSize"/>
            <xsl:attribute name="FontWeight" select="$FontWeight"/>
            <xsl:attribute name="Foreground" select="$Foreground"/>
            <xsl:attribute name="HorizontalAlignment" select="$HorAlign"/>
            <xsl:attribute name="VerticalAlignment" select="$VertAlign"/>
            <xsl:attribute name="TextAlignment" select="$TextAlign"/>
            <xsl:value-of select="$Text"/>
        </xsl:element>
    </xsl:template>

    <xsl:template name="getTextBlock">
        <xsl:param name="Text"/>
        <xsl:param name="FontSize"/>
        <xsl:param name="FontWeight"/>
        <xsl:param name="Foreground"/>
        <xsl:param name="HorAlign"/>
        <xsl:param name="VertAlign"/>
        <xsl:param name="GridRow"/>
        <xsl:param name="GridColumn"/>
        
        <xsl:element name="TextBlock">
            <xsl:attribute name="FontSize" select="$FontSize"/>
            <xsl:attribute name="FontWeight" select="$FontWeight"/>
            <xsl:attribute name="Foreground" select="$Foreground"/>
            <xsl:attribute name="HorizontalAlignment" select="$HorAlign"/>
            <xsl:attribute name="VerticalAlignment" select="$VertAlign"/>
            <xsl:attribute name="Grid.Row" select="$GridRow"/>
            <xsl:attribute name="Grid.Column" select="$GridColumn"/>
            <xsl:value-of select="$Text"/>
        </xsl:element>
    </xsl:template>
    <!--###########-->
    <!-- TEXTBLOCK -->
    <!--###########-->

    <!--######-->
    <!-- FILL -->
    <!--######-->
    <xsl:template name="fillStudents">
        <xsl:for-each select="$Students">
            <xsl:variable name="GridRow" select="sum(position(), 1)"/>
            <xsl:variable name="Name" select="."/>
            
            <xsl:call-template name="getBorderedTextBlock">
                <xsl:with-param name="Background" select="'Gray'"/>
                <xsl:with-param name="BorderThickness" select="'0'"/>
                <xsl:with-param name="FontSize" select="'16'"/>
                <xsl:with-param name="FontWeight" select="'Bold'"/>
                <xsl:with-param name="Foreground" select="'White'"/>
                <xsl:with-param name="GridColumn" select="'0'"/>
                <xsl:with-param name="GridRow" select="$GridRow"/>
                <xsl:with-param name="HorAlign" select="'Center'"/>
                <xsl:with-param name="Margin" select="'3,3,3,3'"/>
                <xsl:with-param name="Opacity" select="'0.9'"/>
                <xsl:with-param name="Text" select="$Name"/>
                <xsl:with-param name="TextAlignment" select="'Center'"/>
                <xsl:with-param name="VertAlign" select="'Center'"/>
            </xsl:call-template>
        </xsl:for-each>
    </xsl:template>

    <xsl:template name="fillCourses">
        <xsl:for-each select="$Courses">
            <xsl:variable name="GridColumn" select="sum(position(), 1)"/>
            <xsl:variable name="Name" select="."/>

            <xsl:call-template name="getBorderedTextBlock">
                <xsl:with-param name="Background" select="'Gray'"/>
                <xsl:with-param name="BorderThickness" select="'0'"/>
                <xsl:with-param name="FontSize" select="'16'"/>
                <xsl:with-param name="FontWeight" select="'Bold'"/>
                <xsl:with-param name="Foreground" select="'White'"/>
                <xsl:with-param name="GridColumn" select="$GridColumn"/>
                <xsl:with-param name="GridRow" select="'0'"/>
                <xsl:with-param name="HorAlign" select="'Center'"/>
                <xsl:with-param name="Margin" select="'3,3,3,3'"/>
                <xsl:with-param name="Opacity" select="'0.9'"/>
                <xsl:with-param name="Text" select="$Name"/>
                <xsl:with-param name="TextAlignment" select="'Center'"/>
                <xsl:with-param name="VertAlign" select="'Center'"/>
            </xsl:call-template>
        </xsl:for-each>
    </xsl:template>

    <xsl:template name="fillScores">
        <xsl:for-each select="$Students">
            <xsl:variable name="StudentPos" select="sum(position(), 1)"/>
            <xsl:variable name="Student" select="."/>

            <xsl:for-each select="$Courses">
                <xsl:variable name="CoursePos" select="sum(position(), 1)"/>
                <xsl:variable name="Course" select="."/>
                <xsl:variable name="Score" select="//Course[@CourseName = $Course]//Result[@Student = $Student]"/>

                <xsl:call-template name="getBorderedTextBlock">
                    <xsl:with-param name="Background" select="'White'"/>
                    <xsl:with-param name="BorderThickness" select="'0'"/>
                    <xsl:with-param name="FontSize" select="'16'"/>
                    <xsl:with-param name="FontWeight" select="'Normal'"/>
                    <xsl:with-param name="Foreground" select="'Black'"/>
                    <xsl:with-param name="GridColumn" select="sum($CoursePos, 1)"/>
                    <xsl:with-param name="GridRow" select="sum($StudentPos, 1)"/>
                    <xsl:with-param name="HorAlign" select="'Center'"/>
                    <xsl:with-param name="Margin" select="'3,3,3,3'"/>
                    <xsl:with-param name="Opacity" select="'0.9'"/>
                    <xsl:with-param name="Text" select="$Score"/>
                    <xsl:with-param name="TextAlignment" select="'Center'"/>
                    <xsl:with-param name="VertAlign" select="'Center'"/>
                </xsl:call-template>
            </xsl:for-each>
        </xsl:for-each>
    </xsl:template>

    <xsl:template name="fillStatistics">
        <xsl:element name="Border">
            <xsl:attribute name="Grid.Column" select="'1'"/>
            <xsl:attribute name="Grid.Row" select="'2'"/>
            <xsl:attribute name="Margin" select="'0,5,0,0'"/>
            <xsl:attribute name="Background" select="'White'"/>

            <xsl:element name="Grid">
                <xsl:element name="Grid.RowDefinitions">
                    <xsl:call-template name="setRowSize">
                        <xsl:with-param name="Height" select="'1*'"/>
                    </xsl:call-template>
                </xsl:element>

                <xsl:element name="Grid.ColumnDefinitions">
                    <xsl:call-template name="setColumnSize">
                        <xsl:with-param name="Width" select="'20*'"/>
                    </xsl:call-template>
                    <xsl:call-template name="setColumnSize">
                        <xsl:with-param name="Width" select="'20*'"/>
                    </xsl:call-template>
                    <xsl:call-template name="setColumnSize">
                        <xsl:with-param name="Width" select="'20*'"/>
                    </xsl:call-template>
                </xsl:element>
                
                <xsl:element name="TextBlock">
                    <xsl:attribute name="FontSize" select="'24'"/>
                    <xsl:attribute name="FontWeight" select="'DemiBold'"/>
                    <xsl:attribute name="Grid.Column" select="'0'"/>
                    <xsl:attribute name="Grid.Row" select="'0'"/>
                    <xsl:attribute name="HorizontalAlignment" select="'Left'"/>
                    <xsl:attribute name="Margin" select="'10,0,0,0'"/>
                    <xsl:attribute name="Text" select="'Gemiddelde: ...'"/>
                    <xsl:attribute name="TextAlignment" select="'Left'"/>
                    <xsl:attribute name="VerticalAlignment" select="'Center'"/>
                </xsl:element>

                <xsl:element name="TextBlock">
                    <xsl:attribute name="FontSize" select="'24'"/>
                    <xsl:attribute name="FontWeight" select="'DemiBold'"/>
                    <xsl:attribute name="Grid.Column" select="'1'"/>
                    <xsl:attribute name="Grid.Row" select="'0'"/>
                    <xsl:attribute name="HorizontalAlignment" select="'Center'"/>
                    <xsl:attribute name="Text" select="'Min: ...'"/>
                    <xsl:attribute name="TextAlignment" select="'Center'"/>
                    <xsl:attribute name="VerticalAlignment" select="'Center'"/>
                </xsl:element>

                <xsl:element name="TextBlock">
                    <xsl:attribute name="FontSize" select="'24'"/>
                    <xsl:attribute name="FontWeight" select="'DemiBold'"/>
                    <xsl:attribute name="Grid.Column" select="'2'"/>
                    <xsl:attribute name="Grid.Row" select="'0'"/>
                    <xsl:attribute name="HorizontalAlignment" select="'Right'"/>
                    <xsl:attribute name="Margin" select="'0,0,10,0'"/>
                    <xsl:attribute name="Text" select="'Max: ...'"/>
                    <xsl:attribute name="TextAlignment" select="'Right'"/>
                    <xsl:attribute name="VerticalAlignment" select="'Center'"/>
                </xsl:element>
            </xsl:element>
        </xsl:element>
    </xsl:template>
    <!--######-->
    <!-- FILL -->
    <!--######-->
</xsl:stylesheet>
