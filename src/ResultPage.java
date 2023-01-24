import javax.swing.*;
import java.awt.*;

public class ResultPage extends JLayeredPane{
    int width;
    int height;
    int increment;
    int correct;
    int outOf;
    int time = 0;
    JPanel background;
    JButton exitButton = new JButton();JButton listButton = new JButton();
    JButton newQuizButton = new JButton();JButton tryAgainButton = new JButton();
    JLabel percentage;JLabel correctAnswers;
    JLabel speed;

    JLabel exitShadow = new JLabel();JLabel listShadow = new JLabel();
    JLabel newQShadow = new JLabel();JLabel tAShadow = new JLabel();
    JLabel percentageShadow = new JLabel();
    ResultPage(int width, int height){
        this.width = width;
        this.height = height;
        this.setSize(width, height);
        this.setLayout(null);
        increment = (int)(height*.02);

        background = new JPanel();
        background.setBackground(new Color(217, 232, 211));
        background.setBounds(0, 0, width, height);
        this.add(background, JLayeredPane.DEFAULT_LAYER);

        adjustComponents();
        addComponents();

        this.setVisible(true);
    }
    public void adjustComponents(){
        adjustButton(exitButton,(int)(width*.05),(int)(height*.7),(int)(width*.17),
                (int)(height*.15), "EXIT", new Color(223, 120, 120), 20);
        adjustButton(newQuizButton,exitButton.getX()+exitButton.getWidth()+increment,(int)(height*.7),(int)(width*.22),
                (int)(height*.15), "NEW QUIZ", new Color(176, 190, 170), 17);
        adjustButton(listButton,newQuizButton.getX()+newQuizButton.getWidth()+increment,(int)(height*.7),(int)(width*.15),
                (int)(height*.15), "LIST", new Color(100, 137, 82), 20);
        adjustButton(tryAgainButton,listButton.getX()+listButton.getWidth()+increment,(int)(height*.7),(int)(width*.28),
                (int)(height*.15), "TRY AGAIN", new Color(141, 205, 113), 20);

        adjustLabel(exitShadow,exitButton.getX(),(int)(height*.7)+increment,exitButton.getWidth(),
                (int)(height*.15), "", new Color(109, 10, 10), 0);
        adjustLabel(newQShadow,newQuizButton.getX(),(int)(height*.7)+increment,newQuizButton.getWidth(),
                (int)(height*.15), "", new Color(23, 66, 4), 0);
        adjustLabel(listShadow,listButton.getX(),(int)(height*.7)+increment,listButton.getWidth(),
                (int)(height*.15), "", new Color(23, 66, 4), 0);
        adjustLabel(tAShadow,tryAgainButton.getX(),(int)(height*.7)+increment,tryAgainButton.getWidth(),
                (int)(height*.15), "", new Color(23, 66, 4), 0);


    }
    public void adjustLabel(JLabel label, int x, int y, int width, int height, String name, Color color, int fontSize){
        label.setBounds(x, y, width, height);
        label.setForeground(Color.WHITE);
        label.setText(name);
        label.setBackground(color);
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setVerticalAlignment(JLabel.CENTER);
        label.setFont(new Font("Times New Roman", Font.BOLD, fontSize));
        label.setOpaque(true);
    }
    public void adjustButton(JButton button, int x, int y, int width, int height, String name, Color color, int fontSize){
        button.setBounds(x, y, width, height);
        button.setForeground(Color.WHITE);
        button.setText(name);
        button.setBackground(color);
        button.setFont(new Font("Times New Roman", Font.BOLD, fontSize));
        button.setFocusable(false);
    }

    public void addComponents(){
        this.add(exitButton, JLayeredPane.MODAL_LAYER);
        this.add(newQuizButton, JLayeredPane.MODAL_LAYER);
        this.add(listButton, JLayeredPane.MODAL_LAYER);
        this.add(tryAgainButton, JLayeredPane.MODAL_LAYER);
        this.add(exitShadow, JLayeredPane.PALETTE_LAYER);
        this.add(listShadow, JLayeredPane.PALETTE_LAYER);
        this.add(newQShadow, JLayeredPane.PALETTE_LAYER);
        this.add(tAShadow, JLayeredPane.PALETTE_LAYER);
    }
    public void placeResults(){
        percentage = new JLabel();
        correctAnswers = new JLabel();
        speed = new JLabel();
        double percent = ((double) correct/(double) outOf)*100;
        double minutes = (double) Math.round((((double)time)/60)*100)/100;
        adjustLabel(percentage, (int)(width*.15), (int)(height*.1), (int)(width*.3),
                (int)(height*.4), (int) percent+"%", new Color(141, 205, 113), 40);
        adjustLabel(percentageShadow, (int)(width*.15), (int)(height*.1)+increment, (int)(width*.3),
                (int)(height*.4), "", new Color(23, 66, 4), 0);
        adjustLabel(correctAnswers, (int)(width*.5), (int)(height*.16), (int)(width*.35),
                (int)(height*.15), "GOT "+correct+"/"+outOf+" CORRECTLY",
                new Color(176, 190, 170), 15);
        correctAnswers.setFont(new Font("Times New Roman", Font.ITALIC, 15));

        adjustLabel(speed, (int)(width*.5), correctAnswers.getY()+correctAnswers.getHeight()+increment*2, (int)(width*.35),
                (int)(height*.15), "FINISHED IN "+minutes+" MINUTES",
                new Color(176, 190, 170), 15);
        speed.setFont(new Font("Times New Roman", Font.ITALIC, 15));

        this.add(correctAnswers, JLayeredPane.MODAL_LAYER);
        this.add(speed, JLayeredPane.MODAL_LAYER);
        this.add(percentageShadow, JLayeredPane.PALETTE_LAYER);
        this.add(percentage, JLayeredPane.MODAL_LAYER);
    }

    public int getCenter(int size, boolean isWidth) {
        if (isWidth) {
            return ((width / 2) - (size / 2));
        } else {
            return ((height / 2) - (size / 2));
        }
    }
}
