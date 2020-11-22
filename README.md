Academics
=================

Academics is a social app for students all around the world.
In Academics, students can ask questions, share suggestions, socialize with one another, and find help.

Introduction
------------

The idea behind Academics is that during the COVID-19, students have a hard time studying in online lectures.
So Academics helps students gather good and reliable material and information to study from (Like course statistics and summaries or, if needed, tests from previous years and different institutions),
and connects between students who need help and those who can provide help.

Installation Instructions
---------------

There are two ways to install the application:

The first one is to download the Academics.apk to your android device and install it. (It's that simple.)

The second method is to download the source code and run it in android studio.
We suggest you install the application using the first method since the second one might not be as reliable (you need to have Android Studio and a configured SDK in your system)
If you choose the second method, follow this steps:
- In github, press the code button and download zip.
- Unzip the Source Code folder to your computer.
- In Android Studio, select open an existing project and select the Source Code folder as the root directory
- Wait for the gradle file to build
- Run the app on an emulator or directly to your Android device

Both the APK and the source code can be found in our repository. The APK file is named "Academics.apk" and the source code is in the folder named "Source Code".


- *Please see that your device language is English since we are still not supporting other languages and it may disturb the view*


Getting Started
---------------
To enter Academics you first need to register.
The registration process requires an email, a password, and a display name that will be shown to other users.
It is recommended to use your full name as a display name.


<img src="screenshots/start.png" width="20%"> <img src="screenshots/enter_email.png" width="20%"> <img src="screenshots/enter_password.png" width="20%"> <img src="screenshots/enter_name.png" width="20%">

After you register, academics will keep you logged in, but if you want to login to your account from a different device just press login and enter your info as shown below:

<img src="screenshots/login.png" width="20%">

 
 
Academics features
-----------------

The main feature of the app is the ability to share a post with others who may be interested in it.
The way we accomplished it is by making a hierarchy of folders containing faculty->department->course.
To share your post you need to select a folder where the post will be stored.
We have five types of posts:

- Question - this post can sit in any folder, it will contain text or maybe an image that helps you describes your question. Using our comment section, people can answer your question, ask a continuing question, or get more understanding of what you meant.

- Suggestion - this post can sit in any folder, it will contain something you think can be helpful to other users of the app. It can be a suggestion for a course, a studying method that you find helpful, or even a link to a video explaining a topic very well.

- Test - This post can sit only in a course folder, this post is a pdf file of a test of the course where the post can be found. The test can be from previous years, from different institutions, or anything that may help other students who have a hard time studying for their exams.

- Summary - This post can sit only in a course folder, this post is a pdf file of a summary for the course where the post can be found.
The summary can be any type of summary(a one a student wrote, one the professor wrote, or the one found in the syllabus) that may help other students who have a hard time understanding the material or for those who just want to know if they want to take the course.

- Social - a joke, a confession, or anything you can share that will help relieve the stress other students have during these hard times. This post can sit in any folder.

Along with the posting system, we have a comment section where people can answer and respond to the post and other comments of the same post.
 
Other minor features are:

 - Saving a folder to your Favorite Folders list so you don't have to look for that folder later.
 <img src="screenshots/favorites.png" width="15%">
 
 - Searching a folder in your Favorite Folders list.
 <img src="screenshots/favorite_search.png" width="15%">
 
 - Sharing clickable links in a post.
 <img src="screenshots/link_post.png" width="15%">
 
 - Viewing all the posts you have posted by viewing your profile.
 <img src="screenshots/profile1.png" width="15%">
 
 - Viewing all the posts another user has posted by clicking on his name on a post of his you have seen.
 <img src="screenshots/profile2.png" width="15%">
 
 - Filtering posts by the five types. (Question, Suggestion, Test, Summary, Social) (Need to refresh the page to get the filtered result)
 <img src="screenshots/flair_filter.png" width="15%">
 
 - Searcing for keywords that will result in folders and posts that are a good match for the keywords.
 <img src="screenshots/search_example.png" width="15%">
 
 - Displaying posts best fitted for you.
 <img src="screenshots/bestfit.png" width="15%">
 
  - Switching between light theme and dark theme.
 <img src="screenshots/light_theme.png" width="15%">
 
 
Libraries Used
--------------

For http request - Retrofit.

For database - Room.

For image loading - Glide.

Photo View - 'com.github.chrisbanes:PhotoView:2.3.0'

Material SearchBar - 'com.github.mancj:MaterialSearchBar:0.8.5'

 
Future Features
--------------

- Better searching algorithm
- Support for multiple languages.
- A web page for those who can't download the application.
- Sharing posts via a link that will open the application or the web page.
- Profile Search by name.
- Custom tags for posts.
- Create business accounts to look for student employees (To help students find jobs in these hard times).
