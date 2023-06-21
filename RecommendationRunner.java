import java.util.ArrayList;

public class RecommendationRunner implements Recommender{

    private FourthRatings fourthRatings;

    public RecommendationRunner() {
        MovieDatabase.initialize("ratedmoviesfull.csv");
        RaterDatabase.initialize("ratings.csv");
        fourthRatings = new FourthRatings();
    }

    @Override
    public ArrayList<String> getItemsToRate() {
        System.out.println("<p style=\"text-align:right\">Owned by Bishal GC</p>");
        ArrayList<String> itemToRate = new ArrayList<>();
        int year = 2013;
        int minimalRaters = 40;
        Filter filter = new YearAfterFilter(year);
        int count = 0;
        for(Rating r : fourthRatings.getAverageRatingsByFilter(minimalRaters, filter)) {
            if(count++ < 15) {
                itemToRate.add(r.getItem());
            }
        }
        return itemToRate;
    }

    @Override
    public void printRecommendationsFor(String webRaterID) {
        int minimalRaters = 1;
        int similarRaters = 40;
        
        ArrayList<Rating> ratings = fourthRatings.getSimilarRatings(webRaterID, similarRaters, minimalRaters);
        
        if(ratings.isEmpty()) {
            System.out.println("<div style=\"display:block;font-size:40px;font-weight:900;color:red;text-align:center;\">No Recommendations</div>");
            System.out.println("<div style=\"display:block;font-size:17px;text-align:center;\">Insufficient Minimal Raters or Movie Ratings!</div>");
            return;
        }
        
        System.out.println("<style>\ntable {" +
            "font-family: arial, sans-serif;" +
            "border-collapse: collapse;" +
            "width: 100%;}" + 
            "\ntd, th {\n" +
            "border: 1px solid #FFFFFF;" +
            "text-align: left;" +
            "font-size: 20px;" +
            "padding: 8px;}\n" +
            "tr:nth-child(even) {" + 
            "background-color: #d9d9f3;}\n</style>");
        System.out.println("<table>");
        int count = 0;
        System.out.printf("<tr><th>%s</th><th>%s</th><th>%s</th><th>%s</th><th>%s</th><th>%s</th><th>%s</th></tr>\n", "Poster", "Title", "Year", "Genre", "Director", "Minutes", "Country");
        for (Rating r: ratings) {
            if (count++ > 15)
                break;
            Movie m = MovieDatabase.getMovie(r.getItem());
            System.out.printf("<tr>\n\t" +
                    "<td><img src=\"%s\" alt=\" N/A\" height=\"200\"/></td>" +
                    "<td>%s</td>" +
                    "<td>%d</td>" +
                    "<td>%s</td>" +
                    "<td>%s</td>" +
                    "<td>%d</td>" +
                    "<td>%s</td>" +
                    "\n</tr>\n", m.getPoster(), m.getTitle(), m.getYear(), m.getGenres(), m.getDirector(), m.getMinutes(), m.getCountry());
        }
        System.out.println("</table>");
    }

    public static void main(String[] args) {
        RecommendationRunner r = new RecommendationRunner();
        r.getItemsToRate();
        r.printRecommendationsFor("314");
    }
}
