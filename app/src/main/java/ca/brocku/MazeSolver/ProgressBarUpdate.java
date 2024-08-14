package ca.brocku.MazeSolver;

import android.widget.ProgressBar;

public class ProgressBarUpdate implements Runnable{
    int progress;
    ProgressBar pb;

    public ProgressBarUpdate(int progress, ProgressBar pb){
        this.progress = progress;
        this.pb = pb;
    }

    @Override
    public void run(){
        pb.setProgress(progress);
    }
}
