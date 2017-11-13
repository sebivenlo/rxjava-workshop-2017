package restaurant;

// copyleft hom
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Utility class to read menu file as array of strings.
 * @author ode
 * @author hom
 */
public class Input {
    private static BufferedReader reader;

    /**
     * Read the whole file as a list of strings.
     * @param file
     * @return a collection of strings.
     */
    public static Collection<String> getMeals(String file) {
        List<String> menu = new ArrayList<String>();
        try{
            reader = new BufferedReader(new FileReader(file));

            String s = reader.readLine();
            while (null != s) {
                menu.add(s);
                s = reader.readLine();
            }
        }
        catch(FileNotFoundException fnfe){
            fnfe.printStackTrace();
        }
        catch(IOException ioe) {
            ioe.printStackTrace();
        }
       return menu;
    }
  }
