// TEMP
import com.gluonhq.charm.glisten.control.AutoCompleteTextField;
import javafx.application.Application;
import javafx.application.HostServices;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.*;
import java.net.URL;
import java.util.*;

/* TODO (TEMP)
    - auto complete field
    - fix images, find better method?
    - Add success/failure animations or sounds
    - Leaderboards and scores tracking?
    - Add comments and clean files (TEMPS)
    - Add more characters
    - update github read me
 */

public class StarWarsCharacterGuessingGame extends Application
{
    // TEMP
    private int turn = 0;
    private Label[] dataLabels;
    private static String[] data;
    private TextField field;
    private Rectangle[] guessFrames;
    private Button submit;
    private List<Label> guesses;
    private TextFlow endText;
    private Rectangle frame;
    private ImageView imageView;
    private Label imageLabel;
    private Label characterName;
    private Pane layout;
    private Stage stage;

    // TEMP
    public static void main(String[] args) throws IOException
    {
        data = getRandomCharacter();
        launch(args);
    }

    // TEMP
    @Override
    public void start(Stage stage) throws Exception
    {
        this.stage = stage;
        stage.setTitle("Star Wars Character Guessing Game");

        layout = new Pane();
        layout.setStyle("-fx-background-color: #FFFFFF");
        setView();

        Scene scene = new Scene(layout, 900, 900);
        stage.setScene(scene);
        stage.show();
    }

    // TEMP
    private void setView() throws FileNotFoundException
    {
        setTitle();
        setInformationIcon();
        setImageFrame();
        setInformationLabels();
        setGuessFrames();
        setGuessField();
        setSubmitButton();

        updateCharacterInformation();
    }

    // TEMP
    private void setTitle() throws FileNotFoundException
    {
        ImageView logo = new ImageView(new Image(new FileInputStream("data/star_wars.png")));
        logo.setFitWidth(120);
        logo.setFitHeight(57.2);
        logo.setX(20);
        logo.setY(5);

        Label title = new Label("Character Guessing Game");
        title.setStyle("-fx-font: 28 arial; -fx-font-weight: bold;");
        title.setLayoutX(150);
        title.setLayoutY(5);
        title.setPrefHeight(logo.getFitHeight());
        title.setAlignment(Pos.CENTER_LEFT);

        layout.getChildren().addAll(logo, title);
    }

    // TEMP
    private void setInformationIcon() throws FileNotFoundException
    {
        ImageView info = new ImageView(new Image(new FileInputStream("data/info.png")));
        info.setFitWidth(35);
        info.setFitHeight(35);
        info.setX(840);
        info.setY(17.5);

        Label hover = new Label();
        hover.setPrefWidth(35);
        hover.setPrefHeight(35);
        hover.setLayoutX(840);
        hover.setLayoutY(17.5);

        Stage infoStage = new Stage();
        infoStage.initOwner(stage);
        infoStage.initStyle(StageStyle.UNDECORATED);

        Pane infoPane = new Pane();
        infoPane.setPrefSize(500, 530);
        infoPane.setStyle("-fx-background-color: white;");
        infoStage.setScene(new Scene(infoPane));

        setInformationPane(infoPane);

        hover.setOnMouseEntered(new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(MouseEvent t)
            {
                infoStage.show();
            }
        });

        hover.setOnMouseExited(new EventHandler<MouseEvent>()
        {

            @Override
            public void handle(MouseEvent t)
            {
                infoStage.hide();
            }
        });

        layout.getChildren().addAll(info, hover);
    }

    // TEMP
    private void setInformationPane(Pane layout)
    {
        Rectangle border = new Rectangle(0, 0, 500, 530);
        border.setFill(Color.TRANSPARENT);
        border.setStroke(Color.BLACK);
        border.setStrokeWidth(1);

        String infoString =
                "Instructions and Information:" +
                "\n\n" +
                "Welcome to the Star Wars Character Guessing Game! Inspired by Wordle and its many spinoffs, the goal of the game is to guess the randomly chosen Star Wars character in the lowest amount of guesses possible! The current game has a total of 3 possible characters.\n" +
                "\n" +
                "At first, all you are given is the character's species, but with every incorrect answer, more information is given. Gender for biological characters or Droid Type for droids (ex: astromech, protocol), Birth Year given in BBY (Before the Battle of Yavin) and ABY (After the Battle of Yavin), Homeworld (wherever the character was born, not where they grew up) and finally their First on Screen Apperance.\n" +
                "\n" +
                "Once you have succesfully guessed the chosen character, or you have run out of guesses, all the information as well as the character's name will be given along with an image and link to their wiki page. You can then press the 'PLAY AGAIN' button to restart the game with another randomly chosen character. Overall, it's pretty simple!\n" +
                "\n\n" +
                "Created by Aiden Carelse (January 2023)";

        Label infoLabel = new Label(infoString);
        infoLabel.setStyle("-fx-font: 16 arial;");
        infoLabel.setLayoutX(10);
        infoLabel.setLayoutY(10);
        infoLabel.setPrefWidth(480);
        infoLabel.setPrefHeight(510);
        infoLabel.setWrapText(true);

        layout.getChildren().addAll(border, infoLabel);
    }

    // TEMP
    private void setImageFrame()
    {
        frame = new Rectangle(50, 80, 250, 250);
        frame.setFill(Color.TRANSPARENT);
        frame.setStroke(Color.BLACK);
        frame.setStrokeWidth(2);

        Label qMark = new Label("?");
        qMark.setStyle("-fx-font: 225 arial;");
        qMark.setLayoutX(frame.getX() + 60);
        qMark.setLayoutY(frame.getY());

        layout.getChildren().add(frame);
        layout.getChildren().add(qMark);
    }

    //TEMP
    private void setInformationLabels()
    {
        characterName = new Label("???");
        characterName.setStyle("-fx-font: 28 arial; -fx-font-weight: bold;");
        characterName.setAlignment(Pos.CENTER);
        characterName.setLayoutY(80);
        characterName.setLayoutX(325);
        characterName.setPrefWidth(520);

        layout.getChildren().add(characterName);

        dataLabels = new Label[5];

        for(int i = 0; i < 5; i++)
        {
            Label label = new Label(getDataLabel(i+1));
            label.setStyle("-fx-font: 20 arial; -fx-font-weight: bold;");
            label.setLayoutY(140 + (i*40));
            label.setLayoutX(325);

            Label information = new Label("???");
            information.setStyle("-fx-font: 18 arial;");
            information.setPrefHeight(label.getHeight());
            information.setAlignment(Pos.CENTER_LEFT);
            information.setLayoutY(label.getLayoutY());
            information.setLayoutX(590);

            dataLabels[i] = information;
            layout.getChildren().addAll(label, information);
        }
    }

    // TEMP
    private void setGuessFrames()
    {
        guessFrames = new Rectangle[5];

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

    // TEMP
    private void setGuessField()
    {
        field = new TextField();
        field.setLayoutX(50);
        field.setLayoutY(655);
        field.setPrefWidth(800);
        field.setPrefHeight(50);
        field.setStyle("-fx-font-size: 20px;");
        field.setPromptText("Guess Character");

        layout.getChildren().add(field);
    }

    // TEMP
    private void setSubmitButton()
    {
        submit = new Button("SUBMIT");
        submit.setPrefWidth(400);
        submit.setPrefHeight(50);
        submit.setLayoutX(250);
        submit.setLayoutY(715);
        submit.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-background-color: #119e16;");
        submit.setTextFill(Color.WHITE);

        guesses = new ArrayList<>();

        submit.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent actionEvent)
            {
                if(submit.getText().equals("SUBMIT"))
                {
                    MakeGuess();
                }
                else
                {
                    resetGame();
                }
            }
        });

        layout.getChildren().add(submit);
    }

    // TEMP
    private void MakeGuess()
    {
        String guess = field.getText();

        if(guess.equals(data[0]))
        {
            setGuessFrame(turn - 1, true);
            endGame(true);

            while(turn < 5)
            {
                updateCharacterInformation();
            }
        }
        else
        {
            setGuessFrame(turn - 1, false);

            if(turn < 5)
            {
                updateCharacterInformation();
            }
            else
            {
                endGame(false);
            }

        }

        field.setText("");
    }

    // TEMP
    private void setGuessFrame(int frame, boolean correct)
    {
        Rectangle current = guessFrames[frame];

        Label guess = new Label(field.getText());
        guess.setLayoutX(current.getX() + 15);
        guess.setLayoutY(current.getY());
        guess.setPrefWidth(current.getWidth());
        guess.setPrefHeight(current.getHeight());
        guess.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        guess.setTextFill(Color.WHITE);

        guesses.add(guess);
        layout.getChildren().add(guess);

        if(correct)
        {
            current.setFill(Color.GREEN);
        }
        else
        {
            current.setFill(Color.RED);
        }
    }

    // TEMP
    private void setCharacterImage()
    {
        Image image = null;

        try
        {
             image = new Image(data[7]);
             imageLabel = null;
        }
        catch (Exception e)
        {
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

    // TEMP
    private void endGame(boolean success)
    {
        characterName.setText(data[0]);
        submit.setText("PLAY AGAIN");
        field.setText("");

        Hyperlink wikiLink = new Hyperlink("here");
        wikiLink.setPadding(new Insets(0));

        wikiLink.setOnAction(event ->
        {
            getHostServices().showDocument(data[6]);
        });

        Text status = new Text(success ? "Congratulations" : "Unlucky");

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
        setCharacterImage();
    }

    // TEMP
    private void resetGame()
    {
        submit.setText("SUBMIT");
        characterName.setText("???");

        layout.getChildren().removeAll(endText, imageView, imageLabel);

        for(Rectangle r : guessFrames)
        {
            r.setFill(Color.WHITE);
        }

        for(Label l : guesses)
        {
            layout.getChildren().remove(l);
        }

        for(Label l : dataLabels)
        {
            l.setText("???");
        }

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

    // TEMP
    private void updateCharacterInformation()
    {
        dataLabels[turn].setText(data[turn+1]);
        turn++;
    }

    // TEMP
    private static String[] getRandomCharacter() throws IOException
    {
        String path = "data/sw_character_data.csv";
        BufferedReader br = new BufferedReader(new FileReader(path));

        int num = getRandomInt((int)br.lines().count());
        br = new BufferedReader(new FileReader(path));

        int index = 0;
        String line = "";

        while((line = br.readLine()) != null)
        {
            if(index == num)
            {
                return line.split(",");
            }

            index++;
        }

        return null;
    }

    // TEMP
    private static int getRandomInt(int max)
    {
        Random r = new Random();
        return r.nextInt(max - 1) + 1;
    }

    // TEMP
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
