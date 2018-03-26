package com.example.junyoung.dropchecker.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;

import com.example.junyoung.dropchecker.Post;
import com.example.junyoung.dropchecker.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MakePostActivity extends AppCompatActivity {
    private String userName;

    private FirebaseUser user;
    private DatabaseReference postsRef;

    @BindView(R.id.toolbar_makepost) Toolbar toolbar;
    @BindView(R.id.edittext_makepost_size) EditText editTextSize;
    @BindView(R.id.edittext_makepost_price) EditText editTextPrice;
    @BindView(R.id.edittext_makepost_subject) EditText editTextSubject;
    @BindView(R.id.edittext_makepost_content) EditText editTextContent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_makepost);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String databaseNodeName = getIntent().getStringExtra("databaseNodeName");
        // Change the content hint based on the post type.
        if (databaseNodeName.equals("buyingPosts")) {
            editTextContent.setHint("What do you want to buy?");
        }

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        postsRef = database.getReference(databaseNodeName);
        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            userName = user.getDisplayName();
        }
    }

    /**
     * Initialize the contents of the Activity's standard options menu.
     *
     * @param menu The options menu in which I place your items.
     * @return return true for the menu to be displayed.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_makepost, menu);

        return true;
    }

    /**
     * This method sends the post information to the Firebase realtime database whenever the send
     * item in my options menu is selected.
     *
     * @param item the menu item that was selected.
     * @return boolean Return false to allow normal menu processing to proceed, true to consume
     * it here.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String subject = editTextSubject.getText().toString();
        String content = editTextContent.getText().toString();
        String price = editTextPrice.getText().toString();
        String size = editTextSize.getText().toString();
        switch (item.getItemId()) {
            case R.id.item_send:
                String postId = postsRef.push().getKey();
                String userPhotoUrlString = null;

                if (user.getPhotoUrl() != null) {
                    userPhotoUrlString = user.getPhotoUrl().toString();
                }
                Post post = new Post(userName, size, price, subject, content, userPhotoUrlString);
                postsRef.child(postId).setValue(post);

                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * This method is called whenever the user chooses to navigate Up within this activity from
     * the action bar.
     *
     * @return true, UP navigation completed successfully.
     */
    @Override
    public boolean onSupportNavigateUp() {
        finish();

        return true;
    }
}
