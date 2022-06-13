package com.ulicae.cinelog.utils.image;

import java.io.File;

public class ImageCacheDownloader {

    static final String BASE_URL = "https://image.tmdb.org/t/p/w185/";

    private final File dataDir;
    private final String filePath;
    private final ImageFinderFactory imageFinderFactory;
    private final CacheImageDownloaderTaskFactory taskFactory;

    public ImageCacheDownloader(File dataDir, String filePath) {
        this(dataDir, filePath, new ImageFinderFactory(), new CacheImageDownloaderTaskFactory());
    }

    ImageCacheDownloader(File dataDir, String filePath, ImageFinderFactory imageFinderFactory,
                         CacheImageDownloaderTaskFactory cacheImageDownloaderTaskFactory) {
        this.dataDir = dataDir;
        this.filePath = filePath;
        this.imageFinderFactory = imageFinderFactory;
        this.taskFactory = cacheImageDownloaderTaskFactory;
    }

    public ImageFinder getPosterFinder() {
        if (!isPosterInCache()) {
            taskFactory.makeTask(dataDir, filePath).execute();
            return imageFinderFactory.makeNetworkImageFinder();
        }
        return imageFinderFactory.makeCacheImageFinder(dataDir);
    }

    boolean isPosterInCache() {
        return ((ImageCacheFinder) imageFinderFactory.makeCacheImageFinder(dataDir))
                .getImage(filePath).exists();
    }

}
