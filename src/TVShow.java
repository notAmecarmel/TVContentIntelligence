public class TVShow {

    private int id;
    private String name;
    private int numberOfSeasons;
    private int numberOfEpisodes;
    private String originalLanguage;
    private int voteCount;
    private double voteAverage;
    private String overview;
    private boolean adult;

    public TVShow(
            int id,
            String name,
            int numberOfSeasons,
            int numberOfEpisodes,
            String originalLanguage,
            int voteCount,
            double voteAverage,
            String overview,
            boolean adult
    ) {
        this.id = id;
        this.name = name;
        this.numberOfSeasons = numberOfSeasons;
        this.numberOfEpisodes = numberOfEpisodes;
        this.originalLanguage = originalLanguage;
        this.voteCount = voteCount;
        this.voteAverage = voteAverage;
        this.overview = overview;
        this.adult = adult;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getNumberOfSeasons() {
        return numberOfSeasons;
    }

    public int getNumberOfEpisodes() {
        return numberOfEpisodes;
    }

    public String getOriginalLanguage() {
        return originalLanguage;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public String getOverview() {
        return overview;
    }

    public boolean isAdult() {
        return adult;
    }
}