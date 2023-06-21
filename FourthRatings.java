import java.util.ArrayList;
import java.util.Collections;

public class FourthRatings {

    private double getAverageByID(String movieID, int minimalRaters) {
        int numOfRaters = 0;
        double totalRatings = 0;
        for (Rater r : RaterDatabase.getRaters()) {
            double rating = r.getRating(movieID);
            if (rating != -1) {
                totalRatings += rating;
                numOfRaters++;
            }
        }
        if (numOfRaters >= minimalRaters) {
            return totalRatings/numOfRaters;
        }
        return 0.0;
    }

    public ArrayList<Rating> getAverageRatings(int minimalRaters) {
        ArrayList<String> movies = MovieDatabase.filterBy(new TrueFilter());
        ArrayList<Rating> ratings = new ArrayList<>();
        for (String m : movies) {
            double rating = getAverageByID(m, minimalRaters);
            if (rating != 0.0) {
                ratings.add(new Rating(m, rating));
            }
        }
        return ratings;
    }

    public ArrayList<Rating> getAverageRatingsByFilter(int minimalRaters, Filter filterCriteria) {
        ArrayList<String> filteredMovies = MovieDatabase.filterBy(filterCriteria);
        ArrayList<Rating> avgRatings = new ArrayList<>();
        for(Rating r :getAverageRatings(minimalRaters)) {
            if(filteredMovies.contains(r.getItem())) {
                avgRatings.add(r);
            }
        }
        Collections.sort(avgRatings, Collections.reverseOrder());
        return avgRatings;
    }

    private double dotProduct(Rater me, Rater r) {
        double product = 0.0;
        ArrayList<String> ratingsR = r.getItemsRated();
        for (String itemRated : me.getItemsRated()) {
            double meRating = me.getRating(itemRated);
            if (meRating > 0.0 && ratingsR.contains(itemRated)) {
                product += ((meRating-5) * (r.getRating(itemRated)-5));
            }
        }
        return product;
    }

    private ArrayList<Rating> getSimilarities(String id) {
        ArrayList<Rating> ratings = new ArrayList<>();
        Rater me = RaterDatabase.getRater(id);
        for (Rater r : RaterDatabase.getRaters()) {
            if (r.equals(me))
                continue;
            double product = dotProduct(me, r);
            if(product > 0.0) {
                ratings.add(new Rating(r.getID(),product));
            }
        }
        Collections.sort(ratings, Collections.reverseOrder());
        return ratings;
    }

    private boolean getNumRaters(String id, int minimalRaters) {
        int numRaters = 0;
        for (Rater r : RaterDatabase.getRaters()) {
            if (r.hasRating(id)) {
                numRaters++;
            }
        }
        return numRaters >= minimalRaters;
    }

    public ArrayList<Rating> getSimilarRatings(String id, int numSimilarRaters, int minimalRaters) {
        ArrayList<Rating> ratings = new ArrayList<>();
        ArrayList<String> ratedMovies = RaterDatabase.getRater(id).getItemsRated();
        ArrayList<Rating> raterSimilarities = getSimilarities(id);
        if (raterSimilarities.isEmpty() || raterSimilarities.size() <= numSimilarRaters){            
            return ratings;
        }
            
        for (String mov : MovieDatabase.filterBy(new TrueFilter())) {
            if (ratedMovies.contains(mov) || !getNumRaters(mov, minimalRaters))
                continue;
            double rating = 0.0;
            int raterCount = 0;
            for (int i = 0; i < numSimilarRaters; i++) {
                Rating r = raterSimilarities.get(i);
                Rater rater = RaterDatabase.getRater(r.getItem());
                if (rater.hasRating(mov)){
                    raterCount++;
                    rating += (rater.getRating(mov) * r.getValue());
                }
            }
            if (raterCount >= minimalRaters) {
                ratings.add(new Rating(mov, rating/raterCount));
            }
        }
        Collections.sort(ratings, Collections.reverseOrder());
        return ratings;
    }
}
