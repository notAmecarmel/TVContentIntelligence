
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.Map;

public class Dashboard extends JFrame {

    private final ContentAnalyzer analyzer;
    private final MediaPlanner planner;

    private JTable resultsTable;
    private DefaultTableModel tableModel;

    private JComboBox<String> languageBox;
    private JTextField ratingField;
    private JTextField votesField;
    private JCheckBox adultCheckBox;

    public Dashboard(List<TVShow> shows) {

        analyzer = new ContentAnalyzer(shows);
        planner = new MediaPlanner(shows);

        setTitle("TV Content Intelligence System");
        setSize(1100, 750);
        setMinimumSize(new Dimension(850, 550));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout(10, 10));

        add(createHeader(), BorderLayout.NORTH);
        add(createMainPanel(), BorderLayout.CENTER);

        setVisible(true);
    }

    // Application header
    private JPanel createHeader() {

        JPanel header = new JPanel(new BorderLayout());
        header.setBorder(
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        );

        JLabel title = new JLabel(
                "TV CONTENT INTELLIGENCE"
        );

        title.setFont(new Font("Arial", Font.BOLD, 24));

        header.add(title, BorderLayout.WEST);

        return header;
    }

    // Main application layout
    private JPanel createMainPanel() {

        JPanel main = new JPanel(new BorderLayout(10, 10));

        main.setBorder(
                BorderFactory.createEmptyBorder(10, 20, 20, 20)
        );

        main.add(createStatsPanel(), BorderLayout.NORTH);
        main.add(createTabs(), BorderLayout.CENTER);

        return main;
    }

    // Dataset statistics
    private JPanel createStatsPanel() {

        JPanel stats = new JPanel(new GridLayout(1, 3, 15, 15));

        int totalShows = analyzer.getTotalShows();

        int languages = analyzer.getShowsByLanguage().size();

        double averageRating = analyzer.getAverageRating();

        stats.add(createStatCard(
                "Total Shows",
                String.format("%,d", totalShows)
        ));

        stats.add(createStatCard(
                "Languages",
                String.valueOf(languages)
        ));

        stats.add(createStatCard(
                "Average Rating",
                String.format("%.2f", averageRating)
        ));

        return stats;
    }

    private JPanel createStatCard(String title, String value) {

        JPanel card = new JPanel(new GridLayout(2, 1));

        card.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                        BorderFactory.createEmptyBorder(15, 15, 15, 15)
                )
        );

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.PLAIN, 14));

        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Arial", Font.BOLD, 26));

        card.add(titleLabel);
        card.add(valueLabel);

        return card;
    }

    // Application tabs
    private JTabbedPane createTabs() {

        JTabbedPane tabs = new JTabbedPane();

        tabs.addTab("Top Content", createTopContentPanel());

        tabs.addTab("Media Planner", createMediaPlannerPanel());

        tabs.addTab("Language Analytics", createLanguagePanel());

        return tabs;
    }

    // Top content analysis
    private JPanel createTopContentPanel() {

        JPanel panel = new JPanel(new BorderLayout(10, 10));

        JButton popularButton = new JButton("Most Popular");
        JButton ratedButton = new JButton("Top Rated");
        JButton votedButton = new JButton("Most Voted");

        JPanel buttons = new JPanel(new FlowLayout());

        buttons.add(popularButton);
        buttons.add(ratedButton);
        buttons.add(votedButton);

        panel.add(buttons, BorderLayout.NORTH);

        JTable table = new JTable();

        DefaultTableModel model = createTableModel();
        table.setModel(model);

        popularButton.addActionListener(e ->
                populateTable(
                        model,
                        analyzer.getMostPopularShows(20)
                )
        );

        ratedButton.addActionListener(e ->
                populateTable(
                        model,
                        analyzer.getTopRatedShows(20)
                )
        );

        votedButton.addActionListener(e ->
                populateTable(
                        model,
                        analyzer.getMostVotedShows(20)
                )
        );

        populateTable(
                model,
                analyzer.getMostPopularShows(20)
        );

        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        return panel;
    }

    // Media planning interface
    private JPanel createMediaPlannerPanel() {

        JPanel panel = new JPanel(new BorderLayout(10, 10));

        JPanel filters = new JPanel(new GridLayout(3, 2, 10, 10));

        filters.setBorder(
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        );

        languageBox = new JComboBox<>();

        languageBox.addItem("All");

        analyzer.getShowsByLanguage()
                .keySet()
                .stream()
                .sorted()
                .forEach(languageBox::addItem);

        ratingField = new JTextField("8.0");
        votesField = new JTextField("5000");

        adultCheckBox = new JCheckBox("Include Adult Content");

        filters.add(new JLabel("Language:"));
        filters.add(languageBox);

        filters.add(new JLabel("Minimum Rating:"));
        filters.add(ratingField);

        filters.add(new JLabel("Minimum Votes:"));
        filters.add(votesField);

        JPanel controls = new JPanel(new BorderLayout());

        controls.add(filters, BorderLayout.CENTER);

        JPanel actions = new JPanel(new FlowLayout());

        JButton analyzeButton = new JButton("Analyze Content");

        actions.add(adultCheckBox);
        actions.add(analyzeButton);

        controls.add(actions, BorderLayout.SOUTH);

        panel.add(controls, BorderLayout.NORTH);

        tableModel = createTableModel();

        resultsTable = new JTable(tableModel);

        panel.add(
                new JScrollPane(resultsTable),
                BorderLayout.CENTER
        );

        analyzeButton.addActionListener(e -> runMediaPlanner());

        return panel;
    }

    // Run the media planner
    private void runMediaPlanner() {

        try {

            String language =
                    (String) languageBox.getSelectedItem();

            if ("All".equals(language)) {
                language = "";
            }

            double minRating =
                    Double.parseDouble(ratingField.getText().trim());

            int minVotes =
                    Integer.parseInt(votesField.getText().trim());

            if (minRating < 0 || minRating > 10 ||
                    minVotes < 0) {
                throw new IllegalArgumentException(
                        "Invalid rating or vote count."
                );
            }

            boolean includeAdult =
                    adultCheckBox.isSelected();

            List<TVShow> recommendations =
                    planner.findContent(
                            language,
                            minRating,
                            minVotes,
                            includeAdult,
                            50
                    );

            populateTable(tableModel, recommendations);

        } catch (IllegalArgumentException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Enter a rating between 0 and 10 " +
                            "and a non-negative integer for votes.",
                    "Invalid Input",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // Language distribution
    private JPanel createLanguagePanel() {

        JPanel panel = new JPanel(new BorderLayout());

        String[] columns = {"Language", "Number of Shows"};

        DefaultTableModel model =
                new DefaultTableModel(columns, 0) {
                    @Override
                    public boolean isCellEditable(
                            int row, int column) {
                        return false;
                    }
                };

        analyzer.getShowsByLanguage()
                .entrySet()
                .stream()
                .sorted(
                        Map.Entry.<String, Long>
                                        comparingByValue()
                                .reversed()
                )
                .forEach(entry ->
                        model.addRow(new Object[]{
                                entry.getKey(),
                                entry.getValue()
                        })
                );

        panel.add(
                new JScrollPane(new JTable(model)),
                BorderLayout.CENTER
        );

        return panel;
    }

    // Reusable table model
    private DefaultTableModel createTableModel() {

        String[] columns = {
                "Show",
                "Language",
                "Rating",
                "Votes",
                "Popularity Score"
        };

        return new DefaultTableModel(columns, 0) {

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
    }

    // Display TV shows in a table
    private void populateTable(
            DefaultTableModel model,
            List<TVShow> shows
    ) {

        model.setRowCount(0);

        for (TVShow show : shows) {

            model.addRow(new Object[]{
                    show.getName(),
                    show.getOriginalLanguage(),
                    show.getVoteAverage(),
                    show.getVoteCount(),
                    String.format(
                            "%.2f",
                            analyzer.calculatePopularityScore(show)
                    )
            });
        }
    }
}
