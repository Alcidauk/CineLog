package com.ulicae.cinelog.utils.image;

public class ImageFinderFactory {

    ImageFinder makeNetworkImageFinder(){
        return new ImageNetworkFinder();
    }

    ImageFinder makeCacheImageFinder(){
        return new ImageCacheFinder();
    }
}
