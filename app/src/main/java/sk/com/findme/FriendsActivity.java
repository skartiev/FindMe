package sk.com.findme;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;

import sk.com.findme.Adapters.ListFriendsAdapter;
import sk.com.findme.Classes.FriendsObject;
import sk.com.findme.Classes.People;


public class FriendsActivity extends ActionBarActivity {
    private final int PICK_CONTACT = 2;
    private ListView friendsList;
    private Button addContactButton;
    private Button showOnMapButton;
    //ArrayList<People> peoples;
    private ArrayList<FriendsObject> friends;
    private ListFriendsAdapter customAdapter;
    private static AsyncHttpClient client = new AsyncHttpClient();
    private static final String host = "http://10.10.40.180";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().hide();
        setContentView(R.layout.activity_friends);
        addContactButton = (Button)findViewById(R.id.buttonPlus);
       // peoples = new ArrayList<People>();

        Intent intent = getIntent();
        if(intent.hasExtra("friends"))  {
            friends = intent.getParcelableArrayListExtra("friends");
            mainContainerToView();
        }
        else  {
            friends = new ArrayList<>();
        }


        //getMas(responseHandler());
        TextView textView = new TextView(this);
        textView.setText("Ваш список друзей пуст, добавьте друзей для дальнейшей работы");

        showOnMapButton = (Button)findViewById(R.id.buttonMap);
        showOnMapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                map();
            }
        });
        client = createAsyncClient();

    }
    @Override
    protected void onPause()  {
        super.onPause();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
    }

    private static AsyncHttpClient createAsyncClient() {
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        asyncHttpClient.setUserAgent("FindMe");
        asyncHttpClient.setMaxRetriesAndTimeout(10, 2000);
        asyncHttpClient.setConnectTimeout(20000);
        asyncHttpClient.setResponseTimeout(20000);
        asyncHttpClient.setTimeout(20000);
        return asyncHttpClient;
    }

    private JsonHttpResponseHandler responseHandler() {
        return new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray array) {
                try {
                    parseJsonResponse(array);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                parseJsonResponse(response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                errorResponse.toString();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.w("LoginActivity", "statusCode" + statusCode + " " + throwable.getMessage());
            }

        };
    }

    public void getMas(JsonHttpResponseHandler jsonHttpResponseHandler)
    {
        final String apiPath = "/getUsersList";
        RequestParams params = new RequestParams();
        get(apiPath, params, jsonHttpResponseHandler);
    }
    public void parseJsonResponse(JSONObject response)
    {
    }
    public void parseJsonResponse(JSONArray array) throws JSONException {
        fromArrayToList(array);

    }

    private void fromArrayToList(JSONArray array) throws JSONException {
        for(int i=0;i<array.length(); i++)
        {
            JSONObject object = array.getJSONObject(i);
            FriendsObject friendsObject = new FriendsObject(object);
            friends.add(friendsObject);
        }
        mainContainerToView();
    }

    private void mainContainerToView() {
        customAdapter = new ListFriendsAdapter(this,friends);
        friendsList = (ListView)findViewById(R.id.listViewFriends);
        friendsList.setAdapter(customAdapter);
        addContactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(intent, PICK_CONTACT);
            }
        });
        friendsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FriendsObject people = friends.get(position);
                callAlert(people);
            }
        });
    }

    public void map()
    {
        Intent mapIntent = new Intent(this,MapActivity.class);
        mapIntent.putParcelableArrayListExtra("array",friends);
        startActivity(mapIntent);
    }
    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.get(null, host+"/getUsersList", params, responseHandler);
    }
    /*public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        params.add("","");
        client.post(null,host+url,params,responseHandler);
    }*/


    private void callAlert(final FriendsObject human)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Вы действительно хотите назначить встречу: "+ human.getName());
        builder.setTitle(human.getName());
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                goToMap(human);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                 dialog.cancel();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void goToMap(FriendsObject human)
    {
        Intent mapActivity = new Intent(this,MapActivity.class);
        mapActivity.putExtra("friend", (java.io.Serializable) human);
        startActivity(mapActivity);
    }
    @Override
    public void onActivityResult(int reqCode, int resultCode, Intent data)
    {
        super.onActivityResult(reqCode,resultCode,data);
        switch (reqCode) {
            case (PICK_CONTACT) :
                if(resultCode == Activity.RESULT_OK)
                {
                    Uri contactData = data.getData();
                    Cursor c =  getContentResolver().query(contactData, null, null, null, null);
                    Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,null,null, null);
                    if (c.moveToFirst() && phones.moveToNext()) {
                        String name = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                        Long id = c.getLong(c.getColumnIndex(ContactsContract.Contacts._ID));
                        String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        Bitmap element = BitmapFactory.decodeStream(openPhoto(id));
                        //peoples.add(new People(element,name,phoneNumber));
                        friends.add(new FriendsObject(name));
                        customAdapter = new ListFriendsAdapter(this,friends);
                        friendsList.setAdapter(customAdapter);
                    }
                }
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_friends, menu);
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

    public InputStream openPhoto(long contactId) {
        Uri contactUri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, contactId);
        Uri photoUri = Uri.withAppendedPath(contactUri, ContactsContract.Contacts.Photo.CONTENT_DIRECTORY);
        Cursor cursor = getContentResolver().query(photoUri,
                new String[] {ContactsContract.Contacts.Photo.PHOTO}, null, null, null);
        if (cursor == null) {
            return null;
        }
        try {
            if (cursor.moveToFirst()) {
                byte[] data = cursor.getBlob(0);
                if (data != null) {
                    return new ByteArrayInputStream(data);
                }
            }
        } finally {
            cursor.close();
        }
        return null;
    }

}
