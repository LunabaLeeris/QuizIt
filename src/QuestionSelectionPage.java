import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.Random;

public class QuestionSelectionPage extends JLayeredPane implements MouseListener {
    Random random = new Random();
    int width;
    int height;
    int min = 10; // default min
    int max = 20; // default max
    boolean added = false;
    boolean ttButtonAdded = false;
    boolean customizeAreaAdded = false;
    JButton listButton = new JButton();
    JButton startButton = new JButton();
    JPanel customizeArea = new JPanel();
    JTextField customMin = new JTextField();
    JTextField customMax = new JTextField();
    JButton update = new JButton();
    JPanel listShadow = new JPanel();
    JPanel startShadow = new JPanel();
    JPanel questionTypePanel = new JPanel();
    JPanel ttButtonsPanel = new JPanel();
    HashMap<String, Boolean> questionTypes = new HashMap<>();
    HashMap<String, JPanel> questionButtonType = new HashMap<>();
    HashMap<String, Color> questionPanelColor = new HashMap<>();
    ArrayList<String> choices = new ArrayList<>();
    QuestionSelectionPage(int width, int height){
        this.width = width;
        this.height = height;
        this.setSize(width, height);

        this.setBackground(new Color(217, 232, 211));
        JPanel background = new JPanel();
        background.setBounds(0, 0, width, height);
        background.setBackground(new Color(217, 232, 211));
        this.add(background, JLayeredPane.DEFAULT_LAYER);
        this.setLayout(null);

        adjustComponents();
        addComponents();

        this.setVisible(true);
    }
    public void adjustComponents(){
        int listBWidth = (int)(width*.2);
        int listBHeight = (int)(height*.15);
        int increment = (int)(height*.02);

        initializeHashMap();
        addLabels();

        listButton.setBounds((int)(width*.5), (int)(height*.7), listBWidth, listBHeight);
        listButton.setText("LIST");
        listButton.setForeground(Color.WHITE);
        listButton.setBackground(new Color(141, 205, 113));
        listButton.setFont(new Font("Times New Roman", Font.BOLD, 20));

        listShadow.setBounds(listButton.getX(), listButton.getY()+increment, listBWidth, listBHeight);
        listShadow.setBackground(new Color(23, 66, 4));

        startButton.setBounds(listButton.getX()+listBWidth+increment, listButton.getY(), listBWidth, listBHeight);
        startButton.setText("START");
        startButton.setForeground(Color.WHITE);
        startButton.setFont(new Font("Times New Roman", Font.BOLD, 20));
        startButton.setBackground(new Color(223, 120, 120));

        startShadow.setBounds(startButton.getX(), startButton.getY()+increment, listBWidth, listBHeight);
        startShadow.setBackground(new Color(109, 10, 10));

        questionTypePanel.setLayout(new GridLayout(3, 2, increment*2, increment*2));
        questionTypePanel.setBackground(new Color(217, 232, 211));
        questionTypePanel.setBounds((int)(width*.05), (int)(height*.05), (int)(width*.4), (int)(height*.8));

        adjustQType();

        questionButtonType.get("tt").setBounds((int)(width*.5), (int)(height*.05), (int)(width*.4), (int)(height*.3));
        questionButtonType.get("tt").setBackground(new Color(176, 190, 170));
        questionButtonType.get("tt").addMouseListener(this);

        ttButtonsPanel.setBounds((int)(width*.6),(int)(height*.37),  (int)(width*.22),(int)(height*.27));
        ttButtonsPanel.setLayout(new GridLayout(3, 1, increment, increment));
        ttButtonsPanel.setBackground(new Color(217, 232, 211));

        customMin.setText("min");
        customMin.setHorizontalAlignment(JTextField.CENTER);
        customMin.setBounds(0, 0, (int)(width*.1), (int)(height*.05));

        customMax.setText("max");
        customMax.setHorizontalAlignment(JTextField.CENTER);
        customMax.setBounds(customMin.getWidth()+increment, 0, (int)(width*.1), (int)(height*.05));

        update.setBounds(increment, (int)(height*.06), (int)(width*.18),(int)(height*.05));
        update.setForeground(Color.WHITE);
        update.setBackground(new Color(141, 205, 113));
        update.setFont(new Font("Times New Roman", Font.BOLD, 12));
        update.setText("UPDATE");
        update.addMouseListener(this);

        customizeArea.setLayout(null);
        customizeArea.setBounds((int)(width*.7), (int)(height*.4), (int)(width*.22), (int)(height*.12));
        customizeArea.setBackground(new Color(23, 66, 4, 204));

    }
    public void addComponents(){
        this.add(listButton, JLayeredPane.PALETTE_LAYER);
        this.add(startButton, JLayeredPane.PALETTE_LAYER);
        this.add(listShadow, JLayeredPane.PALETTE_LAYER);
        this.add(startShadow, JLayeredPane.PALETTE_LAYER);
        this.add(questionButtonType.get("tt"), JLayeredPane.PALETTE_LAYER);
        customizeArea.add(customMin); customizeArea.add(customMax); customizeArea.add(update);
        for (String key: questionButtonType.keySet()) {
            if (Objects.equals(key ,"al") || Objects.equals(key ,"cs")
                    || Objects.equals(key ,"aatrm")){
                questionButtonType.get(key).addMouseListener(this);
                ttButtonsPanel.add(questionButtonType.get(key));
            }
            else if (!Objects.equals(key, "tt")) {
                questionButtonType.get(key).addMouseListener(this);
                questionTypePanel.add(questionButtonType.get(key));
            }
        }
        this.add(questionTypePanel, JLayeredPane.PALETTE_LAYER);

    }
    public void initializeHashMap(){
        questionTypes.put("mc", false); // mc = multiple choices
        questionTypes.put("fb", false); // fb = fill in the blanks
        questionTypes.put("arm", false); // arm = random
        questionTypes.put("tf", false); // tf = true or false
        questionTypes.put("wj", false); // wj = word jumble
        questionTypes.put("id", false); // id = identification
        questionTypes.put("tt", false); // tt = time trial
        questionTypes.put("al", false); // al = apply to all
        questionTypes.put("cs", false); // cs = custom duration
        questionTypes.put("aatrm", false); // atrm = random time duration

        questionButtonType.put("mc", new JPanel(new BorderLayout()));questionButtonType.put("arm", new JPanel(new BorderLayout()));
        questionButtonType.put("fb", new JPanel(new BorderLayout()));questionButtonType.put("tf", new JPanel(new BorderLayout()));
        questionButtonType.put("wj", new JPanel(new BorderLayout()));questionButtonType.put("id", new JPanel(new BorderLayout()));
        questionButtonType.put("tt", new JPanel(new BorderLayout()));questionButtonType.put("al", new JPanel(new BorderLayout()));
        questionButtonType.put("cs", new JPanel(new BorderLayout()));questionButtonType.put("aatrm", new JPanel(new BorderLayout()));

        questionPanelColor.put("mc", new Color(176, 190, 170));questionPanelColor.put("arm", new Color(176, 190, 170));
        questionPanelColor.put("fb", new Color(176, 190, 170));questionPanelColor.put("tf", new Color(176, 190, 170));
        questionPanelColor.put("wj", new Color(176, 190, 170));questionPanelColor.put("id", new Color(176, 190, 170));
        questionPanelColor.put("tt", new Color(176, 190, 170));questionPanelColor.put("al", new Color(176, 190, 170));
        questionPanelColor.put("cs", new Color(176, 190, 170));questionPanelColor.put("aatrm", new Color(176, 190, 170));

    }
    public void addLabels(){
        for (String key: questionButtonType.keySet()){
            JLabel text = new JLabel("", SwingConstants.CENTER);
            text.setVerticalAlignment(JLabel.CENTER);
            text.setFont(new Font("Times New Roman", Font.BOLD, 15));
            text.setForeground(Color.WHITE);
            if (Objects.equals(key, "mc")){
                text.setText("<html>MULTIPLE<br/> CHOICES</html>");
            }
            else if (Objects.equals(key, "fb")){
                text.setText("<html> FILL IN<br/>THE BLANKS</html>");
            }
            else if (Objects.equals(key, "arm")){
                text.setText("<html>RANDOM</html>");
            }
            else if (Objects.equals(key, "tf")){
                text.setText("<html>TRUE OR<br/> FALSE</html>");
            }
            else if (Objects.equals(key, "id")){
                text.setText("<html>IDENTIFY</html>");
            }
            else if (Objects.equals(key, "wj")){
                text.setText("<html> WORD<br/>JUMBLE</html>");
            }
            else {
                text.setFont(new Font("Times New Roman", Font.BOLD, 20));
                if (Objects.equals(key, "tt")) {
                    text.setText("<html>TIME<br/>TRIAL</html>");
                }
                else{
                    text.setFont(new Font("Times New Roman", Font.BOLD, 10));
                     if (Objects.equals(key, "al")) {
                        text.setText("<html>APPLY ALL</html>");
                    } else if (Objects.equals(key, "cs")) {
                        text.setText("<html>CUSTOMIZE</html>");
                    } else if (Objects.equals(key, "aatrm")) {
                        text.setText("<html>RANDOMIZE</html>");
                    }
                }
            }

            questionButtonType.get(key).add(text);
        }
    }
    public void adjustQType(){
        for (JPanel item: questionButtonType.values()){
            item.setBackground(new Color(176, 190, 170));
        }
    }
    public void fillChoice(){
        for (String key: questionTypes.keySet()){
            if (questionTypes.get(key) && (!Objects.equals(key, "tt")&&!Objects.equals(key, "al")
                    &&!Objects.equals(key, "cs")&&!Objects.equals(key, "aatrm"))){
                choices.add(key);
            }
        }
    }
    public void addTTButtons(){
        if (ttButtonAdded) {
            this.add(ttButtonsPanel,JLayeredPane.MODAL_LAYER);
        }
        else{
            this.remove(ttButtonsPanel);
        }
        this.repaint();
        this.revalidate();
    }
    public void addCustomizeArea(){
        if (customizeAreaAdded) {
            this.add(customizeArea, JLayeredPane.POPUP_LAYER);
        }
        else{
            this.remove(customizeArea);
        }
        this.repaint();
        this.revalidate();
    }
    public boolean validateMinMax(){
        boolean isValid = true;
        try {
            int partialMin = Integer.parseInt(customMin.getText());
            int partialMax = Integer.parseInt(customMax.getText());
            min = partialMin;
            max = partialMax;
        }
        catch(NumberFormatException e){
            JOptionPane.showMessageDialog(null, "Please Input An Integer", "", JOptionPane.WARNING_MESSAGE);
            isValid = false;
        }
        System.out.println(min);
        System.out.println(max);
        return isValid;
    }
    public void setOff(String key){
        questionTypes.put(key, false);
        questionButtonType.get(key).setBackground(new Color(176, 190, 170));
        questionPanelColor.put(key, new Color(176, 190, 170));
    }
    public void setOn(String key){
        questionTypes.put(key, true);
        questionButtonType.get(key).setBackground(new Color(223, 120, 120));
        questionPanelColor.put(key, new Color(223, 120, 120));
    }
    public void randomizeQuestionTypes(){
        String[] viableKeys = {"mc", "fb", "tf", "id", "wj"};
        for (String key: viableKeys){
            boolean choice = random.nextBoolean();
            if (choice){setOff(key);}else{setOn(key);}
        }
    }
// -------------------MOUSE LISTENER STUFF------------------------
    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (added) {
            for (String key: questionButtonType.keySet()) {
                if (e.getSource() == questionButtonType.get(key)){
                    if (!questionTypes.get(key) && Objects.equals(key, "al")){
                        setOff("aatrm");
                    }
                    else if (!questionTypes.get(key) && Objects.equals(key, "aatrm")){
                        setOff("al");
                    }
                    else if (Objects.equals(key, "cs")){
                        customizeAreaAdded = !customizeAreaAdded;
                        addCustomizeArea();
                    }
                    else if (Objects.equals(key, "tt") && questionTypes.get("tt")){
                        setOff("al");setOff("aatrm");
                    }
                    if (Objects.equals(key, "arm")){
                        questionButtonType.get(key).setBackground(new Color(223, 120, 120));
                        randomizeQuestionTypes();
                    }
                    else {
                        if (Objects.equals(key, "tt")) {
                            ttButtonAdded = !ttButtonAdded;
                            addTTButtons();
                        }
                        if (!questionTypes.get(key)) {
                            setOn(key);

                        } else {
                            setOff(key);
                        }
                    }
                }
            }
            if (e.getSource() == update) {
                if (validateMinMax()) {
                    this.remove(customizeArea);
                    customizeAreaAdded = false;
                    questionTypes.put("cs", false);
                    questionButtonType.get("cs").setBackground(new Color(176, 190, 170));
                    questionPanelColor.put("cs", new Color(176, 190, 170));
                    this.repaint();
                }
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (e.getSource() == questionButtonType.get("arm")){
            questionButtonType.get("arm").setBackground(new Color(176, 190, 170));
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        if (added) {
            for (String key: questionButtonType.keySet()) {
                if (e.getSource() == questionButtonType.get(key)) {
                    if (questionTypes.get(key)) {
                        questionButtonType.get(key).setBackground(new Color(235, 191, 191));
                    }
                    else {
                        questionButtonType.get(key).setBackground(new Color(196, 232, 181));
                    }

                }
            }
        }
    }

    @Override
    public void mouseExited(MouseEvent e) {
        if (added) {
            for (String key: questionButtonType.keySet()) {
                if (e.getSource() == questionButtonType.get(key)) {
                    questionButtonType.get(key).setBackground(questionPanelColor.get(key));
                }
            }
        }
    }
}
