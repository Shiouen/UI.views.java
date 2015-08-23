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

    <xsl:variable name="LetterAmount" select="string-length(string-join($Courses, ''))"/>

    <xsl:variable name="MaxStudentAmount" select="20"/>

    <xsl:variable name="RowHeight" select="$StudentAmount"/>

    <xsl:template match="/">
        <xsl:element name="Grid">
            <xsl:attribute name="ShowGridLines" select="'False'"/>
            <xsl:attribute name="Loaded" select="'onLoaded'"/>

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

            <xsl:call-template name="getClassTextBlock">
                <xsl:with-param name="Text" select="//@Class"/>
            </xsl:call-template>

            <xsl:element name="Grid">
                <xsl:attribute name="ShowGridLines" select="'False'"/>

                <xsl:attribute name="Grid.Row" select="'1'"/>
                <xsl:attribute name="Grid.Column" select="'1'"/>

                <xsl:call-template name="genRowDefs">
                    <xsl:with-param name="Amount" select="$Students"/>
                    <xsl:with-param name="Height" select="$RowHeight"/>
                </xsl:call-template>
                <xsl:call-template name="genNoWidthColumnDefs">
                    <xsl:with-param name="Amount" select="$Courses"/>
                </xsl:call-template>

                <xsl:call-template name="fillStudents"/>
                <xsl:call-template name="fillCourses"/>
                <xsl:call-template name="fillScores"/>
            </xsl:element>

            <xsl:call-template name="fillStatistics"/>
            <xsl:call-template name="fillCoursesStudentsTextBlock"/>
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
                <xsl:if test="position() &lt;= $MaxStudentAmount">
                    <xsl:call-template name="setRowSize">
                        <xsl:with-param name="Height" select="$Height"/>
                    </xsl:call-template>
                </xsl:if>
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

    <xsl:template name="genNoWidthColumnDefs">
        <xsl:param name="Amount"/>

        <xsl:element name="Grid.ColumnDefinitions">
            <xsl:for-each select="$Amount">
                <xsl:element name="ColumnDefinition"/>
            </xsl:for-each>
            <xsl:element name="ColumnDefinition"/>
        </xsl:element>
    </xsl:template>
    <!--######-->
    <!-- GRID -->
    <!--######-->

    <!--###########-->
    <!-- TEXTBLOCK -->
    <!--###########-->
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

    <xsl:template name="getClassTextBlock">
        <xsl:param name="Text"/>
        
        <xsl:element name="TextBlock">
            <xsl:attribute name="FontSize" select="'32'"/>
            <xsl:attribute name="FontWeight" select="'SemiBold'"/>
            <xsl:attribute name="Foreground" select="'White'"/>
            <xsl:attribute name="HorizontalAlignment" select="'Center'"/>
            <xsl:attribute name="VerticalAlignment" select="'Center'"/>
            <xsl:attribute name="Grid.Row" select="'0'"/>
            <xsl:attribute name="Grid.Column" select="'1'"/>
            
            <xsl:value-of select="$Text"/>
        </xsl:element>
    </xsl:template>

    <xsl:template name="getCourseBorderedTextBlock">
        <xsl:param name="GridColumn"/>
        <xsl:param name="Text"/>

        <xsl:element name="Border">
            <xsl:attribute name="Background" select="'Gray'"/>
            <xsl:attribute name="BorderThickness" select="'0'"/>
            <xsl:attribute name="Grid.Column" select="$GridColumn"/>
            <xsl:attribute name="Grid.Row" select="'0'"/>
            <xsl:attribute name="Margin" select="'3,3,3,3'"/>
            <xsl:attribute name="Opacity" select="'0.9'"/>

            <xsl:element name="TextBlock">
                <xsl:attribute name="FontSize" select="'16'"/>
                <xsl:attribute name="FontWeight" select="'Bold'"/>
                <xsl:attribute name="Foreground" select="'White'"/>
                <xsl:attribute name="HorizontalAlignment" select="'Center'"/>
                <xsl:attribute name="TextAlignment" select="'Center'"/>
                <xsl:attribute name="VerticalAlignment" select="'Center'"/>

                <xsl:value-of select="$Text"/>
            </xsl:element>
        </xsl:element>
    </xsl:template>

    <xsl:template name="getScoreBorderedTextBlock">
        <xsl:param name="GridColumn"/>
        <xsl:param name="GridRow"/>
        <xsl:param name="Text"/>

        <xsl:param name="XNameTextBlock"/>
        <xsl:param name="XNameBorder"/>
        
        <xsl:element name="Border">
            <xsl:attribute name="Background" select="'White'"/>
            <xsl:attribute name="BorderThickness" select="'0'"/>
            <xsl:attribute name="Grid.Column" select="$GridColumn"/>
            <xsl:attribute name="Grid.Row" select="$GridRow"/>
            <xsl:attribute name="Margin" select="'3,3,3,3'"/>
            <xsl:attribute name="Opacity" select="'0.9'"/>
            <xsl:attribute name="x:Name" select="$XNameBorder"/>
            
            <xsl:element name="TextBlock">
                <xsl:attribute name="FontSize" select="'16'"/>
                <xsl:attribute name="HorizontalAlignment" select="'Center'"/>
                <xsl:attribute name="TextAlignment" select="'Center'"/>
                <xsl:attribute name="VerticalAlignment" select="'Center'"/>
                <xsl:attribute name="x:Name" select="$XNameTextBlock"/>

                <xsl:value-of select="$Text"/>
            </xsl:element>
        </xsl:element>
    </xsl:template>

    <xsl:template name="getStatisticBorderedTextBlock">
        <xsl:param name="Function"/>
        <xsl:param name="GridColumn"/>
        <xsl:param name="HorAlign"/>
        <xsl:param name="Margin"/>
        <xsl:param name="Text"/>
        <xsl:param name="TextAlignment"/>
        <xsl:param name="XNameBorder"/>
        <xsl:param name="XNameTextBlock"/>

        <xsl:element name="Border">
            <xsl:attribute name="Background" select="'White'"/>
            <xsl:attribute name="BorderThickness" select="'0'"/>
            <xsl:attribute name="Cursor" select="'Hand'"/>
            <xsl:attribute name="Grid.Column" select="$GridColumn"/>
            <xsl:attribute name="Grid.Row" select="'0'"/>
            <xsl:attribute name="Margin" select="$Margin"/>
            <xsl:attribute name="MouseLeftButtonUp" select="$Function"/>
            <xsl:attribute name="x:Name" select="$XNameBorder"/>

            <xsl:element name="TextBlock">
                <xsl:attribute name="FontSize" select="'24'"/>
                <xsl:attribute name="FontWeight" select="'SemiBold'"/>
                <xsl:attribute name="Foreground" select="'Black'"/>
                <xsl:attribute name="HorizontalAlignment" select="$HorAlign"/>
                <xsl:attribute name="TextAlignment" select="$TextAlignment"/>
                <xsl:attribute name="VerticalAlignment" select="'Center'"/>
                <xsl:attribute name="x:Name" select="$XNameTextBlock"/>
                
                <xsl:value-of select="$Text"/>
            </xsl:element>
        </xsl:element>
    </xsl:template>
    
    <xsl:template name="getStudentBorderedTextBlock">
        <xsl:param name="GridRow"/>
        <xsl:param name="XNameBorder"/>
        <xsl:param name="XNameTextBlock"/>
        <xsl:param name="Text"/>

        <xsl:element name="Border">
            <xsl:attribute name="Background" select="'Gray'"/>
            <xsl:attribute name="BorderThickness" select="'0'"/>
            <xsl:attribute name="Cursor" select="'Hand'"/>
            <xsl:attribute name="Grid.Column" select="'0'"/>
            <xsl:attribute name="Grid.Row" select="$GridRow"/>
            <xsl:attribute name="Margin" select="'3,3,3,3'"/>
            <xsl:attribute name="Opacity" select="'0.9'"/>
            <xsl:attribute name="x:Name" select="$XNameBorder"/>
            <xsl:attribute name="MouseLeftButtonUp" select="'selectStudent'"/>

            <xsl:element name="TextBlock">
                <xsl:attribute name="FontSize" select="'16'"/>
                <xsl:attribute name="FontWeight" select="'Bold'"/>
                <xsl:attribute name="Foreground" select="'White'"/>
                <xsl:attribute name="HorizontalAlignment" select="'Center'"/>
                <xsl:attribute name="TextAlignment" select="'Center'"/>
                <xsl:attribute name="VerticalAlignment" select="'Center'"/>
                <xsl:attribute name="x:Name" select="$XNameTextBlock"/>
                
                <xsl:value-of select="$Text"/>
            </xsl:element>
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
            <xsl:if test="position() &lt;= $MaxStudentAmount">
                <xsl:variable name="GridRow" select="sum(position(), 1)"/>
                <xsl:variable name="Name" select="."/>

                <xsl:call-template name="getStudentBorderedTextBlock">
                    <xsl:with-param name="XNameBorder" select="concat(replace($Name, ' ', '_'), '_Border')"/>
                    <xsl:with-param name="XNameTextBlock" select="concat(replace($Name, ' ', '_'), '_Text_Block')"/>
                    <xsl:with-param name="GridRow" select="$GridRow"/>
                    <xsl:with-param name="Text" select="$Name"/>
                </xsl:call-template>
            </xsl:if>
        </xsl:for-each>
    </xsl:template>

    <xsl:template name="fillCourses">
        <xsl:for-each select="$Courses">
            <xsl:variable name="GridColumn" select="sum(position(), 1)"/>
            <xsl:variable name="Name" select="."/>

            <xsl:call-template name="getCourseBorderedTextBlock">
                <xsl:with-param name="GridColumn" select="$GridColumn"/>
                <xsl:with-param name="Text" select="$Name"/>
            </xsl:call-template>
        </xsl:for-each>
    </xsl:template>

    <xsl:template name="fillScores">
        <xsl:for-each select="$Students">
            <xsl:variable name="StudentPos" select="sum(position(), 1)"/>
            <xsl:variable name="Student" select="."/>
            <xsl:variable name="StudentNoSpaces" select="replace($Student, ' ', '_')"/>

            <xsl:if test="position() &lt;= $MaxStudentAmount">
                <xsl:for-each select="$Courses">
                    <xsl:variable name="CoursePos" select="sum(position(), 1)"/>
                    <xsl:variable name="Course" select="."/>
                    <xsl:variable name="Score" select="//Course[@CourseName = $Course]//Result[@Student = $Student]"/>
                    
                    <xsl:variable name="CourseNoSpaces" select="replace($Course, ' ', '_')"/>
                    <xsl:variable name="XName" select="concat($StudentNoSpaces, concat('_', $CourseNoSpaces))"/>


                    <xsl:variable name="XNameBorder" select="concat($XName, '_Border')"/>
                    <xsl:variable name="XNameTextBlock" select="concat($XName, '_Text_Block')"/>

                    <xsl:call-template name="getScoreBorderedTextBlock">
                        <xsl:with-param name="Text" select="$Score"/>
                        <xsl:with-param name="GridColumn" select="sum($CoursePos, 1)"/>
                        <xsl:with-param name="GridRow" select="sum($StudentPos, 1)"/>
                        <xsl:with-param name="XNameBorder" select="$XNameBorder"/>
                        <xsl:with-param name="XNameTextBlock" select="$XNameTextBlock"/>
                    </xsl:call-template>
                </xsl:for-each>
            </xsl:if>
        </xsl:for-each>
    </xsl:template>

    <xsl:template name="fillStatistics">
        <xsl:element name="Grid">
            <xsl:attribute name="Grid.Column" select="'1'"/>
            <xsl:attribute name="Grid.Row" select="'2'"/>
            <xsl:attribute name="Margin" select="'0,5,0,0'"/>

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
                <xsl:call-template name="setColumnSize">
                    <xsl:with-param name="Width" select="'20*'"/>
                </xsl:call-template>
                <xsl:call-template name="setColumnSize">
                    <xsl:with-param name="Width" select="'20*'"/>
                </xsl:call-template>
            </xsl:element>

            <xsl:call-template name="getStatisticBorderedTextBlock">
                <xsl:with-param name="Function" select="'markMean'"/>
                <xsl:with-param name="GridColumn" select="'0'"/>
                <xsl:with-param name="HorAlign" select="'Center'"/>
                <xsl:with-param name="Margin" select="'10,0,0,0'"/>
                <xsl:with-param name="Text" select="'Gemiddelde: ...'"/>
                <xsl:with-param name="TextAlignment" select="'Left'"/>
                <xsl:with-param name="XNameBorder" select="'Mean_Border'"/>
                <xsl:with-param name="XNameTextBlock" select="'Mean_Text_Block'"/>
            </xsl:call-template>

            <xsl:call-template name="getStatisticBorderedTextBlock">
                <xsl:with-param name="Function" select="'markMin'"/>
                <xsl:with-param name="GridColumn" select="'2'"/>
                <xsl:with-param name="HorAlign" select="'Center'"/>
                <xsl:with-param name="Margin" select="'0,0,0,0'"/>
                <xsl:with-param name="Text" select="'Minimum: ...'"/>
                <xsl:with-param name="TextAlignment" select="'Center'"/>
                <xsl:with-param name="XNameBorder" select="'Min_Border'"/>
                <xsl:with-param name="XNameTextBlock" select="'Min_Text_Block'"/>
            </xsl:call-template>

            <xsl:call-template name="getStatisticBorderedTextBlock">
                <xsl:with-param name="Function" select="'markMax'"/>
                <xsl:with-param name="GridColumn" select="'4'"/>
                <xsl:with-param name="HorAlign" select="'Center'"/>
                <xsl:with-param name="Margin" select="'0,0,10,0'"/>
                <xsl:with-param name="Text" select="'Maximum: ...'"/>
                <xsl:with-param name="TextAlignment" select="'Right'"/>
                <xsl:with-param name="XNameBorder" select="'Max_Border'"/>
                <xsl:with-param name="XNameTextBlock" select="'Max_Text_Block'"/>
            </xsl:call-template>
        </xsl:element>
    </xsl:template>

    <xsl:template name="fillCoursesStudentsTextBlock">
        <xsl:element name="TextBlock">
            <xsl:attribute name="Visibility" select="'Collapsed'"/>
            <xsl:attribute name="x:Name" select="'Course_List'"/>
            
            <xsl:value-of select="string-join($Courses, '|')"/>
        </xsl:element>

        <xsl:element name="TextBlock">
            <xsl:attribute name="Visibility" select="'Collapsed'"/>
            <xsl:attribute name="x:Name" select="'Student_List'"/>

            <xsl:value-of select="string-join($Students, '|')"/>
        </xsl:element>
    </xsl:template>
    <!--######-->
    <!-- FILL -->
    <!--######-->
</xsl:stylesheet>
