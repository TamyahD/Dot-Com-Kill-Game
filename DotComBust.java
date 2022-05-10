package RealDotComBustGame;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class DotComBust {
    private GameHelper helper = new GameHelper();
    private ArrayList<DotCom> dotComsList = new ArrayList<>();
    private int numOfGuesses = 0;

    public static void main(String[] args) {
        DotComBust game = new DotComBust();
        game.setUpGame();
        game.startPlaying();
    }

    public void setUpGame() {
        /*
        * create & initialize DotComObjects with names & locations
        * display brief instruction to user
        */
        String briefInstruction = "Your goal is to sink all 3 dot coms. Try to sink them in the fewest number of guesses";
        DotCom dc01 = new DotCom();
        DotCom dc02 = new DotCom();
        DotCom dc03 = new DotCom();
        dc01.setName("mountaindew");
        dc02.setName("carmax");
        dc03.setName("cola");
        dotComsList.add(dc01);
        dotComsList.add(dc02);
        dotComsList.add(dc03);

        System.out.println(briefInstruction);
        System.out.println(dc01.name + ", " + dc02.name + ", " + dc03.name);

        for (DotCom dotComToSet : dotComsList) {
            ArrayList<String> newLocation = helper.placeDotCom(3);
            dotComToSet.setLocationCells(newLocation);
        }
        /*
        * REPEAT FOR EACH DOTCOM OBJECT IN ARRAYLIST:
        * call placeDotCom() method on helper object to get random location for DotCom
        * set location for each DotCom based on result of placeDotCom() call
        */
    }
    private void startPlaying() {
        /*
        * REPEAT WHILE ANY DOTCOM OBJECT STILL EXISTS:
        * ask the user for guesses
        * call checkUserGuess() method
        */
        while (!dotComsList.isEmpty()) {
            String userGuess = helper.getUserInput("Enter a guess: ");
            checkUserGuess(userGuess);
        }
        finishGame();
    }
    private void checkUserGuess(String userGuess) {
        /*
        * loop through remaining DotCom objects
        * call each DotCom object's checkValue() method
        *
        * increment numOfGuesses variable
        * set local 'result' variable to "miss"
        * set result variable to "hit" or "kill" when necessary
        * if result is "kill", remove DotCom object from dotComsList
        * print result to user
        */
        numOfGuesses++;
        String result = "miss";

        for (int x=0; x < dotComsList.size(); x++) {
            result = dotComsList.get(x).checkValue(userGuess);
            
            if (result.equals("hit")) {
                break;
            }
            if (result.equals("kill")) {
                dotComsList.remove(x);
                break;
            }
        }
        System.out.println(result);
    }
    private void finishGame() {
        /*
        * print message about user's performance,
        * how many guesses it took to sink all DotCom objects
        *
        * print game over message
        * if numOfGuesses is "small", print congratulations message
        * else print insulting message
        */
        String endMessage = "All Dot Coms are dead! Your stock is now worthless :)";
        String positiveMessage = "It only took you " + numOfGuesses + "guesses. You got out before your options sank.";
        String negativeMessage = "Took you long enough. " + numOfGuesses + " guesses to be specific...fish got out with your options";

        if (numOfGuesses <= 18) {
            System.out.println(endMessage);
            System.out.println(positiveMessage);
        }
        else {
            System.out.println(endMessage);
            System.out.println(negativeMessage);
        }
    }

}
class DotCom {
    public String name;
    private ArrayList<String> locationCells;

    public void setLocationCells(ArrayList<String> lc) {
        locationCells = lc;
    }
    public void setName(String n) {
        String ext = ".com";
        this.name = n+ext;
    }
    public String checkValue(String userInput) {
        String result = "miss";
        int index = locationCells.indexOf(userInput);
        
        if (index >= 0 ) {
            locationCells.remove(index);
            
            if (locationCells.isEmpty()) {
                result = "kill";
                System.out.println("Wow, you sunk " + name + "!");
            }
            else {
                result = "hit";
            }
        }
        return result;
    }

}
class GameHelper {
    private static final String alph = "abcdefg";
    private int gridLength = 7;
    private int gridSize = 49;
    private int[] grid = new int[gridSize];
    private int comCount = 0;
    public String getUserInput(String prompt) {
        String inputLine = null;

        System.out.println(prompt + " ");

        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            inputLine = br.readLine();
        } catch (IOException e) {
            System.out.println("IOException: " +e);
        }
        return inputLine.toLowerCase();
    }

    public ArrayList<String> placeDotCom(int comSize) {
        ArrayList<String> alphaCells = new ArrayList<String>();

        String temp = null;
        int[] coords = new int[comSize];
        int attempts = 0;
        boolean success = false;
        int location = 0;

        comCount++;
        int incr = 1;

        if ((comCount %2) == 1) {
            incr = gridLength;
        }

        while (!success & attempts++ < 200) {
            location = (int)(Math.random() *gridSize);
            int x = 0;
            success = true;

            while (success && x < comSize) {
                if (grid[location] == 0) {
                    coords[x++] = location;
                    location += incr;

                    if (location >= gridSize) {
                        success = false;
                    }
                    if (x>0 && (location % gridLength == 0)) {
                        success = false;
                    }
                }
                else {
                    success = false;
                }
            }
        }
        int x = 0;
        int row = 0;
        int col = 0;

        while (x < comSize) {
            grid[coords[x]] = 1;
            row = (int)(coords[x] / gridLength);
            col = coords[x] % gridLength;
            temp = String.valueOf(alph.charAt(col));

            alphaCells.add(temp.concat(Integer.toString(row)));
            x++;
        }
        return alphaCells;
    }

}
