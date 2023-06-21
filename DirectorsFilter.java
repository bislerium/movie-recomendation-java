
public class DirectorsFilter implements Filter{
    String[] directors;

    public DirectorsFilter(String directors) {
        this.directors = directors.split(",");
    }

    @Override
    public boolean satisfies(String id) {
        String director = MovieDatabase.getDirector(id);
        for (String s: directors) {
            if (director.contains(s)) {
                return true;
            }
        }
        return false;
    }
}
