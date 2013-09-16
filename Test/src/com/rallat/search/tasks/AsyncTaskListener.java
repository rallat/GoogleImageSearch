package com.rallat.search.tasks;

public interface AsyncTaskListener {
    public void onPreExecute();

    public void onPostExecute();

    public void doInBackground();

}
