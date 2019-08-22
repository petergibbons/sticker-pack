package com.demo.sticker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ImageButton;
import android.content.Intent;
import android.net.Uri;
import java.io.File;
import android.graphics.Bitmap;
import java.io.FileOutputStream;
import android.support.v4.content.FileProvider;
import java.io.IOException;
import android.graphics.BitmapFactory;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.demo.sticker.BuildConfig;

public class HomeActivity extends AppCompatActivity {

    public static Integer currentSticker = 0;
    ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if(!prefs.getBoolean("firstTime", false)) {
            // run your one time code
            AppIndexingService.enqueueWork(HomeActivity.this);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("firstTime", true);
            editor.commit();
        }

        GridView gridView = (GridView)findViewById(R.id.gridview);
        final StickersAdapter stickersAdapter = new StickersAdapter(this, stickers);
        gridView.setAdapter(stickersAdapter);

        image = (ImageView)findViewById(R.id.mainImageView);

        ImageButton ShareStickerBtn = findViewById(R.id.shareSticker);
        ShareStickerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bitmap icon = BitmapFactory.decodeResource(HomeActivity.this.getResources(),stickers[currentSticker].getImageResource());
                shareImage(icon);

            }
        });

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View view, int position, long id) {
                Sticker sticker = stickers[position];

                image.setImageResource(sticker.getImageResource());
                currentSticker = position;

                // This tells the GridView to redraw itself
                // in turn calling StickerAdapter's getView method again for each cell
                stickersAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        // set back the currently selected sticker we're viewing
        image.setImageResource(stickers[currentSticker].getImageResource());

    }

    private void shareImage(Bitmap bitmap){
        // save bitmap to cache directory
        try {
            File cachePath = new File(this.getCacheDir(), "images");
            cachePath.mkdirs(); // don't forget to make the directory
            FileOutputStream stream = new FileOutputStream(cachePath + "/image.png"); // overwrites this image every time
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            stream.close();

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to copy sticker", Toast.LENGTH_SHORT)
                    .show();
        }
        File imagePath = new File(this.getCacheDir(), "images");
        File newFile = new File(imagePath, "image.png");
        Uri contentUri = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".fileprovider", newFile);

        if (contentUri != null) {
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); // temp permission for receiving app to read this file
            shareIntent.setDataAndType(contentUri, getContentResolver().getType(contentUri));
            shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri);
            shareIntent.setType("image/png");
            startActivity(Intent.createChooser(shareIntent, "Choose an app"));
        }
    }


    public static Sticker[] stickers = {

            new Sticker("beers", R.drawable.beers,
                    "https://firebasestorage.googleapis.com/v0/b/android-sticker-template-61244.appspot.com/o/Beers.png?alt=media&token=17590bcc-587b-4aa8-a97a-5929c4cbf60f", new String [] {"beers","cheers","KC"}),

            new Sticker("baseball", R.drawable.baseballballemoji,
                    "https://firebasestorage.googleapis.com/v0/b/android-sticker-template-61244.appspot.com/o/BaseballBallEmoji.png?alt=media&token=17590bcc-587b-4aa8-a97a-5929c4cbf60f", new String [] {"baseball","KC"}),

            new Sticker("bbq ribs", R.drawable.bbqribs,
                    "https://firebasestorage.googleapis.com/v0/b/android-sticker-template-61244.appspot.com/o/BBQribs.png?alt=media&token=17590bcc-587b-4aa8-a97a-5929c4cbf60f", new String [] {"bbq","ribs","KC"}),

            new Sticker("bbq", R.drawable.bbqsticker,
                    "https://firebasestorage.googleapis.com/v0/b/android-sticker-template-61244.appspot.com/o/BBQSticker.png?alt=media&token=17590bcc-587b-4aa8-a97a-5929c4cbf60f", new String [] {"bbq","KC"}),

            new Sticker("bond bridge", R.drawable.bondbridge,
                    "https://firebasestorage.googleapis.com/v0/b/android-sticker-template-61244.appspot.com/o/bondbridge.png?alt=media&token=17590bcc-587b-4aa8-a97a-5929c4cbf60f", new String [] {"bond","bridge","KC"}),

            new Sticker("cava", R.drawable.cava,
                    "https://firebasestorage.googleapis.com/v0/b/android-sticker-template-61244.appspot.com/o/Cava.gif?alt=media&token=17590bcc-587b-4aa8-a97a-5929c4cbf60f", new String [] {"food","cava","KC"}),

            new Sticker("crown town", R.drawable.crowntown_bb,
                    "https://firebasestorage.googleapis.com/v0/b/android-sticker-template-61244.appspot.com/o/CrownTown_BB.png?alt=media&token=17590bcc-587b-4aa8-a97a-5929c4cbf60f", new String [] {"crown town","bbq","KC"}),

            new Sticker("18th & Vine", R.drawable.eightteenvine,
                    "https://firebasestorage.googleapis.com/v0/b/android-sticker-template-61244.appspot.com/o/eightteenvine.png?alt=media&token=17590bcc-587b-4aa8-a97a-5929c4cbf60f", new String [] {"beers","cheers","KC"}),

            new Sticker("First Friday", R.drawable.firstfridays_cc,
                    "https://firebasestorage.googleapis.com/v0/b/android-sticker-template-61244.appspot.com/o/FirstFridays-CC.png?alt=media&token=17590bcc-587b-4aa8-a97a-5929c4cbf60f", new String [] {"first friday","crossroads","KC"}),

            new Sticker("BBQ 2", R.drawable.kansascity_bbq,
                    "https://firebasestorage.googleapis.com/v0/b/android-sticker-template-61244.appspot.com/o/kansascity_bbq.gif?alt=media&token=17590bcc-587b-4aa8-a97a-5929c4cbf60f", new String [] {"bbq","KC"}),

            new Sticker("Kauffman", R.drawable.kauffman,
                    "https://firebasestorage.googleapis.com/v0/b/android-sticker-template-61244.appspot.com/o/Kauffman.png?alt=media&token=17590bcc-587b-4aa8-a97a-5929c4cbf60f", new String [] {"kauffman","KC"}),

            new Sticker("KC Proud", R.drawable.kc_proud,
                    "https://firebasestorage.googleapis.com/v0/b/android-sticker-template-61244.appspot.com/o/KC_proud.gif?alt=media&token=17590bcc-587b-4aa8-a97a-5929c4cbf60f", new String [] {"kc proud","proud","KC"}),

            new Sticker("Football", R.drawable.kcfootball,
                    "https://firebasestorage.googleapis.com/v0/b/android-sticker-template-61244.appspot.com/o/KCFootball.png?alt=media&token=17590bcc-587b-4aa8-a97a-5929c4cbf60f", new String [] {"football","chiefs","KC"}),

            new Sticker("KC Logo", R.drawable.kclogo,
                    "https://firebasestorage.googleapis.com/v0/b/android-sticker-template-61244.appspot.com/o/KCLogo.png?alt=media&token=17590bcc-587b-4aa8-a97a-5929c4cbf60f", new String [] {"logo","KC"}),

            new Sticker("KC Proud 2", R.drawable.kcproud,
                    "https://firebasestorage.googleapis.com/v0/b/android-sticker-template-61244.appspot.com/o/KCProud.png?alt=media&token=17590bcc-587b-4aa8-a97a-5929c4cbf60f", new String [] {"proud","KC", "kc proud"}),

            new Sticker("KC Scout", R.drawable.kcscout,
                    "https://firebasestorage.googleapis.com/v0/b/android-sticker-template-61244.appspot.com/o/KCScout.png?alt=media&token=17590bcc-587b-4aa8-a97a-5929c4cbf60f", new String [] {"scout","KC"}),

            new Sticker("Soccer", R.drawable.kcsoccer,
                    "https://firebasestorage.googleapis.com/v0/b/android-sticker-template-61244.appspot.com/o/kcsoccer.png?alt=media&token=17590bcc-587b-4aa8-a97a-5929c4cbf60f", new String [] {"soccer","sporting","KC"}),

            new Sticker("Weather", R.drawable.kcweather,
                    "https://firebasestorage.googleapis.com/v0/b/android-sticker-template-61244.appspot.com/o/KCWeather.png?alt=media&token=17590bcc-587b-4aa8-a97a-5929c4cbf60f", new String [] {"weather","cheers","KC"}),

            new Sticker("Kemper", R.drawable.kemper,
                    "https://firebasestorage.googleapis.com/v0/b/android-sticker-template-61244.appspot.com/o/Kemper.gif?alt=media&token=17590bcc-587b-4aa8-a97a-5929c4cbf60f", new String [] {"kemper","KC"}),

            new Sticker("KS<->MO", R.drawable.ksmo,
                    "https://firebasestorage.googleapis.com/v0/b/android-sticker-template-61244.appspot.com/o/ksmo.png?alt=media&token=17590bcc-587b-4aa8-a97a-5929c4cbf60f", new String [] {"ksmo","KC"}),

            new Sticker("Liberty Memorial", R.drawable.libertymemorial,
                    "https://firebasestorage.googleapis.com/v0/b/android-sticker-template-61244.appspot.com/o/LibertyMemorial.png?alt=media&token=17590bcc-587b-4aa8-a97a-5929c4cbf60f", new String [] {"liberty","memorial","KC"}),

            new Sticker("Plaza Fountains", R.drawable.plazafountains,
                    "https://firebasestorage.googleapis.com/v0/b/android-sticker-template-61244.appspot.com/o/PlazaFountains.png?alt=media&token=17590bcc-587b-4aa8-a97a-5929c4cbf60f", new String [] {"fountains","plaza","KC"}),

            new Sticker("Pylons", R.drawable.pylons_copy,
                    "https://firebasestorage.googleapis.com/v0/b/android-sticker-template-61244.appspot.com/o/Pylons%20copy.png?alt=media&token=444b18e1-0e33-40a1-8714-a83fdfc325ad", new String [] {"pylons","bartle","KC"}),

            new Sticker("Ride KC", R.drawable.ride_kansas_city,
                    "https://firebasestorage.googleapis.com/v0/b/android-sticker-template-61244.appspot.com/o/ride_kansas_city.gif?alt=media&token=17590bcc-587b-4aa8-a97a-5929c4cbf60f", new String [] {"ride","streetcar","KC"}),

            new Sticker("Rivermarket", R.drawable.rivermarket,
                    "https://firebasestorage.googleapis.com/v0/b/android-sticker-template-61244.appspot.com/o/RiverMarket.png?alt=media&token=17590bcc-587b-4aa8-a97a-5929c4cbf60f", new String [] {"river market","market","KC"}),

            new Sticker("Shuttlecock", R.drawable.shuttlecock,
                    "https://firebasestorage.googleapis.com/v0/b/android-sticker-template-61244.appspot.com/o/ShuttleCock.png?alt=media&token=17590bcc-587b-4aa8-a97a-5929c4cbf60f", new String [] {"shuttlecock","cock","nelson","KC"}),

            new Sticker("Art Museum", R.drawable.shuttlecocks_artmuseum,
                    "https://firebasestorage.googleapis.com/v0/b/android-sticker-template-61244.appspot.com/o/ShuttleCocks-ArtMuseum.png?alt=media&token=17590bcc-587b-4aa8-a97a-5929c4cbf60f", new String [] {"shuttlecock","cock","nelson","KC", "art", "museum"}),

            new Sticker("Strawberry Hill", R.drawable.strawberryhill,
                    "https://firebasestorage.googleapis.com/v0/b/android-sticker-template-61244.appspot.com/o/Strawberryhill.png?alt=media&token=17590bcc-587b-4aa8-a97a-5929c4cbf60f", new String [] {"strawberry hill","KC"}),

            new Sticker("Streetcar", R.drawable.streetcar,
                    "https://firebasestorage.googleapis.com/v0/b/android-sticker-template-61244.appspot.com/o/StreetCar.png?alt=media&token=17590bcc-587b-4aa8-a97a-5929c4cbf60f", new String [] {"streetcar","KC"}),

            new Sticker("Streetcar 2", R.drawable.streetcar_a,
                    "https://firebasestorage.googleapis.com/v0/b/android-sticker-template-61244.appspot.com/o/StreetCar_a.gif?alt=media&token=17590bcc-587b-4aa8-a97a-5929c4cbf60f", new String [] {"streetcar","KC"}),

            new Sticker("TD Travis", R.drawable.tdtravis,
                    "https://firebasestorage.googleapis.com/v0/b/android-sticker-template-61244.appspot.com/o/tdtravis.gif?alt=media&token=17590bcc-587b-4aa8-a97a-5929c4cbf60f", new String [] {"touchdown","travis","KC"}),

            new Sticker("The Rieger", R.drawable.therieger,
                    "https://firebasestorage.googleapis.com/v0/b/android-sticker-template-61244.appspot.com/o/TheRieger.png?alt=media&token=17590bcc-587b-4aa8-a97a-5929c4cbf60f", new String [] {"rieger","food","KC"}),

            new Sticker("Mahomes Goat", R.drawable.transparent_mahomes_goat,
                    "https://firebasestorage.googleapis.com/v0/b/android-sticker-template-61244.appspot.com/o/transparent_mahomes_goat.png?alt=media&token=17590bcc-587b-4aa8-a97a-5929c4cbf60f", new String [] {"beers","cheers","KC"}),

            new Sticker("Union Station", R.drawable.unionstation,
                    "https://firebasestorage.googleapis.com/v0/b/android-sticker-template-61244.appspot.com/o/unionstation.png?alt=media&token=17590bcc-587b-4aa8-a97a-5929c4cbf60f", new String [] {"union station","KC"}),

            new Sticker("Westport", R.drawable.westport,
                    "https://firebasestorage.googleapis.com/v0/b/android-sticker-template-61244.appspot.com/o/westport.png?alt=media&token=17590bcc-587b-4aa8-a97a-5929c4cbf60f", new String [] {"westport","KC"})

    };
}