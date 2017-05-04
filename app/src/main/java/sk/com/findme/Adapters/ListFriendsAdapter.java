package sk.com.findme.Adapters;

import android.content.ClipData;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import sk.com.findme.Classes.FriendsObject;
import sk.com.findme.Classes.People;
import sk.com.findme.R;

/**
 * Created by sanchirkartiev on 21/03/15.
 */
public class ListFriendsAdapter extends BaseAdapter {

    Context context;
    LayoutInflater inflater;
    ArrayList<FriendsObject> friends;

    public ListFriendsAdapter(Context context, ArrayList<FriendsObject> friends)
    {
        this.friends = friends;
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return friends.size();
    }

    @Override
    public Object getItem(int position) {
        return friends.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = inflater.inflate(R.layout.item_list_friends, parent, false);
        FriendsObject currentPeople = friends.get(position);

        ImageView view = (ImageView)rowView.findViewById(R.id.friendsPhoto);
        TextView firstName = (TextView)rowView.findViewById(R.id.friendsName);
        firstName.setText(currentPeople.getName());
        //view.setImageBitmap(currentPeople.getPicture());
        return rowView;
    }
}
