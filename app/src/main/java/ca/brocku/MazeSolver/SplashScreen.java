package ca.brocku.MazeSolver;

/**
 * Shows a progress bar while copying internal APK files to external storage.
 */

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.ProgressBar;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SplashScreen extends AppCompatActivity {

    public final static String MAP_MMPK = "Map.mmpk";
    public final static int MAP_SIZE = 716856;
    public final static String POINTS = "points.csv";
    public final static int POINTS_SIZE = 31;
    public final static String EDGES = "edges.csv";
    public final static int EDGES_SIZE = 12;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        ExecutorService executor= Executors.newSingleThreadExecutor();
        Handler handler=new Handler(Looper.getMainLooper());

        ProgressBar pb = (ProgressBar)findViewById(R.id.progress);

        String mapFilePath = getExternalFilesDir(null) + File.separator + MAP_MMPK;
        executor.execute(() -> {
            File mapPackage = new File(mapFilePath);
            if(!mapPackage.exists()){
                System.out.println("mmpk not found. Copying...");
                File outFile = new File(mapFilePath);
                try(InputStream in = getAssets().open(MAP_MMPK); OutputStream out = new FileOutputStream(outFile)){
                    byte[] buffer = new byte[1024];
                    int read;
                    int prog = 0;
                    while((read = in.read(buffer)) != -1){
                        out.write(buffer, 0, read);
                        prog++;
                        handler.post(new ProgressBarUpdate((int)(prog*1.0/MAP_SIZE*99), pb));
                    }
                }
                catch (IOException e){
                    System.out.println("Couldn't find " + mapFilePath + " in assets");
                }
            }
        });

        String pointsFilePath = getExternalFilesDir(null) + File.separator + POINTS;
        executor.execute(() -> {
            File points = new File(pointsFilePath);
            if(!points.exists()){
                System.out.println("points not found. Copying...");
                File outFile = new File(pointsFilePath);
                try(InputStream in = getAssets().open(POINTS); OutputStream out = new FileOutputStream(outFile)){
                    byte[] buffer = new byte[1024];
                    int read;
                    while((read = in.read(buffer)) != -1){
                        out.write(buffer, 0, read);
                    }
                }
                catch (IOException e){
                    System.out.println("Couldn't find " + pointsFilePath + " in assets");
                    System.out.println(e);
                }
            }

        });

        String edgesFilePath = getExternalFilesDir(null) + File.separator + EDGES;
        executor.execute(() -> {
            File edges = new File(edgesFilePath);
            if(!edges.exists()){
                System.out.println("edges not found. Copying...");
                File outFile = new File(edgesFilePath);
                try(InputStream in = getAssets().open(EDGES); OutputStream out = new FileOutputStream(outFile)){
                    byte[] buffer = new byte[1024];
                    int read;
                    while((read = in.read(buffer)) != -1){
                        out.write(buffer, 0, read);
                    }
                }
                catch (IOException e){
                    System.out.println("Couldn't find " + edgesFilePath + " in assets");
                }
            }
            pb.setProgress(100);
            finish();
            startActivity(new Intent(this, MainActivity.class));
        });



    }
}