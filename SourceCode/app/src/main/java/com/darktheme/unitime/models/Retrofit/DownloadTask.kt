package com.darktheme.unitime.models.Retrofit

import android.app.Activity
import android.app.ProgressDialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.AsyncTask
import android.os.Environment
import android.os.Handler
import android.util.Log
import android.widget.Toast
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream
import java.net.HttpURLConnection
import java.net.URL


class DownloadTask(private val activity: Activity, downloadUrl: String, filename: String) {
    private var downloadUrl = ""
    private var downloadFileName = ""
    private var progressDialog: ProgressDialog? = null

    private inner class DownloadingTask :
        AsyncTask<Void?, Void?, Void?>() {
        var outputFile: File? = null
        override fun onPreExecute() {
            super.onPreExecute()
            progressDialog = ProgressDialog(activity)
            progressDialog!!.setMessage("Downloading...")
            progressDialog!!.setCancelable(true)
            progressDialog!!.show()
        }

        override fun onPostExecute(result: Void?) {
            super.onPostExecute(result)
            try {
                if (outputFile != null) {
                    progressDialog!!.dismiss()
                    val pdfFile = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), downloadFileName)
                    val path: Uri = FileProvider.getUriForFile(activity, activity.getApplicationContext().getPackageName() + ".provider", pdfFile);
                    val pdfIntent = Intent(Intent.ACTION_VIEW)
                    pdfIntent.setDataAndType(path, "application/pdf")
                    pdfIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    try {
                        activity.startActivity(pdfIntent)
                    } catch (e: ActivityNotFoundException) {
//                            Toast.makeText(
//                                activity,
//                                "No Application available to view PDF",
//                                Toast.LENGTH_SHORT
//                            ).show()
                        println("cant open pdf")
                    }
                } else {
                    Handler().postDelayed({ }, 3000)
                    Log.e(TAG, "Download Failed")
                }
            } catch (e: Exception) {
                e.printStackTrace()

                //Change button text if exception occurs
                Handler().postDelayed({ }, 3000)
                Log.e(
                    TAG,
                    "Download Failed with Exception - " + e.localizedMessage
                )
            }
            super.onPostExecute(result)
        }

        override fun doInBackground(vararg p0: Void?): Void? {
            try {
                val url = URL(downloadUrl) //Create Download URl
                val c =
                    url.openConnection() as HttpURLConnection //Open Url Connection
                c.requestMethod = "GET" //Set Request Method to "GET" since we are grtting data
                c.connect() //connect the URL Connection

                //If Connection response is not OK then show Logs
                if (c.responseCode != HttpURLConnection.HTTP_OK) {
                    Log.e(
                        TAG, "Server returned HTTP " + c.responseCode
                                + " " + c.responseMessage
                    )
                }

                outputFile =
                    File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), downloadFileName) //Create Output file in Main File

                //Create New File if not present
                if (!outputFile!!.exists()) {
                    outputFile!!.createNewFile()
                    Log.e(TAG, "File Created")
                }
                val fos =
                    FileOutputStream(outputFile) //Get OutputStream for NewFile Location
                val `is` = c.inputStream //Get InputStream for connection
                val buffer = ByteArray(1024) //Set buffer type
                var len1 = 0 //init length
                while (`is`.read(buffer).also { len1 = it } != -1) {
                    fos.write(buffer, 0, len1) //Write new file
                }

                //Close all connection after doing task
                fos.close()
                `is`.close()
                activity.runOnUiThread({
                    Toast.makeText(activity,"Download completed", Toast.LENGTH_SHORT).show()
                })
            } catch (e: Exception) {
                //Read exception if something went wrong
                e.printStackTrace()
                outputFile = null
                Log.e(
                    TAG,
                    "Download Error Exception " + e.message
                )
                activity.runOnUiThread({
                    Toast.makeText(activity,"Failed to download", Toast.LENGTH_SHORT).show()
                })
            }
            return null
        }
    }

    companion object {
        private const val TAG = "Download Task"
    }

    init {
        this.downloadUrl = downloadUrl
        downloadFileName = filename
        Log.e(TAG, downloadFileName)
        DownloadingTask().execute()
    }
}