package com.example.vadim.EtsyViewer;

import android.os.AsyncTask;

public abstract class PendingTask extends AsyncTask
{
    int pendingInterval = 0;
    int pendingTrials = 0;
    boolean ready;

    public void setPendingInterval(int pendingInterval) {
        this.pendingInterval = pendingInterval;
    }

    public void setPendingTrials(int pendingTrials) {
        this.pendingTrials = pendingTrials;
    }

    public void setReady(boolean ready) {
        this.ready = ready;
    }

    @Override
    protected Object doInBackground(Object[] objects)
    {
        for(int i = 0; i <= pendingTrials; i++)
        {
            if(!ready)
            {
              try{Thread.sleep(pendingInterval);}catch(Exception e){}
              System.out.println("No answer another check!!!!!!!!!!!");
            }else break;
        }
        return null;
    }

    @Override
    protected void onPostExecute(Object o)
    {
        super.onPostExecute(o);
        System.out.println("Answer received!!!!!!!!!!!");
        run();
    }

    protected abstract void run();
}
