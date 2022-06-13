package com.ulicae.cinelog.utils.image;

import java.io.File;

class ImageFinderFactory {

    ImageFinder makeNetworkImageFinder(){
        return new ImageNetworkFinder();
    }

    ImageFinder makeCacheImageFinder(File cacheDir){
        return new ImageCacheFinder(cacheDir);
    }
}
