# README #

This is an android sample application " EasyFeed" to get data from public RSS feed "NASA Education News"

### What is this repository for? ###

* This is sample android application for reading and displaying data from a public rss feed.
* Version: Beta Version V0.2

### How do I get set up? ###

* This is Gradle-based project, just import it to android studio

### Technical details ###

* This simple application is based on activity/fragment navigation sample on android studio
* Providing Rss reading and displaying into ListView
* Downloading thumbnails with caching
* handle configuration changes using retained fragment
* Easily extendable for new rss feeds, just write your own rss parser

### What is new ? ###

* Fix retained fragment current memory leak.
* Fix details layout style.
* Caching images into Lru (MemoryCache and DiskCache)
Note: the chaching technique used is based on google's android caching bitmaps training:
*https://developer.android.com/training/displaying-bitmaps/cache-bitmap.html

### Future Work ###

* Using Picasso library for image downloading/ caching
* Adding another RssParser by extending the rssparser interface
* handle tablet view
* opening url into webview


### Contact ###
* ahmed.mansy156@gmail.com
* +201220002323