import java.util.List;
import java.util.Map;

public class Main {

    public static void main(String[] args) {

        String filePath = "data/tv_shows.csv";

        List<TVShow> shows = CSVReader.readCSV(filePath);

        System.out.println("TV Content Intelligence System");
        System.out.println("--------------------------------");

        ContentAnalyzer analyzer = new ContentAnalyzer(shows);

        // Basic statistics
        System.out.println("Total shows: " +
                analyzer.getTotalShows());

        System.out.println("Average rating: " +
                analyzer.getAverageRating());


        // Top rated
        System.out.println("\nTOP 10 RATED SHOWS");
        System.out.println("-------------------");

        for (TVShow show : analyzer.getTopRatedShows(10)) {

            System.out.println(
                    show.getName()
                            + " | Rating: "
                            + show.getVoteAverage()
                            + " | Votes: "
                            + show.getVoteCount()
            );
        }


        // Most voted
        System.out.println("\nTOP 10 MOST VOTED SHOWS");
        System.out.println("------------------------");

        for (TVShow show : analyzer.getMostVotedShows(10)) {

            System.out.println(
                    show.getName()
                            + " | Votes: "
                            + show.getVoteCount()
                            + " | Rating: "
                            + show.getVoteAverage()
            );
        }


        // Most seasons
        System.out.println("\nTOP 10 SHOWS BY SEASONS");
        System.out.println("-----------------------");

        for (TVShow show : analyzer.getShowsWithMostSeasons(10)) {

            System.out.println(
                    show.getName()
                            + " | Seasons: "
                            + show.getNumberOfSeasons()
            );
        }


        // Most episodes
        System.out.println("\nTOP 10 SHOWS BY EPISODES");
        System.out.println("-----------------------");

        for (TVShow show : analyzer.getShowsWithMostEpisodes(10)) {

            System.out.println(
                    show.getName()
                            + " | Episodes: "
                            + show.getNumberOfEpisodes()
            );
        }


        // Languages
        System.out.println("\nTOP 10 LANGUAGES");
        System.out.println("----------------");

        analyzer.getShowsByLanguage()
                .entrySet()
                .stream()
                .sorted(
                        Map.Entry.<String, Long>
                                        comparingByValue()
                                .reversed()
                )
                .limit(10)
                .forEach(entry ->
                        System.out.println(
                                entry.getKey()
                                        + " : "
                                        + entry.getValue()
                        )
                );


        // Adult distribution
        System.out.println("\nADULT CONTENT DISTRIBUTION");
        System.out.println("--------------------------");

        Map<Boolean, Long> adultDistribution =
                analyzer.getAdultDistribution();

        System.out.println(
                "Non-adult: "
                        + adultDistribution.getOrDefault(false, 0L)
        );

        System.out.println(
                "Adult: "
                        + adultDistribution.getOrDefault(true, 0L)
        );
        System.out.println("\nTOP 10 MOST POPULAR SHOWS");
        System.out.println("-------------------------");

        for (TVShow show : analyzer.getMostPopularShows(10)) {

            System.out.printf(
                    "%s | Rating: %.2f | Votes: %d | Popularity Score: %.2f%n",
                    show.getName(),
                    show.getVoteAverage(),
                    show.getVoteCount(),
                    analyzer.calculatePopularityScore(show)
            );
        }

        System.out.println("\nMEDIA PLANNER");
        System.out.println("-------------");

        MediaPlanner planner = new MediaPlanner(shows);

        List<TVShow> recommendations = planner.findContent(
                "en",      // Language
                8.0,       // Minimum rating
                5000,      // Minimum votes
                false,     // Include adult content?
                10         // Number of results
        );

        System.out.println(
                "Criteria: English | Rating >= 8.0 | Votes >= 5000 | Non-adult"
        );

        System.out.println("\nRECOMMENDED CONTENT");

        for (TVShow show : recommendations) {

            System.out.printf(
                    "%s | Rating: %.2f | Votes: %d | Popularity: %.2f%n",
                    show.getName(),
                    show.getVoteAverage(),
                    show.getVoteCount(),
                    analyzer.calculatePopularityScore(show)
            );
        }

        System.out.println("\nEMERGING CONTENT");
        System.out.println("-----------------");

        List<TVShow> emergingShows =
                planner.getContentBySegment(
                        ContentSegment.EMERGING,
                        10
                );

        for (TVShow show : emergingShows) {

            System.out.printf(
                    "%s | Rating: %.2f | Votes: %d | Popularity: %.2f%n",
                    show.getName(),
                    show.getVoteAverage(),
                    show.getVoteCount(),
                    analyzer.calculatePopularityScore(show)
            );
        }
    }
}