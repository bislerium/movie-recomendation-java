
import edu.duke.*;

import java.util.*;
import java.util.stream.Collectors;

import org.apache.commons.csv.*;

public class FirstRatings {

    public ArrayList<Movie> loadMovies(String fileName) {
        FileResource fr = new FileResource(fileName);
        ArrayList<Movie> movies= new ArrayList<>();
        for(CSVRecord c : fr.getCSVParser(true)) {
            movies.add(new Movie(c.get("id"), c.get("title"), c.get("year"), c.get("genre"), c.get("director"), c.get("country"), c.get("poster"), Integer.parseInt(c.get("minutes"))));
        }
        return movies;
    }

    public void testLoadMovies(){
        ArrayList<Movie> movies = loadMovies("data/ratedmoviesfull.csv");
//        movies.forEach((a) -> System.out.println(a));
        System.out.println("Number of Movies : " + movies.size());
        long comedyCount= movies.stream().filter((a) -> a.getGenres().contains("Comedy")).count();
        long minCount = movies.stream().filter((a) -> a.getMinutes() > 150).count();
        System.out.println("Number of Movies with Comedy Genre : " + comedyCount);
        System.out.println("Number of Movies that are greater than 150 minutes in length : " + minCount);
        HashMap<String, Integer> dm =numMoviesByDirector(movies);
//        dm.forEach((k,v) -> System.out.println(k + " | " + v));
    }

    public HashMap<String, Integer> numMoviesByDirector(ArrayList<Movie> movies) {
        HashMap<String, Integer> dm = new HashMap<>();
        for(Movie m : movies) {
            String[] directors = m.getDirector().split(",");
            for (String dir : directors) {
                dir = dir.trim();
                if (dm.containsKey(dir)) {
                    dm.put(dir, dm.get(dir) + 1);
                } else {
                    dm.put(dir, 1);
                }
            }
        }
        String director = "";
        int max = 0;
        for(String s : dm.keySet()) {
            int count = dm.get(s);
            if(count > max) {
                director = s;
                max = count;
            }
        }
        System.out.println("Maximum number of films : " + dm.get(director) + " directed by : " + director);
        return dm;
    }

    public ArrayList<Rater> loadRaters(String fileName) {
        FileResource fr = new FileResource(fileName);
        ArrayList<Rater> ratings= new ArrayList<>();
        Rater r = null;
        for(CSVRecord c : fr.getCSVParser(true)) {
            String raterID = c.get("rater_id");
            String movieID = c.get("movie_id");
            Double movieRating = Double.parseDouble(c.get("rating"));
            if( (r == null) || !(r.getID().equals(raterID))) {
                r = new EfficientRater(raterID);
                ratings.add(r);
            }
            r.addRating(movieID, movieRating);
        }
        return ratings;
    }

    public void testLoadRaters() {
        ArrayList<Rater> r = loadRaters("data/ratings.csv");
        System.out.println("Total number of raters : " + r.size());
        r.forEach((a) -> {
            System.out.println("Week1.Rater : " + a.getID() + " | " + " Number of ratings : " + a.numRatings());
            for(String movie : a.getItemsRated()) {
                System.out.println("\tMovie ID : " + movie + " | " + " Rating Given : " + a.getRating(movie) );
            }
        });

        String ratingsFromID = "193";
        System.out.println(ratingsFromID + " has Number of Ratings : " + numberOfRatings(r, ratingsFromID));

        maxRatings(r);

        String movie = "1798709";
        ArrayList<Rater> movieRaters = movieRatedBy(r, movie);
        System.out.println(movie + " movie was rated by " + movieRaters.size() + " raters.");

        System.out.println(differentMovieRated(r) + " different movies were rated by the users");
    }

    public int numberOfRatings(ArrayList<Rater> raters, String id) {
        Rater a = raters.stream().filter((x) -> x.getID().equals(id)).findFirst().get();
        return a.numRatings();
    }

    public void maxRatings(ArrayList<Rater> raters) {
        int max = 0;
        for (Rater r : raters) {
            int ratings = r.numRatings();
            if(ratings > max) {
                max = ratings;
            }
        }
        int finalMax = max;
        ArrayList<Rater> rt = (ArrayList<Rater>) raters.stream()
                .filter((a) -> a.numRatings() == finalMax)
                .collect(Collectors.toList());
        System.out.println("Maximum number of ratings : " + finalMax + "  by given raters : ");
        for (Rater r : rt ) {
            System.out.println("\t-> " + r.getID());
        }
    }

    public ArrayList<Rater> movieRatedBy(ArrayList<Rater> raters, String movie) {
        return (ArrayList<Rater>) raters
                .stream()
                .filter((a) -> a.hasRating(movie))
                .collect(Collectors.toList());
    }

    public int differentMovieRated(ArrayList<Rater> raters) {
        ArrayList<String> movies = new ArrayList<>();
        for (Rater r : raters) {
            for (String s : r.getItemsRated()) {
                if(!movies.contains(s)) {
                    movies.add(s);
                }
            }
        }
        return movies.size();
    }

    public static void main(String[] args) {
        FirstRatings fr = new FirstRatings();
//        fr.testLoadMovies();
        fr.testLoadRaters();
    }
}
