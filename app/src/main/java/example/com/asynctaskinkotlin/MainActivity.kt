package example.com.asynctaskinkotlin

import android.app.ProgressDialog
import android.content.Context
import android.net.ConnectivityManager
import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.TextView
import com.squareup.okhttp.OkHttpClient
import com.squareup.okhttp.Request

class MainActivity : AppCompatActivity() {
    lateinit var txt_result: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // Declare view of textview
        txt_result = findViewById(R.id.txt_result) as TextView
        // execute method of AsyncTask
        getQuestion().execute()
    }

    internal inner class getQuestion : AsyncTask<Void, Void, String>() {
        lateinit var progressDialog: ProgressDialog //Declare ProgressDialog
        var hasInternet = false // Boolean value declare

        override fun onPreExecute() {
            super.onPreExecute()
            // initialization  ProgressDialog
            progressDialog = ProgressDialog(this@MainActivity)
            progressDialog.setMessage("Loading....")
            // Display ProgressDialog
            progressDialog.show()
        }

        override fun doInBackground(vararg p0: Void?): String {

            if (isNetworkAvilable()) { // check net available
                hasInternet = true // change value of Boolean

                val client = OkHttpClient() // declare ok http client
                val url = "https://feeds.citibikenyc.com/stations/stations.json" // Declare url
                val request = Request.Builder().url(url).build() // build the url with request
                val response = client.newCall(request).execute() //execute request
                return response.body()?.string().toString() // convert response in string
            } else {
                return ""
            }

        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)

            progressDialog.dismiss() // Dismiss progressDialog
            // check boolean value
            if (hasInternet) {  // is true
                txt_result.text = result // set result to view
            } else { // is false
                txt_result.text = "No Internet connection" // set value to view
            }
        }
    }

    // check Internet connection is ON OR OFF
    private fun isNetworkAvilable(): Boolean {
        val connectivitymanager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager // Declare connectivity manager
        val activenetworkinfo = connectivitymanager.activeNetworkInfo // check connection ON OR OFF
        return activenetworkinfo != null && activenetworkinfo.isConnected // Return connection
    }
}
