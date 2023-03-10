import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;
import java.io.*;
import java.util.*;

/* TODO (TEMP)
    - Add animation for new information
    - Create executable?
    - Add comments and clean files (TEMPS)
    - Add more characters
    - update github read me
 */

/* TODO (For future versions)
    - MORE CHARACTERS!
    - Scores tracking
    - Sounds
    - Resizeable stage (and nodes)
    - Import to website
 */

/* StarWarsCharacterGuessingGame.java

    Main class for the guessing game, that handles everything from selecting a random character, to displaying
    animations. ALl functions will have proper comments to describe what is done inside each one.

    Created by Aiden Carelse (Jan 2023)

 */
public class StarWarsCharacterGuessingGame extends Application
{
    private static String[] possibleCharacters = null, formattedCharacterNames = null;
    private static String[] data;
    private int turn = 0;

    private Label imageLabel, characterName, invalidGuess = null;
    private Rectangle[] guessFrames;
    private ImageView imageView;
    private List<Label> guesses;
    private Label[] dataLabels;
    private TextFlow endText;
    private TextField field;
    private Rectangle frame;
    private Button submit;

    private Pane layout;
    private Stage stage;

    // Choose a random character and launch the application
    public static void main(String[] args) throws IOException
    {
        data = getRandomCharacter();
        launch(args);
    }

    // Set the game's frame and sub views
    @Override
    public void start(Stage stage) throws Exception
    {
        // Set the main scene
        this.stage = stage;
        stage.setTitle("Star Wars Character Guessing Game");

        layout = new Pane();
        layout.setStyle("-fx-background-color: #FFFFFF");

        double w = 900;
        double h = 825;

        Scene scene = new Scene(layout, w, h);
        stage.getIcons().add(new Image(new FileInputStream("data/star_wars_square.jpeg")));
        stage.setScene(scene);
        stage.show();

        // Set the subviews
        setView();
    }

    // Set up all the application's subviews
    private void setView() throws FileNotFoundException
    {
        setTitle();
        setInformationIcon();
        setImageFrame();
        setInformationLabels();
        setGuessFrames();
        setGuessField();
        setSubmitButton();

        // Display the first set of information
        updateCharacterInformation();
    }

    // Set up the title and logo
    private void setTitle() throws FileNotFoundException
    {
        // Star Wars logo
        ImageView logo = new ImageView(new Image(new FileInputStream("data/star_wars.png")));
        logo.setFitWidth(120);
        logo.setFitHeight(57.2);
        logo.setX(20);
        logo.setY(5);

        // Character Guessing Game title
        Label title = new Label("Character Guessing Game");
        title.setStyle("-fx-font: 28 arial; -fx-font-weight: bold;");
        title.setLayoutX(150);
        title.setLayoutY(5);
        title.setPrefHeight(logo.getFitHeight());
        title.setAlignment(Pos.CENTER_LEFT);

        layout.getChildren().addAll(logo, title);
    }

    // Set up the information button
    private void setInformationIcon() throws FileNotFoundException
    {
        // Set the logo
        ImageView info = new ImageView(new Image(new FileInputStream("data/info.png")));
        info.setFitWidth(35);
        info.setFitHeight(35);
        info.setX(840);
        info.setY(17.5);

        // Create a frame around the logo that handles the hover action
        Label hover = new Label();
        hover.setPrefWidth(35);
        hover.setPrefHeight(35);
        hover.setLayoutX(840);
        hover.setLayoutY(17.5);

        // Create a new stage and panel for the information panel
        Stage infoStage = new Stage();
        infoStage.initOwner(stage);
        infoStage.initStyle(StageStyle.UNDECORATED);

        Pane infoPane = new Pane();
        infoPane.setPrefSize(500, 620);
        infoPane.setStyle("-fx-background-color: white;");
        infoStage.setScene(new Scene(infoPane));

        setInformationPane(infoPane);

        // Mouse hover event handler
        hover.setOnMouseEntered(t ->
        {
            infoStage.setX(stage.getX() + 350);
            infoStage.setY(stage.getY() + 80);
            infoStage.show();
        });

        // Mouse hover exit event handler
        hover.setOnMouseExited(t -> infoStage.hide());

        layout.getChildren().addAll(info, hover);
    }

    // Set up the information panel
    private void setInformationPane(Pane layout)
    {
        // Add a border
        Rectangle border = new Rectangle(0, 0, 500, 620);
        border.setFill(Color.TRANSPARENT);
        border.setStroke(Color.BLACK);
        border.setStrokeWidth(1);

        // Create the information text
        String infoString =
                """
                        Instructions and Information:

                        Welcome to the Star Wars Character Guessing Game! Inspired by Wordle and its many spin-offs, the goal of the game is to guess the randomly chosen Star Wars character in the lowest amount of guesses possible! The current game has a total of 17 characters.

                        At first, all you are given is the character's species, but with every incorrect answer, more information is given. Gender for biological characters or Droid Type for droids (ex: astromech, protocol), Birth Year given in BBY and ABY for Before and After the Battle of Yavin, which takes place during Episode IV: A New Hope (droid birth years are the years they were created and are usually approximations), Homeworld (wherever the character grew up, not necessarily where they were born, for a droid it would not necessarily be where they were built, but where they first worked) and finally their First on Screen Appearance.

                        Once you have successfully guessed the chosen character, or you have run out of guesses, all the information as well as the character's name will be given along with an image and link to their wiki page. You can then press the 'PLAY AGAIN' button to restart the game with another randomly chosen character. Overall, it's pretty simple!


                        Created by Aiden Carelse (January 2023)""";

        Label infoLabel = new Label(infoString);
        infoLabel.setStyle("-fx-font: 16 arial;");
        infoLabel.setLayoutX(10);
        infoLabel.setLayoutY(10);
        infoLabel.setPrefWidth(480);
        infoLabel.setPrefHeight(600);
        infoLabel.setWrapText(true);

        layout.getChildren().addAll(border, infoLabel);
    }

    // Set the character image frame
    private void setImageFrame()
    {
        // Create the frame
        frame = new Rectangle(50, 80, 250, 250);
        frame.setFill(Color.TRANSPARENT);
        frame.setStroke(Color.BLACK);
        frame.setStrokeWidth(2);

        // Create the question mark
        Label qMark = new Label("?");
        qMark.setStyle("-fx-font: 225 arial;");
        qMark.setLayoutX(frame.getX() + 60);
        qMark.setLayoutY(frame.getY());

        layout.getChildren().add(frame);
        layout.getChildren().add(qMark);
    }

    // Set the character's information labels
    private void setInformationLabels()
    {
        // Set up the character's name label
        characterName = new Label("???");
        characterName.setStyle("-fx-font: 28 arial; -fx-font-weight: bold;");
        characterName.setAlignment(Pos.CENTER);
        characterName.setLayoutY(80);
        characterName.setLayoutX(325);
        characterName.setPrefWidth(520);

        layout.getChildren().add(characterName);

        dataLabels = new Label[5];

        // Set up all the additional information labels
        for(int i = 0; i < 5; i++)
        {
            // Create the label
            Label label = new Label(getDataLabel(i+1));
            label.setStyle("-fx-font: 20 arial; -fx-font-weight: bold;");
            label.setLayoutY(140 + (i*40));
            label.setLayoutX(325);

            // Create the data label
            Label information = new Label("???");
            information.setStyle("-fx-font: 18 arial;");
            information.setPrefHeight(label.getHeight());
            information.setPrefWidth(250);
            information.setAlignment(Pos.CENTER_LEFT);
            information.setLayoutY(label.getLayoutY());
            information.setLayoutX(590);

            dataLabels[i] = information;
            layout.getChildren().addAll(label, information);
        }
    }

    // Set up the guess frames
    private void setGuessFrames()
    {
        guessFrames = new Rectangle[5];

        // Five frames for five guesses
        for(int i=0; i<5; i++)
        {
            Rectangle frame = new Rectangle(50, 0, 800, 50);
            frame.setY(350 + (i*60));
            frame.setFill(Color.TRANSPARENT);
            frame.setStroke(Color.BLACK);
            frame.setStrokeWidth(1);

            guessFrames[i] = frame;
            layout.getChildren().add(frame);
        }
    }

    // Set the editable guess field
    private void setGuessField()
    {
        field = new TextField();
        field.setLayoutX(50);
        field.setLayoutY(655);
        field.setPrefWidth(800);
        field.setPrefHeight(50);
        field.setStyle("-fx-font-size: 20px;");
        field.setPromptText("Guess Character");

        // Add auto complete with list of possible characters
        layout.getStylesheets().add("new_style.css");
        AutoCompletionBinding<String> binding = TextFields.bindAutoCompletion(field, formattedCharacterNames);
        binding.setPrefWidth(field.getPrefWidth()/3);

        layout.getChildren().add(field);
    }

    // Set up the submit guess button
    private void setSubmitButton()
    {
        submit = new Button("SUBMIT");
        submit.setPrefWidth(400);
        submit.setPrefHeight(50);
        submit.setLayoutX(250);
        submit.setLayoutY(715);
        submit.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-background-color: #119e16;");
        submit.setTextFill(Color.WHITE);
        submit.setDisable(true);

        guesses = new ArrayList<>();

        // Click event
        submit.setOnAction(actionEvent ->
        {
            // Button either submits a guess or starts a new game
            if(submit.getText().equals("SUBMIT"))
            {
                MakeGuess();
            }
            else
            {
                resetGame();
            }
        });

        // A guess can also be submitted when the enter key is hit on the guess field
        field.setOnKeyPressed(ke ->
        {
            if (ke.getCode().equals(KeyCode.ENTER))
            {
                MakeGuess();
            }
        });

        // Check if the field contains text, if not then disable the submit button
        field.textProperty().addListener((observable, oldValue, newValue) -> submit.setDisable(field.getText().replaceAll("\\s+", "").equals("") && !submit.getText().equals("PLAY AGAIN")));

        layout.getChildren().add(submit);
    }

    // Submit a guess to the game
    private void MakeGuess()
    {
        // Get the guessed character
        String guess = field.getText().toLowerCase();

        // Remove the invalid guess message
        if(invalidGuess != null)
        {
            layout.getChildren().remove(invalidGuess);
        }

        // If the guess is valid, continue
        if(Arrays.asList(possibleCharacters).contains(guess))
        {
            // If the guess is correct
            if(guess.equals(data[0].toLowerCase()))
            {
                // Update the current guess frame
                setGuessFrame(turn - 1, true);
                endGame(true);

                // Show all data
                while(turn < 5)
                {
                    updateCharacterInformation();
                }

                // Show success animation
                showSuccess();
            }
            // If the guess isn't correct
            else
            {
                // Update the current guess frame
                setGuessFrame(turn - 1, false);

                // Give the player more information
                if(turn < 5)
                {
                    updateCharacterInformation();
                }
                // Finish the game if the player ran out o turns
                else
                {
                    endGame(false);
                }

            }
        }
        // If the guess is invalid
        else
        {
            // Display an error message
            invalidGuess = new Label("That character doesn't exist in this game!");
            invalidGuess.setStyle("-fx-font: 18 arial;");
            invalidGuess.setAlignment(Pos.CENTER);
            invalidGuess.setLayoutY(780);
            invalidGuess.setPrefWidth(800);
            invalidGuess.setLayoutX(50);
            invalidGuess.setTextFill(Color.RED);

            layout.getChildren().add(invalidGuess);
        }

        field.setText("");
    }

    // When the character has correctly guessed the character, show animation
    private void showSuccess()
    {
        // Get the confetti gif
        String path = "data/confetti.gif";
        Image image = new Image((new File(path).toURI().toString()));

        // Create images for either sides of the frame
        ImageView leftConfetti = new ImageView(image);
        leftConfetti.setX(-100);

        ImageView rightConfetti = new ImageView(image);
        rightConfetti.setX(750);

        layout.getChildren().addAll(leftConfetti, rightConfetti);

        // Animate the gif so that it plays once and fades out of view
        FadeTransition ft = new FadeTransition(Duration.millis(1000), leftConfetti);
        ft.setFromValue(1.0);
        ft.setToValue(0.0);

        FadeTransition ft2 = new FadeTransition(Duration.millis(1000), rightConfetti);
        ft2.setFromValue(1.0);
        ft2.setToValue(0.0);

        ft.play();
        ft2.play();

        // When the animation ends, remove the left confetti
        ft.statusProperty().addListener((observableValue, oldValue, newValue) ->
        {
            if(newValue == Animation.Status.STOPPED)
            {
                layout.getChildren().remove(leftConfetti);
                layout.getChildren().remove(rightConfetti);
            }
        });
    }

    // Set the current guess frame with the guessed character and corresponding background colour
    private void setGuessFrame(int frame, boolean correct)
    {
        Rectangle current = guessFrames[frame];

        // Display the guess
        Label guess = new Label(field.getText());
        guess.setLayoutX(current.getX() + 15);
        guess.setLayoutY(current.getY());
        guess.setPrefWidth(current.getWidth());
        guess.setPrefHeight(current.getHeight());
        guess.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        guess.setTextFill(Color.WHITE);

        // Display the status as a checkmark of cross
        Label status = new Label(correct ? "???" : "???");
        status.setLayoutX(805);
        status.setLayoutY(current.getY());
        status.setPrefHeight(current.getHeight());
        status.setStyle("-fx-font-size: 28px; -fx-font-weight: bold;");
        status.setTextFill(Color.WHITE);

        guesses.add(guess);
        guesses.add(status);
        layout.getChildren().addAll(guess, status);

        // Display green for correct and red for incorrect
        if(correct)
        {
            current.setFill(Color.GREEN);
        }
        else
        {
            current.setFill(Color.RED);
        }
    }

    // Set the character's image when the game has ended
    private void setCharacterImage()
    {
        Image image = null;

        // Load the image from the database
        try
        {
            image = new Image(data[7].replaceAll("\"", ""));
            imageLabel = null;
        }
        catch (Exception e)
        {
            // If the image can't be loaded, show a default and the character's name
            try
            {
                image = new Image(new FileInputStream("data/blank_pfp.png"));
            }
            catch (Exception ignored) { }

            imageLabel = new Label(data[0]);
            imageLabel.setStyle("-fx-font: 24 arial; -fx-font-weight: bold;");
            imageLabel.setAlignment(Pos.CENTER);
            imageLabel.setLayoutY(frame.getY());
            imageLabel.setLayoutX(frame.getX());
            imageLabel.setPrefWidth(frame.getWidth());
            imageLabel.setPrefHeight(frame.getHeight());
        }

        // Create the image view
        imageView = new ImageView(image);
        imageView.setFitWidth(frame.getWidth());
        imageView.setFitHeight(frame.getHeight());
        imageView.setX(frame.getX());
        imageView.setY(frame.getY());

        frame.toFront();
        layout.getChildren().add(imageView);

        if(imageLabel != null)
        {
            layout.getChildren().add(imageLabel);
        }
    }

    // Finish the game
    private void endGame(boolean success)
    {
        // change the submit button and guess field
        characterName.setText(data[0]);
        submit.setText("PLAY AGAIN");
        field.setText("");

        Hyperlink wikiLink = new Hyperlink("here");
        wikiLink.setPadding(new Insets(0));

        wikiLink.setOnAction(event -> getHostServices().showDocument(data[6]));

        Text status = new Text(success ? "Congratulations" : "Unlucky");

        // Label with the character's wiki page embedded
        endText = new TextFlow(
                status,
                new Text("! Check out " + data[0] + "'s wiki page "),
                wikiLink,
                new Text("!")
        );

        endText.setStyle("-fx-font: 18 arial;");
        endText.setTextAlignment(TextAlignment.CENTER);
        endText.setLayoutY(780);
        endText.setPrefWidth(800);
        endText.setLayoutX(50);

        layout.getChildren().add(endText);

        // Set the character's image
        setCharacterImage();
    }

    // Reset the game
    private void resetGame()
    {
        // Reset the submit button and character name
        submit.setText("SUBMIT");
        characterName.setText("???");

        layout.getChildren().removeAll(endText, imageView, imageLabel);

        // Reset all the guess frames to white
        for(Rectangle r : guessFrames)
        {
            r.setFill(Color.WHITE);
        }

        // Remove all the character guesses
        for(Label l : guesses)
        {
            layout.getChildren().remove(l);
        }

        // Reset all the character's information
        for(Label l : dataLabels)
        {
            l.setText("???");
        }

        // Reset the turn and choose a new character
        try
        {
            turn = 0;
            data = getRandomCharacter();
            updateCharacterInformation();
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    // Provide the player with the next set of character information
    private void updateCharacterInformation()
    {
        // Display the next information
        dataLabels[turn].setText(data[turn+1]);

        int def_font_size = 18;
        int label_max_width = 250;

        Text text = new Text(data[turn+1]);
        text.setFont(new Font("arial", def_font_size));

        // Resize the label's font to fit the information
        while(text.prefWidth(250) > label_max_width)
        {
            def_font_size--;
            text.setFont(new Font("arial", def_font_size));
            dataLabels[turn].setStyle("-fx-font: "+def_font_size+" arial;");
            dataLabels[turn].setTranslateY(1.25*(18 - def_font_size));
        }

        turn++;
    }

    // Select a random character from the database
    private static String[] getRandomCharacter() throws IOException
    {
        // Load the database
        String path = "data/sw_character_data.csv";
        BufferedReader br = new BufferedReader(new FileReader(path));

        // Get a random character index
        int num = getRandomInt((int)new BufferedReader(new FileReader(path)).lines().count());
        boolean setList = possibleCharacters == null;

        // If the lists haven't been set, initialize them
        if(setList)
        {
            possibleCharacters = new String[(int)new BufferedReader(new FileReader(path)).lines().count() - 1];
            formattedCharacterNames = new String[(int)new BufferedReader(new FileReader(path)).lines().count() - 1];
        }

        String[] character = null;
        int index = 0;
        String line;

        // Continuously read database lines
        while((line = br.readLine()) != null)
        {
            // Get the information as a string array
            String[] curr =  line.split(",");

            // Don't add the labels to the characters list
            if(index != 0)
            {
                possibleCharacters[index - 1] = curr[0].toLowerCase();
                formattedCharacterNames[index - 1] = curr[0];
            }

            // If the random index is reached, set the character information
            if(index == num)
            {
                character = curr;

                if(!setList)
                {
                    break;
                }
            }

            index++;
        }

        return character;
    }

    // Get a random integer from 0 to the specified max
    private static int getRandomInt(int max)
    {
        Random r = new Random();
        return r.nextInt(max - 1) + 1;
    }

    // Get the label corresponding with the position
    private String getDataLabel(int a)
    {
        return switch (a)
        {
            case 0 -> "Name";
            case 1 -> "Species";
            case 2 -> "Gender/Droid Type";
            case 3 -> "Birth Year";
            case 4 -> "Homeworld";
            case 5 -> "First Screen Appearance";
            case 6 -> "Wiki Link";
            case 7 -> "Image Link";
            default -> null;
        };
    }
}
