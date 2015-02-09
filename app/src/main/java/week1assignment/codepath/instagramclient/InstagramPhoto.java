package week1assignment.codepath.instagramclient;

/**
 * Created by vincetulit on 2/7/15.
 */
public class InstagramPhoto {
    public String username;
    public String caption;
    public String imageURL;
    public int imageHeight;
    public int imageWidth;

    public String userImageURL;
    public int likesCount;
    public String timeStamp;
    public static final int NO_OF_COMMENTS = 2;
    public String[][] comments; //username + comment for each field


    public InstagramPhoto() {
        comments = new String[2][NO_OF_COMMENTS];
        for(int i=0;i<NO_OF_COMMENTS;i++) {
            comments[0][i]="";
            comments[1][i]="";
        }


    }
}
