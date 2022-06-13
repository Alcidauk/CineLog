package com.ulicae.cinelog.utils.image;

import com.ulicae.cinelog.utils.FileUtilsWrapper;

import java.io.File;

class ImageCacheFinder implements ImageFinder<File> {

    private FileUtilsWrapper fileUtilsWrapper;

    ImageCacheFinder(File dataDir) {
        this(new FileUtilsWrapper(dataDir));
    }

    private ImageCacheFinder(FileUtilsWrapper fileUtilsWrapper) {
        this.fileUtilsWrapper = fileUtilsWrapper;
    }

    @Override
    public File getImage(String imagePath) {
        File posterCacheRoot = fileUtilsWrapper.getCineLogPosterCache();
        return fileUtilsWrapper.getFile(posterCacheRoot.getAbsolutePath() + '/' + imagePath);
    }

}
