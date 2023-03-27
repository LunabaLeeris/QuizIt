import javax.swing.*;
import java.awt.*;

public class FrontPage extends JLayeredPane {
    int width;
    int height;
    JLabel title = new JLabel();
    JLabel titleShadow = new JLabel();
    JButton startButton = new JButton();
    JPanel startBackground = new JPanel();
    JPanel helpBackground = new JPanel();

    FrontPage(int width, int height) {
        this.width = width;
        this.height = height;
        this.setSize(width, height);
        this.setLayout(null);

        JPanel background = new JPanel();
        background.setBackground(new Color(217, 232, 211));
        background.setBounds(0, 0, width, height);
        this.add(background, JLayeredPane.DEFAULT_LAYER);

        adjustComponents();
        addComponents();

        this.setVisible(true);
    }

    // ------------------ADJUSTMENTS---------------------------
    public void adjustSize() {
        width = this.getWidth();
        height = this.getHeight();
    }

    public void adjustComponents() {
        int titleWidth = (int) (width * .8);
        int titleHeight = (int) (height * .3);
        int buttonWidth = (int) (width * .35);
        int buttonHeight = (int) (height * .2);
        int increment = (int) (height * .02);

        title.setText("QUIZ.IT");
        title.setFont(new Font("Times New Roman", Font.BOLD, (int) (width * .17)));
        title.setForeground(new Color(23, 66, 4));
        title.setHorizontalAlignment(JLabel.CENTER);
        title.setBounds(getCenter(titleWidth, true), (int) (height * .1), titleWidth, titleHeight);

        titleShadow.setText("QUIZ.IT");
        titleShadow.setHorizontalAlignment(JLabel.CENTER);
        titleShadow.setFont(new Font("Times New Roman", Font.BOLD, (int) (width * .17)));
        titleShadow.setForeground(new Color(159, 178, 150));
        titleShadow.setBounds(title.getX() + increment / 2, title.getY() + increment / 2, titleWidth, titleHeight);

        startButton.setText("START");
        startButton.setBounds(getCenter(buttonWidth, true), (int) (height * .5), buttonWidth, buttonHeight);
        startButton.setFocusable(false);
        startButton.setForeground(new Color(0xFDFCFD));
        startButton.setBackground(new Color(223, 120, 120));
        startButton.setFont(new Font("Times New Roman", Font.BOLD, 40));

        startBackground.setBounds(startButton.getX(), startButton.getY() + increment, startButton.getWidth(), startButton.getHeight());
        startBackground.setBackground(new Color(109, 10, 10));

    }

    // ---------------------------------------------------------
    public void addComponents() {
        this.add(title, JLayeredPane.MODAL_LAYER);
        this.add(startButton, JLayeredPane.MODAL_LAYER);
        this.add(startBackground, JLayeredPane.PALETTE_LAYER);
        this.add(helpBackground, JLayeredPane.PALETTE_LAYER);
        this.add(titleShadow, JLayeredPane.PALETTE_LAYER);
    }

    public int getCenter(int size, boolean isWidth) {
        if (isWidth) {
            return ((width / 2) - (size / 2));
        } else {
            return ((height / 2) - (size / 2));
        }
    }
}

