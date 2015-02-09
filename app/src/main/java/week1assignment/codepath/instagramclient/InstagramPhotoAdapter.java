package week1assignment.codepath.instagramclient;


import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.format.DateUtils;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.makeramen.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.List;

/**
 * Created by vincetulit on 2/7/15.
 */
public class InstagramPhotoAdapter extends ArrayAdapter<InstagramPhoto> {

    // What data do we need from the activity
    //Context, Data Source
    public InstagramPhotoAdapter(Context context, List<InstagramPhoto> objects) {
        super(context, android.R.layout.simple_list_item_1, objects);
    }

    // Use the template to display each photo
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        InstagramPhoto photo = getItem(position);
        // Check if we are using a recycled view, if not we need to inflate
        if (convertView == null) {
            // create a new view from template
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_photo, parent, false);
        }
        // Lookup the views for populating the data (image, caption)
        TextView tvCaption = (TextView) convertView.findViewById(R.id.tvCaption);
        ImageView ivPhoto = (ImageView) convertView.findViewById(R.id.ivPhoto);
        ImageView ivUserPhoto = (ImageView) convertView.findViewById(R.id.ivUserPhoto);
        TextView tvUsername = (TextView) convertView.findViewById(R.id.tvUsername);
        TextView tvRelativeTime = (TextView) convertView.findViewById(R.id.tvRelativeTime);
        TextView tvLikeCount = (TextView) convertView.findViewById(R.id.tvLikeCount);
        TextView tvComments = (TextView) convertView.findViewById(R.id.tvComments);

        // Insert the model data into each of the view items
        tvUsername.setText(photo.username);
        tvRelativeTime.setText(
                DateUtils.getRelativeTimeSpanString(
                    Long.parseLong(photo.timeStamp)*1000,
                    System.currentTimeMillis(),
                    DateUtils.SECOND_IN_MILLIS));

        tvCaption.setText(photo.caption);
        tvLikeCount.setText(Integer.toString(photo.likesCount) + " likes");

        //Ugly solution due to short time, but works.
        Spannable str = new SpannableString(photo.comments[0][0] + ": " + photo.comments[1][0] + "\n\n" +
                photo.comments[0][1] + ": " + photo.comments[1][1]);
        str.setSpan(new StyleSpan(Typeface.BOLD),0, photo.comments[0][0].length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        str.setSpan(new StyleSpan(Typeface.BOLD),
                photo.comments[0][0].length() + 2 + photo.comments[1][0].length() + 2,
                photo.comments[0][0].length() + 2 + photo.comments[1][0].length() + 2 + photo.comments[0][1].length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        tvComments.setText(str, TextView.BufferType.SPANNABLE);


        // Clear out the imageview if it was recycled (right away)
        ivPhoto.setImageResource(0);
        // Insert the image using picasso (send out async)
        Picasso.with(getContext()).load(photo.imageURL).into(ivPhoto);

        Transformation transformation = new RoundedTransformationBuilder()
                .borderColor(Color.rgb(0x2C,0x65,0x8b))
                .borderWidthDp(2)
                .cornerRadiusDp(30)
                .oval(false)
                .build();
        Picasso.with(getContext()).load(photo.userImageURL).transform(transformation).into(ivUserPhoto);


        // Return the created item as a view
        return convertView;
    }
}
