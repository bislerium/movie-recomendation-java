import java.util.ArrayList;
import java.util.Collections;

public class MovieRunnerSimilarRatings {

    private FourthRatings fourRatings;

    public void printAverageRatings() {

        System.out.println("The number of raters : " + RaterDatabase.size());
        System.out.println("The number of movies : " + MovieDatabase.size());

        int minimalRaters = 35;
        ArrayList<Rating> ratings = fourRatings.getAverageRatings(minimalRaters);

        System.out.println("Number of movies with minimal raters: " + minimalRaters + " are " + ratings.size());
        Collections.sort(ratings);
        for (Rating r : ratings) {
            System.out.println(r.getValue() + " -> " + MovieDatabase.getTitle(r.getItem()));
        }
    }

    public void printAverageRatingsByYearAfterAndGenre() {
        AllFilters filters = new AllFilters();
        int year = 1990;
        YearAfterFilter yearAfterFilter = new YearAfterFilter(year);
        String genre = "Drama";
        GenreFilter genreFilter = new GenreFilter(genre);
        filters.addFilter(yearAfterFilter);
        filters.addFilter(genreFilter);
        int minimalRaters = 8;
        ArrayList<Rating> ratings= fourRatings.getAverageRatingsByFilter(minimalRaters, filters);
        System.out.println("Number of Movies found : " + ratings.size());
        for (Rating r : ratings) {
            String movieID = r.getItem();
            System.out.println(r.getValue() +  " " + MovieDatabase.getYear(movieID) + " "  + MovieDatabase.getTitle(movieID) + "\n\t" + MovieDatabase.getGenres(movieID));
        }
    }

    public void printSimilarRatings() {
        String id = "337";
        int numSimilarRaters = 10;
        int minimalRaters = 3;
        ArrayList<Rating> ratings = fourRatings.getSimilarRatings(id, numSimilarRaters, minimalRaters);
        ratings.forEach((a) -> System.out.println("Movies: " + MovieDatabase.getTitle(a.getItem()) + "\n\tRating: " + a.getValue()));
    }

    public void printSimilarRatingsByGenre() {
        String id = "964";
        int numSimilarRaters = 20;
        int minimalRaters = 5;
        ArrayList<Rating> ratings = fourRatings.getSimilarRatings(id, numSimilarRaters, minimalRaters);

        String genre = "Mystery";
        Filter genreFilter = new GenreFilter(genre);
        ratings.forEach((a) -> {
            String movieID = a.getItem();
            if (genreFilter.satisfies(movieID)){
                System.out.println("Movies: " + MovieDatabase.getTitle(movieID) + " | Rating: " + a.getValue()
                        + "\n\t" + MovieDatabase.getGenres(movieID));
            }
        });
    }

    public void printSimilarRatingsByDirector() {
        String id = "120";
        int numSimilarRaters = 10;
        int minimalRaters = 2;
        ArrayList<Rating> ratings = fourRatings.getSimilarRatings(id, numSimilarRaters, minimalRaters);

        String directors = "Clint Eastwood,J.J. Abrams,Alfred Hitchcock,Sydney Pollack,David Cronenberg,Oliver Stone,Mike Leigh";
        Filter directorsFilter = new DirectorsFilter(directors);
        ratings.forEach((a) -> {
            String movieID = a.getItem();
            if (directorsFilter.satisfies(movieID)){
                System.out.println("Movies: " + MovieDatabase.getTitle(movieID) + " | Rating: " + a.getValue()
                        + "\n\t" + MovieDatabase.getDirector(movieID));
            }
        });
    }

    public void printSimilarRatingsByGenreAndMinutes() {
        String id = "168";
        int numSimilarRaters = 10;
        int minimalRaters = 3;
        ArrayList<Rating> ratings = fourRatings.getSimilarRatings(id, numSimilarRaters, minimalRaters);

        String genre = "Drama";
        int minMin = 80;
        int maxMin = 160;
        AllFilters filters = new AllFilters();
        filters.addFilter(new GenreFilter(genre));
        filters.addFilter(new MinutesFilter(minMin, maxMin));
        ratings.forEach((a) -> {
            String movieID = a.getItem();
            if (filters.satisfies(movieID)){
                System.out.println("Movies: " + MovieDatabase.getTitle(movieID) + " | Minutes:" + MovieDatabase.getMinutes(movieID) + " | Rating: " + a.getValue()
                        + "\n\t" + MovieDatabase.getGenres(movieID));
            }
        });
    }

    public void printSimilarRatingsByYearAfterAndMinutes() {
        String id = "314";
        int numSimilarRaters = 10;
        int minimalRaters = 5;
        ArrayList<Rating> ratings = fourRatings.getSimilarRatings(id, numSimilarRaters, minimalRaters);

        int year = 1975;
        int minMin = 70;
        int maxMin = 200;
        AllFilters filters = new AllFilters();
        filters.addFilter(new YearAfterFilter(year));
        filters.addFilter(new MinutesFilter(minMin, maxMin));
        ratings.forEach((a) -> {
            String movieID = a.getItem();
            if (filters.satisfies(movieID)){
                System.out.println("Movies: " + MovieDatabase.getTitle(movieID) + " | Year:" + MovieDatabase.getYear(movieID) + " | Minutes: " + MovieDatabase.getMinutes(movieID)
                        + "\n\t" + a.getValue());
            }
        });
    }

    public static void main(String[] args) {
        MovieRunnerSimilarRatings movieRunnerSimilarRatings = new MovieRunnerSimilarRatings();
        movieRunnerSimilarRatings.fourRatings = new FourthRatings();
//        MovieDatabase.initialize("ratedmovies_short.csv");
        MovieDatabase.initialize("ratedmoviesfull.csv");
//        RaterDatabase.initialize("data/ratings_short.csv");
        RaterDatabase.initialize("ratings.csv");

//        movieRunnerSimilarRatings.printAverageRatings();
//        movieRunnerSimilarRatings.printAverageRatingsByYearAfterAndGenre();
        movieRunnerSimilarRatings.printSimilarRatings();
//        movieRunnerSimilarRatings.printSimilarRatingsByGenre();
//        movieRunnerSimilarRatings.printSimilarRatingsByDirector();
//        movieRunnerSimilarRatings.printSimilarRatingsByGenreAndMinutes();
//        movieRunnerSimilarRatings.printSimilarRatingsByYearAfterAndMinutes();
    }
}
