package xaml;

import org.jdom2.Element;
import org.jdom2.Namespace;

import javax.lang.model.element.Name;

public class XamlGenerator {
    private XamlGenerator() { }

    /**
     * BACKGROUNDS *
     */
    public static Element getSolidBackground(String control, String color) {
        Element background = new Element(control + ".Background", getDefaultNamespace());
        background.addContent(getSolidColorBrush(color));
        return background;
    }
    public static Element getGradientBackground(String control, String startColor, String endColor, String offset) {
        Element background = new Element(control + ".Background", getDefaultNamespace());
        background.addContent(getLinearGradientBrush(startColor, endColor, offset));
        return background;
    }

    /**
     * BORDER *
     */
    public static Element getBorder(String thickness, String bg, String cursor) {
        Element border = new Element("Border", getDefaultNamespace());

        border.setAttribute("BorderThickness", thickness);
        border.setAttribute("Background", bg);
        border.setAttribute("Cursor", cursor);

        return border;
    }
    public static Element getBorder(String thickness, String width, String height, String bg) {
        Element border = new Element("Border", getDefaultNamespace());

        border.setAttribute("BorderThickness", thickness);
        border.setAttribute("Width", width);
        border.setAttribute("Height", height);
        border.setAttribute("Background", bg);

        return border;
    }
    public static Element getBorder(String thickness, String width, String height, String bg, String opacity) {
        Element border = new Element("Border", getDefaultNamespace());

        border.setAttribute("BorderThickness", thickness);
        border.setAttribute("Width", width);
        border.setAttribute("Height", height);
        border.setAttribute("Background", bg);
        border.setAttribute("Opacity", opacity);

        return border;
    }

    /**
     * BRUSHES *
     */
    public static Element getSolidColorBrush(String color) {
        return new Element("SolidColorBrush", getDefaultNamespace()).setAttribute("Color", color);
    }
    public static Element getLinearGradientBrush(String startColor, String endColor, String offset) {
        Element lgb = new Element("LinearGradientBrush", getDefaultNamespace());

        lgb.addContent(XamlGenerator.getGradientStop(startColor));
        lgb.addContent(XamlGenerator.getGradientStop(endColor, offset));

        return lgb;
    }
    public static Element getGradientStop(String color) {
        Element gs = new Element("GradientStop", getDefaultNamespace());

        gs.setAttribute("Color", color);

        return gs;
    }
    public static Element getGradientStop(String color, String offset) {
        Element gs = new Element("GradientStop", getDefaultNamespace());

        gs.setAttribute("Color", color);
        gs.setAttribute("Offset", offset);

        return gs;
    }

    /**
     * CANVAS *
     */
    public static Element getCanvas() {
        return new Element("Canvas", getDefaultNamespace());
    }
    public static Element getCanvas(String width, String height) {
        Element canvas = getCanvas();

        canvas.setAttribute("Width", width);
        canvas.setAttribute("Height", height);

        return canvas;
    }

    /**
     * EFFECT *
     */
    public static Element getEffect(String control) {
        return new Element(control + ".Effect", getDefaultNamespace());
    }
    public static Element getDropShadowEffect(String blurRadius, String direction, String shadowDepth) {
        Element effect = new Element("DropShadowEffect", getDefaultNamespace());

        effect.setAttribute("BlurRadius", blurRadius);
        effect.setAttribute("Direction", direction);
        effect.setAttribute("ShadowDepth", shadowDepth);

        return effect;
    }

    /**
     * GRID *
     */
    public static Element getGrid() {
        Element grid = new Element("Grid", getDefaultNamespace());
        return grid;
    }
    public static Element getGrid(String[] rowSizes, String[] colSizes, Boolean showGridLines) {
        Element grid = getGrid();

        grid.addContent(getGridRows(rowSizes));
        grid.addContent(getGridColumns(colSizes));

        if (showGridLines) { grid.setAttribute("ShowGridLines", "true"); }

        return grid;
    }
    private static Element getGridRows(String[] rowSizes) {
        Element rowDef = new Element("Grid.RowDefinitions", getDefaultNamespace());

        for (String rowSize : rowSizes) {
            if (rowSize != null) {
                rowDef.addContent(new Element("RowDefinition", getDefaultNamespace()).setAttribute("Height", rowSize));
            } else {
                rowDef.addContent(new Element("RowDefinition", getDefaultNamespace()));
            }
        }

        return rowDef;
    }
    private static Element getGridColumns(String[] colSizes) {
        Element columnDef = new Element("Grid.ColumnDefinitions", getDefaultNamespace());

        for (String colSize : colSizes) {
            if (colSize != null) {
                columnDef.addContent(new Element("ColumnDefinition", getDefaultNamespace()).setAttribute("Width", colSize));
            } else {
                columnDef.addContent(new Element("ColumnDefinition", getDefaultNamespace()));
            }
        }

        return columnDef;
    }

    /**
     * LINE *
     */
    public static Element getLine(String x1, String y1, String x2, String y2, String stroke, String strokeThickness) {
        Element line = new Element("Line", getDefaultNamespace());

        line.setAttribute("X1", x1);
        line.setAttribute("Y1", y1);
        line.setAttribute("X2", x2);
        line.setAttribute("Y2", y2);
        line.setAttribute("Stroke", stroke);
        line.setAttribute("StrokeThickness", strokeThickness);

        return line;
    }
    public static Element getLine(String x1, String y1, String x2, String y2,
                                  String stroke, String strokeThickness, String visibility) {
        Element line = new Element("Line", getDefaultNamespace());

        line.setAttribute("X1", x1);
        line.setAttribute("Y1", y1);
        line.setAttribute("X2", x2);
        line.setAttribute("Y2", y2);
        line.setAttribute("Stroke", stroke);
        line.setAttribute("StrokeThickness", strokeThickness);
        line.setAttribute("Visibility", visibility);

        return line;
    }

    /**
     * NAMESPACES *
     */
    public static void setXNamespace(Element control) {
        control.addNamespaceDeclaration(getXNamespace());
    }
    public static Namespace getDefaultNamespace() {
        return Namespace.getNamespace("http://schemas.microsoft.com/winfx/2006/xaml/presentation");
    }
    public static Namespace getXNamespace() {
        return Namespace.getNamespace("x", "http://schemas.microsoft.com/winfx/2006/xaml");
    }
    public static Namespace getNavigationNamespace(){
        return Namespace.getNamespace("navigation","clr-namespace:System.Windows.Controls;assembly=System.Windows.Controls.Navigation");
    }

    /**
     * PAGE *
     */
    public static Element getPage() {
        return new Element("Page", getDefaultNamespace());
    }

    /**
     * SCROLLVIEWER *
     */
    public static Element getScrollViewer() { return new Element("ScrollViewer", getDefaultNamespace()); }

    /**
     * STACKPANEL *
     */
    public static Element getStackPanel(String margin, String orientation) {
        Element sp = new Element("StackPanel", getDefaultNamespace());

        sp.setAttribute("Margin", margin);
        sp.setAttribute("Orientation", orientation);

        return sp;
    }

    /**
     * TEXTBLOCK *
     */
    public static Element getTextBlock(String text, String fontSize) {
        Element tb = new Element("TextBlock", getDefaultNamespace());

        tb.addContent(text);
        tb.setAttribute("FontSize", fontSize);

        return tb;
    }
    public static Element getTextBlock(String text, String fontSize, String foreground) {
        Element tb = new Element("TextBlock", getDefaultNamespace());

        tb.addContent(text);
        tb.setAttribute("FontSize", fontSize);
        tb.setAttribute("Foreground", foreground);

        return tb;
    }
    public static Element getTextBlock(String text, String fontSize, String foreground, String textWrapping) {
        Element tb = new Element("TextBlock", getDefaultNamespace());

        tb.addContent(text);
        tb.setAttribute("FontSize", fontSize);
        tb.setAttribute("Foreground", foreground);
        tb.setAttribute("TextWrapping", textWrapping);

        return tb;
    }
    public static Element getTextBlock(String text, String fontSize, String fontStyle, String fontWeight,
                                            String horAlign, String vertAlign) {
        Element tb = new Element("TextBlock", getDefaultNamespace());

        tb.addContent(text);
        tb.setAttribute("FontSize", fontSize);
        tb.setAttribute("FontStyle", fontStyle);
        tb.setAttribute("FontWeight", fontWeight);
        tb.setAttribute("HorizontalAlignment", horAlign);
        tb.setAttribute("VerticalAlignment", vertAlign);

        return tb;
    }
    public static Element getTextBlock(String text, String fontSize, String fontStyle, String fontWeight,
                                       String horAlign, String vertAlign, String foreground) {
        Element tb = new Element("TextBlock", getDefaultNamespace());

        tb.addContent(text);
        tb.setAttribute("FontSize", fontSize);
        tb.setAttribute("Foreground", foreground);
        tb.setAttribute("FontStyle", fontStyle);
        tb.setAttribute("FontWeight", fontWeight);
        tb.setAttribute("HorizontalAlignment", horAlign);
        tb.setAttribute("VerticalAlignment", vertAlign);

        return tb;
    }
    /**
     * TRANSFORM *
     */
    public static Element getLayoutTransform(String control) {
        return new Element(control + ".LayoutTransform", getDefaultNamespace());
    }
    public static Element getRenderTransform(String control) {
        return new Element(control + ".RenderTransform", getDefaultNamespace());
    }
    public static Element getRotateTransform(String angle) {
        Element rotate = new Element("RotateTransform", getDefaultNamespace());

        rotate.setAttribute("Angle", angle);

        return rotate;
    }
    public static Element getRotateTransform(String angle, String xOrigin, String yOrigin) {
        Element rotate = new Element("RotateTransform", getDefaultNamespace());

        rotate.setAttribute("Angle", angle);
        rotate.setAttribute("CenterX", xOrigin);
        rotate.setAttribute("CenterY", yOrigin);

        return rotate;
    }
    public static Element getTranslateTransform(String x, String y) {
        Element rotate = new Element("TranslateTransform", getDefaultNamespace());

        rotate.setAttribute("X", x);
        rotate.setAttribute("Y", y);

        return rotate;
    }

    /**
     * VIEWBOX *
     */
    public static Element getViewBox() {
        return new Element("Viewbox", getDefaultNamespace());
    }

    /**
     * Visibility *
     */
    public static void hide(Element element) { element.setAttribute("Visibility", "Collapsed"); }

    /**
     * XNAME *
     */
    public static void setXName(Element element, String name) {
        element.setAttribute("Name", name, getXNamespace());
    }
}