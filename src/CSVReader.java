import java.io.*;
import java.util.*;

public class CSVReader {

    public static List<TVShow> readCSV(String filePath) {

        List<TVShow> shows = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {

            // Skip header
            String line = br.readLine();

            while ((line = br.readLine()) != null) {

                try {
                    List<String> fields = parseCSVLine(line);

                    // Make sure the row has all required fields
                    if (fields.size() < 10) {
                        continue;
                    }

                    int id = Integer.parseInt(fields.get(0));
                    String name = fields.get(1);

                    int seasons = parseInt(fields.get(2));
                    int episodes = parseInt(fields.get(3));

                    String language = fields.get(4);

                    int voteCount = parseInt(fields.get(5));
                    double voteAverage = parseDouble(fields.get(6));

                    String overview = fields.get(7);

                    boolean adult = Boolean.parseBoolean(fields.get(8));

                    TVShow show = new TVShow(
                            id,
                            name,
                            seasons,
                            episodes,
                            language,
                            voteCount,
                            voteAverage,
                            overview,
                            adult
                    );

                    shows.add(show);

                } catch (Exception e) {
                    // Skip malformed rows
                }
            }

        } catch (IOException e) {

            System.out.println("Error reading CSV file:");
            e.printStackTrace();
        }

        return shows;
    }


    // Handles commas inside quoted CSV fields
    private static List<String> parseCSVLine(String line) {

        List<String> fields = new ArrayList<>();

        StringBuilder currentField = new StringBuilder();

        boolean insideQuotes = false;

        for (int i = 0; i < line.length(); i++) {

            char c = line.charAt(i);

            if (c == '"') {

                insideQuotes = !insideQuotes;

            } else if (c == ',' && !insideQuotes) {

                fields.add(currentField.toString().trim());
                currentField.setLength(0);

            } else {

                currentField.append(c);
            }
        }

        // Add final field
        fields.add(currentField.toString().trim());

        return fields;
    }


    private static int parseInt(String value) {

        if (value == null || value.isEmpty()) {
            return 0;
        }

        return Integer.parseInt(value);
    }


    private static double parseDouble(String value) {

        if (value == null || value.isEmpty()) {
            return 0.0;
        }

        return Double.parseDouble(value);
    }
}