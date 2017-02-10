package amg.meettheteam;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ListViewCompat;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    ArrayList<String> avatar=new ArrayList<>();
    ArrayList<String> bio=new ArrayList<>();
    ArrayList<String> name=new ArrayList<>();
    ArrayList<String> title=new ArrayList<>();
    List<Contact> contacts=new ArrayList<>();
    TextView bios;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getJSON task= new getJSON();
        task.execute();
        final ListView listView = (ListView) findViewById(R.id.list);

//Declaring and attaching the custom adapter to our ListView

        ContactsAdapter adapter = new ContactsAdapter(getApplicationContext(),R.layout.listview_layout, contacts);
        listView.setAdapter(adapter);
//Setting an Item click listener to expand the Biodata of selected member
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                bios=(TextView)view.findViewById(R.id.bio);
                if(bios.getVisibility()==View.GONE)
                    bios.setVisibility(View.VISIBLE);
                else
                    bios.setVisibility(View.GONE);
            }
        });


    }

//Declaring an AsyncTask to parse the given JSON file in the background

        class getJSON extends AsyncTask<Void,Void,String> {

            protected String doInBackground(Void... params) {
                try {
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(getResources().openRawResource(R.raw.team)));
                        StringBuilder stringBuilder = new StringBuilder();
                        String line;
                        while ((line = bufferedReader.readLine()) != null) {
                            stringBuilder.append(line).append("\n");
                        }
                        bufferedReader.close();

//Returning the parsed JSON as a string
                    return stringBuilder.toString() ;



                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    return null;

                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }

            }




            protected void onPostExecute(String response){

                try {
//Initializing a JSONArray with the parsed JSON file

                    JSONArray jsonArray = new JSONArray(response);

//Storing the parameters of each JSON object in ArrayLists

                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject object = jsonArray.getJSONObject(i);
                        avatar.add(object.getString("avatar"));
                        bio.add(object.getString("bio"));
                        name.add(object.getString("firstName")+" "+object.getString("lastName"));
                        title.add(object.getString("title"));
                        contacts.add(new Contact(name.get(i),title.get(i),avatar.get(i),bio.get(i)));
                    }
                }catch(JSONException e){
                    e.printStackTrace();
                }

            }
        }

//Declaring a custom class for the ListView and Adapter
    public class Contact {

        String name;
        String title;
        String imageURL;
        String bioData;

        Contact(String name, String title, String imageURL,String bioData) {
            this.name = name;
            this.title = title;
            this.imageURL = imageURL;
            this.bioData=bioData;
        }

    }



// Defining a custom Adapter for my custom ListView


    public class ContactsAdapter extends ArrayAdapter<Contact> {



        public ContactsAdapter(Context context, int resource, List<Contact> items) {
            super(context, resource, items);
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            View v=convertView;
            if (v == null) {
                LayoutInflater vi;
                vi = LayoutInflater.from(getContext());
                v = vi.inflate(R.layout.listview_layout, null);
            }

            Contact p = getItem(position);
            if (p!=null) {

//Populating the custom ListView
                TextView names = (TextView)v.findViewById(R.id.name);
                TextView titles = (TextView)v.findViewById(R.id.title);
                ImageView avatar = (ImageView)v.findViewById(R.id.avatar);
                bios= (TextView)v.findViewById(R.id.bio);

                names.setText(p.name);
                titles.setText(p.title);
                Picasso.with(getApplicationContext()).load(p.imageURL).into(avatar);
                bios.setText(p.bioData);
                bios.setVisibility(View.GONE);
            }
        return v;
        }

    }



    }

