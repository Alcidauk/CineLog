package com.ulicae.cinelog.utils.image;

import com.ulicae.cinelog.utils.FileUtilsWrapper;

import java.io.File;

class ImageCacheFinder implements ImageFinder<File> {

    private final FileUtilsWrapper fileUtilsWrapper;

    ImageCacheFinder(File cacheDir) {
        this(new FileUtilsWrapper(cacheDir));
    }

    private ImageCacheFinder(FileUtilsWrapper fileUtilsWrapper) {
        this.fileUtilsWrapper = fileUtilsWrapper;
    }

    @Override
    public File getImage(String imagePath) {
        File posterCacheRoot = fileUtilsWrapper.getFilesDir();
        return fileUtilsWrapper.getFile(posterCacheRoot.getAbsolutePath() + '/' + imagePath);
    }

}
