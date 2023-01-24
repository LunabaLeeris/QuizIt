import javax.swing.*;
import java.awt.*;

public class NewQuizPage extends JLayeredPane{
    int width;
    int height;
    ListPage listPage;
    JTextArea definition = new JTextArea();
    JTextArea answer = new JTextArea();
    JScrollPane definitionScroll;
    JScrollPane answerScroll;
    JButton listButton = new JButton();
    JPanel listShadow = new JPanel();
    JButton addButton = new JButton();
    JPanel addShadow = new JPanel();;
    JButton finishButton = new JButton();
    JPanel finishShadow = new JPanel();


    NewQuizPage(int width, int height){
        this.width = width;
        this.height = height;
        this.setLayout(null);
        listPage = new ListPage(width, height);
        this.setSize(width, height);

        JPanel background = new JPanel();
        background.setBackground(new Color(217, 232, 211));
        background.setBounds(0, 0, width, height);
        this.add(background, JLayeredPane.DEFAULT_LAYER);

        adjustComponents();
        addComponents();


        this.setVisible(true);
    }
    public void adjustComponents(){
        int definitionWidth = (int) (width*.8);
        int definitionHeight = (int) (height*.45);
        int answerWidth = (int) (width*.45);
        int answerHeight = (int) (height*.3);
        int listBWidth = (int) (width*.15);
        int listBHeight = (int) (height*.12);
        int increment = (int) (height * .02);

        definition.setBounds(getCenter(definitionWidth, true),
                (int) (height*.05), definitionWidth, definitionHeight);
        definition.setText("Input definitions here...");
        definition.setFont(new Font("Times New Roman", Font.PLAIN, 20));
        definition.setLineWrap(true);

        definitionScroll = new JScrollPane(definition, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        definitionScroll.setBounds(definition.getBounds());

        answer.setBounds(definition.getX(), (int) (height*.53), answerWidth, answerHeight);
        answer.setFont(new Font("Times New Roman", Font.PLAIN, 15));
        answer.setText("Input answer/s here...");
        answer.setLineWrap(true);

        answerScroll = new JScrollPane(answer, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        answerScroll.setBounds(definition.getX(), (int) (height*.53), answerWidth, answerHeight);

        addButton.setBounds((int) (width*.57), answer.getY(), listBWidth, listBHeight);
        addButton.setText("ADD");
        addButton.setFocusable(false);
        addButton.setBackground(new Color(141, 205, 113));
        addButton.setForeground(Color.WHITE);

        addShadow.setBounds(addButton.getX(), addButton.getY()+increment, listBWidth, listBHeight);
        addShadow.setBackground(new Color(23, 66, 4));

        listButton.setBounds((int) (width*.73), answer.getY(), listBWidth, listBHeight);
        listButton.setText("LIST");
        listButton.setFocusable(false);
        listButton.setBackground(new Color(176, 190, 170));
        listButton.setForeground(Color.WHITE);

        listShadow.setBounds(listButton.getX(), listButton.getY()+increment, listBWidth, listBHeight);
        listShadow.setBackground(new Color(23, 66, 4));

        finishButton.setBounds((int) (width*.6), (int) (answer.getY()+height*.16), (int) (listBWidth*1.7), listBHeight);
        finishButton.setText("FINISH");
        finishButton.setFocusable(false);
        finishButton.setBackground(new Color(223, 120, 120));
        finishButton.setForeground(Color.WHITE);

        finishShadow.setBounds(finishButton.getX(), finishButton.getY()+increment, finishButton.getWidth(), finishButton.getHeight());
        finishShadow.setBackground(new Color(109, 10, 10));

    }
    public void addComponents(){
        this.add(definitionScroll, JLayeredPane.MODAL_LAYER);
        this.add(answerScroll, JLayeredPane.MODAL_LAYER);
        this.add(addButton, JLayeredPane.MODAL_LAYER);
        this.add(listButton, JLayeredPane.MODAL_LAYER);
        this.add(finishButton, JLayeredPane.MODAL_LAYER);
        this.add(addShadow, JLayeredPane.PALETTE_LAYER);
        this.add(listShadow, JLayeredPane.PALETTE_LAYER);
        this.add(finishShadow, JLayeredPane.PALETTE_LAYER);
    }
    public int getCenter(int size, boolean isWidth) {
        if (isWidth) {
            return ((width / 2) - (size / 2));
        } else {
            return ((height / 2) - (size / 2));
        }
    }
}

