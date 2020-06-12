package fam.badger_ken.matchmaker.widget;

import javax.swing.*;
import java.awt.*;

public class CopyableLabel extends JTextPane {

    private static final long serialVersionUID = -1;

    private static final Font DEFAULT_FONT;

    static {
        Font font = UIManager.getFont("Label.font");
        DEFAULT_FONT = (font != null) ? font: new Font("Tahoma", Font.PLAIN, 11);
    }

    public CopyableLabel(String text) {
        construct();
        this.setText(text);
    }

    private void construct() {
        setContentType("text/html");

        setEditable(false);
        setBackground(null);
        setBorder(null);

        putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, true);
        setFont(DEFAULT_FONT);
    }
}