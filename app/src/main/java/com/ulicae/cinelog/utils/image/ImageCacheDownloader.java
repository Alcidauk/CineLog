package com.ulicae.cinelog.utils.image;

public class ImageCacheDownloader {

    static final String BASE_URL = "https://image.tmdb.org/t/p/w185/";

    private String filePath;
    private ImageFinderFactory imageFinderFactory;
    private CacheImageDownloaderTaskFactory taskFactory;

    public ImageCacheDownloader(String filePath) {
        this(filePath, new ImageFinderFactory(), new CacheImageDownloaderTaskFactory());
    }

    ImageCacheDownloader(String filePath, ImageFinderFactory imageFinderFactory,
                         CacheImageDownloaderTaskFactory cacheImageDownloaderTaskFactory) {
        this.filePath = filePath;
        this.imageFinderFactory = imageFinderFactory;
        this.taskFactory = cacheImageDownloaderTaskFactory;
    }

    public ImageFinder getPosterFinder() {
        if (!isPosterInCache()) {
            taskFactory.makeTask(filePath).execute();
            return imageFinderFactory.makeNetworkImageFinder();
        }
        return imageFinderFactory.makeCacheImageFinder();
    }

    boolean isPosterInCache() {
        return ((ImageCacheFinder) imageFinderFactory.makeCacheImageFinder()).getImage(filePath).exists();
    }

}
