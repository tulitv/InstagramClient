package week1assignment.codepath.instagramclient;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;


public class PhotosActivity extends ActionBarActivity {

    public static final String CLIENT_ID = "47be27eb8c55469badce6b9d68e2d768";
    private ArrayList<InstagramPhoto> photos;
    private InstagramPhotoAdapter aPhotos;
    private SwipeRefreshLayout swipeContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photos);

        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //First empty the arraylist
                photos.clear();
                //and resend request and repopulate data
                fetchPopularPhotos();
            }
        });

        // SEND OUT API REQUEST to POPULAR PHOTOS
        photos = new ArrayList<InstagramPhoto>();
        // 1. Create the adapter linking it to the resource
        aPhotos = new InstagramPhotoAdapter(this, photos);
        // 2. Find the listview from the layout
        ListView lvPhotos = (ListView) findViewById(R.id.lvPhotos);
        // 3. Set the adapter binding it to the ListView
        lvPhotos.setAdapter(aPhotos);

        // Fetch the popular photos
        fetchPopularPhotos();
    }

    public void fetchPopularPhotos() {

        String url = "https://api.instagram.com/v1/media/popular?client_id=" + CLIENT_ID;

        // Create the network client
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url, null, new JsonHttpResponseHandler() {
           // onSuccess (worked)
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                // Iterate each of the photo items and decode the item into a java object
                JSONArray photosJSON = null;
                try {
                    photosJSON = response.getJSONArray("data"); // array of posts
                    // iterate array of posts
                    for (int i = 0; i < photosJSON.length(); i++) {
                        // get the json object at that position
                        JSONObject photoJSON = photosJSON.getJSONObject(i);
                        // decode the attributes of the json into a data model
                        InstagramPhoto photo = new InstagramPhoto();
                        photo.username = photoJSON.getJSONObject("user").getString("username");
                        photo.caption = photoJSON.getJSONObject("caption").getString("text");
                        photo.imageURL = photoJSON.getJSONObject("images")
                                .getJSONObject("standard_resolution").getString("url");
                        photo.userImageURL = photoJSON.getJSONObject("user").getString("profile_picture");
                        photo.likesCount = photoJSON.getJSONObject("likes").getInt("count");
                        photo.timeStamp = photoJSON.getJSONObject("caption").getString("created_time");

                        //Figure out the number of comments exist for each photo
                        int numberOfComments = photoJSON.getJSONObject("comments").getInt("count");
                        //Get all the comments.
                        //Note:  There must be a less space-demanding way to extract only 2 elements
                        JSONArray comments = photoJSON.getJSONObject("comments").getJSONArray("data");

                        for (int j = 0; ((j<photo.NO_OF_COMMENTS) && (j<numberOfComments)); j++) {
                            photo.comments[0][j] = comments.getJSONObject(j).getJSONObject("from").getString("username");
                            photo.comments[1][j] = comments.getJSONObject(j).getString("text");
                        }

                        // Add decoded object to the photos
                        photos.add(photo);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                // callback
                aPhotos.notifyDataSetChanged();

                //...the data has come back, finish populating listview...
                //Now we call setRefreshing(false) to signal refresh has finished
                swipeContainer.setRefreshing(false);
            }

            // onFailure (fail)
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                // DO SOMETHING
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_photos, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
