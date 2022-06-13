package com.ulicae.cinelog.utils.image;

import java.io.File;

public class ImageCacheDownloader {

    static final String BASE_URL = "https://image.tmdb.org/t/p/w185/";

    private final File cacheDir;
    private final String filePath;
    private final ImageFinderFactory imageFinderFactory;
    private final CacheImageDownloaderTaskFactory taskFactory;

    public ImageCacheDownloader(File cacheDir, String filePath) {
        this(cacheDir, filePath, new ImageFinderFactory(), new CacheImageDownloaderTaskFactory());
    }

    ImageCacheDownloader(File cacheDir, String filePath, ImageFinderFactory imageFinderFactory,
                         CacheImageDownloaderTaskFactory cacheImageDownloaderTaskFactory) {
        this.cacheDir = cacheDir;
        this.filePath = filePath;
        this.imageFinderFactory = imageFinderFactory;
        this.taskFactory = cacheImageDownloaderTaskFactory;
    }

    public ImageFinder getPosterFinder() {
        if (!isPosterInCache()) {
            taskFactory.makeTask(cacheDir, filePath).execute();
            return imageFinderFactory.makeNetworkImageFinder();
        }
        return imageFinderFactory.makeCacheImageFinder(cacheDir);
    }

    boolean isPosterInCache() {
        return ((ImageCacheFinder) imageFinderFactory.makeCacheImageFinder(cacheDir))
                .getImage(filePath).exists();
    }

}
