import javax.swing.*;
import java.awt.*;

public class HelpPage extends JLayeredPane {
    int width;
    int height;

    HelpPage(int width, int height){
        this.width = width;
        this.height = height;
        this.setSize(width, height);
        this.setLayout(null);

        JPanel background = new JPanel();
        background.setBackground(new Color(217, 232, 211));
        background.setBounds(0, 0, width, height);
        this.add(background, JLayeredPane.DEFAULT_LAYER);


        this.setVisible(true);
    }
}
