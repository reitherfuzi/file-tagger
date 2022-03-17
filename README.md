# File-Tagger
This command-line application allows to read, write and check any user-defined attribute on any file-type.
Only restriction is that no remote files are supported and that only user-defined attributes can be modified.

## But Why?
These attributes can be used for tagging files to find them easier. It is planned to create my own homemade **Movie-DB**, 
where all my videos are stored on a simple local hard-drive or on some network-accessible server. 
But the movies should also be stored somehow in a database to filter and search for starring people, tags or categories.

## The Idea
So I plan to store the movie-files in a flat hierarchy sorted by the movie-category. Everytime I add a new movie,
the **file-tagger* will set a unique UUID on the movie. Then it will be saved on the hard-drive but at the same
time all the information like category, tags, the UUID and the starring people will be added to the database.

So when I want to see a movie with category *action*, I will be able to sort my database by the category and 
get nice filtered movies of that category. Then I can even filter the movies even more. 
When I found the movie to watch I can get the unique UUID and use the file-tagger to search for it on my local or any
other remote file-system.

The movies at the database will be accessed via a web-application, to keep it nice and simple.

## Nice?
Yeah, of course! I mean I love the idea of this, but sure there is a lot to improve. So let me know!