import javax.swing.*;
import java.awt.*;

public class FrontPage extends JLayeredPane {
    int width;
    int height;
    JLabel title = new JLabel();
    JLabel titleShadow = new JLabel();
    JButton startButton = new JButton();
    JPanel startBackground = new JPanel();
    JButton helpButton = new JButton();
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
        int titleWidth = (int) (width * .6);
        int titleHeight = (int) (height * .3);
        int buttonWidth = (int) (width * .3);
        int buttonHeight = (int) (height * .15);
        int increment = (int) (height * .02);

        title.setText("QUIZ.IT");
        title.setFont(new Font("Times New Roman", Font.BOLD, (int) (width * .13)));
        title.setForeground(new Color(23, 66, 4));
        title.setHorizontalAlignment(JLabel.CENTER);
        title.setBounds(getCenter(titleWidth, true), (int) (height * .1), titleWidth, titleHeight);

        titleShadow.setText("QUIZ.IT");
        titleShadow.setHorizontalAlignment(JLabel.CENTER);
        titleShadow.setFont(new Font("Times New Roman", Font.BOLD, (int) (width * .13)));
        titleShadow.setForeground(new Color(159, 178, 150));
        titleShadow.setBounds(title.getX() + increment / 2, title.getY() + increment / 2, titleWidth, titleHeight);

        startButton.setText("START");
        startButton.setBounds(getCenter(buttonWidth, true), (int) (height * .45), buttonWidth, buttonHeight);
        startButton.setFocusable(false);
        startButton.setForeground(new Color(0xFDFCFD));
        startButton.setBackground(new Color(223, 120, 120));
        startButton.setFont(new Font("Times New Roman", Font.PLAIN, 20));

        startBackground.setBounds(startButton.getX(), startButton.getY() + increment, startButton.getWidth(), startButton.getHeight());
        startBackground.setBackground(new Color(109, 10, 10));

        helpButton.setText("HELP");
        helpButton.setBounds(getCenter(buttonWidth, true), (int) (height * .65), buttonWidth, buttonHeight);
        helpButton.setFocusable(false);
        helpButton.setForeground(new Color(0xFDFCFD));
        helpButton.setBackground(new Color(141, 205, 113));
        helpButton.setFont(new Font("Times New Roman", Font.PLAIN, 20));

        helpBackground.setBounds(helpButton.getX(), helpButton.getY() + increment, helpButton.getWidth(), helpButton.getHeight());
        helpBackground.setBackground(new Color(63, 82, 54));


    }

    // ---------------------------------------------------------
    public void addComponents() {
        this.add(title, JLayeredPane.MODAL_LAYER);
        this.add(startButton, JLayeredPane.MODAL_LAYER);
        this.add(helpButton, JLayeredPane.MODAL_LAYER);
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

