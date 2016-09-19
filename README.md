# README #

This is an android sample application " EasyFeed" to get data from public RSS feed "NASA Education News"

### What is this repository for? ###

* This is sample android application for reading and displaying data from public rss feed.
* Version: Beta Version V0.1

### How do I get set up? ###

* This is Gradle-based project, just import it to android studio

### Technical details ###

* This simple application is based on activity/fragment navigation sample on android studio
* Providing Rss reading and displaying into ListView
* Downloading thumbnails without caching
* handle configuration changes using retained fragment
* Easily extendable for new rss feeds, just write your own rss parser


### Future Work ###

* Fix retained fragment current memory leak.
* Fix details layout style.
* Caching images into Lru (MemoryCache and DiskCache)
* Using Picasso library for image downloading/ caching
* Adding another RssParser by extending the rssparser interface
* handle tablet view
* opening url into webview


### Contact ###
* ahmed.mansy156@gmail.com
* +201220002323