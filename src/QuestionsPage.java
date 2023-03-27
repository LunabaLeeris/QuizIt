import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.*;
import java.util.Timer;

public class QuestionsPage extends JLayeredPane implements MouseListener, ActionListener {
    Random random = new Random();
    ResultPage resultPage;
    HashMap<String, Boolean> booleanToString = new HashMap<>();
    ArrayList<String> definitions = new ArrayList<>(); // list of definitions
    ArrayList<String> answers = new ArrayList<>(); // list of answers
    ArrayList<String> choices = new ArrayList<>(); // list of possible questions
    ArrayList<Integer> chosenQuestionNumbers = new ArrayList<>();
    JTextField answerArea;
    JTextArea definitionArea;
    JLabel answerLabel;
    JLabel[] choicePanels = new JLabel[4];
    JLabel[] tF = new JLabel[2];
    JLabel timeLeft;
    JPanel background = new JPanel();
    JPanel choicesArea = new JPanel();
    JProgressBar timeRemaining;
    StringBuilder longestWord;
    int correctAnswers = 0;
    int answerPlacement;
    int width;
    int height;
    int min;
    int max;
    int increment;
    int questionNumber = 0;
    float millisecondsPassed = 0;
    int secondsPassed = 0;
    int posOfLongestWord = 0;
    int chosenQuestion; // random definition and answer combination
    int chosenQuestionType; // random question type e.g. multiple choices
    int chosenDuration; // random time duration
    boolean timeTrial = false;
    boolean applyToAll = false;
    boolean randomizeTime = false;
    boolean isNotAsking = true;
    boolean added = false;
    boolean choice;
    boolean timerBarScheduled = false;
    boolean isTransitioning = false; // tells whether the screen is validating the answer
    boolean sleep = false;
    // ---------------Thread runner----------
    Timer timer = new Timer();
    Timer timer1 = new Timer();
    TimerTask taskRunner = new TimerTask() {
        @Override
        public void run() {
            if (isNotAsking){
                if (sleep){
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    sleep = false;
                }
                questionNumber++;
                if (questionNumber > definitions.size()){
                    timer.purge();
                    timer.cancel();
                    timer1.purge();
                    timer1.cancel();
                    removeAll();
                    revalidate();
                    repaint();
                    showResult();
                }
                else {
                    randomizeAttributes();
                    updateScreen();
                    isNotAsking = false;
                }
                isTransitioning = false;
            }
        }
    };

    //--------------------------------------
    QuestionsPage(int width, int height){
        this.width = width;
        this.height = height;
        this.increment = (int)(height*.02);
        this.setSize(width, height);
        this.setLayout(null);
        this.increment = (int)(height*.02);

        resultPage = new ResultPage(width, height);

        background.setBackground(new Color(217, 232, 211));
        background.setBounds(0, 0, width, height);
        this.add(background, JLayeredPane.DEFAULT_LAYER);

        adjustComponents();
        initializeHashMaps();

        this.setVisible(true);
    }
    public void adjustComponents(){
        for (int i = 0; i < 4; i++){
            choicePanels[i] = new JLabel();
            choicePanels[i].setBackground(new Color(176, 190, 170));
            choicePanels[i].setFont(new Font("Times New Roman", Font.BOLD, 15));
            choicePanels[i].setHorizontalAlignment(JLabel.CENTER);
            choicePanels[i].setVerticalAlignment(JLabel.CENTER);
            choicePanels[i].setForeground(Color.WHITE);
            choicePanels[i].setOpaque(true);
            choicesArea.add(choicePanels[i]);
        }
        for (int i = 0; i < 2; i++){
            tF[i] = new JLabel();
            tF[i].setBackground(new Color(176, 190, 170));
            tF[i].setFont(new Font("Times New Roman", Font.BOLD, 20));
            tF[i].setHorizontalAlignment(JLabel.CENTER);
            tF[i].setVerticalAlignment(JLabel.CENTER);
            tF[i].setForeground(Color.WHITE);
            tF[i].setOpaque(true);
            tF[i].addMouseListener(this);
            if (i == 0){tF[i].setText("TRUE");}else{tF[i].setText("FALSE");}
        }
    }
    public void initializeHashMaps(){
        booleanToString.put("TRUE", true);
        booleanToString.put("FALSE", false);
    }
    public boolean inList(ArrayList<Integer> chosenQuestionNumbers, int number){
        for (int i: chosenQuestionNumbers){
            if (number == i){
                return true;
            }
        }
        return false;
    }
    public void runGame(){
        timer.scheduleAtFixedRate(taskRunner, 0, 1000/60);
    }
    public void randomizeAttributes(){
        chosenQuestion = random.nextInt(definitions.size());
        while (!chosenQuestionNumbers.isEmpty() && inList(chosenQuestionNumbers, chosenQuestion)){
            chosenQuestion = random.nextInt(definitions.size());
        }
        chosenQuestionNumbers.add(chosenQuestion);
        chosenQuestionType = random.nextInt(choices.size());
        if (applyToAll){
            timeTrial = true;
        }
        else if (randomizeTime){
            timeTrial = random.nextBoolean();
        }
    }
    public void addQuestionToScreen(){
        String methodChosen = choices.get(chosenQuestionType);
        if (Objects.equals(methodChosen, "mc")){
            multipleChoice();
        }
        else if (Objects.equals(methodChosen, "fb")){
            fillInTheBlanks();
        }
        else if (Objects.equals(methodChosen, "tf")){
            trueOrFalse();
        }
        else if (Objects.equals(methodChosen, "wj")){
            wordJumble();
        }
        else if (Objects.equals(methodChosen, "id")){
            identify();
        }
    }
    public void updateScreen(){
        this.removeAll();
        this.add(background);
        this.updateDetails();
        addQuestionToScreen();
        this.revalidate();
        this.repaint();
    }
    public void updateDetails(){
        JLabel questionNumberLabel = new JLabel();
        JLabel questionTypeLabel = new JLabel();

        questionNumberLabel.setHorizontalAlignment(JLabel.CENTER);
        questionNumberLabel.setVerticalAlignment(JLabel.CENTER);
        questionNumberLabel.setText((questionNumber)+"/"+definitions.size());
        questionNumberLabel.setBounds(getCenter((int)(width*.8), true), (int)(height*.02), (int)(width*.15), (int)(height*.065));
        questionNumberLabel.setFont(new Font("Times New Roman", Font.BOLD, 20));
        questionNumberLabel.setBackground(new Color(223, 120, 120));
        questionNumberLabel.setForeground(Color.WHITE);
        questionNumberLabel.setOpaque(true);

        questionTypeLabel.setHorizontalAlignment(JLabel.CENTER);
        questionTypeLabel.setVerticalAlignment(JLabel.CENTER);
        questionTypeLabel.setBounds((int)(width*.7), (int)(height*.02), (int)(width*.2), (int)(height*.065));
        questionTypeLabel.setFont(new Font("Times New Roman", Font.BOLD, 12));
        questionTypeLabel.setBackground(new Color(63, 82, 54));
        questionTypeLabel.setForeground(Color.WHITE);
        questionTypeLabel.setOpaque(true);

        updateQuestionTypeText(questionTypeLabel);
        if (timeTrial) {addTimeTrialDetails();}

        this.add(questionNumberLabel, JLayeredPane.MODAL_LAYER);
        this.add(questionTypeLabel, JLayeredPane.MODAL_LAYER);
    }
    public void updateQuestionTypeText(JLabel questionTypeLabel) {
        String chosen = choices.get(chosenQuestionType);
        if (Objects.equals(chosen, "mc")) {
            questionTypeLabel.setText("<html>MULTIPLE<br/> CHOICES</html>");
        } else if (Objects.equals(chosen, "fb")) {
            questionTypeLabel.setText("<html> FILL IN<br/>THE BLANKS</html>");
        } else if (Objects.equals(chosen, "tf")) {
            questionTypeLabel.setText("<html>TRUE OR<br/> FALSE</html>");
        } else if (Objects.equals(chosen, "id")) {
            questionTypeLabel.setText("<html>IDENTIFY</html>");
        } else if (Objects.equals(chosen, "wj")) {
            questionTypeLabel.setText("<html> WORD<br/>JUMBLE</html>");
        }
    }
    public void addTimeTrialDetails(){
        chosenDuration = random.nextInt(min, max+1);
        timeRemaining = new JProgressBar();
        timeRemaining.setValue(100); // sets the value of the progress bar (0 - 100 percent)
        timeRemaining.setBounds(getCenter((int)(width*.8), true), (int)(height*.12), (int)(width*.8), increment);
        timeRemaining.setForeground(new Color(194, 1, 1)); // changes the color of the moving bar
        timeRemaining.setBackground(new Color(252, 207, 207)); // changes the color of the back of the bar

        timeLeft = new JLabel();
        timeLeft.setBounds(getCenter((int)(width*.2), true), (int)(height*.02), (int)(width*.2), (int)(height*.065));
        timeLeft.setForeground(Color.BLACK);
        timeLeft.setFont(new Font("Times New Roman", Font.BOLD, 30));
        timeLeft.setHorizontalAlignment(JLabel.CENTER);
        timeLeft.setVerticalAlignment(JLabel.CENTER);

        if (!timerBarScheduled) {
            timerBarScheduled = true;
            timer1 = new Timer();
            TimerTask timerBar = new TimerTask() {
                @Override
                public void run() {
                    if (timeTrial && !isTransitioning && !sleep) {
                        secondsPassed++;
                        millisecondsPassed += 100;
                        double value = ((chosenDuration - (millisecondsPassed / 1000)) / chosenDuration) * 100;
                        timeRemaining.setValue((int) value);
                        timeLeft.setText(""+(int) (chosenDuration - (millisecondsPassed / 1000)));
                        if (timeRemaining.getValue() <= 0) {
                            JOptionPane.showMessageDialog(null, "You ran out of time", "FAILED", JOptionPane.WARNING_MESSAGE);
                            resetTimerBar();
                            validateAnswer(null, null, true);
                        }
                    }
                }
            };
            timer1.scheduleAtFixedRate(timerBar, 0, 100);
        }

        this.add(timeLeft, JLayeredPane.MODAL_LAYER);
        this.add(timeRemaining, JLayeredPane.MODAL_LAYER);
    }

    public void resetTimerBar(){
        timer1.purge();
        timer1.cancel();
        millisecondsPassed = 0;
        timerBarScheduled = false;
    }

    public void validateAnswer(JLabel label, String answer, boolean ranOutOfTime){
        isNotAsking = true;
        if (Objects.equals(choices.get(chosenQuestionType),"mc")){
            if (ranOutOfTime){
                choicePanels[answerPlacement].setBackground(new Color(141, 205, 113));
            }
            else if (!Objects.equals(label.getText(), answers.get(chosenQuestion))){
                label.setBackground(new Color(223, 120, 120));
                choicePanels[answerPlacement].setBackground(new Color(141, 205, 113));
            }
            else{
                correctAnswers++;
                label.setBackground(new Color(141, 205, 113));
            }
            choicesArea.repaint();
            choicesArea.revalidate();
        }
        else if (Objects.equals(choices.get(chosenQuestionType),"wj") ||
                Objects.equals(choices.get(chosenQuestionType),"id")){
            answerLabel.setText(answers.get(chosenQuestion));
            answerArea.setEditable(false);
            if (ranOutOfTime){
                answerArea.setBackground(new Color(223, 120, 120));
            }
            else if (!answer.toUpperCase().equals(answers.get(chosenQuestion).toUpperCase())){
                answerArea.setBackground(new Color(223, 120, 120));
            }
            else{
                correctAnswers++;
                answerArea.setBackground(new Color(141, 205, 113));
            }
        }
        else if (Objects.equals(choices.get(chosenQuestionType),"tf")){
            if (ranOutOfTime){
                if (choice){
                    tF[0].setBackground(new Color(141, 205, 113));
                }
                else{
                    tF[1].setBackground(new Color(141, 205, 113));
                }
            }
            else if (choice != booleanToString.get(label.getText())){
                label.setBackground(new Color(223, 120, 120));
                if (Objects.equals(label.getText(), "TRUE")){
                    tF[1].setBackground(new Color(141, 205, 113));
                }
                else{
                    tF[0].setBackground(new Color(141, 205, 113));
                }

            }
            else{
                correctAnswers++;
                label.setBackground(new Color(141, 205, 113));
            }
        }
        else if (Objects.equals(choices.get(chosenQuestionType),"fb")){
            answerArea.setEditable(false);
            String newDefinition = revealAnswerWithQuotation();
            definitionArea.setText(newDefinition);
            if (ranOutOfTime){
                answerArea.setBackground(new Color(223, 120, 120));
            }
            else if (!answer.toUpperCase().equals(String.valueOf(longestWord).toUpperCase())){
                answerArea.setBackground(new Color(223, 120, 120));
            }
            else{
                correctAnswers++;
                answerArea.setBackground(new Color(141, 205, 113));
            }
        }
        sleep = true;
        isNotAsking = true;
        isTransitioning = true;
        resetTimerBar();
        this.revalidate();
        this.repaint();
    }
    public void showResult(){
        resultPage.correct = correctAnswers;
        resultPage.outOf = definitions.size();
        resultPage.time = secondsPassed;
        resultPage.placeResults();
        resultPage.revalidate();
        resultPage.repaint();
        this.add(resultPage, JLayeredPane.PALETTE_LAYER);
    }
    public void adjustLabel(JLabel label, int x, int y, int width, int height, String name, Color color, int fontSize) {
        label.setBounds(x, y, width, height);
        label.setForeground(Color.WHITE);
        label.setText(name);
        label.setBackground(color);
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setVerticalAlignment(JLabel.CENTER);
        label.setFont(new Font("Times New Roman", Font.BOLD, fontSize));
        label.setOpaque(true);
    }
    public String revealAnswerWithQuotation(){
        StringBuilder newDefinition = new StringBuilder(definitions.get(chosenQuestion));
        newDefinition.insert(posOfLongestWord, '"');
        if (posOfLongestWord+longestWord.length() == definitions.get(chosenQuestion).length()){
            newDefinition.append('"');
        }
        else {
            newDefinition.insert(posOfLongestWord + longestWord.length() + 1, '"');
        }
        for (int i = posOfLongestWord+1, j = 0; i < posOfLongestWord+longestWord.length(); i++, j++){
            newDefinition.setCharAt(i, longestWord.charAt(j));
        }
        return String.valueOf(newDefinition);
    }

// ---------------------METHODS DEPENDING ON RANDOMIZED QUESTION TYPE--------------------
    public void multipleChoice(){
        int definitionWidth = (int) (width*.8);
        int definitionHeight = (int) (height*.45);
        int choicesAreaWidth = (int) (width*.6);
        int choicesAreaHeight = (int) (height*.28);

        String definition = definitions.get(chosenQuestion);
        definitionArea = new JTextArea();
        JScrollPane definitionScroll;

        for (JLabel label: choicePanels){
            label.setBackground(new Color(63, 82, 54));
        }

        definitionArea.setBounds(getCenter(definitionWidth, true),
                (int) (height*.15), definitionWidth, definitionHeight);
        definitionArea.setText(definition);
        definitionArea.setFont(new Font("Times New Roman", Font.PLAIN, 20));
        definitionArea.setLineWrap(true);
        definitionArea.setEditable(false);

        definitionScroll = new JScrollPane(definitionArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        definitionScroll.setBounds(definitionArea.getBounds());

        choicesArea.setLayout(new GridLayout(2, 2, increment, increment));
        choicesArea.setBounds(getCenter(choicesAreaWidth, true), definitionArea.getY()+definitionArea.getHeight()+increment, choicesAreaWidth, choicesAreaHeight);
        choicesArea.setBackground(background.getBackground());

        fillChoices();

        choicesArea.revalidate();
        choicesArea.repaint();

        this.add(choicesArea, JLayeredPane.PALETTE_LAYER);
        this.add(definitionScroll, JLayeredPane.PALETTE_LAYER);
    }
    public void fillChoices(){
        ArrayList<Integer> choices = new ArrayList<>();
        answerPlacement = random.nextInt(4);
        choices.add(chosenQuestion);

        for (int i = 0; i < 4; i++){

            if (i != answerPlacement){
                int randChoice = random.nextInt(answers.size());
                while (inList(choices, randChoice)){
                    randChoice = random.nextInt(answers.size());
                }
                choices.add(randChoice);

                choicePanels[i].setText(answers.get(randChoice));
            }
            else{
                choicePanels[i].setText(answers.get(chosenQuestion));
            }
            choicePanels[i].addMouseListener(this);
        }
    }
    public void wordJumble(){
        int definitionWidth = (int) (width*.8);
        int definitionHeight = (int) (height*.45);
        int jumbledAnswerWidth = (int) (width*.4);
        int jumbledAnswerHeight = (int) (height*.1);
        int answerAreaWidth = (int) (width*.36);
        int answerAreaHeight = (int) (height*.1);

        String definition = definitions.get(chosenQuestion);
        String answer = answers.get(chosenQuestion);
        answer = answer.toUpperCase();
        String jumbledAnswer = String.valueOf(jumbleWord(answer));

        answerLabel = new JLabel();
        answerArea = new JTextField();
        definitionArea = new JTextArea();
        JScrollPane definitionScroll;

        definitionArea.setBounds(getCenter(definitionWidth, true),
                (int) (height*.15), definitionWidth, definitionHeight);
        definitionArea.setText(definition);
        definitionArea.setFont(new Font("Times New Roman", Font.PLAIN, 20));
        definitionArea.setLineWrap(true);
        definitionArea.setEditable(false);

        definitionScroll = new JScrollPane(definitionArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        definitionScroll.setBounds(definitionArea.getBounds());

        adjustLabel(answerLabel,getCenter(jumbledAnswerWidth, true), definitionArea.getY()+definitionArea.getHeight()+increment*2,
                jumbledAnswerWidth, jumbledAnswerHeight, jumbledAnswer, new Color(170, 139, 86), 15);
        answerLabel.setBounds(getCenter(jumbledAnswerWidth, true), definitionArea.getY()+definitionArea.getHeight()+increment,
                jumbledAnswerWidth, jumbledAnswerHeight);

        answerArea.setBounds(getCenter(answerAreaWidth, true), answerLabel.getY()+ answerLabel.getHeight()+increment,
                answerAreaWidth, answerAreaHeight);
        answerArea.setText("ANSWER");
        answerArea.setFont(new Font("Times New Roman", Font.PLAIN, 17));
        answerArea.setHorizontalAlignment(JTextField.CENTER);

        answerArea.addActionListener(this);

        this.add(answerArea, JLayeredPane.MODAL_LAYER);
        this.add(answerLabel, JLayeredPane.MODAL_LAYER);
        this.add(definitionScroll, JLayeredPane.PALETTE_LAYER);
    }
    public StringBuilder jumbleWord(String word){
        String jumbledWord1 = new String(new char[word.length()]).replace('\0', '*');
        StringBuilder jumbledWord = new StringBuilder(jumbledWord1);
        for (int i = 0; i < word.length(); i++){
            int placement = random.nextInt(0, word.length());
            while (jumbledWord.charAt(placement) != '*' && word.charAt(i) != '*'){
                placement = random.nextInt(0, word.length());
            }
            jumbledWord.setCharAt(placement, word.charAt(i));
        }
        return jumbledWord;
    }
    public void fillInTheBlanks(){
        int definitionWidth = (int) (width*.8);
        int definitionHeight = (int) (height*.45);
        int jumbledAnswerWidth = (int) (width*.4);
        int jumbledAnswerHeight = (int) (height*.1);
        int answerAreaWidth = (int) (width*.36);
        int answerAreaHeight = (int) (height*.1);

        String definition = definitions.get(chosenQuestion);
        String blankedDefinition = createBlanks(definition);
        String answer = answers.get(chosenQuestion);

        answerLabel = new JLabel();
        answerArea = new JTextField();
        definitionArea = new JTextArea();
        JScrollPane definitionScroll;

        definitionArea.setBounds(getCenter(definitionWidth, true),
                (int) (height*.15), definitionWidth, definitionHeight);
        definitionArea.setText(blankedDefinition);
        definitionArea.setFont(new Font("Times New Roman", Font.PLAIN, 20));
        definitionArea.setLineWrap(true);
        definitionArea.setEditable(false);

        definitionScroll = new JScrollPane(definitionArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        definitionScroll.setBounds(definitionArea.getBounds());

        adjustLabel(answerLabel,getCenter(jumbledAnswerWidth, true), definitionArea.getY()+definitionArea.getHeight()+increment*2,
                jumbledAnswerWidth, jumbledAnswerHeight, answer, new Color(170, 139, 86), 15);
        answerLabel.setBounds(getCenter(jumbledAnswerWidth, true), definitionArea.getY()+definitionArea.getHeight()+increment,
                jumbledAnswerWidth, jumbledAnswerHeight);

        answerArea.setBounds(getCenter(answerAreaWidth, true), answerLabel.getY()+ answerLabel.getHeight()+increment,
                answerAreaWidth, answerAreaHeight);
        answerArea.setText("ANSWER");
        answerArea.setFont(new Font("Times New Roman", Font.PLAIN, 17));
        answerArea.setHorizontalAlignment(JTextField.CENTER);

        answerArea.addActionListener(this);

        this.add(answerArea, JLayeredPane.MODAL_LAYER);
        this.add(answerLabel, JLayeredPane.MODAL_LAYER);
        this.add(definitionScroll, JLayeredPane.PALETTE_LAYER);
    }
    public String createBlanks(String definition){
        StringBuilder modifiedDefinition = new StringBuilder(definition);
        int lengthOfLongestWord = 0;
        int partialCount = 0;
        posOfLongestWord = 0;
        longestWord = new StringBuilder("");

        for (int i = 0; i < modifiedDefinition.length(); i++){
            if (modifiedDefinition.charAt(i)==' ') {
                if (partialCount > lengthOfLongestWord) {
                    lengthOfLongestWord = partialCount;
                    posOfLongestWord = i - lengthOfLongestWord;
                }
                partialCount = 0;
            }
            else{
                partialCount++;
            }
            if (i+1 == modifiedDefinition.length() && partialCount > 0){
                if (partialCount > lengthOfLongestWord){
                    lengthOfLongestWord = partialCount;
                    posOfLongestWord = (i+1)-lengthOfLongestWord;
                    partialCount = 0;
                }
            }
        }
        for (int i = posOfLongestWord; i < posOfLongestWord+lengthOfLongestWord; i++){
            longestWord.append(definition.charAt(i));
            modifiedDefinition.setCharAt(i, '_');
        }
        System.out.println(longestWord);
        return String.valueOf(modifiedDefinition);
    }
    public void trueOrFalse(){
        int definitionWidth = (int) (width*.8);
        int definitionHeight = (int) (height*.45);
        int answerWidth = (int) (width*.4);
        int answerHeight = (int) (height*.1);
        choice = random.nextBoolean();
        String answer;
        String definition = definitions.get(chosenQuestion);

        if (choice){answer = answers.get(chosenQuestion);}
        else{int i = random.nextInt(0, definitions.size());
            while (i == chosenQuestion){i = random.nextInt(0, definitions.size());}
            answer = answers.get(i);}

        answerLabel = new JLabel();
        definitionArea = new JTextArea();
        JScrollPane definitionScroll;

        definitionArea.setBounds(getCenter(definitionWidth, true),
                (int) (height*.15), definitionWidth, definitionHeight);
        definitionArea.setText(definition);
        definitionArea.setFont(new Font("Times New Roman", Font.PLAIN, 20));
        definitionArea.setLineWrap(true);
        definitionArea.setEditable(false);

        definitionScroll = new JScrollPane(definitionArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        definitionScroll.setBounds(definitionArea.getBounds());

        adjustLabel(answerLabel,getCenter(answerWidth, true), definitionArea.getY()+definitionArea.getHeight()+increment*2,
                answerWidth, answerHeight, answer, new Color(170, 139, 86), 15);
        answerLabel.setBounds(getCenter(answerWidth, true), definitionArea.getY()+definitionArea.getHeight()+increment,
                answerWidth, answerHeight);

        tF[0].setBounds(getCenter((int)(width*.45), true), answerLabel.getY()+ answerLabel.getHeight()+increment,
                (int) (width*.215), (int)(height*.15));

        tF[1].setBounds(tF[0].getX()+tF[0].getWidth()+increment, tF[0].getY(),
                (int) (width*.215), (int)(height*.15));

        tF[0].setBackground(new Color(63, 82, 54));
        tF[1].setBackground(new Color(63, 82, 54));

        this.add(tF[0], JLayeredPane.MODAL_LAYER);
        this.add(tF[1], JLayeredPane.MODAL_LAYER);
        this.add(answerLabel, JLayeredPane.MODAL_LAYER);
        this.add(definitionScroll, JLayeredPane.PALETTE_LAYER);
    }
    public void identify(){
        int definitionWidth = (int) (width*.8);
        int definitionHeight = (int) (height*.45);
        int jumbledAnswerWidth = (int) (width*.4);
        int jumbledAnswerHeight = (int) (height*.1);
        int answerAreaWidth = (int) (width*.36);
        int answerAreaHeight = (int) (height*.1);

        String definition = definitions.get(chosenQuestion);

        answerLabel = new JLabel();
        answerArea = new JTextField();
        definitionArea = new JTextArea();
        JScrollPane definitionScroll;

        definitionArea.setBounds(getCenter(definitionWidth, true),
                (int) (height*.15), definitionWidth, definitionHeight);
        definitionArea.setText(definition);
        definitionArea.setFont(new Font("Times New Roman", Font.PLAIN, 20));
        definitionArea.setLineWrap(true);
        definitionArea.setEditable(false);

        definitionScroll = new JScrollPane(definitionArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        definitionScroll.setBounds(definitionArea.getBounds());

        adjustLabel(answerLabel,getCenter(jumbledAnswerWidth, true), definitionArea.getY()+definitionArea.getHeight()+increment*2,
                jumbledAnswerWidth, jumbledAnswerHeight, "???", new Color(170, 139, 86), 15);
        answerLabel.setBounds(getCenter(jumbledAnswerWidth, true), definitionArea.getY()+definitionArea.getHeight()+increment,
                jumbledAnswerWidth, jumbledAnswerHeight);

        answerArea.setBounds(getCenter(answerAreaWidth, true), answerLabel.getY()+ answerLabel.getHeight()+increment,
                answerAreaWidth, answerAreaHeight);
        answerArea.setText("ANSWER");
        answerArea.setFont(new Font("Times New Roman", Font.PLAIN, 17));
        answerArea.setHorizontalAlignment(JTextField.CENTER);

        answerArea.addActionListener(this);

        this.add(answerArea, JLayeredPane.MODAL_LAYER);
        this.add(answerLabel, JLayeredPane.MODAL_LAYER);
        this.add(definitionScroll, JLayeredPane.PALETTE_LAYER);

    }

// --------------------- MOUSE LISTENER STUFF ------------------------------------
    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (added && !isNotAsking && !isTransitioning){
            for (JLabel label: choicePanels){
                if (e.getSource() == label){
                    validateAnswer(label, null, false);
                }
            }
            for (JLabel label: tF){
                if (e.getSource() == label){
                    validateAnswer(label, null, false);
                }
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {
        if (added && !isTransitioning){
            for (JLabel label: choicePanels){
                if (e.getSource() == label){
                    label.setBackground(new Color(104, 141, 96));
                }
            }
            for (JLabel label: tF){
                if (e.getSource() == label){
                    label.setBackground(new Color(104, 141, 96));
                }
            }
        }
    }

    @Override
    public void mouseExited(MouseEvent e) {
        if (added && !isTransitioning){
            for (JLabel label: choicePanels){
                if (e.getSource() == label){
                    label.setBackground(new Color(63, 82, 54));
                }
            }
            for (JLabel label: tF){
                if (e.getSource() == label){
                    label.setBackground(new Color(63, 82, 54));
                }
            }
        }
    }
    public int getCenter(int size, boolean isWidth) {
        if (isWidth) {
            return ((width / 2) - (size / 2));
        } else {
            return ((height / 2) - (size / 2));
        }
    }
// ------------------------- ACTION LISTENERS FOR ENTERING -----------------------
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == answerArea){
            validateAnswer(null, answerArea.getText(), false);
        }
    }
}
