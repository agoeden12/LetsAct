package andrew.com.lets_act;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.animation.AnimationUtils;
import android.widget.HorizontalScrollView;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeScreen extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    @BindView(R.id.homeScreenHiUserTextView) TextView hiUserTextView;
    @BindView(R.id.homeScreenSponsorImageSwitcher) ImageSwitcher imageSwitcher;
    @BindView(R.id.homeScreenToolbar) Toolbar myToolbar;
    @BindView(R.id.homeScreenNavigation) NavigationView navigationView;
    @BindView(R.id.homeScreenDrawer) DrawerLayout drawerLayout;
    @BindView(R.id.local_events_scroll_view) HorizontalScrollView localEventsScroll;

    public static final String TAG = "LetsAct:";

    public FirebaseAuth mFirebaseAuth;
    public FirebaseUser mUser;
    public DatabaseReference mDatabaseReference;
    //TODO: Use firebase storage with database to handle events and image arrays
    private Integer images[] = {R.drawable.neighborhoodofgood, R.drawable.stjudelogo};
    private int currentImage = 0;

    private Context mContext = this;

    private ArrayList<Events> mEventsArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        ButterKnife.bind(this);
        initializeToolBar();
        initializeImageSwitcher();
        initializeFirebaseVariables();

        setOnSwipeListeners();

        setHiUserText();

        populateLocalEvents();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.app_bar, menu);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void initializeToolBar(){
        setSupportActionBar(myToolbar);
        myToolbar.setNavigationIcon(R.drawable.ic_menu_black_24dp);

        myToolbar.setNavigationOnClickListener(v -> {
            drawerLayout.openDrawer(GravityCompat.START);
        });
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void initializeImageSwitcher() {
        imageSwitcher.setFactory(() -> {
            ImageView imageView = new ImageView(mContext);
            imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            return imageView;
        });

        imageSwitcher.setOutAnimation(AnimationUtils.loadAnimation(
                mContext, android.R.anim.slide_in_left));
        imageSwitcher.setInAnimation(AnimationUtils.loadAnimation(
                mContext, android.R.anim.slide_out_right));

        setCurrentImage(0);
    }

    private void setCurrentImage(int changeNumber){
        if(changeNumber == 1){
            imageSwitcher.setInAnimation(AnimationUtils.loadAnimation(
                    mContext, android.R.anim.slide_in_left));
            imageSwitcher.setOutAnimation(AnimationUtils.loadAnimation(
                    mContext, android.R.anim.slide_out_right));
        }

        currentImage = currentImage + changeNumber;
        if (currentImage > images.length)
            currentImage = 0;
        if (currentImage < 0)
            currentImage = 1;
        imageSwitcher.setImageResource(images[currentImage]);
    }
    
    private void initializeFirebaseVariables(){
        mFirebaseAuth = FirebaseAuth.getInstance();
        mUser =  mFirebaseAuth.getCurrentUser();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.action_settings:{
                drawerLayout.closeDrawers();
                startActivity(new Intent(mContext, Settings.class));
                return true;
            }

            case R.id.action_stats:{
                startActivity(new Intent(mContext, Stats.class));
                return true;
            }
//
//            case R.id.action_sign_out:{
//                return true;
//            }

            default:{
                return true;
            }
        }
    }


    @SuppressLint("ClickableViewAccessibility")
    private void setOnSwipeListeners(){
        imageSwitcher.setOnTouchListener(new OnSwipeTouchListener(mContext) {
            public void onSwipeRight() {
                setCurrentImage(1);
            }
            public void onSwipeLeft() {
                setCurrentImage(-1);
            }
        });
    }

    //TODO: Implement a better method for this.
    private void setHiUserText(){
        String displayName  = mUser.getDisplayName();
        if (displayName != null) {

            String hiUserText = hiUserTextView.getText().toString().trim();
            hiUserText = hiUserText.substring(0, hiUserText.length() - 1)
                    + displayName
                    + hiUserText.substring(hiUserText.length() - 1);
            hiUserTextView.setText(hiUserText);
        }
    }

    //TODO: use firebase Database for local events using horizontal RecyclerView
    private void populateLocalEvents(){
//        Events canHungerEvent = new Events();
//        canHungerEvent.setEventTitle("North Gwinnett Co-Op");
//        canHungerEvent.setEventDescription("Seeking one to two people for Saturday" +
//                " Volunteer");
//        canHungerEvent.setImageResourceId(R.drawable.northgwinnettcoop);
//
//        mEventsArrayList.add(canHungerEvent);
    }

}
