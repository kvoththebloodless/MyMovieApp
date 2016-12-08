# MyMovieApp

Built an app, optimized for tablets, to help users discover popular and highly rated movies on the web. It displays a scrolling grid of movie trailers, launches a details screen whenever a particular movie is selected, allows users to save favorites, play trailers, and read user reviews. This app utilizes core Android user interface components and fetches movie information using themoviedb.org web API.

The application provides Tablet support in the form of master-detail layout upon orientation change to landscape.

##Where to put the API key?

The api key is to be input into the API_KEY variable in the Global class in the misc directory.

## Project Specification

### User Interface - Layout

- UI contains an element (e.g., a spinner or settings menu) to toggle the sort order of the movies by: most popular, highest rated.

- Movies are displayed in the main layout via a grid of their corresponding movie poster thumbnails.

- UI contains a screen for displaying the details for a selected movie.

- Movie Details layout contains title, release date, movie poster, vote average, and plot synopsis.

- Movie Details layout contains a section for displaying trailer videos and user reviews.

###User Interface - Function

- When a user changes the sort criteria (most popular, highest rated, and favorites) the main view gets updated correctly.

- When a movie poster thumbnail is selected, the movie details screen is launched.

- When a trailer is selected, app uses an Intent to launch the trailer.

- In the movies detail screen, a user can tap a button(for example, a star) to mark it as a Favorite.

###Network API Implementation

- In a background thread, app queries the /movie/popular or /movie/top_rated API for the sort criteria specified in the settings menu.

- App requests for related videos for a selected movie via the /movie/{id}/videos endpoint in a background thread and displays those       details when the user selects a movie.

- App requests for user reviews for a selected movie via the /movie/{id}/reviews endpoint in a background thread and displays those       details when the user selects a movie.

###Data Persistence

- App saves a "Favorited" movie to SharedPreferences or a database using the movie’s id.

- When the "favorites" setting option is selected, the main view displays the entire favorites collection based on movie IDs stored in     SharedPreferences or a database.

<img src="https://cloud.githubusercontent.com/assets/13608668/14762906/fce7bbd4-09a3-11e6-9cf4-f8e212bdc7c5.JPG" width="30%"></img> <img src="https://cloud.githubusercontent.com/assets/13608668/14762909/2390e922-09a4-11e6-8b84-ac021e63e6d9.JPG" width="30%"></img> <img src="https://cloud.githubusercontent.com/assets/13608668/14762933/62a78af8-09a4-11e6-9126-1c4762985ddf.JPG" width="30%"></img> <img src="https://cloud.githubusercontent.com/assets/13608668/14762907/014acd4c-09a4-11e6-9159-de15d07ef573.JPG" width="30%"></img> 
