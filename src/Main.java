
import javax.swing.SwingUtilities;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        String filePath = "data/tv_shows.csv";

        System.out.println("Loading TV show dataset...");

        List<TVShow> shows = CSVReader.readCSV(filePath);

        System.out.println(
                "Loaded " + shows.size() + " TV shows."
        );

        if (shows.isEmpty()) {
            System.err.println("No TV shows loaded. Check CSV path.");
            return;
        }

        SwingUtilities.invokeLater(() -> {
            new Dashboard(shows);
        });
    }
}
