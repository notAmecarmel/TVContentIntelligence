import java.util.*;
import java.util.stream.Collectors;

public class ContentAnalyzer {

    private final List<TVShow> shows;

    public ContentAnalyzer(List<TVShow> shows) {
        this.shows = shows;
    }

    // 1. Total number of shows
    public int getTotalShows() {
        return shows.size();
    }

    // 2. Average rating
    public double getAverageRating() {

        return shows.stream()
                .filter(show -> show.getVoteAverage() > 0)
                .mapToDouble(TVShow::getVoteAverage)
                .average()
                .orElse(0.0);
    }

    // 3. Top-rated shows
    public List<TVShow> getTopRatedShows(int limit) {

        return shows.stream()
                .filter(show -> show.getVoteAverage() > 0)
                .sorted(
                        Comparator.comparingDouble(
                                TVShow::getVoteAverage
                        ).reversed()
                )
                .limit(limit)
                .collect(Collectors.toList());
    }

    // 4. Most-voted shows
    public List<TVShow> getMostVotedShows(int limit) {

        return shows.stream()
                .filter(show -> show.getVoteCount() > 0)
                .sorted(
                        Comparator.comparingInt(
                                TVShow::getVoteCount
                        ).reversed()
                )
                .limit(limit)
                .collect(Collectors.toList());
    }

    // 5. Shows with the most seasons
    public List<TVShow> getShowsWithMostSeasons(int limit) {

        return shows.stream()
                .filter(show -> show.getNumberOfSeasons() > 0)
                .sorted(
                        Comparator.comparingInt(
                                TVShow::getNumberOfSeasons
                        ).reversed()
                )
                .limit(limit)
                .collect(Collectors.toList());
    }

    // 6. Shows with the most episodes
    public List<TVShow> getShowsWithMostEpisodes(int limit) {

        return shows.stream()
                .filter(show -> show.getNumberOfEpisodes() > 0)
                .sorted(
                        Comparator.comparingInt(
                                TVShow::getNumberOfEpisodes
                        ).reversed()
                )
                .limit(limit)
                .collect(Collectors.toList());
    }

    // 7. Number of shows by language
    public Map<String, Long> getShowsByLanguage() {

        return shows.stream()
                .filter(show ->
                        show.getOriginalLanguage() != null &&
                                !show.getOriginalLanguage().isEmpty()
                )
                .collect(
                        Collectors.groupingBy(
                                TVShow::getOriginalLanguage,
                                Collectors.counting()
                        )
                );
    }

    // 8. Adult vs non-adult distribution
    public Map<Boolean, Long> getAdultDistribution() {

        return shows.stream()
                .collect(
                        Collectors.groupingBy(
                                TVShow::isAdult,
                                Collectors.counting()
                        )
                );
    }
}