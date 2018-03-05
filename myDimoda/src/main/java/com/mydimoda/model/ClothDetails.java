package com.mydimoda.model;

/**
 * Created by Parth on 3/5/2018.
 */

public class ClothDetails {
    //    String id;
    String imageUrl;
    String type;

    public ClothDetails(/*String id, */String imageUrl, String type) {
//        this.id = id;
        this.imageUrl = imageUrl;
        this.type = type;
    }

    /* public String getId() {
         return id;
     }

     public void setId(String id) {
         this.id = id;
     }
 */
    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
