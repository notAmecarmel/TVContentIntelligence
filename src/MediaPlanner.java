import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class MediaPlanner {

    private final List<TVShow> shows;
    private final ContentAnalyzer analyzer;

    public MediaPlanner(List<TVShow> shows) {
        this.shows = shows;
        this.analyzer = new ContentAnalyzer(shows);
    }

    public List<TVShow> findContent(
            String language,
            double minimumRating,
            int minimumVotes,
            boolean includeAdultContent,
            int limit
    ) {

        return shows.stream()

                // Filter by language
                .filter(show ->
                        language == null ||
                                language.isEmpty() ||
                                show.getOriginalLanguage()
                                        .equalsIgnoreCase(language)
                )

                // Filter by minimum rating
                .filter(show ->
                        show.getVoteAverage() >= minimumRating
                )

                // Filter by minimum engagement
                .filter(show ->
                        show.getVoteCount() >= minimumVotes
                )

                // Filter adult content
                .filter(show ->
                        includeAdultContent || !show.isAdult()
                )

                // Rank by our popularity score
                .sorted(
                        Comparator.comparingDouble(
                                analyzer::calculatePopularityScore
                        ).reversed()
                )

                .limit(limit)

                .collect(Collectors.toList());
    }
    public ContentSegment classifyContent(TVShow show) {

        double rating = show.getVoteAverage();
        int votes = show.getVoteCount();

        if (rating >= 8.0 && votes >= 5000) {
            return ContentSegment.HIGH_PERFORMER;
        }

        if (rating >= 8.0 && votes < 5000) {
            return ContentSegment.EMERGING;
        }

        return ContentSegment.LOW_PERFORMER;
    }
    public List<TVShow> getContentBySegment(
            ContentSegment segment,
            int limit
    ) {

        return shows.stream()
                .filter(show -> classifyContent(show) == segment)
                .sorted(
                        Comparator.comparingDouble(
                                analyzer::calculatePopularityScore
                        ).reversed()
                )
                .limit(limit)
                .collect(Collectors.toList());
    }
}